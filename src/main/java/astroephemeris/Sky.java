package astroephemeris;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroPosition;

public class Sky {

	
	private List<PointOfInterest> pointsOfInterest = new ArrayList<>();
	private Map<PointOfInterest, AstroPosition> astroPositions = new HashMap<>();
	
	public Sky addAstro(PointOfInterest pointOfInterest) {
		if (!pointOfInterest.equals(PointOfInterest.EARTH)){
			pointsOfInterest.add(pointOfInterest);
		}
	
		return this;
	}
	
	public <A extends PointOfInterest> Sky addPoints(A ... points) {
		
		for (var k : points) {
			addAstro(k);
		}
		return this;
	}
	
	public <A extends PointOfInterest> Sky addAstros(Collection<A> keys) {
		
		for (var k : keys) {
			addAstro(k);
		}
		return this;
	}
	
	public List<PointOfInterest> pointsOfInterest() {
		return Collections.unmodifiableList(pointsOfInterest);
	}
	
	public void setPointPosition(PointOfInterest key, AstroPosition astroPosition) {
		astroPositions.put(key, astroPosition);
	}
	
	public Optional<AstroPosition> getPointPosition(PointOfInterest point) {
		return Optional.ofNullable(astroPositions.get(point));
	}
}
