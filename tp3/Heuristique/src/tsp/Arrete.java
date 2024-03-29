package tsp;

public class Arrete implements Comparable<Arrete> {

	private double value;
	private Sommet src;
	private Sommet dest;
		
	@Override
	public int compareTo(Arrete arg) {
		return (int)(value-arg.getValue());
	}

	public Arrete(double value, int src, int dest) {
		this.value = value;
		this.src = Sommet.getSommet(src);
		this.dest = Sommet.getSommet(dest);
	}

	public double getValue() {
		return value;
	}

	public Sommet getSrc() {
		return src;
	}

	public Sommet getDest() {
		return dest;
	}
	
}
