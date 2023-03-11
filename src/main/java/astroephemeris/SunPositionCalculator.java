package astroephemeris;

import astroephemeris.catalog.AstroCatalog;
import astroephemeris.catalog.Planet;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.SpacialCoordinate;

public class SunPositionCalculator implements AstroPositionCalculator {

	@Override
	public AstroPosition positionFrom(ObservationPoint point) {
	
		    var earth = AstroCatalog.instance().planet(Planet.EARTH);

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
