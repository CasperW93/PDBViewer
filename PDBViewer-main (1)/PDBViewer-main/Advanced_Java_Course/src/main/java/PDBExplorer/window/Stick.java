package PDBExplorer.window;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;

// Class that creates cylinders as bonds betwee natoms
public class Stick {

    public static final double DEFAULT_RADIUS = 0.08;

    public static Cylinder create (Point3D a, Point3D b){
        var YAXIS = new Point3D(0, 100, 0);
        var midpoint = a.midpoint(b);
        var direction = b.subtract(a);
        var perpendicularAxis = YAXIS.crossProduct(direction);
        var angle = YAXIS.angle(direction);
        var cylinder = new Cylinder(DEFAULT_RADIUS, 100, 4);
        cylinder.setRotationAxis(perpendicularAxis);
        cylinder.setRotate(angle);
        cylinder.setTranslateX(midpoint.getX());
        cylinder.setTranslateY(midpoint.getY());
        cylinder.setTranslateZ(midpoint.getZ());
        cylinder.setScaleY(a.distance(b) / cylinder.getHeight());

        return cylinder;

    }
}
