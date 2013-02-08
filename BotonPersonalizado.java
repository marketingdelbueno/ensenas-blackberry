package com.rim.samples.device.enSenas;

import com.rim.samples.device.enSenas.resizeBitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.LabelField;

//clase que crea botones con ciertas caracteristicas especiales como dibujo del boton foco personalizado etc
public class BotonPersonalizado extends Field
{
    private Bitmap _currentPicture;
    private String extension,nombre,play_o_error;
    private NeoEssentiaScreen main;
    private boolean seleccionado;
  
  public BotonPersonalizado(String name,NeoEssentiaScreen menu,String ext, long style, Bitmap imagen, int ancho_pantalla, int alto_pantalla,String play_o_error) 
  {
      //se inicialza todo
        super(style);//le asigna el estilo al boton (centrado, de un lado, etc)
        seleccionado = false;
        extension = ext;
        nombre = name;
        main = menu;
        _currentPicture = imagen;       
        this.play_o_error=play_o_error;
   }
    
    //Si se hace focus sobre el boton
  protected void onFocus(int direction) 
    {
        seleccionado = true;
        invalidate();
    }

    //Si se quita se quita el foco del boton
    protected void onUnfocus() 
    {
        seleccionado = false;
        if(nombre!=null && main!=null){
            main.set_estado("Buscar...");
        }
        invalidate();
    }
    
    //Nos dice si el boton tiene el foco actualmente
   public boolean Seleccionado(){
        return seleccionado;
   }
   
   //Dibuja el boton cuando tiene el foco
    protected void drawFocus(Graphics graphics, boolean on) 
    {
        //Si el boton es el play o error se dibuja otro foco distinto al resto
       if(play_o_error!=null){
            if(play_o_error.equals("play")){
                Bitmap g = Bitmap.getBitmapResource("playfocus"+extension+".png" );
                graphics.drawBitmap(0, 0,  g.getWidth(), g.getHeight(),g, 0, 0);
            }
            else{
                Bitmap g = Bitmap.getBitmapResource("errorfocus"+extension+".png" );
                graphics.drawBitmap(0, 0,  g.getWidth(), g.getHeight(),g, 0, 0);
            }                        
            invalidate();
        }
        else //Dibuja un foco en forma de cuadro alrededor del boton
        {
            Bitmap g = Bitmap.getBitmapResource("focus"+extension+".png" );    
            graphics.drawBitmap(0, 0,  g.getWidth(), g.getHeight(),g, 0, 0);        
            if(nombre!=null && main!=null){
                main.set_estado(nombre);
            }
            invalidate();//Se refresca el Display
        }
    }
    
    protected void layout(int width, int height) 
    {
        setExtent(Math.min( width, _currentPicture.getWidth() ), 
        Math.min( height, _currentPicture.getHeight() ));
    }

   protected void paint(Graphics graphics) 
    {
        graphics.drawBitmap(0, 0,  _currentPicture.getWidth(), _currentPicture.getHeight(), _currentPicture, 0, 0);
     }
    
    protected boolean navigationClick(int status, int time) 
    {
        fieldChangeNotify(1);
        return true;
    }
    
}




