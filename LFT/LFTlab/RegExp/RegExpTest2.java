public class RegExpTest2{
	
	public static void main(String []args){
		
		DFA dfaRange = range('A','F').compile().dfa();
		dfaRange.toDOT("range");
		System . out . println ( dfaRange.scan ( args [0]) ? " OK " : " NOPE " );
		dfaRange.minimize().toDOT("range min");

	}
	public static RegExp range(char from, char to){
		RegExpChoice temp = new RegExpChoice(new RegExpSymbol(from), new RegExpSymbol((char)(from+1)));
		for(int i = from+2; i<=to;i++)
			temp = new RegExpChoice(temp, new RegExpSymbol((char)(i)));
		return temp;
	}

}