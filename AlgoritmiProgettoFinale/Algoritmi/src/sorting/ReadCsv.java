package sorting;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che legge dal file records.csv
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
Creazione della classe ReadCsv con un una costante SIZE che indica la grandezza 
dell'array di Record nel quale mettiamo SIZE caratteri letti da un file.
*/
public class ReadCsv {    
    //final static int SIZE = 2000000;
    final static int SIZE = 1000000; // QUICKSORT
    public static void main(String [] args){
        ReadCsv obj = new ReadCsv();
        Record [] record = obj.run();
    }
    
    /**
     * @return Record[] oggetto di array di Record  
     */
    public static Record[] run(){
        String csvFile;
        csvFile = "C://Users/mateusz93/Documents/Algoritmi2015-2016/records.csv";
        //csvFile = "C://Users/Sayer/Desktop/ALGORITMI/records.csv";
        //csvFile = "C://Users/Joana Minja/Desktop/records.csv";
        BufferedReader br = null;
        String line = "";
        Record[] array = new Record[SIZE];
        int i = 0;
        try{
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && i<SIZE) {
                String [] parts = line.split(",");
               
                int num = Integer.parseInt(parts[0]);
                int versus = Integer.parseInt(parts[2]);
                double last = Double.parseDouble(parts[3]);
                array[i] = new Record(num, parts[1], versus, last);
                i++;
            }  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
        return array;
    }
}
