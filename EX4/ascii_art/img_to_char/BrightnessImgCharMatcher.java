package ascii_art.img_to_char;

import image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * BrightnessImgCharMatcher class.
 * this class responsible for setting the brightness of the ASCII characters
 */
public class BrightnessImgCharMatcher {

    /**
     * private content represent the initial maximum brightness
     */
    private static final int INITIAL_MAX_BRIGHTNESS = 0;
    /**
     * private content represent the initial minimum brightness
     */
    private static final int INITIAL_MIN_BRIGHTNESS = 1;
    /**
     * private content represent the number 0
     */
    private static final int ZERO = 0;
    /**
     * private content represent the number 1
     */
    private static final int ONE = 1;
    /**
     * private content represent the char pixels to renderer
     */
    private static final int CHAR_RENDER_PIXEL = 16;
    /**
     * private content represent the color red factor
     */
    private static final double COLOR_RED_FACTOR = 0.2126;
    /**
     * private content represent the color green factor
     */
    private static final double COLOR_GREEN_FACTOR = 0.7152;
    /**
     * private content represent the color blue factor
     */
    private static final double COLOR_BLUE_FACTOR = 0.0722;
    /**
     * private content represent maximum rgb value
     */
    private static final int MAX_RGB = 255;

    /**
     * private field representing the render font
     */
    private final String font;
    /**
     * private field representing the image
     */
    private final Image img;
    /**
     * private field representing the maximum brightness
     */
    private float maxBrightness;
    /**
     * private field representing  the minimum brightness
     */
    private float minBrightness;
    /**
     * private field representing the char brightness map values. char=key, float=value
     */
    private final Map<Character, Float> charBrightnessMap;
    /**
     * private field representing the char brightness map values before normalizing them.
     * char=key, float=value
     */
    private final Map<Character, Float> charBrightnessBeforeNormalMap;
    /**
     * private field representing the subImage color map. Image=key, float=value
     */
    private final Map<Image, Character> subImgMap;

    /***
     * BrightnessImgCharMatcher constructor.
     * this method creates BrightnessImgCharMatcher object.
     *
     * @param img   the given image to render
     * @param font  the render font
     */
    public BrightnessImgCharMatcher(Image img, String font) {
        this.font = font;
        this.img = img;
        this.maxBrightness = INITIAL_MAX_BRIGHTNESS;
        this.minBrightness = INITIAL_MIN_BRIGHTNESS;
        this.charBrightnessMap = new HashMap<>();
        this.charBrightnessBeforeNormalMap = new HashMap<>();
        this.subImgMap = new HashMap<>();
    }

    /**
     * this method gets the current char set and number of char in row, and convert the image to a Ascii art,
     * by calculating each sub-image color and matching the closest char.
     *
     * @param numCharsInRow How many characters will we draw in each line of the ASCII image
     * @param charSet       The set of characters with which we would like to draw our picture
     * @return The method returns a two-dimensional array of characters (ASCII) representing an image
     * O(charSet.len)+O(charBrightnessBeforeNormalMap.len) +
     * O((size*size + charBrightnessMap.len)*(num of sub-images) = O(n^2) runtime
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        int sizeSubImg = img.getWidth() / numCharsInRow;
        int colNum = img.getHeight() / sizeSubImg;
        char[][] charImg = new char[colNum][numCharsInRow];
        calculateBrightnessOfCharSet(charSet);
        NormalizeAllCharsBrightness();
        return getCharImg(numCharsInRow, sizeSubImg, charImg);
    }

    /**
     * private method - this method gets a char set and check if there is char in the char set that
     * it not it the charBrightnessMap, if it doesn't, calculate its brightness.
     *
     * @param charSet The set of characters with which we would like to draw our picture
     * O(charSet.len) runtime
     */
    private void calculateBrightnessOfCharSet(Character[] charSet) {
        for (Character c : charSet) {
            if (!charBrightnessBeforeNormalMap.containsKey(c)) {
                calculateSingleCharBrightness(c);
            }
        }
    }

    /**
     * private method - this method runs over all subImage of image, And returns each char image.
     * together it creates a two-dimensional array of characters (ASCII) representing an image
     *
     * @param numCharsInRow How many characters will we draw in each line of the ASCII image
     * @param sizeSubImg    size of each subImg
     * @param charImg       2d array of chars in the correct size to fill.
     * @return The method returns a two-dimensional array of characters (ASCII) representing an image
     * O((size*size + charBrightnessMap.len)*(num of sub-images) runtime
     */
    private char[][] getCharImg(int numCharsInRow, int sizeSubImg, char[][] charImg) {
        int row = ZERO;
        int col = ZERO;
        for (Image subImg : img.subImages(sizeSubImg)) {
            charImg[row][col] = findCharWithClosestBrightness(subImg, sizeSubImg);
            col++;
            if (col >= numCharsInRow) {
                col = ZERO;
                row++;
            }
        }
        return charImg;
    }

    /**
     * private method - this method calculate a single char brightness.
     *
     * @param c the given char to calculate its brightness
     * O(CharRenderer) + O(CHAR_RENDER_PIXEL*CHAR_RENDER_PIXEL) runtime
     */
    private void calculateSingleCharBrightness(char c) {
        boolean[][] charRenderer = CharRenderer.getImg(c, CHAR_RENDER_PIXEL, font);
        float charBrightness = (float) getNumOfTrueInCharRenderer(charRenderer) /
                (CHAR_RENDER_PIXEL * CHAR_RENDER_PIXEL);
        if (charBrightness > maxBrightness) {
            maxBrightness = charBrightness;
        }
        if (charBrightness < minBrightness) {
            minBrightness = charBrightness;
        }
        charBrightnessBeforeNormalMap.put(c, charBrightness);
    }

    /**
     * private method - this method counts the number of true values in the given 2d boolean array,
     * representing the char image.
     *
     * @param charRenderer 2d boolean array representing the char image.
     * @return number of true values in the given array.
     * O(CHAR_RENDER_PIXEL*CHAR_RENDER_PIXEL) runtime
     */
    private int getNumOfTrueInCharRenderer(boolean[][] charRenderer) {
        int numOfTrue = ZERO;
        for (int i = ZERO; i < CHAR_RENDER_PIXEL; i++) {
            for (int j = ZERO; j < CHAR_RENDER_PIXEL; j++) {
                if (charRenderer[i][j]) {
                    numOfTrue++;
                }
            }
        }
        return numOfTrue;
    }

    /**
     * private method - this method normalize all all chars brightness by taking all the calculating chars
     * values and normalizing - stretching the brightness values.
     * O(charBrightnessBeforeNormalMap.len) runtime
     */
    private void NormalizeAllCharsBrightness() {
        for (Map.Entry<Character, Float> entry : charBrightnessBeforeNormalMap.entrySet()) {
            float newCharBrightness = (entry.getValue() - minBrightness) / (maxBrightness - minBrightness);
            charBrightnessMap.put(entry.getKey(), newCharBrightness);
        }
    }

    /**
     * private method - this method gets a subImage and counties its number of grey pixels.
     *
     * @param subImg the current subImage to calculate
     * @param size   size of the subImage
     * @return number of grey pixels of the subImage
     * O(size*size) runtime
     */
    private float getSumOfGreyPixels(Image subImg, int size) {
        float sumGreyPixels = ZERO;
        for (int i = ZERO; i < size; i++) {
            for (int j = ZERO; j < size; j++) {
                sumGreyPixels += subImg.getPixel(i, j).getRed() * COLOR_RED_FACTOR +
                        subImg.getPixel(i, j).getGreen() * COLOR_GREEN_FACTOR +
                        subImg.getPixel(i, j).getBlue() * COLOR_BLUE_FACTOR;
            }
        }
        return sumGreyPixels;
    }

    /**
     * private method - this method calculate the given subImage brightness.
     *
     * @param subImg the current subImage to calculate
     * @param size   size of the subImage
     * @return subImage brightness
     * O(size*size) runtime
     */
    private float calculateSingleImgBrightness(Image subImg, int size) {
        float sum = getSumOfGreyPixels(subImg, size);
        return sum / ((size * size) * MAX_RGB);
    }

    /**
     * private method - this method gets a subImg and find the closest char to it by finding the closest
     * brightness value char.
     * this method also update the subImgMap field, to hold the given subImage,and its correct char matcher.
     *
     * @param subImg the current subImage to calculate
     * @param size   size of the subImage
     * @return the closest brightness char .
     * O(size*size + charBrightnessMap.len) runtime
     */
    private char findCharWithClosestBrightness(Image subImg, int size) {
        if (subImgMap.containsKey(subImg)) {
            return subImgMap.get(subImg);
        }
        float imgBrightness = calculateSingleImgBrightness(subImg, size);
        float closestBrightnessGap = ONE;
        char closestChar = ZERO;
        for (Map.Entry<Character, Float> entry : charBrightnessMap.entrySet()) {
            float charGap = Math.abs(entry.getValue() - imgBrightness);
            if (charGap < closestBrightnessGap) {
                closestChar = entry.getKey();
                closestBrightnessGap = charGap;
            }
        }
        subImgMap.put(subImg, closestChar);
        return closestChar;
    }
}
