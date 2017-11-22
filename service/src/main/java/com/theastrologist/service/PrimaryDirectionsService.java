package com.theastrologist.service;

import com.google.common.collect.Sets;
import com.theastrologist.domain.*;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.domain.primarydirection.PotentialDirection;
import com.theastrologist.domain.primarydirection.PrimaryDirection;
import com.theastrologist.util.CalcUtil;
import com.theastrologist.util.DateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swisseph.*;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

@Service
public class PrimaryDirectionsService {

    private static final Logger LOG = Logger.getLogger(PrimaryDirectionsService.class);
    public static final int MAX_YEARS_DURATION = 130;

    @Autowired
    private Swieph swiephService;

    private PrimaryDirectionsService() {
    }

    public SortedSet<PrimaryDirection> getPrimaryDirections(SkyPosition skyPosition) {
        // Créer tableau avec toutes les positions natales et les aspects potentiels
        Set<PotentialDirection> potentialDirections = getPotentialDirections(skyPosition);

        // Générer la liste des événements triée par date
        return generatePotentialDirections(skyPosition, potentialDirections);
    }

    public SortedSet<PrimaryDirection> generatePotentialDirections(SkyPosition skyPosition, Set<PotentialDirection> potentialDirections) {
        SortedSet<PrimaryDirection> directions = Sets.newTreeSet();

        DateTime natalDate = skyPosition.getDate();
        final DateTime maxDate = natalDate.plusYears(MAX_YEARS_DURATION);

        for (Planet planet : Planet.values()) {
            if (planet != Planet.ASCENDANT) {

                DateTime currentDate = natalDate;

                Iterator<PotentialDirection> iterator = potentialDirections.iterator();

                PlanetPosition natalPosition = null;

                while (currentDate.isBefore(maxDate)) {
                    PotentialDirection potentialDirection = iterator.next();

                    PlanetPosition directedPosition = potentialDirection.getPosition();
                    Aspect directionAspect = potentialDirection.getAspect();
                    Degree directedPositionDegree = directedPosition.getDegree();
                    Planet directionPlanet = potentialDirection.getPlanet();

                    if (natalPosition != null) {
                        // Calcul de l'arc entre la position natale et celle qui est dirigée
                        Degree arc = CalcUtil.getArc(natalPosition.getDegree(), directedPositionDegree);

                        // Conversion de l'arc en date (1° = 1 an)
                        // Duration durationForArc = DateUtil.getDurationForArc(arc);
                        // currentDate = natalDate.plus(durationForArc);

                        if (arc.getDegree() == 360 || arc.getBaseDegree() == 0) {
                            continue;
                        }

                        // Calcul de quand l'ascendant bougera de cet arc là
                        Degree directedAS = skyPosition.getAscendantPosition().getDegree().offset(arc);
                        DateTime directedDate = getTimeForDirectedAscendant2(directedAS, natalDate, skyPosition.getLatitude(), skyPosition.getLongitude());

                        /*Degree degreeInHouseToFindInTransit = directedPosition.getDegreeInHouse();
                        House houseToFindInTransit = directedPosition.getHouse();

                        //DateTime directedDate = getTimeForDirectedAscendant(planet, degreeInHouseToFindInTransit,
                        //        houseToFindInTransit, natalDate, skyPosition.getLatitude(), skyPosition.getLongitude());*/

                        Period asArcDuration = new Period(natalDate, directedDate);

                        Period durationForArc = DateUtil.convertPrimaryDirectionDuration(asArcDuration);
                        currentDate = natalDate.plus(durationForArc);

                        if (currentDate.isBefore(maxDate)) {
                            PrimaryDirection direction = new PrimaryDirection(currentDate,
                                    //new Degree(360 - arc.getBaseDegree()),
                                    arc,
                                    asArcDuration,
                                    directionAspect,
                                    directionPlanet,
                                    planet);

                            //System.out.println(direction.toString());

                            directions.add(direction);
                        }
                    }

                    // Démarrage du comptage dès qu'on est sur la position planétaire
                    if (directionAspect == Aspect.CONJONCTION && directionPlanet == planet) {
                        natalPosition = directedPosition;
                    }

                    // Rembobinage si on a atteint la fin
                    if (!iterator.hasNext()) {
                        iterator = potentialDirections.iterator();
                    }
                }
            }
        }

        return directions;
    }

    public SortedSet<PotentialDirection> getPotentialDirections(SkyPosition skyPosition) {
        SortedSet<PotentialDirection> potentialdirections = Sets.newTreeSet();

        Degree asDegree = skyPosition.getAscendantPosition().getDegree();

        for (Planet planet : Planet.values()) {
            PlanetPosition originalPosition = skyPosition.getPlanetPosition(planet);

            Degree originalPositionDegree = originalPosition.getDegree();

            for (Aspect aspect : Aspect.values()) {
                Degree newAngle = originalPositionDegree.offset(new Degree(aspect.getAngleSeparation()));
                do {
                    // On ajoute à la position initiale l'arc équivalent pour l'aspect en question (ex : 90° pour un carré)
                    PlanetPosition potentialPosition = PlanetPosition.createPlanetPosition(newAngle, asDegree);
                    PotentialDirection conjonctionPotentialDirection = new PotentialDirection(planet, aspect,
                            potentialPosition, originalPosition);
                    potentialdirections.add(conjonctionPotentialDirection);

                    newAngle = newAngle.offset(new Degree(aspect.getAngleSeparation()));
                } while (originalPositionDegree.compareTo(newAngle) != 0);
            }
        }
        return potentialdirections;
    }

    private DateTime getTimeForDirectedAscendant2(Degree degree,
                                                  DateTime from, Degree latitude, Degree longitude) {
        SweDate jDate = DateUtil.getSweDateUTC(from);

        //int planetFlags = SweConst.SEFLG_TRANSIT_LONGITUDE;
        int planetFlags = SweConst.SEFLG_MOSEPH |
                SweConst.SEFLG_TRANSIT_LONGITUDE;

        int houseFlags = 0;

        SwissEph swissEph = swiephService.value();

        TransitCalculator tc = new TCHouses(
                swissEph,
                SweConst.SE_ASC,
                SweConst.SE_HSYS_EQUAL,
                longitude.getBaseDegree(),
                latitude.getBaseDegree(),
                planetFlags,
                degree.getBaseDegree());

        double nextTransitUT = swissEph.getTransitUT(tc, jDate.getJulDay(), false);

        long dateMillis = DateTimeUtils.fromJulianDay(nextTransitUT);
        return new DateTime(dateMillis);
    }
}
