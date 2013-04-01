
package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.blackberry.api.browser.Browser;

    //Redirecciona a la pagina de facebook para colocar un comentario de aprendizaje con EnSenas
    public class ComentarFacebook extends MainScreen
    {      
        public ComentarFacebook(String palabra)
        {
            Browser.getDefaultSession().displayPage( 
            //"http://m.facebook.com/sharer.php?u=http://YOUR SITE URL" "
            "http://m.facebook.com/sharer.php?m2w&s=100&u=http%3A%2F%2Fwww.neoessentia.org%2F&p[title]=Ensenas+Venezolano&p[summary]=Acabo+de+aprender+la+palabra+"+ palabra +"+en+lenguaje+de+senas+venezolana%2C+aprendelo+tu+tambien"
            );
        }
   
    } 
