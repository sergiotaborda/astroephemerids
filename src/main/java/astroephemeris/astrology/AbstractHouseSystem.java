package astroephemeris.astrology;

public abstract class AbstractHouseSystem implements HouseSystem{

	protected final void extrapolateOpositeHouses(Chart chart) {
		
		chart.getHouse(10).map(h -> new House(4, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		chart.getHouse(11).map(h -> new House(5, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		chart.getHouse(12).map(h -> new House(6, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		chart.getHouse(1).map(h -> new House(7, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		chart.getHouse(2).map(h -> new House(8, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		chart.getHouse(3).map(h -> new House(9, h.cuspid().oposite())).ifPresent(it -> chart.addHouse(it));
		
	
	}
}
