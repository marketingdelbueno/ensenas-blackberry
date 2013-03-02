
package com.rim.samples.device.enSenas;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
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
                //se hace escribible y leible para luego ser borrado
            }
            
            if ( !file.exists() ) {//si no existe el archivo, que es la condicion deseada

                String modoConexion;//se determina si se esta conectado a ineternet por medio de alguna red wifi o otro medio distinto
                
                if(u.isWiFi()){
                    modoConexion = ";interface=wifi";//en caso de ser wifi
                }
                else{
                    modoConexion = ";deviceside=true;apn="+apn;//en caso de ser gastando los megas del plan de datos de la operadora
                }
                
                String currentFile = this.remoteName + modoConexion;//aqui se establece la direccion del archivo mas el tipo de conexion utilizada para la descarga
                
                conn = (HttpConnection) Connector.open(currentFile);//se abre la conexion con el link de descarga
                status = conn.getResponseCode();//se le asigna a status el un valor que indica si la conexion se establecio bien o no
   
                if (status == HttpConnection.HTTP_OK)//si se ha establecido una conexion
                {                    
                    InputStream in = conn.openInputStream();//se abre la conexion                     
                    file.create();//el archivo que ya sabemos de antemano que no existe, se crea
                    file.setWritable(true);//se hace escribible
                    OutputStream out = file.openOutputStream();//se establece a la variable "out" como la que va a imprimir en "file"
                    int length = -1;
                    byte[] readBlock = new byte[1024*64];//se lee o se descarga de 256 en 256 bytes por ves
                    int fileSize = 0;
                    while ((length = in.read(readBlock)) != -1) {//mientras no se haya leido todo
                        out.write(readBlock, 0, length);
                        fileSize += length;
                    }
                    //de aqui en adelante se recaudan datos y se cierran conexiones
                    totalSize += fileSize;
                    in.close();
                    conn.close();
                    in = null;
                    conn = null;
                    Thread.yield(); // allow other threads time
                    out.close();
                    file.close();                    
                    se_descargo=true;                        
                }
                          
           //la proxima linea se usa para contar la cantidad de veces que este archivo que se acaba de descargar es descargado,
           //esto con fines de lograr hacer el modulo de "mas vistos" 
           String contar = "http://videodiccionario.thinkasoft.com/control.php?id=" + palabra_consultada + modoConexion;
           
           conn = (HttpConnection) Connector.open(contar);//como antes se establece una conexion con el link en la variable contar
           status = conn.getResponseCode();//se obtiene el status de la conexion, fallida o lograda
            if (status == HttpConnection.HTTP_OK)//si se logro una conexion
             {                    
             //se hace la lectura de los datos de control que tiene la cantidad de veces que se ha descargado una palabra
              InputStream in = conn.openInputStream();                    
              int length = -1;
              byte[] readBlock = new byte[1024*64];
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
    
    public boolean descargado(){//retorna el valor de se_descargo true o false
        return se_descargo;
    }
        
    public boolean archivo_existe(){//retorna el valor de file.exists() true o false
      return file.exists();
        }
    
    public void borrar_file(){//borra un archivo arroja excepciones en caso de este no existir
       try{
        file.delete();
        }
        catch (Exception e) {
         }
    }    
}

