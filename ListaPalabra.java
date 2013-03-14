package com.rim.samples.device.EnSenas;

import net.rim.device.api.ui.component .*;
import net.rim.device.api.collection.util.*; 
import net.rim.device.api.util.*;
import java.util.*;

class ListaPalabra extends SortedReadableList implements KeywordProvider
{
    ListaPalabra(Vector p)
    {
        super(new ListaPalabraComparator());    
                   
        loadFrom(p.elements());      
    } 
   
    public String[] getKeywords( Object element )
    {        
        if(element instanceof Palabra )
        {            
            return StringUtilities.stringToWords(element.toString());
        }        
        return null;
    }  
    
     final static class ListaPalabraComparator implements Comparator
    {   
        
        public int compare(Object o1, Object o2)
        {
            if((o1.toString().compareTo(o2.toString()))<0)
            {
                return -1;
            }
            if((o1.toString().compareTo(o2.toString()))>0)
            {
                return 1;
            }
            else
            {
                return 0;
            }
       
            
        }        
    }    
}
