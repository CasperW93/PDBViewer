package PDBExplorer.window;

import PDBExplorer.model.PdbMonomer;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

import java.util.Collection;
import java.util.stream.Collectors;

public interface SelectionModel<PdbMonomer> {
    boolean select(PdbMonomer t);

    boolean setSelected(PdbMonomer t, boolean select);

    boolean isSelected(PdbMonomer t);

    boolean selectAll(Collection<PdbMonomer> list);

    void clearSelection();

    boolean clearSelection(PdbMonomer t);

    boolean clearSelection(Collection<PdbMonomer> set);

    ObservableSet<PdbMonomer> getSelectedItems();


    }





