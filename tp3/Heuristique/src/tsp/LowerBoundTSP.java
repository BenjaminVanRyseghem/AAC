package tsp;

public class LowerBoundTSP{

//	private void printMatrix(double[][] m){
//		int n = m.length;
//		for(int i = 0 ; i < n ; i ++){
//			for(int j = 0 ; j < n ; j ++){
//				System.out.print(m[i][j]+"\t");
//			}
//			System.out.println();
//		}
//	}
	
    public static double lowerBoundValue(double[][] matrix){
    	double minimum = 0.0;
    	int n = matrix.length;

    	for (int i = 0 ; i < n ; i ++){
    		double min = matrix[i][0];
    		for (int j=1; j < n ; j++){
    			if(matrix[i][j] < min){
    				min = matrix[i][j]; 
    			}
    		}
    		if(min < NodeTSP.MAX_VALUE){
	    		for (int j=0; j < n ; j++){
	    			matrix[i][j] = matrix[i][j] - min;
	    		}
	    		minimum += min;
    		}
    	}
    	
    	for (int j = 0 ; j < n ; j ++){
    		double min = matrix[0][j];
    		for (int i=1; i < n ; i++){
    			if(matrix[i][j] < min){
    				min = matrix[i][j]; 
    			}
    		}
    		if(min < NodeTSP.MAX_VALUE){
	    		for (int i=0; i < n ; i++){
	    			matrix[i][j] = matrix[i][j] - min;
	    		}
	    		minimum += min;
    		}
    	}
    	
    	return minimum;
    }

}