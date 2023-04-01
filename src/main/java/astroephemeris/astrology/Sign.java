package astroephemeris.astrology;

import java.util.Optional;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.math.Angle;

public enum Sign {

	ARIES('\u2648', PointOfInterest.MARS),
	TAURUS('\u2649', PointOfInterest.VENUS, PointOfInterest.CERES),
	GEMINI('\u264A', PointOfInterest.MERCURY),
	CANCER('\u264B',PointOfInterest.MOON),
	LEO('\u264C',PointOfInterest.SUN),
	VIRGIN('\u264D', PointOfInterest.MERCURY),
	LIBRA('\u264E',PointOfInterest.VENUS),
	SCORPIUS('\u264F', PointOfInterest.MARS, PointOfInterest.PLUTO),
	SAGIATARIUS('\u2650', PointOfInterest.JUPITER),
	CAPRICORD('\u2651', PointOfInterest.SATURN),
	AQUARIUS('\u2652', PointOfInterest.SATURN, PointOfInterest.URANUS),
	PIXIES('\u2653',PointOfInterest.JUPITER, PointOfInterest.NEPTUNE);

	private char symbol;
	private PointOfInterest primaryRegent;
	private PointOfInterest secondaryRegent;

	Sign(char symbol, PointOfInterest primaryRegent) {
		this.symbol = symbol;
		this.primaryRegent = primaryRegent;
	}

	Sign(char symbol, PointOfInterest primaryRegent, PointOfInterest secondaryRegent) {
		this.symbol = symbol;
		this.primaryRegent = primaryRegent;
		this.secondaryRegent = secondaryRegent;
	}
	
	public PointOfInterest primaryRegent() {
		return primaryRegent;
	}
	
	public Optional<PointOfInterest> secondaryRegent() {
		return Optional.ofNullable(secondaryRegent);
	}

	public static Sign from(Angle angle) {
		if (angle.isNegative()) {
			return from(angle.simplify().plus(Angle.degrees(360.0)));
		}
		var g = angle.simplify().valueInDegrees() / 30;
		
		for (var s : Sign.values()) {
			var min = s.ordinal();
			var max = (s.ordinal() + 1);
			
			if ( g >= min && g < max ) {
				return s;
			}
		}
		throw new IllegalArgumentException();
	}

	Angle cuspid() {
		return 	Angle.degrees(ordinal() * 30);
	}
	
	@Override
	public String toString() {
		return String.valueOf(symbol);
	}
	
}
