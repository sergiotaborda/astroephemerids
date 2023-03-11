package astroephemeris.math;

import java.util.List;

public final class TimeExpantion<T extends AdditiveGroup<T>> {

	
	public static <X extends AdditiveGroup<X>> TimeExpantion<X> of(X ... terms){
		return new TimeExpantion<>(List.of(terms));
	}
	
	public static <X extends AdditiveGroup<X>> TimeExpantion<X> of(List<X> terms){
		return new TimeExpantion<>(terms);
	}

	private final List<T> terms;
	
	private TimeExpantion(List<T> terms) {
		this.terms = terms;
	}
	
	public T apply(Number time) {
		
	    var sum = terms.get(0);
        var t = time;
        for (int i = 1; i < terms.size() ; i++) {
        	sum = sum.plus(terms.get(i).scale(t));
        	t = t.times(time);
        }
       
		return sum;
	}

	public T get(int i) {
		return terms.get(i);
	}
}