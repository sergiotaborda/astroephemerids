package astroephemeris.astrology;

import astroephemeris.math.Angle;

public enum Aspect {

	CONJUCTION(0, '\u260C'),
	SEMI_SEXTIL(30,'\u26BA'),
	SEXTIL(60,'\u26B9'),
	QUADRATURE(90, '\u25A1'),
	TRINE(120, '\u25B3'),
	QUINCUNX (150,'\u26BB'),
	OPOSITION(180,'\u260D');

	private int diff;
	private char symbol;

	Aspect(int diff, char symbol) {
		this.diff = diff;
		this.symbol = symbol;
	}
	
	public Angle difference() {
		return Angle.degrees(diff);
	}
	
	public char symbol() {
		return symbol;
	}
}
