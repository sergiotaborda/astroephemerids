package astroephemeris;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;

public abstract class SingleAstroPositionCalculator implements PointOfInterestPositionCalculator{

	
	private final PointOfInterest astro;

	protected SingleAstroPositionCalculator(PointOfInterest astro) {
		this.astro = astro;
	}
	
	
	public final boolean canCalculatePosition(PointOfInterest astro) {
		return this.astro.equals(astro);
	}
	
	public AstroPosition calculatePosition(PointOfInterest astro, ObservationPoint point) {
		if (!canCalculatePosition(astro)) {
			throw new IllegalArgumentException("Cannot calculate " + astro);
		}
		return calculatePosition(point);
	}
	
	protected abstract AstroPosition calculatePosition(ObservationPoint point);
}
