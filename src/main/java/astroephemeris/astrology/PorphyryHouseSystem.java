package astroephemeris.astrology;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.math.Angle;

public class PorphyryHouseSystem extends AbstractHouseSystem {
	

	@Override
	public Chart calculateHouses(Chart chart) {
		// http://radixpro.com/project-houses/house-systems/porphyri/

		var asc = chart.getPoint(PointOfInterest.ASC).orElseThrow().signPosition().angle();
		var mc = chart.getPoint(PointOfInterest.MC).orElseThrow().signPosition().angle();

		var increment = mc.minus(asc).abs();
		
		if (increment.compareTo(Angle.HALF_CIRCLE) > 0) {
			increment = increment.plus(Angle.HALF_CIRCLE).simplify();
		}
		 
		var increment4 = increment.over(astroephemeris.math.Number.from(3));
		
	    increment = mc.plus(Angle.HALF_CIRCLE).simplify().minus(asc).abs();
	    
		if (increment.compareTo(Angle.HALF_CIRCLE) > 0) {
			increment = increment.plus(Angle.HALF_CIRCLE).simplify();
		}
		
		var increment1 = increment.over(astroephemeris.math.Number.from(3));
		
		
		var a = mc;
		chart.addHouse(new House(10, SignPosition.from(a)));
		a = a.plus(increment4).simplify();
		chart.addHouse(new House(11, SignPosition.from(a)));
		a = a.plus(increment4).simplify();
		chart.addHouse(new House(12, SignPosition.from(a)));
		a = asc;
		chart.addHouse(new House(1, SignPosition.from(a)));
		a = a.plus(increment1).simplify();
		chart.addHouse(new House(2, SignPosition.from(a)));
		a = a.plus(increment1).simplify();
		chart.addHouse(new House(3, SignPosition.from(a)));

		
		extrapolateOpositeHouses(chart);
		
		return chart;
	}

	

}
