package astroephemeris;

import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;

public interface AstroPositionCalculator {

	
	AstroPosition positionFrom(ObservationPoint point);
}
