package astroephemeris.coordinates;

import astroephemeris.math.Number;

public record CubicCoordinate(Number x,Number y, Number z) implements SpacialCoordinate {
	
    public static final CubicCoordinate ORIGIN = new CubicCoordinate(Number.ZERO, Number.ZERO, Number.ZERO);
    
    


	

}
