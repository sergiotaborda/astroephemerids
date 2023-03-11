package astroephemeris;

import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public class MidHeavenPositionCalculator implements AstroPositionCalculator{
	

	@Override
	public AstroPosition positionFrom(ObservationPoint point) {
	   
		var localSiderealTime = point.localSideralTime();
		var obliquityEcliptic = HeliocentricDynamics.obliquityEcliptic(point);
		
		// http://radixpro.com/a4a-start/medium-coeli/
		//  L = atan( sin ARMC / (cos ARMC . cos E))
		
		var numerator = localSiderealTime.toDegrees().sin();
		var denominator = localSiderealTime.toDegrees().cos().times(obliquityEcliptic.cos());
		
		var mc = Angle.atan2d(numerator, denominator);
		
	    return new AstroPosition(
        	point,
        	new AstroCoordinate(RightAscention.from(mc), null, null),
            null
	    );
	    
	}

}
