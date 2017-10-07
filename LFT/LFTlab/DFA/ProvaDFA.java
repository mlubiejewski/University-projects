public class ProvaDFA{
  public static void main(String args[]){
    DFA tz = new DFA (4);
	tz . setMove (0 , '1', 0);
	tz . setMove (0 , '0' , 1);
	tz . setMove (1 , '1' , 0);
	tz . setMove (1 , '0' , 2);
	tz . setMove (2 , '1' , 0);
	tz . setMove (2 , '0' , 3);
	tz . setMove (3 , '1' , 3);
	tz . setMove (3 , '0' , 3);
	tz . addFinalState (3);
	tz . toDOT("TreZeri");
	tz . toJava("TreZeri");
    System . out . println ( tz.scan ( args [0]) ? " OK " : " NOPE " );
    System.out.println(tz.reach(0));
    System.out.println(tz.empty());
    System.out.println(tz.sink());
    System.out.println(tz.samples());
  }
}