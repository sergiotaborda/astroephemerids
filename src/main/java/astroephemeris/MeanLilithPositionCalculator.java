package astroephemeris;

import astroephemeris.catalog.PointOfInterest;
import astroephemeris.coordinates.AstroCoordinate;
import astroephemeris.coordinates.AstroPosition;
import astroephemeris.coordinates.ObservationPoint;
import astroephemeris.coordinates.RightAscention;
import astroephemeris.math.Angle;
import astroephemeris.math.TimeExpantion;

public class MeanLilithPositionCalculator extends SingleAstroPositionCalculator {

	private static final TimeExpantion<Angle> PEREGEE_POSITION_TIME_EXPANATION = TimeExpantion.of(
			Angle.degrees(83.3532465d),
			Angle.degrees(4069.0137287), 
			Angle.degrees( -0.0103200d ),
			Angle.degrees( -80053 ).inverse() , 
			Angle.degrees(18999000 ).inverse()
			);
	
	public MeanLilithPositionCalculator() {
		super(PointOfInterest.MEAN_LILITH);
	}

	@Override
	protected AstroPosition calculatePosition(ObservationPoint point) {
		 var moonApogeePosition = PEREGEE_POSITION_TIME_EXPANATION.apply(point.factorT()).minus(Angle.HALF_CIRCLE).simplify();
		 
		   return new AstroPosition(
		        	point,
		        	new AstroCoordinate(RightAscention.from(moonApogeePosition), null, null),
		        	null
			    );
	}

}
