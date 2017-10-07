package dictionary;

/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 *  
 * @see HashMap
 * @param <T> Key value
 * @param <V> Value stored
 */

public class MyHash<T,V> {
    private int fullness;
    private Record<T,V>[] array;
    private int m ;

    /**
     * 
     * wrapper class for generic data
     * @param <T>
     * @param <V>
     */
    private class Record<T, V>{
        T key;
        V object;

        /**
         * Costruttore
         * @param k valore della chiave
         * @param o oggetto da mappare
         */
        public Record(T k, V o){
            key = k;
            object = o;
        }
        
        /**
         * Restituisce il valore del Record
         *
         * @return valore del record
         *         null se non definito
         */
        public V getObject(){
            return object;
        }
    }

    /**
     * Nuovo hashator
     * @param dim dimensione dell'hashmap
     */
    public MyHash(int dim){
        array = new Record[dim];
        m = dim;
        fullness=0;
    }

    /**
     * inserisce i dati nell'hashmap
     * i valori duplicati con la stessa chiave vengono sovrascritti
     *
     * @param key 
     * @param data 
     * @return true se inserito
     *         false altrimenti
     */
    public boolean insert(T key, V data){
        if(key!=null && data!=null){
            fullness++;
            double percent = fullness / array.length *100;
            // se supera il 75% di riempimento, ridimensiona
            if(percent >= 75 || fullness>=array.length){
                this.resize();
            }
            int index = this.hash(key);
            Record<T, V> tmp = new Record<T, V>(key, data);
            array[index]= tmp;
            return true;
        }
        else return false;
    }
    
    /**
     * funzione hash basata sull'hashcode di Object
     *
     * @param key chiave da usare
     * @return chiave di tipo int da usare nell'hash
     */
    private int hash(T key){
        double a= (Math.sqrt(5)-1)/2;
        Double indexD = m * ((key.hashCode() * a) % 1);
        int index = Math.abs(indexD.intValue());
        return index;
    }
    
    /**
     * Cerca e restituisce l'oggetto corrispondente alla chiave
     * @param key 
     * @return valore contenuto del record
     *         null se non è presente
     */

    public V search(T key){
        if(key == null){
            return null;
        }
        int index = this.hash(key);
        if(array[index]!=null){
            return (V) array[index].getObject();
        }
        else
            return null;

    }
    
    /**
     * cancella dall'hashmap
     *
     * @param key chiave dell'oggetto da cancellare
     */
    public void delete(T key){
        int index = this.hash(key);
        array[index]= null;
    }
    
    /**
     * Raddoppia la dimensione dell'array delle chiavi
     */
    private void resize(){
        int index;
        Record<T, V>[] tmp;
        tmp = new Record[array.length *2];
        m = tmp.length;
        for(int i=0; i< array.length; i++){
            if(array[i]!=null){
                T key = (T)array[i].key;
                index = hash( key );
                tmp[index] = array[i];				
            }			
        }
        array = tmp;
    }
}
