package sorting;

import java.util.Comparator;

/**
 *
 * @authro Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che esegue l'algoritmo di ordinamento QuickSort.
 */


public class QuickSort {
    /**
     * 
     * @param <T> tipo generico 
     * @param arr array di tipi generici
     * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
     * @param left indice sinistro
     * @param right indice destro
     */
    
    public <T> void quickSort(T[] arr, Comparator <T> comparator, int left, int right){
        int pIndex = partition(arr, comparator, left, right);
        if(left < pIndex-1)
            quickSort(arr, comparator, left, pIndex-1);
        if(pIndex+1 < right)
            quickSort(arr, comparator, pIndex+1, right);
    }
    /**
     * 
     * @param <T> tipo generico
     * @param arr array di tipi generici  
     * @param comparator oggetto che fa il matching tra l'oggetto e gli elementi dell'array
     * @param left indice sinistro
     * @param right indice destro
     * @return 
     */

    public static <T> int partition(T [] arr, Comparator <T> comparator, int left, int right){
        int i = left+1;
        int j = right;
        T pivot = arr[left];
        for(; j >= i;) {
            if(comparator.compare(arr[i], pivot ) <= 0)
                i++;
            else if(comparator.compare(arr[j], pivot ) > 0)
                j--;
            else{
                Sorting.swap((T[]) arr, i, j);
                i++;
                j--;
            }
        }
        Sorting.swap(arr,left,j);

        return j;
    }
}
