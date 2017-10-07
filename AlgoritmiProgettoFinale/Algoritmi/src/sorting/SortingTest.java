package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe di test di tutti gli algoritmi di ordinamento.
 */

public class SortingTest {
    public static void main(String [] args){
        Record[] record = ReadCsv.run();
        Comparator<Record> stringa = new ComparatorStringRecord();
        int n = ReadCsv.SIZE-1;
        
        //MERGE SORT TEST
        
        MergeSort sorter = new MergeSort();
        sorter.mergeSort(record, stringa, 0, n);
        
        
        //INSERTION SORT TEST
        /*
        InsertionSort sorter = new InsertionSort();
        sorter.insertionSort(record, stringa);
        */
        
        //QUICK SORT TEST
        /*
        QuickSort sorter = new QuickSort();
        sorter.quickSort(record, stringa, 0, n);       
        */
        
        //HEAP SORT TEST
        /*
        HeapSort sorter = new HeapSort();
        sorter.heapSort(record, stringa);
        */

        //STAMPA
        for (Record r : record) {
            System.out.print(r.toString());
        }
        System.out.println();  
    }
    
}
