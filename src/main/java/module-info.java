module es.iesfranciscodelosrios.Puerto {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens es.iesfranciscodelosrios.Puerto to javafx.fxml;
    exports es.iesfranciscodelosrios.Puerto;
}
