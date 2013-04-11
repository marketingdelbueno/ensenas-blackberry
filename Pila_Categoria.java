package com.rim.samples.device.EnSenas;
import java.util.Stack;
import java.util.Vector;

class Pila_Categoria
{
    private String categoria;
    private int num_subcategorias;
    private Vector sub_categorias;
    
    Pila_Categoria(String nombre)
    {
      categoria = nombre;
      num_subcategorias = 0;
      sub_categorias = new Stack();      
    }
    
    String get_nombre(){
        return categoria;
        }
        
    int getnumcat(){
        
        return num_subcategorias;
       
        }
    
    Pila_Subcategoria get_subcat(int n){
        
       
        return (Pila_Subcategoria) sub_categorias.elementAt(n);
        
        }
    
    void agregar_subcat(String subc, Vector pal){
        sub_categorias.addElement( new Pila_Subcategoria( subc,pal ) );
        num_subcategorias++;
        }
        
  
}

