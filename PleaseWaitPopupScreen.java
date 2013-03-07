package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Field;
import java.lang.Throwable;
import java.lang.RuntimeException;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.ui.component.Dialog;

        //esta clase es algo complicada pero es bien de pinga una ves que se entiende
        //esta clase muestra un cuadro con un gif que dice "downloading please wait"
        //y dura mientras la aplicacion descarga una vez que la descarga ha culminado esta gif desaparece

public class PleaseWaitPopupScreen extends PopupScreen {//esta clase extiende popupscreen
    private AnimatedGIFField _ourAnimation = null;  //se declaran sus atributos de tipo AnimatedGIFField 
    private LabelField _ourLabelField = null;       //y LabelField
    private PleaseWaitPopupScreen(String text) {    //se declara su constructor
        super(new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR));
        GIFEncodedImage ourAnimation = (GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("cargando.gif");//se carga el gif animado
        _ourAnimation = new AnimatedGIFField(ourAnimation, Field.FIELD_HCENTER);// y al atributo de la clase _ourAnimation se le asigna el gif
                                                                                //ya previamente cargado
        this.add(_ourAnimation);//se añade al popupscreen el gif animado en la variable _ourAnimation
        _ourLabelField = new LabelField(text, Field.FIELD_HCENTER);//se agrega al label el nombre del video que se va mostrar
        this.add(_ourLabelField);//y se agrega dicho label al popupscreen
    }
    
    //este metodo se llama antes de declarar el objeto como tal, se que suena complicado pero es asi
    //y atravez de aqui es que se llama al constructor de arriba y se siguen un conjunto de pasos
    public static void showScreenAndWait(/*final descargar_video runThis,*/ String text/*, final VerVideo video*/) {
     final PleaseWaitPopupScreen thisScreen = new PleaseWaitPopupScreen(text);//se crea el popupscreen del constructor arriba
        Thread threadToRun = new Thread() {//se corre un hilo
            public void run() {
                // y se arroja otro hilo donde se muestra el gif como se ve abajo
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(thisScreen);
                    }
                });
                // ahora se corre el codigo del descargador que entro como parametro de este metodo final descargar_video runThis
                Utilidades u = new Utilidades();//se crea el favuloso Utilidades u
               
                //if( u.existe_microsd() && !runThis.archivo_existe() ){//se valida si existe microsd porq sino no tiene caso
                                                                      //tambien se valida si el archivo existe porq de lo contrario no tendria
                                                                      //sentido gastar megas volviendolo a descargar
                    UiApplication.getUiApplication().invokeLater(new Runnable() {//se ejecuta este hilo pero primero se esperan 60 seg
                    //es decir si a los 60 segundos no se ha descargado el video entonces se asume que no se descargo correctamente
                        public void run() {                            
                            //if( video.cargado() ){
                                UiApplication.getUiApplication().popScreen(thisScreen);//se quita el popupscreen del gif
                                //video.ver();                //y se llama a video.ver donde ahi si el estado no es descargado de una vez dice
                                                            //hubo problemas en la conexion
                            //}
                        }},4000,false );//el false aqui significa que no se va a volver a lanzar el hilo, es decir que no es un bucle
                    
                    /*try {
                        runThis.run();//se ejecuta el descargador y hasta que este no se ejecute no se siguen ejecutando las lineas de cogido siguientes
                    } catch (Throwable t) {
                        t.printStackTrace();
                        throw new RuntimeException("Exception detected while waiting: " + t.toString());
                    }*/
//cuando ya se descargo el archivo se esperan 2 segunditos, se llama al hilo que quita la popupscreen y muestra el video satisfactoriamente descargado
                    /*UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            if( video.cargado() ){
                                UiApplication.getUiApplication().popScreen(thisScreen);
                                video.ver();
                            }
                        }},1000,false );*/
                    //}
                /*else{//ahora si el video existe o no hay la microsd, quita el gif y se llama a video.ver que como dije antes 
                     //o reproduce el video o dice que hay algun problema en este caso el unico problema posible es que no haya microsd incertada
                   UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().popScreen(thisScreen);
                        video.ver();
                      }},1000,false );
                    }
            */}
        };
        threadToRun.start();//y aki en realidad se ejecuta todo lo anterior
    }
}



