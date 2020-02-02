package com.theastrologist.util;

import com.theastrologist.core.Swieph;
import com.theastrologist.domain.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.TCPlanet;
import swisseph.TransitCalculator;

import java.util.TimeZone;

public class CalcUtil {
    public static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    public static final double DELTA = 1e-5;
    public static final double DELTAORBS = 1e-2;

    public static Sign getSign(Degree degree) {
        int signNumber = (int) (degree.getBaseDegree() / 30) + 1;
        return Sign.getSign(signNumber);
    }

    public static Degree getDegreeInSign(Degree degree) {
        return new Degree(degree.getBaseDegree() % 30);
    }

    public static Degree shiftDegreeWithHouse(Degree degree, Degree ascendantDegree) {
        double baseDegree = degree.getBaseDegree() - ascendantDegree.getBaseDegree();
        return CalcUtil.equilibrate(new Degree(baseDegree));
    }

    public static Degree getDegreeInHouse(Degree degree, Degree ascendantDegree) {
        Degree baseDegreeInHouse = shiftDegreeWithHouse(degree, ascendantDegree);
        return getDegreeInSign(baseDegreeInHouse);
    }

    public static House getHouse(Degree degree, Degree ascendantDegree) {
        Degree baseDegreeInHouse = shiftDegreeWithHouse(degree, ascendantDegree);
        int houseNumber = (int) (baseDegreeInHouse.getBaseDegree() / 30) + 1;
        return House.getHouse(houseNumber);
    }

    public static Degree equilibrate(Degree degree) {
        return new Degree(equilibrate(degree.getBaseDegree()));
    }

    public static Degree getArc(Degree from, Degree to) {
        return new Degree(equilibrate(to.getBaseDegree() - from.getBaseDegree()));
    }

    public static double equilibrate(double degree) {
        double returnedValue;
        if (degree == 0) {
            returnedValue = 0L;
        } else if (degree < 0) {
            returnedValue = degree + 360;
        } else if (degree > 360) {
            returnedValue = degree - 360;
        } else {
            returnedValue = degree;
        }
        return returnedValue;
    }

    public static double getOpposite(double baseDegree) {
        return baseDegree < 180 ? baseDegree + 180 : baseDegree - 180;
    }

    public static Degree getOpposite(Degree degree) {
        double baseDegree = degree.getBaseDegree();
        return new Degree(getOpposite(baseDegree));
    }

    public static Sign getOpposite(Sign sign) {
        int signNumber = sign.getSignNumber();
        return signNumber > 6 ? Sign.getSign(signNumber - 6) : Sign.getSign(signNumber + 6);
    }

    public static House getOpposite(House house) {
        int houseNumber = house.getHouseNumber();
        return houseNumber > 6 ? House.getHouse(houseNumber - 6) : House.getHouse(houseNumber + 6);
    }

    public static Degree calculatePartDeFortune(Degree ascDegree, Degree sunDegree, Degree moonDegree) {
        double partDegree = ascDegree.getBaseDegree() + moonDegree.getBaseDegree() - sunDegree.getBaseDegree();

        if (partDegree < 0) {
            partDegree += 360;
        } else if (partDegree >= 360) {
            partDegree -= 360;
        }

        return new Degree(partDegree);
    }
}
