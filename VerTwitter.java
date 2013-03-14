package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.blackberry.api.browser.Browser;


            public class VerTwitter extends MainScreen
    {      
        public VerTwitter()
        {
        Browser.getDefaultSession().displayPage( "http://mobile.twitter.com/neoessentia" );
        }
   
    } 
