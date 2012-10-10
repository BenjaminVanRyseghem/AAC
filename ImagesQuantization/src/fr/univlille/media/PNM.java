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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class providing static methods to read or write PNM images.
 * 
 * @author Samuel Hym
 */
public class PNM {
	private static boolean isDigit(int c) {
		return c >= 0x30 && c <= 0x39; 
	}
	private static int getDigit(int c) {
		return c - 0x30;
	}
	
	/** 
	 * Internal parsing method to get an integer in decimal form from an inputstream.
	 * This method strips comments and white spaces surrounding the expected integer.
	 * 
	 * This is parsing is assuming ASCII-compatible encoding for digits, '#' and '\n' (UTF-8, Latin-1, etc.)
	 * 
	 * @param in {@link InputStream} to read from
	 * @param maxValue Maximum value to expect for the integer to be read
	 * @param maxNbDigits Maximum number of digits for one digit (should match log(maxValue))
	 * @param expectFinalNotDigit If true, checks that the next byte in the stream after the 
	 *  integer is not a digit (useful is the maximum number of digits is reached but we still
	 *  want to ensure that the following byte is white space).
	 * @throws IOException The method does not handle IOExceptions thrown by the {@link InputStream}.   
	 * @throws PNMReaderException Exceptions thrown whenever no integer or an integer too large is found. 
	 */
	private static int parseAndStripInt(InputStream in, int maxValue, int maxNbDigits, boolean expectFinalNotDigit)
		throws IOException, PNMReaderException {
		boolean parsingBegan = false;
		boolean gobblingComment = false;
		int currentVal = 0;
		int currentNbDigits = 0;
		int c = in.read();
		
		while((!parsingBegan) || gobblingComment || (isDigit(c) && currentNbDigits < maxNbDigits)) {
			if (c == -1)
				throw (new PNMReaderException("PNM format expected (file too short)"));
			if (gobblingComment) {
				if (c == '\n')
					gobblingComment = false;
				c = in.read();
			} else {
				if (!parsingBegan && c == '#') {
					gobblingComment = true;
					c = in.read();
				} else {
					if (isDigit(c)) {
						parsingBegan = true;

						if (currentVal * 10 + getDigit(c) > maxValue)
							throw (new PNMReaderException("Integer too large"));
						currentVal = currentVal * 10 + getDigit(c);
						currentNbDigits ++;

						if (currentNbDigits == maxNbDigits && expectFinalNotDigit) {
							c = in.read();
							if (isDigit(c))
								throw (new PNMReaderException("Integer too large"));
						}
						if (currentNbDigits < maxNbDigits)
							c = in.read();
						else break;
					} else {
						if (parsingBegan)
							break;
						else
							c = in.read(); // Gobbling
					}
				}
			}
		}
		
		return currentVal;
	}
	
	/**
	 * Internal parsing method to get an integer in decimal form from an inputstream.
	 * This method uses maximal values for bounds.
	 * 
	 * @see PNM#parseAndStripInt(InputStream in, int maxValue, int maxNbDigits, boolean expectFinalNotDigit)
	 */
	private static int parseAndStripInt(InputStream in) throws IOException, PNMReaderException {
		return parseAndStripInt(in, Integer.MAX_VALUE, (int)Math.ceil(Math.log10(Integer.MAX_VALUE)), true);
	}
	
	/**
	 * Reads a PGM (greyscale) image from an InputStream, assuming the parsing of the PGM 
	 * header has already been performed.
	 * 
	 * @param in the {@link InputStream} to read from
	 * @param raw if true, the image is in Raw format; if false, in Plain format
	 * @param width width of the image to read
	 * @param height height of the image to read
	 * @param depth depth (maximal value of grey level) of the image to read
	 * @return the {@link ComponentGreyImage} corresponding to the PGM data
	 * @throws IOException The method does not handle IOExceptions thrown by
	 *  the {@link InputStream}.
	 * @throws PNMReaderException Exceptions thrown whenever the data read in
	 *  the stream do not match PGM format.
	 */
	public static ComponentGreyImage readPGM(InputStream in, boolean raw, int width, int height, int depth) throws IOException, PNMReaderException {
		int[] image = new int[width * height];
		int maxNbDigits = (int)Math.ceil(Math.log10(depth+1));
		
		for(int i = 0; i < width * height; i++) {
			if (raw) {
				if (depth > 255)
					image[i] = in.read() << 8 + in.read();
				else
					image[i] = in.read();
			}
			else {
				image[i] = parseAndStripInt(in, depth, maxNbDigits, false);
			}
		}
		
		return new ComponentGreyImage(width, height, depth, image);
	}

	/**
	 * Reads a PNM image from an {@link InputStream}.
	 * At the moment only PGM images are handled.
	 * 
	 * @param in The {@link InputStream} to read from
	 * @return The {@link Image} read from the stream
	 * @throws IOException The method does not handle IOExceptions thrown by
	 *  the {@link InputStream}.
	 * @throws PNMReaderException Exceptions thrown whenever the data read in
	 *  the stream do not match PNM format.
	 */
	public static Image readPNM(InputStream in) throws IOException, PNMReaderException {
		boolean raw;
 		int format;
		int width, height, depth;
		
		if (in.read() != 'P')
			throw (new PNMReaderException("PNM format expected (invalid magic)"));
		format = parseAndStripInt(in);
		if (format > 0 && format < 4)
			raw = false;
		else
			if (format > 3 && format < 7) {
				raw = true;
				format -= 3;
			}
			else
				throw (new PNMReaderException("PNM format expected (invalid magic number)"));
		
		width = parseAndStripInt(in);
		height = parseAndStripInt(in);
		depth = parseAndStripInt(in, 65535, 5, true);
				
		switch(format) {
		case 1:
			//return readPBM(in, raw, width, height, depth);
		case 2:
			return readPGM(in, raw, width, height, depth);
		case 3:
			//return readPPM(in, raw, width, height, depth);
		}
		
		return new ComponentGreyImage(0, 0, 255, new int[0]);
	}
	
	/**
	 * Writes a greyscale {@link GreyImage} to an {@link OutputStream} in ASCII PGM format.
	 * 
	 * @param gi the {@link GreyImage} to output
	 * @param out the {@link OutputStream} to write to
	 * @throws IOException The method does not handle IOExceptions thrown by
	 *  the {@link OutputStream}.
	 */
	public static void writePlainPGM(GreyImage gi, OutputStream out) throws IOException {
		OutputStreamWriter w = new OutputStreamWriter(out);

		w.write("P2\n" + gi.getWidth() + " " + gi.getHeight() + "\n" + gi.getDepth() + "\n");
		for(int y = 0; y < gi.getHeight(); y++)
			for(int x = 0; x < gi.getWidth(); x++)
				if ((y * gi.getWidth() + x) % 11 == 10)
					w.write(gi.getGrey(x, y) + "\n");
				else
					w.write(gi.getGrey(x, y) + " ");
		w.flush();
	}

}
