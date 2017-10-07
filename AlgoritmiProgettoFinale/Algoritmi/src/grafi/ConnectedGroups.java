package grafi;

import java.util.ArrayList;
/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/
public class ConnectedGroups {

    //dato grafo con 1 nodo -> 1
    //dato grafo con 1 solo arco orientato 1 1
    //dato grafo con 1 arco doppio 2

    public static void main(String[] args) {
        Graph gr= new Graph();
        gr = GraphItaly.readFile();
/*
        System.out.println("n° vertex: "+gr.vertices.size());
        ArrayList<ConnectedVertex> stronglyConnected;
        stronglyConnected = Graph.stronglyConnectedCompWeight(gr);
        System.out.println("n° strongly connected vertex: "+stronglyConnected.size());
        for(int i=0; i<stronglyConnected.size();i++){
            stronglyConnected.get(i).printVertex();
        }
*/
        //Vertex source = gr.find("borsoi");
        Vertex source = gr.find("torino");
        System.out.println(source);
        Dijkstra d= new Dijkstra(gr, source);
        //Vertex destination = gr.find("chies d'alpago");
        Vertex destination = gr.find("catania");
        double dist =d.distance(destination)/1000;
        System.out.println(source.name+" "+destination.name+" "+dist);
        //dijkstra trova il cammino minimo
        Dijkstra.findRoute(gr, "Torino", "borsoi");
    }
}
