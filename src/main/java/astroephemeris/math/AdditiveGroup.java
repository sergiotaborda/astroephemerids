package astroephemeris.math;

public interface AdditiveGroup<A extends AdditiveGroup<A>> {

	public A plus(A other);
	public A minus(A other);
	public A negate();
	
	public A abs();
	public boolean isPositive();
	public boolean isNegative();
	public boolean isZero();
	
	public A scale(Number factor); // Move to another interface
}
