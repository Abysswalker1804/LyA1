package org.example.lya1.Serializables;

import java.io.Serializable;

public class Simbolo implements Serializable {
    private String simbolo; //Máximo 40 longitud
    private String tipo; //Máximo 17 de longitud
    private String valor; //Máximo 21: 10 parte entera, un punto y 10 parte fraccionaria

    public Simbolo(String simbolo, String tipo, String valor){
        String simbolo_intermedio;
        if(simbolo.length()>=40){//Fijar tamaño del simbolo
            simbolo_intermedio=simbolo.substring(0,40);
        }else{
            simbolo_intermedio=simbolo;
            for (int i=0; (i+simbolo_intermedio.length())<40; i++){
                simbolo_intermedio=simbolo_intermedio+" ";
            }
        }
        this.simbolo=simbolo_intermedio;
        String tipo_intermedio;
        if(tipo.length()>=17){//Fijar tamaño del tipo
            tipo_intermedio=tipo.substring(0,17);
        }else{
            tipo_intermedio=tipo;
            for(int i=0; (i+tipo_intermedio.length())<17; i++){
                tipo_intermedio=tipo_intermedio+" ";
            }
        }
        this.tipo=tipo_intermedio;
        String valor_intermedio;
        if(valor.length()>=21){//Fijar el tamaño del valor
            valor_intermedio=valor.substring(0,21);
        }else{
            valor_intermedio=valor;
            for (int i=0; (i+valor_intermedio.length())<=21; i++){
                valor_intermedio=valor_intermedio+" ";
            }
        }
        this.valor=valor_intermedio;
    }

    @Override
    public String toString(){
        return "Simbolo{simbolo: "+simbolo+"| tipo: "+tipo+"| valor: "+valor+"}";
    }
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


}
