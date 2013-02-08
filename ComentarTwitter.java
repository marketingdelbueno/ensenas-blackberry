
package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.blackberry.api.browser.Browser;

    //Redirecciona a twitter para colocar una comentario de aprendizaje con EnSeñas
   public class ComentarTwitter extends MainScreen
    {      
        public ComentarTwitter(String palabra)
        {
            Browser.getDefaultSession().displayPage(
            "http://mobile.twitter.com/home?status=Aprendi%20la%20palabra%20" + palabra + "%20por%20medio%20de%20la%20aplicacion%20BB%20de%20@Neoessentia" );
        }
   
    } 

        
