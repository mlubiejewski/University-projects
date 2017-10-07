package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che esegue l'algoritmo di ordinamento MergeSort basato sulla tecnica divid et impera.
 */

/*
MergeSort divide in due l'array, ordina la prima metà, ordina la seconda metà
poi fa il merge delle due, unisce i due array ordinati
*/
public class MergeSort{
    /**
     * 
     * @param <T> tipo generico 
     * @param arr array di tipi generici
     * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
     * @param first indice al primo elemento dell'array
     * @param last indice all' ultimo elemento dell'array
     */
    public <T> void mergeSort(T[] arr, Comparator<T> comparator, int first, int last){    
        if(first < last){
            int middle = (first+last)/2;        
            mergeSort(arr, comparator,  first, middle);        
            mergeSort(arr, comparator, middle+1, last);       
            merge(arr, comparator, first, middle, last);
        }
    }
  
  /**
   * 
   * @param <T> tipo generico   
   * @param arr array di tipi generici
   * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
   * @param first indice al primo elemento dell'array
   * @param middle indice a metà dell'array
   * @param last indice all' ultimo elemento dell'array
   * 
   */
  
  /*
  n --> lunghezza dell'array
  i --> prima posizione
  j --> metà dell'array
  k --> indice dell'array temp che è l'array temporaneo ordinato
  */
  static <T> void merge(T[] arr, Comparator<T> comparator, int first, int middle, int last){
    int n = last - first + 1;
    int i = first;
    int j = middle + 1;
    int k = 0;
    Object [] temp = new Object [n];
      
    while(i <= middle && j <= last){
        if(comparator.compare(arr[i], arr[j])<=0) 
            temp[k++] = arr[i++];
        else 
            temp[k++] = arr[j++];
    }
    while(i <= middle)
        temp[k++] = arr[i++]; 
    while(j <= last)
        temp[k++] = arr[j++]; 
    for(k = 0; k < n; k++) 
        arr[first + k] = (T)temp[k];
    }
} 