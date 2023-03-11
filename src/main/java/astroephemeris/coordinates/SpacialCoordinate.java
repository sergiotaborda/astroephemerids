package astroephemeris.coordinates;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public interface SpacialCoordinate {


	public static CubicCoordinate origin() {
		return CubicCoordinate.ORIGIN;
	}
	
	public static CubicCoordinate xyz(Number x, Number y, Number z) {
		return new CubicCoordinate(x, y, z);
	}

	public static SphericalCoordinate spherical(Number radius, Angle theta, Angle phi) {
		return new SphericalCoordinate(radius, theta, phi);
	}

}
