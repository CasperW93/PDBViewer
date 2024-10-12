package PDBExplorer.model;

import java.util.ArrayList;


// PdbComplex represents one conformation of a protein, consisting of one or multiple polymers
public class PdbComplex {

    private ArrayList<PdbPolymer> polymerList;

    public PdbComplex(ArrayList<PdbPolymer> polymerList){
        this.polymerList = polymerList;
    }

    public ArrayList<PdbPolymer> getPolymerList(){
        return this.polymerList;
    }
}
