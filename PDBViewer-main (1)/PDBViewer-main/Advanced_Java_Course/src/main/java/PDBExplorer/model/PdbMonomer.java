package PDBExplorer.model;

import java.util.ArrayList;


// PdbMonomer represents one monomer (i.e. amino acid or nucleotide)
public class PdbMonomer {

    private ArrayList<PdbAtom> atomList;
    String label;
    String type;

    int ID;

    public PdbMonomer(String label, String type, ArrayList<PdbAtom> atomlist, int ID){
        this.label = label;
        this.type = type;
        this.atomList = atomlist;
        this.ID = ID;
    }

    public ArrayList<PdbAtom> getAtomList() {
        return atomList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label){
        this.label = label;

    }

    public void setType(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public int getID(){return ID;}

    public PdbAtom getAlpha(){
        PdbAtom atom = null;


        for(int i = 0; i < this.atomList.size(); i++){
            
            if(this.atomList.get(i).getRole().equals("CA")){
                atom = this.atomList.get(i);             

            }
        }
        
        return atom;


    }

    public PdbAtom  getBeta(){

        PdbAtom atom = null;


        for(int i = 0; i < this.atomList.size(); i++){

            if(this.atomList.get(i).getRole().equals("CB")){
                atom = this.atomList.get(i);

            }
        }

        return atom;

    }

    public boolean hasBeta(){
        boolean hasBeta = false;

        for(int i = 0; i < this.atomList.size(); i++){

            if(this.atomList.get(i).getRole().equals("CB")){
                hasBeta = true;

            }
        }


        return hasBeta;
    }
}
