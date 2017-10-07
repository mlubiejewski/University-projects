import java.util.Comparator;
import org.junit.Test;
import sorting.*;
import static org.junit.Assert.*;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 */
public class TestSorting{
	
	//QUICK SORT TEST
	
	@Test
	public void testSortedIntArrayQuickSort(){
		Integer [] array = new Integer [] {-2, -1, 0, 3, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();                
		QuickSort qs = new QuickSort();
                qs.quickSort(array, x, 0, 4);                
		assertArrayEquals(array, new Integer [] {-2, -1, 0, 3, 5});
	}
	
	@Test
	public void testUnsortedIntArrayQuickSort(){
		Integer [] array = new Integer [] {-4, 5, -21, 34, 0, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                QuickSort qs = new QuickSort();                
                qs.quickSort(array, x, 0, array.length-1);	
		assertArrayEquals(array, new Integer [] {-21, -4, 0, 5, 5, 34});
	}
	
	@Test
	public void testRevIntArrayQuickSort(){
		Integer [] array = new Integer [] {5, 4, 3, 2, 1};
		ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                QuickSort qs = new QuickSort();                
                qs.quickSort(array, x, 0, array.length-1);			
		for(int i=0; i<array.length-1; i++)
			assertTrue(array[i] <= array[i+1]);
	}
	
        //MERGE SORT TEST
        
	@Test
	public void testSortedIntArrayMergeSort(){
		Integer [] array = new Integer [] {-2, -1, 0, 3, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();                
                MergeSort ms = new MergeSort();                
                ms.mergeSort(array, x, 0, array.length-1);
		assertArrayEquals(array, new Integer [] {-2, -1, 0, 3, 5});
	}
	
	@Test
	public void testUnsortedIntArrayMergeSort(){
		Integer [] array = new Integer [] {-4, 5, -21, 34, 0, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                MergeSort ms = new MergeSort();                
                ms.mergeSort(array, x, 0, array.length-1);
		assertArrayEquals(array, new Integer [] {-21, -4, 0, 5, 5, 34});
	}
	
	@Test
	public void testRevIntArrayMergeSort(){
		Integer [] array = new Integer [] {5, 4, 3, 2, 1};
		ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                MergeSort ms = new MergeSort();                
                ms.mergeSort(array, x, 0, array.length-1);			
		for(int i=0; i<array.length-1; i++)
			assertTrue(array[i] <= array[i+1]);
	}
        
        //INSERTION SORT TEST
        
	@Test
	public void testSortedIntArrayInsertionSort(){
		Integer [] array = new Integer [] {-2, -1, 0, 3, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();                
                InsertionSort is = new InsertionSort();                
                is.insertionSort(array, x);
		assertArrayEquals(array, new Integer [] {-2, -1, 0, 3, 5});
	}
	
	@Test
	public void testUnsortedIntArrayInsertionSort(){
		Integer [] array = new Integer [] {-4, 5, -21, 34, 0, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                InsertionSort is = new InsertionSort();                
                is.insertionSort(array, x);
		assertArrayEquals(array, new Integer [] {-21, -4, 0, 5, 5, 34});
	}
	
	@Test
	public void testRevIntArrayInsertionSort(){
		Integer [] array = new Integer [] {5, 4, 3, 2, 1};
		ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                InsertionSort is = new InsertionSort();                
                is.insertionSort(array, x);			
		for(int i=0; i<array.length-1; i++)
			assertTrue(array[i] <= array[i+1]);
	}
        
        //HEAP SORT TEST
        @Test
	public void testSortedIntArrayHeapSort(){
		Integer [] array = new Integer [] {-2, -1, 0, 3, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();                
                HeapSort hs = new HeapSort();                
                hs.heapSort(array, x);
		assertArrayEquals(array, new Integer [] {-2, -1, 0, 3, 5});
	}
	
	@Test
	public void testUnsortedIntArrayHeapSort(){
		Integer [] array = new Integer [] {-4, 5, -21, 34, 0, 5};
                ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                HeapSort hs = new HeapSort();                
                hs.heapSort(array, x);
		assertArrayEquals(array, new Integer [] {-21, -4, 0, 5, 5, 34});
	}
	
	@Test
	public void testRevIntArrayHeapSort(){
		Integer [] array = new Integer [] {5, 4, 3, 2, 1};
		ComparatorIntegerJUnit x = new ComparatorIntegerJUnit();
                HeapSort hs = new HeapSort();                
                hs.heapSort(array, x);			
		for(int i=0; i<array.length-1; i++)
			assertTrue(array[i] <= array[i+1]);
	}
}