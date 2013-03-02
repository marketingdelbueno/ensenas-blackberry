package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import java.io.IOException;
import javax.microedition.media.control.*;
import javax.microedition.media.*;
import net.rim.device.api.ui.Graphics;
import java.io.InputStream;
import net.rim.device.api.system.RadioInfo;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;

   public class VideoPlay extends MainScreen
    {      
   
     String vid;
     Player player;
     VideoControl videoControl;
     Field videoField;
     Utilidades u;
      public String apn(){//clase que determina el apn de la operadora que posee el celular (cantv,digitel,movilnet)
        String nombre_operadora = RadioInfo.getCurrentNetworkName();
        String apn=null;
        if(nombre_operadora=="DIGITEL GSM"||nombre_operadora=="Digitel"){
            apn="gprsweb.digitel.ve";
        }        
        if(nombre_operadora=="Movistar"){
            apn="internet.movistar.ve";
        }
        if(nombre_operadora=="Telecomunicaciones Movilnet"||nombre_operadora=="Movilnet"){
            apn="int.movilnet.com.ve";                
        }
        return apn;
    }
     
     public VideoPlay(String v)
        {
            vid = v;
            u= new Utilidades();
            //try 
            {
                String modoConexion;
                if(u.isWiFi()){
                    modoConexion = ";interface=wifi";//en caso de ser wifi
                }
                else{
                    modoConexion = ";deviceside=true;apn="+apn();//en caso de ser gastando los megas del plan de datos de la operadora
                }
                //Dialog.alert(modoConexion);
                
                //
                if( vid.charAt(0) == '/' ){        
                    try{
                        InputStream is = getClass().getResourceAsStream(vid);
                        player = javax.microedition.media.Manager.createPlayer(is,"video/mp4");
                        player.realize();
                        if ((videoControl = (VideoControl)player.getControl("VideoControl")) != null) {
                            videoField = (Field)videoControl.initDisplayMode( 0, "net.rim.device.api.ui.Field" );
                            
                            if( vid.charAt(0) != '/' )
                                add(videoField);
                                
                            videoControl.setDisplaySize((int)(Graphics.getScreenWidth()),(int)(Graphics.getScreenHeight()) );
                            VolumeControl volume = (VolumeControl) player.getControl("VolumeControl");
                            volume.setLevel(100); 
                            videoControl.setVisible(true);
                        }
                    }
                    catch(MediaException me)
                    {
                        Dialog.alert(me.toString());
                    }
                    catch(IOException ioe)
                    {
                        Dialog.alert(ioe.toString());
                    }
                }
                else if( vid.charAt(0) == 'r' ){
                    vid=vid+modoConexion;
                    //Browser.getDefaultSession().displayPage(vid+modoConexion);
                    //player = javax.microedition.media.Manager.createPlayer( vid+modoConexion);  
                 }   
            }
        }   
        
        public void svideo(){
            Browser.getDefaultSession().displayPage(vid);
        }
        
        public Player video(){
            return player;
            }
            
        public Field field(){
            return videoField;
            }
              
        public boolean onSavePrompt(){
            return true;
            }     
    } 
