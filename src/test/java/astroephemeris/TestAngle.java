package astroephemeris;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class TestAngle {

	
	@Test
	public void testTrignometric() {
		var zero = Angle.degrees(0);
		
		assertEquals(Number.from(0.0), zero.sin());
		assertEquals(Number.from(1.0), zero.cos());
		
		var d90 = Angle.degrees(90);
		
		assertEquals(Number.from(1.0), d90.sin());
		assertEquals(Number.from(0.0), d90.cos());
		
		var d180 = Angle.degrees(180);
		
		assertEquals(Number.from(0.0), d180.sin());
		assertEquals(Number.from(-1.0), d180.cos());
		
		var d270 = Angle.degrees(270);
		
		assertEquals(Number.from(-1.0), d270.sin());
		assertEquals(Number.from(0.0), d270.cos());
	}
	

}
