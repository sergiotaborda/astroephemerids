package astroephemeris.astrology;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import astroephemeris.AscendentPositionCalculator;
import astroephemeris.MidHeavenPositionCalculator;
import astroephemeris.NasaHorizonsSkyCalculator;
import astroephemeris.Sky;
import astroephemeris.SkyCalculator;
import astroephemeris.catalog.AstroKey;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.math.Angle;

public class ChartCalculator {

	private List<ChartPointCalculator> calculators = new LinkedList<>();
	private SkyCalculator skyCalculator = new NasaHorizonsSkyCalculator();
	private List<AstroKey> astros = new LinkedList<>();
	private HouseSystem houseSystem = new EqualHouseSystem();
	
	public Chart calculate(ObservationPoint point) {
		
		var sky = new Sky();
		
		sky.addAstros(astros);
		
		sky = skyCalculator.calculate(sky, point);
		
		var chart = new Chart( point , sky, houseSystem);
		
		
		var position = new AscendentPositionCalculator().positionFrom(point);
		
		chart.addPoint(new ChartPoint(new Ascendent(), SignPosition.from(position.astoCoordinate().rightAscention())));
	
		
	    position = new MidHeavenPositionCalculator().positionFrom(point);
		
		chart.addPoint(new ChartPoint(new MidHeaven(), SignPosition.from(position.astoCoordinate().rightAscention())));
	
		
		houseSystem.calculateHouses(chart);
		
		
		for (var astro : sky.astros()) {
			
		    sky.getAstroPosition(astro).ifPresent(	p -> {
			    var s  = SignPosition.from(p.astoCoordinate().rightAscention());
				
			    chart.addPoint(new ChartPoint(astro,  s));
		    });
			
		}
		
		for (var c : calculators) {
			c.addPoints(chart);
		}
		
		// calculate aspects
		var points = chart.points();
		
		
		for (int i = 0; i < points.size(); i++) {
			var ip = points.get(i);
			
			for (int j = i + 1; j < points.size(); j++) {

				var jp = points.get(j);
				
				aspectOf( ip.signPosition().angle().minus(jp.signPosition().angle()).abs())
				.ifPresent(aspect -> chart.addAspect(ip.astro(), jp.astro(), aspect));

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
	
	public ChartCalculator setSkyCalculator(SkyCalculator calculator) {
		this.skyCalculator = calculator;
		return this;
	}
	
	public ChartCalculator addChartPointCalculator(ChartPointCalculator calculator) {
		this.calculators.add(calculator);
		return this;
	}
	
	public ChartCalculator addAstro(AstroKey astro) {
		this.astros.add(astro);
		return this;
	}
	
	public <A extends AstroKey> ChartCalculator addAstros(A ... keys) {
		
		for (var k : keys) {
			addAstro(k);
		}
		return this;
	}
	
}
