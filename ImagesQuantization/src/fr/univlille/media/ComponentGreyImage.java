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

/**
 * Class to represent greyscale images as an array of such levels.
 * 
 * The cell x+width*y of the array contains the grey level of pixel (x, y).
 * 
 * @author Samuel Hym
 */
public class ComponentGreyImage extends GreyImage {
	/**
	 * The image itself: cell x+width*y contains the grey level of pixel (x, y).
	 */
	private int[] image;
	
	/**
	 * Returns the grey level of a pixel.
	 * 
	 * @param x
	 * @param y
	 * @return the grey level of pixel (x, y).
	 */
	public int getGrey(int x, int y) {
		return image[x + this.width * y];
	}
	
	/**
	 * Sets the grey level of a pixel.
	 * 
	 * @param x
	 * @param y
	 * @param g grey level to assign to the pixel (x, y)
	 */
	public void setGrey(int x, int y, int g) {
		image[x + y * this.width] = g;
	}

	/**
	 * Getter for the image array.
	 * 
	 * @return the image array
	 */
	public int[] getImage() {
		return this.image;
	}
	
	/**
	 * Builds a ComponentGreyImage of the given width, height, depth and image array.
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 * @param image must contain in cell x+width*y the shade of grey of pixel (x, y).
	 */
	public ComponentGreyImage(int width, int height, int depth, int[] image) {
		assert image.length == width * height;
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.image = image;
	}
	
	/**
	 * Builds a ComponentGreyImage as a collage of a matrix of greyscale images.
	 * All the images must share the same depth.
	 * 
	 * @param images the matrix of images to compose
	 * @param sep the minimal separation (of black pixels) between 2 images
	 */
	public ComponentGreyImage(GreyImage[][] images, int sep) {
		this.width = 0;
		this.height = 0;
		this.depth = 0;
		
		for(int i = 0; i < images.length; i++)
			for(int j = 0; j < images[i].length; j++) {
				if (this.depth == 0)
					this.depth = images[i][j].getDepth();
				else
					if(images[i][j].getDepth() != this.depth && images[i][j].getDepth() != 0)
						throw new IllegalArgumentException("ComponentGreyImage constructor: all images in matrix must share the same depth");
			}
		
		for(int i = 0; i < images.length; i++) {
			int lineWidth = 0;
			int lineHeight = 0;
			
			for(int j = 0; j < images[i].length; j++) {
				if (j > 0)
					lineWidth += sep;
				lineWidth += images[i][j].getWidth();
				lineHeight = Math.max(lineHeight, images[i][j].getHeight());
			}
			
			this.width = Math.max(this.width, lineWidth);
			if (i > 0)
				this.height += sep;
			this.height += lineHeight;			
		}
		
		this.image = new int[this.width * this.height];
		
		// Variables to hold the origin of the image we are currently copying
		int refX = 0;
		int refY = 0;
		int maxHeight;
		for(int i = 0; i < images.length; i++) {
			maxHeight = 0;
			for(int j = 0; j < images[i].length; j++) {
				for(int y = 0; y < images[i][j].getHeight(); y++)
					for(int x = 0; x < images[i][j].getWidth(); x++)
						this.image[refX + x + (refY + y) * this.width] = images[i][j].getGrey(x, y);
				refX += images[i][j].getWidth() + sep;
				maxHeight = Math.max(maxHeight, images[i][j].getHeight());
			}
			refX = 0;
			refY += maxHeight + sep;
		}
	}
	
	public String toString() {
		return "ComponentGreyImage(" + width + "x" + height + "[" + depth + "])";
	}	
}
