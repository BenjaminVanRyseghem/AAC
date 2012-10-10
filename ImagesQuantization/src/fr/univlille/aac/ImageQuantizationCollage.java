package fr.univlille.aac;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.univlille.media.ComponentGreyImage;
import fr.univlille.media.IndexedGreyImage;
import fr.univlille.media.PNM;
import fr.univlille.media.PNMReaderException;


public class ImageQuantizationCollage {
	/**
	 * Outputs the levels of grey in the palette.
	 * 
	 * @param img the image of which the palette must be displayed
	 */
	public static void printPalette(IndexedGreyImage img) {
		for(Integer j: img.getPalette())
			System.out.print(j + " ");
		System.out.println();
	}
	
	/**
	 * Sample main function.
	 * The program must be invoked with two arguments: the original image 
	 * filename and the destination image filename.
	 * 
	 * @param args the original image filename and the destination image filename
	 * @throws IOException thrown if a problem is encountered in I/O operations
	 * @throws PNMReaderException thrown if a problem is encountered in reading/writing a PNM file
	 */
	public static void main(String[] args) throws IOException, PNMReaderException {
		IndexedGreyImage img = new IndexedGreyImage((ComponentGreyImage) PNM.readPNM(new FileInputStream(args[0])));
		System.out.println("Starting from image: " + img.toString());

		ImageQuantizationCollage.printPalette(img);
		
		Integer[][] paletteSizes =
			new Integer[][] { { 256,  32,   8, 256 },
				              { 256,  32,   8, 256 },
				              { 256,   2,  16,   4 },
				              { 256,   2,  16,   4 } };
		Boolean[][] stretch =
			new Boolean[][] { { false, false, false, false },
				              {  true,  true,  true,  true },
				              { false, false, false, false },
				              {  true,  true,  true,  true } };
		
		IndexedGreyImage[][] collage = new IndexedGreyImage[paletteSizes.length][];
		
		for(int i = 0; i < paletteSizes.length; i++) {
			collage[i] = new IndexedGreyImage[paletteSizes[i].length];
			
			for(int j = 0; j < paletteSizes[i].length; j++) {
				collage[i][j] = LossyGreyQuantizer.quantizer(img, paletteSizes[i][j]);
				if (stretch[i][j])
					collage[i][j].strechPalette();
				System.out.println(collage[i][j]);
				ImageQuantizationCollage.printPalette(collage[i][j]);
			}
		}
		
		PNM.writePlainPGM(new ComponentGreyImage(collage, 5), new FileOutputStream(args[1]));
	}
}
