package astroephemeris.astrology;

import astroephemeris.catalog.PointOfInterest;

public class ArabicPartsCalculator implements ChartPointCalculator {

	@Override
	public void addPoints(Chart chart) {
		// https://jessicadavidson.co.uk/2016/03/12/how-to-calculate-the-part-of-fortune/
		
		var asc = chart.getPoint(PointOfInterest.ASC).orElseThrow().signPosition().angle();
		var sun = chart.getPoint(PointOfInterest.SUN).orElseThrow().signPosition().angle();;
		var moon = chart.getPoint(PointOfInterest.MOON).orElseThrow().signPosition().angle();;
		
		var isDay = chart.observationPoint().utcTime().getHour() < 12;
		var parsFortune = isDay
				 ?  asc.plus(moon).minus(sun).simplify()
				 :  asc.plus(sun).minus(moon).simplify();
		
		chart.addPoint(new ChartPoint(Pars.FORTUNE, SignPosition.from(parsFortune)));
	}

}
