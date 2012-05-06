package ngrams;

public class Prediction_POJO {
	
	String email;
	double cdl_score_spam;
	double cdl_score_nonspam;
	String prediction;
	
	
	public Prediction_POJO(String email1,double cdl_score_spam1,double cdl_score_nonspam1,String prediction1) {
		this.email=email1;
		this.cdl_score_spam=cdl_score_spam1;
		this.cdl_score_nonspam=cdl_score_nonspam1;
		this.prediction=prediction1;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getCdl_score_spam() {
		return cdl_score_spam;
	}
	public void setCdl_score_spam(double cdlScoreSpam) {
		cdl_score_spam = cdlScoreSpam;
	}
	public double getCdl_score_nonspam() {
		return cdl_score_nonspam;
	}
	public void setCdl_score_nonspam(double cdlScoreNonspam) {
		cdl_score_nonspam = cdlScoreNonspam;
	}
	public String getPrediction() {
		return prediction;
	}
	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

}
