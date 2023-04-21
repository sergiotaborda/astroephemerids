package astroephemeris.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import astroephemeris.astrology.Chart;
import astroephemeris.astrology.Sign;
import astroephemeris.math.Angle;
import astroephemeris.math.Number;

public class ChartDraw {

	 record Point (int x, int y) {}
	 
	public BufferedImage draw(Chart chart) {
	
		
		      int width = 1600, height = 900;
		      var center =	new Point(width/2, height / 2);
		      var padding = 10;
		      var innerRadius = 300;
		      var outerBorder = 100;
		      var outerRadius = height - 2* padding - innerRadius; 
	
		      
		      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		      Graphics2D g = image.createGraphics();
		      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		      Font font = new Font("TimesRoman", Font.BOLD, 20);
		      g.setFont(font);
		      
		   
		      FontMetrics fontMetrics = g.getFontMetrics();
		   
		      g.setPaint(Color.black);
		      
		   
		      drawCircle(g,center , outerRadius );
		      drawCircle(g,center , outerRadius + outerBorder );
		      
		      var adjustement = Angle.HALF_CIRCLE.minus(chart.getHouse(1).get().cuspid().angle());
		      
		      var R = (outerRadius)/2;
		      var V = (outerRadius + outerBorder)/2;
		      
		      for (var h : chart.houses()) {
		    	  if (h.number() == 1 || h.number() == 7) {
		    	      g.setPaint(Color.BLUE);
		    	  } else  if (h.number() == 10 || h.number() == 4) {
		    	      g.setPaint(Color.GREEN);
		    	  } else {
		    		  g.setPaint(Color.black);
		    	  }
		    	  var p =  new Point(
		    			  (int)(center.x + h.cuspid().angle().plus(adjustement).negate().cos().toDouble() * R ), 
		    			  (int)(center.y + h.cuspid().angle().plus(adjustement).negate().sin().toDouble() * R)
		    	  );
		    	  drawLine(g, center, p);
		    	  
		    	   g.drawString(Integer.toString(h.number()), p.x, p.y);
		      }
		      

			   
		      var d = Angle.CIRCLE.over(Number.from(12));
		      var half = d.over(Number.from(2));
		      var a = chart.getHouse(1).get().cuspid().sign().cuspid();
		      var signs = Sign.values();
		      g.setPaint(Color.ORANGE);
		      for (var s  = 0 ; s < 12; s ++) {
		    	 
		    	  var p =  new Point(
		    			  (int)(center.x + a.plus(adjustement).negate().cos().toDouble() * R ), 
		    			  (int)(center.y + a.plus(adjustement).negate().sin().toDouble() * R)
		    	  );
		    	  var p2 =  new Point(
		    			  (int)(center.x + a.plus(adjustement).negate().cos().toDouble() * V ), 
		    			  (int)(center.y + a.plus(adjustement).negate().sin().toDouble() * V)
		    	  );
		    	  
		    	  var p3 =  new Point(
		    			  (int)(center.x + a.minus(half).plus(adjustement).negate().cos().toDouble() * V ), 
		    			  (int)(center.y + a.minus(half).plus(adjustement).negate().sin().toDouble() * V)
		    	  );
		    	  
		    	  drawLine(g, p, p2);
		    	  a = a.plus(d);
		    	  g.drawString(signs[s].toString(), p3.x, p3.y);
		      }
		      
			  g.setPaint(Color.WHITE);
		      drawCircleFill(g,center , innerRadius );
	
		      g.setPaint(Color.black);
		      drawCircle(g,center , innerRadius );
		      
		      return image;
		      
		
	}
	
	private void drawLine(Graphics2D g, Point a, Point b) {
		   g.drawLine(a.x, a.y, b.x, b.y);
		
	}

	private void drawCircle(Graphics2D g, Point center, int radius) {
		
		var x = center.x - radius/2;
		var y = center.y - radius/2;
		
		g.drawOval(x, y, radius, radius);
	}
	
	private void drawCircleFill(Graphics2D g, Point center, int radius) {
		
		var x = center.x - radius/2;
		var y = center.y - radius/2;
		
		g.fillOval(x, y, radius, radius);
	}
}
