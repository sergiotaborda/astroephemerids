package astroephemeris.catalog;

public enum Planet implements AstroKey{


	MERCURY("Mercury", '\u263F'),
	VENUS("Venus", '\u2640'),
	EARTH("Earth", '\u2641'),
	MARS("Mars", '\u2642'),
	JUPITER("Jupiter",'\u2643'),
	SATURN("Saturn", '\u2644'),
	URANUS("Uranus", '\u2645'),
	NEPTUNE("Neptune", '\u2646'),
	PLUTO("Pluto",'\u2647');
	
	private char symbol;
	private String name;

	Planet(String name, char symbol){
		this.name = name;
		this.symbol = symbol;
	}

	@Override
	public String value() {
		return name;
	}
}
