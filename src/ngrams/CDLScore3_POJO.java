package ngrams;

/*this class is a POJO class for CDL Score for Documents considering bigrams*/
public class CDLScore3_POJO {
	private String inputFileName;
	float CDLScore_Spam_Data_Set;
	float CDLScore_NonSpam_Data_Set;
	float CDLScore_Laplace_Smoothing_Spam_Data_Set;
	float CDLScore_Laplace_Smoothing_NonSpam_Data_Set;
	float CDLScore_Good_turing_Spam_Data_Set;
	float CDLScore_Good_turing_NonSpam_Data_Set;
	
		public CDLScore3_POJO(String inputFileName2, Float CDL_Spam,Float CDL_NonSpam,Float CDL_LS_Spam,Float CDL_LS_NonSpam,Float CDL_GT_Spam,Float CDL_GT_NonSpam) {
		// TODO Auto-generated constructor stub
		this.inputFileName=inputFileName2;
		this.CDLScore_Spam_Data_Set=CDL_Spam;
		this.CDLScore_NonSpam_Data_Set=CDL_NonSpam;
		this.CDLScore_Laplace_Smoothing_Spam_Data_Set=CDL_LS_Spam;
		this.CDLScore_Laplace_Smoothing_NonSpam_Data_Set=CDL_LS_NonSpam;
		this.CDLScore_Good_turing_Spam_Data_Set=CDL_GT_Spam;
		this.CDLScore_Good_turing_NonSpam_Data_Set=CDL_GT_NonSpam;
	
	}

		public String getInputFileName() {
			return inputFileName;
		}

		public void setInputFileName(String inputFileName) {
			this.inputFileName = inputFileName;
		}

		public float getCDLScore_Spam_Data_Set() {
			return CDLScore_Spam_Data_Set;
		}

		public void setCDLScore_Spam_Data_Set(float cDLScoreSpamDataSet) {
			CDLScore_Spam_Data_Set = cDLScoreSpamDataSet;
		}

		public float getCDLScore_NonSpam_Data_Set() {
			return CDLScore_NonSpam_Data_Set;
		}

		public void setCDLScore_NonSpam_Data_Set(float cDLScoreNonSpamDataSet) {
			CDLScore_NonSpam_Data_Set = cDLScoreNonSpamDataSet;
		}

		public float getCDLScore_Laplace_Smoothing_Spam_Data_Set() {
			return CDLScore_Laplace_Smoothing_Spam_Data_Set;
		}

		public void setCDLScore_Laplace_Smoothing_Spam_Data_Set(
				float cDLScoreLaplaceSmoothingSpamDataSet) {
			CDLScore_Laplace_Smoothing_Spam_Data_Set = cDLScoreLaplaceSmoothingSpamDataSet;
		}

		public float getCDLScore_Laplace_Smoothing_NonSpam_Data_Set() {
			return CDLScore_Laplace_Smoothing_NonSpam_Data_Set;
		}

		public void setCDLScore_Laplace_Smoothing_NonSpam_Data_Set(
				float cDLScoreLaplaceSmoothingNonSpamDataSet) {
			CDLScore_Laplace_Smoothing_NonSpam_Data_Set = cDLScoreLaplaceSmoothingNonSpamDataSet;
		}

		public float getCDLScore_Good_turing_Spam_Data_Set() {
			return CDLScore_Good_turing_Spam_Data_Set;
		}

		public void setCDLScore_Good_turing_Spam_Data_Set(
				float cDLScoreGoodTuringSpamDataSet) {
			CDLScore_Good_turing_Spam_Data_Set = cDLScoreGoodTuringSpamDataSet;
		}

		public float getCDLScore_Good_turing_NonSpam_Data_Set() {
			return CDLScore_Good_turing_NonSpam_Data_Set;
		}

		public void setCDLScore_Good_turing_NonSpam_Data_Set(
				float cDLScoreGoodTuringNonSpamDataSet) {
			CDLScore_Good_turing_NonSpam_Data_Set = cDLScoreGoodTuringNonSpamDataSet;
		}
	
	
	
	}
