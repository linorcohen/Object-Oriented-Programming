package image;

import java.awt.*;
import java.util.function.BiFunction;

/**
 * SubImage class.
 * this class represent a sub image of the given image, implements the Image interface
 */
public class SubImage implements Image {

    /**
     * private field represent the sub image size
     */
    private final int size;
    /**
     * private field represent the get pixel function
     */
    private final BiFunction<Integer, Integer, Color> propertySupplier;
    /**
     * private field represent the sub image array
     */
    private final Color[][] subImage;


    /**
     * SubImage constructor.
     * creates a SubImage object
     *
     * @param x                col number of the left top pixel
     * @param y                row number of the left top pixel
     * @param size             size of the sub image
     * @param propertySupplier get pixel function
     */
    public SubImage(int x, int y, int size, BiFunction<Integer, Integer, Color> propertySupplier) {
        this.propertySupplier = propertySupplier;
        this.subImage = getSubImg(x, y, size);
        this.size = size;
    }

    /**
     * private method - this method gets the subImage according to the given coordinates and size,
     * using the pixel function.
     *
     * @param x    col number of the left top pixel
     * @param y    row number of the left top pixel
     * @param size size of the sub image
     * @return 2d array of Colors representing the sub image
     */
    private Color[][] getSubImg(int x, int y, int size) {
        Color[][] subImg = new Color[size][size];
        for (int i = y; i < y + size; i++) {
            for (int j = x; j < x + size; j++) {
                subImg[i - y][j - x] = propertySupplier.apply(i, j);
            }
        }
        return subImg;
    }

    /**
     * thie method returns the pixel of the sub in=mage in the position (x,y)
     *
     * @param x col number
     * @param y row number
     * @return Color in position (x,y)
     */
    @Override
    public Color getPixel(int x, int y) {
        return subImage[y][x];
    }

    /**
     * this method returns the width of the sub image
     */
    @Override
    public int getWidth() {
        return size;
    }

    /**
     * this method returns the height of the sub image
     */
    @Override
    public int getHeight() {
        return size;
    }
}
