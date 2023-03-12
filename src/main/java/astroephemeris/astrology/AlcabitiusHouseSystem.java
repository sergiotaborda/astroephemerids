package astroephemeris.astrology;

import java.util.List;

import astroephemeris.HeliocentricDynamics;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public class AlcabitiusHouseSystem extends AbstractHouseSystem {
	

	@Override
	public Chart calculateHouses(Chart chart) {
		//http://radixpro.com/project-houses/house-systems/alcabitius/
		
		var RAMC = chart.observationPoint().localSideralTime().toDegrees();
		var asc = chart.getPoint(new Ascendent()).orElseThrow().signPosition().angle();
		var mc = chart.getPoint(new MidHeaven()).orElseThrow().signPosition().angle();

		var increment = mc.minus(asc).abs();
		
		if (increment.compareTo(Angle.degrees(180.0)) > 0) {
			increment = increment.plus(Angle.degrees(180.0)).simplify();
		}
		 increment =increment.over(astroephemeris.math.Number.from(3));
		
	
	
		var a = mc;
		chart.addHouse(new House(10, SignPosition.from(a)));
		a = a.plus(increment).simplify();
		chart.addHouse(new House(11, SignPosition.from(a)));
		a = a.plus(increment).simplify();
		chart.addHouse(new House(12, SignPosition.from(a)));
		a = asc;
		chart.addHouse(new House(1, SignPosition.from(a)));
		a = a.plus(increment).simplify();
		chart.addHouse(new House(2, SignPosition.from(a)));
		a = a.plus(increment).simplify();
		chart.addHouse(new House(3, SignPosition.from(a)));

		
		extrapolateOpositeHouses(chart);
		
		return chart;
	}

	

}
