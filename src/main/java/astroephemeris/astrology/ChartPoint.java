package astroephemeris.astrology;

import astroephemeris.catalog.PointOfInterest;

public record ChartPoint(PointOfInterest point, SignPosition signPosition) {


	public boolean isDomicile() {
		return this.signPosition.sign().primaryRegent().equals(point)
				||  this.signPosition.sign().secondaryRegent().map(it -> it.equals(point)).orElse(false);
	}
	
}
