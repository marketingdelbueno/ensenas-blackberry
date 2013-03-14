package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import java.io.InputStream;
import net.rim.device.api.ui.Screen;
import java.util.Vector;
import net.rim.device.api.io.LineReader;
import java.io.EOFException;
import java.io.IOException;
import net.rim.device.api.ui.component.KeywordFilterField;
import net.rim.device.api.system.EncodedImage;
import javax.microedition.media.control.*;
import javax.microedition.media.*;
import net.rim.device.api.ui.Graphics;
import java.util.Hashtable;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.component.LabelField;

   public class NeoEssentiaScreen extends MainScreen implements FieldChangeListener
{
    private HorizontalFieldManager buscarCamp;
    private BasicEditField BuscarCampo;
    private BotonPersonalizado twitter,facebook,catalogo,masvisto,info,dicci;
    private Vector palabras;
    private ListaPalabra listapalabra;
    private Hashtable palabras_hash_video,palabras_hash;
    private HorizontalFieldManager hor, hor1, hor2, hor3,espaciador1;
    private VerticalFieldManager vert,hor_vid;
    private Bitmap imagen_ensenas;
    private BitmapField CampoImagenEnsenas;    
    private NeoEssentia _app;    
    private Vector categorias;
    private Utilidades u;
    private VerticalFieldManager fondo;
    private String nombre_icono;
    private int tam_icono;
    private RichTextField estado;
    
    public NeoEssentiaScreen(NeoEssentia app)
    {        
        _app = app;
          
        try {
           FileConnection temporal = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/");
             if (!temporal.exists()) {
                temporal.mkdir();
             }
           temporal.close();
        }catch (IOException ex) {  }
                
        u = new Utilidades();
        hor = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
        hor_vid = new VerticalFieldManager(VerticalFieldManager.FIELD_HCENTER ){
            public void paint(Graphics graphics)
            {
                graphics.clear();
            graphics.setColor( 0xFFFFFF );
            graphics.fillRect( 0, 0,  Display.getWidth(),  Display.getHeight() );
            graphics.setColor( Color.BLACK );
                
                invalidate();
                super.paint(graphics);
            }
        };
        hor2 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
        hor3 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
        espaciador1 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
       
        palabras_hash_video = new Hashtable();
        palabras_hash = new Hashtable();
        palabras = leer_categoria();
       
        listapalabra = new ListaPalabra(palabras);        
        BuscarCampo= new BasicEditField("",null);
        buscarCamp = u.buscarCampo();
        HorizontalFieldManager aux = new HorizontalFieldManager ( HorizontalFieldManager.HORIZONTAL_SCROLL){
            protected void sublayout(int maxWidth, int maxHeight) {                
                int ancho=(int)(buscarCamp.getPreferredWidth()*0.80);
                //u.alerta(String.valueOf(buscarCamp.getPreferredWidth()));
                int alto=buscarCamp.getPreferredHeight();
                super.sublayout(ancho, alto);
                setExtent(ancho, alto);
            }
        };
        aux.add(BuscarCampo);
        buscarCamp.add(aux);
        String extension = new String("");       
        if( (int)Graphics.getScreenWidth() <= (int)320 ){
            extension = new String("240");
            tam_icono = 70;
            }    
        else if( (int)Graphics.getScreenWidth() > (int)320 && (int)Graphics.getScreenWidth() <= (int)480 ){
            extension = new String("300");
            tam_icono = 90;
            }
        else{
            extension = new String("480");
            tam_icono = 128;
            }
     
        catalogo = new BotonPersonalizado("Catálogo",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("catalogo"+ extension +".png" ),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        masvisto = new BotonPersonalizado("Más Vistos",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("vistos"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        info = new BotonPersonalizado("Ayuda e Información",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "info"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        facebook = new BotonPersonalizado("Facebook",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "facebook"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        twitter = new BotonPersonalizado("Twitter",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "twitter"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        dicci = new BotonPersonalizado("Buscar A-Z",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "book"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
      
        twitter.setChangeListener(this);
        facebook.setChangeListener(this);
        catalogo.setChangeListener(this);
        info.setChangeListener(this);
        dicci.setChangeListener(this);
        masvisto.setChangeListener(this);        
        VerticalFieldManager vert = u.titulo();        
        fondo = u.fondo(false);        
        /*int ancho_espacio=(int)((Graphics.getScreenWidth()-buscarCamp.getPreferredWidth())/16);
        Bitmap espa = Bitmap.getBitmapResource("espacio.png");
        resizeBitmap resize2 = new resizeBitmap(espa,ancho_espacio,1);        
        hor.add( (new BitmapField( resize2.get_bitmap() , Field.NON_FOCUSABLE )) );
        */
        hor.add(buscarCamp);
        vert.add(hor);
        this.setBanner(vert);
        
        hor2.add(catalogo);hor2.add(dicci);hor2.add(masvisto);
        hor3.add(info);hor3.add(facebook);hor3.add(twitter);
        fondo.add(hor2);
        fondo.add(hor3);
        add(fondo);
        
       int altura_espacio=(int)(Graphics.getScreenHeight()-(tam_icono*2)-u.banner().getBitmapHeight()-u.titulo().getPreferredHeight()-hor.getPreferredHeight());
       if(altura_espacio>0){
           Bitmap esp = Bitmap.getBitmapResource("espacio.png");
           resizeBitmap resize = new resizeBitmap(esp,esp.getWidth(),(int)(altura_espacio));      
           espaciador1.add(new BitmapField( resize.get_bitmap() , Field.NON_FOCUSABLE ));
           fondo.add(espaciador1);
       }
        
        estado = new RichTextField("Buscar...",Field.NON_FOCUSABLE);
        hor_vid.add( estado );
        hor_vid.add( u.banner() );
        this.setStatus( hor_vid );
    }
    
    public void set_estado(String e){
        estado.setText(e);
        this.invalidate();
    }
 
    
      protected boolean keyChar(char key, int status, int time)
        {
           if (key == Characters.ENTER )
            {
           
           if(  BuscarCampo.isFocus()){
                IrVideo();
               
            }
           else if(catalogo.Seleccionado() )
            {
                VerCategoria infoScreen = new VerCategoria(categorias,palabras_hash,palabras_hash_video,_app,listapalabra);
                _app.pushScreen(infoScreen); 
               
            }
            else if(masvisto.Seleccionado() ){
                new VerVisto();
               
                }
            else if( info.Seleccionado() ){
                 VerInfo infoScreen = new  VerInfo();
                _app.pushScreen(infoScreen);
                
                }
            else if( facebook.Seleccionado() ){
                new VerFacebook();
                
                }
            else if( twitter.Seleccionado() ){
                new VerTwitter();
               
                }
            else if( dicci.Seleccionado() ){
                 VerDiccionario infoScreen = new VerDiccionario(listapalabra,_app, palabras_hash,palabras_hash_video);
                _app.pushScreen(infoScreen);
               
                }
                return true;
            }
                return super.keyChar(key, status, time);
            
       }
    
    public boolean invokeAction(int action)
    {        
       switch(action)
        {
            case ACTION_INVOKE: // Trackball click.
                if( BuscarCampo.isFocus() ){
                    IrVideo();
                    return true;
                    }
        }    
        return  super.invokeAction(action);
    }   
    
    
    public  Vector leer_categoria(){    
        InputStream stream = getClass().getResourceAsStream("/Data/palabras.txt");
        LineReader lineReader = new LineReader(stream);                               
        Integer num_cat,num_pal;
        String num,categoria,sub_categoria,palabrita,line,pala_sin;
        int i,j,k,comma;
        Vector palabras = new Vector();
        categorias = new Vector();    
        try
        {        
            num = new String( lineReader.readLine() );
            num_cat = Integer.valueOf( num );
             
            for(i=0 ; i< num_cat.intValue() ; i++){
               categoria = new String(lineReader.readLine());
               int num_sub = Integer.parseInt( new String(lineReader.readLine()) );
               Vector sub_palabras = new Vector();
                   
                for(j=0 ; j < num_sub ; j++){
                    line = new String(lineReader.readLine());
                    comma = line.indexOf(',');
                    
                        if( comma == -1 ){
                            pala_sin = u.quitar_carac_espec( line );
                            palabras.addElement( new Palabra(line) );
                            sub_palabras.addElement( new Palabra(line) );
                            palabras_hash.put(pala_sin, line);
                            palabras_hash_video.put( pala_sin ,"");
                        }
                        else{
                            String pala = line.substring(0,comma);
                            pala_sin = u.quitar_carac_espec( pala );
                            String video = line.substring(comma+1,line.length());
                            palabras.addElement( new Palabra(pala) );
                            sub_palabras.addElement( new Palabra(pala) );
                            palabras_hash.put(pala_sin, pala);
                            palabras_hash_video.put( pala_sin ,video);
                        }
                 }                
                 Pila_Subcategoria c = new Pila_Subcategoria(categoria,sub_palabras);
                 categorias.addElement( c );
            }
        }
        catch(EOFException eof)
        {
            System.out.println("Error leyendo los datos");
        }
        catch(IOException ioe)
        {
        System.out.println("Error leyendo los datos");
                    
        } 
        return palabras;
    }

    public void fieldChanged(Field field, int context)
    {
        
        if( field == twitter ){
                new VerTwitter();
            
        }
        else if( field == facebook ){
                new VerFacebook();
            
        }
        else if(field == catalogo){
                VerCategoria infoScreen = new VerCategoria(categorias,palabras_hash,palabras_hash_video,_app,listapalabra);
                _app.pushScreen(infoScreen); 
                    
        }
        else if(field == info){
                VerInfo infoScreen = new  VerInfo();
                _app.pushScreen(infoScreen);             
            
        }
        else if(field == dicci){
                VerDiccionario infoScreen = new VerDiccionario(listapalabra,_app, palabras_hash,palabras_hash_video);
                _app.pushScreen(infoScreen);             
            
        }
        else if(field == masvisto){
                new VerVisto();
                
        }
    }
   
   public void IrVideo(){
       
               String buscar = u.quitar_carac_espec( BuscarCampo.getText().toString() );                      
               if( palabras_hash.containsKey( buscar ) == true ){
                   
                   String pala_correcta = ( palabras_hash.get(buscar) ).toString();                    

                   final String localName;
                   final String remoteName =( palabras_hash_video.get(buscar) ).toString();
                   
                   if(remoteName.equals("")){
                     Dialog.alert("El video no está en la base de datos");  
                       }
                   else{    
                   descargar_video descargador;
                   localName = "file:///SDCard/BlackBerry/temporalNeoEssentia/"+buscar+".mp4";
                   descargador = new descargar_video(remoteName,localName,buscar);
                        
                   VerVideo infoScreen = new VerVideo( pala_correcta , localName ,_app,palabras_hash,palabras_hash_video,listapalabra,descargador );
                   BuscarCampo.setText("");
                   _app.pushScreen(infoScreen);
                    }
                   }
                else{
                    Dialog.alert("No se encontró la palabra");
                    }
       
       }
   
    public boolean onClose()
    {
                try {
            FileConnection file = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/");
             
             if (file.exists()) {
                   while(file.list().hasMoreElements()){
                        FileConnection temp = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/"+file.list().nextElement().toString());
                        temp.delete();
                        temp.close();
                   }
                   file.delete();
             }
             file.close();
        }catch (IOException ex) {  }
        Dialog.alert("Vuelve pronto");
        System.exit(0);

        return true;
    }
}


