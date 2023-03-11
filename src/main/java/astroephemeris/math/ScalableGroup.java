package astroephemeris.math;

public interface ScalableGroup<A extends ScalableGroup<A>> {

	public A times(Number factor);
	public A over(Number factor);
	
}
