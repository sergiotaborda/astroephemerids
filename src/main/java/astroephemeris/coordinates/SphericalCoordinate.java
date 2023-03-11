package astroephemeris.coordinates;

import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public record SphericalCoordinate(Number radius ,Angle theta, Angle phi) implements SpacialCoordinate {



}
