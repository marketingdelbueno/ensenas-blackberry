package com.rim.samples.device.enSenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import java.io.InputStream;
import net.rim.device.api.ui.Screen;
import java.util.Vector;
import net.rim.device.api.io.LineReader;
import java.io.EOFException;
import java.io.IOException;
import net.rim.device.api.ui.component.KeywordFilterField;
import net.rim.device.api.system.EncodedImage;
import javax.microedition.media.control.*;
import javax.microedition.media.*;
import net.rim.device.api.ui.Graphics;
import java.util.Hashtable;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.component.LabelField;

   public class NeoEssentiaScreen extends MainScreen implements FieldChangeListener
{
    private BasicEditField BuscarCampo;//esta clase muestra un recuadro donde se pueden escribir cosas tiene un conjunto de
    //metodos para manipular dicha clase
    private BotonPersonalizado twitter,facebook,catalogo,masvisto,info,dicci;//esta clase fue implementada por nosotros para crear botones
    //no estandar sino con la forma el dise;o que nosotros querramos
    private Vector palabras;
    private ListaPalabra listapalabra;
    private Hashtable palabras_hash_video,palabras_hash;//esta clase viene en la api que es lo que llamariamos comunmente tabla de hash
    private HorizontalFieldManager buscarCamp, hor, hor1, hor2, hor3,espaciador1;//HorizontalFieldManager clase que usamos mucho
    //esta clase ordena como un arreglo horizontal, campos que pueden tener cualquier cosa desde fotos videos textos en forma de labels etc
    //       |          |         |               |              |     |                |
    //       | "dibujo" | "texto" | "otro dibujo" | "un videito" | ... | "ultimo campo" |
    //       |          |         |               |              |     |                |
    private VerticalFieldManager vert,hor_vid,fondo;
    //esta clase VerticalFieldManager es parecidad a la HorizontalFieldManager solo que alinea a los campos de forma vertical
    //     _____________
    //     | "campo 1" |
    //     _____________
    //     | "campo 2" |
    //     _____________
    //     | "campo 3" |
    //     _____________
    //     | "campo 4" |
    //     _____________
    //     | "campo 5" |
    //     _____________
    //  una matriz seria la combinacion de un horizontal con un vertical field manager ! XD 
    private Bitmap imagen_ensenas;
    private BitmapField CampoImagenEnsenas;    
    private NeoEssentia _app;    
    private Vector categorias;
    private Utilidades u;
    private String nombre_icono;
    private int tam_icono;
    private RichTextField estado;
    
    // ***************************************************************** //
    // esta es la segunda pantalla que se ejecuta al abrir la aplicacion //    
    // ***************************************************************** //
    
    public NeoEssentiaScreen(NeoEssentia app)
    {        
        _app = app;//se pasa la aplicacion a esta pantalla
        try {
           //se establece la conexion con la carpeta temporal donde se han de guardar los videos que se han de ver 
           FileConnection temporal = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/");
             if (!temporal.exists()) {
                temporal.mkdir();// si no existe el direcctorio temporalNeoEssentia se crea condicion que es la ideal
                //esta carpeta se borra al salir de la aplicacion por ende, si esta carpeta existe, es un PROBLEMA!
             }
           temporal.close();//se cierra la conexion ya que la carpeta ahora existe
        }catch (IOException ex) {  }//se maneja la excepcion
                
        u = new Utilidades();//se crea la variable u de tipo Utilidades que es la que tiene muchas cosas importantes
        hor = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER); 
        //este es un verticalfieldmanager vulgar pero a continuacion se le reimplementan algunos metodos para obtener las siguientes funcionalidades
        hor_vid = new VerticalFieldManager(VerticalFieldManager.FIELD_HCENTER ){
            
            public void paint(Graphics graphics)//este metodo paint le pone un colo al fondo del verticalfieldmanager
            {
                graphics.clear();
                graphics.setColor( 0xFFFFFF );//color blanco
                graphics.fillRect( 0, 0,  Display.getWidth(),  Display.getHeight() );//Display.getWidth() y Display.getHeight() te retornan el anto y el ancho
                graphics.setColor( Color.BLACK );
                invalidate();//invalidate hace es refrescar la pantalla
                super.paint(graphics);
            }
        };
        hor2 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
        hor3 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
        espaciador1 = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
       
        palabras_hash_video = new Hashtable();
        palabras_hash = new Hashtable();
        palabras = leer_categoria();
       
        listapalabra = new ListaPalabra(palabras);        
        BuscarCampo= new BasicEditField("",null);
        buscarCamp = u.buscarCampo();
        HorizontalFieldManager aux = new HorizontalFieldManager ( HorizontalFieldManager.HORIZONTAL_SCROLL){
            protected void sublayout(int maxWidth, int maxHeight) {                
                int ancho=(int)(buscarCamp.getPreferredWidth()*0.80);
                //u.alerta(String.valueOf(buscarCamp.getPreferredWidth()));
                int alto=buscarCamp.getPreferredHeight();
                super.sublayout(ancho, alto);
                setExtent(ancho, alto);
            }
        };
        aux.add(BuscarCampo);
        buscarCamp.add(aux);
        String extension = new String("");       
        //se pregunta el tama;o de la pantalla y se determina el tamano de los iconos
        if( (int)Graphics.getScreenWidth() <= (int)320 ){
            //hau imagenes con un ancho de 240 y se llamaria nombre_de_imagen240.png
            extension = new String("240");
            tam_icono = 70;
            }    
        else if( (int)Graphics.getScreenWidth() > (int)320 && (int)Graphics.getScreenWidth() <= (int)480 ){
            extension = new String("300");
            tam_icono = 90;
            }
        else{
            extension = new String("480");
            tam_icono = 128;
            }
        //aki se crean los botones personalizados catalogo, mas visto, info, facebook, twitter, dicci, estudia con detenimiento la clase boton personalizado 
        catalogo = new BotonPersonalizado("Catálogo",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("catalogo"+ extension +".png" ),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        masvisto = new BotonPersonalizado("Más Vistos",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource("vistos"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        info = new BotonPersonalizado("Ayuda e Información",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "info"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        facebook = new BotonPersonalizado("Facebook",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "facebook"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        twitter = new BotonPersonalizado("Twitter",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "twitter"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        dicci = new BotonPersonalizado("Buscar A-Z",this,extension, Field.FOCUSABLE, Bitmap.getBitmapResource( "book"+ extension +".png"),Graphics.getScreenHeight(),Graphics.getScreenWidth(),null);
        //se le activa la opcion de que puedan ser clickeados
        twitter.setChangeListener(this);
        facebook.setChangeListener(this);
        catalogo.setChangeListener(this);
        info.setChangeListener(this);
        dicci.setChangeListener(this);
        masvisto.setChangeListener(this);
        //ahora se asignan horizontalfieldmanagers a verticalfieldsmanager y viceversa
        VerticalFieldManager vert = u.titulo();
        fondo = u.fondo(false);
        hor.add(buscarCamp);
        vert.add(hor);
        this.setBanner(vert);// se le asigna al banner el logo de ensenas junto con el campo de escritura estos siempre van a ser visibles y estaticos        
        
        hor2.add(catalogo);hor2.add(dicci);hor2.add(masvisto);
        hor3.add(info);hor3.add(facebook);hor3.add(twitter);
        fondo.add(hor2);
        fondo.add(hor3);
        add(fondo);
        //todo esto antes hecho quedaria algo asi
        /*
                   _____________________________________________________
                  ||   hor2 = ||  catalogo  ||  dicci     ||   masvisto ||
          fonfo=  ||__________||____________||____________||____________||
                  ||   hor3 = ||  info      ||  facebook  ||   twitter  ||
                  ||__________||____________||____________||____________||
         
        */
       // ******************************estas lineas fueron borradas del codigo ***********************************
       //las proximas 7 lineas calculan el tama;o de la pantalla 
       /*int altura_espacio=(int)(Graphics.getScreenHeight()-(tam_icono*2)-u.banner().getBitmapHeight()-u.titulo().getPreferredHeight()-hor.getPreferredHeight());
       if(altura_espacio>0){
           Bitmap esp = Bitmap.getBitmapResource("espacio.png");
           resizeBitmap resize = new resizeBitmap(esp,esp.getWidth(),(int)(altura_espacio));      
           espaciador1.add(new BitmapField( resize.get_bitmap() , Field.NON_FOCUSABLE ));
           fondo.add(espaciador1);
       }*/
       // ******************************las deje por prudencia **************************************************** 
       
       
        //en las proximas lineas se le asigna al estado el valor de "buscar..." dado a que el primer elemento que entra en focus por defecto es el
        //campo de busqueda, se agrega tanto el estado como lo que nosotros llamamos banner() a la pantalla en la parte inferior de manera estatica
        estado = new RichTextField("Buscar...",Field.NON_FOCUSABLE);
        hor_vid.add( estado );
        hor_vid.add( u.banner() );
        this.setStatus( hor_vid );
    }
    
    //en caso de estar seleccionado algun boton se le asigna a la variable estado el nombre del icono seleccionado
    public void set_estado(String e){
        estado.setText(e);
        this.invalidate();
    }
 
    //esta funcion le asigna funcionalidades a las teclas al ser presionadas, especificamente a la tecla "ENTER" 
    protected boolean keyChar(char key, int status, int time)
    {
        if ( key == Characters.ENTER )//si es Enter la tecla presionada
        {        
            if(  BuscarCampo.isFocus()){//si se esta situado en el campo BuscarCampo entonces
                IrVideo();//se va a IrVideo()
                
            }
            else if(catalogo.Seleccionado() )//si se esta en el boton catalogo, ese al hacercele foco su atributo seleccionado se hace true 
            //entonces catalogo.Seleccionado() es igual a true
            {
                VerCategoria infoScreen = new VerCategoria(categorias,palabras_hash,palabras_hash_video,_app,listapalabra);//se crea lña variable
                //VerCategoria con todos sus atributos
                _app.pushScreen(infoScreen); 
                //y se pone en pantalla la pantalla propia de VerCategoria                
            }
            else if(masvisto.Seleccionado() ){//el mismo proceso anterior para cada uno de los botones
                new VerVisto();
                
                }
            else if( info.Seleccionado() ){//el mismo proceso anterior para cada uno de los botones
                    VerInfo infoScreen = new  VerInfo();
                _app.pushScreen(infoScreen);
                
                }
            else if( facebook.Seleccionado() ){//el mismo proceso anterior para cada uno de los botones
                new VerFacebook();
                
                }
            else if( twitter.Seleccionado() ){//el mismo proceso anterior para cada uno de los botones
                new VerTwitter();
                
                }
            else if( dicci.Seleccionado() ){//el mismo proceso anterior para cada uno de los botones
                    VerDiccionario infoScreen = new VerDiccionario(listapalabra,_app, palabras_hash,palabras_hash_video);
                _app.pushScreen(infoScreen);
                
                }
            return true;
        }
            return super.keyChar(key, status, time);
    }
    
    public boolean invokeAction(int action)// esta funcion se ejecuta cuando se unde la bolita o trackball del Blacberry
    {        
       switch(action)
        {
            case ACTION_INVOKE: // Trackball click
                if( BuscarCampo.isFocus() ){//si se esta en buscar campo entonces
                    IrVideo();//se va a IrVideo
                    return true;
                    }
        }    
        return  super.invokeAction(action);
    }   
    
    
    public  Vector leer_categoria(){    
        InputStream stream = getClass().getResourceAsStream("/Data/palabras.txt");
        LineReader lineReader = new LineReader(stream);                               
        Integer num_cat,num_pal;
        String num,categoria,sub_categoria,palabrita,line,pala_sin;
        int i,j,k,comma;
        Vector palabras = new Vector();
        categorias = new Vector();    
        try
        {        
            num = new String( lineReader.readLine() );
            num_cat = Integer.valueOf( num );
             
            for(i=0 ; i< num_cat.intValue() ; i++){
               categoria = new String(lineReader.readLine());
               int num_sub = Integer.parseInt( new String(lineReader.readLine()) );
               Vector sub_palabras = new Vector();
                   
                for(j=0 ; j < num_sub ; j++){
                    line = new String(lineReader.readLine());
                    comma = line.indexOf(',');
                    
                        if( comma == -1 ){
                            pala_sin = u.quitar_carac_espec( line );
                            palabras.addElement( new Palabra(line) );
                            sub_palabras.addElement( new Palabra(line) );
                            palabras_hash.put(pala_sin, line);
                            palabras_hash_video.put( pala_sin ,"");
                        }
                        else{
                            String pala = line.substring(0,comma);
                            pala_sin = u.quitar_carac_espec( pala );
                            String video = line.substring(comma+1,line.length());
                            palabras.addElement( new Palabra(pala) );
                            sub_palabras.addElement( new Palabra(pala) );
                            palabras_hash.put(pala_sin, pala);
                            palabras_hash_video.put( pala_sin ,video);
                        }
                 }                
                 Pila_Subcategoria c = new Pila_Subcategoria(categoria,sub_palabras);
                 categorias.addElement( c );
            }
        }
        catch(EOFException eof)
        {
            System.out.println("Error leyendo los datos");
        }
        catch(IOException ioe)
        {
            System.out.println("Error leyendo los datos");
                    
        } 
        return palabras;
    }

    public void fieldChanged(Field field, int context)//esto sucede cuando se le da al trackball ubicado en un boton 
    {        
        if( field == twitter ){
                new VerTwitter();
            
        }
        else if( field == facebook ){
                new VerFacebook();
            
        }
        else if(field == catalogo){
                VerCategoria infoScreen = new VerCategoria(categorias,palabras_hash,palabras_hash_video,_app,listapalabra);
                _app.pushScreen(infoScreen); 
                    
        }
        else if(field == info){
                VerInfo infoScreen = new  VerInfo();
                _app.pushScreen(infoScreen);             
            
        }
        else if(field == dicci){
                VerDiccionario infoScreen = new VerDiccionario(listapalabra,_app, palabras_hash,palabras_hash_video);
                _app.pushScreen(infoScreen);             
            
        }
        else if(field == masvisto){
                new VerVisto();
                
        }
    }
   
   public void IrVideo(){
       
        String buscar = u.quitar_carac_espec( BuscarCampo.getText().toString() );//se toma el texto del campo editable "BuscaCampo"                      
        if( palabras_hash.containsKey( buscar ) == true ){//aki se verifica si la palabra existe en la lista de palabras,
                                                            // esto seria falso tambien en caso de que el campo de busqueda este vacio
            
            String pala_correcta = ( palabras_hash.get(buscar) ).toString();//aki ya la palabra con el formato deseado de escritura
                                                                            //es decir sin acentos y sin caracteres especiales

            final String localName;
            final String remoteName =( palabras_hash_video.get(buscar) ).toString();
            
            if(remoteName.equals("")){
                Dialog.alert("El video no está en la base de datos");  
            }
            /*else if(remoteName.charAt(0) == 'h' ){    //si todo bien la palabra existe y eso
                descargar_video descargador;//se declara el hilo descargador mas aun no se lanza
                localName = "file:///SDCard/BlackBerry/temporalNeoEssentia/"+buscar+".mp4";
                FileConnection file;
                 try {
                    file = (FileConnection) Connector.open(localName);//se establece una conexion con el archivo a abrir
                    if(  file.exists() ){
                        file.setWritable(true);
                        file.delete();
                        //se hace escribible y leible para luego ser borrado
                    }
                } catch (IOException e) { } catch (Exception e) {}
                
                descargador = new descargar_video(remoteName,localName,buscar);//se lanza el hilo
                // y acontinuacion se llama a la clase VerVideo donde se hace todo lo necesario para ver el video una vez descargado
                VerVideo infoScreen = new VerVideo( pala_correcta , localName ,_app,palabras_hash,palabras_hash_video,listapalabra,descargador );
                BuscarCampo.setText("");//se limpia el campo de texto
                _app.pushScreen(infoScreen);//y se muestra la pantalla en el dispositivo de la clase VerVideo
            }*/
            else{    //prueba
                                
                VerVideo infoScreen = new VerVideo( pala_correcta , remoteName ,_app,palabras_hash,palabras_hash_video,listapalabra,null );
                BuscarCampo.setText("");//se limpia el campo de texto
                _app.pushScreen(infoScreen);//y se muestra la pantalla en el dispositivo de la clase VerVideo
            }
        }
        else{
            Dialog.alert("No se encontró la palabra");
        }       
    }
   
    public boolean onClose()//aqui ya se esta cerrando la aplicacion
    {
        try {
            FileConnection file = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/");//se abre una conexion con la 
            //carpeta temporal temporalNeoEssentia, esto con el fin de borrarla.
             if (file.exists()){//si existe, lo cual TIENE que pasar, sino... es un problemon...
                   while(file.list().hasMoreElements()){//se borran en este while todos los videos que se encuentren adentro
                        FileConnection temp = (FileConnection) Connector.open("file:///SDCard/BlackBerry/temporalNeoEssentia/"+file.list().nextElement().toString());
                        temp.delete();
                        temp.close();
                   }//una ves ya la carpeta vacia
                   file.delete();//se borra la carpeta
             }
             file.close();//y se cierra la conexion con dicha carpeta
        }catch (IOException ex) {  }
        Dialog.alert("Vuelve pronto");//ya que el close es un evento que se lanza en un hilo se puede llamar trankilamente a Dialog.Alert que 
        //muestra un cuadrito con el texto vuelve pronto
        System.exit(0);//y se cierra finalmente el programa

        return true;
    }
}


