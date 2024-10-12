package PDBExplorer.model;

// PdbBond represents a bond between two atoms
public class PdbBond {

    private int originID;
    private int targetID;

    private String originPolymer;
    private String targetPolymer;

    public PdbBond(int originID, int targetID, String originPolymer, String targetPolymer){
        this.originID = originID;
        this.targetID = targetID;
        this.originPolymer = originPolymer;
        this.targetPolymer = targetPolymer;
    }

    public int getOriginID() {
        return originID;
    }

    public int getTargetID() {
        return targetID;
    }

    public String getOriginPolymer() {
        return originPolymer;
    }

    public String getTargetPolymer() {
        return targetPolymer;
    }
}


