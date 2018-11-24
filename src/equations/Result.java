package equations;

public class Result {
	
	private final int i;
	private final int res;
	
	public Result(int i, int res) {
		this.i = i;
		this.res = res;
	}

	public int getI() {
		return i;
	}

	public int getRes() {
		return res;
	}

	@Override
	public String toString() {
		return "Result [i=" + i + ", res=" + res + "]";
	}
}
