package astroephemeris.math;

public final class Angle implements AdditiveGroup<Angle> , ScalableGroup<Angle>, Comparable<Angle>{
	
	public static Angle HALF_CIRCLE = Angle.degrees(180.0);
	public static Angle CIRCLE = Angle.degrees(360.0);
	
	public static Angle degrees(double degrees) {
		return new Angle(Number.from(degrees));
	}
	
	public static Angle degrees(Number degrees) {
		return new Angle(degrees);
	}
	
	public static Angle radians(Number value) {
		return radians(value.toDouble());
	}
	
	public static Angle radians(double value) {
		return degrees((180.0 / Math.PI) * value);
	}
	

	public static Angle atan(Number value) {
	   var angle = Angle.radians(Math.atan(value.toDouble()));
	   if (angle.isNegative()) {
		   angle = angle.plus(Angle.degrees(180));
	   }
	        		
	   return angle;   
	}
	
	public static Angle atan2d(Number numerator, Number denominator) {
		
		if (denominator.isZero() && numerator.isZero()) {
			throw new IllegalArgumentException("Cannot calculate atan2d(0,0)");
		}
		
        var atan = Angle.radians(Math.atan(numerator.toDouble() / denominator.toDouble()));
        
        if (denominator.isNegative()) {
        	atan = atan.minus(Angle.degrees(180));
        }
        		
        return atan;
	}
	
//	public static Angle acos2d(Number numerator, Number denominator) {
//		
//		if (denominator.isZero() && numerator.isZero()) {
//			throw new IllegalArgumentException("Cannot calculate acos2d(0,0)");
//		}
//		
//		if (denominator.isZero() && !numerator.isZero()) {
//			return Angle.degrees(90);
//		} else if (denominator.isPositive()) {
//			return atan2d(numerator, denominator);
//		}
//        		
//        return atan2d(numerator, denominator).plus(Angle.degrees(90));
//	}
	

	public static Angle acos(Number value) {
		return Angle.radians(Math.acos(value.toDouble()));
	}
	
	private final Number valueInDegrees;

	Angle(Number value) {
		this.valueInDegrees = value;
	}
	
	
	public Angle plus(Angle other) {
		return new Angle(this.valueInDegrees.plus(other.valueInDegrees));
	}
	
	public Angle minus(Angle other) {
		return new Angle(this.valueInDegrees.minus(other.valueInDegrees));
	}
	
	@Override
	public Angle negate() {
		 return new Angle(this.valueInDegrees.negate());
	}

	public Number sin() {
		if (valueInDegrees.isZero() || valueInDegrees.toDouble() == 180) {
			return Number.ZERO;
		} else if (valueInDegrees.toDouble() == 90) {
			return Number.from(1);
		} else if (valueInDegrees.toDouble() == 270) {
			return Number.from(-1);
		}
		return Number.from(Math.sin((valueInDegrees.toDouble() * Math.PI)  / 180.0));
	}
	
	public Number cos() {
		if (valueInDegrees.toDouble() == 90 || valueInDegrees.toDouble() == 270) {
			return Number.ZERO;
		} else if (valueInDegrees.isZero()) {
			return Number.from(1);
		} else if (valueInDegrees.toDouble() == 180) {
			return Number.from(-1);
		}
		return Number.from(Math.cos((valueInDegrees.toDouble() * Math.PI)  / 180.0));
	}

	public Number tan() {
		return Number.from(Math.tan((valueInDegrees.toDouble() * Math.PI)  / 180.0));
	}
	
	Number CIRCLE_DEGREES = Number.from(360);

	public Angle simplify() {
	
	  var floor = this.valueInDegrees.over(CIRCLE_DEGREES).floor();
      return new Angle(this.valueInDegrees.minus(floor.times(CIRCLE_DEGREES)));
	}


	@Override
	public Angle scale(Number factor) {
		return times(factor);
	}

	@Override
	public Angle times(Number factor) {
		return new Angle(this.valueInDegrees.times(factor));
	}

	public Angle over(Number factor) {
		return new Angle(this.valueInDegrees.over(factor));
	}

	@Override
	public Angle abs() {
		return new Angle(valueInDegrees.abs());
	}

	@Override
	public boolean isPositive() {
		return valueInDegrees.isPositive();
	}

	@Override
	public boolean isNegative() {
		return valueInDegrees.isNegative();
	}

	@Override
	public boolean isZero() {
		return valueInDegrees.isZero();
	}

	public double valueInDegrees() {
		return valueInDegrees.toDouble();
	}

	public Number ratio(Angle other) {
		return valueInDegrees.over(other.valueInDegrees);
	}

	@Override
	public String toString() {
		return this.valueInDegrees.toString();
	}


	@Override
	public boolean equals(Object other) {
		return other instanceof Angle that
				&& that.valueInDegrees.equals(this.valueInDegrees);
	}

	
	@Override
	public int hashCode() {
		return valueInDegrees.hashCode();
	}

	

	@Override
	public int compareTo(Angle other) {

		return valueInDegrees.compareTo(other.valueInDegrees);
	}
	

	public static Angle of(int degrees, int minutes) {
		return degrees(degrees + minutes / 60d);
	}

	
	public Angle inverse() {
		return new Angle(this.valueInDegrees.invert());
	}



	






}
