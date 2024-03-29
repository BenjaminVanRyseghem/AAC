package tsp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This heuristic sorts the arcs by increasing value and 
 * considers each arc in turn for insertion
 * An arc is inserted if and only if it does not create a subtour.
 * The method stops when a tour is obtained.
 */
public class DecreasingArcHeuristicTSP implements HeuristicTSP {

	/** TODO coder cette méthode */
	public double computeSolution(double[][] matrix, List<Integer> solution) {
		double value = 0.0;
		int n = matrix.length;
		int index = 1;
		int size;
		
		List<Sommet> summits = new ArrayList<Sommet>();
		
		for ( int i = 0 ; i < n ; i++){
			summits.add(Sommet.getSommet(i));
		}
		
		List<Arrete> edges = this.getSortedListOfEdges(matrix);
		
		Sommet first = edges.get(0).getSrc();
		
		solution.add(first.getId());
		size = edges.size();
		
		while(index < size && solution.size() < n) {
			
			Arrete edge = edges.get(index);
			if (edge.getSrc().isArretesSortantesEmpty() && edge.getDest().isArretesEntrantesEmpty() && edge.getDest() != first) {
				int id = edge.getDest().getId();
				solution.add(id);
				edge.getSrc().addArreteSortante(edge);
				edge.getDest().addArreteEntrante(edge);
				value+=edge.getValue();
			}
			index++;
		}
		
		return value;
	}

	private List<Arrete> getSortedListOfEdges(double[][] matrix) {
		
		List<Arrete> edges = new ArrayList<Arrete>();
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				edges.add(new Arrete(matrix[i][j],i,j));
			}
		}
		
		Collections.sort(edges);
		
		return edges;
	}
}