package PDBExplorer.window;

import javafx.scene.Group;

public class BallGroup {

    Group balls = new Group();
    static SelectionModelClass selectionModel = new SelectionModelClass();

    public Group getBalls() {
        return balls;
    }

    public static SelectionModelClass getSelectionModel() {
        return selectionModel;
    }
}
