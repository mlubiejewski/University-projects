package grafi;


import java.util.ArrayList;
import java.util.Comparator;
import sorting.MergeSort2;
/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 * Priority Queue dei minimi usato per confrontare i vertici 
 * 
 * @param <T>
 * 
 * @see Dijkstra
 * @see Vertex
 * 
 */
public class MinPriorityQueue<T> {
    /**
     * classe privata usata per comparare i vertici
     * 
     * @see Comparator
     */
    private class VertexComparator  implements Comparator<Vertex>{

        @Override
        public int compare(Vertex o1, Vertex o2) {
            return o1.compareTo(o2);
        }
    }

    ArrayList<Vertex> list;
    VertexComparator comparator;
    MergeSort2<Vertex> sorter;

    /**
     * Crea la lista
     * 
     * @param list ArrayList passata come parametro la lista che verrà inizializzata
     */
    public MinPriorityQueue(ArrayList<Vertex> list){
        this.list =list;

        comparator = new VertexComparator();
        sorter = new MergeSort2<Vertex>(comparator);
        calculatePriority();
    }

    public void calculatePriority(){
        sorter.sort(list);

    }
    /**
     * estrae il primo elemento della lista e ne calcola la priorità          
     * 
     * @return vertice minimo di tipo Vertex         
     */
    public Vertex extractMin(){
        Vertex v = list.remove(0);
        calculatePriority();

        return v;

    }
    /**
     * Dice se è vuoto 
     * 
     * @return true se è vuoto
     *         false altrimenti
     */
    public boolean isEmpty(){
            return list.isEmpty();
    }
}


