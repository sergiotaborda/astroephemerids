package astroephemeris.catalog;

import java.util.List;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;
import astroephemeris.math.TimeExpantion;

public record AstroData(
		 PointOfInterest key,
	     List<Double> L, // meanLongitude
	     List<Double> a, // semiMajorAxisLength
	     List<Double> e, // eccentricity
	     List<Double> i, // inclination
	     List<Double> N, // ascendingNode
	     List<Double> P // perihelion longitude
		) {


    public TimeExpantion<Angle> ascendingNode() {
    	return TimeExpantion.of(N.stream().map(it -> Angle.degrees(it)).toList());
    }
    
    public TimeExpantion<Angle> inclination() {
    	return TimeExpantion.of(i.stream().map(it -> Angle.degrees(it)).toList());
    }

	public TimeExpantion<Angle> meanLongitude() {
		return TimeExpantion.of(L.stream().map(it -> Angle.degrees(it)).toList());
	}

	public TimeExpantion<Number> semiMajorAxisLength() {
    	return TimeExpantion.of(a.stream().map(it -> Number.from(it)).toList());
	}

	public TimeExpantion<Number> eccentricity() {
    	return TimeExpantion.of(e.stream().map(it -> Number.from(it)).toList());
	}

	public TimeExpantion<Angle> perihelion() {
		return TimeExpantion.of(P.stream().map(it -> Angle.degrees(it)).toList());
	}
	
}
