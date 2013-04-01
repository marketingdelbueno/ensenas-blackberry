package com.rim.samples.device.EnSenas;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.Field;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField; 
import javax.microedition.media.control.*;
import javax.microedition.media.*;
import net.rim.device.api.ui.Graphics;
import java.io.InputStream;
import net.rim.device.api.ui.component.Dialog;
import java.io.IOException;

//Se muestra la imagen de inicio
public class Inicio extends MainScreen{
  
  NeoEssentia _app;
     Player player;
     VideoControl videoControl;
     Field videoField;
  
  public Inicio(NeoEssentia app){
        
            Utilidades u = new Utilidades();
            this.setBanner( u.inicio() ); //Se obtiene la imagen de Utildades y se ubica como banner
            _app = app;
            
            /*Se crea un hilo que espera 1,5 segundos mostrar el menu principal,
            en esos 1,5 segundos el usuario visualiza la imagen de bienvenida
            */
            UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                    ver_propaganda();
                }
            },1500,false );
  }
  
  //Inicio de menu prinicipal       
  public void menu_principal(){
           NeoEssentiaScreen screen = new NeoEssentiaScreen(_app); 
            _app.pushScreen(screen);
        }      

  public void ver_propaganda(){
      
         VideoPlay vi = new VideoPlay("/videos/propaganda.mp4");
         this.setBanner(vi.field());
         
         try{
            vi.video().start();
         }
          catch(MediaException me)
          {
            Dialog.alert(me.toString());
          }
         long duracionVideo = vi.duracionVideo();
         UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                    menu_principal();
                }
            },duracionVideo/1000,false );      
      }
}
