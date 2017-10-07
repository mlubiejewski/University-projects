
package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe utilizzata per comparare i campi last (ultimo campo di records) di Record.
 */

public class ComparatorDouble implements Comparator<Record> {
    
    @Override
    public int compare(Record r1, Record r2){
        return (r1.getLast()).compareTo(r2.getLast());
    }
    
}
