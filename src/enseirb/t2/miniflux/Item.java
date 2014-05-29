package enseirb.t2.miniflux;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Item {
	private String linkFlux;
	private String title;
	private String description;
	private String uri;
	private Date publishedDate;
	private boolean read;

	public Item() {
		this.linkFlux=null;
		this.title=null;
		this.description=null;
		this.uri=null;
		this.publishedDate=null;
		this.read=false;
	}

	public Item(String linkFlux, String title, String description, String uri, Date publishedDate) {
		this.linkFlux=linkFlux;
		this.title=title;
		this.description=description;
		this.uri=uri;
		this.publishedDate=publishedDate;
		this.read=false;
	}


	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getLinkFlux() {
		return linkFlux;
	}

	@JsonProperty("linkFlux")
	public void setLinkFlux(String linkFlux) {
		this.linkFlux = linkFlux;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getUri() {
		return this.uri;
	}

	public Date getPublishedDate() {
		return this.publishedDate;
	}

	public String toString() {
		String s=new String("");
		s=s.concat("link: "+this.linkFlux+"\n");
		s=s.concat("Description: "+this.description+"\n");
		s=s.concat("title: "+this.title+"\n");
		s=s.concat("uri: "+this.uri+"\n");
		return s;
	}
}
