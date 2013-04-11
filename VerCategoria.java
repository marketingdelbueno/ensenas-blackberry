package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.Graphics;
import java.util.Stack;
import java.util.Vector;

import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Field;
import java.util.Hashtable;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.system.Characters;

  public class VerCategoria extends MainScreen
    {
        public Hashtable palabras_hash,palabras_hash_video;
        public TreeField myTree; 
        public NeoEssentia _app;
        public ListaPalabra listapalabra;
        public Utilidades u;
        
        public VerCategoria(Vector categorias,Hashtable p1,Hashtable p2,NeoEssentia app,ListaPalabra l)
        {  
        
            u = new Utilidades();
            palabras_hash = p1;
            palabras_hash_video = p2;
            listapalabra = l;
            _app = app;
        
            VerticalFieldManager vert = u.titulo();
               
            TreeCallback myCallback = new TreeCallback();
            myTree = new TreeField(myCallback, Field.FOCUSABLE){
                 
               protected void paint(Graphics graphics) 
               {
                    graphics.setColor(Color.BLACK);
                    super.paint(graphics);
               }
         
                };
           Pila_Subcategoria p_s;
            Palabra p;
            int i,j,k;
            
            for(i=categorias.size()-1; i >= 0; i-- ){
                
                   p_s = (Pila_Subcategoria) categorias.elementAt(i) ;
                    int nodesubcategoria = myTree.addChildNode(0, p_s.get_nombre());
                
                    for(k=p_s.getnumsub()-1; k>=0 ; k-- ){
                        
                          p =  p_s.get_palabra(k);
                          myTree.addChildNode(nodesubcategoria, p.getpalabra());  
                    }
                     myTree.setExpanded(nodesubcategoria, false);
              
            }
     
            VerticalFieldManager fondo = u.fondo(true);
          
            this.setBanner(vert);
            fondo.add(myTree);
            add(fondo);
        }
            
        static private class TreeCallback implements TreeFieldCallback 
        {
            public void drawTreeItem(TreeField _tree, Graphics g, int node, int y, int width, int indent) 
            {
                String text = (String)_tree.getCookie(node); 
                g.drawText(text, indent, y);
            }
        }
      
                 
     protected boolean keyChar(char key, int status, int time)
        {
            //Si se pulsa el boton de escape
            if( key == Characters.ENTER ){
                String p = myTree.getCookie( myTree.getCurrentNode() ).toString();
              if( palabras_hash.contains( p ) == true  ){
               
                    String buscar = u.quitar_carac_espec( p );
              //      String pala_correcta = ( palabras_hash.get(buscar) ).toString();                    

                   final String localName;
                   final String remoteName =( palabras_hash_video.get(buscar) ).toString();
                   descargar_video descargador;
                   
                   
                   localName = "file:///SDCard/BlackBerry/temporalNeoEssentia/"+buscar+".mp4";
                   descargador = new descargar_video(remoteName,localName,buscar);
                   
                VerVideo infoScreen = new VerVideo( p , localName ,_app ,  palabras_hash,palabras_hash_video,listapalabra,descargador);
                _app.pushScreen(infoScreen);
               
               }
               else{
                   
                    myTree.setExpanded(myTree.getCurrentNode() ,!myTree.getExpanded( myTree.getCurrentNode() ) );
                   
                   }
                   return true;
            }
            return super.keyChar(key, status, time); //Retorna las acciones que hace el teclado normalmente
       }
        
      public boolean invokeAction(int action)
    {        
        
        switch(action)
        {
            case ACTION_INVOKE: // Trackball click.
                String p = myTree.getCookie( myTree.getCurrentNode() ).toString();
                
                 if( palabras_hash.contains( p ) == true  ){
               
                    String buscar = u.quitar_carac_espec( p );
                   // String pala_correcta = ( palabras_hash.get(buscar) ).toString();                    

                   final String localName;
                   final String remoteName =( palabras_hash_video.get(buscar) ).toString();
                   descargar_video descargador;
                   
                   
                   localName = "file:///SDCard/BlackBerry/temporalNeoEssentia/"+buscar+".mp4";
                   descargador = new descargar_video(remoteName,localName,buscar);
                   
                VerVideo infoScreen = new VerVideo( p , localName ,_app ,  palabras_hash,palabras_hash_video,listapalabra,descargador);
                _app.pushScreen(infoScreen);
               
               }
               else{
                   
                    myTree.setExpanded(myTree.getCurrentNode() ,!myTree.getExpanded( myTree.getCurrentNode() ) );
                   
                   }
                return true; // We've consumed the event.
        
            
        }    
        return  super.invokeAction(action);
    }   
        
          public boolean onSavePrompt(){
               return true;
               }      
    } 
    
