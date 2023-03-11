package astroephemeris.coordinates;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public record AstroCoordinate(RightAscention rightAscention, Angle declination,Number distance) {

	public ObservedPoint toObservedPoint(ObservationPoint point) {
		var lst = point.localSideralTime();
		 
		var u = lst.minus(rightAscention).toDegrees();
		var x = u.cos().times(declination.cos()); 
		var y = u.sin().times(declination.cos()); 
		var z = declination.sin();
		
		
        // rotate so z is the local zenith
		var latitude = point.location().latitude();
		var xhor = x.times(latitude.sin()).minus(z.times(latitude.cos())); //   double xhor = x * MathUtils.sind(obs.getLatitude()) - z * MathUtils.cosd(obs.getLatitude());
		var yhor = y;
		var zhor = x.times(latitude.cos()).plus(z.times(latitude.sin())); // double zhor = x * MathUtils.cosd(obs.getLatitude()) + z * MathUtils.sind(obs.getLatitude());
     
		
		var azimuth = Angle.atan2d(yhor, xhor).plus(Angle.degrees(180)); // double azimuth = MathUtils.rev(MathUtils.atan2d(yhor, xhor) + 180.0); // so 0 degrees is north
        var altitude = Angle.atan2d(zhor, xhor.square().plus(yhor.square())); // double altitude = MathUtils.atan2d(zhor, Math.sqrt(xhor * xhor + yhor * yhor));
        
        
        return new ObservedPoint(altitude, azimuth);
	}
	

	
}
