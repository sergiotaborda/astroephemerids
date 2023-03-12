package astroephemeris.astrology;

import java.util.List;

import astroephemeris.HeliocentricDynamics;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public class RegiomontanusHouseSystem extends AbstractHouseSystem {
	

	@Override
	public Chart calculateHouses(Chart chart) {
		//http://radixpro.com/project-houses/house-systems/regiomontanus/
		
		var GL = chart.observationPoint().location().latitude();
		var RAMC = chart.observationPoint().localSideralTime().toDegrees();
		var E = HeliocentricDynamics.obliquityEcliptic(chart.observationPoint());
		
		var cuspids = List.of(Angle.degrees(30),Angle.degrees(60),Angle.degrees(120),Angle.degrees(150)).stream()
			.map(H -> {
				var numerator = H.sin().times(GL.tan());
				var denominator = H.plus(RAMC).cos();
				var R = Angle.atan2d(numerator, denominator);
				numerator = R.cos().times( H.plus(RAMC).tan());
				denominator = R.plus(E).cos();
				
				return Angle.atan2d(numerator, denominator);
			}).toList();
			
		var mc = chart.getPoint(new MidHeaven()).orElseThrow();
		var asc = chart.getPoint(new Ascendent()).orElseThrow();
		
		chart.addHouse(new House(1, asc.signPosition()));
		chart.addHouse(new House(2, SignPosition.from(RightAscention.from(cuspids.get(2)))));
		chart.addHouse(new House(3, SignPosition.from(RightAscention.from(cuspids.get(3)))));
//		
//		chart.addHouse(new House(4, mc.signPosition().oposite()));
//		chart.addHouse(new House(5, SignPosition.from(RightAscention.from(cuspids.get(0))).oposite()));
//		chart.addHouse(new House(6, SignPosition.from(RightAscention.from(cuspids.get(1))).oposite()));
//		chart.addHouse(new House(7, asc.signPosition().oposite()));
//		chart.addHouse(new House(8, SignPosition.from(RightAscention.from(cuspids.get(2))).oposite()));
//		chart.addHouse(new House(9, SignPosition.from(RightAscention.from(cuspids.get(3))).oposite()));
		
		chart.addHouse(new House(10, mc.signPosition()));
		chart.addHouse(new House(11, SignPosition.from(RightAscention.from(cuspids.get(0)))));
		chart.addHouse(new House(12, SignPosition.from(RightAscention.from(cuspids.get(1)))));
		
		extrapolateOpositeHouses(chart);
		
		return chart;
	}

	

}
