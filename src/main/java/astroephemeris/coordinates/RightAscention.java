package astroephemeris.coordinates;

import java.time.LocalDateTime;
import java.time.LocalTime;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class RightAscention {

	public static RightAscention from(Angle angle) {
		return new RightAscention(angle);
	}
	
	public static RightAscention degrees(Number degrees) {
		return new RightAscention(Angle.degrees(degrees));
	}
	
	public static RightAscention hours(Number hours) {
		return new RightAscention(Angle.degrees(15.0).times(hours));
	}

	public static RightAscention hours(int  hours,  int minutes, double seconds) {
		var ms = Number.from(seconds).over(Number.from(60));
		var hm = Number.from(minutes).plus(ms).over(Number.from(60));
		
		var h = Number.from(hours).plus(hm);
		return new RightAscention(Angle.degrees(15.0).times(h));
	}

	
	private final Angle degrees;
	
	public RightAscention(Angle degrees) {
		this.degrees = degrees;
	}
	
	public Angle toDegrees() {
		return degrees;
	}
	
	public Number toHours() {
		return degrees.ratio(Angle.degrees(15.0));
	}
	
	@Override
	public String toString() {
		var h = toHours();
		var hfloor = h.floor();
		var min = h.minus(hfloor).times(Number.from(60));
		var minfloor = min.floor();
		var sec = min.minus(minfloor).times(Number.from(60));

		return ((int)hfloor.toDouble()) + ":" + ((int)minfloor.toDouble()) + ":" + sec;
	}


	@Override
	public boolean equals(Object other) {
		return other instanceof RightAscention that
				&& this.degrees.equals(that.degrees);
	}

	
	@Override
	public int hashCode() {
		return degrees.hashCode();
	}

	public RightAscention minus(RightAscention rightAscention) {
		 return new RightAscention(this.degrees.minus(rightAscention.degrees));
	}


	public LocalTime toTime() {
		var h = this.toHours().toDouble();
		var hh = Math.floor(h);
		var m = (h - hh) * 60;
		var mm =  Math.floor(m);
		var s = (m - mm) * 60;
		var ss =  Math.floor(s);
		
		return LocalTime.of((int)hh, (int)mm, (int)ss);
	}

}
