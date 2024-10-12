package PDBExplorer.window;

import PDBExplorer.model.PdbAtom;
import PDBExplorer.model.PdbMonomer;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.HashMap;
import java.util.Map;

import static PDBExplorer.window.MoleculeFigure.*;
import static PDBExplorer.window.TextGroup.textSelectionModel;
import static PDBExplorer.window.WindowPresenter.monomerToText;

public class Ball extends BallGroup {

    static Point3D locationSphere;




    // The Ball class describes the shapes of the atoms (i.e. spheres). A 3D atom shape consists of a location, radius, and color.


    public static Sphere create(Point3D location, double radius, Color col, int atomID, Map<Sphere, Integer> sphereToAtomID, Map<Integer, PdbAtom> atomIDToAtom, Map<PdbAtom, PdbMonomer> atomToMonomer ){

        Sphere sphere = new Sphere(radius, 5);
        sphere.setTranslateX(location.getX());
        sphere.setTranslateY(location.getY());
        sphere.setTranslateZ(location.getZ());
        var material=new PhongMaterial();
        material.setDiffuseColor(col);
        material.setSpecularColor(Color.WHITE);
        sphere.setMaterial(material);
        locationSphere = location;

        sphere.setOnMouseClicked(e -> {

            int ID = sphereToAtomID.get(sphere);



                if(!e.isShiftDown()){


                    if(selectionModel.isSelected(atomToMonomer.get(atomIDToAtom.get(ID))) & selectionModel.set.size() == 1){
                        selectionModel.clearSelection();



                    }else{
                        selectionModel.clearSelection();

                        boolean isSelected = selectionModel.isSelected(atomToMonomer.get(atomIDToAtom.get(ID)));


                        selectionModel.setSelected(atomToMonomer.get(atomIDToAtom.get(ID)), !isSelected);

                        // textSelectionModel.setSelected(atomToMonomer.get(atomIDToAtom.get(ID)), !isTextSelected);



                    }
                }else{
                    boolean isSelected = selectionModel.isSelected(atomToMonomer.get(atomIDToAtom.get(ID)));
                    //boolean isTextSelected = textSelectionModel.isSelected(atomToMonomer.get(atomIDToAtom.get(ID)));


                    selectionModel.setSelected(atomToMonomer.get(atomIDToAtom.get(ID)), !isSelected);

                    // textSelectionModel.setSelected(atomToMonomer.get(atomIDToAtom.get(ID)), !isTextSelected);



                }




        });


        return sphere;
    }

    public static Point3D getLocationSphere() {
        return locationSphere;
    }
}
