package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che svolge l'algoritmo di ordinamento HeapSort 
 */

public class HeapSort {
  /**
   * 
   * @param <T> tipo generico 
   * @param arr array di tipi generici  
   * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
   * 
   */  
    public <T> void heapSort(T[] arr, Comparator <T> comparator ){
        int heapSize = arr.length-1;
        buildHeap(arr, comparator, heapSize);
        for(int i = arr.length-1; i > 0; i--){
            Sorting.swap(arr, 0, i);
            heapSize--;
            heapify(arr, comparator, 0, heapSize);
        }
    }
    
   /**
    * 
    * @param <T> tipo generico 
    * @param arr array di tipi generici  
    * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
    * @param heapSize lunghezza dell'array
    */
  
    public  static <T> void buildHeap(T[] arr, Comparator <T> comparator, int heapSize){  
        for(int i = arr.length/2; i >=0 ; i--)
            heapify(arr, comparator, i, heapSize);
    }
   
   /**
    * 
    * @param <T> tipo generico 
    * @param arr array di tipi generici  
    * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
    * @param heapSize lunghezza dell'array
    * @param i indice dell'elemento passato
    */
    
    public static <T> void heapify(T[] arr, Comparator <T> comparator, int i, int heapSize){
        int left = 2*i;
        int right = 2*i + 1;
        int largest = i;
        if(left <= heapSize && comparator.compare(arr[left], arr[i] ) > 0)
            largest = left;
        else
            largest = i;
        if(right <= heapSize && comparator.compare( arr[right], arr[largest] ) > 0)
            largest = right;
        if(largest != i){
            Sorting.swap(arr, i, largest);
            heapify(arr, comparator, largest, heapSize);
        }
    }
}
