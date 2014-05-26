package enseirb.t2.miniflux;

import java.util.List;

public class Flux {

	private String link;       //lien vers le flux
	private List<Item> items;  //les differents articles du flux

	public Flux() {
		this.link=null;
		this.items=null;
	}

	public String getLink() {
		return this.link;
	}

	public List<Item> getItems() {
		return this.items;
	}
}
