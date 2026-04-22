package ar.edu.et32;
import java.io.*;

public class Utils {

    private static BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

    public static BufferedReader getLector() {
        return lector;
    }

    public static PrintWriter getOut() {
        return out;
    }
}



