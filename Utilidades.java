package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import javax.microedition.io.*;
import java.io.*;
import javax.microedition.io.file.FileSystemRegistry;
import java.util.*;
import net.rim.device.api.ui.component.LabelField;

//Nos proporciona metodos que pueden ser usados para distintos problemas
class Utilidades {
    
    //Constructor
    Utilidades() {    }
    
    //Elimina caracteres especiales a una cadena
    public String quitar_carac_espec( String input ){
         
           String original = new String("áàéèíìóòúùÁÀÉÈÍÌÓÒÚÙ ?");
           String ascii = new String("aaeeiioouuaaeeiioouu_-");
           String output = input.toLowerCase();
           for (int i=0; i<original.length(); i++) {
               output = output.replace(original.charAt(i), ascii.charAt(i));
           }
           String nuevo = new String();
           for (int i=0; i<output.length(); i++) {
               if(output.charAt(i) == 'ñ'){
                   nuevo = nuevo + "ny";
               }
               else{
                   nuevo = nuevo + output.charAt(i);
               }
           }    
           return nuevo;
    }
    
    //Crea un field con el titulo del menu Ensenas     
    public VerticalFieldManager titulo(){
             
         VerticalFieldManager vert = new VerticalFieldManager(VerticalFieldManager.FIELD_HCENTER){
            //Se establece el color de fondo
            public void paint(Graphics graphics)
            {
                graphics.clear();
                graphics.setColor( 0xFFFFFF );
                graphics.fillRect( 0, 0, Display.getWidth(), Display.getHeight()*80 );
                invalidate();
                super.paint(graphics);
            }                
         };
         
         BitmapField CampoImagenEnsenas;
         Bitmap imagen_ensenas;
         //Si la resolucion de la pantalla del BB es conocida, se muestra la imagen adecuada...
         if( String.valueOf(Graphics.getScreenWidth()).equals("240") ||
             String.valueOf(Graphics.getScreenWidth()).equals("320") ||
             String.valueOf(Graphics.getScreenWidth()).equals("360") ||
             String.valueOf(Graphics.getScreenWidth()).equals("480") ||
             String.valueOf(Graphics.getScreenWidth()).equals("640")
            ){
             imagen_ensenas = Bitmap.getBitmapResource("ensenas"+String.valueOf(Graphics.getScreenWidth())+".png");
             CampoImagenEnsenas = new BitmapField(imagen_ensenas, Field.NON_FOCUSABLE );
             }
         else{//...si no, se muestra una imagen redimensionada
             imagen_ensenas = Bitmap.getBitmapResource("ensenas640.png");
             resizeBitmap imagen_cambiada= new resizeBitmap(imagen_ensenas, Graphics.getScreenWidth() , (int)(Graphics.getScreenHeight()*0.1658));//1658        
             CampoImagenEnsenas = new BitmapField(imagen_cambiada.get_bitmap(), Field.NON_FOCUSABLE );
          }
               
             vert.add(CampoImagenEnsenas);
             return vert;
     }
    
    
    //Se crea imagen de bienvenida a la aplicacion
     public BitmapField inicio(){
         Bitmap InicioBitmap;
         BitmapField InicioField;
            //Si la resolucion del BB es conocida, se usa una imagen adecuada...
            if( String.valueOf(Graphics.getScreenWidth()).equals("240") ||
                String.valueOf(Graphics.getScreenWidth()).equals("320") ||
                String.valueOf(Graphics.getScreenWidth()).equals("360") ||
                String.valueOf(Graphics.getScreenWidth()).equals("480") ||
                String.valueOf(Graphics.getScreenWidth()).equals("640")
            ){
                InicioBitmap = Bitmap.getBitmapResource("inicio/"+String.valueOf(Graphics.getScreenWidth())+"x"+String.valueOf(Graphics.getScreenHeight())+".jpg");
                InicioField = new BitmapField( InicioBitmap, Field.NON_FOCUSABLE );
            }
            else //... si no, se redimensiona la imagen.
            {
                InicioBitmap = Bitmap.getBitmapResource("inicio/640x480.jpg");
                resizeBitmap imagen_cambiada= new resizeBitmap(InicioBitmap, Graphics.getScreenWidth() , Graphics.getScreenHeight());
                InicioField = new BitmapField( imagen_cambiada.get_bitmap() , Field.NON_FOCUSABLE );
            }
         return InicioField;
      }
      
      // Se crea un field con el fondo del menu principal                  
      public VerticalFieldManager fondo(boolean Expandir){
          VerticalFieldManager fondo;
       if(  Expandir == false){   
            fondo = new VerticalFieldManager(
                VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.FIELD_HCENTER){
                //Se cambia el color de fondo del menu principal 
                public void paint(Graphics graphics)
                {
                    graphics.clear();
                    graphics.setColor( 0xFFFFFF );
                    graphics.fillRect( 0, 0, Display.getWidth(), Display.getHeight()*80 );
                    invalidate();
                    super.paint(graphics);
                }
            };
        }
        else{
            
            fondo = new VerticalFieldManager( VerticalFieldManager.USE_ALL_HEIGHT |
            VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.FIELD_HCENTER){
            //Se cambia el color de fondo del menu principal 
            public void paint(Graphics graphics)
            {
                graphics.clear();
                graphics.setColor( 0xFFFFFF );
                graphics.fillRect( 0, 0, Display.getWidth(), Display.getHeight()*80 );
                invalidate();
                super.paint(graphics);
                }
            };

            
            }
            return fondo;    
       }
    
    public VerticalFieldManager fondo(final String tam){
        VerticalFieldManager fondo;
        fondo = new VerticalFieldManager( VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.FIELD_HCENTER ){
        //Se cambia el color de fondo del menu principal                         
            Bitmap picture =  Bitmap.getBitmapResource("thinkasoft.png");
            int he=(Graphics.getScreenHeight()-banner().getBitmapHeight()-picture.getHeight())/2;           
            public void paint(Graphics graphics){ 
                graphics.setColor(0xffffff);
                super.paint(graphics); 
            } 
            //Se establece la forma del campo de busqueda con una imagen    
            public void paintBackground(Graphics graphics) {           
                int wi=0;
                graphics.setColor(0xffffff);
                graphics.drawBitmap(wi ,he , picture.getWidth(), picture.getHeight(), picture, 0, 0);                   
            }            
        };        
        
        return fondo;    
    }
        //Se crea el campo de busqueda del menu principal
     public HorizontalFieldManager buscarCampo(){
            HorizontalFieldManager BuscarCampo=new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER | HorizontalFieldManager.USE_ALL_WIDTH | HorizontalFieldManager.FIELD_HCENTER ){
                int anch = Graphics.getScreenWidth();
                int alt = Graphics.getScreenHeight();
                Bitmap picture= Bitmap.getBitmapResource("cuadro"+String.valueOf(anch)+"x"+String.valueOf(alt)+".png");
                int ancho= picture.getWidth();
                int alto= picture.getHeight();
                public int getPreferredWidth() 
                {
                    return (ancho);
                }                
                public int getPreferredHeight() 
                {
                    return (alto);
                }
                public void paint(Graphics graphics){ 
                    graphics.setColor(0x000000);
                    super.paint(graphics); 
                } 
                //Se establece la forma del campo de busqueda con una imagen    
                public void paintBackground(Graphics graphics) { 
                    graphics.setColor(0xffffff);
                    graphics.drawBitmap(0, 0,  picture.getWidth(), picture.getHeight(), picture, 0, 0);                    
                }                
            };        
        BuscarCampo.add(new LabelField("       "));
        return BuscarCampo;            
    }
      //Se crea el field con el banner de publicidad
      public AnimatedGIFField banner(){
        AnimatedGIFField banner;
        //Si la resolucion del BB es conocida se muestra una imagen adecuada...
        if( String.valueOf(Graphics.getScreenWidth()).equals("240") ||
            String.valueOf(Graphics.getScreenWidth()).equals("320") ||
            String.valueOf(Graphics.getScreenWidth()).equals("360") ||
            String.valueOf(Graphics.getScreenWidth()).equals("480") ||
            String.valueOf(Graphics.getScreenWidth()).equals("640")
        ){
            GIFEncodedImage ourAnimation = (GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("logo_240x35.gif");
            banner = new AnimatedGIFField(ourAnimation, Field.FIELD_HCENTER);
        }
        else //si no, se redimensiona la imagen
        {
            GIFEncodedImage ourAnimation = (GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("cargando.gif");
            banner = new AnimatedGIFField(ourAnimation, Field.FIELD_HCENTER);
        }
        return banner;
     }
     
     public void alerta(final String text){
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                Dialog.alert(text);
            }
        });    
     }
                  
      //Se comprueba que el BB tenga una tarjeta de memoria.      
      public boolean existe_microsd(){
                String root = null;
                boolean sdcard = false;
                Enumeration e = FileSystemRegistry.listRoots();
                while (e.hasMoreElements() && !sdcard) {
                    root = (String) e.nextElement();
                    if( root.equalsIgnoreCase("sdcard/") ) {
                        sdcard = true;
                    }
                }
            return sdcard;
       }
      
      //Se comprueba que este disponible la conexion  
      public boolean isWiFi()
      {
            boolean wifiOn=(RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN)!=0;
            boolean wifiConnected= (WLANInfo.getWLANState()==WLANInfo.WLAN_STATE_CONNECTED);
            return (wifiOn & wifiConnected);
      }
} 
