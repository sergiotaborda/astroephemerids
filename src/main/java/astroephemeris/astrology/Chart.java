package astroephemeris.astrology;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.ObservationPoint;

public class Chart {

	
	private ObservationPoint point;
	private HouseSystem houseSystem;
	private Map<PointOfInterest, ChartPoint> points = new LinkedHashMap<>();

	private Map<Integer, House> houses = new HashMap<>();
	private Map<AspectKey, Aspect> aspectsMapping = new HashMap<>();
	private Map<PointOfInterest,Integer> pointsHouses = new HashMap<>();
	
	public Chart(ObservationPoint point, HouseSystem houseSystem) {
		this.point = point;
		this.houseSystem = houseSystem;
	}

	public void addPoint(ChartPoint chartPoint) {
		this.points.put(chartPoint.point(), chartPoint);
	}
	
	public List<ChartPoint> points(){
		return points.values().stream().toList();
	}

	public List<House> houses(){
		return houses.values().stream().sorted((a,b) -> a.number() - b.number()).toList();
	}
	
	public Optional<House> getHouse(int number){
		return Optional.ofNullable(houses.get(number));
	}
	
	
	public void addHouse(House house) {
		this.houses.put(house.number(), house);
	}

	public void addAspect(PointOfInterest a, PointOfInterest b, Aspect aspect) {
		if (a != null && b != null) {
			 aspectsMapping.put(new AspectKey(a,b), aspect);
		}
		
	}

	public Optional<Aspect> aspectBetween(PointOfInterest a, PointOfInterest b) {
		 if (a != null && b != null) {
			 return Optional.ofNullable(aspectsMapping.get(new AspectKey(a,b)));
		 }
		 return Optional.empty();
	}

	public ObservationPoint observationPoint() {
		return point;
	}

	
	public Optional<ChartPoint> getPoint(PointOfInterest point) {
		return Optional.ofNullable(points.get(point));
	}


	public Optional<Integer> houseof(PointOfInterest point) {
		var h = pointsHouses.get(point);
		if (h != null) {
			return Optional.of(h);
		}
		
		var pp = points.get(point);
		
		for (var entry : houses.entrySet()) {
			if (pp.signPosition().angle().compareTo(entry.getValue().cuspid().angle()) <= 0) {
				var previous = houses.get((entry.getKey() - 1) % 12);
				if (pp.signPosition().angle().compareTo(previous.cuspid().angle()) >= 0) {
					pointsHouses.put(point, entry.getKey());
					return Optional.of(entry.getKey());
				}
			}
		}
		return Optional.empty();
	}
}

class AspectKey {
	
	PointOfInterest a;
	PointOfInterest b;
	
	AspectKey(PointOfInterest a, PointOfInterest b){
		this.a = a;
		this.b = b;
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return  other instanceof AspectKey that
				&& ( ( this.a.equals(that.a) &&  this.b.equals(that.b))
					|| ( this.a.equals(that.b) &&  this.b.equals(that.a))
				);
	}
}
