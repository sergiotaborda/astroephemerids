package astroephemeris;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import astroephemeris.catalog.AstroKey;
import astroephemeris.catalog.Planet;
import astroephemeris.coordinates.AstroPosition;

public class Sky {

	
	private List<String> astros = new ArrayList<>();
	private Map<String, AstroPosition> astroPositions = new HashMap<>();
	
	public Sky addAstro(AstroKey key) {
		if (!key.value().equals(Planet.EARTH.value())){
			astros.add(key.value());
		}
	
		return this;
	}
	
	public <A extends AstroKey> Sky addAstros(A ... keys) {
		
		for (var k : keys) {
			addAstro(k);
		}
		return this;
	}
	
	public <A extends AstroKey> Sky addAstros(Collection<A> keys) {
		
		for (var k : keys) {
			addAstro(k);
		}
		return this;
	}
	
	public List<AstroKey> astros() {
		return astros.stream().map(it -> new AstroKey() {

			@Override
			public String value() {
				return it;
			}
			}
		).collect(Collectors.toList());
	}
	
	public void setAstroPosition(AstroKey key, AstroPosition astroPosition) {
		astroPositions.put(key.value(), astroPosition);
	}
	
	public Optional<AstroPosition> getAstroPosition(AstroKey key) {
		return Optional.ofNullable(astroPositions.get(key.value()));
	}
}
