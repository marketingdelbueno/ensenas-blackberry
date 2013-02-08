package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.blackberry.api.browser.Browser;


            public class VerVisto extends MainScreen
    {      
        public VerVisto()
        {
        Browser.getDefaultSession().displayPage( "http://videodiccionario.thinkasoft.com/vistos.php" );
        
        }
    } 
