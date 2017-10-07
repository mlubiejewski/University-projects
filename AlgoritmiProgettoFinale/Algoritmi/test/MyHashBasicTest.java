import dictionary.MyHash;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 */

public class MyHashBasicTest {

    MyHash<Integer, String> map;
    MyHash<Integer, String> map2;

    @Before
    public void setUp() {
        map = new MyHash<Integer, String>(5); 
        map2 = new MyHash<Integer, String>(5); 
        map2.insert(1, "null");
        map2.insert(2, "Blu");
        map2.insert(3, "Rosa");
    }

    @Test
    public void insertTest() {
        assertEquals(true,map.insert(1, "Blu") );
    }

    @Test
    public void insertNullKeyTest() {
        assertEquals(false,map.insert(null, "Rosa") );
    }

    @Test
    public void insertNullDataTest() {
        assertEquals(false,map.insert(2, null) );
    }

    @Test
    public void searchNullKeyTest() {
        assertEquals(null,map2.search(null) );
    }

    @Test
    public void searchNonexistenceKeyTest() {
        assertEquals(null,map2.search(4) );
    }

    @Test
    public void searchExistentKeyTest() {
        assertEquals("Rosa",map2.search(3) );
    }


    @Test
    public void searchExistentKeyAfterResizeTest() {
        map2.insert(4, "aaa");
        map2.insert(5, "bbb");
        map2.insert(6, "ccc");
        assertEquals("Rosa",map2.search(3) );
    }

}
