
package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.blackberry.api.browser.Browser;

    //Redirecciona a la pagina de facebook para colocar un comentario de aprendizaje con EnSenas
    public class ComentarFacebook extends MainScreen
    {      
        public ComentarFacebook(String palabra)
        {
            Browser.getDefaultSession().displayPage( 
            "http://www.facebook.com/sharer/sharer.php?s=100&p[url]=http%3A%2F%2Fwww.neoessentia.org%2F&p[title]=Ensenas+Venezolano&p[summary]=Acabo+de+aprender+la+palabra+"+ palabra +"+en+lenguaje+de+senas+venezolana%2C+aprendelo+tu+tambien"    
            );
        }
   
    } 
