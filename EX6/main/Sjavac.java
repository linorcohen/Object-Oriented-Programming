package oop.ex6.main;

import oop.ex6.validator.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sjavac class. this class read the given file and returns its validity.
 */
public class Sjavac {
    /**
     * 0 – if the code is legal.
     */
    private static final int VALID_INPUT = 0;
    /**
     * 1 – if the code is illegal.
     */
    private static final int INVALID_INPUT = 1;
    /**
     * 2 – in case of IO errors.
     */
    private static final int IO_ERROR = 2;
    /**
     * Constants representing the error messages:
     */
    private static final String INVALID_PATH_ERROR = "Invalid file path.";
    private static final String READING_ERROR = "Problem while reading the file.";
    private static final String LINE = "Line ";
    private static final String COLON = ": ";

    public static void main(String[] args) {
        String inputFile = args[0];
        Parser parser = new Parser(null);
        // reader should be initialized inside try's parentheses for auto closing
        try (BufferedReader reader1 = new BufferedReader(new FileReader(inputFile));
             BufferedReader reader2 = new BufferedReader(new FileReader(inputFile))) {
            parser.setReader(reader1);
                parser.initialParsing();
                parser.setReader(reader2);
                parser.mainParsing();
        } catch (FileNotFoundException e) {
            System.err.println(INVALID_PATH_ERROR);
            System.out.println(IO_ERROR);
            return;
        } catch (IOException e) {
            System.err.println(READING_ERROR);
            System.out.println(IO_ERROR);
            return;
        } catch (Exception e) {
            System.err.println(LINE + parser.getCurrentLineIndex() + COLON + e.getMessage());
            System.out.println(INVALID_INPUT);
            return;
        }
        System.out.println(VALID_INPUT);
    }
}
