/***
 * Tiny library to use a really simple representation of images for teaching purposes.
 *     
 * Copyright (c) 2009 Université de Lille 1.
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use, 
 * modify and/or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following 
 * URL "http://www.cecill.info". You should also have received a copy
 * with this code.
 * 
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited
 * liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.univlille.media;

import java.util.TreeSet;

/**
 * Class to represent greyscale images with a palette and an array of indices of 
 * palette.
 * 
 * The cell x+width*y of the array contains the index of the palette grey level 
 * for pixel (x, y).
 * 
 * @author Samuel Hym
 */
public class IndexedGreyImage extends GreyImage {
	
	/**
	 * The palette
	 */
	private int[] palette;
	/**
	 * The image array: cell x+width*y contains the index of the 
	 * palette grey level for pixel (x, y).
	 */
	private int[] image;
	
	/**
	 * Getter for the image array.
	 * 
	 * @return The image array: cell x+width*y contains the index of the 
	 * palette grey level for pixel (x, y).
	 */
	public int[] getImage() {
		return image;
	}
	
	/**
	 * Getter for the palette.
	 * 
	 * @return The palette as an array of integers in the interval [0..depth]
	 */
	public int[] getPalette() {
		return palette;
	}
	
	/**
	 * Returns the grey level of a pixel
	 * 
	 * @param x
	 * @param y
	 * @return the grey level of pixel (x, y)
	 */
	public int getGrey(int x, int y) {
		return palette[image[x + y * width]];
	}
	
	/**
	 * Builds a new {@link IndexedGreyImage} of given width, height, depth, palette and image array.
	 * 
	 * @param width
	 * @param height
	 * @param depth the maximum value of grey in the palette
	 * @param palette the palette as an array of integers in the interval [0..depth]
	 * @param image The image array: cell x+width*y contains the index of the 
	 *  palette grey level for pixel (x, y).
	 */
	public IndexedGreyImage(int width, int height, int depth, int[] palette, int[] image) {
		assert (image.length == width * height);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.palette = palette;
		this.image = image;
	}
	
	/**
	 * Builds a new {@link IndexedGreyImage} representing the same greyscale 
	 * image as the {@link GreyImage} given as argument by computing the 
	 * palette needed for the image.
	 * 
	 * @param srcimg
	 */
	public IndexedGreyImage(GreyImage srcimg) {
		this.width = srcimg.getWidth();
		this.height = srcimg.getHeight();
		this.depth = srcimg.getDepth();
		
		TreeSet<Integer> greys = new TreeSet<Integer>();
		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
				greys.add(srcimg.getGrey(x, y));
		
		this.palette = new int[greys.size()];
		int[] xref = new int[this.depth + 1];
		int i = 0;
		for(Integer g: greys) {
			this.palette[i] = g;
			xref[g] = i;
			i++;
		}
		
		this.image = new int[this.width * this.height];
		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
				this.image[x + y * this.width] = xref[srcimg.getGrey(x, y)];	
	}
	
	/**
	 * Clones the current {@link IndexedGreyImage}, duplicating its palette and its image array 
	 * (to perform later manipulations without impacting the original image).
	 * 
	 * @return the cloned {@link IndexedGreyImage}
	 */
	public IndexedGreyImage clone() {
		return new IndexedGreyImage(this.width, this.height, this.depth, this.palette.clone(), this.image.clone());
	}
	
	/**
	 * Stretches the palette of the current {@link IndexedGreyImage}.
	 * 
	 * Modifies the palette of the current image so that the minimal value becomes 0 
	 * and the maximal value becomes depth. All grey levels in between are accordingly 
	 * linearly modified.
	 * This increases contrast of the image.
	 */
	public void strechPalette() {
		if (palette.length == 0)
			return;
		int min = palette[0], max = palette[0];
		for(int i = 1; i < palette.length; i++) {
			min = Math.min(min, palette[i]);
			max = Math.max(max, palette[i]);			
		}
		if (min == max)
			return;
		
		for(int i = 0; i < palette.length; i++)
			palette[i] = (int)Math.round((palette[i] - min) * (float)(this.depth) / (float)(max - min));		
	}
	
	/**
	 * {@inheritDoc}
	 * Also shows the size of the palette.
	 */
	public String toString() {
		return "IndexedGreyImage(" + width + "x" + height + "[" + depth + "|" + this.palette.length + "])";
	}

}
