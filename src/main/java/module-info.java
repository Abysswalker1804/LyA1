module org.example.lya1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;

    opens org.example.lya1 to javafx.fxml;
    exports org.example.lya1;
}