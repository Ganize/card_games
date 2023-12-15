module com.game.flashcard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.game.flashcard to javafx.fxml;
    exports com.game.flashcard;
}