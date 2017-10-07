public class ProvaEquivalentTo{
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
		DFA B = new DFA (4);
		B . setMove (0 , '1', 1);
		B . setMove (0 , '0' , 0);
		B . setMove (1 , '1' , 1);
		B . setMove (1 , '0' , 2);
		B . setMove (2 , '1' , 3);
		B . setMove (2 , '0' , 2);
		B . setMove (3 , '1' , 3);
		B . setMove (3 , '0' , 2);
		B . addFinalState (1);
		B . addFinalState(3);
		B . toDOT("A");

  /* DFA tz = new DFA (3);
	tz . setMove (0 , '1', 0);
	tz . setMove (0 , '0' , 1);
	tz . setMove (1 , '1' , 0);
	tz . setMove (1 , '0' , 2);
	tz . setMove (2 , '1' , 0);
	tz . setMove (2 , '0' , 2);
	tz . addFinalState (2);
	tz . toDOT("TreZeri");*/
    	System . out . println (A.equivalentTo(B));
  }
}