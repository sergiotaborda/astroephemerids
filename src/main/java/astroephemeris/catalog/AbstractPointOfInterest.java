package astroephemeris.catalog;

public abstract class AbstractPointOfInterest implements PointOfInterest {

	private final String name;
	private final int order;

	protected AbstractPointOfInterest(int order, String name) {
		this.order = order;
		this.name = name;
	}
	
	@Override
	public int compareTo(PointOfInterest o) {
		 return o instanceof AbstractPointOfInterest a ?  this.order - a.order : 0;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof PointOfInterest poi
				&& this.name.equals(poi.toString());
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public String name() {
		return this.name;
	}
}
