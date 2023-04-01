package astroephemeris.writting;

import java.io.PrintWriter;

import astroephemeris.astrology.Chart;

public interface Writer {

	public void write(Chart chart, PrintWriter writer);
}
