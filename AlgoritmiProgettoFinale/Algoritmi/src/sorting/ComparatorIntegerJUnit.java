package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe utilizzata per eseguire i test in JUnit.
 */
public class ComparatorIntegerJUnit implements Comparator<Integer>{
    
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
