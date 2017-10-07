package grafi;
/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 * Classe che crea e setta gli archi da un noto all'altro 
 **/
public class Edge {

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
	Vertex source;
	Vertex destination;
	double distance;
	
    public Edge(Vertex sour, Vertex dest, double dist){
        source=sour;
        destination =dest;
        distance = dist;		
    }	
}
