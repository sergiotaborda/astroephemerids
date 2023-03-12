package astroephemeris.astrology;

import astroephemeris.catalog.AstroKey;

public enum Pars  implements AstroKey {
	
	FORTUNE("Fortune", '\u2647'); // TODO put symbol correct

	
	private char symbol;
	private String name;

	Pars(String name, char symbol){
		this.name = name;
		this.symbol = symbol;
	}

	@Override
	public String value() {
		return name;
	}

}
