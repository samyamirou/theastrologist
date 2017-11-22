package com.theastrologist.domain.planetvalue;

import com.theastrologist.domain.SkyArtefact;

/**
 * Created by Samy on 26/07/2015.
 */
public class PlanetValueReason {
    private final int points;
    private PlanetValueReasonType reasonType;
    private SkyArtefact artefact;

    public PlanetValueReason(PlanetValueReasonType reason, SkyArtefact skyArtefact, int points) {
        reasonType = reason;
        artefact = skyArtefact;
        this.points = points;
    }
}
