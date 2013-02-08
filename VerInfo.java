package com.rim.samples.device.enSenas;

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
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
//clase verinfo
public class VerInfo extends MainScreen
{
    private FontFamily fontFamily[] = FontFamily.getFontFamilies();
    private Font font;
    public VerInfo()
    {   
        //la favulosa variable u de tipo utilidades
        Utilidades u = new Utilidades();
        //se asigna el titulo al banner de la api de blackberry
        this.setBanner(u.titulo());
        //al vert se le asigna el u.fondo(String) con un string que es especial para este momento
        VerticalFieldManager vert = u.fondo(extension());
        // a partir de aqui se crea un vertical field manager con unas caracteristicas peculiares
        VerticalFieldManager labels = new VerticalFieldManager(VerticalFieldManager.FIELD_HCENTER | VerticalFieldManager.VERTICAL_SCROLL ){
            //se tiene que volver a crear la variable u para que sea usada dentro de este vertical field manager
            Utilidades u = new Utilidades();
            //se calcula la altura del espacio visible real es decir de toda la pantalla menos la altura del banner
            int height=(Graphics.getScreenHeight()-u.banner().getBitmapHeight());
            //se calcula el ancho de la pantalla del Blackberry
            int width=Graphics.getScreenWidth();
            //este metodo es importante el sublayout, este se encarga de hacer que el vertical field manager solo sea rellenado del
            //ancho de maxWidth y el alto de maxHeight
            protected void sublayout(int maxWidth, int maxHeight){
                super.sublayout(width, height);
                setExtent(width, height);
            }
        };
        leer_data(labels);//se llena de labels el verticalfieldmanager llamado labels        
        vert.add(labels);//y este se le asigna a vert
        add(vert);//al final se asigna todo al screen donde nos encontramos actualmente
    }  
    public String extension(){//este metodo determina el tamaño de la pantalla 
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
    //este metodo lee el archivo con los creditos y los asigna a los labels con los que 
    //se llenara el verticalfieldmanager llamado labels arriba descrito
    public void leer_data(VerticalFieldManager lab){    
        
        VerticalFieldManager labels = lab;
        InputStream stream = getClass().getResourceAsStream("/info/info.txt");//se lee un stream en esta direccion
        LineReader lineReader = new LineReader(stream);//se encarga de leer las lineas del txt
        Integer num_cat,num_sub,num_pal;
        String line;
        //estas dos lineas siguientes ponen la fuente y el tama;o de dicha fuente a todas las letras de este screen
        font = fontFamily[0].getFont(FontFamily.TRUE_TYPE_FONT, 13);
        Font.setDefaultFont(font);
        
        try
        {
            int cont = 0 ;//contador!
            //la linea siguiente lo que hace es poner un campo vacio, es decir "invisible" pero focusable que permite hacer scroll a los labels            
            labels.add(new NullField(NullField.FOCUSABLE));
            
            while((line = new String(lineReader.readLine()))!=null){//mientras hayan lineas por leer
                if(line.equals("logothinkasoft.png")){//si se consigue en el txt la palabra "logothinkasoft.png"
                    //se agrega a labels el logo de thinkasoft
                    BitmapField logo = new BitmapField( Bitmap.getBitmapResource("logothinkasoft.png" ) , Field.NON_FOCUSABLE | Field.FIELD_HCENTER );
                    labels.add(logo);
                }
                else if(line.equals("logos4biz.png")){//igual que el anterior
                    BitmapField logo = new BitmapField( Bitmap.getBitmapResource("logos4biz.png") , Field.NON_FOCUSABLE | Field.FIELD_HCENTER );
                    labels.add(logo);
                }
                else{//sino pasa nada de lo anterior que es lo que mas comunmente pasa
                //se crean richtextfields con el color de letras en negro
                    RichTextField texto = new RichTextField( line,RichTextField.TEXT_ALIGN_HCENTER | RichTextField.NON_FOCUSABLE ){
                        protected void paint(Graphics graphics) 
                        {
                            graphics.setColor(Color.BLACK);
                            super.paint(graphics);
                        }
                    };                
                    cont++;
                    if(cont==2){//y cada 3 labels se asigna un nullfield para lo del scroll
                        labels.add(new NullField(NullField.FOCUSABLE));
                        cont=0;
                    }
                    labels.add(texto);
                }                
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
    }
//este metodo se implementa de manera que cuando se salga de la aplicacion o de este 
//caso de esta pantalla no pregunte si se quiere guardar alguna cosa
    public boolean onSavePrompt(){
        return true;
    }
}
