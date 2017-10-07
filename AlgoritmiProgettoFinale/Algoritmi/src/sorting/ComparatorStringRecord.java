package sorting;
import java.util.Comparator;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe utlizzata per comparare word (le parole in records) di Record.
 **/ 

public class ComparatorStringRecord implements Comparator<Record>{
    
    public int compare(Record r1, Record r2){
         return r1.getWord().compareTo(r2.getWord());
    }
    
}
