package astroephemeris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import astroephemeris.astrology.ArabicPartsCalculator;
import astroephemeris.astrology.ChartCalculator;
import astroephemeris.astrology.ChartPoint;
import astroephemeris.astrology.PlacidusHouseSystem;
import astroephemeris.astrology.SignPosition;
import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.GeoCoordinates;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;
import astroephemeris.writting.AsciiDocWriter;
import astroephemeris.writting.AsciiDocWriter;

public class EphemeridTableGenerator {

	public static void main(String[] args) {
		
		ObservationPoint point = ObservationPoint.at(
				GeoCoordinates.at("39° 49' 26'' N" , "7°29'31'' W"),
				ZonedDateTime.of(1978, 6, 13, 4, 15, 0, 0, ZoneId.of("Europe/Lisbon"))
	    );
		
//		ObservationPoint point = ObservationPoint.at(
//				GeoCoordinates.at(astroephemeris.math.Number.from(-19.7474) ,astroephemeris.math.Number.from(-47.9392)),
//				ZonedDateTime.of(1977, 8, 15, 23, 45, 0, 0, ZoneId.of("America/Sao_Paulo"))
//	    );
//		
		var st = point.localSideralTime();
		
		System.out.println("At " + st.toString());
		
		
		var chart = new ChartCalculator()
				.addPoint(PointOfInterest.SUN)
				.addPoint(PointOfInterest.MOON)
				.addPoint(PointOfInterest.MEAN_LILITH)
				.addPoint(PointOfInterest.MOON_TRUE_NODE)
				.addPoints(PointOfInterest.planets())
				.addPoints(PointOfInterest.centaurs())
				.setHouseSystem(new PlacidusHouseSystem())
				.addChartPointCalculator(new ArabicPartsCalculator())
				.calculate(point);
		
		File file = new File("astro.adoc");
		
		try (var p =  new PrintWriter(file, Charset.forName("UTF-8"))){
			new AsciiDocWriter().write(chart, p);
			System.out.println(file.getAbsolutePath());
		} catch (FileNotFoundException e1) {
			 
		} catch (IOException e2) {
			
		}
		
		
		
		
		System.out.println("Points");
	 	
		for (var p : chart.points()) {
			System.out.print(p.point() + "\t\t\t|" + " " + p.signPosition().toString());
			if (p.isDomicile()) {
				System.out.print( "\tDom.");
			}
			System.out.println();
		}
		
		System.out.println("Houses");
		
		for (var h : chart.houses()) {
			System.out.print(h.number() + "\t\t|" + " " + h.cuspid().toString() + "\t| " + h.cuspid().sign().primaryRegent());
			
			 chart.houseof(h.cuspid().sign().primaryRegent()).ifPresent( otherHouse -> System.out.print(" \t| " + otherHouse));
			
			
			
			System.out.println();
			
		}
		
		
	
		System.out.println("Aspects");
		var points = chart.points();
		
		for (int i = 0; i < points.size(); i++) {
			var a = points.get(i);
		
			for (int j = i + 1; j < points.size(); j++) {
				var b = points.get(j);
	
				chart.aspectBetween(a.point(), b.point()).ifPresent(it ->{
					System.out.println(a.point() + "	" + it.name() + "		" + b.point());
				});
		
			}

		}
		
		System.out.println("Disposition");
		
		
	
		Map<PointOfInterest, List<DispositionChain>> disposersMapping = new HashMap<>();
		List< DispositionChain> disposers = new LinkedList<>();

		var planets = new ArrayList<>(PointOfInterest.planets());
		planets.add(PointOfInterest.MOON);
		planets.add(PointOfInterest.SUN);
		
		for (var p : planets) {
			chart.getPoint(p).ifPresent(position -> {
				if (!position.isDomicile()) {
					var list = disposersMapping.computeIfAbsent(p, (j) -> new ArrayList<>(2));
					
					var dispositor = position.signPosition().sign().primaryRegent();
					var d =  new DispositionChain(position, dispositor );
					list.add(d);
					disposers.add(d);
					
					position.signPosition().sign().secondaryRegent().ifPresent(s -> {
					   var sdispositor = position.signPosition().sign().primaryRegent();
					    var sd =  new DispositionChain(position, sdispositor );
						list.add(sd);
						disposers.add(sd);
					});
				}
			});
		}
		

		var deep = new LinkedList<DispositionChain>(disposers);
		while(!deep.isEmpty()) {
			var e = deep.pop();
			var d =  e.dispositor;
			var other = disposersMapping.get(d);
			if (other != null) {
				if (other.stream().anyMatch(it -> !it.point.isDomicile())) {
					 e.next = new ArrayList<>();
					 
					 for ( var f : other) {
						 if (!f.point.isDomicile()) {
							 deep.addLast(f);
						 }
					 }
					 
				}
				
			}
		}
		
		
		
		Node<PointOfInterest> root = new Node<>(null);
		Map<PointOfInterest, Node<PointOfInterest>> mapping = new HashMap<>();

	    deep = new LinkedList<DispositionChain>(disposers);
	 	while(!deep.isEmpty()) {
			var e = deep.pop();
			var n = mapping.computeIfAbsent(e.point.point(), (j) -> new Node<>(j));
			var d = mapping.computeIfAbsent(e.dispositor, (j) -> new Node<>(j));
			if (e.next == null) {
				
				root.add(d);
				
			} else {
				 for ( var f : e.next) {
					 deep.addLast(f);
				 }
				
			}
			d.add(n);
		}
		

		
		for (var c : root.children) {
			print(c, 0);
		}
	


	}

	private static void print(Node<PointOfInterest> c, int tab) {
		System.out.println("  ".repeat(tab) +  "|" + c.value);
		for (var cc : c.children) {
			print(cc, tab+ 1);
		}
	
	}
	
	
}

class DispositionChain {
	
	public DispositionChain(ChartPoint p, PointOfInterest dispositor) {
		this.point = p;
		this.dispositor = dispositor;
	}
	
	ChartPoint point;
	SignPosition signPosition;
	PointOfInterest dispositor;
	List<DispositionChain> next;
	
}


class Node<T> {
	public final T value;
	public Node<T> parent;
	public final List<Node<T>> children = new ArrayList<>();
	
	public Node(T value) {
		this.value = value;
	}
	
	public void add(Node<T> other) {
		if (!children.stream().anyMatch(it -> it.value.equals(other.value))) {
			other.parent = this;
			this.children.add(other);
		}
	
	}
}
class TestSkyCalculator implements SkyCalculator {

	@Override
	public Sky calculate(Sky sky, ObservationPoint point) {
		
		sky.setPointPosition(PointOfInterest.SUN,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(81, 46)), null, null), null));
		sky.setPointPosition(PointOfInterest.MOON, new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(12, 29)), null, null), null));
		sky.setPointPosition(PointOfInterest.MERCURY,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(20, 4)), null, null), null));
		sky.setPointPosition(PointOfInterest.VENUS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(26,18)), null, null), null));
		sky.setPointPosition(PointOfInterest.MARS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(29, 28)), null, null), null));
		sky.setPointPosition(PointOfInterest.JUPITER,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setPointPosition(PointOfInterest.SATURN,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setPointPosition(PointOfInterest.NEPTUNE,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setPointPosition(PointOfInterest.URANUS,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		sky.setPointPosition(PointOfInterest.PLUTO,  new AstroPosition(point, new AstroCoordinate(RightAscention.from(Angle.of(91, 43)), null, null), null));
		
		return sky;
	}
	
}
