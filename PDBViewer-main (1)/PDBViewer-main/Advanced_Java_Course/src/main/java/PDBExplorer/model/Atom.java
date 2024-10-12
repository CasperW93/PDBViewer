package Assignment_5.model;

import javafx.geometry.Point3D;

public class Atom {

    // An atom consists of a type and a location
    private AtomicSpecies type;
    private Point3D location;

    public Atom(AtomicSpecies type, Point3D location){
        this.type = type;
        this.location = location;
    }

    public AtomicSpecies getType() {
        return type;
    }

    public Point3D getLocation() {
        return location;
    }
}
