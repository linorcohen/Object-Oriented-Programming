package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 *
 * @author Dan Nirel
 */
class FileImage implements Image {
    /**
     * private constant represents the default color while object
     */
    private static final Color DEFAULT_COLOR = Color.WHITE;
    /**
     * private constant of the number 1
     */
    private static final int ONE = 1;
    /**
     * private constant of the number 2
     */
    private static final int TWO = 2;
    /**
     * private constant of the number 0
     */
    private static final int ZERO = 0;
    /**
     * private field of the 2d pixel array
     */
    private final Color[][] pixelArray;
    /**
     * private field of the new height of the image
     */
    private final int newHeight;
    /**
     * private field of the new width of the image
     */
    private final int newWidth;

    /**
     * FileImage constructor.
     * creates a file image object.
     *
     * @param filename path of the image
     * @throws IOException in case ImageIO.read throws execution
     */
    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        newWidth = calculatePowerTowSize(origWidth);
        newHeight = calculatePowerTowSize(origHeight);

        pixelArray = new Color[newHeight][newWidth];

        addWhitePixelsToPixelArray(origWidth, origHeight);
        addOriginImgToPixelArray(im, origHeight, origWidth);
    }

    /**
     * private method - this method calculate the new size as to be a power of two.
     *
     * @param size a given size
     * @return closest power of two of that size
     */
    private int calculatePowerTowSize(int size) {
        int powerOfTwoNumber = ONE;
        while (size > powerOfTwoNumber) {
            powerOfTwoNumber *= TWO;
        }
        return powerOfTwoNumber;
    }

    /**
     * private method - this methods add white pixels to the frame of the image.
     *
     * @param origWidth  origin width of the image
     * @param origHeight origin height of the image
     */
    private void addWhitePixelsToPixelArray(int origWidth, int origHeight) {
        // rows filling with white color
        int numOfRowsToFill = (int) Math.ceil((float) ((newHeight - origHeight) / TWO));
        fillRowsWithWhitePixels(ZERO, numOfRowsToFill);
        fillRowsWithWhitePixels(newHeight - numOfRowsToFill, newHeight);
        // cols filling with white color
        int numOfColsToFill = (int) Math.ceil((float) ((newWidth - origWidth) / TWO));
        fillColsWithWhitePixels(ZERO, numOfColsToFill);
        fillColsWithWhitePixels(newWidth - numOfColsToFill - ONE, newWidth);
    }

    /**
     * private image - this method fills the given rows range with whites pixels.
     *
     * @param startRow index of the start row
     * @param endRow   index of the end row
     */
    private void fillRowsWithWhitePixels(int startRow, int endRow) {
        for (int row = startRow; row < endRow; row++) {
            for (int col = ZERO; col < newWidth; col++) {
                pixelArray[row][col] = DEFAULT_COLOR;
            }
        }
    }

    /**
     * private image - this method fills the given cols range with whites pixels.
     *
     * @param startCol index of the start col
     * @param endCol   index of the end col
     */
    private void fillColsWithWhitePixels(int startCol, int endCol) {
        for (int col = startCol; col < endCol; col++) {
            for (int row = ZERO; row < newHeight; row++) {
                pixelArray[row][col] = DEFAULT_COLOR;
            }
        }
    }

    /**
     * private method - this method adds the origin image pixels to the class field pixel array
     *
     * @param img        image to add
     * @param origWidth  origin width of the image
     * @param origHeight origin height of the image
     */
    private void addOriginImgToPixelArray(java.awt.image.BufferedImage img, int origHeight, int origWidth) {
        int startRow = (newHeight - origHeight) / TWO;
        int startCol = (newWidth - origWidth) / TWO;
        for (int row = startRow; row < startRow + origHeight; row++) {
            for (int col = startCol; col < startCol + origWidth; col++) {
                pixelArray[row][col] = new Color(img.getRGB(col - startCol, row - startRow));
            }
        }
    }

    /**
     * this method returns the current image width.
     */
    @Override
    public int getWidth() {
        return newWidth;
    }

    /**
     * this method returns the current image height.
     */
    @Override
    public int getHeight() {
        return newHeight;
    }

    /**
     * this method returns the pixel int the position (x,y).
     *
     * @param x col number
     * @param y row number
     * @return the color in the given position
     */
    @Override
    public Color getPixel(int x, int y) {
        Color pixel = pixelArray[x][y];
        return new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
    }

}

