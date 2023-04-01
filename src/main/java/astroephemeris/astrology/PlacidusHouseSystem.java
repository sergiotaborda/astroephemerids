package astroephemeris.astrology;

import astroephemeris.HeliocentricDynamics;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class PlacidusHouseSystem extends AbstractHouseSystem {
	

	@Override
	public Chart calculateHouses(Chart chart) {
		// https://alt.astrology.moderated.narkive.com/jqcGiSkP/placidus-house-equations
		
		var L = chart.observationPoint().location().latitude();
		var RAMC = chart.observationPoint().localSideralTime().toDegrees();
		var E = HeliocentricDynamics.obliquityEcliptic(chart.observationPoint());
		

		var eleven = runA(RAMC, E, L, 30, Number.from(3.0));
		var twelve = runA(RAMC, E, L, 60, Number.from(1.5));
		var two =  runB(RAMC, E, L, 120, Number.from(1.5));
		var three = runB(RAMC, E, L, 150, Number.from(3));

			
		var mc = chart.getPoint(PointOfInterest.MC).orElseThrow();
		var asc = chart.getPoint(PointOfInterest.ASC).orElseThrow();
		
		chart.addHouse(new House(1, asc.signPosition()));
		chart.addHouse(new House(2, SignPosition.from(two)));
		chart.addHouse(new House(3, SignPosition.from(three)));
		
		chart.addHouse(new House(10, mc.signPosition()));
		chart.addHouse(new House(11, SignPosition.from(eleven)));
		chart.addHouse(new House(12, SignPosition.from(twelve)));
		
		extrapolateOpositeHouses(chart);
		
		return chart;
	}

	private Angle runA(Angle RAMC, Angle E, Angle L, int adjust, Number F) {
		var RA0 = RAMC.plus(Angle.degrees(adjust));
		var RA = RAMC.plus( Angle.acos(RA0.sin().negate().times(E.tan()).times(L.tan())).over(F));
		
		while (RA0.minus(RA).abs().valueInDegrees()  > 0.0005) {
			RA0 = RA;
			RA = RAMC.plus( Angle.acos(RA0.sin().negate().times(E.tan()).times(L.tan())).over(F));
		}
	
		return Angle.atan2d(RA.sin(), (E.cos().times(RA.cos())));
	}

	private Angle runB(Angle RAMC, Angle E, Angle L, int adjust, Number F) {
		
		var RA0 = RAMC.plus(Angle.degrees(adjust));
		var RA = RAMC.plus(Angle.degrees(180)).minus( Angle.acos(RA0.sin().times(E.tan()).times(L.tan())).over(F));
		
		while (RA0.minus(RA).abs().valueInDegrees()  > 0.0005) {
			RA0 = RA;
		    RA = RAMC.plus(Angle.degrees(180)).minus( Angle.acos(RA0.sin().times(E.tan()).times(L.tan())).over(F));	
		}
	
		return Angle.atan2d(RA.sin(), (E.cos().times(RA.cos())));
	}
}
