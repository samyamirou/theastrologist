package com.theastrologist.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Embeddable
public class SignDecan extends Decan {

    @Enumerated(EnumType.STRING)
    private Sign baseSign;

    @Enumerated(EnumType.STRING)
    private Sign relatedSign;

    public SignDecan() {
        super();
    }

    // Pour la serialization
    public SignDecan(Sign baseSign, Sign relatedSign) {
        this.baseSign = baseSign;
        this.relatedSign = relatedSign;
        this.decanNumber = calculateDecan(baseSign.getSignNumber(), relatedSign.getSignNumber());
    }

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
