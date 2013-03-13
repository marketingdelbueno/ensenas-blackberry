package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.KeywordFilterField;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Color;
import java.util.Hashtable;
import java.util.Vector;
import net.rim.device.api.ui.component.TextField;

public class VerDiccionario extends MainScreen{
       private TextField bCampo;
       private KeywordFilterField filtro;
       private ListaPalabra listaP;
       private NeoEssentia app3;
       private Utilidades u;
       private Hashtable palabras_hash_video,  palabras_hash;
    
    //Se desarrolla ventana de Buscar A-Z
    public VerDiccionario(ListaPalabra listap, NeoEssentia app2, Hashtable p_h, Hashtable p_h_v){  
            
            u = new Utilidades();
            VerticalFieldManager vert = u.titulo();
            app3 = app2;   
            listaP = listap;
            palabras_hash_video = p_h_v;
            palabras_hash = p_h;
            filtro =  new KeywordFilterField(){
             
              //Resaltar foco de la palabra seleccionada
               protected void drawFocus(Graphics graphics, boolean on)
               {
                  graphics.setColor(Color.ROYALBLUE);
                  graphics.fillRect(0,filtro.getRowHeight()*filtro.getSelectedIndex(),this.getWidth(),filtro.getRowHeight());
                  graphics.setColor(Color.BLACK);
                  invalidate();
                  super.paint(graphics);
               }
               
               //Dibujar letras en blanco   
                protected void paint(Graphics graphics) 
                {
                   graphics.setColor(Color.BLACK);
                   super.paint(graphics);
                }
            };
            
            filtro.setSourceList(listaP, listaP); //Agregar lista de palabras al filtro
            bCampo = new TextField(){
               //Cambio el color de la letra del campo de busca
               protected void paint(Graphics graphics) 
                {
                    graphics.setColor(Color.BLACK);
                    super.paint(graphics);
                }
            };
            
            bCampo.setLabel("Buscar: ");
            bCampo.setMaxSize(20);
            filtro.setKeywordField(bCampo); //Agrego el campo que contendra las palabras que se filtraran
            vert.add(bCampo);
            this.setBanner(vert);
            VerticalFieldManager fondo = u.fondo(true);
            fondo.add(filtro);
            add(fondo);
        }
           
           
     protected boolean keyChar(char key, int status, int time)
        {
            //Si se pulsa el boton de escape
            if( key == Characters.ENTER && !bCampo.isFocus()){
              Palabra pal = (Palabra)filtro.getSelectedElement();  //buscar la palabra clave
              String buscar = u.quitar_carac_espec( pal.getpalabra() ); //quitar los caracteres especiales
            //  String pala_correcta = ( palabras_hash.get(buscar) ).toString();//buscar la palabra escrita correctamente
    
              final String localName;
              final String remoteName =( palabras_hash_video.get(buscar) ).toString();//buscar el link del video que le correspode
               VerVideo infoScreen = new VerVideo( buscar , remoteName ,app3,null,null,null,null );
               app3.pushScreen(infoScreen);   
                
                return true; //Se retorna verdad para finalizar la accion
            }
            return super.keyChar(key, status, time); //Retorna las acciones que hace el teclado normalmente
       }
           
    public boolean invokeAction(int action)
    {        
        switch(action)
        {
            case ACTION_INVOKE: // Trackball click.
            
                //Si el campo de buscar tiene el foco al darle al trackball, procedo a ver el video
                if(!bCampo.isFocus()){
                    Palabra pal = (Palabra)filtro.getSelectedElement();  //buscar la palabra clave
                    String buscar = u.quitar_carac_espec( pal.getpalabra() ); //quitar los caracteres especiales
                  //  String pala_correcta = ( palabras_hash.get(buscar) ).toString();//buscar la palabra escrita correctamente
    
                    final String localName;
                    final String remoteName =( palabras_hash_video.get(buscar) ).toString();//buscar el link del video que le correspode
                    VerVideo infoScreen = new VerVideo( buscar , remoteName ,app3,null,null,null,null );
                    app3.pushScreen(infoScreen);
               };break;
               default: return super.invokeAction(action);
        }
         return true; // We've consumed the event.
    }   
           
           public boolean onSavePrompt(){
               return true;
               }
       
    }
