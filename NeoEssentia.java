package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.container.*;
import net.rim.blackberry.api.homescreen.HomeScreen;

public class NeoEssentia extends UiApplication
{
    public NeoEssentia(boolean autoStart)
    {
        if (autoStart)
        {
        final Bitmap regularIcon = Bitmap.getBitmapResource("icon.png");
        final Bitmap rolloverIcon = Bitmap.getBitmapResource("focusedicon.png");

        invokeLater(new Runnable() 
        {
            public void run() 
            {
                ApplicationManager myApp = ApplicationManager.getApplicationManager();
                boolean inStartup = true;

                while (inStartup)
                {
                    if (myApp.inStartup())
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (Exception ex)
                        {
                          // TODO: handle exception.
                        }
                    }
                    else
                    {
                        try
                        {
                            HomeScreen.updateIcon(regularIcon, 0);
                            HomeScreen.setRolloverIcon(rolloverIcon, 0);
                            inStartup = false;
                        }
                        catch (Exception ex)
                        {
                            // Couldn't set the rollover icon, handle exception
                        }
                    }
                 }
                 // We're done setting up the icons. Exit the app.
                 System.exit(0);
            }
        });
             
    }
    else
    {
         // The application was started by the user, display the UI.
         try
        {
            Inicio screen = new Inicio(this);   
            pushScreen(screen);            
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }
  }

   public static void main(String[] args)
    {
        NeoEssentia theApp;
        
        //Check for the argument defined in the project properties.
        if (args != null && args.length > 0 && args[0].equals("autostart"))
        {   
            theApp = new NeoEssentia(true);
            theApp.enterEventDispatcher();
        } 
         else 
        { 
            theApp = new NeoEssentia(false);       
            theApp.enterEventDispatcher();
        }
        
    }
}

