package astroephemeris.astrology;

import astroephemeris.math.Angle;

public enum Sign {

	ARIES('\u2648'),
	TAURUS('\u2649'),
	GEMINI('\u264A'),
	CANCER('\u264B'),
	LEO('\u264C'),
	VIRGIN('\u264D'),
	LIBRA('\u264E'),
	SCORPIUS('\u264F'),
	SAGIATARIUS('\u2650'),
	CAPRICORD('\u2651'),
	AQUARIUS('\u2652'),
	PIXIES('\u2653');


	private char symbol;

	Sign(char symbol) {
		this.symbol = symbol;
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
