package astroephemeris.coordinates;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public record GeoCoordinates(	
		Number latitudeInDegrees,
		Number longitudeInDegrees
	) {
	
	public static GeoCoordinates at(Number latitudeInDegrees, Number longitudeInDegrees) {
		return new GeoCoordinates(latitudeInDegrees , longitudeInDegrees);
	}
	
	public static GeoCoordinates at(String latitudeInDegreesFormat, String longitudeInDegreesFormat) {
		return at(parse(latitudeInDegreesFormat), parse(longitudeInDegreesFormat) );
	}

	private static Number parse(String text) {
		var pos = text.indexOf("°");
		if (pos< 0) {
			return null;
		}
		var degrees = Integer.parseInt(text.substring(0, pos).trim());
		text = text.substring(pos+ 1);
		
		pos = text.indexOf("'", pos);
		if (pos< 0) {
			return null;
		}
		var minutes = Integer.parseInt(text.substring(0, pos).trim());
		text = text.substring(pos+ 1);
		pos = text.indexOf("'", pos);
		var seconds = 0.0;
		if (pos >= 0) {
		    seconds = Integer.parseInt(text.substring(0, pos).trim());
		    text = text.substring(pos+ 2);
		}
	
		 text = text.trim().toUpperCase();
		
		var value = Number.from(degrees)
				.plus(Number.reason(minutes, 60))
				.plus(Number.from(seconds).over(Number.from(3600)));
			
		
		if (text.equals("S") || text.equals("W")) {
			value = value.negate();
		} 
		return value;
	}

	public boolean isEast() {
		return longitudeInDegrees.toDouble() >=0;
	}
	
	public boolean isWest() {
		return longitudeInDegrees.toDouble() <0;
	}
	
	public Angle latitude() {
		return Angle.degrees(latitudeInDegrees.toDouble());
	}
	
	
	public Angle longitude() {
		return Angle.degrees(longitudeInDegrees.toDouble());
	}
	
}
