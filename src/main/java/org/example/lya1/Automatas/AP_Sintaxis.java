package org.example.lya1.Automatas;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AP_Sintaxis {
    private String [] tokens;
    private Pila pila;
    private boolean flag;
    public String errorMsg;
    public AP_Sintaxis(String [] tokens){
        this.tokens=tokens;
        flag=true;
    }
    public static void main(String args[]){
        AP_Sintaxis sintax=new AP_Sintaxis((".start dclr @var").split("\\s+"));
        System.out.println((sintax.evaluar())?"Válida":"No válida");
    }
    public boolean evaluar(){
        flag=true;
        q0(0);
        return flag;
    }
    private void q0(int pos){
        //inicio | ε | s
        if(pos< tokens.length){
            if (tokens[pos].equals(".start")){
                pila=new Pila("s");//Push inicial para inicializar la pila
                q1(pos+1);
            }else{
                errorMsg="Error de sintaxis cerca de \"" + tokens[pos] + "\"!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \"" + tokens[pos-1] + "\"!";
            flag=false;
        }
    }
    private void q1(int pos) {
        if(pos< tokens.length){
            String elemento;
            switch (tokens[pos]){
                case "add":
                case "ADD":
                    //suma | ε | +
                    pila.push("+");
                    q8(pos+1);
                    break;
                case "mul":
                case "MUL":
                    //multiplicacion | o | *
                    pila.push("*");
                    q8(pos+1);
                    break;
                case "sub":
                case "SUB":
                    //resta | ε | -
                    pila.push("-");
                    q8(pos+1);
                    break;
                case "div":
                case "DIV":
                    //division | ε | /
                    pila.push("/");
                    q8(pos+1);
                    break;
                case "print":
                case "PRINT":
                    //impresion | ε | p
                    pila.push("p");
                    q7(pos+1);
                    break;
                case "dclr":
                case "DCLR":
                    //declaracion | ε | d
                    pila.push("d");
                    q3(pos+1);
                    break;
                case "set":
                case "SET":
                    //asignacion | ε | a
                    pila.push("a");
                    q4(pos+1);
                    break;
                case ".end":
                    //fin | s | ε
                    if(pila.pop().equals("s"))
                        q2(pos+1);
                    else {
                        errorMsg="Error de sintaxis cerca de \"" + tokens[pos] + "\"!";
                        flag = false;
                    }
                    break;
                case "if":
                    //if | ε | f
                    pila.push("f");
                    q10(pos+1);
                    break;
                case "while":
                    elemento=pila.pop();
                    if(elemento.equals("x")){
                        //while | x | x
                        pila.push("x");
                        q10(pos+1);
                    }else{
                        //while | ε | i
                        pila.push(elemento);//Si no es una x, regresar a la pila
                        pila.push("i");
                        q10(pos+1);
                    }
                    break;
                case "do":
                    //do | ε | x
                    pila.push("x");
                    q1(pos+1);
                    break;
                case "finish":
                    elemento= pila.pop();
                    if(elemento.equals("i") || elemento.equals("f")){
                        q1(pos+1);
                    }else{
                        errorMsg="Estructura incorrecta de ciclo o condicional!";
                        flag=false;
                    }
                    break;
                case "else":
                    elemento= pila.pop();
                    if(elemento.equals("f")){
                        pila.push("f");
                        q1(pos+1);
                    }else{
                        errorMsg="Estructura incorrecta de ciclo o condicional!";
                        flag=false;
                    }
                    break;
                default:
                    errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba una instrucción!";
                    flag=false;
            }
        }else {
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag = false;
        }
    }
    private void q2(int pos) {/*Estado de aceptacion*/
        if(pos< tokens.length){
            //Si no se han consumido todos los tokens, falla porque no deben de quedar al llegar a este estado
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }else{
            if(pila.pop()==null){//Si la pila está vacía y ya no hay tokens, es aceptada
                flag=true;
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
                flag=false;
            }
        }
    }
    private void q3(int pos) {
        if(pos<tokens.length){
            if(AFD_Identificadores.evaluar(tokens[pos])){
                String emelento_pila=pila.pop();
                if (emelento_pila.equals("d")){
                    q6(pos+1);
                }else{
                    errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba una declaración!";
                    flag=false;
                }
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un identificador!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag=false;
        }
    }
    private void q4(int pos) {
        if(pos< tokens.length){
            if(AFD_Identificadores.evaluar(tokens[pos])){
                //identificador | ε | ε
                q5(pos+1);
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un identificador!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag=false;
        }
    }
    private void q5(int pos) {
        if(pos< tokens.length){
            if (AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos]) || AFD_cadena.evaluar(tokens[pos])){
                String elemento_pila=pila.pop();
                if(elemento_pila.equals("a")){
                    q1(pos+1);
                }else{
                    errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba una asignación!";
                    flag=false;
                }
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un identificador o valor!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag=false;
        }
    }
    private void q6(int pos) {
        if(pos< tokens.length){
            String elemento;
            switch (tokens[pos]){
                case "add":
                case "ADD":
                    //suma | ε | +
                    pila.push("+");
                    q8(pos+1);
                    break;
                case "mul":
                case "MUL":
                    //multiplicacion | o | *
                    pila.push("*");
                    q8(pos+1);
                    break;
                case "sub":
                case "SUB":
                    //resta | ε | -
                    pila.push("-");
                    q8(pos+1);
                    break;
                case "div":
                case "DIV":
                    //division | ε | /
                    pila.push("/");
                    q8(pos+1);
                    break;
                case "print":
                case "PRINT":
                    //impresion | ε | p
                    pila.push("p");
                    q7(pos+1);
                    break;
                case "dclr":
                case "DCLR":
                    //declaracion | ε | d
                    pila.push("d");
                    q3(pos+1);
                    break;
                case "set":
                case "SET":
                    //asignacion | ε | a
                    pila.push("a");
                    q4(pos+1);
                    break;
                case ".end":
                    //fin | s | ε
                    if(pila.pop().equals("s"))
                        q2(pos+1);
                    else {
                        errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
                        flag = false;
                    }
                    break;
                case "if":
                    //if | ε | f
                    pila.push("f");
                    q10(pos+1);
                    break;
                case "while":
                    elemento=pila.pop();
                    if(elemento.equals("x")){
                        //while | x | x
                        pila.push("x");
                        q10(pos+1);
                    }else{
                        //while | ε | i
                        pila.push(elemento);//Si no es una x, regresar a la pila
                        pila.push("i");
                        q10(pos+1);
                    }
                    break;
                case "do":
                    //do | ε | x
                    pila.push("x");
                    q1(pos+1);
                    break;
                case "finish":
                    elemento= pila.pop();
                    if(elemento.equals("i") || elemento.equals("f")){
                        q1(pos+1);
                    }else{
                        errorMsg="Estructura incorrecta de ciclo o condicional!";
                        flag=false;
                    }
                    break;
                case "else":
                    elemento= pila.pop();
                    if(elemento.equals("f")){
                        pila.push("f");
                        q1(pos+1);
                    }else{
                        errorMsg="Estructura incorrecta de ciclo o condicional!";
                        flag=false;
                    }
                    break;
                default:
                    if (AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos]) || AFD_cadena.evaluar(tokens[pos])){
                        q1(pos+1);
                    }else{
                        errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
                        flag=false;
                    }
            }
        }else {
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag = false;
        }
    }
    private void q7(int pos) {
        if(pos< tokens.length){
            if (AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos]) || AFD_cadena.evaluar(tokens[pos])) {
                String elemento_pila = pila.pop();
                if (elemento_pila.equals("p")) {
                    q1(pos + 1);
                } else {
                    errorMsg="Error de sintaxis cerca de \"" + tokens[pos] + "\"!\nSe esperaba una asignación!";
                    flag = false;
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de Sintaxis");
                //alert.setHeaderText("Algo salió mal...");
                alert.setContentText("Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un identificador o valor!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){}
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag=false;
        }
    }
    private void q8(int pos) {
        if(pos< tokens.length){
            if(AFD_Identificadores.evaluar(tokens[pos])){//Revisar si es un identificador
                //identificador | ε | ε
                q9(pos+1);
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un identificador!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos-1]+"\"!";
            flag=false;
        }
    }
    private void q9(int pos) {
        if(pos< tokens.length){
            if(AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos])){
                String elemento_pila= pila.pop();
                switch (elemento_pila){
                    case "+":
                    case "*":
                    case "-":
                    case "/":
                        q1(pos+1);
                        break;
                    default:
                        errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un valor numérico o idenfificador!";
                        flag=false;
                }
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un valor numérico o idenfificador!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }
    }
    private void q10(int pos){
        if(pos< tokens.length){
            switch (tokens[pos]){
                case "true":
                case "false":
                    q11(pos+1);
                    break;
                default:
                    if(AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos])){
                        pila.push("o");
                        q12(pos+1);
                    }else{
                        errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba una operación lógica o valor booleano!";
                        flag=false;
                    }
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }
    }
    private void q11(int pos){
        if(pos< tokens.length){
            String elemento;
            switch (tokens[pos]){
                case "then":
                    elemento= pila.pop();
                    if(elemento.equals("f") || elemento.equals("i")){
                        pila.push(elemento);
                        q1(pos+1);
                    }else {
                        errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nEstructura incorrecta de ciclo o condicional!";
                        flag=false;
                    }
                    break;
                case "finish":
                    if(pila.pop().equals("x"))
                        q1(pos+1);
                    break;
                default:
                    errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un \"then\" o \"finish\"!";
                    flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }
    }
    private void q12(int pos){
        if(pos< tokens.length){
            if(tokens[pos].equals("<") || tokens[pos].equals(">") || tokens[pos].equals("="))
                q13(pos+1);
            else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba un operador lógico!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }
    }
    private void q13(int pos){
        if(pos< tokens.length){
            if(AFD_Identificadores.evaluar(tokens[pos]) || AFD_numero.evaluar(tokens[pos])){
                if(pila.pop().equals("o"))
                    q11(pos+1);
                else{
                    errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nEstructura incorrecta de operación lógica";
                    flag=false;
                }
            }else{
                errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!\nSe esperaba una operación lógica o valor booleano!";
                flag=false;
            }
        }else{
            errorMsg="Error de sintaxis cerca de \""+tokens[pos]+"\"!";
            flag=false;
        }
    }
}

class Pila{
    private Nodo tope;
    public Pila(String tope){
        this.tope=new Nodo(tope);
    }
    public void push(String cad){
        if(tope==null){
            tope=new Nodo(cad);
        }else{
            Nodo aux=tope;
            tope=new Nodo(cad);
            tope.setAnt(aux);
        }
    }
    public String pop(){
        if(tope==null){
            return null;
        }else{
            if(tope.getAnt()==null){
                //Es el tope
                Nodo aux=tope;
                tope=null;
                return aux.getPalabra();
            }else{
                //No es el tope
                Nodo aux=tope;
                tope=tope.getAnt();
                return aux.getPalabra();
            }
        }
    }
}

class Nodo{
    private String palabra;
    private Nodo ant;
    public Nodo(String palabra){
        this.palabra=palabra;
        ant=null;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Nodo getAnt() {
        return ant;
    }

    public void setAnt(Nodo ant) {
        this.ant = ant;
    }
}
