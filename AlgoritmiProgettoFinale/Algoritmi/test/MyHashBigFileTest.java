
import dictionary.MyHash;
import sorting.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 */

public class MyHashBigFileTest {
	
    MyHash<Integer, String> prova;
    MyHash<String, String> provaString;
    MyHash<Double, String> provaDouble;

    Record [] records;
    @Before
    public void setUp() throws Exception {
        prova = new MyHash<Integer,String>(100000);
        provaString = new MyHash<String,String>(100000);
        provaDouble = new MyHash<Double,String>(100000);

        records = ReadCsv.run();
    }
        
    @Test
    public void testInsert() {
      boolean insert = true;
        for(int i=0;i<100000;i++){
            System.out.println(i);
            if(!prova.insert(records[i].getNumber(),records[i].getWord()))
              insert = false;
            
        }
        assert(insert);
    }
    @Test
    public void testInsertDouble() {
      boolean insertDouble = true;
        for(int i=0;i<100000;i++){
            System.out.println(i);
            if(!provaDouble.insert(records[i].getLast(), records[i].getWord()))
              insertDouble = false;
        }
        assert(insertDouble);
    }
    @Test
    public void testInsertString() {
      boolean insertString = true;
        for(int i=0;i<100000;i++){
            System.out.println(i);
            if(!provaString.insert(records[i].getWord(), records[i].getWord()))
              insertString = false;
        }
        assert(insertString);
    }
    @Test
    public void testFindInt() {
        for(int i=0;i<100000;i++){
            System.out.println(i);
            prova.insert(records[i].getNumber(), records[i].getWord());
        }
        for(int i=0;i<100000;i++){
            System.out.println(i);
            prova.search(records[i].getNumber());
        }
        assert(true);
    }
    @Test
    public void testFindDouble() {
        for(int i=0;i<100000;i++){
            System.out.println(i);
            provaDouble.insert(records[i].getLast(), records[i].getWord());
        }
        for(int i=0;i<100000;i++){
            System.out.println(i);
            provaDouble.search(records[i].getLast());
        }
        assert(true);
    }
    @Test
    public void testFindString() {
        for(int i=0;i<100000;i++){
            System.out.println(i+" "+records[i].getWord().hashCode());
            provaString.insert(records[i].getWord(),records[i].getWord());
        }
        for(int i=0;i<100000;i++){
            System.out.println(i);
            provaString.search(records[i].getWord());
        }
        assert(true);
    }

    
    @Test
    public void testDelete() {
        for(int i=0;i<100000;i++){
            prova.insert(records[i].getNumber(), records[i].getWord());
        }
        for(int i=0;i<50000;i++){
            prova.delete(records[i].getNumber());
        }
        assert(true);
    }
    @Test
    public void testDeleteDouble() {
        for(int i=0;i<100000;i++){
            provaDouble.insert(records[i].getLast(), records[i].getWord());
        }
        for(int i=0;i<50000;i++){
            provaDouble.delete(records[i].getLast());
        }
        assert(true);
    }
    @Test
    public void testDeleteString() {
        for(int i=0;i<100000;i++){
            provaString.insert(records[i].getWord(), records[i].getWord());
        }
        for(int i=0;i<50000;i++){
            provaString.delete(records[i].getWord());
        }
        assert(true);
    }
}
