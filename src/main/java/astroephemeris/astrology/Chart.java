package astroephemeris.astrology;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import astroephemeris.Sky;
import astroephemeris.catalog.AstroKey;
import astroephemeris.coordinates.ObservationPoint;

public class Chart {

	
	private ObservationPoint point;
	private Sky sky;
	private HouseSystem houseSystem;
	private Map<String, ChartPoint> points = new LinkedHashMap<>();

	private Map<Integer, House> houses = new HashMap<>();
	private Map<AstroKey, Map<AstroKey, Aspect>> aspectsMapping = new HashMap<>();
	
	public Chart(ObservationPoint point, Sky sky, HouseSystem houseSystem) {
		this.point = point;
		this.sky = sky;
		this.houseSystem = houseSystem;
	}

	public void addPoint(ChartPoint chartPoint) {
		this.points.put(chartPoint.astro().value(), chartPoint);
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

	public void addAspect(AstroKey a, AstroKey b, Aspect aspect) {
		 aspectsMapping.computeIfAbsent(a, (k) -> new HashMap<>()).put(b, aspect);
	}

	public Optional<Aspect> aspectBetween(AstroKey a, AstroKey b) {
		 return Optional.ofNullable(aspectsMapping.get(a)).map(it -> it.get(b));
	}

	public ObservationPoint observationPoint() {
		return point;
	}

	
	public Optional<ChartPoint> getPoint(AstroKey astroKey) {
		return Optional.ofNullable(points.get(astroKey.value()));
	}
}
