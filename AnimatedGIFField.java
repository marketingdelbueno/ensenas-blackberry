package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

//Un campo que desplega un gif animado

class AnimatedGIFField extends BitmapField
{
    private GIFEncodedImage _image;     //La imagen que se dibujara
    private int _currentFrame;          //El cuadro actual en la secuencia de animacion
    private AnimatorThread _animatorThread;

    public AnimatedGIFField(GIFEncodedImage image)
    {
        this(image, 0);
    }

    public AnimatedGIFField(GIFEncodedImage image, long style)
    {
        //se llama a super para configurar el campo con el estilo especidicado en la variable style
        //se pasa una imagen como parametro para configurar el tama;o del cuadro de dicha secuencia a ejecutar
        super(image.getBitmap(), style);

        //guarda la imagen y sus dimenciones
        _image = image;
        
        //empieza el hilo que ejecuta la animacion
        _animatorThread = new AnimatorThread(this);
        _animatorThread.start();
    }

    protected void paint(Graphics graphics)
    {
        //se llama a super.paint. esto dibujara el primer cuadro de fondo y manejara cualquier dibujado de foco
        super.paint(graphics);

        //no dibujar primer cuadro ya que este esta dibujado
        if (_currentFrame != 0)
        {
            //dibuja el cuadro de animacion
            graphics.drawImage(_image.getFrameLeft(_currentFrame), _image.getFrameTop(_currentFrame),
                _image.getFrameWidth(_currentFrame), _image.getFrameHeight(_currentFrame), _image, _currentFrame, 0, 0);
        }
    }

    //detiene la animacion cuando el screen donde esta la animacion es cerrado o ocultado
    protected void onUndisplay()
    {
        _animatorThread.stop();
        super.onUndisplay();
    }


    //el hilo que maneja la animacion
    private class AnimatorThread extends Thread
    {
        private AnimatedGIFField _theField;
        private boolean _keepGoing = true;
        private int _totalFrames;     //el numero total de cuadros .
        private int _loopCount;       //el numero de veces que la animacion es completada
        private int _totalLoops;      //el numero de veces que la animacion puede tiene permiso para reproducirce

        public AnimatorThread(AnimatedGIFField theField)
        {
            _theField = theField;
            _totalFrames = _image.getFrameCount();
            _totalLoops = _image.getIterations();

        }

        public synchronized void stop()
        {
            _keepGoing = false;
        }

        public void run()
        {
            while(_keepGoing)
            {
                //se hace invalidate para refrescar
                UiApplication.getUiApplication().invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        _theField.invalidate();
                    }
                });

                try
                {
                    //duerme el cuadro actual para que el proximo pueda ser dibujado
                    sleep(_image.getFrameDelay(_currentFrame) * 10);
                }
                catch (InterruptedException iex)
                {} //en caso de que no se duerma sucediendo asi una excepcion

                //incrementa el cuadro
                ++_currentFrame;

                if (_currentFrame == _totalFrames)
                {
                    //resetea el contador en caso de haber llegado al cuadro final de la animacion
                    _currentFrame = 0;

                    ++_loopCount;

                    //revisa si la animacion esta hecha para continuar o detenerce una vez alcanzado su final
                    if (_loopCount == _totalLoops)
                    {
                        _keepGoing = false;
                    }
                }
            }
        }
    }
}
