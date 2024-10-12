package PDBExplorer.model;

import Assignment_5.model.Atom;
import Assignment_5.model.AtomicSpecies;
import Assignment_5.model.Bond;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static PDBExplorer.window.MoleculeFigure.atomToPolymer;

public class PdbIO {

    static HashMap<PdbAtom, String> atomToPolymerIO = new HashMap<>();

    // Translates the three-letter-code of an amino acid into the corresponding one-letter-code
    public static PdbMonomer oneLetterCode(PdbMonomer mono) {
        String label = mono.getLabel();

        if (label.equals("ALA")) {
            mono.setLabel("A");
        }
        if (label.equals("ARG")) {
            mono.setLabel("R");
        }
        if (label.equals("ASN")) {
            mono.setLabel("N");
        }
        if (label.equals("ASP")) {
            mono.setLabel("D");
        }
        if (label.equals("CYS")) {
            mono.setLabel("C");
        }
        if (label.equals("GLU")) {
            mono.setLabel("E");
        }
        if (label.equals("GLN")) {
            mono.setLabel("Q");
        }
        if (label.equals("GLY")) {
            mono.setLabel("G");
        }
        if (label.equals("HIS")) {
            mono.setLabel("H");
        }
        if (label.equals("ILE")) {
            mono.setLabel("I");
        }
        if (label.equals("LEU")) {
            mono.setLabel("L");
        }
        if (label.equals("LYS")) {
            mono.setLabel("K");
        }
        if (label.equals("MET")) {
            mono.setLabel("M");
        }
        if (label.equals("PHE")) {
            mono.setLabel("F");
        }
        if (label.equals("PRO")) {
            mono.setLabel("P");
        }
        if (label.equals("SER")) {
            mono.setLabel("S");
        }
        if (label.equals("THR")) {
            mono.setLabel("T");
        }
        if (label.equals("TRP")) {
            mono.setLabel("W");
        }
        if (label.equals("TYR")) {
            mono.setLabel("Y");
        }
        if (label.equals("VAL")) {
            mono.setLabel("V");
        }
        return mono;
    }

    // readAtoms() takes a pdb-textfile and creates all monomers, polymers and atoms based on this file
    public static PdbComplex readAtoms(String path) throws IOException {
        ArrayList<SecondaryStructure> secondaryStructures = new ArrayList<>();
        ArrayList<PdbAtom> atomList = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new StringReader(path));
        String record;
        ArrayList<String> aminoAcidList = new ArrayList<>();
        ArrayList<String> seqList = new ArrayList<>();

        int lineNumber = 0;
        PdbMonomer monomer = null;
        ArrayList<PdbMonomer> monomerList = new ArrayList<>();
        PdbPolymer polymer = null;
        int polymerID = 0;


        ArrayList<PdbPolymer> polymerList = new ArrayList<>();
        PdbComplex complex = new PdbComplex(polymerList);


        while ((record = bf.readLine()) != null) {
            String[] line = record.split("\\s+");

            String[] recordArray = record.split("");

            if (line[0].contains("ATOM")) {

                String aminoAcid = (recordArray[22] + recordArray[23] + recordArray[24] + recordArray[25]).trim();
                aminoAcidList.add(aminoAcid);
                seqList.add(recordArray[21]);


                if (lineNumber > 0 && !aminoAcidList.get(lineNumber - 1).equals(aminoAcidList.get(lineNumber)) && seqList.get(lineNumber - 1).equals(seqList.get(lineNumber)) ) {
                    ArrayList<PdbAtom> atomList2 = new ArrayList<>();
                    monomer = new PdbMonomer((recordArray[17] + recordArray[18] + recordArray[19]).trim(), "null", atomList2, Integer.parseInt((recordArray[22] + recordArray[23] + recordArray[24] + recordArray[25]).trim()));
                    monomer = oneLetterCode(monomer);
                    polymer.getMonomerList().add(monomer);




                }

                if (lineNumber > 0 && !seqList.get(lineNumber - 1).equals(seqList.get(lineNumber)) && !aminoAcidList.get(lineNumber - 1).equals(aminoAcidList.get(lineNumber))) {
                    //monomerID = 0;
                    ArrayList<PdbAtom> atomList2 = new ArrayList<>();
                    monomer = new PdbMonomer((recordArray[17] + recordArray[18] + recordArray[19]).trim(), "null", atomList2, Integer.parseInt((recordArray[22] + recordArray[23] + recordArray[24] + recordArray[25]).trim()));

                    ArrayList<PdbMonomer> monomerList2 = new ArrayList<>();
                    polymer = new PdbPolymer(recordArray[21], monomerList2, ++polymerID);
                    monomer = oneLetterCode(monomer);


                    polymer.getMonomerList().add(oneLetterCode(monomer));

                    complex.getPolymerList().add(polymer);



                }

                if (lineNumber == 0) {
                    monomer = new PdbMonomer((recordArray[17] + recordArray[18] + recordArray[19]).trim(), "null", atomList, Integer.parseInt((recordArray[22] + recordArray[23] + recordArray[24] + recordArray[25]).trim()));
                    polymer = new PdbPolymer(recordArray[21], monomerList, ++polymerID);
                    monomer = oneLetterCode(monomer);

                    polymer.getMonomerList().add(oneLetterCode(monomer));
                    complex.getPolymerList().add(polymer);



                }


                if ((recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim().contains("C")) {


                    Point3D location = new Point3D(Double.parseDouble((recordArray[30] + recordArray[31] + recordArray[32] + recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36] + recordArray[37]).trim()), Double.parseDouble((recordArray[38] + recordArray[39] + recordArray[40] + recordArray[41] + recordArray[42] + recordArray[43] + recordArray[44] + recordArray[45]).trim()), Double.parseDouble((recordArray[46] + recordArray[47] + recordArray[48] + recordArray[49] + recordArray[50] + recordArray[51] + recordArray[52] + recordArray[53]).trim()));

                    PdbAtom a = new PdbAtom("C", 0.3, Color.GRAY, (recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim(), Integer.parseInt(line[1]), location);
                    atomToPolymerIO.put(a, polymer.getLabel());
                    monomer.getAtomList().add(a);



                } else if ((recordArray[12]+ recordArray[13] + recordArray[14] + recordArray[15]).trim().contains("O")) {


                    Point3D location = new Point3D(Double.parseDouble((recordArray[30] + recordArray[31] + recordArray[32] + recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36] + recordArray[37]).trim()), Double.parseDouble((recordArray[38] + recordArray[39] + recordArray[40] + recordArray[41] + recordArray[42] + recordArray[43] + recordArray[44] + recordArray[45]).trim()), Double.parseDouble((recordArray[46] + recordArray[47] + recordArray[48] + recordArray[49] + recordArray[50] + recordArray[51] + recordArray[52] + recordArray[53]).trim()));

                    PdbAtom a = new PdbAtom("O", 0.2, Color.RED, (recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim(), Integer.parseInt(line[1]), location);
                    atomToPolymerIO.put(a, polymer.getLabel());
                    monomer.getAtomList().add(a);

                } else if ((recordArray[12]+ recordArray[13] + recordArray[14] + recordArray[15]).trim().contains("N")) {

                    Point3D location = new Point3D(Double.parseDouble((recordArray[30] + recordArray[31] + recordArray[32] + recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36] + recordArray[37]).trim()), Double.parseDouble((recordArray[38] + recordArray[39] + recordArray[40] + recordArray[41] + recordArray[42] + recordArray[43] + recordArray[44] + recordArray[45]).trim()), Double.parseDouble((recordArray[46] + recordArray[47] + recordArray[48] + recordArray[49] + recordArray[50] + recordArray[51] + recordArray[52] + recordArray[53]).trim()));

                    PdbAtom a = new PdbAtom("N", 0.22, Color.BLUE, (recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim(), Integer.parseInt(line[1]), location);
                    atomToPolymerIO.put(a, polymer.getLabel());
                    monomer.getAtomList().add(a);


                }else if ((recordArray[12]+ recordArray[13] + recordArray[14] + recordArray[15]).trim().contains("P")) {


                    Point3D location = new Point3D(Double.parseDouble((recordArray[30] + recordArray[31] + recordArray[32] + recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36] + recordArray[37]).trim()), Double.parseDouble((recordArray[38] + recordArray[39] + recordArray[40] + recordArray[41] + recordArray[42] + recordArray[43] + recordArray[44] + recordArray[45]).trim()), Double.parseDouble((recordArray[46] + recordArray[47] + recordArray[48] + recordArray[49] + recordArray[50] + recordArray[51] + recordArray[52] + recordArray[53]).trim()));

                    PdbAtom a = new PdbAtom("P", 0.4, Color.PURPLE, (recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim(), Integer.parseInt(line[1]), location);
                    atomToPolymerIO.put(a, polymer.getLabel());
                    monomer.getAtomList().add(a);
                }else if ((recordArray[12]+ recordArray[13] + recordArray[14] + recordArray[15]).trim().contains("S")) {


                    Point3D location = new Point3D(Double.parseDouble((recordArray[30] + recordArray[31] + recordArray[32] + recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36] + recordArray[37]).trim()), Double.parseDouble((recordArray[38] + recordArray[39] + recordArray[40] + recordArray[41] + recordArray[42] + recordArray[43] + recordArray[44] + recordArray[45]).trim()), Double.parseDouble((recordArray[46] + recordArray[47] + recordArray[48] + recordArray[49] + recordArray[50] + recordArray[51] + recordArray[52] + recordArray[53]).trim()));

                    PdbAtom a = new PdbAtom("S", 0.35, Color.YELLOW, (recordArray[12] + recordArray[13] + recordArray[14] + recordArray[15]).trim(), Integer.parseInt(line[1]), location);
                    atomToPolymerIO.put(a, polymer.getLabel());
                    monomer.getAtomList().add(a);
                }

                lineNumber++;


            }

            // For secondary structure, check if line length is smaller than x. If yes, take line[a] and line[b] and split the first character from them
            if(line[0].contains("HELIX")){

                    SecondaryStructure secondaryStructure = new SecondaryStructure("Helix", Integer.parseInt((recordArray[21] + recordArray[22] + recordArray[23] + recordArray[24]).trim()), Integer.parseInt((recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36]).trim()), recordArray[19], recordArray[31]);
                    secondaryStructures.add(secondaryStructure);






            }
            if(line[0].contains("SHEET")){


                    SecondaryStructure secondaryStructure = new SecondaryStructure("Sheet", Integer.parseInt((recordArray[22] + recordArray[23] + recordArray[24] + recordArray[25]).trim()), Integer.parseInt((recordArray[33] + recordArray[34] + recordArray[35] + recordArray[36]).trim()), recordArray[21], recordArray[32]);
                    secondaryStructures.add(secondaryStructure);

            }
            if(line[0].contains("TURN")){

                SecondaryStructure secondaryStructure = new SecondaryStructure("Turn", Integer.parseInt(line[5]), Integer.parseInt(line[8]), line[4], line[7]);
                secondaryStructures.add(secondaryStructure);




            }
        }



        for(int i = 0; i < secondaryStructures.size(); i++){


            String type = secondaryStructures.get(i).getType();
            String polymerStart = secondaryStructures.get(i).getStartPolymerID();
            int IDStart = secondaryStructures.get(i).getStartID();
            int IDEnd = secondaryStructures.get(i).getEndID();
            int IDRange = 0;

            if(IDStart < 0 | IDEnd < 0){
                 IDRange = Math.abs(IDEnd) + Math.abs(IDStart) + 1;


            }else{
                 IDRange = (IDEnd - IDStart) + 1;
            }




            for(int j = 0; j < complex.getPolymerList().size(); j++){



                if(complex.getPolymerList().get(j).getLabel().equals(polymerStart)){

                    ArrayList<PdbMonomer> monoList = complex.getPolymerList().get(j).getMonomerList();

                    for(int k = 0; k < monoList.size(); k++){
                        if(monoList.get(k).getID() == IDStart){

                            for(int l = k; l < IDRange + k; l++){

                                complex.getPolymerList().get(j).getMonomerList().get(l).setType(type);


                            }

                        }

                    }


                }


            }

        }



        return complex;
    }



    // The readBonds method creates the bonds based on a list of atoms. It calculates the total distance between the atoms (x-distance plus y-distance plus z-distance)
    // The atoms with a distance of less than 2 to each other get bonded

    public static ArrayList<PdbBond> readBonds(ArrayList<PdbAtom> atomList) throws IOException {

        ArrayList<PdbBond> bondList = new ArrayList<>();
        double xDif;
        double yDif;
        double zDif;
        boolean sameBond = false;


        for (int i = 0; i < atomList.size(); i++) {

            for (int j = 0; j < atomList.size(); j++) {

                if (i == j) {
                    continue;
                }

                xDif = (atomList.get(j).getLocation().getX() - atomList.get(i).getLocation().getX()) * (atomList.get(j).getLocation().getX() - atomList.get(i).getLocation().getX()) ;
                yDif = (atomList.get(j).getLocation().getY() - atomList.get(i).getLocation().getY()) * (atomList.get(j).getLocation().getY() - atomList.get(i).getLocation().getY());
                zDif = (atomList.get(j).getLocation().getZ() - atomList.get(i).getLocation().getZ()) * (atomList.get(j).getLocation().getZ() - atomList.get(i).getLocation().getZ());
                double totalDif = Math.sqrt(xDif + yDif + zDif);

                if(totalDif <= 2){
                    PdbBond b = new PdbBond(atomList.get(i).getID(), atomList.get(j).getID(), atomToPolymerIO.get(atomList.get(i)), atomToPolymerIO.get(atomList.get(j)));

                    if(bondList.size() == 0 | !sameBond){
                        bondList.add(b);

                    }
                    sameBond = false;



                }




            }
        }


        return bondList;
    }

    public static ArrayList<PdbAtom> allAtomsInComplex(PdbComplex c){
        ArrayList<PdbAtom> atomList = new ArrayList<>();

        for(int i = 0; i < c.getPolymerList().size(); i++){

            for(int j = 0; j < c.getPolymerList().get(i).getMonomerList().size(); j++){
                c.getPolymerList().get(i).getMonomerList().get(j).getAtomList();



                for(int k = 0; k < c.getPolymerList().get(i).getMonomerList().get(j).getAtomList().size(); k++){

                    atomList.add(c.getPolymerList().get(i).getMonomerList().get(j).getAtomList().get(k));

                }



            }


        }







        return atomList;
    }


    public static PdbAtom searchAtomWithID(int ID, ArrayList<PdbAtom> list){
        PdbAtom atom = null;

        for(int i = 0; i < list.size(); i++){
            int atomID = list.get(i).getID();

            if(atomID == ID){
                atom = list.get(i);
                break;
            }

        }

    return atom;
    }


}





