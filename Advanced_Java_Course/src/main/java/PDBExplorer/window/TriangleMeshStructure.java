package PDBExplorer.window;

import Assignment_5.model.Bond;
import PDBExplorer.model.PdbAtom;
import PDBExplorer.model.PdbBond;
import PDBExplorer.model.PdbIO;
import PDBExplorer.model.PdbMonomer;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.Map;

import static PDBExplorer.window.MoleculeFigure.*;

// Ribbons-Class
public class TriangleMeshStructure {

    private static int points = 0;

    // averageBetaFunction calculates the average distance from all beta-atoms in an amino acid to the alpha atom for a particular structure,
    // thereby generating a simulated beta-atom for amino-acids that do not have one
    public static Point3D averageBetaFunction(ArrayList<PdbMonomer> monomerList){


        double betaX = 0;
        double betaY = 0;
        double betaZ = 0;
        double alphaX = 0;
        double alphaY = 0;
        double alphaZ = 0;

        double alphaBetaDifX = 0;
        double alphaBetaDifY = 0;
        double alphaBetaDifZ = 0;


        int betaCounter = 0;

        for(int i = 0; i < monomerList.size(); i++){

            for(int j = 0; j < monomerList.get(i).getAtomList().size(); j++){

                if(monomerList.get(i).getAtomList().get(j).getRole().equals("CB")){
                     betaX = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getX();
                     betaY = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getY();
                     betaZ = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getZ();

                    if(monomerList.get(i).getAtomList().get(j).getRole().equals("CA")){
                        alphaX = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getX();
                        alphaY = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getY();
                        alphaZ = atomSphereLocationMap.get(monomerList.get(i).getAtomList().get(j)).getZ();

                    }

                    alphaBetaDifX += betaX - alphaX;
                    alphaBetaDifY += betaY - alphaY;
                    alphaBetaDifZ += betaZ - alphaZ;


                    betaCounter++;

                }



            }

        }


        double averageX = alphaBetaDifX/betaCounter;
        double averageY = alphaBetaDifY/betaCounter;
        double averageZ = alphaBetaDifZ/betaCounter;



        Point3D location = new Point3D(averageX, averageY, averageZ);


        return location;
    }

    // calculateReflectionPoint calculates the reflection point based on the locations of the alpha and beta C for a particular amino acid
    public static Point3D calculateReflectionPoint(Point3D alpha, Point3D beta){

        double alphaX = alpha.getX();
        double betaX = beta.getX();
        double alphaY = alpha.getY();
        double betaY = beta.getY();
        double alphaZ = alpha.getZ();
        double betaZ = beta.getZ();

        double differenceX = betaX - alphaX;
        double differenceY =   betaY - alphaY;
        double differenceZ =  betaZ - alphaZ;


        Point3D reflectionPoint = new Point3D((-1 * differenceX) + alphaX, (-1 * differenceY) + alphaY, (-1 * differenceZ) + alphaZ);


        return reflectionPoint;
    }

    // totalMesh uses all other functions in this class to create a concatenation of meshes.
    public static TriangleMesh totalMesh(ArrayList<PdbMonomer> monomerList, ArrayList<PdbBond> bondList, Map<Sphere, Integer> sphereToAtomID, Map<Integer, PdbAtom> atomIDToAtom, Map<PdbAtom, PdbMonomer> atomToMonomer ){
        points = 0;

        TriangleMesh tm = new TriangleMesh();
        Point3D betaReplacement = averageBetaFunction(monomerList);


        Point3D alpha1 = null;
        Point3D beta1 = null;
        Point3D projected1 = null;

        Point3D alpha2 = null;
        Point3D beta2 = null;
        Point3D projected2 = null;

        double betaReplacementX = 0;
        double betaReplacementY = 0;
        double betaReplacementZ = 0;


        for(int i = 0; i < bondList.size(); i++){

            int originID = bondList.get(i).getOriginID();
            int targetID = bondList.get(i).getTargetID();


            ArrayList<Point3D> locationList = new ArrayList<>();

            PdbMonomer mono1 = atomToMonomer.get(atomIDToAtom.get(originID));
            PdbMonomer mono2 = atomToMonomer.get(atomIDToAtom.get(targetID));




            if(mono1.hasBeta() & mono2.hasBeta()){
                 alpha1 = atomSphereLocationMap.get(mono1.getAlpha());
                 beta1 = atomSphereLocationMap.get(mono1.getBeta());
                 projected1 = calculateReflectionPoint(alpha1, beta1);


                 alpha2 = atomSphereLocationMap.get(mono2.getAlpha());
                 beta2 = atomSphereLocationMap.get(mono2.getBeta());
                 projected2 = calculateReflectionPoint(alpha2, beta2);






            }else if(mono1.hasBeta() & !mono2.hasBeta()){


                alpha1 = atomSphereLocationMap.get(mono1.getAlpha());
                beta1 = atomSphereLocationMap.get(mono1.getBeta());
                projected1 = calculateReflectionPoint(alpha1, beta1);

                alpha2 = atomSphereLocationMap.get(mono2.getAlpha());
                betaReplacementX = alpha2.getX() + betaReplacement.getX();
                betaReplacementY = alpha2.getY() + betaReplacement.getY();
                betaReplacementZ = alpha2.getZ() + betaReplacement.getZ();

                beta2 = new Point3D(betaReplacementX, betaReplacementY, betaReplacementZ);
                projected2 = calculateReflectionPoint(alpha2, beta2);


            }else if(!mono1.hasBeta() & mono2.hasBeta()){

                alpha1 = atomSphereLocationMap.get(mono1.getAlpha());
                betaReplacementX = alpha1.getX() + betaReplacement.getX();
                betaReplacementY = alpha1.getY() + betaReplacement.getY();
                betaReplacementZ = alpha1.getZ() + betaReplacement.getZ();

                beta1 = new Point3D(betaReplacementX, betaReplacementY, betaReplacementZ);
                projected1 = calculateReflectionPoint(alpha1, beta1);

                alpha2 = atomSphereLocationMap.get(mono2.getAlpha());
                beta2 = atomSphereLocationMap.get(mono2.getBeta());
                projected2 = calculateReflectionPoint(alpha2, beta2);



            }else if(!mono1.hasBeta() & !mono2.hasBeta()){

                alpha1 = atomSphereLocationMap.get(mono1.getAlpha());
                betaReplacementX = alpha1.getX() + betaReplacement.getX();
                betaReplacementY = alpha1.getY() + betaReplacement.getY();
                betaReplacementZ = alpha1.getZ() + betaReplacement.getZ();

                beta1 = new Point3D(betaReplacementX, betaReplacementY, betaReplacementZ);
                projected1 = calculateReflectionPoint(alpha1, beta1);

                alpha2 = atomSphereLocationMap.get(mono2.getAlpha());
                betaReplacementX = alpha2.getX() + betaReplacement.getX();
                betaReplacementY = alpha2.getY() + betaReplacement.getY();
                betaReplacementZ = alpha2.getZ() + betaReplacement.getZ();

                beta2 = new Point3D(betaReplacementX, betaReplacementY, betaReplacementZ);
                projected2 = calculateReflectionPoint(alpha2, beta2);


            }

            locationList.add(projected1);
            locationList.add(alpha1);
            locationList.add(beta1);


            locationList.add(projected2);
            locationList.add(alpha2);
            locationList.add(beta2);

            TriangleMesh t = createMeshSection(locationList);

            tm.getPoints().addAll(t.getPoints());
            tm.getTexCoords().addAll(t.getTexCoords());
            points = tm.getPoints().size()/3;
            tm.getFaces().addAll(t.getFaces());
            tm.getFaceSmoothingGroups().addAll(t.getFaceSmoothingGroups());



        }

        return tm;

    }


    // createMeshSection constructs 1 mesh section between two amino acids
    public static TriangleMesh createMeshSection(ArrayList<Point3D> pointList){


        TriangleMesh tm = new TriangleMesh();
        float[] pointArray = new float[pointList.size() * 3];


        for(int i = 0; i < pointList.size(); i++){

                pointArray[i*3] = (float) pointList.get(i).getX();
                pointArray[i*3 + 1] = (float) pointList.get(i).getY();
                pointArray[i*3 + 2] = (float) pointList.get(i).getZ();


        }
        tm.getPoints().addAll(pointArray);

        int[] smoothing = {1, 1, 1, 1, 2, 2, 2, 2};


        int[] faces = {
                points + 0, 0, points + 1, 0, points + 4, 0,
                points + 0, 0, points + 4, 0, points + 5, 0,
                points + 1, 0, points + 2, 0, points + 3, 0,
                points + 1, 0, points + 3, 0, points + 4, 0,

                points + 0, 0, points + 4, 0, points + 1, 0,
                points + 0, 0, points + 5, 0, points + 4, 0,
                points + 1, 0, points + 3, 0, points + 2, 0,
                points + 1, 0, points + 4, 0, points + 3, 0
        };

        tm.getTexCoords().addAll(0,0);

        tm.getFaces().addAll(faces);



        tm.getFaceSmoothingGroups().addAll(smoothing);


        return tm;
    }
}
