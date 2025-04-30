package org.example.lya1.Sumador;

import java.util.Scanner;

public class sumador_binario{
    public static void main(String args[]){
        //Recibir la cadena con la suma
        Scanner entrada=new Scanner(System.in);
        String cadena=entrada.nextLine();

        //Revisar si cuenta con un espacio antes
        if(cadena.charAt(0)!=' ')
            cadena=" "+cadena;
        String cadena_original=cadena;
        //Añadir espacios necesarios para escribir el resultado de la suma
        int pos=0;
        do{pos++;}while(cadena.charAt(pos) != '+');
        for(int i=0; i<pos; i++){
            cadena=cadena+" ";
        }
        Sumador sumador=new Sumador(cadena_original);
        try{sumador.q0(cadena,0);}catch(Exception e){System.out.println("Ha ocurrido un error");}
    }
}

class Sumador{
    public static String original;
    static StringBuilder sb=new StringBuilder();
    public Sumador(String org){
        original=org;
    }
    public void q0(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,' ');
                q1(sb.toString(),pos+1);//Si lee espacio vacío, escribir espacio vacío y derecha. Mandar a q1
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q1(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q1(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q1
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q1(sb.toString(),pos+1);//Si lee 1, escribir 1 y derecha. Mandar a q1
            }else if(cadena.charAt(pos)=='+'){
                sb.setCharAt(pos,'+');
                q2(sb.toString(),pos-1);//Si lee +, escribir + e izquierda. Mandar a q2
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q2(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='.'){
                sb.setCharAt(pos,'.');
                q2(sb.toString(),pos-1);//Si lee punto, escribir punto e izquierda. Mandar a q2
            }else if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,' ');
                q15(sb.toString(),pos+1);//Si lee espacio vacío, escribir espacio vacío y derecha. Mandar a q15
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'.');
                q8(sb.toString(),pos+1);//Si lee 0, escribir punto y derecha. Mandar a q8
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'.');
                q3(sb.toString(),pos+1);//Si lee 1, escribir punto y derecha. Mandar a q3
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q3(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='.'){
                sb.setCharAt(pos,'.');
                q3(sb.toString(),pos+1);//Si lee punto, escribir punto y derecha. Mandar a q3
            }else if(cadena.charAt(pos)=='+'){
                sb.setCharAt(pos,'+');
                q3(sb.toString(),pos+1);//Si lee +, escribir + y derecha. Mandar a q3
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q3(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q3
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q3(sb.toString(),pos+1);//Si lee 1, escribir 1 y derecha. Mandar a q3
            }else if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q4(sb.toString(),pos-1);//Si lee _, escribir _ e izquierda. Mandar a q4
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q4(sb.toString(),pos-1);//Si lee =, escribir = e izquierda. Mandar a q4
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q4(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'_');
                q12(sb.toString(),pos+1);//Si lee 0, escribe _ y derecha. Mandara q12
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'_');
                q5(sb.toString(),pos+1);//Si lee 1, escribe _ y derecha. Mandar a q5
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q5(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q5(sb.toString(),pos+1);//Si lee _, escribe _ y derecha. Mandar a q5
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q6(sb.toString(),pos+1);//Si lee =, escribir = y derecha. Mandar a q6
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q6(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='A'){
                sb.setCharAt(pos,'1');
                q14(sb.toString(),pos+1);//Si lee A, escribir 1 y derecha. Mandar a q14
            }else if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,'0');
                q14(sb.toString(),pos+1);//Si lee espacio vacío, escribe A y derecha. Mandar a q14
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q6(sb.toString(),pos+1);//Si lee 1, escribe 1 y derecha. Mandar a q6
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q6(sb.toString(),pos+1);//Si lee 0, escribe 0 y derecha. Mandar a q6
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q7(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='+'){
                sb.setCharAt(pos,'+');
                q2(sb.toString(),pos-1);//Si lee +, escribe + e izquierda. Mandar a q2
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q7(sb.toString(),pos-1);//Si lee 0, escribe 0 e izquierda. Mandar a q7
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q7(sb.toString(),pos-1);//Si lee 1, escribir 1 e izquierda. Mandar a q7
            }else if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q7(sb.toString(),pos-1);//Si lee _, escribir _ e izquierda. Mandar a q7
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q7(sb.toString(),pos-1);//Si lee =, escribir = e izquierda. Mandar a q7
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q8(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='.'){
                sb.setCharAt(pos,'.');
                q8(sb.toString(),pos+1);//Si lee punto, escribir punto y derecha. Mandar a q8
            }else if(cadena.charAt(pos)=='+'){
                sb.setCharAt(pos,'+');
                q8(sb.toString(),pos+1);//Si lee +, escribir + y derecha. Mandar a q8
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q8(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q8
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q8(sb.toString(),pos+1);//Si lee 1, escribir 1 y derecha. Mandar a q8
            }else if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q9(sb.toString(),pos-1);//Si lee _, escribir _ e izquierda. Mandar a q9
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q9(sb.toString(),pos-1);//Si lee =, escribir = e izquierda. Mandar a q9
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q9(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'_');
                q10(sb.toString(),pos+1);//Si lee punto, escribir punto y derecha. Mandar a q10
            }else if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'_');
                q12(sb.toString(),pos+1);//Si lee +, escribir + y derecha. Mandar a q12
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q10(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q10(sb.toString(),pos+1);//Si lee _, escribe _ y derecha. Mandar a q10
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q11(sb.toString(),pos+1);//Si lee =, escribir = y derecha. Mandar a q11
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q11(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q11(sb.toString(),pos+1);//Si lee 1, escribe 1 y derecha. Mandar a q11
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q11(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q11
            }else if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,'0');
                q7(sb.toString(),pos-1);//Si lee espacio vacío, escribir 0 e izquierda. Mandar a q7
            }else if(cadena.charAt(pos)=='A'){
                sb.setCharAt(pos,'1');
                q7(sb.toString(),pos-1);//Si lee A, escribir 1 e izquierda. Mandar a q7
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q12(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='_'){
                sb.setCharAt(pos,'_');
                q12(sb.toString(),pos+1);//Si lee _, escribe _ y derecha. Mandar a q12
            }else if(cadena.charAt(pos)=='='){
                sb.setCharAt(pos,'=');
                q13(sb.toString(),pos+1);//Si lee =, escribir = y derecha. Mandar a q13
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q13(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if(cadena.charAt(pos)=='1'){
                sb.setCharAt(pos,'1');
                q13(sb.toString(),pos+1);//Si lee 1, escribe 1 y derecha. Mandar a q13
            }else if(cadena.charAt(pos)=='0'){
                sb.setCharAt(pos,'0');
                q13(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q13
            }else if(cadena.charAt(pos)=='A'){
                sb.setCharAt(pos,'0');
                q14(sb.toString(),pos+1);//Si lee A, escribir 0 y derecha. Mandar a q14
            }else if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,'1');
                q7(sb.toString(),pos-1);//Si lee espacio vacío, escribir 1 e izquierda. Mandar a q7
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q14(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);
            if((cadena.charAt(pos)+"").equals(" ")){
                sb.setCharAt(pos,'A');
                q7(sb.toString(),pos-1);//Si lee espacio vacío, escribe A e izquierda. Mandar a q7
            }else
                System.out.println("Cadena no válida");
        }else
            System.out.println("Cadena no válida");
    }
    public void q15(String cadena, int pos){
        if(pos<cadena.length()){
            sb=new StringBuilder(cadena);

            switch(cadena.charAt(pos)){
                case '1':
                    sb.setCharAt(pos,'1');
                    q15(sb.toString(),pos+1);//Si lee 1, escribe 1 y derecha. Mandar a q15
                    break;
                case '0':
                    sb.setCharAt(pos,'0');
                    q15(sb.toString(),pos+1);//Si lee 0, escribir 0 y derecha. Mandar a q15
                    break;
                case '=':
                    sb.setCharAt(pos,'=');
                    q15(sb.toString(),pos+1);//Si lee =, escribir = y derecha. Mandar a q15
                    break;
                case '+':
                    sb.setCharAt(pos,'+');
                    q15(sb.toString(),pos+1);//Si lee +, escribir + y derecha. Mandar a q15
                    break;
                case '.':
                    sb.setCharAt(pos,'.');
                    q15(sb.toString(),pos+1);//Si lee punto, escribir punto y derecha. Mandar a q15
                    break;
                case '_':
                    sb.setCharAt(pos,'_');
                    q15(sb.toString(),pos+1);//Si lee _, escribir _ y derecha. Mandar a q15
                    break;
                case 'A':
                    sb.setCharAt(pos,'1');
                    halt(sb.toString());//Si lee espacio vacío, escribir 1 e izquierda. Mandar a halt
                    break;
                case ' ':
                    sb.setCharAt(pos,' ');
                    halt(sb.toString());//Si lee espacio vacío, escribir 1 e izquierda. Mandar a halt
                    break;
                default:
                    System.out.println("Cadena no válida");
            }
        }else
            System.out.println("Cadena no válida");
    }
    public void halt(String cadena){
        String []separar=cadena.split("=");
        String invertida=invertir(separar[1]);
        System.out.println(original+invertida);
        //System.out.println(cadena);

    }
    public String invertir(String cad){
        String invertida="";
        String []cadena=cad.split(" ");
        for (int i=0; i<cadena[0].length(); i++){
            invertida=invertida+cadena[0].charAt(cadena[0].length()-1-i);
        }
        return invertida;
    }
}