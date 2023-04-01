package astroephemeris;

import java.util.Set;

import astroephemeris.catalog.AstroCatalog;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;

public class PlanetPositionCalculator implements PointOfInterestPositionCalculator{

	private static final Set<PointOfInterest> planets = Set.of(
			PointOfInterest.MERCURY,
			PointOfInterest.VENUS,
			PointOfInterest.MARS,
			PointOfInterest.JUPITER,
			PointOfInterest.SATURN,
			PointOfInterest.URANUS,
			PointOfInterest.NEPTUNE,
			PointOfInterest.PLUTO
	);
	

	@Override
	public boolean canCalculatePosition(PointOfInterest planet) {
		return planets.contains(planet);
	}

	
	@Override
	public AstroPosition calculatePosition(PointOfInterest planet, ObservationPoint point) {
		if (!canCalculatePosition(planet)) {
			throw new IllegalArgumentException("Cannto calculate " + planet);
		}
		
	    var earth = AstroCatalog.instance().planet(PointOfInterest.EARTH).orElseThrow();
        var astro = AstroCatalog.instance().planet(planet).orElseThrow();
        
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
