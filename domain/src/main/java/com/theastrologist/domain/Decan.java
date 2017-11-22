package com.theastrologist.domain;


public abstract class Decan  implements SkyArtefact {
    protected int decanNumber;
    protected transient Degree relativeDegree;

    public Decan(Degree relativeDegree) {
        this.relativeDegree = relativeDegree;
        decanNumber = getDecan(relativeDegree);
    }

    private int getDecan(Degree relativeDegree) {
        double baseDegree = relativeDegree.getBaseDegree();
        if(baseDegree < 0 || baseDegree >= 30) {
            throw new ArrayIndexOutOfBoundsException("Degree must be between 0 and 30 excluded. Actual : " + baseDegree);
        }
        return (int) (baseDegree / 10) + 1;
    }

    public int getDecanNumber() {
        return decanNumber;
    }

    protected int calculateRelatedHouseOrSign(int decanNumber, int baseHouseOrSign) {
        int resultedHouseNumber = baseHouseOrSign + ((decanNumber - 1) * 4);
        if(resultedHouseNumber > 12) {
            resultedHouseNumber = resultedHouseNumber - 12;
        }
        return resultedHouseNumber;
    }
}
