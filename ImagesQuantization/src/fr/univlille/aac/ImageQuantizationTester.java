package fr.univlille.aac;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.univlille.media.*;


public class ImageQuantizationTester {
    public static long error(GreyImage img1, GreyImage img2) {
        assert (img1.getWidth() == img2.getWidth());
        assert (img1.getHeight() == img2.getHeight());
        assert (img1.getDepth() == img2.getDepth());
        long error = 0;
        int greyDiff;

        for(int x = 0; x < img1.getWidth(); x++)
            for(int y = 0; y < img1.getHeight(); y++) {
                greyDiff = img1.getGrey(x, y) - img2.getGrey(x, y);
                error += greyDiff * greyDiff;
            }

        return error;
    }

    public static void main(String[] args) throws IOException, PNMReaderException {
        String[] defaultPaletteSizes = new String[] { "256", "53", "32", "8", "3", "2", "1" };

        for(String arg : args) {
            String[] paletteSizes;
            String file;
            int pos;

            pos = arg.lastIndexOf(':');
            if (pos == -1) {
                file = arg;
                paletteSizes = defaultPaletteSizes;
            }
            else {
                file = arg.substring(0, pos);
                paletteSizes = arg.substring(pos+1).split(",");
            }
            IndexedGreyImage img = new IndexedGreyImage((ComponentGreyImage) PNM.readPNM(new FileInputStream(file)));
            String baseFileName = (new File(file)).getName();
            pos = baseFileName.lastIndexOf('.');
            if (pos > -1)
                baseFileName = baseFileName.substring(0, pos);

            for(String sizeStr : paletteSizes) {
                int size = Integer.parseInt(sizeStr);
                IndexedGreyImage res = LossyGreyQuantizer.quantizer(img, size);
                System.err.println("From " + file + " (" + img.toString() + "), reduced to " + size + " (" + (res.getPalette().length == size?"":"un") + "expected) as " + res.toString() + " with error " + error(img, res));
                PNM.writePlainPGM(res, new FileOutputStream(baseFileName + "-" + size + ".pgm"));
            }
        }
    }
}
