package astroephemeris.catalog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstroCatalog {

	
	private static final AstroCatalog ME = new AstroCatalog();
		
	
	public static AstroCatalog instance() {
		return ME;
	}
	
	private Map<String , Astro> astros = new HashMap<>();
	
	AstroCatalog(){
		add(new Astro(Planet.MERCURY , 
				 Arrays.asList(252.250906, 149474.0722491, 0.00030397, 0.000000018),
	             Arrays.asList(0.387098310, 0.0, 0.0, 0.0),
	             Arrays.asList(0.20563175, 0.000020406, -0.0000000284, -0.00000000017),
	             Arrays.asList(7.004986, 0.0018215, -0.00001809, 0.000000053),
	             Arrays.asList(48.330893, 1.1861890, 0.00017587, 0.000000211),
	             Arrays.asList(77.456119, 1.5564775, 0.00029589, 0.000000056)
			));
		add(new Astro(Planet.VENUS , 
				 Arrays.asList(181.979801, 58519.2130302, 0.00031060, 0.000000015),
	             Arrays.asList(0.723329820, 0.0, 0.0, 0.0),
	             Arrays.asList(0.00677188, -0.000047766, 0.0000000975, 0.00000000044),
	             Arrays.asList(3.394662, 0.0010037, -0.00000088, -0.000000007),
	             Arrays.asList(76.679920, 0.9011190, 0.00040665, -0.000000080),
	             Arrays.asList(131.563707, 1.4022188, -0.00107337, -0.000005315)
			));
		add(new Astro(Planet.EARTH , 
				List.of(100.466449, 36000.7698231, 0.00030368, 0.000000021),
		        List.of(1.000001018, 0.0, 0.0, 0.0),
		        List.of(0.01670862, -0.000042037, -0.0000001236, 0.00000000004),
		        List.of(0.0, 0.0, 0.0, 0.0),
		        List.of(0.0, 0.0, 0.0, 0.0),
		        List.of(102.937348, 1.7195269, 0.00045962, 0.000000499)	
			));
		add(new Astro(Planet.MARS , 
                Arrays.asList(355.433275, 19141.6964746, 0.00031097, 0.000000015),
                Arrays.asList(1.523679342, 0.0, 0.0, 0.0),
                Arrays.asList(0.09340062, 0.000090483, -0.0000000806, -0.00000000035),
                Arrays.asList(1.849726, -0.0006010, 0.00001276, -0.000000006),
                Arrays.asList(49.558093, 0.7720923, 0.00001605, 0.000002325),
                Arrays.asList(336.060234, 1.8410331, 0.00013515, 0.000000318)
			));
		add(new Astro(Planet.JUPITER , 
                Arrays.asList(34.351484, 3036.3027889, 0.00022374, 0.000000025),
                Arrays.asList(5.202603191, 0.0000001913, 0.0, 0.0),
                Arrays.asList(0.04849485, 0.000163244, -0.0000004719, -0.00000000197),
                Arrays.asList(1.303270, -0.0054966, 0.00000465, -0.000000004),
                Arrays.asList(100.464441, 1.0209550, 0.00040117, 0.000000569),
                Arrays.asList(14.331309, 1.6126668, 0.00103127, -0.000004569)
			));
		add(new Astro(Planet.SATURN , 
                Arrays.asList(50.077471, 1223.5110141, 0.00051952, -0.000000003),
                Arrays.asList(9.554909596, -0.0000021389, 0.0, 0.0),
                Arrays.asList(0.05550862, -0.000346818, -0.0000006456, 0.00000000338),
                Arrays.asList(2.488878, -0.0037363, -0.00001516, 0.000000089),
                Arrays.asList(113.665524, 0.8770979, -0.00012067, -0.000002380),
                Arrays.asList(93.056787, 1.9637694, 0.00083757, 0.000004899)
			));
		add(new Astro(Planet.URANUS , 
                Arrays.asList(314.055005, 429.8640561, 0.00030434, 0.000000026),
                Arrays.asList(19.218446062, -0.0000000372, 0.00000000098, 0.0),
                Arrays.asList(0.04629590, -0.000027337, 0.0000000790, 0.00000000025),
                Arrays.asList(0.773196, 0.0007744, 0.00003749, -0.000000092),
                Arrays.asList(74.005947, 0.5211258, 0.00133982, 0.000018516),
                Arrays.asList(173.005159, 1.4863784, 0.00021450, 0.000000433)
			));
		add(new Astro(Planet.NEPTUNE , 
                Arrays.asList(304.348665, 219.8833092, 0.00030926, 0.000000018),
                Arrays.asList(30.110386869, -0.0000001663, 0.00000000069, 0.0),
                Arrays.asList(0.00898809, 0.000006408, -0.0000000008, -0.00000000005),
                Arrays.asList(1.769952, -0.0093082, -0.00000708, 0.000000028),
                Arrays.asList(131.784057, 1.1022057, 0.00026006, -0.000000636),
                Arrays.asList(48.123691, 1.4262677, 0.00037918, -0.000000003)
		));
	}
	
	
	private void add(Astro astro) {
		astros.put(astro.key().value(), astro);
		
	}
	public Astro planet(Planet planet) {
		return astros.get(planet.value());
	}
}
