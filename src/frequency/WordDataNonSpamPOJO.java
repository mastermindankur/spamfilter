package frequency;

public class WordDataNonSpamPOJO {

	private String word;
	private float CTF;
	private float DF;

	
	public WordDataNonSpamPOJO(String word2, float termFrequency,
			float documentFrequency) {
		// TODO Auto-generated constructor stub
		this.setWord(word2);
		this.setCTF(termFrequency);
		this.setDF(documentFrequency);

	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public float getCTF() {
		return CTF;
	}
	public void setCTF(float cTF) {
		CTF = cTF;
	}
	public float getDF() {
		return DF;
	}
	public void setDF(float dF) {
		DF = dF;
	}

}
