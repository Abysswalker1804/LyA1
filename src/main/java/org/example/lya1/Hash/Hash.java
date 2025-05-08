package org.example.lya1.Hash;
import java.math.BigInteger;

public class Hash{
    public static void main(String args[]){
        Hash hash=new Hash();
        System.out.println(hash.hash("BEGIN"));
    }
    public String hash(String cadena){
        //Suma en base a carácteres de base 64
        char car;
        BigInteger mezcla,desp_16,desp_32,suma=new BigInteger("0");
        for(int i=0; i<cadena.length(); i++){
            car=cadena.charAt(i);
            suma=suma.add(new BigInteger(valor(car)+""));
        }
        //Desplazamiento a 16 y a 32 bits
        desp_16=suma.divide(new BigInteger("65536"));
        desp_32=suma.divide(new BigInteger("4294967296"));
        mezcla=suma;

        while(desp_16.equals(new BigInteger("0"))){
            mezcla=mezcla.multiply(suma);
            desp_16=desp_16.subtract(new BigInteger("1"));
        }
        BigInteger mezcla_intermedia=mezcla;
        while(desp_32.equals(new BigInteger("0"))){
            mezcla=mezcla.multiply(mezcla_intermedia);
            desp_32=desp_32.subtract(new BigInteger("1"));
        }

        return (mezcla.mod(new BigInteger("65536"))).mod(new BigInteger("10000")).toString();
    }
    private int valor(char car){
        int valor;
        switch (car) {
            // Números del 1 al 9
            case '1': valor = 1; break;
            case '2': valor = 2; break;
            case '3': valor = 3; break;
            case '4': valor = 4; break;
            case '5': valor = 5; break;
            case '6': valor = 6; break;
            case '7': valor = 7; break;
            case '8': valor = 8; break;
            case '9': valor = 9; break;

            // Letras minúsculas a-z (valores 10-35)
            case 'a': valor = 10; break;
            case 'b': valor = 11; break;
            case 'c': valor = 12; break;
            case 'd': valor = 13; break;
            case 'e': valor = 14; break;
            case 'f': valor = 15; break;
            case 'g': valor = 16; break;
            case 'h': valor = 17; break;
            case 'i': valor = 18; break;
            case 'j': valor = 19; break;
            case 'k': valor = 20; break;
            case 'l': valor = 21; break;
            case 'm': valor = 22; break;
            case 'n': valor = 23; break;
            case 'o': valor = 24; break;
            case 'p': valor = 25; break;
            case 'q': valor = 26; break;
            case 'r': valor = 27; break;
            case 's': valor = 28; break;
            case 't': valor = 29; break;
            case 'u': valor = 30; break;
            case 'v': valor = 31; break;
            case 'w': valor = 32; break;
            case 'x': valor = 33; break;
            case 'y': valor = 34; break;
            case 'z': valor = 35; break;

            // Letras mayúsculas A-Z (valores 36-61)
            case 'A': valor = 36; break;
            case 'B': valor = 37; break;
            case 'C': valor = 38; break;
            case 'D': valor = 39; break;
            case 'E': valor = 40; break;
            case 'F': valor = 41; break;
            case 'G': valor = 42; break;
            case 'H': valor = 43; break;
            case 'I': valor = 44; break;
            case 'J': valor = 45; break;
            case 'K': valor = 46; break;
            case 'L': valor = 47; break;
            case 'M': valor = 48; break;
            case 'N': valor = 49; break;
            case 'O': valor = 50; break;
            case 'P': valor = 51; break;
            case 'Q': valor = 52; break;
            case 'R': valor = 53; break;
            case 'S': valor = 54; break;
            case 'T': valor = 55; break;
            case 'U': valor = 56; break;
            case 'V': valor = 57; break;
            case 'W': valor = 58; break;
            case 'X': valor = 59; break;
            case 'Y': valor = 60; break;
            case 'Z': valor = 61; break;

            // Símbolos
            case '@': valor = 62; break;
            case '_': valor = 63; break;

            case'0':
            default: valor = 0; break;
        }
        return valor;
    }
}
