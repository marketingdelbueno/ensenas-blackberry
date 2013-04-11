
package com.rim.samples.device.EnSenas;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.io.file.FileConnection;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.CoverageInfo;

//clase que implementa la interfaz Runnable
class descargar_video implements Runnable {
    
    private final String remoteName;
    private final String localName;
    private String apn,palabra_consultada;
    private FileConnection file;
    private FileConnection prueba;
    private boolean se_descargo;
    private HttpConnection conn;
    private int status;
    private Utilidades u;
    
    public descargar_video(String remoteName, String localName,String palabra_consultada) {
        this.remoteName = remoteName;
        this.localName = localName;
        this.palabra_consultada = palabra_consultada;
        
        se_descargo = false;
        apn=apn(); //Establezco el APN segun la operadora utilizada
        u=new Utilidades();
        
        try {
            file = (FileConnection) Connector.open(this.localName);//se establece una conexion con el archivo a abrir
            prueba = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/prueba"+palabra_consultada+".txt");
        } catch (IOException e) { } catch (Exception e) {}
   
    }
    public String apn(){//clase que determina el apn de la operadora que posee el celular (cantv,digitel,movilnet)
        String nombre_operadora = RadioInfo.getCurrentNetworkName();
        String apn=null;
        if(nombre_operadora=="DIGITEL GSM"||nombre_operadora=="Digitel"){
            apn="gprsweb.digitel.ve";
        }        
        if(nombre_operadora=="Movistar"){
            apn="internet.movistar.ve";
        }
        if(nombre_operadora=="Telecomunicaciones Movilnet"||nombre_operadora=="Movilnet"){
            apn="int.movilnet.com.ve";                
        }
        return apn;
    }
    public void run() {//se implementa la interfaz run como se dijo que pasaria antes
        try {
            int chunkIndex = 0;
            int totalSize = 0;
          
            //se valida si existe el archivo y que ademas esta guardado con basura(debido a algun error)
            if(  file.exists() && (file.fileSize() <= (long)10000) ){
                file.setWritable(true);
                file.setReadable(true);
                file.delete();
                prueba.setWritable(true);
                prueba.setReadable(true);
                prueba.delete();
                //se hace escribible y leible para luego ser borrado
            }
            
            if ( !file.exists() ) {

                String modoConexion;//se determina si se esta conectado a ineternet por medio de alguna red wifi o otro medio distinto
                
                if(u.isWiFi()){
                    modoConexion = ";deviceside=true;interface=wifi";//en caso de ser wifi
                }
                else{
                    modoConexion = ";deviceside=true;apn="+apn;//en caso de ser gastando los megas del plan de datos de la operadora
                }
                
                String currentFile = this.remoteName + modoConexion;
                
                conn = (HttpConnection) Connector.open(currentFile);
                status = conn.getResponseCode();
   
                if (status == HttpConnection.HTTP_OK)//si se ha establecido una conexion
                {                    
                    InputStream in = conn.openDataInputStream();                    
                    file.create();
                    file.setWritable(true);
                    prueba.create();
                    prueba.setWritable(true);
                    OutputStream out = file.openOutputStream();
                    OutputStream out_prueba = prueba.openOutputStream();
                    String disponible =  "tiempo = 0  ;  avaliable es "+in.available()+"\n\n";          
                    long start = System.currentTimeMillis();            
                    long end = System.currentTimeMillis();
                    out_prueba.write(disponible.getBytes());
                    
                    int length = -1;
                    byte[] readBlock = new byte[1024*16];
                    int fileSize = 0;
                    while ((length = in.read(readBlock)) != -1) {
                        out.write(readBlock, 0, length);
                        end = System.currentTimeMillis();
                        long tiempo = end - start;
                        //start = end ;
                        fileSize += length;
                        String len = "tiempo = "+(float)tiempo/1000+"s  tamano descargado = "+(float)fileSize/1024+"KB  tama;o del buffer  "+(float)length/1024+"KB \n";
                        out_prueba.write(len.getBytes());
                        //out_prueba.write("\n".getBytes());
                        //readBlock = new byte[102400];
                    }                                    
                    totalSize += fileSize;
                    in.close();
                    conn.close();
                    in = null;
                    conn = null;
                    out.close();
                    out_prueba.close();
                    prueba.close();                    
                    Thread.yield(); // allow other threads time
                    se_descargo=true;
                        
                }   
                          
                     
           String contar = "http://videodiccionario.thinkasoft.com/control.php?id=" + palabra_consultada + modoConexion;
           
           conn = (HttpConnection) Connector.open(contar);
           status = conn.getResponseCode();
            if (status == HttpConnection.HTTP_OK)
             {                    
              InputStream in = conn.openInputStream();                    
              int length = -1;
              byte[] readBlock = new byte[1024];
              int fileSize = 0;
              while ((length = in.read(readBlock)) != -1)
              in.close();
              conn.close();
              in = null;
              conn = null;
                 }         
              
            }
            else{
                se_descargo=true;            
            }  
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }
    
    public boolean descargado(){
        return se_descargo;
    }
        
    public boolean archivo_existe(){
      return file.exists();
        }
    
    public void borrar_file(){       
       try{
        file.delete();
        }
        catch (Exception e) {
         }
    }    
}

