package astroephemeris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import astroephemeris.astrology.SignPosition;
import astroephemeris.catalog.AstroKey;
import astroephemeris.catalog.Moon;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;
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
		} else if (astro.value().equals("MeanLilith")) {
			return calculateLilihtMeanPosition(point);
		}
		return calculatePlanetPosition(astro,point);
	
	}

	private CompletableFuture<AstroPosition> calculateLilihtMeanPosition(ObservationPoint point) {

		var origin = point.utcTime().toLocalDateTime();
		var start = format( point.utcTime().minusDays(18));
		var end = format( point.utcTime().plusMinutes(1).plusDays(18));
		
		record Pair(LocalDateTime time, double distance, double velocidty) {}
		
		var url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=301"
				+ "&OBJ_DATA=NO&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=geo&START_TIME="
				+ start
				+ "&STOP_TIME="
				+ end
				+"&STEP_SIZE=1min&QUANTITIES=%2220%20%22&CSV_FORMAT=YES";
				

		try {
			var request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
		
	
			return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(response -> {
						if (response.statusCode() == 200) {
							var text = response.body();
							
							var reader = new BufferedReader(new StringReader(text));
							
						    int mark = -1;
							var table = new ArrayList<Pair>(60000);
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
									var item = new Pair(parse(data[0].trim()), Double.parseDouble(data[3]), Double.parseDouble(data[4]));
									table.add(item);
									if (item.time.equals(origin)) {
										mark = table.size() - 1;
									}
								}
							}
	
							var inflectionPoint = new Pair[2];
							var index = 0;
							for (int i = 1; i < table.size() - 1; i++) {
							    var da = table.get(i-1).velocidty;
								var d0 = table.get(i).velocidty;
								var dd = table.get(i+1).velocidty;
								if (Math.signum(d0) != Math.signum(da) || Math.signum(d0) != Math.signum(dd)) {
									inflectionPoint[index++] = table.get(i+1);
									i+=7;
									if (index > 1) {
										break;
									}
								}
							}
							
//							for (int i = mark - 1; i >=0 ; i--) {
//							    var da = table.get(i-1).velocidty;
//								var d0 = table.get(i).velocidty;
//								var dd = table.get(i+1).velocidty;
//								if (Math.signum(d0-dd) != Math.signum(da-d0)) {
//									inflectionPoint[0] = table.get(i);
//									break;
//								}
//							}
//							for (int i = mark + 1; i < table.size() ; i++) {
//							    var da = table.get(i-1).velocidty;
//								var d0 = table.get(i).velocidty;
//								var dd = table.get(i+1).velocidty;
//								if (Math.signum(d0-dd) != Math.signum(da-d0)) {
//									inflectionPoint[1] = table.get(i);
//									break;
//								}
//							}
//							
							Pair apogee = null;
							Pair peregee = null;
							
							if (inflectionPoint[0].distance > inflectionPoint[1].distance) {
								apogee = inflectionPoint[0];
								peregee = inflectionPoint[1];
							} else {
								apogee = inflectionPoint[1];
								peregee = inflectionPoint[0];
							}
							
							var moon = new Moon();
							var La = calculatePlanetPosition(moon,point.withTime(apogee.time.atZone(ZoneId.of("UTC"))));
							var Lp = calculatePlanetPosition(moon,point.withTime(peregee.time.atZone(ZoneId.of("UTC"))));
							
				
							
							try {
								var minutes = Duration.between(apogee.time, peregee.time).abs().toDays();
								var eventMinutes = Duration.between(apogee.time, point.utcTime()).abs().toDays();
								
								var u = Lp.get().astoCoordinate().rightAscention().toDegrees()
									.minus(Angle.HALF_CIRCLE)
									.minus(La.get().astoCoordinate().rightAscention().toDegrees());
								
								var v = u.over(Number.from(minutes));
								
								var p = La.get().astoCoordinate().rightAscention().toDegrees().minus(v.times(Number.from(eventMinutes)));
								
								var s = SignPosition.from(p);
								
								  return new AstroPosition(
								        	point,
								        	new AstroCoordinate(RightAscention.from(p), null, null),
								        	null
									    );
								
							} catch (InterruptedException | ExecutionException e) {
								throw new RuntimeException(e);
							}
							
						  
						
						} else {
							throw new RuntimeException(response.body());
						}
						
					});
			
	
		
		} catch ( URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private LocalDateTime parse(String dateTime) {
		var parts = dateTime.split(" ");
		var date = parts[0].split("-");
		var time = parts[1].split(":");
		var year = Integer.parseInt(date[0]);
		var month = parseMonth(date[1]);
		var day = Integer.parseInt(date[2]);
		var hour = Integer.parseInt(time[0]);
		var min = Integer.parseInt(time[1]);
		
		return LocalDateTime.of(year, month, day, hour, min, 0,0);
	}

	private int parseMonth(String name) {
		return switch (name.toLowerCase()){
		case "jan" -> 1;
		case "feb" -> 2;
		case "mar" -> 3;
		case "apr" -> 4;
		case "may" -> 5;
		case "jun" -> 6;
		case "jul" -> 7;
		case "aug" -> 8;
		case "sep" -> 9;
		case "oct" -> 10;
		case "nov" -> 11;
		case "dez" -> 12;
		default -> throw new RuntimeException("month = "+ name);
		}; 
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
						
							var L = NasaHorizonsPositionCalculator.forAstro(new Moon()).positionFrom(point).astoCoordinate().rightAscention().toDegrees();
							 
							var omegaM =  Angle.degrees(Number.from(Double.parseDouble(list.get(0)[5])));
							var MeanAnomaly =  RightAscention.degrees(Number.from(Double.parseDouble(list.get(0)[9])));
							
							var w = Angle.degrees(Double.parseDouble(list.get(0)[6]));
							
							
							
					
							 var x = SignPosition.from(L.minus(w).minus(omegaM).simplify());
							 
						    var nu = Angle.degrees(Double.parseDouble(list.get(0)[5]) + Double.parseDouble(list.get(0)[10]));
						    var mean = Angle.degrees(Double.parseDouble(list.get(0)[5]) + Double.parseDouble(list.get(0)[9]));
						    
						 
						    var a = SignPosition.from( L.minus(nu));
						    var b = SignPosition.from( L.plus(nu));
						    
						    var c = SignPosition.from( L.minus(mean));
						    var d = SignPosition.from( L.plus(mean));
						    
						
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

	private CompletableFuture<AstroPosition> calculatePlanetPosition(AstroKey realAstro, ObservationPoint point) {
		var code = Optional.ofNullable(astroNameToBodyCodeMapping.get(realAstro.value())).orElse(realAstro.value());
		
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
