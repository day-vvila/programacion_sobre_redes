package ar.edu.et32;
import java.io.*;

public class Ejercicios {

    public void Guia1_Ej1(){
        try {
            Utils.getOut().println("Ingrese el nombre de la organizacion:");
            String aux = Utils.getLector().readLine().toUpperCase();

            String[] palabras = aux.split(" ");
            String acronimo = "";

            for(String p : palabras){
                acronimo += p.charAt(0);
            }

            Utils.getOut().println("Acronimo: " + acronimo);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej2() {
        try {
            Utils.getOut().println("Ingrese palabra:");
            String p = Utils.getLector().readLine();

            String inv = new StringBuilder(p).reverse().toString();

            if(p.equalsIgnoreCase(inv))
                Utils.getOut().println("Es palindromo");
            else
                Utils.getOut().println("No es palindromo");

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej3() {
        try {
            Utils.getOut().println("Ingrese texto:");
            String t = Utils.getLector().readLine().toLowerCase();

            int c = 0;
            for(char ch : t.toCharArray()){
                if("aeiou".indexOf(ch) != -1) c++;
            }

            Utils.getOut().println("Vocales: " + c);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej4() {
        try {
            Utils.getOut().println("Texto:");
            String t = Utils.getLector().readLine();

            Utils.getOut().println("Buscar:");
            String b = Utils.getLector().readLine();

            Utils.getOut().println("Reemplazo:");
            String r = Utils.getLector().readLine();

            Utils.getOut().println(t.replace(b, r));

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej5() {
        try {
            Utils.getOut().println("Email:");
            String e = Utils.getLector().readLine();

            String u = e.substring(0, e.indexOf("@"));
            u = u.substring(0,1).toUpperCase() + u.substring(1).toLowerCase();

            Utils.getOut().println("Bienvenido " + u);

        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public void Guia1_Ej6() {
        try {
            Utils.getOut().println("Frase:");
            String t = Utils.getLector().readLine();

            t = t.trim().replaceAll(" +", " ");
            Utils.getOut().println(t);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej7() {
        try {
            Utils.getOut().println("Password:");
            String p = Utils.getLector().readLine();

            boolean l = p.length() >= 8;
            boolean n = p.matches(".*\\d.*");
            boolean c = p.toLowerCase().contains("clave");

            if(l && n && !c)
                Utils.getOut().println("SEGURO");
            else
                Utils.getOut().println("VULNERABLE");

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej8() {
        try {
            Utils.getOut().println("Ruta:");
            String r = Utils.getLector().readLine();

            String ext = r.substring(r.lastIndexOf(".")+1);
            Utils.getOut().println("Extension: " + ext);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej9() {
        try {
            Utils.getOut().println("Ingrese texto:");
            String linea = Utils.getLector().readLine();

            String textoFinal = "";

            for(int i = 0; i < linea.length(); i++) {
                if(i % 2 == 0)
                    textoFinal += Character.toUpperCase(linea.charAt(i));
                else
                    textoFinal += Character.toLowerCase(linea.charAt(i));
            }

            Utils.getOut().println(textoFinal);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej10() {
        try {
            Utils.getOut().println("Texto:");
            String t = Utils.getLector().readLine();

            Utils.getOut().println("Buscar:");
            String b = Utils.getLector().readLine();

            int ini = t.indexOf(b);
            int fin = ini + b.length();

            Utils.getOut().println("Inicio: " + ini);
            Utils.getOut().println("Fin: " + fin);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej11() {
        try {
            Utils.getOut().println("Producto:");
            String p = Utils.getLector().readLine();

            Utils.getOut().println("Cantidad:");
            int c = Integer.parseInt(Utils.getLector().readLine());

            Utils.getOut().println("Precio:");
            float pr = Float.parseFloat(Utils.getLector().readLine());

            StringBuilder sb = new StringBuilder();
            sb.append("Producto: ").append(p)
              .append(" Cantidad: ").append(c)
              .append(" Precio: ").append(pr)
              .append(" Total: ").append(c * pr);

            Utils.getOut().println(sb.toString());

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej12() {
        try {
            String n = Utils.getLector().readLine();

            StringBuilder sb = new StringBuilder(n);
            sb.insert(0, "Dr./Dra. ");

            Utils.getOut().println(sb);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej13() {
        try {
            String t = Utils.getLector().readLine();
            StringBuilder sb = new StringBuilder(t);

            int i = sb.indexOf("error");

            if(i != -1)
                sb.delete(i, i + 6);

            Utils.getOut().println(sb);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej14() {
        try {
            String p = Utils.getLector().readLine();

            StringBuilder sb = new StringBuilder(p);
            sb.insert(0, "<b>");
            sb.append("</b>");

            Utils.getOut().println(sb);

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Guia1_Ej15() {
        try {
            String p1 = Utils.getLector().readLine();
            String p2 = Utils.getLector().readLine();
            String p3 = Utils.getLector().readLine();

            StringBuilder sb = new StringBuilder();
            sb.append(p1).append("-").append(p2).append("-").append(p3);

            int i = sb.indexOf(p2);

            if(i != -1)
                sb.delete(i - 1, i + p2.length() + 1);

            Utils.getOut().println(sb);

        } catch (IOException e) { e.printStackTrace(); }
    }
}

