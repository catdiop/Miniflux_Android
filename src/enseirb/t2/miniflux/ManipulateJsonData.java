package enseirb.t2.miniflux;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

public class ManipulateJsonData {
	public static List<Item> getItems(String jsonData) {
		List<Item> items=new LinkedList<Item>();
		
		try{
			ObjectMapper om=new ObjectMapper();
			om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			TypeFactory typeFactory = om.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(
	                                            List.class, Item.class);
	        items=om.readValue(jsonData, collectionType);
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		
		return items;
	}
}
