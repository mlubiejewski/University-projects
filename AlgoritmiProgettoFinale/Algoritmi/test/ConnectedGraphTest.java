import grafi.*;
import java.util.ArrayList;
import org.junit.Test;
/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 */
public class ConnectedGraphTest {

    @Test
    public void singleNodeTest() {
        Graph graph= new Graph();
        graph.addVertex(graph, "prova");
        ArrayList<ConnectedVertex> stronglyConnected;
        stronglyConnected = Graph.stronglyConnectedCompWeight(graph);
        System.out.println(stronglyConnected.get(0).getName());
        String [] parts = stronglyConnected.get(0).getName().split(",");
        System.out.println(parts[0]);
        assert(parts[0].equals("prova"));
    }
    @Test
    public void singleOrientedEdgeTest() {
        Graph graph = new Graph();
        graph.addEdge(graph, "prova", "prova1", 10);
        ArrayList<ConnectedVertex> stronglyConnected;
        stronglyConnected = Graph.stronglyConnectedCompWeight(graph);
        String [] parts1 = stronglyConnected.get(0).getName().split(",");
        String [] parts2 = stronglyConnected.get(1).getName().split(",");
        boolean firstElem = parts1[0].equals("prova");
        boolean secondElem = parts2[0].equals("prova1");
        assert(firstElem&&secondElem);

    }
   
}
