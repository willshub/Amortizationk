package main.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: wraphale
 * Date: 4/25/14
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmortizationUtil {
    private static Console console = System.console();

    public static String readLine(String userPrompt) throws IOException {
        String line = "";

        if (console != null) {
            line = console.readLine(userPrompt);
        } else {
            // print("console is null\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            print(userPrompt);
            line = bufferedReader.readLine();
        }
        return line.trim();
    }

    public static void print(String s) {
        printf("%s", s);
    }

    public static void printf(String formatString, Object... args) {

        try {
            if (console != null) {
                console.printf(formatString, args);
            } else {
                System.out.print(String.format(formatString, args));
            }
        } catch (IllegalFormatException e) {
            System.err.print("Error printing...\n");
        }
    }
}
