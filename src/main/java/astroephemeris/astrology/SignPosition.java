package astroephemeris.astrology;

import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;

public final class SignPosition {

	
	private final Angle angle;

	public static SignPosition from(RightAscention ra) {
		return new SignPosition(ra.toDegrees().simplify());
	}
	
	public static SignPosition degrees(double degrees) {
		return new SignPosition(Angle.degrees(degrees).simplify());
	}
	
	public static SignPosition from(Angle angle) {
		return new SignPosition(angle.simplify());
	}
	
	private SignPosition(Angle angle) {
		this.angle = angle;
	}
	
	public Sign sign() {
	  return Sign.from(angle);
	}
	
	public Angle angle() {
		  return angle;
		}

	@Override
	public String toString() {
		var sign = sign();
		var a = angle.minus(sign.cuspid());
		var d = (int)Math.floor(a.valueInDegrees());
		var min =(int) Math.floor((a.valueInDegrees() - d)*60);
		return sign.name().substring(0, 3).toUpperCase() + " " + d +"°" + min + "'";
	}


	@Override
	public boolean equals(Object other) {
		return other instanceof SignPosition that
				&& that.angle.equals(that.angle);
	}

	
	@Override
	public int hashCode() {
		return angle.hashCode();
	}


	public SignPosition oposite() {
		return SignPosition.from(angle.plus(Angle.degrees(180.0)).simplify());
	}
}
