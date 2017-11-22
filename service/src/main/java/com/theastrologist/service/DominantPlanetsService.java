package com.theastrologist.service;

import com.google.common.collect.Sets;
import com.theastrologist.domain.*;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.domain.aspect.AspectPosition;
import com.theastrologist.domain.planetvalue.DominantPlanets;
import com.theastrologist.domain.planetvalue.PlanetValue;
import com.theastrologist.domain.planetvalue.PlanetValueReasonType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.SortedMap;
import java.util.SortedSet;

@Service
public class DominantPlanetsService {
    private static final Logger LOG = Logger.getLogger(DominantPlanetsService.class);

    public DominantPlanetsService() {}

    public DominantPlanets getDominantPlanets(SkyPosition position) {
        DominantPlanets dominants = new DominantPlanets();
        dominants.setSignCalculation(getDominantPlanetsSigns(position));
        dominants.setHouseCalculation(getDominantPlanetsHouses(position));
        return dominants;
    }

    public SortedSet<PlanetValue> getDominantPlanetsSigns(SkyPosition position) {

        // Calcul des dominantes

        SortedSet<PlanetValue> dominantPlanets = Sets.newTreeSet();

        for (Planet planet : Planet.getRealPlanets()) {
            PlanetValue value = calculateDominantSigns(planet, position);
            dominantPlanets.add(value);
        }
        return dominantPlanets;
    }

    public SortedSet<PlanetValue> getDominantPlanetsHouses(SkyPosition position) {

        // Calcul des dominantes

        SortedSet<PlanetValue> dominantPlanets = Sets.newTreeSet();

        for (Planet planet : Planet.getRealPlanets()) {
            PlanetValue value = calculateDominantHouses(planet, position);
            dominantPlanets.add(value);
        }
        return dominantPlanets;
    }

    public PlanetValue calculateDominantSigns(Planet planet, SkyPosition position) {
        PlanetPosition planetPosition = position.getPlanetPosition(planet);
        PlanetValue value = new PlanetValue(planet);
        Sign sign = planetPosition.getSign();

        appendIfPlanetMaitrePrincipalSign(planet, value, position);

        boolean hasConjonctionWithLuminaire = appendIfHasConjunctionWithLuminaire(planet, value, position);
        boolean hasConjonctionWithMCSudNode = appendIfHasConjunctionWithMCOrSudNode(planet, value, position);


        if (!hasConjonctionWithLuminaire && !hasConjonctionWithMCSudNode && planet != Planet.LUNE && planet != Planet.SOLEIL) {
            appendIfIsInPrincipaleSign(planetPosition, value, position);
        }

        if (sign.isMasterPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.MASTER_SIGN, sign);
        }

        if (sign.isExaltedPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.EXALTED_SIGN, sign);
        }

        if (sign.isExilPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.DETRIMENT_SIGN, sign);
        }

        if (sign.isChutePlanet(planet)) {
            value.appendValue(PlanetValueReasonType.FALL_SIGN, sign);
        }

        return value;
    }

    public PlanetValue calculateDominantHouses(Planet planet, SkyPosition position) {
        PlanetPosition planetPosition = position.getPlanetPosition(planet);
        PlanetValue value = new PlanetValue(planet);
        House house = planetPosition.getHouse();

        appendIfPlanetMaitrePrincipalHouse(planet, value, position);

        boolean hasConjonctionWithLuminaire = appendIfHasConjunctionWithLuminaire(planet, value, position);
        boolean hasConjonctionWithMCSudNode = appendIfHasConjunctionWithMCOrSudNode(planet, value, position);

        if (!hasConjonctionWithLuminaire && !hasConjonctionWithMCSudNode && planet != Planet.LUNE && planet != Planet.SOLEIL) {
            appendIfIsInPrincipaleHouse(planetPosition, value, position);
        }

        if (house.isMasterPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.MASTER_HOUSE, house);
        }

        if (house.isExaltedPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.EXALTED_HOUSE, house);
        }

        if (house.isExilPlanet(planet)) {
            value.appendValue(PlanetValueReasonType.DETRIMENT_HOUSE, house);
        }

        if (house.isChutePlanet(planet)) {
            value.appendValue(PlanetValueReasonType.FALL_HOUSE, house);
        }

        return value;
    }

    private boolean appendIfHasConjunctionWithLuminaire(Planet planet, PlanetValue value, SkyPosition position) {
        boolean returnValue = false;
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectsMap = position.getAspects();
        SortedMap<Planet, AspectPosition> aspects = aspectsMap.get(planet);

        for (Planet principalePlanet : Planet.getPrincipalePlanets()) {
            if (aspects.containsKey(principalePlanet) &&
                    aspects.get(principalePlanet).getAspect() == Aspect.CONJONCTION) {
                value.appendValue(PlanetValueReasonType.CONJONCTION_SUN_MOON_AS, principalePlanet);
                returnValue = true;
            }
        }

        return returnValue;
    }

    private boolean appendIfHasConjunctionWithMCOrSudNode(Planet planet, PlanetValue value, SkyPosition position) {
        boolean returnValue = false;
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectsMap = position.getAspects();
        SortedMap<Planet, AspectPosition> aspects = aspectsMap.get(planet);

        for (Planet secondPlanet : Planet.getSecondPlanets()) {
            if (aspects.containsKey(secondPlanet) &&
                    aspects.get(secondPlanet).getAspect() == Aspect.CONJONCTION) {
                value.appendValue(PlanetValueReasonType.CONJONCTION_MC_SUD_NODE, secondPlanet);
                returnValue = true;
            }
        }

        return returnValue;
    }

    private void appendIfPlanetMaitrePrincipalSign(Planet planet, PlanetValue value, SkyPosition position) {
        if (isPlanetMasterSoleil(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_SIGN, position.getPlanetPosition(Planet.SOLEIL).getSign());
        }

        if (isPlanetMasterLune(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_SIGN, position.getPlanetPosition(Planet.LUNE).getSign());
        }

        if (isPlanetMasterAscendant(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_SIGN, position.getPlanetPosition(Planet.ASCENDANT).getSign());
        }

        if (isPlanetMasterNoeudSud(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_SIGN, position.getPlanetPosition(Planet.NOEUD_SUD_MOYEN).getSign());
        }
    }

    private boolean isPlanetMasterSoleil(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.SOLEIL).getSign().isMasterPlanet(planet);
    }

    private boolean isPlanetMasterLune(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.LUNE).getSign().isMasterPlanet(planet);
    }

    private boolean isPlanetMasterAscendant(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.ASCENDANT).getSign().isMasterPlanet(planet);
    }

    private boolean isPlanetMasterNoeudSud(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.NOEUD_SUD_MOYEN).getSign().isMasterPlanet(planet);
    }

    private void appendIfIsInPrincipaleSign(PlanetPosition planetPosition, PlanetValue value, SkyPosition position) {
        Sign sign = planetPosition.getSign();

        if (sign == position.getPlanetPosition(Planet.ASCENDANT).getSign()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_SIGN, position.getPlanetPosition(Planet.ASCENDANT).getSign());
        }

        if (sign == position.getPlanetPosition(Planet.LUNE).getSign()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_SIGN, position.getPlanetPosition(Planet.LUNE).getSign());
        }

        if (sign == position.getPlanetPosition(Planet.SOLEIL).getSign()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_SIGN, position.getPlanetPosition(Planet.SOLEIL).getSign());
        }
    }

    private void appendIfPlanetMaitrePrincipalHouse(Planet planet, PlanetValue value, SkyPosition position) {
        if (isPlanetMasterSoleilHouse(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_HOUSE, position.getPlanetPosition(Planet.SOLEIL).getHouse());
        }

        if (isPlanetMasterLuneHouse(planet, position)) {
            value.appendValue(PlanetValueReasonType.MAIN_HOUSE, position.getPlanetPosition(Planet.LUNE).getHouse());
        }
    }

    private boolean isPlanetMasterSoleilHouse(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.SOLEIL).getHouse().isMasterPlanet(planet);
    }

    private boolean isPlanetMasterLuneHouse(Planet planet, SkyPosition position) {
        return position.getPlanetPosition(Planet.LUNE).getHouse().isMasterPlanet(planet);
    }

    private void appendIfIsInPrincipaleHouse(PlanetPosition planetPosition, PlanetValue value, SkyPosition position) {
        House house = planetPosition.getHouse();

        if (house == position.getPlanetPosition(Planet.SOLEIL).getHouse()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_HOUSE, position.getPlanetPosition(Planet.SOLEIL).getHouse());
        }

        if (house == position.getPlanetPosition(Planet.LUNE).getHouse()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_HOUSE, position.getPlanetPosition(Planet.LUNE).getHouse());
        }

        if (house == position.getPlanetPosition(Planet.NOEUD_SUD_MOYEN).getHouse()) {
            value.appendValue(PlanetValueReasonType.IS_IN_MAIN_HOUSE, position.getPlanetPosition(Planet.NOEUD_SUD_MOYEN).getHouse());
        }
    }
}
