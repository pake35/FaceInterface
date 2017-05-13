package params;

public class Condidate {
	private String confidence;
	private Face face;
	private Object object;

	public void Candidate(Face face,Object object,String confidence) {
		this.face = face;
		this.object = object;
		this.confidence = confidence;
	}

	public String getConfidence() {
		return confidence;
	}

	public Face getFace() {
		return face;
	}

	public Object getObject() {
		return object;
	}

}
