package params;

public class Face {
	private String id;
	private String imgId;
	private String height;
	private String width;
	private String center;
	private String tag;
	private String personId;
	private boolean isReal;
	private Eye eye;
	private Mouth mouth;
	

	// client通过getFace 或者getImg 构造real的Face
	public Face(String id, String imgId, String personId, String center,
			String width, String height, String tag, Eye eye, Mouth mouth) {
		this.id = id;
		this.imgId = imgId;
		this.personId = personId;
		this.center = center;
		this.height = height;
		this.width = width;
		this.tag = tag;
		this.eye = eye;
		this.mouth = mouth;
		this.isReal = true;
	}

	public String getId() {
		return id;
	}

	public String getImgId() {
		return imgId;
	}

	public Eye getEye() {
		return eye;
	}

	public Mouth getMouth() {
		return mouth;
	}



	public String getHeight() {
		return height;
	}

	public String getWidth() {
		return width;
	}

	public String getCenter() {
		return center;
	}

	public String getTag() {
		return tag;
	}

	public String getPersonId() {
		return personId;
	}

	public boolean isReal() {
		return isReal;
	}

}
