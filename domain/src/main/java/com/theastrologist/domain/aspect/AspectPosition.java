package com.theastrologist.domain.aspect;


import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;

public class AspectPosition {
    private Aspect aspect;
    private Planet planet;
    private Degree orbDelta;
    private transient Planet planetComparison;
    private transient PlanetPosition planetPosition;
    private transient PlanetPosition planetComparisonPosition;

    /**
     * Créer une position de transit
     * @param planet la planète natale
     * @param planetInTransit la planète qui transite
     * @param planetPosition position de la planète natale
     * @param planetInTransitPosition position de la planète qui transite
     * @return Le calcul du transit, ou alors NULL si ce n'est pas un aspect connu
     */
    public static AspectPosition createTransitPosition(Planet planet, Planet planetInTransit, PlanetPosition
            planetPosition, PlanetPosition planetInTransitPosition) {

        return getAspectOrTransitPosition(planet, planetInTransit, planetPosition, planetInTransitPosition, true);
    }

    /**
     * Créer un aspect
     * @param planet la planète natale
     * @param planetComparison la planète en comparaison
     * @param planetPosition position de la planète natale
     * @param planetComparisonPosition position de la planète qui transite
     * @return Le calcul du transit, ou alors NULL si ce n'est pas un aspect connu
     */
    public static AspectPosition createAspectPosition(Planet planet, Planet planetComparison, PlanetPosition
            planetPosition, PlanetPosition planetComparisonPosition) {

        return getAspectOrTransitPosition(planet, planetComparison, planetPosition, planetComparisonPosition, false);
    }

    private static AspectPosition getAspectOrTransitPosition(Planet planet, Planet planetComparison, PlanetPosition planetPosition, PlanetPosition planetComparisonPosition, boolean transit) {
        AspectPosition aspectPosition = new AspectPosition(planet, planetComparison, planetPosition,
                planetComparisonPosition);
        boolean shouldDestroy = aspectPosition.calculateAspectPosition(transit);

        return shouldDestroy ? null : aspectPosition;
    }


    private boolean calculateAspectPosition(boolean transit) {

        // -270 ou 270 ==> 90
        // -180 ==> 180
        // 300 ou -300 ==> 60
        // -240 ou 240 ==> 120

        boolean shouldDestroy = true;

        double planetDegree = planetPosition.getDegree().getBaseDegree();
        double planetComparisonDegree = planetComparisonPosition.getDegree().getBaseDegree();

        double comparison = planetComparisonDegree - planetDegree;
        boolean devant = comparison < 0;
        comparison = Math.abs(comparison);

        // 1 // 274 => -273 ==> 87 ==> écart de - 3
        // 274 // 1 => 273 => 87 ==> écart de -3
        // 1 // 268 => -267 ==> 93 ==> écart de + 3

        // 1 // 358 ==> -357 ==> 3 ==> écart de 3, offset négatif
        // 358 // 1 ==> 357 ==> 3 ==> écart de 3, offset positif

        if (comparison > 180) {
            // Ici on est dans le cas où
            comparison = Math.abs(comparison - 360);
        }

        for (Aspect aspect : Aspect.values()) {
            int angleSeparation = aspect.getAngleSeparation();
            double orbDelta = comparison - angleSeparation; // -3

            double aspectOrbe = transit ? aspect.getOrbeTransit() : aspect.getOrbe(); // + ou - 5


            if (Math.abs(orbDelta) <= aspectOrbe) {
                if (devant) {
                    orbDelta = -orbDelta;
                }
                this.aspect = aspect;
                this.orbDelta = new Degree(orbDelta);
                shouldDestroy = false;
                break;
            }
        }

        return shouldDestroy;
    }

    private AspectPosition(Planet planet, Planet planetComparison, PlanetPosition planetPosition, PlanetPosition
            planetComparisonPosition) {
        this.planet = planet;
        this.planetComparison = planetComparison;
        this.planetPosition = planetPosition;
        this.planetComparisonPosition = planetComparisonPosition;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public Planet getPlanet() {
        return planet;
    }

    public Planet getPlanetComparison() {
        return planetComparison;
    }

    public PlanetPosition getPlanetPosition() {
        return planetPosition;
    }

    public PlanetPosition getPlanetComparisonPosition() {
        return planetComparisonPosition;
    }

    public Degree getOrbDelta() {
        return orbDelta;
    }
}
