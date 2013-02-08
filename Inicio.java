package com.rim.samples.device.enSenas;
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
//muestra un video de 7 seg al inicio despues de verse la imagen de ensenas
  public void ver_propaganda(){
      
         VideoPlay vi = new VideoPlay("/videos/propaganda.mp4");//se llama a la clase VideoPlay creada en este codigo fuente la cual se encarga de reproducir el video
         //hay que explicar algo, durante el desarrollo del proyecto, se entendio por banner la zona inferior del dispositivo donde va el "banner publicitario"
         //y se programo pensando asi, pero para la api de blackberry el banner es la zona superior de la pantalla donde se puso el logo de ensenas
         //y lo que nosotros llamamos banner en nuestro codigo, es asignado en lo que para blackberry es this.setStatus();
         
         this.setBanner(vi.field());//en el banner se pone el campo del video que ocupa toda la pantalla
         try{
            vi.video().start();//play al video, o mejor dicho "start"
         }
          catch(MediaException me)
          {
            Dialog.alert(me.toString());//se maneja la excepcion
          }
         //estas lineas son meramente importantes se usan mucho a lo largo del codigo, esta es la forma en que java maneja eventos, por medio de 
         //hilos que se ejecutan en medio de una situacion peculiar
         UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                    menu_principal();
                }
            },7700,false );      // esto quiere decir que a los 7700 milisegundos o 7,7 segundos se ejecuta el metodo menu_principal()
      }
}
