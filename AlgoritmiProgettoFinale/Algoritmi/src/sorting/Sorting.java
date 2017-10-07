package sorting;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe di appoggio utilizzata in QuickSort e HeapSort
 */
public class Sorting{
    public static <T> void swap(T[] arr, int i, int j){
       T temp = arr[i];
       arr[i] = arr[j];
       arr[j] = temp;
    }  
}