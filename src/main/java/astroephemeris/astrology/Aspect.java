package astroephemeris.astrology;

import astroephemeris.math.Angle;

public enum Aspect {

	CONJUCTION(0),
	SEMI_SEXTIL(30),
	SEXTIL(60),
	QUADRATURE(90),
	TRINE(120),
	QUINCUNX (150),
	OPOSITION(180);

	private int diff;

	Aspect(int diff) {
		this.diff = diff;
	}
	
	public Angle difference() {
		return Angle.degrees(diff);
	}
}
