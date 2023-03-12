package astroephemeris;

import astroephemeris.coordinates.ObservationPoint;

public interface SkyCalculator {
	
	public Sky calculate(Sky sky, ObservationPoint point);

}
