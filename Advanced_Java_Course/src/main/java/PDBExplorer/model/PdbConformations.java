package PDBExplorer.model;

import java.util.ArrayList;

// PdbConformations represents all conformations of a single protein through a list of complexes
public class PdbConformations {

    ArrayList<PdbComplex> complexList = new ArrayList<>();

    public PdbConformations(ArrayList<PdbComplex> complexList){
        this.complexList = complexList;
    }

    public ArrayList<PdbComplex> getComplexList() {
        return complexList;
    }
}
