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
 * Abstract class for color (red, green, blue) images. 
 * 
 * @author Samuel Hym
 */
public abstract class ColorImage extends Image {
	/**
	 * Returns the red component of a pixel of the image.
	 * 
	 * @param x
	 * @param y
	 * @return the red component for pixel (x, y)
	 */
	public abstract int getRed(int x, int y);
	public abstract int getGreen(int x, int y);
	public abstract int getBlue(int x, int y);
	public int[] getColor(int x, int y) {
		int[] c = new int[3];
		c[0] = getRed(x, y);
		c[1] = getGreen(x, y);
		c[2] = getBlue(x, y);
		return c;
	}
}
