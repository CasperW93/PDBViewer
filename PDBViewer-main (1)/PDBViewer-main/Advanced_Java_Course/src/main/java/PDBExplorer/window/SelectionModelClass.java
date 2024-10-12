package PDBExplorer.window;

import PDBExplorer.model.PdbMonomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.*;
import java.util.stream.Collectors;


// SelectionModelClass based on lecture
public class SelectionModelClass implements SelectionModel<PdbMonomer> {

    ObservableSet<PdbMonomer> set = FXCollections.observableSet();


    @Override
    public boolean select(PdbMonomer t) {
        set.add(t);
        return true;
    }

    @Override
    public boolean setSelected(PdbMonomer t, boolean select) {
        if (select) {
            set.add(t);
            return true;

        } else {
            set.remove(t);
            return false;

        }
    }

    @Override
    public boolean isSelected(PdbMonomer t) {
        if(set.contains(t)){
            return true;
        }else{
            return false;


        }
    }

    @Override
    public boolean selectAll(Collection<PdbMonomer> list) {

        set.addAll(list);

        return true;
    }

    @Override
    public void clearSelection() {
        set.clear();

    }

    @Override
    public boolean clearSelection(PdbMonomer t) {
        set.remove(t);
        return true;
    }

    @Override
    public boolean clearSelection(Collection<PdbMonomer> list) {
        set.removeAll(list);
        return true;
    }

    @Override
    public ObservableSet<PdbMonomer> getSelectedItems() {
        return set;
    }

    public static <Sphere> Collection<? extends Sphere> flatten(Collection<? extends ArrayList<Sphere>> lists) {
        return lists.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static ArrayList<Sphere> flat(HashMap<PdbMonomer, ArrayList<Sphere>> map){
        ArrayList<Sphere> sphereList = new ArrayList<>();

        for(PdbMonomer key : map.keySet()){



           ArrayList<Sphere> list = new ArrayList<>();

           list.addAll(map.get(key));

            sphereList.addAll(list);

        }



        return sphereList;
    }



    public static <T> void updateOpacity(Collection<? extends Sphere> list, double opacity) {
        for (var shape : list) {
            var color = ((PhongMaterial) shape.getMaterial()).getDiffuseColor();
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
            ((PhongMaterial) shape.getMaterial()).setDiffuseColor(color);
        }
    }
}