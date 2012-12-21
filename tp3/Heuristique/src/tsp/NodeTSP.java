package tsp;

import java.util.ArrayList;
import java.util.List;

import branchAndBound.Node;


enum state {FIRST, SECOND, END};
public class NodeTSP implements Node<List<Integer>> {

	static double MAX_VALUE = 1.7976931348623157E308;
	
	private double[][] matrix;
	Arrete arrete;
	NodeTSP father;
	state etat;
	double lb;
	
	private void printMatrix(){
		int n = matrix.length;
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < n ; j ++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
    /** to create the first node ==> root note */
	public NodeTSP(double[][] matrix) {
		this.matrix = matrix;
		int n = matrix.length;
		lb = -1;
		getLB();
		
		arrete = this.getNextArrete();
		father = null;
		etat = state.FIRST;
		
		
		int u = arrete.getSrc().getId();
		int v = arrete.getDest().getId();
		for(int i = 0 ; i < n ; i++){
			matrix[i][v] = MAX_VALUE+1;
			matrix[u][i] = MAX_VALUE+1;
		}
		
		arrete.getSrc().addArreteSortante(arrete);
		arrete.getDest().addArreteEntrante(arrete);
	}

	/** useful to create the children */
	private NodeTSP(NodeTSP father, Arrete arrete, boolean selected) {
		
		this.father = father;
		lb = -1;
		this.arrete = arrete;
		
		int n = father.matrix.length;
		matrix = new double[n][n];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < n ; j ++){
				this.matrix[i][j] = father.matrix[i][j];
			}
		}
		
		
		int u = arrete.getSrc().getId();
		int v = arrete.getDest().getId();
		if(selected){
			arrete.getSrc().addArreteSortante(arrete);
			arrete.getDest().addArreteEntrante(arrete);
			for(int i = 0 ; i < n ; i++){
				matrix[i][v] = MAX_VALUE+1;
				matrix[u][i] = MAX_VALUE+1;
			}
		}
		else{
			matrix[u][v] = MAX_VALUE+1;
		}
		etat = state.FIRST;
	}


	public String toString() {
		String s = "NODE TSP\n";
		return s;
	}

	public double getLB() {
		if(lb == -1)
			lb = LowerBoundTSP.lowerBoundValue(this.matrix);
		return lb;
	}

	
	public double getValue() {
		double fatherValue;
		if(father == null){
			fatherValue = 0;
		}
		else{
			fatherValue = father.getValue();
		}
		return fatherValue + arrete.getValue() + this.getLB();
	}

	public List<Integer> getSolution() {
		if(father == null) return new ArrayList<Integer>();

		List<Integer> listCustomers = new ArrayList<Integer>();
		List<Integer> father_listCustomers = father.getSolution();
		int size = father_listCustomers.size();
		for (int i = 0; i < size; i++) {
			listCustomers.add(father_listCustomers.get(i));
		}
		if(!listCustomers.contains(arrete.getSrc().getId())) listCustomers.add(arrete.getSrc().getId());
		if(!listCustomers.contains(arrete.getDest().getId())) listCustomers.add(arrete.getDest().getId());
		
		return listCustomers;
	}
	
	private NodeTSP root(){
		if(father == null) return this;
		return father.root();
	}

	public boolean isFeasible() {
		if(arrete == null) return true;
		return arrete.getSrc().getArretes_sortantes().size()<=1 && arrete.getDest().getArretes_entrantes().size()<=1 && (isLeaf() || arrete.getDest() != this.root().arrete.getSrc());
	}

	public boolean isLeaf() {
		return this.getNextArrete() == null;
		
	}

	public boolean hasNextChild() {
		return etat != state.END && this.isFeasible();
	}
	
	private Arrete getNextArrete(){
		List<Arrete> zero_edges = new ArrayList<Arrete>();
		int n = matrix.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(matrix[i][j] == 0){
					zero_edges.add(new Arrete(matrix[i][j],i,j));
				}
			}
		}
		
		if(zero_edges.isEmpty()) return null;
		
		Arrete best = null;
		double max = 0;
		
		for(Arrete a : zero_edges){
			double min = Integer.MAX_VALUE;
			for(int v = 0 ; v < n ; v ++){
				if(v != a.getSrc().getId() && matrix[v][a.getDest().getId()] < min){
					min = matrix[v][a.getDest().getId()];
				}
				if(v != a.getDest().getId() && matrix[a.getSrc().getId()][v] < min){
					min = matrix[a.getSrc().getId()][v];
				}
			}
			if(min>max){
				best = a;
				max = min;
			}
		}
		return best;
	}
	
	public Node<List<Integer>> getNextChild() {
		switch(etat){
			case FIRST:  
				etat = state.SECOND;
				return new NodeTSP(this, this.getNextArrete(), true);
			case SECOND:  
				etat = state.END;
				return new NodeTSP(this, this.getNextArrete(), false);
			default:
				return null;
		}
	}

}