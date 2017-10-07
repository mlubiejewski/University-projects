package grafi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 */


public class GraphItaly {

     /**
     * Legge e parsifica il file italian_dist_graph.csv
     * 
     * @return gf1, grafo di tipo Graph contentente tutti gli archi come doppiamente orientati 
     *         null, se il file non esiste
     */
    public static Graph readFile() {

        Graph gf1 = new Graph();
        Charset charset = StandardCharsets.UTF_8;

        Path currentRelativePath = Paths.get("./src/grafi/italian_dist_graph.csv");

        ArrayList<String> start = new ArrayList<String>();
        ArrayList<String> destination = new ArrayList<String>();
        ArrayList<Double> distance = new ArrayList<Double>();

        try (BufferedReader reader = Files.newBufferedReader(currentRelativePath, charset)) {
            String line = null;
            int i =0;
            //while ((line = reader.readLine()) != null && i<26000) {	
            while ((line = reader.readLine()) != null) {		    
                i++;

                String[] tuple = line.split(",");

                start.add(tuple[0]);
                destination.add(tuple[1]);
                distance.add(Double.parseDouble(tuple[2]));

                gf1.addDoubleEdge(gf1,tuple[0], tuple[1], Double.parseDouble(tuple[2]));
                if(i%1000==0){
                        System.out.println(i/1000+"\r");		        			        	
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        System.out.println("finito");
        
        return gf1;
    }
}
