package PDBExplorer;

import PDBExplorer.model.PDBWebClient;
import PDBExplorer.window.WindowPresenter;
import PDBExplorer.window.WindowView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class ProteinViewerMain extends Application {

    // start method of the main class

    @Override
    public void start(Stage stage) throws IOException {


        var view = new WindowView();
        var controller = view.getController();

        var presenter = new WindowPresenter(stage, view);

        PDBWebClient pdb = new PDBWebClient();
        stage.setMaxWidth(5000);
        stage.setMaxHeight(1500);



        stage.setTitle("PDB Explorer");
        stage.setScene(new Scene(view.getRoot()));
        stage.show();
    }

    public static void main(String args[]){

        launch();
    }
}




