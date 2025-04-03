package org.example.lya1.Serializables;

import java.io.Serializable;

public class Simbolo implements Serializable {
    private String simbolo;
    private String tipo;
    private String valor;

    public Simbolo(String simbolo, String tipo, String valor){
        this.simbolo=simbolo;
        this.tipo=tipo;
        this.valor=valor;
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
