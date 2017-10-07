package grafi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import sorting.MergeSort2;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/

public class Graph {
    ArrayList<Vertex> vertices;
    HashMap<String,ArrayList<Edge>> adjacency;
    int time;

    public Graph(){
        vertices = new ArrayList<Vertex>();
        adjacency = new HashMap<String,ArrayList<Edge>>();
    }
    
    /**
     * 
     * Aggiunge un vertice di nome name al grafo graph  
     * 
     * @param graph grafo a cui aggiungere il vertice di tipo Graph
     * @param name nome del vertice di tipo String
     */
    public void addVertex(Graph graph,String name){
        Vertex v = new Vertex(name);
        graph.vertices.add(v);
        graph.adjacency.put(name, new ArrayList<Edge>());
    }
    
    /**
     * Cerca il vertice nel grafo 
     * 
     * @param name nome del nodo da trovare
     * @return  l'oggetto vertice se abbiamo trovato il nodo 
     *          null altrimenti
     */
    public Vertex find(String name){
        for(int i=0; i<this.vertices.size(); i++){
            if(this.vertices.get(i).name.equalsIgnoreCase(name)){
                return this.vertices.get(i);
            }
        }
        return null;
    }
    
    /**
     * Dice se il grafo contiene o meno un nodo name 
     * 
     * @param graph grafo in cui avviene la ricerca
     * @param name nome del vertice da cercare
     * @return  True se vertice è contenuto
     *          False altrimenti
     */
    
    public boolean contains(Graph graph,String name){
        if(find(name)!=null){
            return true;
        }
        return false;
    }

    /**
     * Aggiunge un arco orientato al grafo
     * 
     * source -> destination
     * @param grafo dove inserire l'arco di tipo Graph
     * @param source nodo di partenza tipo Vertex
     * @param destination nodo di arrivo tipo Vertex
     * @param distance peso o costo in tempo dell'arco
     */
    
    public void addEdge(Graph grafo,String source, String destination, double distance){
        if(grafo.find( source)==null){
            grafo.addVertex(grafo,source);
        }
        if(grafo.find( destination)==null){
            grafo.addVertex(grafo,destination);
        }
        Edge a = new Edge(grafo.find(source),grafo.find(destination) , distance);
        grafo.adjacency.get(source).add(a);
    }

    /**
     * Aggiunge un arco non orientato nel grafo 
     * 
     * source->destination
     * source<-destination
     * @param graph dove inserire l'arco di tipo Graph
     * @param source uno dei nodi di tipo Vertex
     * @param destination l'altro nodo di tipo Vertex
     * @param distance peso o costo in tempo dell'arco tra i due nodi
     */
    
    public void addDoubleEdge(Graph graph,String source, String destination, double distance){
        graph.addEdge(graph, source, destination, distance);
        graph.addEdge(graph, destination, source, distance);
    }	
    
    /**
     * Restituoisce i nodi adiacenti di un vertice Vertex
     * 
     * @param graph grafo di tipo Graph dove trovare la lista delle adiacenze
     * @param vertex nome del nodo Vertex
     * @return  lista di Edges dove la radice è vertex 
     *          void se il vertice non ha nodi adiacenti
     */
    
    public ArrayList<Edge> neighbors(Graph graph,String vertex){
        ArrayList<Edge> neighbors = null;
        if(graph.adjacency.containsKey(vertex)){
            neighbors = graph.adjacency.get(vertex);
        }
        return neighbors;
    }
    
    /**
    * Stampa i vertici del grafo e gli archi usando la ricerca Deep First Search
    * Funzione per la ricerca
    * 
    * @param graph grafo da esplorare Graph
    */
    public  void dfs(Graph graph){
        time=0;        
        for(int i=0; i<graph.vertices.size(); i++){                
            graph.vertices.get(i).color="WHITE";
            graph.vertices.get(i).parent=null;	           
        }
        
        for(int i=0; i<graph.vertices.size(); i++){
            if(graph.vertices.get(i).color.equalsIgnoreCase("WHITE")){
                dfsVisit(graph,graph.vertices.get(i));
            }
        }
    }
    
    /**
    * dfs ricorsiva
    * 
    * @param graph 
    * @param vertex
    */
    private void dfsVisit(Graph graph, Vertex vertex) {
        time = time+1;
        vertex.d= time;
        vertex.color = "GRAY";
        ArrayList<Edge> adiacentiA = neighbors(graph,vertex.name);              
        for(int i=0; i<adiacentiA.size(); i++){
            Vertex tmp = adiacentiA.get(i).getDestination();
            if(tmp.color.equalsIgnoreCase("WHITE")){
                tmp.parent = vertex;
                dfsVisit(graph,tmp);
            }
        }
        vertex.color ="BLACK";
        time=time+1;
        vertex.f= time;
    }
   
    /**
     * Crea un grafo trasposto di g
     * 
     * @param g il grafo che voglio trasporre
     * @return grafo trasposto con gli archi invertiti di tipo Graph
     */
    public static Graph transposed(Graph g){
        Graph transpoted = new Graph();
        for(int i=0; i<g.vertices.size(); i++){
            transpoted.vertices.add(g.vertices.get(i));
            transpoted.adjacency.put(g.vertices.get(i).name, new ArrayList<Edge>());
        }
        Iterator<Entry<String, ArrayList<Edge>>> it = g.adjacency.entrySet().iterator();
        while(it.hasNext()){
            Entry<String,ArrayList<Edge>> edgeList =  (Entry<String, ArrayList<Edge>>) it.next();
            for(int i=0; i<edgeList.getValue().size(); i++){
                Edge tmp = edgeList.getValue().get(i);
                transpoted.addEdge(transpoted, tmp.destination.name, tmp.source.name, tmp.distance);
            }			
        }
        return transpoted;
    }

    /** 
     * Metodo che calcola la componente fortemente connessa del grafo
     * 
     * @param g il grafo di input
     * @return ArrayList di tipo ConnectedVertex in cui ogni vertice è una componente fortemente connessa di g
     */
    public static ArrayList<ConnectedVertex> stronglyConnectedCompWeight(Graph g){
        System.out.println("Executing Strongly connected");
        Graph transposted;
        ArrayList<ConnectedVertex> stronglyConnected;
        System.out.println("Executing dfs");                
        g.dfs(g);
        System.out.println("Executing tr");
        transposted = Graph.transposed(g);        
        Graph.decrescentOrderBlackTime(transposted);
        transposted.dfs(transposted);
        Graph.orderGrayTime(transposted);
        stronglyConnected = mergeVertex(transposted);
        return stronglyConnected;
    }

    /**
     * Unisce i vertici che sono parte dell'albero di vertici fortemente connessi
     * 
     * @param t il grafo trasposto dopo la visita in dfs con i vertici ordinati per tempo di scoperta
     * @return ArrayList di Connectedvertex, ogni vertice è una componente fortemente connessadi t
     */
    public static ArrayList<ConnectedVertex> mergeVertex( Graph t){
        ArrayList<ConnectedVertex> merged = new ArrayList<ConnectedVertex>();

        for(int i=0;i<t.vertices.size();i++){
            String newName=t.vertices.get(i).name;
            int k=i;
            int count=1;
            int z =0;
            for(int j=1; j<t.vertices.size();j++){				
                if(t.vertices.get(j).d > t.vertices.get(k).d && t.vertices.get(j).f < t.vertices.get(k).f){
                    if(z<5){
                        newName = newName +", "+  t.vertices.remove(j).name;
                        z++;
                    }
                    else{
                       t.vertices.remove(j);
                    }
                    count++;
                    j--;					
                }
            }
            newName = newName +", etc.";
            ConnectedVertex tmp = new ConnectedVertex(newName, count);
            merged.add(tmp);
        }
        return merged;		
    }
    
    /**
     * Ordine decrescente dei vertici del grafo
     * confrtonta il tempo nel quali i vertici sono completamente esplorati 
     * 
     * @param g il grafo di input Graph
     */
    public static void decrescentOrderBlackTime(Graph g){
        class VertexComparatorTime implements Comparator<Vertex>{
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return  Vertex.descendingCompareFinalTime(o1, o2);
            }
        }

        VertexComparatorTime comparator = new VertexComparatorTime();
        MergeSort2<Vertex> sorter = new  MergeSort2<Vertex>(comparator);
        sorter.sort(g.vertices);	
    }

    /**
     * Ordine dei vertici di un grafo 
     * confrontati con il tempo nel quale un grafo è stato scoperto
     * 
     * @param g il grafo di input Graph
     */
    public static void orderGrayTime(Graph g){
        class VertexComparatorTime implements Comparator<Vertex>{

            @Override
            public int compare(Vertex o1, Vertex o2) {
                return  Vertex.compareFindTime(o1, o2);
            }
        }

        VertexComparatorTime comparator = new VertexComparatorTime();
        MergeSort2<Vertex> sorter = new  MergeSort2<Vertex>(comparator);
        sorter.sort(g.vertices);	
    }
}

