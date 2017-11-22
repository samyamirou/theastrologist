package com.theastrologist.service;

import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.util.DateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swisseph.*;

/**
 * Created by Samy on 11/05/2017.
 */
@Service
public class RevolutionService {
	private static final Logger LOG = Logger.getLogger(RevolutionService.class);

	@Autowired
	private ThemeService themeService;

	@Autowired
	private Swieph swieph;

	public RevolutionService() {}

	public SkyPosition getSolarRevolution(SkyPosition natalTheme, DateTime from, Degree latitude, Degree longitude) {
		DateTime solarRevolutionUT = getSolarRevolutionUT(natalTheme, from);
		return themeService.getSkyPosition(solarRevolutionUT, latitude, longitude);
	}

	public SkyPosition getLunarRevolution(SkyPosition natalTheme, DateTime from, Degree latitude, Degree longitude) {
		DateTime moonRevolutionUT = getLunarRevolutionUT(natalTheme, from);
		return themeService.getSkyPosition(moonRevolutionUT, latitude, longitude);
	}

	public DateTime getSolarRevolutionUT(SkyPosition natalTheme, DateTime from) {
		PlanetPosition soleilPosition = natalTheme.getPlanetPosition(Planet.SOLEIL);
		return getRevolution(soleilPosition, from, SweConst.SE_SUN);
	}

	public DateTime getLunarRevolutionUT(SkyPosition natalTheme, DateTime from) {
		PlanetPosition lunePosition = natalTheme.getPlanetPosition(Planet.LUNE);
		return getRevolution(lunePosition, from, SweConst.SE_MOON);
	}

	private DateTime getRevolution(PlanetPosition position, DateTime from, int swPlanet) {
		SweDate jDate = DateUtil.getSweDateUTC(from);

		int flags = SweConst.SEFLG_MOSEPH | SweConst.SEFLG_TRANSIT_LONGITUDE;

		TransitCalculator tc = new TCPlanet(
				swieph.value(),
				swPlanet,
				flags,
				position.getDegree().getBaseDegree());

		double nextTransitUT = swieph.value().getTransitUT(tc, jDate.getJulDay(), false);

		long dateMillis = DateTimeUtils.fromJulianDay(nextTransitUT);
		return new DateTime(dateMillis);
	}
}
