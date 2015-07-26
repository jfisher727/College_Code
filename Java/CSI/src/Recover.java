import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Course: CSE 4051, Fall 2013
 * Project: proj03, Recover
 */
public class Recover {

    private static final int SECOND_TAG = 0xD8;
    private static final int FIRST_TAG = 0xFF;
    private static final int DOUBLE_DIGIT = 10;
    private static final int IMAGE_CHUNK_SIZE = 512;

    /*
     * Used for opening the stream from the argument
     * passed to the program
     */
    public static InputStream openStream (final String arg) {
        try {
            return new URL(arg).openStream();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Checks the first two bytes of the data chunk to see if they
     * are equal to that of the jpg header standard
     */
    public static boolean checkForImage (final byte [] dataChunk) {
        return ((dataChunk [0] == (byte) FIRST_TAG)
                && (dataChunk [1] == (byte) SECOND_TAG));
    }

    /*
     * Continues to read bytes until the container is actually full
     * and 512 bytes have actually been read from the inputstream
     */
    public static byte [] readMoreBytes (final byte [] container,
            final InputStream dataReader) {
        int bytesRead;
        try {
            bytesRead = dataReader.read(container);
            while (bytesRead < IMAGE_CHUNK_SIZE) {
                final int moreBytesRead = dataReader.read(container,
                        bytesRead, IMAGE_CHUNK_SIZE - bytesRead);
                bytesRead += moreBytesRead;
            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return container;
    }

    //generates a new file for output of the image
    public static File generateFile (final int imageCounter) {
        String fileName;
        if (imageCounter < DOUBLE_DIGIT) {
            fileName = "image0" + imageCounter + ".jpg";
        }
        else {
            fileName = "image" + imageCounter + ".jpg";
        }
        return new File(fileName);
    }

    /*
     * Parses the data read from the output stream to find jpg images
     * and creates new files for each image found and writes them
     * to that file, continues to write one image until the next
     * one is found, assuming all data between headers goes to the
     * previous image
     */
    public static void parseForImages (final InputStream dataReader) {
        int imageCounter = 1, moreBytesRead = 0;
        final byte [] dataChunk = new byte[IMAGE_CHUNK_SIZE];
        FileOutputStream output = null;
        boolean writing = false;
        try {
            //reads until we have reached the end of the file
            while (moreBytesRead != -1) {
                int bytesRead;
                bytesRead = dataReader.read(dataChunk);
                while (bytesRead < IMAGE_CHUNK_SIZE) {
                    final int endPoint = IMAGE_CHUNK_SIZE - bytesRead;
                    moreBytesRead = dataReader.read(dataChunk,
                            bytesRead, endPoint);
                    bytesRead += moreBytesRead;
                }
                final boolean imageFound = checkForImage (dataChunk);
                if (imageFound) {
                    //starting to print image
                    output = new FileOutputStream
                            (generateFile(imageCounter));
                    imageCounter++;
                    output.write(dataChunk);
                    writing = true;
                }
                if (writing && !imageFound) {
                    output.write(dataChunk);
                }
            }
            dataReader.close();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main (final String [] args) {
        final String passedArg = args [0];
        final InputStream dataReader = openStream(passedArg);
        if (dataReader != null) {
            parseForImages (dataReader);
        }
        else {
            System.out.println("Couldn't open data source.");
        }
    }
}
