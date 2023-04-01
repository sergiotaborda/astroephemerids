package astroephemeris;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import astroephemeris.astrology.Sign;
import astroephemeris.astrology.SignPosition;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.GeoCoordinates;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;

public class TestPositions {

	
	GeoCoordinates geo = GeoCoordinates.at("39° 49' 26'' N" , "7°29'31'' W");
	
	ObservationPoint point = ObservationPoint.at(
			geo,
			ZonedDateTime.of(1978, 6, 13, 4, 15, 0, 0, ZoneId.of("Europe/Lisbon"))
    );

	
	@Test
	public void sideralTime() {
		

		var st = point.localSideralTime();
		assertEquals(RightAscention.hours(20,9,22.38574547524678), st);
		
	}
	
	@Test
	public void sideralTimeNetherlands() {
		
		GeoCoordinates geo = GeoCoordinates.at("52° 13' N" , "6° 54' E");
		
		ObservationPoint point = ObservationPoint.at(
				geo,
				ZonedDateTime.of(2016, 11, 2, 22, 17, 30, 0, ZoneId.of("Europe/Amsterdam"))
	    );

		var st = point.localSideralTime();
		assertEquals(RightAscention.hours(0,35,23.64675427695545), st);
		
	}
	
	@Test
	public void sunPosition() {
		
		var sun = new SunPositionCalculator();
	
		var position = sun.calculatePosition(point);
		
	
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		assertEquals(Sign.GEMINI,sign.sign());
		assertEquals("GEM 21°46'",sign.toString());
	}
	
	@Test
	public void mercuryPosition() {

		var calculator = new PlanetPositionCalculator();
	
		var position = calculator.calculatePosition(PointOfInterest.MERCURY, point);
		
	
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		assertEquals(Sign.GEMINI,sign.sign());
		assertEquals("GEM 19°16'",sign.toString());
	}
	
	@Test
	public void mcPosition() {

		var position =  new MidHeavenPositionCalculator().calculatePosition(point);
		
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		assertEquals(Sign.AQUARIUS,sign.sign());
		assertEquals("AQU 0°9'",sign.toString());
	}
	
	@Test
	public void ascPosition() {

		var position =  new AscendentPositionCalculator().calculatePosition(point);
		
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		assertEquals(Sign.TAURUS,sign.sign());
		assertEquals("TAU 20°20'",sign.toString());
	}
	
	@Test
	public void asceatherlandsPosition() {

		GeoCoordinates geo = GeoCoordinates.at("52° 13' N" , "6° 54' E");
		
		ObservationPoint point = ObservationPoint.at(
				geo,
				ZonedDateTime.of(2016, 11, 2, 22, 17, 30, 0, ZoneId.of("Europe/Amsterdam"))
	    );
		
		var position =  new AscendentPositionCalculator().calculatePosition(point);
		
		var sign = SignPosition.from(position.astoCoordinate().rightAscention());
		
		assertEquals(Sign.LEO,sign.sign());
		assertEquals("LEO 3°30'",sign.toString());
	}

}
