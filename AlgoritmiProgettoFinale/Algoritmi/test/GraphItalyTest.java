import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import grafi.*;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 */

public class GraphItalyTest {
    Graph gf1;
    @Before
    public void setup() {
        //crea il grafo
        gf1 = GraphItaly.readFile();
    }
    
    /**
     * test creazione grafo
     */
    @Test 
    public void creaGrafo(){ 
        assert(gf1!=null);
    }
    
    /**
     * test dijkstra
     */
    @Test 
    public void dijkstra(){
        Vertex source = gf1.find("torino");
        double km=0;
        if(source != null){
            Dijkstra dij = new Dijkstra(gf1, source);

            Vertex destination1 = gf1.find("catania");
            if(destination1!= null){
                km = dij.distance(destination1)/1000;
                System.out.println(km);
            }
            else{
                fail("destination not found");
            }
        }
        else{
            fail("source not found");
        }
        assert(km>1207 && km<1208);
    }

}
