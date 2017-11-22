package com.theastrologist.domain;

public class SignDecan extends Decan {

    private final Sign baseSign;
    private final Sign relatedSign;

    public SignDecan(Degree relativeDegree, Sign baseSign) {
        super(relativeDegree);
        this.baseSign = baseSign;
        this.relatedSign = Sign.getSign(calculateRelatedHouseOrSign(this.decanNumber, baseSign.getSignNumber()));
    }

    public static SignDecan getDecan(Degree relativeDegree, Sign baseSign) {
        return new SignDecan(relativeDegree, baseSign);
    }

    public Sign getRelatedSign() {
        return relatedSign;
    }

    public Sign getBaseSign() {
        return baseSign;
    }
}
