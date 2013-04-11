package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.Field;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField; 
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Color;
import net.rim.device.api.io.LineReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import net.rim.device.api.ui.component.NullField;

   public class VerInfo extends MainScreen
    {
  
   public VerInfo()
    {  
        Utilidades u = new Utilidades();
        this.setBanner(u.titulo());
        VerticalFieldManager vert = u.fondo(extension());
        VerticalFieldManager labels = new VerticalFieldManager( VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR){
            Utilidades u = new Utilidades();
            int height=(Graphics.getScreenHeight()-u.banner().getBitmapHeight());
            int width=Graphics.getScreenWidth();
            protected void sublayout(int maxWidth, int maxHeight) {                
                super.sublayout(width, height);
                setExtent(width, height);
            }           
        };
        leer_data(labels);        
        vert.add(labels);
        add(vert);
    }  
    public String extension(){
        String extension = new String("");
        if( (int)Graphics.getScreenWidth() <= (int)320 ){
            extension = new String("240");
        }    
        else if( (int)Graphics.getScreenWidth() > (int)320 && (int)Graphics.getScreenWidth() <= (int)480 ){
            extension = new String("300");
        }
        else{
            extension = new String("480");
        }
        return extension;
    }
    public void leer_data(VerticalFieldManager lab){    
        VerticalFieldManager labels =lab;
        InputStream stream = getClass().getResourceAsStream("/info/info.txt");
        LineReader lineReader = new LineReader(stream);                               
        Integer num_cat,num_sub,num_pal;
        String line;
        try
        {
            while((line = new String(lineReader.readLine()))!=null){
                labels.add( new RichTextField(line){
                    protected void paint(Graphics graphics) 
                    {
                            graphics.setColor(Color.BLACK);
                            super.paint(graphics);
                    }
                }            
            );
            labels.add(new NullField(NullField.FOCUSABLE));
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
        //return palabras;
    }

    public boolean onSavePrompt(){
        return true;
    }
}
