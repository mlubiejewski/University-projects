
package sorting;

import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe utilizzata per comparare i campi versus (secondo campo di records) di Record.
 */

public class ComparatorInteger implements Comparator<Record>{
    
    @Override
    public int compare(Record r1, Record r2){
        return (r2.getVersus()).compareTo(r2.getVersus());
    }
            
}
