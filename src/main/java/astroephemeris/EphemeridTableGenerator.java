package astroephemeris;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import astroephemeris.astrology.AlcabitiusHouseSystem;
import astroephemeris.astrology.ArabicPartsCalculator;
import astroephemeris.astrology.ChartCalculator;
import astroephemeris.astrology.EqualHouseSystem;
import astroephemeris.astrology.PlacidusHouseSystem;
import astroephemeris.astrology.PorphyryHouseSystem;
import astroephemeris.astrology.RegiomontanusHouseSystem;
import astroephemeris.astrology.SignPosition;
import astroephemeris.catalog.Centaur;
import astroephemeris.catalog.Moon;
import astroephemeris.catalog.MoonTrueNode;
import astroephemeris.catalog.Planet;
import astroephemeris.catalog.Sun;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.GeoCoordinates;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public class EphemeridTableGenerator {

	public static void main(String[] args) {
		
		GeoCoordinates geo = GeoCoordinates.at("39° 49' 26'' N" , "7°29'31'' W");
		
		ObservationPoint point = ObservationPoint.at(
				geo,
				ZonedDateTime.of(1978, 6, 13, 4, 15, 0, 0, ZoneId.of("Europe/Lisbon"))
	    );
		
		var st = point.localSideralTime();
		
		System.out.println("At " + st.toString());
//		
//		var position = new AscendentPositionCalculator().positionFrom(point);
//		
//		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
//		
//		System.out.println("ASC 		|"  + " " + sign.toString());
//		
//		position = new MidHeavenPositionCalculator().positionFrom(point);
//		
//		sign = SignPosition.from(position.astoCoordinate().rightAscention());
//		
//		System.out.println("MC 		|"  + " " + sign.toString());
//		
//		
//	    position = new SunPositionCalculator().positionFrom(point);
//	   
//		sign = SignPosition.from(position.astoCoordinate().rightAscention());
//		
//		System.out.println("Sun 		|"  + " " + sign.toString());
//		
//		for (var planet : Planet.values()) {
//			if (planet != Planet.EARTH) {
//				position =  PlanetPositionCalculator.of(planet).positionFrom(point);
//				
//			    sign = SignPosition.from(position.astoCoordinate().rightAscention());
//				
//				System.out.println(planet.name() + " |" + " " + sign.toString());
//			}
//		}
//		
//		
//		System.out.println();
//		System.out.println();
		

		var chart = new ChartCalculator()
				.addAstro(new Sun())
				.addAstro(new Moon())
				.addAstro(new MoonTrueNode())
				.addAstros(Centaur.values())
				.addAstros(Planet.values())
				//.setSkyCalculator( new TestSkyCalculator())
				.setHouseSystem(new PlacidusHouseSystem())
				.addChartPointCalculator(new ArabicPartsCalculator())
				.calculate(point);
		
	
		for (var p : chart.points()) {
			System.out.println(p.astro().value() + "			|" + " " + p.signPosition().toString());
		}
		
		for (var h : chart.houses()) {
			System.out.println(h.number() + "			|" + " " + h.cuspid().toString());
		}
		
		var points = chart.points();
	
	
		for (int i = 0; i < points.size(); i++) {
			var a = points.get(i);
		
			for (int j = i + 1; j < points.size(); j++) {
				var b = points.get(j);
	
				chart.aspectBetween(a.astro(), b.astro()).ifPresent(it ->{
					System.out.println(a.astro().value() + "	" + it.name() + "		" + b.astro().value());
				});
		
			}

		}

	}
	
	
}

class TestSkyCalculator implements SkyCalculator {

	@Override
	public Sky calculate(Sky sky, ObservationPoint point) {
		
		sky.setAstroPosition(new Sun(),  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(81, 46)), null, null), null));
		sky.setAstroPosition(new Moon(), new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(12, 29)), null, null), null));
		sky.setAstroPosition(Planet.MERCURY,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(20, 4)), null, null), null));
		sky.setAstroPosition(Planet.VENUS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(26,18)), null, null), null));
		sky.setAstroPosition(Planet.MARS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(29, 28)), null, null), null));
		sky.setAstroPosition(Planet.JUPITER,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setAstroPosition(Planet.SATURN,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setAstroPosition(Planet.NEPTUNE,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setAstroPosition(Planet.URANUS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setAstroPosition(Planet.PLUTO,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		
		return sky;
	}
	
}
