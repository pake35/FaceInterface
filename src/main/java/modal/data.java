package modal;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_teacher")
public class data implements Serializable{
	private int id;
	private String url;
	@Column(name = "value")
	private Blob picture;

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void serUrl(String url){
		this.url = url;
	}
	
	@Column(name = "id")
	public int getId() {
		return id;
	}

	@Column(name = "value")
	public Blob getPicture() {
		return picture;
	}
}
