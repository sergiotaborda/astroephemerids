package astroephemeris;

import astroephemeris.catalog.AstroCatalog;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.SpacialCoordinate;

public class SunPositionCalculator extends SingleAstroPositionCalculator {

	protected SunPositionCalculator() {
		super(PointOfInterest.SUN);
	}

	@Override
	public AstroPosition calculatePosition(ObservationPoint point) {
	
		    var earth = AstroCatalog.instance().planet(PointOfInterest.EARTH).orElseThrow();

	        var sun_xyz = SpacialCoordinate.origin();
	        var earth_xyz = HeliocentricDynamics.positionFrom(point,earth);
	        var radec = HeliocentricDynamics.radecr(point, sun_xyz, earth_xyz);
	        
	        
	        var altaz = radec.toObservedPoint(point);
	      
	        AstroPosition eph = new AstroPosition(
	        	point,
	        	radec,
	        	altaz
	        );

	        return eph;
	}

}
