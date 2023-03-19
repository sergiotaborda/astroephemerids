package astroephemeris.coordinates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class ObservationPoint {

	
	public static ObservationPoint at(
	    GeoCoordinates location,
	    ZonedDateTime time
	) {
		return new ObservationPoint(location, time);
	}
	
	private GeoCoordinates location;
	private ZonedDateTime time;
	private TimeZone timeZone;

	private ObservationPoint(GeoCoordinates location, ZonedDateTime time) {
		this.location = location;
		this.time = time;
		this.timeZone = TimeZone.getTimeZone(time.getZone());
	}

	public Number julianDay() { 
		var utc = utcTime();
        var jd0 = jd0(utc.toLocalDate());
        var time = Number.from(utc.getHour())
        		.plus(Number.reason(utc.getMinute(), 60))
        		.plus(Number.reason(utc.getSecond(), 3600))
        		.over(Number.from(24));
        
        //		(utc.getHour() + ((utc.getMinute()) / 60.0) + (utc.getSecond() / 3600.0)) / 24;
        return jd0.plus(time);   
	}
	
	private Number jd0(LocalDate date) {
        int y = date.getYear();
        int m = date.getMonthValue();
        if (m < 3) {
            m += 12;
            y -= 1;
        }
        int day = date.getDayOfMonth();
        double a = Math.floor(y / 100);
        double b = 2 - a + Math.floor(a / 4);
        double j = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524.5;
        return Number.from(j);
    }
	
	
	public Number factorT() {
		return julianDay().minus(Number.from(2451545)).over(Number.from(36525)); 
	}
	
	
	public GeoCoordinates location() {
		return location;
	}

	public ZonedDateTime time() {
		return time;
	}
	
	public ZonedDateTime utcTime() {
		return time.withZoneSameInstant(ZoneId.of("UTC"));
	}


	public TimeZone timeZone() {
		return timeZone;
	}

	public RightAscention localSideralTime() {
		
		 var utc = utcTime();
		double res = greenwichSideralTime(utc.toLocalDate());
        res += 1.00273790935 * (utc.getHour() + (utc.getMinute()  + (utc.getSecond() / 60.0)) / 60.0);
       
        while (res > 24) {
            res -= 24.0;
        }

        res += location.longitudeInDegrees().toDouble() / 15.0;

        while (res < 0) {
            res += 24.0;
        }

        while (res > 24) {
            res -= 24.0;
        }

        return RightAscention.hours(Number.from(res));
    }
		 
	private double greenwichSideralTime(LocalDate date) {
		
	    double T = (jd0(date).toDouble() - 2451545.0) / 36525;
        double res = 100.46061837 + T * (36000.770053608 + T * (0.000387933 - T / 38710000.0));
 
        return Angle.degrees(res).simplify().valueInDegrees() / 15;
//		var st = TimeExpantion.of(
//				Angle.degrees(100.46061837),
//				Angle.degrees(36000.770053608),
//				Angle.degrees(0.000387933),
//				Angle.degrees(Number.from(-38710000).invert())
//		).apply(T).simplify();
//	
//		return RightAscention.from(st);
	 }



	public ObservationPoint withTime(ZonedDateTime otherTime) {
		return new ObservationPoint(this.location, otherTime);
	}
}
