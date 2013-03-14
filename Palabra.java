package com.rim.samples.device.EnSenas;

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
