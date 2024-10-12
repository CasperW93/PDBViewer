package Assignment_5.model;

public class Bond {

    // A bond consists of an origin-index and a target-index of the atoms in an ArrayList.

    private int indexOrigin;
    private int indexTarget;

    public Bond(int indexOrigin, int indexTarget){
        this.indexOrigin = indexOrigin;
        this.indexTarget = indexTarget;
    }

    public int getIndexOrigin() {
        return indexOrigin;
    }

    public int getIndexTarget() {
        return indexTarget;
    }
}
