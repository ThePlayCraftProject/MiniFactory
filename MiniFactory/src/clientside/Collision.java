package clientside;

public class Collision implements Comparable<Collision>{
	private Object o;
	private Object o2;
	private double t;
	
	public Object getO() {
		return o;
	}

	public Object getO2() {
		return o2;
	}

	public double getT() {
		return t;
	}
	
	public Collision(Object o, Object o2, double t) {
		this.o = o;
		this.o2 = o2;
		this.t = t;
	}

	@Override
	public int compareTo(Collision arg0) {
		if (t > arg0.t) return 1;
		return -1;
	}
}
