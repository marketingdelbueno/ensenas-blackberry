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
    
   public class VideoPlay extends MainScreen
    {      
   
     String vid;
     Player player;
     VideoControl videoControl;
     Field videoField;
     
     public VideoPlay(String v)
        {
            vid = v;
          
            try 
            {
                if( vid.charAt(0) == '/' ){
                InputStream is = getClass().getResourceAsStream(vid);
                player = javax.microedition.media.Manager.createPlayer(is,"video/mp4");
                  }
                else{
                player = javax.microedition.media.Manager.createPlayer(vid);
                 }
                    
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
