package tsp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * This heuristic iteratively appends a customer
 * to the current solution until a tour is obtained
 *
 */

public class InsertHeuristicTSP implements HeuristicTSP {

	public double computeSolution(double[][] matrix, List<Integer> solution) {
		double value = 0.0;
		int i;
		int n = matrix.length;
		int indexToAdd = 0;
		solution.add(0);
		
		for (i = 0; i < n-1; i++){
			int current_summit = solution.get(i);
			List<Double> distances = new ArrayList<Double>();
			
			for(double d : matrix[current_summit]){
				distances.add(d);
			}
			
			Collections.sort(distances);
			indexToAdd = this.findIndexToAdd(distances, solution);
			solution.add(indexToAdd);
			value += matrix[i][indexToAdd];
		}
		// On n'oublie pas le chemin de retour
		value += matrix[indexToAdd][0];
		
		return value;
	}

	private int findIndexToAdd(List<Double> distances, List<Integer> solution) {
		int i;
		int size = distances.size();
		for(i = 0; i < size; i++){
			if(!solution.contains(i)) return i;
		}
		return -1;
	}

}