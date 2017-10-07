import dictionary.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sorting.ReadCsv;
import sorting.Record;

/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 */

public class BinSearchTreeBigFileTest {

    private static class IntegerComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return i1.compareTo(i2);
        }		
    }
    private static class DoubleComparator implements Comparator<Double> {
        public int compare(Double i1, Double i2) {
            return i1.compareTo(i2);
        }		
    }
    private static class StringComparator implements Comparator<String> {
        public int compare(String i1, String i2) {
            return i1.compareTo(i2);
        }		
    }
    BinSearchTree<Integer> binSearchInt;
    BinSearchTree<Double> binSearchDouble;
    BinSearchTree<String> binSearchString;
    Record [] records;
    

    @Before
    public void setUp() throws Exception {
        IntegerComparator compareInt;
        DoubleComparator compareDouble;
        StringComparator compareString;
        compareInt = new IntegerComparator();
        compareDouble = new DoubleComparator();
        compareString = new StringComparator();
        binSearchInt = new BinSearchTree<Integer>(compareInt, 1);
        binSearchDouble = new BinSearchTree<Double>(compareDouble, 1.0);
        binSearchString = new BinSearchTree<String>(compareString, " ");

         records = ReadCsv.run();

        System.out.println("finito");
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testInsert() {
        for(int i = 0; i<1000;i++){                    
            binSearchInt.insert(records[i].getNumber());
        }
        assert(true);
    }
    
    @Test
    public void testInsertDouble() {
        for(int i = 0; i<1000;i++){
            binSearchDouble.insert(records[i].getLast());
        }
        assert(true);
    }
    
    @Test
    public void testInsertString() {
        for(int i = 0; i<1000;i++){   
            binSearchString.insert(records[i].getWord());
        }
        assert(true);
    }
    
    @Test
    public void testDelete() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchInt.insert(records[i].getNumber());
        }
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("delete "+i);
            }
            binSearchInt.delete(records[i].getNumber());
        }
        assert(true);
    }
    
    @Test
    public void testDeleteDouble() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchDouble.insert(records[i].getLast());
        }
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("delete "+i);
            }
            binSearchDouble.delete(records[i].getLast());
        }
        assert(true);
    }
    
    @Test
    public void testDeleteString() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchString.insert(records[i].getWord());
        }
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("delete "+i);
            }
            binSearchString.delete(records[i].getWord());
        }
        assert(true);
    }
    
    @Test
    public void testFind() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchInt.insert(records[i].getNumber());
        }
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("find "+i);
            }
            binSearchInt.findNode(records[i].getNumber());
        }
        assert(true);
    }
    
    @Test
    public void testFindDouble() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchDouble.insert(records[i].getLast());
        }
        for(int i = 0; i<10000;i++){
            if(i%100==0){
                System.out.println("find "+i);
            }
            binSearchDouble.findNode(records[i].getLast());
        }
        assert(true);
    }
    
    @Test
    public void testFindString() {
        for(int i = 0; i<1000;i++){
            if(i%100==0){
                System.out.println("insertion "+i);
            }
            binSearchString.insert(records[i].getWord());
        }
        for(int i = 0; i<10000;i++){
            if(i%100==0){
                System.out.println("find "+i);
            }
            binSearchString.findNode(records[i].getWord());
        }
        assert(true);
    }
}
