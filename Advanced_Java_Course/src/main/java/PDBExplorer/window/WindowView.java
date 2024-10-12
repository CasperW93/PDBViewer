package PDBExplorer.window;

import PDBExplorer.window.WindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class WindowView {

    private Parent root;
    private WindowController controller;

    public WindowView() throws IOException {

        var input = Objects.requireNonNull(getClass().getResource("Window.fxml").openStream());

        var fxmlLoader = new FXMLLoader();
        fxmlLoader.load(input);
        controller = fxmlLoader.getController();
        root = fxmlLoader.getRoot();


    }

    public Parent getRoot() {
        return root;
    }

    public WindowController getController() {
        return controller;
    }
}
