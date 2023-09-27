package image;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * SubImageIterableProperty class.
 * this class represent an iterable property of the SubImage
 */
public class SubImageIterableProperty<T> implements Iterable<T> {

    private static final int ZERO = 0;
    private final Image img;
    private final BiFunction<Integer, Integer, T> propertySupplier;
    private final int sizeOfImage;

    /**
     * SubImageIterableProperty constructor.
     * creates an SubImageIterableProperty object
     *
     * @param img              image to get subImages from
     * @param sizeOfImage      size of each sub image
     * @param propertySupplier function that return a subImage object
     */
    public SubImageIterableProperty(Image img, int sizeOfImage, BiFunction<Integer, Integer, T> propertySupplier) {
        this.img = img;
        this.propertySupplier = propertySupplier;
        this.sizeOfImage = sizeOfImage;
    }

    /**
     * iterator - this iterator iterate over all sub images in the size of the field sizeOfImagr
     * from row to row.
     *
     * @return SubImage object
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int x = ZERO, y = ZERO;

            @Override
            public boolean hasNext() {
                return y < img.getHeight();
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                var subImage = propertySupplier.apply(x, y);
                x += sizeOfImage;
                if (x >= img.getWidth()) {
                    x = ZERO;
                    y += sizeOfImage;
                }
                return subImage;
            }
        };

    }
}
