package astroephemeris;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class NasaHorizonsPositionCalculator implements PointOfInterestPositionCalculator {


	

	private static final Map<PointOfInterest, String> POINT_TO_BODY_CODE_MAPPING = Map.ofEntries(
			Map.entry(PointOfInterest.SUN, "Sun"),
			Map.entry(PointOfInterest.MERCURY, "199"),
			Map.entry(PointOfInterest.VENUS, "299"),
			Map.entry(PointOfInterest.MOON, "301"),
			Map.entry(PointOfInterest.MARS, "499"),
			Map.entry(PointOfInterest.JUPITER, "599"),
			Map.entry(PointOfInterest.SATURN, "699"),
			Map.entry(PointOfInterest.URANUS, "799"),
			Map.entry(PointOfInterest.NEPTUNE, "899"),
			Map.entry(PointOfInterest.PLUTO, "134340")
	);
	
	
	private HttpClient httpClient = HttpClient.newHttpClient();
	
	public NasaHorizonsPositionCalculator(){}
	
	@Override
	public boolean canCalculatePosition(PointOfInterest point) {
		return POINT_TO_BODY_CODE_MAPPING.containsKey(point)
				|| PointOfInterest.MOON_TRUE_NODE.equals(point);
	}
	
	@Override
	public AstroPosition calculatePosition(PointOfInterest astro, ObservationPoint point) {
		try {
			return asyncPositionFrom(astro, point).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public CompletableFuture<AstroPosition> asyncPositionFrom(PointOfInterest astro,ObservationPoint point) {	
		
		if (astro.equals(PointOfInterest.MOON_TRUE_NODE)) {
			return calculateTrueNodePosition(point);
		} 
		return calculatePlanetPosition(astro,point);
	
	}



	private CompletableFuture<AstroPosition> calculateTrueNodePosition(ObservationPoint point) {
		var start = format(point.utcTime());
		var end = format(point.utcTime().plusMinutes(1));
		
		
		
		var url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=301&OBJ_DATA=NO&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=ELEMENTS&CENTER=geo&START_TIME="
				+ start 
				+ "&STOP_TIME="
				+ end 
				+ "&STEP_SIZE=1min&CSV_FORMAT=YES";
				
		try {
			var request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
		
	
			return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(response -> {
						if (response.statusCode() == 200) {
							var text = response.body();
							
							var reader = new BufferedReader(new StringReader(text));
							
							var list = new ArrayList<String[]>(2);
							var iterator = reader.lines().iterator();
							var dataOn = false;
							while (iterator.hasNext()) {
								var line = iterator.next();
								if (line.startsWith("$$SOE")) {
								
									dataOn = true;
									continue;
									
								} else if (line.startsWith("$$EOE")) {
									break;
								} else if (dataOn) {
									var data = line.split(",");
									list.add(data);
								}
							}
						
							var omegaM =  Angle.degrees(Number.from(Double.parseDouble(list.get(0)[5])));
						
						    return new AstroPosition(
					        	point,
					        	new AstroCoordinate(RightAscention.from(omegaM), null, null),
					        	null
						    );
						
						} else {
							throw new RuntimeException(response.body());
						}
						
					});
			
	
		
		} catch ( URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private CompletableFuture<AstroPosition> calculatePlanetPosition(PointOfInterest realAstro, ObservationPoint point) {
		var code = Optional.ofNullable(POINT_TO_BODY_CODE_MAPPING.get(realAstro)).orElse(realAstro.toString());
		
		var start = format(point.utcTime());
		var end = format(point.utcTime().plusMinutes(1));
		
		
		
		var url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND="
				+ code
				+ "&OBJ_DATA=NO&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=geo&START_TIME="
				+ start
				+ "&STOP_TIME="
				+ end
				+"&STEP_SIZE=1min&QUANTITIES=%221,31%20%22&CSV_FORMAT=YES";
				

		try {
			var request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
		
	
			return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(response -> {
						if (response.statusCode() == 200) {
							var text = response.body();
							
							var reader = new BufferedReader(new StringReader(text));
							
							var list = new ArrayList<String[]>(2);
							var iterator = reader.lines().iterator();
							var dataOn = false;
							while (iterator.hasNext()) {
								var line = iterator.next();
								if (line.startsWith("$$SOE")) {
								
									dataOn = true;
									continue;
									
								} else if (line.startsWith("$$EOE")) {
									break;
								} else if (dataOn) {
									var data = line.split(",");
									list.add(data);
								}
							}
						
							var ra =  RightAscention.degrees(Number.from(Double.parseDouble(list.get(0)[5])));
							
						    return new AstroPosition(
					        	point,
					        	new AstroCoordinate(ra, null, null),
					        	null
						    );
						
						} else {
							throw new RuntimeException(response.body());
						}
						
					});
			
	
		
		} catch ( URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private String format(ZonedDateTime time) {
		var s = time.toString();
		return s.substring(0, s.length() - 6);
	}





}
