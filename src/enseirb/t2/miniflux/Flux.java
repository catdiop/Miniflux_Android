package enseirb.t2.miniflux;

import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class Flux {

	private String link;       //lien vers le flux
	private List<Item> items;  //les differents articles du flux

	public Flux() {
		this.link=null;
		this.items=null;
	}

	public Flux(String link) {
		//construire l'url du flux
		String fluxInJson=null;
		String restLink=null;
		//faire une requête HttpCall pour retrouver le flux
		
	}

	public String getLink() {
		return this.link;
	}

	public List<Item> getItems() {
		return this.items;
	}
}
