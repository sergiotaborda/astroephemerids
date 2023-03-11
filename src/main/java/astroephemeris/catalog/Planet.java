package astroephemeris.catalog;

public enum Planet implements AstroKey{


	MERCURY('\u263F'),
	VENUS('\u2640'),
	EARTH('\u2641'),
	MARS('\u2642'),
	JUPITER('\u2643'),
	SATURN('\u2644'),
	URANUS('\u2645'),
	NEPTUNE('\u2646');
	//PLUTO('\u2647');
	
	private char symbol;

	Planet(char symbol){
		this.symbol = symbol;
	}

	@Override
	public String value() {
		return String.valueOf(symbol);
	}
}
