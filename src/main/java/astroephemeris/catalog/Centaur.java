package astroephemeris.catalog;

public enum Centaur implements AstroKey {
	CERES("Ceres", '\u2647'), // TODO put symbol correct
	CHIRON("Chiron", '\u2647'); // TODO put symbol correct

	
	
	private char symbol;
	private String name;

	Centaur(String name, char symbol){
		this.name = name;
		this.symbol = symbol;
	}

	@Override
	public String value() {
		return name;
	}

}
