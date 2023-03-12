package astroephemeris;

import astroephemeris.catalog.Astro;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.CubicCoordinate;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.coordinates.SpacialCoordinate;
import astroephemeris.math.Angle;
import astroephemeris.math.Number;
import astroephemeris.math.TimeExpantion;

public class HeliocentricDynamics {

 
	/**
	 * Calculate heliocentric xyz for Astro as seen at ObservationPoint
	 * @param p
	 * @param obs
	 * @return
	 */
    public static CubicCoordinate positionFrom(ObservationPoint point, Astro p) {

    	if (p == null) {
    		return CubicCoordinate.ORIGIN;
    	}
        Number T = point.factorT();

        // longitude of ascending node
        Angle N = p.ascendingNode().apply(T).simplify(); 
        // inclination
        Angle i = p.inclination().apply(T).simplify();  
        // Mean longitude
        Angle L = p.meanLongitude().apply(T).simplify();
       
        // semimajor axis
        Number a = p.semiMajorAxisLength().apply(T);
        // eccentricity
        Number e = p.eccentricity().apply(T);
        
        // longitude of perihelion
        Angle P =  p.perihelion().apply(T).simplify();
        
        Angle M = L.minus(P).simplify();
        Angle w = L.minus(N).minus(M).simplify();
        
        // Eccentric anomaly
        var f = Angle.radians(e).times(M.sin()); 
        var g = Number.one().plus(e.times(M.cos()));
        
        Angle E0 = M.plus(f.times(g));
        
        var h = Angle.radians(e).times(E0.sin());
        var k = Number.one().minus(e.times(E0.cos()));

        Angle E = E0.minus(E0.minus(h).minus(M).over(k));

        while (E0.minus(E).abs().valueInDegrees()  > 0.0005) {
            E0 = E;
            h = Angle.radians(e.times(E0.sin()));
            k = Number.one().minus(e.times(E0.cos()));
            E = E0.minus(E0.minus(h).minus(M).over(k));
        };

        Number x = a.times(E.cos().minus(e));
        Number y = a.times(Number.one().minus(e.square()).sqrt()).times(E.sin()); 
        
        Number r = x.hypot(y);
        Angle v = Angle.atan2d(y, x).simplify(); 

        var u = v.plus(w);
        
        var xeclip = r.times(N.cos().times(u.cos()).minus(N.sin().times(u.sin()).times(i.cos()))); // double xeclip = r * (MathUtils.cosd(N) * MathUtils.cosd(v + w) - MathUtils.sind(N) * MathUtils.sind(v + w) * MathUtils.cosd(i));
        var yeclip = r.times(N.sin().times(u.cos()).plus(N.cos().times(u.sin()).times(i.cos()))); // double yeclip = r * (MathUtils.sind(N) * MathUtils.cosd(v + w) + MathUtils.cosd(N) * MathUtils.sind(v + w) * MathUtils.cosd(i));;
        var zeclip = r.times(u.sin().times(i.sin())); // double zeclip = r * MathUtils.sind(v + w) * MathUtils.sind(i);

        return SpacialCoordinate.xyz(xeclip, yeclip, zeclip);
    }
    
    // radecr returns ra, dec and earth distance
    // obj and base are Heliocentric Ecliptic Rectangular Coordinates
    // for the object and earth and obs is the observer
    public static AstroCoordinate radecr(ObservationPoint point, CubicCoordinate obj, CubicCoordinate base) {
      
    	// Equatorial co-ordinates
        var x = obj.x();
        var y = obj.y();
        var z = obj.z();
 
        // Obliquity of Ecliptic
        var obl = obliquityEcliptic(point);
        // Convert to Geocentric co-ordinates
        var dx = x.minus(base.x());
        var dy = y.minus(base.y()); 
        var dz = z.minus(base.z());
        
        var x1 = dx;
        var y1 = dy.times(obl.cos()).minus(dz.times(obl.sin()));
        var z1 = dy.times(obl.sin()).minus(dz.times(obl.cos()));
    
        // RA and dec
        var ra = RightAscention.from(Angle.atan2d(y1,  x1)); 
        var dec = Angle.atan2d(z1 , x1.hypot(y1)); 
        // Earth distance
        var r = x1.square().plus(y1.square()).plus(z1.square()).sqrt();
        		
        return new AstroCoordinate(ra, dec, r);
        
   
    }
    
    public static Angle obliquityEcliptic(ObservationPoint point) {
    
        return TimeExpantion.of(
        		Angle.degrees(23.439291111111),
        		Angle.degrees(-0.0130041666667),
        		Angle.degrees(-1.63888888889e-07),
        		Angle.degrees(5.959274797e-09)
        ).apply(point.factorT());
         
    }
}
