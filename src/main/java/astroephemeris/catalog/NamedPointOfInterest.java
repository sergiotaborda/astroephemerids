package astroephemeris.catalog;

public class NamedPointOfInterest extends AbstractPointOfInterest{

	private String symbol;

	public NamedPointOfInterest(int order, String name) {
		super(order,name);
	}
	
	public NamedPointOfInterest(int order, String name, char symbol) {
		super(order, name);
		this.symbol = Character.toString(symbol);
	}
	
	public NamedPointOfInterest(int order, String name, String symbol) {
		super(order, name);
		this.symbol = symbol;
	}


	@Override
	public String symbol() {
		return symbol;
	}


}
