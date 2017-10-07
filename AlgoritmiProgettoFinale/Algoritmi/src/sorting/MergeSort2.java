package sorting;

import java.util.Comparator;
import java.util.ArrayList;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 * @param <T> tipo generico T
 * 
 **/

public class MergeSort2<T>{
    
    private Comparator<T> comparator;
    /**
     * @param  comparator Oggetto usato per confrontare due oggetti di tipo T
     */
    public MergeSort2(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void sort(ArrayList<T> array){
        if(array==null){
            return;
        }
        mergeSort(array, 0, (array.size())-1);
    }

    /**
     * 
     * @param <T> tipo generico 
     * @param array array di tipi generici     
     * @param first indice al primo elemento dell'array
     * @param last indice all' ultimo elemento dell'array
     */
    void mergeSort(ArrayList<T> array, int first, int last){
        int middle;
        if(first < last){
            middle = (first + last)/2;
            mergeSort(array, first, middle);
            mergeSort(array, middle+1, last);
            merge(array, first, middle, last );
        }
    }


   void merge(ArrayList<T> array, int first,int middle ,int last){
        int n1 = middle - first+1;
        int n2 = last - middle;
        //crea 2 nuovi vettori left e right
        ArrayList<T> left = new ArrayList<T>();
        ArrayList<T> right = new ArrayList<T>();		  

        int i,j;  //i index of left, j index of right

        for(i=0; i<n1; i++)
            left.add(array.get(first+i)); 
    		  
        for(j=0; j<n2; j++)
            right.add(array.get(middle+j+1));

        i=0;         
        j=0;
        int k=first;

        while(i<n1 && j<n2){
            if((comparator.compare(left.get(i), right.get(j)))<0 ){
                array.set(k, left.get(i));
                i++;
                k++;
            }
            else{
                array.set(k, right.get(j));		         
                j++;
                k++;
            }
        }

        while(i<n1){
           array.set(k, left.get(i));
           i++;
           k++;
        }
        while(j<n2){
            array.set(k, right.get(j));
            j++;
            k++;
        }
    } 
}
	  
