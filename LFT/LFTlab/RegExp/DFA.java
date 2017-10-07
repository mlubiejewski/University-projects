import java.util.HashSet;
import java.util.HashMap;

/**
 * Un oggetto della classe DFA rappresenta un automa a stati finiti
 * deterministico
 */
public class DFA
{
    /**
     * Numero degli stati dell'automa. Ogni stato e` rappresentato da
     * un numero interno non negativo, lo stato con indice 0 e` lo
     * stato iniziale.
     */
    private int numberOfStates;

    /** Insieme degli stati finali dell'automa. */
    private HashSet<Integer> finalStates;

    /**
     * Funzione di transizione dell'automa, rappresentata come una
     * mappa da mosse a stati di arrivo.
     */
    private HashMap<Move, Integer> transitions;

    /**
     * Crea un DFA con un dato numero di stati.
     * @param  n Il numero di stati dell'automa.
     */
    public DFA(int n) {
	numberOfStates = n;
	finalStates = new HashSet<Integer>();
	transitions = new HashMap<Move, Integer>();
    }

    /**
     * Aggiunge uno stato all'automa.
     * @return L'indice del nuovo stato creato
     */
    public int newState() {
	return numberOfStates++;
    }

    /**
     * Aggiunge una transizione all'automa.
     * @param  p  Lo stato di partenza della transizione.
     * @param  ch Il simbolo che etichetta la transizione.
     * @param  q  Lo stato di arrivo della transizione.
     * @return <code>true</code> se lo stato di partenza e lo stato di
     *         arrivo sono validi, <code>false</code> altrimenti.
     */
    public boolean setMove(int p, char ch, int q) {
	if (!validState(p) || !validState(q))
	    return false;

	transitions.put(new Move(p, ch), q);
	return true;
    }

    /**
     * Aggiunge uno stato finale.
     * @param  p Lo stato che si vuole aggiungere a quelli finali.
     * @return <code>true</code> se lo stato e` valido,
     *         <code>false</code> altrimenti.
     */
    public boolean addFinalState(int p) {
	if (validState(p)) {
	    finalStates.add(p);
	    return true;
	} else
	    return false;
    }

    /**
     * Determina se uno stato e` valido oppure no.
     * @param  p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` valido,
     *         <code>false</code> altrimenti.
     * @see #numberOfStates
     */
    public boolean validState(int p) {
	return (p >= 0 && p < numberOfStates);
    }

    /**
     * Determina se uno stato e` finale oppure no.
     * @param  p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` finale,
     *         <code>false</code> altrimenti.
     * @see #finalStates
     */
    public boolean finalState(int p) {
	return finalStates.contains(p);
    }

    /**
     * Restituisce il numero di stati dell'automa.
     * @return Numero di stati.
     */
    public int numberOfStates() {
	return numberOfStates;
    }

    /**
     * Restituisce l'alfabeto dell'automa, ovvero l'insieme di simboli
     * che compaiono come etichette delle transizioni dell'automa.
     * @return L'alfabeto dell'automa.
     */
    public HashSet<Character> alphabet() {
	HashSet<Character> alphabet = new HashSet<Character>();
	for (Move m : transitions.keySet())
	    alphabet.add(m.ch);
	return alphabet;
    }

    /**
     * Esegue una mossa dell'automa.
     * @param p  Stato di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Stato di arrivo dopo la transizione, oppure
     *         <code>-1</code> se l'automa non ha una transizione
     *         etichettata con <code>ch</code> dallo stato
     *         <code>p</code>.
     */
    public int move(int p, char ch) {
	Move move = new Move(p, ch);
	if (transitions.containsKey(move))
	    return transitions.get(move);
	else
	    return -1;
    }

    /**
     * Verifica se una stringa e` riconosciuta dall'automa.
     * @param  s  Stringa da riconoscere.
     * @return <code>true</code> se la stringa e` stata riconosciuta,
     *         <code>false</code> altrimenti.
     */
    public boolean scan(String s) {
	// DA IMPLEMENTARE
	  int state = 0;
	  int i = 0;
	  while ( state >= 0 && i < s . length ()) {
        final char ch = s . charAt ( i ++);
        state = move(state, ch);
	  }
	  System.out.println("Ultimo stato scan: " + state);
	  return finalState(state);
    }

    /**
     * Stampa una rappresentazione testuale dell'automa da
     * visualizzare con <a href="http://www.graphviz.org">GraphViz</a>.
     * @param name Nome dell'automa.
     */
    public void toDOT(String name) {
	// DA IMPLEMENTARE
	  System.out.println("digraph " + name+ " {");
      System.out.println("rankdir=LR;");
      System.out.println("node [shape = doublecircle];");
      for(int i=0; i<numberOfStates();i++)
		  if(finalState(i))
		    System.out.println("q" + i + ";");
      System.out.println("node [shape = circle];");
      for(Move key:transitions.keySet()){
        System.out.println("q" + key.start + " -> q" + transitions.get(key) + " [ label = " + key.ch + "  ];");
      }
      System.out.println("}");
    }

    /**
     * Stampa una classe Java con un metodo <code>scan</code> che implementa
     * l'automa.
     * @param name Nome della classe da generare.
     */
    public void toJava(String name) {
	// DA IMPLEMENTARE
		System.out.println("public class " + name);
		System.out.println("{");
		System.out.println("\tpublic static boolean scan(String s)");
		System.out.println("\t{");
		System.out.println("\t\tint state = 0;");
		System.out.println("\t\tint i = 0;");
		System.out.println("\t\twhile (state >= 0 && i < s.length()) {");
		System.out.println("\t\t\tfinal char ch = s.charAt(i++);");
		System.out.println("\t\t\tswitch (state) {");
		for (int i = 0; i < this.numberOfStates; i++){
			int cont = 0;
			System.out.println("\t\t\tcase " + i + ":");
			for(char p : this.alphabet()){
				int q = move(i, p);
				if(q != -1){
					if (cont == 0){
						System.out.println("\t\t\t\tif (ch == '" + p + "')");
						System.out.println("\t\t\t\t\tstate = " + q + ";");
						cont++;
					}
					else{
						System.out.println("\t\t\t\telse if (ch == '" + p + "')");
						System.out.println("\t\t\t\t\tstate = " + q + ";");
					}
				}
			}
			System.out.println("\t\t\t\telse");	
			System.out.println("\t\t\t\t\tstate = -1;");
			System.out.println("\t\t\t\tbreak;");
		}
		System.out.println("\t\t\t}");
		System.out.println("\t\t}");
		if (this.finalStates.size() == 1) {
			for (int i : finalStates)
				System.out.println("\t\treturn state == " + i + ";");
		}
		else{
			int cor = 0;
			System.out.print("\t\treturn ");
			for (int i : finalStates){
				System.out.print("state == " + i);
				cor++;
				if (cor < finalStates.size()) {
					System.out.print(" || ");
				}
			}
			System.out.print(";\n");
		}
		System.out.println("\t}");
		System.out.println();
		System.out.println("\tpublic static void main(String[] args)");
		System.out.println("\t{");
		System.out.println("\t\tSystem.out.println(scan(args[0]) ? \"OK\" : \"NOPE\");");
		System.out.println("\t}");
		System.out.println("}");
    }

    public HashSet<Integer> reach(int q) {
		if(!validState(q))
		  return null;
		HashSet<Integer> s = new HashSet<Integer>();
		Boolean [] r = new Boolean[numberOfStates()];
		for(int i=0 ; i<numberOfStates(); i++){
		  if(i!=q)
		     r[i] = false;
		  else
		     r[i] = true;
		 }
		 boolean trovato=true;
		 while(trovato){
			  trovato = false;
			  for(int i=0; i<numberOfStates(); i++){
				  if(r[i]){
					  for(char ch: alphabet()){
						Move m = new Move(i, ch);
					    if(transitions.get(m)!=null){
						  if(r[transitions.get(m)]==false){
					        r[transitions.get(m)]=true;
					        trovato=true;
					      }
					    }
					  }
				   }
			   }
		   }
		   for(int i=0; i<numberOfStates(); i++)
			   if(r[i])
			     s.add(i);

		   return s;
	}

	public boolean empty(){
		for(int i: reach(0))
		    if(finalState(i))
		      return false;
		return true;
	}

	 public HashSet<Integer> sink() {
		 HashSet<Integer> s = new HashSet<Integer>();
		 boolean pozzo = true;
		 int j=0;
		 while(j<numberOfStates()){
		 	for(int i: reach(j))
				if(finalState(i))
				  pozzo = false;
			if(pozzo == true)
				s.add(j);
			pozzo = true;
			j++;
		}
		return s;
	 }

	 public HashSet<String> samples() {
			HashSet<String> s = new HashSet<String>();
			String [] r = new String[numberOfStates()];
			int m;
			for(int i=0 ; i<numberOfStates(); i++){
				r[i]=null;

			 }
			 r[0] = "";
			 boolean trovato=true;
			 HashSet<Character> ch = alphabet();
			 while(trovato){
				  trovato = false;
				  for(int i=0; i<numberOfStates(); i++){
					  if(r[i]!=null){
						  for(Character c: ch){
							m = move(i, c);
						    if(validState(m)){
							  if(r[m]==null){
						        r[m]=r[i].toString()+c.toString();
						        trovato=true;
						      }
						    }
						  }
					   }
				   }
			   }
			   for(int i=0; i<numberOfStates(); i++){
				   if(r[i]!=null&&finalState(i)){
				     s.add(r[i]);
				 }
			   }
			   return s;
	}

	public DFA minimize(){
		int n = numberOfStates();
		//1 allocare matrice
		boolean [][] eq = new boolean [n][n];
		eq = riempiTabella();
		//5 allocazione array
	    int [] m = new int[n];
	    for(int i = 0; i<n ; i++){
			boolean trovato = false;
	    	for(int j = 0; j < n&&!trovato; j++)
	    		if(eq[i][j]){
	    		  m[i]=j;
				  trovato = true;
				 }
		}
		//6 allocare DFA B
        int max=m[0];
	    for(int i =1; i<n ; i++)
    		if(m[i]>max)
				max = m[i];
		DFA B = new DFA(max+1);
		for(Move key:transitions.keySet())
			B.setMove(m[key.start], key.ch, m[transitions.get(key)]);
		for(int i = 0; i<n;i++)
			if(finalState(i))
				B.addFinalState(m[i]);
		//7 ritornare DFA B
		return B;
	}
	public boolean[][] riempiTabella(){
			int n = numberOfStates();
			//1 allocare matrice
			boolean [][] eq = new boolean [n][n];
			//2 inizializzare matrice
			for(int i= 0; i < n; i++)
				for(int j = 0; j < n; j++)
					if((finalState(i)&&finalState(j))||(!finalState(i)&&!finalState(j)))
						eq[i][j] = true; //si mette a true gli stati INDISTINGUIBILI (i,j entrambi finali o entrambi non finali)
					else
						eq[i][j] = false;//se uno stato è finale e l'altro no, i due stati sono DISTINGUIBILI
	        boolean trovato = true;
	        while(trovato){//4 ripetere 3
				trovato = false;
				//3 si pone gli elementi Distinguibili a false
				for(int i= 0; i < n; i++)
					for(int j = 0; j < n; j++)
						for(char ch: alphabet())
						  if(move(i,ch)!=-1 && move(j,ch)!=-1)
							if((eq[i][j])&&(!(eq[move(i,ch)][move(j,ch)]))){
								eq[i][j] = false;
								trovato = true;
							}
	    	}
	    	for(int i= 0; i < n; i++){
					for(int j = 0; j < n; j++)
						System.out.print(eq[i][j]);
					System.out.println();
			}
	    	return eq;
	}

	public boolean equivalentTo(DFA B){
		DFA C = new DFA(numberOfStates()+B.numberOfStates);
		int q0=0, r0=numberOfStates;
		for(Move key:transitions.keySet())
			C.setMove(key.start, key.ch, transitions.get(key));
		for(Move key:B.transitions.keySet())
			C.setMove(key.start+numberOfStates(), key.ch, B.transitions.get(key)+numberOfStates());
		for(int i=0;i<numberOfStates();i++)
				if(finalState(i))
					C.addFinalState(i);
		for(int i=numberOfStates;i<C.numberOfStates;i++)
			if(B.finalState(i-numberOfStates))
				C.addFinalState(i);
		boolean [][] eq = new boolean[C.numberOfStates()][C.numberOfStates()];
		eq = C.riempiTabella();
		if (eq[q0][r0])
			return true;
		else
			return false;
	}


}
