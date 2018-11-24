package terms;

import java.util.Comparator;

/**
 * A term will occur first in the priority queue if its norm is small.
 * @author Aurelien
 */
public class TermComparator implements Comparator<Term> {

	@Override
	public int compare(Term t0, Term t1) {
		if (Math.abs(t0.getA()) < Math.abs(t1.getA()))
			return -1;
		else if (Math.abs(t0.getA()) > Math.abs(t1.getA()))
			return 1;
		else {
			if (t0.getI() < t1.getI())
				return -1;
			else
				return (t0.getI() == t1.getI()) ? 0 : 1;
		}
	}
}
