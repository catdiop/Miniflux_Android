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
		//faire une requ�te HttpCall pour retrouver le flux
		HttpCall task=new HttpCall();
		task.execute(new String[] { restLink });
		try{
			fluxInJson=task.get();
			//mapping du json re�u en liste d'items
			ObjectMapper om=new ObjectMapper();
			om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonFactory f=new JsonFactory();
			JsonParser jp=f.createJsonParser(fluxInJson);
			jp.nextToken();
			while (jp.nextToken()==JsonToken.START_OBJECT) {
				Item i=om.readValue(fluxInJson, Item.class);
				this.items.add(i);
			}
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
	}

	public String getLink() {
		return this.link;
	}

	public List<Item> getItems() {
		return this.items;
	}
}
