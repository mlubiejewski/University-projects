package grafi;

import java.util.ArrayList;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/

public class ConnectedComponents {

    //dato grafo con 1 nodo -> 1
    //dato grafo con 1 solo arco orientato 1 1
    //dato grafo con 1 arco doppio 2

    public static void main(String[] args) {
        Graph gr = new Graph();

        gr = GraphItaly.readFile();

        System.out.println("n° vertex: "+gr.vertices.size());
        ArrayList<ConnectedVertex> stronglyConnected;
        stronglyConnected = Graph.stronglyConnectedCompWeight(gr);
        System.out.println("n° strongly connected vertex: "+stronglyConnected.size());

        for(int i=0; i<stronglyConnected.size();i++){
            stronglyConnected.get(i).printVertex();
        }		   	   
    }
}
