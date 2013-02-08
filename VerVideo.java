package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.KeywordFilterField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.BitmapField;
import javax.microedition.io.file.FileConnection;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.Dialog;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Characters;
import javax.microedition.media.control.*;
import net.rim.device.api.io.LineReader;
import net.rim.device.api.system.Bitmap;
import javax.microedition.io.Connector;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Field;
import javax.microedition.media.*;
import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.HttpConnection;
 public class VerVideo extends MainScreen implements FieldChangeListener
    {      
    BotonPersonalizado twitter,facebook,catalogo,masvisto,credito,dicci,descargar,repetir,videoBoton,play,error;
    HorizontalFieldManager mostrar_video, mostrar,botones, buscar_manager,problema,espaciador1;
    Hashtable palabras_hash_video,palabras_hash;
    VerticalFieldManager imagen,fondo;
    Bitmap imagen_ensenas;
    KeywordFilterField campofiltro;
    descargar_video descargador;
    String vid,palabra,pala_sin;
    BasicEditField buscarCampo;
    ListaPalabra listapalabra;
    AnimatedGIFField f;
    boolean ya_cargo;
    NeoEssentia _app;
    Utilidades u;
    VideoPlay vi;
    Player player;
    int tam_icono,tam_imag;
    
           String extension;
       public VerVideo(String p,String v,NeoEssentia app, Hashtable p_h, Hashtable p_h_v, ListaPalabra l,descargar_video d)
        {            
            _app=app;
            u = new Utilidades();
             tam_icono=0;
            tam_imag=0;
            palabras_hash_video = p_h_v;
            palabras_hash = p_h;
            listapalabra = l;
            vid = v;
            palabra = p;
            pala_sin = u.quitar_carac_espec(palabra);
            descargador = d;
            ya_cargo = true;
            player = null;
            
            cargarbotones();
            fondo.add(imagen);
            fondo.add(buscar_manager);
          
            mostrar = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
            mostrar_video = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
            problema = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
             espaciador1=new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
           
            problema.add(error);
          
            mostrar_video.add( play );
            
            fondo.add(mostrar_video);
            fondo.add(botones);

            int altura_espacio=(int)(Graphics.getScreenHeight()-(tam_icono)-u.titulo().getPreferredHeight()-tam_imag);
            if(altura_espacio>0){
                Bitmap esp = Bitmap.getBitmapResource("espacio.png");
                resizeBitmap resize = new resizeBitmap(esp,esp.getWidth(),(int)(altura_espacio));       
                Bitmap espacio= resize.get_bitmap();
                espaciador1.add(new BitmapField( espacio , Field.NON_FOCUSABLE ));
                fondo.add(espaciador1);
            }
            add(fondo);
            PleaseWaitPopupScreen.showScreenAndWait(d,palabra,this);
           
         }
         
         public boolean cargado(){
             return ya_cargo;
             }
         
         public void ver(){

           if( u.existe_microsd() ){
           if( descargador.descargado() || descargador.archivo_existe() ){
                
                if(ya_cargo){
                    vi = new VideoPlay(vid);
                    player = vi.video();
                    ya_cargo=false;
                }
               
                try{
                    player.start();
                }
                catch(MediaException me)
                {
                    Dialog.alert(me.toString());
                }
                 _app.pushScreen(vi);
               
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        while(player.getState() == Player.STARTED ){}
                        vi.close();
                    }
                }, 1000 ,false );
              
            }
            else{
                Dialog.alert("Existen Problemas de Conexión ");
               
               if( ya_cargo ){ 
                    fondo.replace(mostrar_video,problema);
                    fondo.invalidate();
                    descargador.borrar_file();
                    ya_cargo = false;
                }
             }
            }
              else{
                  if( ya_cargo ){ 
                    fondo.replace(mostrar_video,problema);
                    fondo.invalidate();
                    ya_cargo = false;
                    }
                  Dialog.alert("Para continuar necesita insertar una tarjeta MicroSD");
                  }
        }
        
        public void cargarbotones()
        {
            imagen = u.titulo();
            fondo = u.fondo(false);
           
           
            if( (int)Graphics.getScreenWidth() <= (int)320 ){
                extension = new String("240");
                tam_icono=70;
                tam_imag=119;
                }    
            else if( (int)Graphics.getScreenWidth() > (int)320 && (int)Graphics.getScreenWidth() <= (int)480 ){
                extension = new String("300");
                tam_icono=90;
                tam_imag=153;
                }
            else{
                extension = new String("480");
                tam_icono=128;
                tam_imag=217;
                }
            
            buscar_manager = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
           
            if( (int)Graphics.getScreenWidth() <= (int)320 ){
                extension = new String("240");
            }    
            else if( (int)Graphics.getScreenWidth() > (int)320 && (int)Graphics.getScreenWidth() <= (int)480 ){
                extension = new String("300");
            }
            else{
                extension = new String("480");
            }
           
            facebook = new BotonPersonalizado(null,null,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("facebook"+ extension + "v.png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
            twitter = new BotonPersonalizado(null,null,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("twitter"+ extension + "v.png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
            descargar = new BotonPersonalizado(null,null,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("descargar"+ extension + "v.png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
          
            play = new BotonPersonalizado(null,null,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("play"+ extension + ".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),"play");
            error = new BotonPersonalizado(null,null,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("error"+ extension + ".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),"error");
           
            twitter.setChangeListener(this);
            facebook.setChangeListener(this);
            descargar.setChangeListener(this);
          play.setChangeListener(this);
          error.setChangeListener(this);
          
            botones = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
            botones.add(facebook);botones.add(twitter);
            botones.add(descargar);
       }

        //Permite detectar un evento en el teclado
        protected boolean keyChar(char key, int status, int time)
        {
            //Si se pulsa el boton de escape
            if( key == Characters.ESCAPE &&  player != null ){
                if( player.getState() != Player.CLOSED )
                   player.close(); //Se cierra el hilo del Player si se encuentra abierto
            }
            //Si se pulsa el boton de Enter
            else if( key == Characters.ENTER ){
                //Se ejecuta la accion del boton que este seleccionado
                if( play.Seleccionado() || error.Seleccionado()) 
                    ver();
                else if ( twitter.Seleccionado() )  
                    comentar_twitter();
                else if (descargar.Seleccionado() )
                    Browser.getDefaultSession().displayPage( (palabras_hash_video.get(pala_sin) ).toString() );
                else if (facebook.Seleccionado() )
                    comentar_facebook();
                return true; //Se retorna verdad para finalizar la accion
            }
            return super.keyChar(key, status, time); //Retorna las acciones que hace el teclado normalmente
       }

    public boolean invokeAction(int action)
    {        
       switch(action)
        {
            case ACTION_INVOKE: //Trackball click.
                if( play.Seleccionado() || error.Seleccionado()) ver();break;
            default:    
                return  super.invokeAction(action);
        };   
        return true; 
    }   
    
    public void comentar_twitter(){
        new ComentarTwitter( palabra );
        }
   
    public void comentar_facebook(){    
        new ComentarFacebook(palabra);
    }
    
    //Metodo que se activa cuando se presiona un boton
    public void fieldChanged(Field field, int context)
    {
        if( field == twitter){
                comentar_twitter();
            }
        else if( field == facebook){
                comentar_facebook(); 
            }
        else if(descargar ==field){
                //Se llama a la pagina que contiene el video mp4
                Browser.getDefaultSession().displayPage( (palabras_hash_video.get(pala_sin) ).toString() );
            }
        else if( play == field || error ==field )
                ver();
    }
    //Elimina el menu de guardado al salir del mainScreen
    public boolean onSavePrompt(){
            return true;
        }   
}
