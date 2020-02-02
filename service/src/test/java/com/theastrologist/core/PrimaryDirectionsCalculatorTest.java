package com.theastrologist.core;

import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.domain.primarydirection.PotentialDirection;
import com.theastrologist.domain.primarydirection.PrimaryDirection;
import com.theastrologist.util.CalcUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PrimaryDirectionsCalculatorTest {
    static final Logger LOG = Logger.getLogger(PrimaryDirectionsCalculatorTest.class);
    private final DateTime TEST_DATE = new DateTime(1985, 1, 4, 11, 20, CalcUtil.DATE_TIME_ZONE);
    private final Degree LATITUDE = new Degree(48, 39);
    private final Degree LONGITUDE = new Degree(2, 25);
    private SkyPosition samySkyPosition;
    private SkyPosition charlesFloquetSkyPosition;
    private SkyPosition victorHugoSkyPosition;
    private SkyPosition georgesClemenceauSkyPosition;
    private SkyPosition esoMaliceSkyPosition;

    @Before
    public void setup() {
        samySkyPosition = ThemeCalculator.getInstance().getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        charlesFloquetSkyPosition = ThemeCalculator.getInstance().getSkyPosition(
                new DateTime(1828, 10, 2, 6, 30, CalcUtil.DATE_TIME_ZONE),
                new Degree(48, 51), new Degree(2, 17)
        );

        victorHugoSkyPosition = ThemeCalculator.getInstance().getSkyPosition(
                new DateTime(1802, 2, 26, 22, 30, CalcUtil.DATE_TIME_ZONE),
                new Degree(48, 51), new Degree(2, 21)
        );

        georgesClemenceauSkyPosition = ThemeCalculator.getInstance().getSkyPosition(
                new DateTime(1841, 9, 28, 21, 30, CalcUtil.DATE_TIME_ZONE),
                new Degree(46, 40), new Degree(0, 51)
        );

        esoMaliceSkyPosition = ThemeCalculator.getInstance().getSkyPosition(
                new DateTime(1972, 2, 5, 6, 30, CalcUtil.DATE_TIME_ZONE),
                new Degree(43, 12), new Degree(2, 21)
        );
    }

    @Test
    public void testPotentialPositions() {
        Set<PotentialDirection> potentialDirections = PrimaryDirectionsCalculator.getInstance().getPotentialDirections(samySkyPosition);
        assertThat(potentialDirections, notNullValue());

        PotentialDirection next = potentialDirections.iterator().next();
        assertThat(next, notNullValue());
        assertThat(next.getAspect(), equalTo(Aspect.CONJONCTION));
        assertThat(next.getPlanet(), equalTo(Planet.LILITH_EXACTE));
        assertThat(next.getPosition().getDegree(), equalTo(new Degree(1, 9)));

        // Compter
        int conj = 0;
        int opp = 0;
        int car = 0;
        int trig = 0;
        int sext = 0;
        for (PotentialDirection dir : potentialDirections) {
            System.out.println(dir.getPlanet().name() + " - " + dir.getAspect().name() + " - " + dir.getPosition().toString());
            if (dir.getPlanet() == Planet.NEPTUNE) {
                switch (dir.getAspect()) {
                    case CARRE:
                        car++;
                        break;
                    case SEXTILE:
                        sext++;
                        break;
                    case CONJONCTION:
                        conj++;
                        break;
                    case OPPOSITION:
                        opp++;
                        break;
                    case TRIGONE:
                        trig++;
                        break;
                }
            }
        }

        assertThat(conj, equalTo(1));
        assertThat(opp, equalTo(1));
        assertThat(car, equalTo(2));
        assertThat(trig, equalTo(2));
        assertThat(sext, equalTo(2));

        // 17 planètes x 8 (1 conj, 1 opp, 2 trigones, 2 carrés, 2 sextiles)
        assertThat(potentialDirections, hasSize(136));
    }

    @Test
    public void testGetArcZero() {
        Degree a = new Degree(0, 0);
        Degree b = new Degree(0, 0);
        Degree arc = CalcUtil.getArc(a, b);

        assertThat(arc, equalTo(new Degree(0, 0)));
    }

    @Test
    public void testGetArcPositive() {
        Degree a = new Degree(0, 0);
        Degree b = new Degree(3, 2);
        Degree arc = CalcUtil.getArc(a, b);

        assertThat(arc, equalTo(new Degree(3, 2)));
    }

    @Test
    public void testGetArcNegative() {
        Degree a = new Degree(20, 0);
        Degree b = new Degree(10, 0);
        Degree arc = CalcUtil.getArc(a, b);

        assertThat(arc, equalTo(new Degree(350, 0)));
    }

    @Test
    public void testGetArcGreater180() {
        Degree a = new Degree(50, 0);
        Degree b = new Degree(240, 3);
        Degree arc = CalcUtil.getArc(a, b);

        assertThat(arc, equalTo(new Degree(190, 3)));
    }

    private static SortedSet<PrimaryDirection> getPrimaryDirections(SkyPosition skyPosition) {
        PrimaryDirectionsCalculator instance = PrimaryDirectionsCalculator.getInstance();
        Set<PotentialDirection> potentialDirections = instance.getPotentialDirections(skyPosition);
        return instance.generatePotentialDirections(skyPosition, potentialDirections);
    }

    @Test
    public void testGenerateDirectionsSamy() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(samySkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        assertThat(first.getDate().getYear(), equalTo(1985));
    }

    @Test
    public void testGenerateDirectionsCharlesFloquet() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(charlesFloquetSkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        ArrayList<PrimaryDirection> collect = primaryDirections.stream()
                .filter(primaryDirection -> primaryDirection.getPlanet() == Planet.PLUTON &&
                        primaryDirection.getAspect() == Aspect.OPPOSITION &&
                        primaryDirection.getDirectedPlanet() == Planet.SATURNE).collect(Collectors.toCollection(ArrayList::new));
        assertThat(collect, not(empty()));
        PrimaryDirection primaryDirection = collect.get(0);
        Period arcDuration = primaryDirection.getArcDuration();
        System.out.println("Durée : " + arcDuration);
        assertThat(arcDuration.getHours(), equalTo(5));
        assertThat(arcDuration.getMinutes(), equalTo(44));
        assertThat(primaryDirection.getDate().getYear(), equalTo(1916));
    }

    @Test
    public void testGenerateDirectionsVictorHugo() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(victorHugoSkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        ArrayList<PrimaryDirection> collect = primaryDirections.stream()
                .filter(primaryDirection ->
                        primaryDirection.getPlanet() == Planet.SATURNE &&
                                primaryDirection.getAspect() == Aspect.TRIGONE &&
                                primaryDirection.getDirectedPlanet() == Planet.JUPITER).collect(Collectors.toCollection(ArrayList::new));
        assertThat(collect, not(empty()));
        assertThat(collect.get(0).getDate().getYear(), equalTo(1928));
    }

    @Test
    public void testGenerateDirectionsVictorHugo2() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(victorHugoSkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        ArrayList<PrimaryDirection> collect1 = primaryDirections.stream()
                .filter(primaryDirection ->
                        primaryDirection.getPlanet() == Planet.SOLEIL &&
                                primaryDirection.getAspect() == Aspect.TRIGONE &&
                                primaryDirection.getDirectedPlanet() == Planet.JUPITER).collect(Collectors.toCollection(ArrayList::new));
        assertThat(collect1, not(empty()));
        PrimaryDirection primaryDirection1 = collect1.get(0);
        assertThat(primaryDirection1.getArcDuration().getHours(), equalTo(5));
        assertThat(primaryDirection1.getArcDuration().getMinutes(), equalTo(42));
    }

    @Test
    public void testGenerateDirectionsGeorgesClemenceau() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(georgesClemenceauSkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        ArrayList<PrimaryDirection> collect = primaryDirections.stream()
                .filter(primaryDirection -> primaryDirection.getPlanet() == Planet.URANUS &&
                        primaryDirection.getAspect() == Aspect.TRIGONE &&
                        primaryDirection.getDirectedPlanet() == Planet.LUNE).collect(Collectors.toCollection(ArrayList::new));

        assertThat(collect, empty());
        //assertThat(collect.get(0).getDate().getYear(), equalTo(1869));
    }

    @Test
    public void testGenerateDirectionsEsoMalice() {
        SortedSet<PrimaryDirection> primaryDirections = getPrimaryDirections(esoMaliceSkyPosition);

        assertThat(primaryDirections, notNullValue());
        assertThat(primaryDirections, not(empty()));
        PrimaryDirection first = primaryDirections.first();
        assertThat(first, notNullValue());

        for (PrimaryDirection primaryDirection : primaryDirections) {
            System.out.println(primaryDirection.toString());
        }

        ArrayList<PrimaryDirection> collect = primaryDirections.stream()
                .filter(primaryDirection -> primaryDirection.getPlanet() == Planet.LUNE &&
                        primaryDirection.getAspect() == Aspect.CONJONCTION &&
                        primaryDirection.getDirectedPlanet() == Planet.JUPITER).collect(Collectors.toCollection(ArrayList::new));
        assertThat(collect, empty());
        //assertThat(collect.get(0).getArcDuration().getHours(), equalTo(3));
    }

    @Test
    public void testPotentialDirectionComparison() {
        Degree asDegree = new Degree(11, 46);
        PlanetPosition asPosition = PlanetPosition.createPlanetPosition(asDegree, asDegree);
        PlanetPosition carreLunePosition = PlanetPosition.createPlanetPosition(new Degree(11, 41), asDegree);
        PlanetPosition originalLunePosition = PlanetPosition.createPlanetPosition(new Degree(71, 41), asDegree);
        PotentialDirection a = new PotentialDirection(Planet.ASCENDANT, Aspect.CONJONCTION, asPosition, asPosition);
        PotentialDirection b = new PotentialDirection(Planet.LUNE, Aspect.CARRE, carreLunePosition, originalLunePosition);

        assertThat(a.compareTo(b), greaterThan(0));
    }
}
