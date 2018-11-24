package terms;

/**
 *
 * @author Aurelien
 */
public class Term {
    
    private int a;
    private final int i;
    
    public Term(int a, int i) {
		this.a = a;
		this.i = i;
	}

	public int getA() {
		return a;
	}

	public int getI() {
		return i;
	}
	
	public void flipA() {
		this.a *= -1;
	}
	
	public void setA(int newA) {
		this.a = newA;
	}
	
	@Override
	public String toString() {
		return this.getA() + "·x" + this.getI();
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (i != other.i)
			return false;
		return true;
	}
}
