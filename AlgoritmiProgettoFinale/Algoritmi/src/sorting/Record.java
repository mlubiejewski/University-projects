package sorting;

/**
 *
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 * 
 * Classe che definisce l'oggetto Record.
 */

public class Record {
    private int number;
    private String word;
    private Integer versus;
    private Double last;
    
    /**
     * dati letti dal file
     * @param num primo campo di records.csv rappresentato da un numero intero univoco 
     * @param word secondo campo di records.csv sono le parole del testo di tipo String
     * @param versus terzo campo di records.csv indica il verso di tipo int
     * @param last ultimo campo di records.csv di tipo double
     */
    public Record (int num, String word, int versus, double last){
        this.number = num;
        this.word = word; 
        this.versus = versus;
        this.last = last;

    }
    /**
     * Assegna al campo number dell'oggetto Record il valore number passato come parametro.
     * @param number valore da assegnare di tipo int
     */
    public void setNumber(int number) {
        this.number = number;
    }
    /**
     * 
     * @param word valore da assegnare di tipo String
     * Assegna al campo word dell'oggetto Record il valore word passato come parametro.
     */
    public void setWord(String word) {
        this.word = word;
    }
    /** 
     * Assegna al campo versus dell'oggetto Record il valore versus passato come parametro.
     * @param versus valore da assegnare di tipo Integer
     */
    
    public void setVersus(Integer versus) {
        this.versus = versus;
    }
    /**
     * Assegna al campo last dell'oggetto Record il valore last passato come parametro.
     * @param last valore da assegnare al campo last
     */
    public void setLast(Double last) {
        this.last = last;
    }
    /**
     * @return number campo dell'oggeto Record di tipo int
     */
    public int getNumber() {
        return number;
    }
    /**
     * @return word campo dell'oggeto Record di tipo String 
     */
    public String getWord() {
        return word;
    }
    /**
     * @return versus campo dell'oggeto Record di tipo Integer
     */
    public Integer getVersus() {
        return versus;
    }
    /**
     * @return last campo dell'oggeto Record di tipo Double 
     */
    public Double getLast() {
        return last;
    }
    /**
     * @return una String composta da tutti gli elementi dell'oggetto.
     */
    @Override
    public String toString() {
        return "Record{" + "number=" + number + ", word=" + word + ", versus=" + versus + ", last=" + last + "} \n";
    }
}