package astroephemeris;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;

public interface PointOfInterestPositionCalculator {

	boolean canCalculatePosition(PointOfInterest astro);
	
	AstroPosition calculatePosition(PointOfInterest astro, ObservationPoint point);
}
