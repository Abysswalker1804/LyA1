package org.example.lya1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private static enum State{
        Q0,
        Q1,
        //Evité q2 debido a que es lo mismo si se llega a q3
        Q3
    }

    private static final List<String> palabrasReservadas= Arrays.asList("dclr","DCLR","set","SET","add","ADD","cmp","CMP","je","JE","jne","JNE","mul","MUL","div","DIV");

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
        crearTablaSimbolos();
        impimirTablaSimbolos(); //Prueba, borrar despues
        cda_editor=new CodeArea();
        IniciarEditor();

        Panel pnl_principal=new Panel();
        //MenuItems
        MenuItem mit_guardar=new MenuItem("Guardar");
        MenuItem mit_abrir=new MenuItem("Abrir");
        mit_abrir.setOnAction(event -> cargarArchivo(stage));

        MenuItem mit_buscar=new MenuItem("Buscar");
        MenuItem mit_buscar_reemplazar=new MenuItem("Buscar y Reemplazar");

        MenuItem mit_run=new MenuItem("Ejecutar código");
        //Menus
        Menu men_archivo=new Menu("Archivo");
        men_archivo.getItems().addAll(mit_guardar,mit_abrir);
        Menu men_editar=new Menu("Editar");
        men_editar.getItems().addAll(mit_buscar,mit_buscar_reemplazar);
        Menu men_run=new Menu("Ejecutar");
        men_run.getItems().addAll(mit_run);
        //MenuBar
        MenuBar mbr_principal=new MenuBar();
        mbr_principal.getMenus().addAll(men_archivo,men_editar,men_run);

        pnl_principal.setHeading(mbr_principal);
        pnl_principal.setBody(cda_editor);

        pnl_principal.setPadding(new Insets(5));


        escena=new Scene(pnl_principal,600,600);
        escena.getStylesheets().add(getClass().getResource("/css/main.css").toString());
    }

    private void IniciarEditor() {
        cda_editor.setStyle("-fx-font-size: 14px; -fx-font-family: Consolas;");
        cda_editor.textProperty().addListener((obs, texto, nuevoTexto) -> {
            if (!nuevoTexto.isEmpty() && !nuevoTexto.isBlank()) {
                cda_editor.setStyleSpans(0, identificarPalabras(nuevoTexto));
            }
        });
    }

    private StyleSpans<Collection<String>> identificarPalabras(String texto){
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();

        // Utilizamos una expresión regular para dividir el texto en palabras, incluyendo símbolos como puntuación
        String[] textoArreglo = texto.split("\\s+");  // Esto separa por espacios, saltos de línea, tabulaciones
        int posActual = 0; // Posición inicial en el texto

        int longitud_acumulada=0;
        for (String palabra : textoArreglo) {
            int length = palabra.length();

            if(posActual!=0){
                if (palabrasReservadas.contains(palabra)) {
                    creadorSpans.add(Collections.singleton("palabraReservada"), length+1);
                } else {
                    if(esAceptada(palabra)){
                        creadorSpans.add(Collections.singleton("identificador"),length+1);
                    }else{
                        if(palabra.equals(".start") || palabra.equals(".end")){
                            creadorSpans.add(Collections.singleton("iniciofin"),length+1);
                        }else{
                            if(esNumeroValido(palabra)){
                                creadorSpans.add(Collections.singleton("default"),length+1);
                            }else{
                                creadorSpans.add(Collections.singleton("error"), length+1);
                            }
                        }
                    }
                }
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
                            creadorSpans.add(Collections.singleton("error"), length);
                        }
                    }
                }
            }


            posActual += length + 1; // Avanzamos a la siguiente palabra considerando los espacios
            longitud_acumulada=longitud_acumulada+length;
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
        Simbolo [] arr_simbolo=new Simbolo[palabrasReservadas.size()];
        for(int i=0; i< palabrasReservadas.size(); i++){arr_simbolo[i]=new Simbolo(palabrasReservadas.get(i),"palabra_reservada",null);}
        try(ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream("TablaDeSimbolos.dat"))){
            for(int i=0; i< palabrasReservadas.size(); i++){oos.writeObject(arr_simbolo[i]);}
        }catch (IOException ioe){
            System.out.println("Error al guardar en el binario :( !!");
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
}