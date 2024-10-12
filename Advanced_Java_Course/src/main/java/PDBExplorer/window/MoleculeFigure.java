package PDBExplorer.window;

import PDBExplorer.model.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static PDBExplorer.window.TriangleMeshStructure.totalMesh;

// MoleculeFigure translates the atoms, monomers, and polymers into a collection of balls, sticks, and ribbons that belong to one PdbComplex
public class MoleculeFigure {


    int OriginID;

    static MeshView mv;

    static HashMap<PdbAtom, Point3D> atomSphereLocationMap = new HashMap<>();

    public static HashMap<PdbAtom, String> atomToPolymer;


   // static ArrayList<Map<Sphere, Integer>> mapList = new ArrayList();



    static ArrayList<Point3D> locationList = new ArrayList<>();
    static ArrayList<Sphere>    ballList;
    static Sphere monoSphere = new Sphere();
    static Sphere polySphere = new Sphere();
    static Sphere sphere = new Sphere();
    static Sphere secSphere = new Sphere();


    static ArrayList<PdbMonomer> totalMonomerList = new ArrayList<>();
    Group ballsFigure = new Group();
    Group sticksFigure = new Group();
    Group polymerBallsFigure = new Group();
    Group monomerBallsFigure = new Group();
    Group secBallsFigure = new Group();
    Group ribbonGroupFigure = new Group();


    Group totalRibbons = new Group();
    HashMap<PdbMonomer, ArrayList<Sphere>> item2shapes = new HashMap<>();
    Map<Sphere, Integer> sphereToAtomID = new HashMap<>();
    Map<Integer, PdbAtom> atomIDToAtom= new HashMap<>();
    Map<PdbAtom, PdbMonomer> atomToMonomer= new HashMap<>();


    // The compute-method takes a molecule and two (empty) groups as input. It then fills all groups (balls, monomerballs, polymerballs, secballs and sticks) with shapes, spheres for the balls group and cylinders for the sticks group


    public MoleculeFigure compute(PdbComplex model) throws IOException {

        MoleculeFigure mf = new MoleculeFigure();

        atomToPolymer = new HashMap<>();

        mf.item2shapes = new HashMap<>();
        mf.sphereToAtomID = new HashMap<>();
        mf.atomIDToAtom = new HashMap<>();
        mf.atomToMonomer = new HashMap<>();

        Random random = new Random();



        ArrayList<PdbAtom> atomList = PdbIO.allAtomsInComplex(model);
        ArrayList<PdbBond> bondList = PdbIO.readBonds(atomList);


        // calculate average x, y, z coordinates of all atoms
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;

        // The locations of all shapes are normalized/centered by subtracting the average X-, Y-, and Z-locations.

        for(int i = 0; i < atomList.size(); i++){
            sumX += atomList.get(i).getLocation().getX();
            sumY += atomList.get(i).getLocation().getY();
            sumZ += atomList.get(i).getLocation().getZ();
        }

        double averageX = sumX / atomList.size();
        double averageY = sumY / atomList.size();
        double averageZ = sumZ / atomList.size();

        ArrayList<Color> polyList = new ArrayList<>();

        ArrayList<Color> colorList = new ArrayList<>();
        Color color1 = Color.RED;
        Color color2 = Color.DARKBLUE;
        Color color3 = Color.GREEN;
        Color color4 = Color.PURPLE;
        Color color5 = Color.GRAY;
        Color color6 = Color.BLACK;
        Color color7 = Color.BEIGE;
        Color color8 = Color.TURQUOISE;
        Color color9 = Color.YELLOW;
        Color color10 = Color.BROWN;
        Color color11 = Color.PINK;
        Color color12 = Color.LIGHTCORAL;
        Color color13 = Color.LIGHTCYAN;
        Color color14 = Color.DARKGOLDENROD;
        Color color15 = Color.LAVENDERBLUSH;
        Color color16 = Color.ANTIQUEWHITE;
        Color color17 = Color.NAVAJOWHITE;
        Color color18 = Color.CORNSILK;
        Color color19 = Color.ROYALBLUE;
        Color color20 = Color.KHAKI;
        Color color21 = Color.LAWNGREEN;
        Color color22 = Color.MAGENTA;


        colorList.add(color1);
        colorList.add(color2);
        colorList.add(color3);
        colorList.add(color4);
        colorList.add(color5);
        colorList.add(color6);
        colorList.add(color7);
        colorList.add(color8);
        colorList.add(color9);
        colorList.add(color10);
        colorList.add(color11);
        colorList.add(color12);
        colorList.add(color13);
        colorList.add(color14);
        colorList.add(color15);
        colorList.add(color16);
        colorList.add(color17);
        colorList.add(color18);
        colorList.add(color19);
        colorList.add(color20);
        colorList.add(color21);
        colorList.add(color22);


        for(int i = 0; i < model.getPolymerList().size(); i++){

            float r = random.nextFloat();
            float b = random.nextFloat();
            float g = random.nextFloat();

            Color polyColor = new Color(r, b, g, 1);


            Group polymerMono = new Group();
            Group polymerPoly = new Group();
            Group polymerSec = new Group();
            Group polymerNormal = new Group();


            polyList.add(colorList.get(i));




            // Amino acids are colored using MAEditor coloring scheme
            for(int j = 0; j < model.getPolymerList().get(i).getMonomerList().size(); j++){
                PdbMonomer mono = model.getPolymerList().get(i).getMonomerList().get(j);
                Color monoColor = null;
                Color secColor = null;

                if(mono.getType().equals("Helix")){
                    secColor = Color.YELLOW;

                }
                else if(mono.getType().equals("Sheet")){
                    secColor = Color.GREEN;
                }
                else if(mono.getType().equals("null")){
                    secColor = Color.WHITE;
                }

                if(mono.getLabel().equals("G") | mono.getLabel().equals("A")){
                    monoColor = Color.LIGHTGREEN;
                }
                else if(mono.getLabel().equals("C") ){
                    monoColor = Color.GREEN;
                }
                else if(mono.getLabel().equals("D") | mono.getLabel().equals("E") | mono.getLabel().equals("N") | mono.getLabel().equals("Q") ){
                    monoColor = Color.DARKGREEN;
                }
                else if(mono.getLabel().equals("I") | mono.getLabel().equals("L") | mono.getLabel().equals("M") | mono.getLabel().equals("V")){
                    monoColor = Color.BLUE;
                }
                else if(mono.getLabel().equals("F") | mono.getLabel().equals("W") | mono.getLabel().equals("Y")){
                    monoColor = Color.PURPLE;
                }
                else if(mono.getLabel().equals("H")){
                    monoColor = Color.DARKBLUE;
                }
                else if(mono.getLabel().equals("K") | mono.getLabel().equals("R")){
                    monoColor = Color.ORANGE;
                }
                else if(mono.getLabel().equals("P")){
                    monoColor = Color.PINK;
                }
                else if(mono.getLabel().equals("S") | mono.getLabel().equals("T")){
                    monoColor = Color.RED;
                }else {
                    monoColor = Color.WHITE;
                }

                ballList = new ArrayList<>();


                for(int k = 0; k < model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().size(); k++){

                    double x = model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getLocation().getX() - averageX;
                    double y = model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getLocation().getY() - averageY;
                    double z = model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getLocation().getZ() - averageZ;

                    Point3D location = new Point3D(x, y, z);
                    locationList.add(location);



                    if(i < 22){
                        polySphere = Ball.create(location, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getRadius(), polyList.get(i), model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID(),  mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer);
                    }else{
                        polySphere = Ball.create(location, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getRadius(), polyColor, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID(),  mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer);

                    }


                    sphere = Ball.create(location, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getRadius(), model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getColor(), model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID() , mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer);
                    //balls.getChildren().add(sphere);

                    monoSphere = Ball.create(location, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getRadius(), monoColor, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID(), mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer);
                    secSphere = Ball.create(location, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getRadius(), secColor, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID(), mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer);
                    //monomerBalls.getChildren().add(monoSphere);
                    //secBalls.getChildren().add(secSphere);

                    polymerNormal.getChildren().add(sphere);
                    polymerPoly.getChildren().add(polySphere);
                    polymerMono.getChildren().add(monoSphere);
                    polymerSec.getChildren().add(secSphere);


                    atomToPolymer.put(model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k), model.getPolymerList().get(i).getLabel());


                    ballList.add(sphere);
                    ballList.add(monoSphere);
                    ballList.add(secSphere);
                    ballList.add(polySphere);

                    mf.sphereToAtomID.put(sphere, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID() );
                    mf.sphereToAtomID.put(monoSphere, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID() );
                    mf.sphereToAtomID.put(secSphere, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID() );
                    mf.sphereToAtomID.put(polySphere, model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID() );

                    mf.atomIDToAtom.put(model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k).getID(), model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k));

                    mf.atomToMonomer.put(model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k), model.getPolymerList().get(i).getMonomerList().get(j));


                    atomSphereLocationMap.put(model.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k), location);
                }

                totalMonomerList.add(model.getPolymerList().get(i).getMonomerList().get(j));

                mf.item2shapes.put(model.getPolymerList().get(i).getMonomerList().get(j), ballList);
                //monomerToTextTest.put()



            }

            mf.polymerBallsFigure.getChildren().add(polymerPoly);
            mf.monomerBallsFigure.getChildren().add(polymerMono);
            mf.secBallsFigure.getChildren().add(polymerSec);
            mf.ballsFigure.getChildren().add(polymerNormal);




        }

        ArrayList<String> polymerOverview = new ArrayList<>();

        Group polySticks = new Group();
        ArrayList<PdbBond> splitBonds = null;

        ArrayList<PdbMonomer> firstElementOfPolymer = new ArrayList<>();
        int previousElementIndex = 0;


        for(int j = 0; j < bondList.size(); j++){



            OriginID = bondList.get(j).getOriginID();
            int TargetID = bondList.get(j).getTargetID();


            polymerOverview.add(atomToPolymer.get(mf.atomIDToAtom.get(bondList.get(j).getOriginID())));

            double xOrigin = PdbIO.searchAtomWithID(OriginID, atomList).getLocation().getX() - averageX;
            double yOrigin = PdbIO.searchAtomWithID(OriginID, atomList).getLocation().getY() - averageY;
            double zOrigin = PdbIO.searchAtomWithID(OriginID, atomList).getLocation().getZ() - averageZ;

            double xTarget = PdbIO.searchAtomWithID(TargetID, atomList).getLocation().getX() - averageX;
            double yTarget = PdbIO.searchAtomWithID(TargetID, atomList).getLocation().getY() - averageY;
            double zTarget = PdbIO.searchAtomWithID(TargetID, atomList).getLocation().getZ() - averageZ;


            Point3D originLocation = new Point3D(xOrigin, yOrigin, zOrigin);
            Point3D targetLocation = new Point3D(xTarget, yTarget, zTarget);

            Cylinder cylinder = Stick.create(originLocation, targetLocation);




            if(polymerOverview.size() == 1){
                firstElementOfPolymer.add(mf.atomToMonomer.get(mf.atomIDToAtom.get(OriginID)));
                splitBonds = new ArrayList<>();
                mv = new MeshView();
                splitBonds.add(bondList.get(j));


                polySticks = new Group();
                polySticks.getChildren().add(cylinder);



            }else if(!polymerOverview.get(j).equals(polymerOverview.get(j - 1))){
                firstElementOfPolymer.add(mf.atomToMonomer.get(mf.atomIDToAtom.get(OriginID)));
                previousElementIndex = firstElementOfPolymer.size() - 2;

                // Ribbons are not created for nucleotides

                if(isNotNucleotide(firstElementOfPolymer.get(previousElementIndex))){
                    mv.setMesh(totalMesh(totalMonomerList, splitBonds, mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer));
                    mv.setMaterial(new PhongMaterial(Color.YELLOW));
                    mf.ribbonGroupFigure.getChildren().add(mv);
                    mv = new MeshView();


                }


                splitBonds = new ArrayList<>();


                mf.sticksFigure.getChildren().add(polySticks);
                polySticks = new Group();
                polySticks.getChildren().add(cylinder);

            }

            else{
                splitBonds.add(bondList.get(j));
                polySticks.getChildren().add(cylinder);

            }



        }


        if(isNotNucleotide(firstElementOfPolymer.get(previousElementIndex))){
            mv.setMesh(totalMesh(totalMonomerList, splitBonds, mf.sphereToAtomID, mf.atomIDToAtom, mf.atomToMonomer));
            mv.setMaterial(new PhongMaterial(Color.YELLOW));
            mf.ribbonGroupFigure.getChildren().add(mv);

        }



        mf.sticksFigure.getChildren().add(polySticks);



        return mf;
    }





    public Map<PdbMonomer, ArrayList<Sphere>> getItem2shapes() {
        return item2shapes;
    }

    public Map<Sphere, Integer> getSphereToAtomID() {
        return sphereToAtomID;
    }

    public Map<Integer, PdbAtom> getAtomIDToAtom() {
        return atomIDToAtom;
    }

    public Map<PdbAtom, PdbMonomer> getAtomToMonomer() {
        return atomToMonomer;
    }

    public boolean isNotNucleotide(PdbMonomer mono){
        boolean isNotNucleotide = true;

        if(mono.getLabel().equals("DA") ){
            isNotNucleotide = false;

        }
        else if(mono.getLabel().equals("DC") ){
            isNotNucleotide = false;


        }
        else if( mono.getLabel().equals("DG") ){
            isNotNucleotide = false;

        }
        else if(mono.getLabel().equals("DT")){
            isNotNucleotide = false;

        }



            return isNotNucleotide;
    }
}
