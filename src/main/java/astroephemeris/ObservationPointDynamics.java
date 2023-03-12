package astroephemeris;

class ObservationPointDynamics {

	
//	 public RiseSetTimes sunrise(ObservationPoint obs, double twilight, Astro earth) {
//	        Observatory obscopy = obs.copy();
//	        RiseSetTimes riseset = new RiseSetTimes("", "");
//	        ZonedDateTime obsDT = obscopy.getCurrentTime();
//	        obscopy.setCurrentTime(obsDT.withHour(12).withMinute(0).withSecond(0).withNano(0));
//	        double lst = DateTimeUtils.local_sidereal(obscopy);
//	        Coord3D earth_xyz = PlanetPositionCalculator.helios(earth, obscopy);
//	        Coord3D sun_xyz = new Coord3D(0.0, 0.0, 0.0);
//	        List<Double> radec = PlanetPositionCalculator.radecr(sun_xyz, earth_xyz, obscopy);
//	        double UTsun = 12.0 + radec.get(0) - lst;
//	        if (UTsun < 0.0) {
//	            UTsun += 24.0;
//	        }
//
//	        if (UTsun > 24.0) {
//	            UTsun -= 24.0;
//	        }
//
//	        double cosLHA = (MathUtils.sind(twilight) - MathUtils.sind(obs.getLatitude()) * MathUtils.sind(radec.get(1)))
//	                / (MathUtils.cosd(obs.getLatitude()) * MathUtils.cosd(radec.get(1)));
//	        // Check for midnight sun and midday night.
//	        if (cosLHA > 1.0) {
//	            riseset.setRise("----");
//	            riseset.setSet("----");
//	        } else if (cosLHA < -1.0) {
//	            riseset.setRise("++++");
//	            riseset.setSet("++++");
//	        } else {
//	            // rise/set times allowing for not today.
//	            double lha = MathUtils.acosd(cosLHA) / 15.0;
//	            if ((UTsun - lha) < 0.0) {
//	                riseset.setRise(hmstring(24.0 + UTsun - lha));
//	            } else {
//	                riseset.setRise(hmstring(UTsun - lha));
//	            }
//	            if ((UTsun + lha) > 24.0) {
//	                riseset.setSet(hmstring(UTsun + lha - 24.0));
//	            } else {
//	                riseset.setSet(hmstring(UTsun + lha));
//	            }
//	        }
//
//	        return riseset;
//	    }
	 
}
