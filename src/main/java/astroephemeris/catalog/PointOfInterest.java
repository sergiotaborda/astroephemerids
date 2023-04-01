package astroephemeris.catalog;

import java.util.Set;

public interface PointOfInterest extends Comparable<PointOfInterest> {


	public static final PointOfInterest SUN = new NamedPointOfInterest(1, "Sun", '\u2609');
	public static final PointOfInterest MOON = new NamedPointOfInterest(2, "Moon", '\u263E');

	
	// Planets
	public static final PointOfInterest MERCURY = new NamedPointOfInterest(3,"Mercury", '\u263F');
	public static final PointOfInterest VENUS = new NamedPointOfInterest(4,"Venus", '\u2640');
	public static final PointOfInterest EARTH = new NamedPointOfInterest(0,"Earth", '\u2641');
	public static final PointOfInterest MARS = new NamedPointOfInterest(5,"Mars", '\u2642');
	public static final PointOfInterest JUPITER = new NamedPointOfInterest(6,"Jupiter",'\u2643');
	public static final PointOfInterest SATURN = new NamedPointOfInterest(7,"Saturn", '\u2644');
	public static final PointOfInterest URANUS = new NamedPointOfInterest(8,"Uranus", '\u2645');
	public static final PointOfInterest NEPTUNE = new NamedPointOfInterest(9,"Neptune", '\u2646');
	public static final PointOfInterest PLUTO = new NamedPointOfInterest(10,"Pluto",'\u2647');
	
	public static Set<PointOfInterest> planets(){
		return Set.of(
				MERCURY,
				VENUS,
				MARS,
				JUPITER, 
				SATURN, 
				URANUS,
				NEPTUNE,
				PLUTO
		);
	}

	
	// Moon Points 
	public static final PointOfInterest MEAN_LILITH = new NamedPointOfInterest(11,"MeanLilith", '\u26B8');
	public static final PointOfInterest MOON_TRUE_NODE  =new NamedPointOfInterest(12,"MoonTrueNode", "\u260A");
	
	
	// Centaurs
	public static final PointOfInterest CERES = new NamedPointOfInterest(20,"Ceres", '\u2647'); // TODO put symbol correct
	public static final PointOfInterest CHIRON = new NamedPointOfInterest(21,"Chiron", '\u2647'); // TODO put symbol correct

	public static Set<PointOfInterest> centaurs(){
		return Set.of(
				CERES,
				CHIRON
		);
	}

	// Ascendent / MidHeaven
	
	public static final PointOfInterest ASC = new NamedPointOfInterest(40,"ASC", "ASC");
	public static final PointOfInterest MC = new NamedPointOfInterest(41, "MC", "MC");
	
	public String name();
	public String symbol();
	
}
