package astroephemeris.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigNumber implements Number {

    static BigNumber parse(double number) {
		var b = BigDecimal.valueOf(number);
		return new BigNumber(
			b.unscaledValue(),
			BigInteger.TEN.pow(b.scale())
		);
	}

	private final BigInteger numerator;
	private final BigInteger denominator;

	BigNumber(BigInteger numerator, BigInteger denominator){
		var gcd = numerator.gcd(denominator);
		
		this.numerator = numerator.divide(gcd);
		this.denominator = denominator.divide(gcd);
	}
	
	@Override
	public Number abs() {
		return this.isNegative() ? negate() : this;
	}

	@Override
	public boolean isPositive() {
		 return numerator.signum() > 0;
	}

	@Override
	public boolean isNegative() {
		 return numerator.signum() < 0;
	}

	@Override
	public boolean isZero() {
		 return numerator.signum() == 0;
	}

	@Override
	public Number scale(Number factor) {
		return times(factor);
	}

	@Override
	public int compareTo(Number other) {
		return Double.compare(this.toDouble(), other.toDouble());
	}

	@Override
	public Number plus(Number other) {
		var o = asBigNumber(other);
		//a/b + c/d = (ad + bc) / db
		return new BigNumber(
			this.numerator.multiply(o.denominator).add(o.numerator.multiply(this.denominator)),
			this.denominator.multiply(o.denominator)
		);
	}

	@Override
	public Number minus(Number other) {
		var o = asBigNumber(other);
		//a/b - c/d = (ad - bc) / db
		return new BigNumber(
			this.numerator.multiply(o.denominator).subtract(o.numerator.multiply(this.denominator)),
			this.denominator.multiply(o.denominator)
		);
	}

	@Override
	public Number negate() {
		return new BigNumber(numerator.negate(), denominator);
	}

	@Override
	public Number times(Number other) {
		var o = asBigNumber(other);
		return new BigNumber(
				this.numerator.multiply(o.numerator),
				this.denominator.multiply(o.denominator)
		);
	}

	@Override
	public Number over(Number other) {
		var o = asBigNumber(other);
		return new BigNumber(
				this.numerator.multiply(o.denominator),
				this.denominator.multiply(o.numerator)
		);
	}

	private BigNumber asBigNumber(Number other) {
	  if (other instanceof BigNumber big) {
		  return big;
	  }
	  return BigNumber.parse(other.toDouble());
	}


	@Override
	public Number invert() {
		if (numerator.signum() < 0) {
			return new BigNumber(denominator.negate(), numerator.negate());
		}
		
		return new BigNumber(denominator, numerator);
	}

	@Override
	public double toDouble() {
		return new BigDecimal(numerator)
				.divide(new BigDecimal(denominator), MathContext.DECIMAL128)
				.doubleValue();
	}

	@Override
	public Number sqrt() {
		return new DoubleNumber(this.toDouble()).sqrt();
	}

	@Override
	public int sign() {
		return numerator.signum();
	}

	@Override
	public Number hypot(Number other) {
		return this.square().plus(other.square()).sqrt();
	}

	@Override
	public Number floor() {
		return new BigNumber(numerator.divideAndRemainder(denominator)[0], BigInteger.ONE);
	}

	@Override
	public Number ceil() {
		var divRem = numerator.divideAndRemainder(denominator);
		if (divRem[1].signum() == 0) {
			return new BigNumber(divRem[0], BigInteger.ONE);
		} else {
			return new BigNumber(divRem[0].add(BigInteger.ONE), BigInteger.ONE);
		}	
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BigNumber big) {
			return this.numerator.equals(big.numerator) 
				&& this.denominator.equals(big.denominator);
		} else if (other instanceof Number n) {
			return Double.compare(this.toDouble(), n.toDouble()) == 0;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(this.toDouble());
	}
	
	@Override
	public String toString() {
		return new BigDecimal(numerator)
				.divide(new BigDecimal(denominator),RoundingMode.FLOOR)
				.toString();
	}
}
