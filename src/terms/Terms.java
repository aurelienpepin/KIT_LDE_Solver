package terms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Terms implements Iterable<Term> {

	private final SortedSet<Term> set;
	private final Map<Integer, Term> map;
	
	public Terms() {
		this.set = new TreeSet<>(new TermComparator());
		this.map = new HashMap<>();
	}
	
	public void add(Term term) {
		this.set.add(term);
		this.map.put(term.getI(), term);
	}
	
	public int size() {
		return this.set.size();
	}
	
	public Term first() {
		return this.set.first();
	}
	
	public Term get(int i) {
		return this.map.get(i);
	}
	
	public boolean remove(Term term) {
		if (term == null)
			return false;
		
		this.map.remove(term.getI());
		return this.set.remove(term);
	}
	
	public boolean remove(int i) {
		Term term = this.map.remove(i);
		
		if (term == null)
			return false;
		
		return this.set.remove(term);
	}

	/**
	 * Guarantees the order.
	 * Complexity: O(n * log n)
	 */
	@Override
	public Iterator<Term> iterator() {
		return this.set.iterator();
	}
	
	/**
	 * Doesn't guarantee the order.
	 * Complexity: O(n)
	 * @return
	 */
	public Iterator<Term> messyIterator() {
		return this.map.values().iterator();
	}

	@Override
	public String toString() {
		return "Terms [set=" + set + "]";
	}
}
