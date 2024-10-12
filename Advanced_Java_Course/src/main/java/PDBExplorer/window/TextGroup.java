package PDBExplorer.window;

import javafx.scene.Group;

public class TextGroup {
    Group textGroup = new Group();

    static SelectionModelClass textSelectionModel = new SelectionModelClass();

    public Group getTextGroup() {
        return textGroup;
    }

    public static SelectionModelClass getTextSelectionModel() {
        return textSelectionModel;
    }
}
