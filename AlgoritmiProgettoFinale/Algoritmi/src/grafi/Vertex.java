package grafi;


/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/

public class Vertex implements Comparable<Vertex>{
    String name;
    String color;			//white, gray, black
    Vertex parent;
    double weight;			// the distance from a source vertex
    int d;					// the time in which a vertex is discovered
    int f;					// the time in which a vertex is fully explored
    public Vertex(String nome){
        name=nome;
        parent = null;
        color="WHITE";
        f=0;
        d=0;
    }
	
    /**	 
     * Confronta il peso, weight, dei due vertici         
     */
    @Override
    public int compareTo(Vertex o) {
        if(this.weight < o.weight){
            return -1;
        }
        if(this.weight > o.weight ){
            return 1;
        }
        return 0;
    }

    /**
     * confronta il tempo di esplorazione di due vertici 
     * 
     * @param o1 primo vertice da comparare
     * @param o2 secondo vertice da comparare
     * @return 0 se il tempo è lo stesso
     *         1 se o1 è minore di o2
     *        -1 se o2 è più grande di o1
     */
    public static int descendingCompareFinalTime(Vertex o1, Vertex o2) {
        if(o1.f<o2.f){
            return 1;
        }
        if(o1.f>o2.f){
            return -1;
        }
        return 0;
    }
	
    /**	 
     * confronta il tempo di scoperta dei due vertici 
     * @param o1 primo vertice da confrontare
     * @param o2 secondo vertice da confrontare
     * @return 0 se il tempo è lo stesso
     *         1 se o2 è migliore di o1
     *        -1 se o1 è migliore di o2
     */
    public static int compareFindTime(Vertex o1, Vertex o2) {
        if(o1.d<o2.d){
            return -1;
        }
        if(o1.d>o2.d){
            return 1;
        }
        return 0;
    }

    public boolean equalsIgnoreCase(String a) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }	
}
