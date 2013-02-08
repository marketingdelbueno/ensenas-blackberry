package com.rim.samples.device.enSenas;
/***************************************************************LEA AKI PRIMERAMENTE
 * PARA ENTENDER LA FUNCIONALIDAD DE ESTA CLASE LEA TODA LA CLASE UNA VEZ NO IMPORTA QUE NO ENTIENDA Y LE ASEGURO QUE A LA SEGUNDA LEIDA
 * USTED ENTENDERA A LA PERFECCION
 */
 
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.container.*;
import net.rim.blackberry.api.homescreen.HomeScreen;

//esta es la clase principal donde se encuentra el main, que es el comienzo de todo, es decir
//la ejecucucion de toda la aplicacion empieza aqui
public class NeoEssentia extends UiApplication
{
    public NeoEssentia(boolean autoStart)
    {
        //el codigo que aqui empieza lo que hace es asignarle un foco al icono que aparece cuando se prende el celular
        //el de seleccionar la aplicacion "EnSenas"
        if (autoStart)//si autostar es true autostart es un valor que se le introdujo al proyecto "focusicono", a este se le dio click secundario
        //y se selecciono propiedades en la pesta;a application se le escribio en argument "autostart"
        //esto significa que estas sentencias que vienen a continuacion se ejecutan sin que el usuario entre en la aplicacion, en "segundo plano"
        {
        final Bitmap regularIcon = Bitmap.getBitmapResource("icon.png");//este es el icono sin foco
        final Bitmap rolloverIcon = Bitmap.getBitmapResource("focusedicon.png");//este es el icono con foco

        invokeLater(new Runnable() //aqui se ejecuta un runable lo que significa que es un hilo que esta en sugundo plano
        {
            public void run() 
            {
                ApplicationManager myApp = ApplicationManager.getApplicationManager();
                boolean inStartup = true;

                while (inStartup)//mientras instartup sea true es decir que el usuario no ha entrado en la aplicacion
                {
                    if (myApp.inStartup())//esto quiere decir si no se esta situado sobre el icono de inicio de la aplicacion EnSenas
                    {
                        try
                        {
                            Thread.sleep(1000);//duerme el hilo por un segundo
                        }
                        catch (Exception ex)
                        {
                          // TODO: handle exception.
                        }
                    }
                    else//si se esta situado sobre el icono
                    {
                        try
                        {
                            HomeScreen.updateIcon(regularIcon, 0);//se va a actualizar el icono  sin foco
                            HomeScreen.setRolloverIcon(rolloverIcon, 0);//se cambio por el icono enfocado
                            inStartup = false;
                        }
                        catch (Exception ex)
                        {
                            // Couldn't set the rollover icon, handle exception
                        }
                    }
                 }
                 // We're done setting up the icons. Exit the app.
                 System.exit(0);//se sale de "esta parte de la aplicacion"
            }
        });
             //el focusicono es un jdp que esta ubicado a lo ultimo de la lista de clases, siendo este un proyecto y no una clase como tal 
    }
    else
    {
         // ahora si no es "autostart" el argumento del main quiere decir que el usuario le dio "click" a la aplicacion y entro en ella normalmente
         try
        {
            Inicio screen = new Inicio(this);//se llama a la clase inicio   
            pushScreen(screen);            //se manda a la pantalla dicha clase con todas sus funcionalidades
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();//si sucede una excepcion se dibuja el arbol de error
        }
    }
  }

   public static void main(String[] args)
    {
        NeoEssentia theApp;
        
        //************************ Aqui sucede lo escencial para la ejecucion de todo**********************/
        if (args != null && args.length > 0 && args[0].equals("autostart"))//Si el argumento que entra en el  main(String[] args) es "autostart" 
        {   
            theApp = new NeoEssentia(true);//se llama a NeoEssentia que es el constructor de la clase con el argumento "true"
            //es decir que se ejecuta lo del icono en el bb con o sin foco
            theApp.enterEventDispatcher();//se ejecuta el disparador de eventos
        } 
         else //sino
        { 
        //se ejecuta la aplicacion como tal
            theApp = new NeoEssentia(false);//se ejecuta el constructor NeoEssentia con el argumento false
            theApp.enterEventDispatcher();//y se ejecuta el disparador de evento
        }
        
    }
}

