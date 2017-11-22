package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.planetvalue.PlanetValue;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by Samy on 25/07/2015.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class DominantPlanetsTest {

    public static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    private final DateTime SAMY_DATE = new DateTime(1985, 1, 4, 11, 20, DATE_TIME_ZONE);
    private final DateTime LAURENT_DATE = new DateTime(1980, 8, 9, 14, 25, DATE_TIME_ZONE);
    private final DateTime EMILIE_DATE = new DateTime(1983, 7, 1, 13, 20, DATE_TIME_ZONE);
    private final DateTime VANESSA_DATE = new DateTime(1974, 10, 4, 4, 20, DATE_TIME_ZONE);
    private final Degree LATITUDE = new Degree(48, 39);
    private final Degree LONGITUDE = new Degree(2, 25);
    private SkyPosition samySkyPosition;
    private SkyPosition laurentSkyPosition;
    private SkyPosition emilieSkyPosition;
    private SkyPosition vanessaSkyPosition;
    private SortedSet<PlanetValue> samyDominantPlanetsSigns;
    private SortedSet<PlanetValue> laurentDominantPlanetsSigns;
    private SortedSet<PlanetValue> emilieDominantPlanetsSigns;
    private SortedSet<PlanetValue> vanessaDominantPlanetsSigns;
    private SortedSet<PlanetValue> samyDominantPlanetsHouses;
    private SortedSet<PlanetValue> laurentDominantPlanetsHouses;
    private SortedSet<PlanetValue> emilieDominantPlanetsHouses;
    private SortedSet<PlanetValue> vanessaDominantPlanetsHouses;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private DominantPlanetsService dominantPlanetsService;

    @Before
    public void setUp() throws Exception {
        samySkyPosition = themeService.getSkyPosition(SAMY_DATE, LATITUDE, LONGITUDE);
        laurentSkyPosition = themeService.getSkyPosition(LAURENT_DATE, LATITUDE, LONGITUDE);
        emilieSkyPosition = themeService.getSkyPosition(EMILIE_DATE, LATITUDE, LONGITUDE);
        vanessaSkyPosition = themeService.getSkyPosition(VANESSA_DATE, LATITUDE, LONGITUDE);

        samyDominantPlanetsSigns = dominantPlanetsService.getDominantPlanetsSigns(samySkyPosition);
        laurentDominantPlanetsSigns = dominantPlanetsService.getDominantPlanetsSigns(laurentSkyPosition);
        emilieDominantPlanetsSigns = dominantPlanetsService.getDominantPlanetsSigns(emilieSkyPosition);
        vanessaDominantPlanetsSigns = dominantPlanetsService.getDominantPlanetsSigns(vanessaSkyPosition);

        samyDominantPlanetsHouses = dominantPlanetsService.getDominantPlanetsHouses(samySkyPosition);
        laurentDominantPlanetsHouses = dominantPlanetsService.getDominantPlanetsHouses(laurentSkyPosition);
        emilieDominantPlanetsHouses = dominantPlanetsService.getDominantPlanetsHouses(emilieSkyPosition);
        vanessaDominantPlanetsHouses = dominantPlanetsService.getDominantPlanetsHouses(vanessaSkyPosition);
    }

    @Test
    public void testGetDominantPlanetsSamySigns() throws Exception {
        SortedSet<PlanetValue> testSet = samyDominantPlanetsSigns;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), Matchers.is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
    }

    @Test
    public void testGetDominantPlanetsSamyHouses() throws Exception {
        SortedSet<PlanetValue> testSet = samyDominantPlanetsHouses;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
    }

    @Test
    public void testGetDominantPlanetsLaurentSigns() throws Exception {
        SortedSet<PlanetValue> testSet = laurentDominantPlanetsSigns;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));

        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
    }

    @Test
    public void testGetDominantPlanetsLaurentHouses() throws Exception {
        SortedSet<PlanetValue> testSet = laurentDominantPlanetsHouses;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));

        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
    }

    @Test
    public void testGetDominantPlanetsEmilieSigns() throws Exception {
        SortedSet<PlanetValue> testSet = emilieDominantPlanetsSigns;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        PlanetValue next = iter.next();
        assertThat(next.getPlanet(), is(Planet.NEPTUNE));
        assertThat(next.getValue(), is(43));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.LUNE));
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
    }

    @Test
    public void testGetDominantPlanetsEmilieHouses() throws Exception {
        SortedSet<PlanetValue> testSet = emilieDominantPlanetsHouses;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        PlanetValue next = iter.next();
        assertThat(next.getPlanet(), is(Planet.MERCURE));
        assertThat(next.getValue(), is(30));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
    }

    @Test
    public void testGetDominantPlanetsVanessaSigns() throws Exception {
        SortedSet<PlanetValue> testSet = vanessaDominantPlanetsSigns;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.URANUS));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.LUNE));
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
    }

    @Test
    public void testGetDominantPlanetsVanessaHouses() throws Exception {
        SortedSet<PlanetValue> testSet = vanessaDominantPlanetsHouses;
        assertThat(testSet, notNullValue());
        assertThat(testSet, not(empty()));
        List<PlanetValue> testList = new ArrayList<PlanetValue>(testSet);
        Iterator<PlanetValue> iter = testList.iterator();
        assertThat(iter.next().getPlanet(), is(Planet.PLUTON));
        assertThat(iter.next().getPlanet(), is(Planet.MARS));
        assertThat(iter.next().getPlanet(), is(Planet.JUPITER));
        assertThat(iter.next().getPlanet(), is(Planet.NEPTUNE));
        assertThat(iter.next().getPlanet(), is(Planet.VENUS));
        assertThat(iter.next().getPlanet(), is(Planet.SATURNE));
        assertThat(iter.next().getPlanet(), is(Planet.SOLEIL));
        assertThat(iter.next().getPlanet(), is(Planet.MERCURE));
        assertThat(iter.next().getPlanet(), is(Planet.LUNE));
    }

    public int compareSigns(SkyPosition skyPosition, Planet planet, Planet planetToCompare) {

        int dominantPoints = dominantPlanetsService.calculateDominantSigns(planet, skyPosition).getValue();
        int dominantPointsToCompare = dominantPlanetsService.calculateDominantSigns(planetToCompare, skyPosition).getValue();

        return dominantPoints - dominantPointsToCompare;
    }

    public int compareHouses(SkyPosition skyPosition, Planet planet, Planet planetToCompare) {

        int dominantPoints = dominantPlanetsService.calculateDominantHouses(planet, skyPosition).getValue();
        int dominantPointsToCompare = dominantPlanetsService.calculateDominantHouses(planetToCompare, skyPosition).getValue();

        return dominantPoints - dominantPointsToCompare;
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormaleSigns() throws Exception {
        // Ascendant Poissons
        // Rien pour Soleil
        int comparison = compareSigns(samySkyPosition, Planet.JUPITER, Planet.SOLEIL);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormaleHouses() throws Exception {
        // Ascendant Poissons
        // Rien pour Soleil
        int comparison = compareHouses(samySkyPosition, Planet.JUPITER, Planet.SOLEIL);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormale2Signs() throws Exception {
        // Noeud sud conjoint à saturne, Soleil Capricorne, Soleil Maison XI
        int comparison = compareSigns(samySkyPosition, Planet.SATURNE, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormale2Houses() throws Exception {
        // Noeud sud conjoint à saturne, Soleil Capricorne, Soleil Maison XI
        int comparison = compareHouses(samySkyPosition, Planet.SATURNE, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormale3Signs() throws Exception {
        // Soleil en XI et en capricorne, Uranus en X
        // Venus dans signe de naissance
        int comparison = compareSigns(samySkyPosition, Planet.URANUS, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlanetePrincipaleEtPlaneteNormale3Houses() throws Exception {
        // Soleil en XI et en capricorne, Uranus en X
        // Venus dans signe de naissance
        int comparison = compareHouses(samySkyPosition, Planet.URANUS, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesConjonctionLuminaireSigns() throws Exception {
        int comparison = compareSigns(samySkyPosition, Planet.URANUS, Planet.JUPITER);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitreSigns() throws Exception {
        // Soleil en Cancer
        // Noeud sud en sagittaire (exil) et en III, mercure en gémeaux en IX (exil)
        int comparison = compareSigns(emilieSkyPosition, Planet.MERCURE, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitreHouses() throws Exception {
        // Soleil en Cancer
        // Noeud sud en sagittaire (exil) et en III, mercure en gémeaux en IX (exil)
        int comparison = compareHouses(emilieSkyPosition, Planet.MERCURE, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitrePlusieursSigns() throws Exception {
        int comparison = compareSigns(laurentSkyPosition, Planet.SOLEIL, Planet.URANUS);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitrePlusieursHouses() throws Exception {
        int comparison = compareHouses(laurentSkyPosition, Planet.SOLEIL, Planet.URANUS);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitreExaltationPlusieursSigns() throws Exception {
        int comparison = compareSigns(laurentSkyPosition, Planet.SOLEIL, Planet.URANUS);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesMaitreExaltationPlusieursHouses() throws Exception {
        int comparison = compareHouses(laurentSkyPosition, Planet.SOLEIL, Planet.URANUS);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesExaltationSigns() throws Exception {
        int comparison = compareSigns(laurentSkyPosition, Planet.URANUS, Planet.SATURNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesExaltationHouses() throws Exception {
        int comparison = compareHouses(laurentSkyPosition, Planet.URANUS, Planet.SATURNE);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesExilSigns() throws Exception {
        int comparison = compareSigns(laurentSkyPosition, Planet.MARS, Planet.SATURNE);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesExilHouses() throws Exception {
        int comparison = compareHouses(laurentSkyPosition, Planet.MARS, Planet.SATURNE);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesSigneDeNaissanceSigns() throws Exception {
        int comparison = compareSigns(samySkyPosition, Planet.NEPTUNE, Planet.MERCURE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareDeuxPlanetesPrincipalesSigneDeNaissanceHouses() throws Exception {
        int comparison = compareHouses(samySkyPosition, Planet.NEPTUNE, Planet.MERCURE);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testCompareConjonctionLuminaireNormaleSigns() throws Exception {
        int comparison = compareSigns(laurentSkyPosition, Planet.LUNE, Planet.JUPITER);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testCompareConjonctionLuminaireNormaleHouses() throws Exception {
        int comparison = compareHouses(laurentSkyPosition, Planet.LUNE, Planet.JUPITER);
        assertThat(comparison, lessThan(0));
    }

    @Test
    public void testComparePlaneteDansSigneDeNaissanceSigns() throws Exception {
        int comparison = compareSigns(samySkyPosition, Planet.VENUS, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void testComparePlaneteDansSigneDeNaissanceHouses() throws Exception {
        int comparison = compareHouses(samySkyPosition, Planet.VENUS, Planet.LUNE);
        assertThat(comparison, greaterThan(0));
    }
}
