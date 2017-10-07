public class ProvaMin{
  public static void main(String args[]){
    DFA A = new DFA (4);
	A . setMove (0 , '1', 1);
	A . setMove (0 , '0' , 0);
	A . setMove (1 , '1' , 1);
	A . setMove (1 , '0' , 2);
	A . setMove (2 , '1' , 3);
	A . setMove (2 , '0' , 2);
	A . setMove (3 , '1' , 3);
	A . setMove (3 , '0' , 2);
	A . addFinalState (1);
	A . addFinalState(3);
	A . toDOT("A");
    System . out . println ( A.scan ( args [0]) ? " OK " : " NOPE " );
    System.out.println(A.reach(0));
    System.out.println(A.empty());
    System.out.println(A.sink());
    System.out.println(A.samples());
	DFA B = A.minimize();
	B.toDOT("TreZeriMin");
	System.out.println(B.reach(0));
	System.out.println(B.samples());
  }
}