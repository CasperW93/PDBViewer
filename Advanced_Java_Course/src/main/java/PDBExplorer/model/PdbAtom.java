package PDBExplorer.model;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;


// PdbAtom represents one single atom
public class PdbAtom {

    String symbol;
    String role;
    int ID;
    Point3D loc;
    double radius;
    Color color;

    public PdbAtom(String letter, double radius, Color color, String role, int ID, Point3D location){
        this.color = color;
        this.radius = radius;
        this.symbol = letter;
        this.ID = ID;
        this.loc = location;
        this.role = role;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRole() {
        return role;
    }

    public int getID() {
        return ID;
    }

    public Point3D getLocation() {
        return loc;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }


}
