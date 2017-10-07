package grafi;

import java.util.ArrayList;
/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/

public class Dijkstra {
  
    MinPriorityQueue<Vertex> queue;
    ArrayList<Vertex> s;
    ArrayList<Vertex> q;

    /**
     * 
     * Dopo l'esecuzione dell'algoritmo i vertici 
     * contengono la distanza minima dalla radice
     * 
     * @param graph grafo con nodi adiacenti
     * @param source radice, nodo di partenza
     * 
     * s -> nodi esplorati
     * q -> nodi da aggiungere alla coda di priorità
     * queue -> preority queue, contiente tutti i nodi di q 
     */
    public Dijkstra(Graph graph, Vertex source){

        inizializeSingleSource(graph, source);
        s = new ArrayList<Vertex>();
        q = new ArrayList<Vertex>();
        for(int i =0; i< graph.vertices.size(); i++){
            q.add(graph.vertices.get(i));
        }        
        queue = new MinPriorityQueue<Vertex>(q);
        Vertex u;
        Vertex v;
        double w;
        int index =0;        
        while(!queue.isEmpty()){
            index++;
            if(index%1000==0)
                System.out.println(index);            
            u = queue.extractMin();
            s.add(u);
            for(int i=0; i<graph.adjacency.get(u.name).size() ; i++){
                v = graph.adjacency.get(u.name).get(i).destination;
                w = graph.adjacency.get(u.name).get(i).distance;
                relax(u ,v, w);
            }
        }
    }

    /**
     * inizializza tutti i nodi del grafo al valore massimo Double.MAX_VALUE
     * e la radice source ha peso weight = 0
     *    
     * @param graph grafo che deve essere inizializzato
     * @param source nodo di partenza di tipo Vertex
     */
    public void inizializeSingleSource(Graph graph, Vertex source){
        for(int i=0; i<graph.vertices.size(); i++){
            if(graph.vertices.get(i).name.equalsIgnoreCase(source.name)){
                graph.vertices.get(i).weight = 0;
                graph.vertices.get(i).parent = null;
            }
            else{
                graph.vertices.get(i).weight = Double.MAX_VALUE;
                graph.vertices.get(i).parent = null;
            }
        }
    }
    
    /**
     * Trova il minore tra i pesi dei nodi e degli archi raggiunti
     * 
     * @param u nodo di partenza
     * @param v nodo di uscita
     * @param w peso da comparare, weight
     */
    public void relax(Vertex u, Vertex v, double w){
        if(v.weight > (u.weight+w)){
            v.weight = u.weight+w;
            v.parent = u;
        }
    }
    
    /**
     * Trova nel grafo il nodo destination e il suo peso
     * 
     * @param destination nodo da trovare
     * @return  weight di destination
     *          -1 se non è presente
     */
    public double distance(Vertex destination){
        for(int i=0; i< s.size(); i++){
            if(s.get(i).name.equalsIgnoreCase(destination.name)){
                return s.get(i).weight;
            }
        }
        return -1.0;
    }
    /**
     * Calcola il percorso minimo tra i vertici source e destination
     * stampa la distanza e il percorso
     * 
     * @param g grafo che contiene source e destination
     * @param source 
     * @param destination
     * 
     */
    public static void findRoute(Graph g, String source, String destination){
        
        ArrayList<String> route = new ArrayList<String>();
        Vertex sour = g.find(source);
        Vertex dest = g.find(destination);
        if(sour == null || dest == null){
            System.out.println("Source and/or destiantion not found");
            return;
        }
        
        Dijkstra dj = new Dijkstra(g,sour);	
        Vertex p;
        p = dj.find(dj, destination);
        if(p!=null){
            while(p.parent != null){
                route.add(p.name);
                p = p.parent;
            }
            route.add(source);
        }

        if(route.size()<2){
            System.out.println("No route from " + source + " and " + destination );
        }
        else{
            System.out.println("The route is long: "+ dj.distance(dest)/1000 + "Km");
            for(int j = route.size()-1;  j >= 0; j-- ){
                System.out.println("-> "+ route.get(j));
            }
        }
    }

    /**
     * Trova e restituisce il vertice in un grafo con path minimo
     *
     * @param d 
     * @param name nome del vertice
     * @return Vertex con nome name
     */
    public Vertex find(Dijkstra d, String name){		
        for(int i=0; i<d.s.size(); i++){
            if(d.s.get(i).name.equalsIgnoreCase(name)){
                return d.s.get(i);
            }
        }
        return null;		
    }

}
