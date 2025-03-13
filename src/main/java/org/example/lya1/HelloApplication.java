package org.example.lya1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class HelloApplication extends Application {
    Scene escena;
    @Override
    public void start(Stage stage){
        CrearUI();
        stage.setTitle("Compilador");
        stage.setScene(escena);
        stage.show();
    }
    private void CrearUI(){
        TextArea txa_editor=new TextArea();

        Panel pnl_principal=new Panel();
        //MenuItems
        MenuItem mit_guardar=new MenuItem("Guardar");
        MenuItem mit_abrir=new MenuItem("Abrir");
        MenuItem mit_buscar=new MenuItem("Buscar");
        MenuItem mit_buscar_reemplazar=new MenuItem("Buscar y Reemplazar");
        //Menus
        Menu men_archivo=new Menu("Archivo");
        men_archivo.getItems().addAll(mit_guardar,mit_abrir);
        Menu men_editar=new Menu("Editar");
        men_editar.getItems().addAll(mit_buscar,mit_buscar_reemplazar);
        //MenuBar
        MenuBar mbr_principal=new MenuBar();
        mbr_principal.getMenus().addAll(men_archivo,men_editar);

        pnl_principal.setHeading(mbr_principal);
        pnl_principal.setBody(txa_editor);


        escena=new Scene(pnl_principal,600,600);
    }

    public static void main(String[] args) {
        launch();
    }
}