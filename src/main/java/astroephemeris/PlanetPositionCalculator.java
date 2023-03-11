package astroephemeris;

import astroephemeris.catalog.AstroCatalog;
import astroephemeris.catalog.Planet;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;

public class PlanetPositionCalculator implements AstroPositionCalculator{

	
	public static AstroPositionCalculator of(Planet planet) {
		return new PlanetPositionCalculator(planet);
	}
	
	private final Planet planet;

	PlanetPositionCalculator(Planet planet) {
		this.planet = planet;
	}
	
	@Override
	public AstroPosition positionFrom(ObservationPoint point) {
        var earth = AstroCatalog.instance().planet(Planet.EARTH);
        var astro = AstroCatalog.instance().planet(planet);
        
        var planet_xyz = HeliocentricDynamics.positionFrom(point, astro);
        var earth_xyz = HeliocentricDynamics.positionFrom(point, earth);
        var radec = HeliocentricDynamics.radecr(point, planet_xyz, earth_xyz);
        
        var altaz = radec.toObservedPoint(point);
        
        return new AstroPosition(
	        	point,
	        	radec,
	        	altaz
	    );
        
	}

}
