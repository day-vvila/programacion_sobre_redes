package ar.edu.et32;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String leer() throws IOException {
        return br.readLine();
    }

    public static void mostrar(String msg) {
        System.out.println(msg);
    }
}