package com.rim.samples.device.EnSenas;
 
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Field;
import java.lang.Throwable;
import java.lang.RuntimeException;

class Banner {
    private AnimatedGIFField _ourAnimation = null;
    private LabelField _ourLabelField = null;  
    
    Banner() {        
        GIFEncodedImage ourAnimation = (GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("cargando.gif");
        _ourAnimation = new AnimatedGIFField(ourAnimation, Field.FIELD_HCENTER);
    }
    
    public AnimatedGIFField propaganda(){
        return _ourAnimation;
    }
} 
