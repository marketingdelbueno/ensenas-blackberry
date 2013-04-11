package com.rim.samples.device.EnSenas;
import java.util.Stack;
import java.util.Vector;

class Pila_Subcategoria {

    private String subcategoria;
    private int num_palabras;
    private Vector Palabras;
    
    Pila_Subcategoria(String n, Vector pal) { 
    int i;
    
       subcategoria = n;
       num_palabras = pal.size();
       Palabras = new Stack();
       
      Palabras = pal;
    }

    Palabra get_palabra(int n){
        return (Palabra) Palabras.elementAt(n);
        }

    int getnumsub(){
        
        return num_palabras;
        }

        String get_nombre(){
            return subcategoria;
            }

} 
