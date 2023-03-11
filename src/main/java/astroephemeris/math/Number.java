package astroephemeris.math;

public interface 
Number extends AdditiveGroup<Number> , Comparable<Number> {

	final static Number ZERO = new DoubleNumber(0.0); //new BigNumber(BigInteger.ZERO, BigInteger.ONE);
	final static Number ONE =  new DoubleNumber(1.0);// new BigNumber(BigInteger.ONE, BigInteger.ONE);
	
	public static Number zero() {
		return ZERO;
	}
	
	public static Number one() {
		return ONE;
	}
	
	public static Number from(double value) {
		//return BigNumber.parse(value);
		return new DoubleNumber(value);
	}
	
	public static Number from(int numerator) {
		//return new BigNumber(BigInteger.valueOf(numerator), BigInteger.ONE);
		return new DoubleNumber(numerator);
	}
	
	public static Number reason(int numerator, int denominator) {
		return new DoubleNumber(numerator * 1d / denominator);
		//return new BigNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}
	
	public Number plus(Number other);
	public Number minus(Number other);
	public Number negate();
	
	public Number times(Number other);
	public Number over(Number other);

	public Number invert();
	
	public double toDouble();

	public default Number square() {
		return this.times(this);
	}

	public Number sqrt();

	public int sign();

	public Number hypot(Number y);

	public Number floor();
	public Number ceil();



}
