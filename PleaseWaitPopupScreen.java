package com.rim.samples.device.EnSenas;

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

public class PleaseWaitPopupScreen extends PopupScreen {

    private AnimatedGIFField _ourAnimation = null;
    private LabelField _ourLabelField = null;
  
    private PleaseWaitPopupScreen(String text) {
        super(new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR));
        GIFEncodedImage ourAnimation = (GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("cargando.gif");
        _ourAnimation = new AnimatedGIFField(ourAnimation, Field.FIELD_HCENTER);
        this.add(_ourAnimation);
        _ourLabelField = new LabelField(text, Field.FIELD_HCENTER);
        this.add(_ourLabelField);
    }

    public static void showScreenAndWait(final descargar_video runThis, String text, final VerVideo video) {
     final PleaseWaitPopupScreen thisScreen = new PleaseWaitPopupScreen(text);
        Thread threadToRun = new Thread() {
            public void run() {
                // First, display this screen
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(thisScreen);
                    }
                });
                // Now run the code that must be executed in the Background
                Utilidades u = new Utilidades();
               
                //CoverageInfo.getCoverageStatus()==CoverageInfo.COVERAGE_DIRECT
                if( u.existe_microsd() && !runThis.archivo_existe() ){
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                   
                   }});
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            
                            if( video.cargado() ){
                                UiApplication.getUiApplication().popScreen(thisScreen);
                                video.ver();
                            }
                        }},60000,false );
                    
                    try {
                        runThis.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                        throw new RuntimeException("Exception detected while waiting: " + t.toString());
                    }
                   
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                        if( video.cargado() ){
                        UiApplication.getUiApplication().popScreen(thisScreen);
                        video.ver();
                        }
                        }},100,false );
                }
                else{
                  UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                   
                   }});
                   UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                      UiApplication.getUiApplication().popScreen(thisScreen);
                      video.ver();
                      }},100,false );
                    }
            }
        };
        threadToRun.start();
    }
}



