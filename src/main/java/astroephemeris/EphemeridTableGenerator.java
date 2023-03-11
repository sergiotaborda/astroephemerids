package astroephemeris;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import astroephemeris.astrology.SignPosition;
import astroephemeris.catalog.Planet;
import astroephemeris.coordinates.GeoCoordinates;
import astroephemeris.coordinates.ObservationPoint;

public class EphemeridTableGenerator {

	public static void main(String[] args) {
		
		GeoCoordinates geo = GeoCoordinates.at("39° 49' 26'' N" , "7°29'31'' W");
		
		ObservationPoint point = ObservationPoint.at(
				geo,
				ZonedDateTime.of(1978, 6, 13, 4, 15, 0, 0, ZoneId.of("Europe/Lisbon"))
	    );
		
		var st = point.localSideralTime();
		
		System.out.println("At " + st.toString());
		
		
		var position = new AscendentPositionCalculator().positionFrom(point);
		
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		System.out.println("ASC 		|"  + " " + sign.toString());
		
		position = new MidHeavenPositionCalculator().positionFrom(point);
		
		sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		System.out.println("MC 		|"  + " " + sign.toString());
		
		
	    position = new SunPositionCalculator().positionFrom(point);
	
		 sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		System.out.println("Sun 		|"  + " " + sign.toString());
		
		for (var planet : Planet.values()) {
			if (planet != Planet.EARTH) {
				position =  PlanetPositionCalculator.of(planet).positionFrom(point);
				
			    sign = SignPosition.from(position.astoCoordinate().rightAscention());
				
				System.out.println(planet.name() + " |" + " " + sign.toString());
			}
		}
		
		
	
	}

}
