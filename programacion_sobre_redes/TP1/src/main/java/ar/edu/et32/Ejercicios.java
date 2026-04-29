package ar.edu.et32;

import java.io.*;
import java.util.*;

public class Ejercicios {

    ArrayList<String> lista = new ArrayList<>();
    HashMap<String, String> diccionario = new HashMap<>();

    public void ejercicio1() throws IOException {

        lista.clear();

        Utils.mostrar("Ingrese 5 nombres:");

        for(int i = 0; i < 5; i++){
            Utils.mostrar("Nombre " + (i + 1) + ":");
            lista.add(Utils.leer());
        }

        Utils.mostrar("Lista: " + lista);
        Utils.mostrar("Cantidad: " + lista.size());
        Utils.mostrar("Primero: " + lista.get(0));
        Utils.mostrar("Último: " + lista.get(lista.size() - 1));

        Utils.mostrar("Mayúsculas:");
        for(String n : lista){
            Utils.mostrar(n.toUpperCase());
        }
    }

    public void ejercicio2() throws IOException {

        Utils.mostrar("Ingrese nombre:");
        String n = Utils.leer();

        if(lista.contains(n)){
            Utils.mostrar("Posición: " + lista.indexOf(n));
        } else {
            Utils.mostrar("No existe");
        }
    }

    public void ejercicio3() throws IOException {

        Utils.mostrar("Nuevo valor:");
        lista.set(2, Utils.leer());

        Utils.mostrar("Eliminar:");
        lista.remove(Utils.leer());

        Utils.mostrar("Lista: " + lista);
    }

    public void ejercicio4() {

        for(int i = 0; i < lista.size(); i++){
            System.out.println(lista.get(i));
        }

        for(String n : lista){
            System.out.println(n);
        }

        Iterator<String> it = lista.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    public void ejercicio5() throws IOException {

        Utils.mostrar("Ingrese frase:");
        String f = Utils.leer();

        String[] p = f.split(" ");
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(p));

        Utils.mostrar("Cantidad: " + arr.size());

        String larga = "";
        for(String s : arr){
            if(s.length() > larga.length()){
                larga = s;
            }
        }

        Utils.mostrar("Más larga: " + larga);

        Utils.mostrar("Letra:");
        String l = Utils.leer();

        int c = 0;
        for(String s : arr){
            if(s.contains(l)){
                c++;
            }
        }

        Utils.mostrar("Contienen: " + c);
    }

    public void ejercicio6() throws IOException {

        Utils.mostrar("Ingrese frase:");
        String f = Utils.leer().toLowerCase();

        String[] p = f.split(" ");

        for(String s : p){
            s = s.replace("a","*")
                 .replace("e","*")
                 .replace("i","*")
                 .replace("o","*")
                 .replace("u","*");

            Utils.mostrar(s);
        }
    }

    public void ejercicio7() throws IOException {

        diccionario.clear();

        for(int i = 0; i < 5; i++){

            Utils.mostrar("Español:");
            String es = Utils.leer();

            Utils.mostrar("Inglés:");
            String en = Utils.leer();

            diccionario.put(es, en);
        }

        Utils.mostrar(diccionario.toString());
    }

    public void ejercicio8() throws IOException {

        Utils.mostrar("Palabra:");
        String p = Utils.leer();

        if(diccionario.containsKey(p)){
            Utils.mostrar(diccionario.get(p));
        } else {
            Utils.mostrar("No existe");
        }
    }

    public void ejercicio9() throws IOException {

        Utils.mostrar("Frase:");
        String f = Utils.leer();

        for(String p : f.split(" ")){
            if(diccionario.containsKey(p)){
                System.out.print(diccionario.get(p) + " ");
            } else {
                System.out.print("[???] ");
            }
        }

        System.out.println();
    }

    public void ejercicio10() throws IOException {

        Utils.mostrar("Frase:");
        String f = Utils.leer();

        HashMap<String, Integer> cont = new HashMap<>();

        for(String p : f.split(" ")){

            if(cont.containsKey(p)){
                cont.put(p, cont.get(p) + 1);
            } else {
                cont.put(p, 1);
            }
        }

        Utils.mostrar(cont.toString());
    }

    public void ejercicio11() throws IOException {

        ArrayList<Integer> nums = new ArrayList<>();

        Utils.mostrar("Ingrese números (0 termina):");

        int n;
        do{
            n = Integer.parseInt(Utils.leer());
            if(n != 0) nums.add(n);
        } while(n != 0);

        HashSet<Integer> set = new HashSet<>(nums);

        Utils.mostrar("Original: " + nums);
        Utils.mostrar("Sin repetidos: " + set);
    }

    public void ejercicio12() throws IOException {

        ArrayList<String> alumnos = new ArrayList<>();
        HashMap<String, Integer> notas = new HashMap<>();

        int op;

        do{

            Utils.mostrar("\n1 Agregar 2 Mostrar 3 Buscar 4 Modificar 5 Promedio 0 Salir");
            op = Integer.parseInt(Utils.leer());

            if(op == 1){

                Utils.mostrar("Nombre:");
                String n = Utils.leer();

                Utils.mostrar("Nota:");
                int nota = Integer.parseInt(Utils.leer());

                alumnos.add(n);
                notas.put(n, nota);
            }

            else if(op == 2){
                Utils.mostrar(notas.toString());
            }

            else if(op == 3){

                Utils.mostrar("Buscar:");
                String n = Utils.leer();

                if(notas.containsKey(n)){
                    Utils.mostrar("" + notas.get(n));
                } else {
                    Utils.mostrar("No existe");
                }
            }

            else if(op == 4){

                Utils.mostrar("Alumno:");
                String n = Utils.leer();

                if(notas.containsKey(n)){
                    Utils.mostrar("Nueva nota:");
                    notas.put(n, Integer.parseInt(Utils.leer()));
                }
            }

            else if(op == 5){

                int suma = 0;

                for(int v : notas.values()){
                    suma += v;
                }

                double prom = notas.size() > 0 ? (double)suma / notas.size() : 0;

                Utils.mostrar("Promedio: " + prom);

                for(String a : notas.keySet()){
                    int v = notas.get(a);

                    if(v >= 6){
                        System.out.println("\u001B[32m" + a + " APROBADO\u001B[0m");
                    } else {
                        System.out.println("\u001B[31m" + a + " DESAPROBADO\u001B[0m");
                    }
                }
            }

        } while(op != 0);
    }
}