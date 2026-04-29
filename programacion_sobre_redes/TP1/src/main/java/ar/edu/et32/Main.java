package ar.edu.et32;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Ejercicios e = new Ejercicios();
        int op;

        do {

            System.out.println("\nMENU");
            System.out.println("1 Ejercicio 1");
            System.out.println("2 Ejercicio 2");
            System.out.println("3 Ejercicio 3");
            System.out.println("4 Ejercicio 4");
            System.out.println("5 Ejercicio 5");
            System.out.println("6 Ejercicio 6");
            System.out.println("7 Ejercicio 7");
            System.out.println("8 Ejercicio 8");
            System.out.println("9 Ejercicio 9");
            System.out.println("10 Ejercicio 10");
            System.out.println("11 Ejercicio 11");
            System.out.println("12 Ejercicio 12");
            System.out.println("0 salir");

            System.out.print("opcion: ");
            op = Integer.parseInt(Utils.leer());

            switch(op){

                case 1:
                    e.ejercicio1();
                    break;

                case 2:
                    e.ejercicio2();
                    break;

                case 3:
                    e.ejercicio3();
                    break;

                case 4:
                    e.ejercicio4();
                    break;

                case 5:
                    e.ejercicio5();
                    break;

                case 6:
                    e.ejercicio6();
                    break;

                case 7:
                    e.ejercicio7();
                    break;

                case 8:
                    e.ejercicio8();
                    break;

                case 9:
                    e.ejercicio9();
                    break;

                case 10:
                    e.ejercicio10();
                    break;

                case 11:
                    e.ejercicio11();
                    break;

                case 12:
                    e.ejercicio12();
                    break;

                case 0:
                    System.out.println("fin del programa");
                    break;

                default:
                    System.out.println("opcion invalida");
            }

        } while(op != 0);
    }
}
