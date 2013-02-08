package com.rim.samples.device.enSenas;
//una clase con un poco e atributos y metodos para obterne estos atributos
class Palabra
{
    private String categoria,sub_categoria;
    
    private String palabra;
    
    Palabra(String p)
    {
        palabra = p;
    }
    
    public String toString()
    {
         return palabra;
    }
    
    String getpalabra()
    {
        return palabra;
    }        
  
}
