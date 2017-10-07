package sorting;

import java.util.Comparator;

/**
 * 
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che applica l'algoritmo di ordinamento InsertionSort
 */

public class InsertionSort {
    /**
     *
     * @param <T> tipo generico
     * @param arr array di tipi generici
     * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
     */
    public <T> void insertionSort(T[] arr, Comparator <T> comparator){            
        for(int i = 1; i < arr.length; i++){
            T x = arr[i];
            int j = i;            
            while(j > 0 && comparator.compare(x, arr[j-1]) < 0){
                arr[j] = arr[j-1];
                j--;
            }            
            arr[j] = x;
        }   
    }
}
    

