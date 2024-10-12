package PDBExplorer.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;

public class PDBReadService extends Service<ArrayList<String>> {
    @Override
    protected Task<ArrayList<String>> createTask() {
        System.out.println("Invoked Service");
        return new PDBWebClient();
    }
}
