package astroephemeris.math;

public final class DoubleNumber implements astroephemeris.math.Number {
	
	double value;


	DoubleNumber(double value) {
		this.value = value;
	}

	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof DoubleNumber that 
				&& Double.compare(this.value, that.value) == 0;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}
	
	public Number plus(Number other) {
		return new DoubleNumber(this.value + other.toDouble());
	}

	public Number minus(Number other) {
		return new DoubleNumber(this.value - other.toDouble());
	}

	public Number times(Number other) {
		return new DoubleNumber(this.value * other.toDouble());
	}

	public Number over(Number other) {
		return new DoubleNumber(this.value / other.toDouble());
	}

	public Number negate() {
		return new DoubleNumber(-this.value);
	}

	public Number invert() {
		return new DoubleNumber(-this.value);
	}

	public double toDouble() {
		return value;
	}

	@Override
	public Number abs() {
		return Number.from(Math.abs(value));
	}

	@Override
	public boolean isPositive() {
		return Double.compare(value, 0) > 0;
	}

	@Override
	public boolean isNegative() {
		return Double.compare(value, 0) < 0;
	}

	@Override
	public boolean isZero() {
		return Double.compare(value, 0) == 0;
	}

	@Override
	public Number scale(Number factor) {
		return Number.from(this.value * factor.toDouble());
	}

	@Override
	public Number sqrt() {
		return Number.from(Math.sqrt(this.value));
	}

	@Override
	public int sign() {
		return (int)Math.signum(value);
	}


	@Override
	public Number hypot(Number other) {
		return Number.from(Math.hypot(this.value, other.toDouble()));
	}


	@Override
	public Number floor() {
		return Number.from(Math.floor(value));
	}


	@Override
	public Number ceil() {
		return Number.from(Math.ceil(value));
	}


	@Override
	public int compareTo(Number o) {
		return Double.compare(this.value, o.toDouble()); 		
	}

}
