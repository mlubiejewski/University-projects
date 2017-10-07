import org.junit.Test;
import grafi.*;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 **/
public class GraphTest {
   
    @Test
    public void testAddVertex() {
        Graph graph = new Graph();
        graph.addVertex(graph, "a");
        boolean found = graph.contains(graph, "a");
        assert(found);
    }
    
    @Test
    public void testFindNullNode() {
        Graph graph = new Graph();
        assert(graph.find("a")==null);
    }
    
    @Test
    public void testContainsNot() {
        Graph graph = new Graph();
        assert(!graph.contains(graph, "a"));
    }
        
    @Test
    public void testAddEdge() {
        Graph graph = new Graph();
        graph.addEdge(graph, "a", "b", 12);
        assert(graph.contains(graph, "a")&&graph.contains(graph, "b"));
    }
  
        @Test
    public void testDfs() {
        System.out.println("dfs");
        Graph g = new Graph();
        g.addEdge(g, "Torino", "Milano", 150.0);
        g.addEdge(g, "Torino", "Venezia", 300.0);
        g.addEdge(g, "Milano", "Savona", 180.0);
        g.addEdge(g, "Milano", "Torino", 200.0);
        Graph instance = new Graph();
        instance.dfs(g);                        
    }
}
