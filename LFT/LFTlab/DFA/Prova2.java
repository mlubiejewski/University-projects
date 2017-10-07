public class Prova2{
  public static void main(String args[]){
    DFA cn = new DFA (8);
    //stato 0
	setMoveCifre (0, 2, cn);
	cn . setMove (0 , '+' , 1);
	//cn . setMove (0 , '-' , 1);
	cn . setMove (0 , '+' , 2);
	cn . setMove (0 , '.' , 3);
	//stato 1
	setMoveCifre(1, 2, cn);
	cn . setMove (1 , '.' , 3);
    //stato 2
	setMoveCifre (2, 2, cn);
	cn . setMove (2 , '.' , 3);
	cn . setMove (2 , 'e' , 5);
	//stato 3
	setMoveCifre(3, 4, cn);
	//stato 4
	setMoveCifre(4, 4, cn);
	cn . setMove (4 , 'e' , 5);
	//stato 5
	setMoveCifre(5, 7, cn);
	cn . setMove (5 , '+' , 6);
	cn . setMove (5 , '-' , 6);
	//stato 6
	setMoveCifre(6, 7, cn);
    //stato 7
    setMoveCifre(7, 7, cn);
	cn . addFinalState (2);
	cn . addFinalState (4);
	cn . addFinalState (7);
    System . out . println ( cn.scan ( args [0]) ? " OK " : " NOPE " );
    System.out.println("Stati raggiungibili da stato 0: " + cn.reach(0));
	System.out.println("Linguaggio vuoto? " + cn.empty());
    System.out.println("Stati pozzo: " + cn.sink());
    cn.toDOT("CostantiNumeriche");

  }
  public static void setMoveCifre(int iniziale, int finale, DFA cn){
	  for(int i=0;i<10;i++)
	   cn.setMove (iniziale , (char)(i+48), finale);//il carattere 0 corrisponde a 48 in ASCII
  }
}