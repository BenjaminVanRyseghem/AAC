package fr.univlille.aac;
import fr.univlille.media.*;

// Canevas à remplir

public class LossyGreyQuantizer {
    /**
     * Retourne un tableau indiquant, pour chaque couleur de la
     * palette, le nombre de pixel de l'image de cette couleur
     *
     * @param img image à traiter
     */
    public static int[] histogram(IndexedGreyImage img) {
    	int[] pixelColors = img.getImage();
    	int[] histogram = new int[img.getPalette().length];
    	
    	for (int i = 0; i < histogram.length; i++) {
			histogram[i] = 0;
		}
    	
//    	for (int i = 0; i < pixelColors.length; i++) {
//			int index = pixelColors[i];
//			histogram[index] = histogram[index]+1;
//		}
//    	
    	for (int index : pixelColors) {
    		histogram[index] = histogram[index]+1;
		}
    	
    	return histogram;
    }

    /**
     * Calcule le niveau de gris ideal pour minimiser l'erreur globale
     * si on fusionne tous les niveaux de gris compris entre start et
     * end dans la palette
     *
     * @param palette    palette de l'image
     * @param histogram  histogramme de l'image
     * @param start      premier indice (inclus) de niveau de gris a traiter
     * @param end        dernier indice (inclus) de niveau de gris a traiter
     */
    public static int intervalIdealGrey(int[] palette, int[] histogram, int start, int end) {
    	int sumOfHisto = 0;
    	int sum41 = 0;
    	
    	for (int i = start; i < end; i++) {
			sumOfHisto += histogram[i];
		}
    	
    	for (int i = start; i < end; i++) {
			sum41 += histogram[i]*palette[i];
		}
    	
        return (int)sum41/sumOfHisto;
    }

    /**
     * Calcule l'erreur obtenue en remplacant tous les niveaux de gris
     * compris entre start et end par le niveau de gris ideal
     *
     * @param palette    palette de l'image
     * @param histogram  histogramme de l'image
     * @param start      premier indice (inclus) de niveau de gris a traiter
     * @param end        dernier indice (inclus) de niveau de gris a traiter
     */
    public static int intervalError(int[] palette, int[] histogram, int start, int end) {
    	int globalError = 0;
    	int idealGreyColor = LossyGreyQuantizer.intervalIdealGrey(palette, histogram, start, end);
    	
    	for (int i = start; i < end; i++) {
    		globalError += (histogram[i]*idealGreyColor)^2;
		}
    	
        return globalError;
    }

    /**
     * Calcule une nouvelle image ayant une palette de targetPaletteSize niveaux
     * de gris et qui ait la plus petite erreur possible avec l'image d'origine
     *
     * @param img                image d'origine
     * @param targetPaletteSize  taille de la palette de l'image produite
     */
    public static IndexedGreyImage quantizer(IndexedGreyImage img, int targetPaletteSize) {
    	
    	
        return null;
    }
}

