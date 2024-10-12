package PDBExplorer.model;

public class SecondaryStructure {

    String type;
    String startPolymerID;
    String endPolymerID;

    int startID;
    int endID;
    int secStructureLength;

    public SecondaryStructure(String type, int startID, int endID, String startPolymerID, String endPolymerID){
        this.type = type;
        this.startID = startID;
        this.endID = endID;
        this.startPolymerID = startPolymerID;
        this.endPolymerID = endPolymerID;
    }

    public String getType() {
        return type;
    }

    public int getStartID() {
        return startID;
    }

    public int getEndID() {
        return endID;
    }

    public int getSecStructureLength() {
        return secStructureLength;
    }

    public String getStartPolymerID() {
        return startPolymerID;
    }

    public String getEndPolymerID() {
        return endPolymerID;
    }
}
