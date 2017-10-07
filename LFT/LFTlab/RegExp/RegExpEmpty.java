public class RegExpEmpty implements RegExp {
	public NFA compile() {
			NFA a = new NFA(2);
			a.addFinalState(1);
			return a;
    }
}