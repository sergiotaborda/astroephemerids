package astroephemeris;

import java.io.BufferedReader;
import java.io.IOException;
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

import astroephemeris.catalog.AstroKey;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Number;

public class NasaHorizonsPositionCalculator implements AstroPositionCalculator {

	
	public static  NasaHorizonsPositionCalculator forAstro(AstroKey astro) {
		 return new NasaHorizonsPositionCalculator(astro);
		
	}
	
	private static Map<String, String> astroNameToBodyCodeMapping = Map.ofEntries(
			Map.entry("Sun", "Sun"),
			Map.entry("Mercury", "199"),
			Map.entry("Venus", "299"),
			Map.entry("Moon", "301"),
			Map.entry("Mars", "499"),
			Map.entry("Jupiter", "599"),
			Map.entry("Saturn", "699"),
			Map.entry("Uranus", "799"),
			Map.entry("Neptune", "899"),
			Map.entry("Pluto", "134340")
	);
	
	private AstroKey astro;
	private HttpClient httpClient = HttpClient.newHttpClient();
	
	NasaHorizonsPositionCalculator(AstroKey astro){
		this.astro = astro;
	}
	
	@Override
	public AstroPosition positionFrom(ObservationPoint point) {
		try {
			return asyncPositionFrom(point).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public CompletableFuture<AstroPosition> asyncPositionFrom(ObservationPoint point) {	
		
		if (astro.value().equals("TrueNode")) {
			return calculateTrueNodePosition(point);
		}
		return calculatePlanetPosition(point);
	
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

	private CompletableFuture<AstroPosition> calculatePlanetPosition(ObservationPoint point) {
		var code = Optional.ofNullable(astroNameToBodyCodeMapping.get(astro.value())).orElse(astro.value());
		
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
