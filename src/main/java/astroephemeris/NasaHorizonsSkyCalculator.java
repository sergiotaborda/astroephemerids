package astroephemeris;

import java.util.concurrent.CompletableFuture;

import astroephemeris.coordinates.ObservationPoint;

public class NasaHorizonsSkyCalculator implements SkyCalculator {
	
	
	public NasaHorizonsSkyCalculator() {
		
	}
	
	@Override
	public Sky calculate(Sky sky, ObservationPoint point) {
	
		
		sky.astros().stream().map(astro -> 
				 NasaHorizonsPositionCalculator.forAstro(astro).asyncPositionFrom(point).thenApply( position -> {
					 sky.setAstroPosition(astro, position);
					 return sky;
				 })) .map(CompletableFuture::join)
		         .toList();

		return sky;
	}

}
