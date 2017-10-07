
import dictionary.BinSearchTree;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 */
public class BinSearchTreeTest {
	
    Comparator<Integer> intComparator;
    BinSearchTree<Integer> albero0;
    BinSearchTree<Integer> alberoNull;
    BinSearchTree<Integer> albero;
    private class IntegerComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return i1.compareTo(i2);
          }       
      }

    @Before
    public void setup(){
        intComparator = new IntegerComparator();
        albero0 = new BinSearchTree<>(intComparator, 0);

        albero = new BinSearchTree<>(intComparator,5);
        albero.insert(3);
        albero.insert(7);
        albero.insert(2);
        albero.insert(4);
        albero.insert(6);
        albero.insert(8);
    }

    @Test
    public void insertTest(){
        albero0.insert(1);
        assert(true);
    }
    @Test
    public void insertNullTest(){
        try {
            albero.insert(null);
        } catch (NullPointerException e) {
            assert(true);
        }
    }

    @Test
    public void insertionPlaceTest(){
        boolean left = intComparator.compare(albero.getRoot().getLeft().getKey(), 3)==0;
        boolean right = intComparator.compare(albero.getRoot().getRight().getKey(), 7)==0;
        assert(left&&right);
    }
    
    @Test
    public void findTest(){
        //Node<Integer> foundKey;
        int foundKey;
        foundKey = albero.getRoot().find(7);
        assert(foundKey == 7);
    }

    @Test
    public void deleteTest(){
        albero.delete(5);
        assert(!albero.contains(5));
    }

}
