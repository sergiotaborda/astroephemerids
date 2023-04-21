package astroephemeris.writting;

import java.io.PrintWriter;
import java.util.ArrayList;

import astroephemeris.astrology.Chart;

public class AsciiDocWriter implements Writer {
	
	
	public void write(Chart chart , PrintWriter writer) {
		
		writer.append("Horário Sideral : " + chart.observationPoint().localSideralTime().toString()).println();
		
		writer.println();
		
		writer.println("image::chart.png[]");
		
		writer.println();
		
		writer.append("== ").append("Planetas").println();
		
		writer.println();
		
		writer.println("|====");
		
		var points = new ArrayList<>(chart.points().stream().filter(p -> p.point() != null ).toList());
		
		points.sort((a,b) -> a.point().compareTo(b.point()));
	
		for (var p : points) {
			
			writer.append("|").append(p.point().symbol()).append(" | ").append( p.signPosition().toString()).append(" | ");
			
			if (p.isDomicile()) {
				writer.print( "Dom.");
			}
			writer.println();
		}
		
		writer.println("|====");
		
		
		writer.append("== ").append("Casas").println();
		
		writer.println();
		
		writer.println("|====");

		for (var p : chart.houses()) {
			
		
			
			writer.append("|").append(Integer.toString(p.number()))
				  .append("|").append( p.cuspid().toString())
				  .append("|").append( p.cuspid().sign().primaryRegent().symbol());
			
			var otherHouse = chart.houseof(p.cuspid().sign().primaryRegent());
			writer.append("|");
			if (otherHouse.isPresent()) {
				writer.append(Integer.toString(otherHouse.get()));
			} 
	
			writer.println();
		}
		
		writer.println("|====");
		
		writer.append("== ").append("Aspectos").println();
		
		writer.println();
		
		writer.println("|====");

		
		
		
		writer.append("|  ");
		
		
		for (int i = 1; i < points.size(); i++) {
			var a = points.get(i);
			
			writer.append("|").append(a.point().symbol());
			
		} 
		writer.println();
		
		for (int i = 0; i < points.size() - 2; i++) {
			var a = points.get(i);
			
			writer.append("|").append(a.point().symbol());
			
			for (int j = 1; j < points.size(); j++) {
				var b = points.get(j);
				
				var p = chart.aspectBetween(a.point(), b.point());
				if (p.isPresent()) {
					writer.append("|").append(p.get().symbol());
				} else {
					writer.append("|").append(" ");
				}
				
				
			
				
			} 
			writer.println();
		}
	
		writer.println("|====");
		
		
	}

}
