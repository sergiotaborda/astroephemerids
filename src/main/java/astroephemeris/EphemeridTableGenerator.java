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
import astroephemeris.catalog.MeanLilith;
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
import astroephemeris.math.TimeExpantion;

public class EphemeridTableGenerator {

	public static void main(String[] args) {
		
//		ObservationPoint point = ObservationPoint.at(
//				GeoCoordinates.at("39° 49' 26'' N" , "7°29'31'' W"),
//				ZonedDateTime.of(1978, 6, 13, 4, 15, 0, 0, ZoneId.of("Europe/Lisbon"))
//	    );
//		
		ObservationPoint point = ObservationPoint.at(
				GeoCoordinates.at(astroephemeris.math.Number.from(-19.7474) ,astroephemeris.math.Number.from(-47.9392)),
				ZonedDateTime.of(1977, 8, 15, 23, 45, 0, 0, ZoneId.of("America/Sao_Paulo"))
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
				//.addAstro(new Sun())
				//.addAstro(new Moon())
				.addAstro(new MeanLilith())
				//.addAstro(new MoonTrueNode())
			//	.addAstros(Centaur.values())
				//.addAstros(Planet.values())
				//.setSkyCalculator( new TestSkyCalculator())
			//	.setHouseSystem(new PlacidusHouseSystem())
			//	.addChartPointCalculator(new ArabicPartsCalculator())
				.calculate(point);
		
		var L0 = TimeExpantion.of(Angle.degrees(218.31617d), Angle.degrees(481267.88088), Angle.degrees(4.06/3600.0) );//  218.31617 + 481267.88088*T - 4.06*T*T/3600.0
		
		var T = point.factorT();
		
		var l0 = L0.apply(T);
		System.out.println("LO=" + SignPosition.from(l0));
		chart.getPoint(new Moon()).ifPresent(r -> {
			
			System.out.println(SignPosition.from( r.signPosition().angle().minus(l0)));
			System.out.println(SignPosition.from( r.signPosition().angle().plus(l0)));
		});
		
		
	
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
