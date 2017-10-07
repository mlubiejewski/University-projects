public class RegExpTest{
	public static void main(String[] args){
		RegExpSequence s0=new RegExpSequence(new RegExpSymbol('/'), new RegExpSymbol('*'));
		RegExpChoice c1=new RegExpChoice(new RegExpSymbol('c'), new RegExpSymbol('*'));
		RegExpSequence s1=new RegExpSequence(new RegExpSymbol('c'), new RegExpSymbol('/'));
		RegExpChoice c2=new RegExpChoice(c1, s1);
		RegExpStar st1=new RegExpStar(c2);
		RegExpSequence s2=new RegExpSequence(s0, st1);
		RegExpSequence s3=new RegExpSequence(new RegExpSymbol('*'), new RegExpSymbol('/'));
		RegExpSequence s4=new RegExpSequence(s2, s3);
		NFA ris = s4.compile();
		ris.dfa().toDOT("RegExp");
		ris.dfa().minimize().toDOT("RegExp min");
		System . out . println ( ris.dfa().scan ( args [0]) ? " OK " : " NOPE " );
	}
}