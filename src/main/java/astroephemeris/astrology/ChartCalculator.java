package astroephemeris.astrology;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import astroephemeris.AscendentPositionCalculator;
import astroephemeris.MeanLilithPositionCalculator;
import astroephemeris.MidHeavenPositionCalculator;
import astroephemeris.NasaHorizonsPositionCalculator;
import astroephemeris.PointOfInterestPositionCalculator;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.math.Angle;

public class ChartCalculator {

	private List<ChartPointCalculator> calculators = new LinkedList<>();
	private List<PointOfInterest> pointsOfInterest = new LinkedList<>();
	private HouseSystem houseSystem = new EqualHouseSystem();
	
	
	public ChartCalculator() {
		this.pointsOfInterest.add(PointOfInterest.ASC);
		this.pointsOfInterest.add(PointOfInterest.MC);
		
		this.addPointCalculator(new AscendentPositionCalculator());
		this.addPointCalculator(new MidHeavenPositionCalculator());
		this.addPointCalculator(new MeanLilithPositionCalculator());
		this.addPointCalculator(new NasaHorizonsPositionCalculator());
	}
	
	public ChartCalculator addPointCalculator(PointOfInterestPositionCalculator calculator) {
		this.calculators.add(new ChartPointCalculator() {

			@Override
			public void addPoints(Chart chart) {
				for (var p : pointsOfInterest) {
					if (calculator.canCalculatePosition(p)) {
						var position = calculator.calculatePosition(p, chart.observationPoint());
						chart.addPoint(new ChartPoint(p, SignPosition.from(position.astoCoordinate().rightAscention())));
					}
				}
			}
			
		});
		return this;
	}
	
	
	public Chart calculate(ObservationPoint point) {
		
	
		var chart = new Chart( point, houseSystem);
		

		for (var c : calculators) {
			c.addPoints(chart);
		}
		
		// calculate houses
		
		houseSystem.calculateHouses(chart);
		
		
		// calculate aspects
		var points = chart.points();
		
		
		for (int i = 0; i < points.size(); i++) {
			var ip = points.get(i);
			
			for (int j = i + 1; j < points.size(); j++) {

				var jp = points.get(j);
				
				aspectOf( ip.signPosition().angle().minus(jp.signPosition().angle()).abs())
				.ifPresent(aspect -> chart.addAspect(ip.point(), jp.point(), aspect));

			}
		}
		
		
		return chart;
		
	}
	
	private Optional<Aspect> aspectOf(Angle diff) {
		for (var aspect : Aspect.values()) {
			var min = aspect.difference().minus(Angle.degrees(5));
			var max = aspect.difference().plus(Angle.degrees(5));
			
			if (diff.compareTo(min) >=0 && diff.compareTo(max) <=0) {
				return Optional.of(aspect);
			}
		}
		return Optional.empty();
	}

	public ChartCalculator setHouseSystem(HouseSystem houseSystem) {
		this.houseSystem = houseSystem;
		return this;
	}
	
	public ChartCalculator addChartPointCalculator(ChartPointCalculator calculator) {
		this.calculators.add(calculator);
		return this;
	}
	
	public ChartCalculator addPoint(PointOfInterest point) {
		this.pointsOfInterest.add(point);
		return this;
	}
	
	public <A extends PointOfInterest> ChartCalculator addPoints(A ... points) {
		
		for (var p : points) {
			addPoint(p);
		}
		return this;
	}
	
	public <A extends PointOfInterest> ChartCalculator addPoints(Collection<A> points) {
		
		for (var p : points) {
			addPoint(p);
		}
		return this;
	}
	
}
