package astroephemeris;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public class AscendentPositionCalculator extends SingleAstroPositionCalculator{
	

	public AscendentPositionCalculator() {
		super(PointOfInterest.ASC);
	}

	@Override
	public AstroPosition calculatePosition(ObservationPoint point) {
	    var latitude = point.location().latitude();
		var localSiderealTime = point.localSideralTime();
		var obliquityEcliptic = HeliocentricDynamics.obliquityEcliptic(point);
		
		
	    // http://radixpro.com/a4a-start/the-ascendant/
		// Longitude asc = atan( cos RAMC / -(sin E . tan GL + cos E . sin RAMC))
		var numerator = localSiderealTime.toDegrees().cos();
		// 0.9880984521266581
		// 0.9880984516881889
		var s = obliquityEcliptic.sin(); 
		var lt = latitude.tan();
		var b = s.times(lt);
		//0.5130741263148573
		//0.24368750299836553
	    var c = obliquityEcliptic.cos().times(localSiderealTime.toDegrees().sin());
	    // 0.1411319832244320
	    // 0.14113198609973268
	    var denomiator = b.plus(c).negate();
	    
	    var x = numerator.over(denomiator);
	    var ascendant = Angle.atan(x);


	    ascendant = ascendant.simplify();
	    
	    return new AstroPosition(
        	point,
        	new AstroCoordinate(RightAscention.from(ascendant), null, null),
            null
	    );
	    
	}


}
