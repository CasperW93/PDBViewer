package PDBExplorer.model;

import java.util.ArrayList;

// Class that represents a polymer (i.e. one molecule consisting of multiple amino acids) that makes up the total protein complex
public class PdbPolymer {

    private ArrayList<PdbMonomer> monomerList;
    private String label;
    private int polymerNumber;

    public PdbPolymer(String label, ArrayList<PdbMonomer> monomerList, int polymerNumber){
        this.label = label;
        this.monomerList = monomerList;
        this.polymerNumber = polymerNumber;
    }

    public ArrayList<PdbMonomer> getMonomerList() {
        return monomerList;
    }

    public String getLabel() {
        return label;
    }

    public int getPolymerNumber() {
        return polymerNumber;
    }


    public ArrayList<PdbAtom> getAtoms(){
        ArrayList<PdbMonomer> monoList = getMonomerList();
        ArrayList<PdbAtom> atoms = new ArrayList<>();

        for(int i =0; i < monoList.size(); i++){
            ArrayList<PdbAtom> monoAtoms = monoList.get(i).getAtomList();

            for(int j = 0; j < monoAtoms.size(); j++){
                atoms.add(monoAtoms.get(j));
            }

        }
        return atoms;
   }
}
