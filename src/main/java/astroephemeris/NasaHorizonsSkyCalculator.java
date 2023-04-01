package astroephemeris;

import java.util.concurrent.CompletableFuture;

import astroephemeris.coordinates.ObservationPoint;

public class NasaHorizonsSkyCalculator implements SkyCalculator {
	
	private static final NasaHorizonsPositionCalculator CALCULATOR = new NasaHorizonsPositionCalculator();
	
	public NasaHorizonsSkyCalculator() {
		
	}
	
	@Override
	public Sky calculate(Sky sky, ObservationPoint point) {
	
		
		sky.pointsOfInterest().stream().map(astro -> 
				  CALCULATOR.asyncPositionFrom(astro, point).thenApply( position -> {
					 sky.setPointPosition(astro, position);
					 return sky;
				 })) .map(CompletableFuture::join)
		         .toList();

		return sky;
	}

}
