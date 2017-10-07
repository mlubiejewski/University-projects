public class RegExpChoice implements RegExp {
    private RegExp e1;
    private RegExp e2;

    RegExpChoice(RegExp e1, RegExp e2) {
		this.e1 = e1;
		this.e2 = e2;
    }

    public NFA compile() {
		NFA a = new NFA(2);
		final int n = a.append(e1.compile());
		final int m = a.append(e2.compile());
		a.addMove(0, NFA.EPSILON, n);
		a.addMove(0, NFA.EPSILON, m);
		a.addMove(n + 1, NFA.EPSILON, 1);
		a.addMove(m + 1, NFA.EPSILON, 1);
		a.addFinalState(1);
		return a;
    }
}