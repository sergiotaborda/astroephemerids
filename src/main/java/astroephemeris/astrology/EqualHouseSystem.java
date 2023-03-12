package astroephemeris.astrology;

import astroephemeris.coordinates.RightAscention;

public class EqualHouseSystem implements HouseSystem {
	

	@Override
	public Chart calculateHouses(Chart chart) {
		
		for (int i = 1; i <= 12; i++ ) {
			chart.addHouse(new House(i, SignPosition.from(RightAscention.degrees(astroephemeris.math.Number.from((i-1) * 30)))));
		}
		
		return chart;
	}

}
