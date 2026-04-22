package ar.edu.et32;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Ejercicios ej = new Ejercicios();
        int op;

        do {
            Utils.getOut().println("\nMENU");
            Utils.getOut().println("1-15 ejercicios | 0 salir");

            op = Integer.parseInt(Utils.getLector().readLine());

            switch(op){
                case 1: ej.Guia1_Ej1(); break;
                case 2: ej.Guia1_Ej2(); break;
                case 3: ej.Guia1_Ej3(); break;
                case 4: ej.Guia1_Ej4(); break;
                case 5: ej.Guia1_Ej5(); break;
                case 6: ej.Guia1_Ej6(); break;
                case 7: ej.Guia1_Ej7(); break;
                case 8: ej.Guia1_Ej8(); break;
                case 9: ej.Guia1_Ej9(); break;
                case 10: ej.Guia1_Ej10(); break;
                case 11: ej.Guia1_Ej11(); break;
                case 12: ej.Guia1_Ej12(); break;
                case 13: ej.Guia1_Ej13(); break;
                case 14: ej.Guia1_Ej14(); break;
                case 15: ej.Guia1_Ej15(); break;
                case 0: Utils.getOut().println("Saliendo..."); break;
                default: Utils.getOut().println("Opcion invalida");
            }

        } while(op != 0);
    }
}
