package org.example.lya1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.lya1.Automatas.AFD_Identificadores;
import org.example.lya1.Automatas.AFD_cadena;
import org.example.lya1.Automatas.AP_Sintaxis;
import org.example.lya1.Hash.Hash;
import org.example.lya1.Serializables.Simbolo;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class HelloApplication extends Application {
    private Scene escena;
    private CodeArea cda_editor;
    private CodeArea cda_error;
    private boolean error_lexico=false;
    private static enum State{
        Q0,
        Q1,
        //Evité q2 debido a que es lo mismo si se llega a q3
        Q3
    }

    private static final List<String> palabrasReservadas= Arrays.asList("dclr","DCLR","set","SET","add","ADD","mul","MUL","div","DIV","sub","SUB","print","PRINT");

    private static enum StateNum{
        Q0,
        Q1,
        Q2,
        Q3
    }
    @Override
    public void start(Stage stage){
        CrearUI(stage);
        stage.setTitle("Compilador");
        stage.setScene(escena);
        stage.show();
        Image icon = new Image(getClass().getResourceAsStream("/images/othala.png"));
        stage.getIcons().add(icon);
    }
    private void CrearUI(Stage stage){
        //crearTablaSimbolos();
        //impimirTablaSimbolos();
        cda_editor=new CodeArea();
        IniciarEditor();

        Panel pnl_principal=new Panel();
        //MenuItems
        MenuItem mit_guardar=new MenuItem("Guardar");
        MenuItem mit_abrir=new MenuItem("Abrir");
        mit_abrir.setOnAction(event -> cargarArchivo(stage));

        MenuItem mit_buscar=new MenuItem("Buscar");
        MenuItem mit_buscar_reemplazar=new MenuItem("Buscar y Reemplazar");

        MenuItem mit_compilar=new MenuItem("Compilar");
        mit_compilar.setOnAction(event -> {
            if(!error_lexico){//Si no hay error léxico
                String mensaje_lex="Evaluación léxica correcta!";
                StyleSpans<Collection<String>> spans_lex = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("compilacion"), mensaje_lex.length()).create();
                cda_error.replaceText(mensaje_lex);
                cda_error.setStyleSpans(0,spans_lex);

                AP_Sintaxis ap_sintax=new AP_Sintaxis(cda_editor.getText().split("\\s+"));
                if(ap_sintax.evaluar()){
                    String mensaje=mensaje_lex+"\nEvaluación sintáctica correcta!";
                    StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("compilacion"), mensaje.length()).create();
                    cda_error.replaceText(mensaje);
                    cda_error.setStyleSpans(0,spans);
                }else{
                    String mensaje=ap_sintax.errorMsg;
                    StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("error"), mensaje.length()).create();
                    cda_error.replaceText(mensaje);
                    cda_error.setStyleSpans(0,spans);
                }
                //crearTablaSimbolos();//Tabla de simbolos crear
                //impimirTablaSimbolos();
                //System.out.println(AFD_cadena.evaluar('"'+"cadena"+'"'));
            }else{
                String mensaje="No puede compilarse el código porque hay simbolos o palabras no reconocidas!";
                StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("error"), mensaje.length()).create();
                cda_error.replaceText(mensaje);
                cda_error.setStyleSpans(0,spans);
            }

        });
        //Menus
        Menu men_archivo=new Menu("Archivo");
        men_archivo.getItems().addAll(mit_guardar,mit_abrir);
        Menu men_editar=new Menu("Editar");
        men_editar.getItems().addAll(mit_buscar,mit_buscar_reemplazar);
        Menu men_run=new Menu("Ejecutar");
        men_run.getItems().addAll(mit_compilar);
        //MenuBar
        MenuBar mbr_principal=new MenuBar();
        mbr_principal.getMenus().addAll(men_archivo,men_editar,men_run);

        //Seccion de errores
        cda_error=new CodeArea();
        cda_error.setEditable(false);
        cda_error.setMinHeight(100);
        cda_error.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 5px;");

        pnl_principal.setHeading(mbr_principal);
        pnl_principal.setBody(cda_editor);
        pnl_principal.setFooter(cda_error);

        pnl_principal.setPadding(new Insets(5));


        escena=new Scene(pnl_principal,600,600);
        escena.getStylesheets().add(getClass().getResource("/css/main.css").toString());
    }

    private void IniciarEditor() {
        cda_editor.setStyle("-fx-font-size: 14px; -fx-font-family: Consolas;");
        cda_editor.textProperty().addListener((obs, texto, nuevoTexto) -> {
            if (!nuevoTexto.isEmpty() && !nuevoTexto.isBlank()) {
                cda_editor.setStyleSpans(0, identificarPalabras(nuevoTexto));
                //Aquí se revisa la sintaxis
            }
        });
    }

    private StyleSpans<Collection<String>> identificarPalabras(String texto){
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();
        //String[] textoArreglo = texto.split("\\s+");
        String [] textoArreglo=Tokenizador.tokenizar(texto);

        int longitud_acumulada=0;
        error_lexico=false;
        for (String palabra : textoArreglo) {
            int length = palabra.length();//Para aplicar los estilos en rangos correspondientes
            if (palabrasReservadas.contains(palabra)) {
                creadorSpans.add(Collections.singleton("palabraReservada"), length);
            } else {
                if(AFD_Identificadores.evaluar(palabra)){
                    creadorSpans.add(Collections.singleton("identificador"),length);
                }else{
                    if(palabra.equals(".start") || palabra.equals(".end")){
                        creadorSpans.add(Collections.singleton("iniciofin"),length);
                    }else{
                        if(esNumeroValido(palabra)){
                            creadorSpans.add(Collections.singleton("default"),length);
                        }else{
                            if(AFD_cadena.evaluar(palabra)){
                                creadorSpans.add(Collections.singleton("cadena"),length);
                            }else{
                                if(!palabra.isEmpty() && (palabra.charAt(0)==' ' || palabra.charAt(0)=='\n' || palabra.charAt(0)=='\t')){
                                    creadorSpans.add(Collections.singleton("default"),length);
                                }else{
                                    creadorSpans.add(Collections.singleton("error"), length);
                                    error_lexico=true;
                                }
                            }
                        }
                    }
                }
            }
            /*
            if(posActual!=0){

            }else{
                if (palabrasReservadas.contains(palabra)) {
                    creadorSpans.add(Collections.singleton("palabraReservada"), length);
                } else {
                    if(palabra.equals(".start") || palabra.equals(".end")){
                        creadorSpans.add(Collections.singleton("iniciofin"),length);
                    }else{
                        if(esNumeroValido(palabra)){
                            creadorSpans.add(Collections.singleton("default"),length);
                        }else{
                            if(AFD_cadena.evaluar(palabra)){
                                creadorSpans.add(Collections.singleton("cadena"),length);
                            }else{
                                creadorSpans.add(Collections.singleton("error"), length);
                            }
                        }
                    }
                }
            }*/

        }

        return creadorSpans.create();
    }

    private boolean esAceptada(String input) {
        State currentState = State.Q0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (currentState) {
                case Q0:
                    // Debe ser '@' para pasar a q1
                    if (c == '@') {
                        currentState = State.Q1;
                    } else {
                        return false; // Rechaza
                    }
                    break;

                case Q1:
                    // Debe ser letra, dígito o '_' para pasar a q3
                    if (esLetra(c) || esDigito(c) || c == '_') {
                        currentState = State.Q3;
                    } else {
                        return false; // Rechaza
                    }
                    break;

                case Q3:
                    // En q3 se aceptan más letras, dígitos o '_'
                    if (esLetra(c) || esDigito(c) || c == '_') {
                        // Se mantiene en q3
                        currentState = State.Q3;
                    } else {
                        return false; // Rechaza
                    }
                    break;
            }
        }

        // Acepta solo si termina en q3
        return (currentState == State.Q3);
    }


    private boolean esLetra(char c) {
        return ( (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z')  ||
                c == 'ñ' || c == 'Ñ');
        //Acepta char desde a a z, también mayúsculas y ñ min y mayus
    }

    private boolean esDigito(char c) {
        return (c >= '0' && c <= '9');
    }

    public static void main(String[] args) {
        launch();
    }

    private boolean esNumeroValido(String input) {
        StateNum currentStateNum = StateNum.Q0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (currentStateNum) {
                case Q0:
                    if (esDigito(c)) {
                        currentStateNum = StateNum.Q1;
                    } else {
                        return false;
                    }
                    break;

                case Q1:
                    if (esDigito(c)) {
                        currentStateNum = StateNum.Q1;
                    } else if (c == '.') {
                        currentStateNum = StateNum.Q2;
                    } else {
                        return false;
                    }
                    break;

                case Q2:
                    if (esDigito(c)) {
                        currentStateNum = StateNum.Q3;
                    } else {
                        return false;
                    }
                    break;

                case Q3:
                    if (esDigito(c)) {
                        currentStateNum = StateNum.Q3;
                    } else {
                        return false;
                    }
                    break;
            }
        }

        // La cadena se acepta si termina en Q1 (entero) o en Q3 (flotante válido).
        return (currentStateNum == StateNum.Q1 || currentStateNum == StateNum.Q3);
    }

    private void cargarArchivo(Stage stage){
        FileChooser cargador = new FileChooser();
        cargador.setTitle("Cargar Archivo");
        cargador.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto","*.txt"));
        File archivo=cargador.showOpenDialog(stage);
        if(archivo!=null){
            try{
                String contenido = Files.readString(Path.of(archivo.getAbsolutePath()));
                cda_editor.replaceText(contenido);
            }catch(IOException ioe){ioe.printStackTrace();}
        }
    }
    private void crearTablaSimbolos(){
        boolean continuar=true;

        String [] contenido=cda_editor.getText().split("\\s+");
        List<Simbolo> lista_identificadores=new ArrayList<>();
        for(String token: contenido){
            if(!(token.equals(".start") || token.equals(".end"))){
                if(!palabrasReservadas.contains(token)){
                    if(esAceptada(token)){
                        lista_identificadores.add(new Simbolo(token,"identificador",""));
                    }else if(esNumeroValido(token)){
                        lista_identificadores.add(new Simbolo(token,"número",token));
                    }else if(AFD_cadena.evaluar(token)){
                        lista_identificadores.add(new Simbolo(token,"cadena",token));
                    }else{
                        String mensaje="Símbolo no identificado cerca de '"+token+"'!";
                        StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("error"), mensaje.length()).create();
                        cda_error.replaceText(mensaje);
                        cda_error.setStyleSpans(0,spans);
                        continuar=false;
                        break;
                    }
                }
            }
        }
        if(continuar){
            Simbolo [] arr_simbolo=new Simbolo[palabrasReservadas.size()];
            for(int i=0; i< palabrasReservadas.size(); i++){arr_simbolo[i]=new Simbolo(palabrasReservadas.get(i),"palabra_reservada","");}
            try(ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream("TablaDeSimbolos.dat"))){
                for(int i=0; i< palabrasReservadas.size(); i++){oos.writeObject(arr_simbolo[i]);}

                String mensaje="Código compilado con éxito!";
                StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("compilacion"), mensaje.length()).create();
                cda_error.replaceText(mensaje);
                cda_error.setStyleSpans(0,spans);
            }catch (IOException ioe){
                System.out.println("Error al guardar en el binario :( !!");
            }
        }
    }

    private void impimirTablaSimbolos(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TablaDeSimbolos.dat"))){
           Simbolo simbolo=(Simbolo) ois.readObject();
            System.out.println("Objetos leídos del archivo:");
            while (simbolo!=null) {
                System.out.println(simbolo);
                simbolo=(Simbolo) ois.readObject();
            }
        }catch (EOFException eof){
            System.out.println("Fin del archivo");
        }catch (Exception e){System.out.println("Error al imprimir tabla de simbolos :( !!");}
    }

    private void crearArchivoSimbolos(){//Tamaño de registro de 156: String de 40, 17 y 21
        try{
            Hash hash=new Hash();
            RandomAccessFile raf = new RandomAccessFile("./archivo_simbolos.bin", "rw");
            raf.seek(0);
            String [] contenido=cda_editor.getText().split("\\s+");
            int posicion=0;
            for(String token: contenido){
                if(!(token.equals(".start") || token.equals(".end"))){
                    if(!palabrasReservadas.contains(token)){
                        if(esAceptada(token)){
                            Simbolo s;
                            if(esNumeroValido(contenido[posicion+1]) || AFD_cadena.evaluar(token)){ //Si lo que sigue del identificador es una cadena o número, lo guarda
                                s=new Simbolo(token,"identificador",contenido[posicion+1]);
                            }else {
                                s = new Simbolo(token, "identificador", "nulo");
                            }
                            raf.seek(156*Integer.parseInt(hash.hash(token)));
                            raf.writeUTF(s.getSimbolo());
                            raf.writeUTF(s.getTipo());
                            raf.writeUTF(s.getTipo());
                        }else if(esNumeroValido(token)){
                            //Numero
                        }else if(AFD_cadena.evaluar(token)){
                            //cadena
                        }else{
                            String mensaje="Símbolo no identificado cerca de '"+token+"'!";
                            StyleSpans<Collection<String>> spans = new StyleSpansBuilder<Collection<String>>().add(Collections.singleton("error"), mensaje.length()).create();
                            cda_error.replaceText(mensaje);
                            cda_error.setStyleSpans(0,spans);
                            //continuar=false;
                            break;
                        }
                    }
                }else{
                    raf.seek(156*Integer.parseInt(hash.hash(token)));
                    Simbolo s =new Simbolo(token, "palabra reservada", "");
                    raf.writeUTF(s.getSimbolo());
                    raf.writeUTF(s.getTipo());
                    raf.writeUTF(s.getTipo());
                    //Debería detectar .start y .end
                }
                posicion++;
            }
        }catch (FileNotFoundException fnfe){
            System.out.println("No se encontró el archivo!");
        }catch (IOException ioe){
            System.out.println("Excepción de datos serializados (IOE):");
            ioe.printStackTrace();
        };
    }
    private boolean revisarSintaxis(String texto){
        boolean flag=true;
        String[] textoArreglo = texto.split("\\s+");  // Esto separa por espacios, saltos de línea, tabulaciones
        for(String token: textoArreglo){
            if(token.equals(".start")){}
        }
        return flag;
    }
}

class Tokenizador{
    private static String [] tokens;
    /*prueba
    public static void main(String args[]){
        tokenizar(".start\n dclr @var .end");
        for(String token : tokens){
            System.out.println("\""+token+"\"");
        }
    }*/
    public static String[] tokenizar(String texto){
        tokens=new String[1];
        char caracter;
        String espacio="", palabra="";
        for(int i=0; i<texto.length();i++){
            caracter=texto.charAt(i);
            switch (caracter) {
                case ' ':
                case '\n':
                case '\t':
                    espacio = espacio + caracter;
                    if(!palabra.isEmpty()){
                        addToken(palabra);
                        palabra="";
                    }else if (tokens[0]==null){
                        tokens[0]=palabra;
                        palabra="";
                    }
                    break;
                default:
                    palabra=palabra+caracter;
                    if(!espacio.isEmpty()){
                        addToken(espacio);
                        espacio="";
                    }else if (tokens[0]==null){
                        tokens[0]=espacio;
                        espacio="";
                    }
            }
            if(i==texto.length()-1){
               if(!espacio.isEmpty()){
                   addToken(espacio);
                   espacio="";
               } else if(!palabra.isEmpty()){
                   addToken(palabra);
                   palabra="";
               }
            }
        }
        String [] aux=new String[tokens.length-1];
        for(int i=1; i< tokens.length; i++){
            aux[i-1]=tokens[i];
        }
        tokens=aux;
        return tokens;
    }
    private static void addToken(String token){
        String [] aux;
        if(tokens==null){
            aux=new String[1];
        }else{
            aux=tokens;
        }
        tokens=new String[aux.length+1];
        for(int i=0; i< aux.length; i++){if(aux[i]!=null){tokens[i]=aux[i];}}
        tokens[aux.length]=token;
    }
}