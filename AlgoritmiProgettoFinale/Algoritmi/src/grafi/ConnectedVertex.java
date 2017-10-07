package grafi;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 */

public class ConnectedVertex {
    /**
     * Inizializza il nodo con peso e nome 
     * 
     * @param name nome del vertice di tipo String
     * @param weight peso del vertice di tipo int 
     */
    String name;
    int weight;

    public ConnectedVertex(String n, int w){
            name = n;
            weight = w;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
	
    public void printVertex(){
        System.out.println("vertex: " + this.name + ", weight: " + this.weight );
    }

}
