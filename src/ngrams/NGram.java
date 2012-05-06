package ngrams;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.JFileChooser;

import org.hibernate.Session;
import org.hibernate.Transaction;

import frequency.HibernateUtil;





public class NGram {
	
	HashMap <String,HashMap> outer=new HashMap<String,HashMap>();//outer is the hashmap on first word
	Session session = HibernateUtil.getSessionFactory().openSession();
	
	
	private static class WordData { 
	      String bigram;
	      int count;
	      float probability;
	      
	      WordData(String w) {
	         bigram = w;
	         count = 1;  // The initial value of count is 1.
	      }	
	   } // end class WordData

	
	
	//this class will calculate N Gram on Input File
	public  void calculateBiGramOnInputFile(String inputFileName,String inputFilePath) throws FileNotFoundException
	{
		 FileInputStream fstream = new FileInputStream(inputFilePath);  
         // Get the object of DataInputStream  
         DataInputStream in = new DataInputStream(fstream);  
         BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
		
         String word = readNextWord();
         String previousWord=null;
         String bigram=null;
         while(word!=null)
         {
        	 word = word.toLowerCase();
        	 if(word.endsWith("."))
              	word=word.substring(0, word.length()-1);
        	 
        	 if(word.endsWith(":"))
               	word=word.substring(0, word.length()-1);
              
        	 if(previousWord!=null)
        	 {
        		 bigram=previousWord+" "+word;
        		 System.out.println(bigram);
        		 
        		 if(!outer.containsKey(previousWord))
        		 {
        			 HashMap <String,WordData> inner=new HashMap<String,WordData>();//inner is the hashmap on 2 words
        			 if(!inner.containsKey(bigram))
        			 {
        			 //System.out.println("encountered it for the first time "+bigram+" creating inner and outer hashmap");
        			 inner.put(bigram,new WordData(bigram));//first time inserting
        			 outer.put(previousWord,inner);//first time inserting into outer hashmap
        			 } 
        		 }
        		 
        		 else
        		 {
        			 HashMap <String,WordData> inner=outer.get(previousWord);
        			 //System.out.println("outer contains the key "+previousWord); 
        			 
        			 if(!inner.containsKey(bigram))
        			 {
        			 //System.out.println("inserting bigram "+bigram+" updating outer hashmap");
        			 inner.put(bigram,new WordData(bigram));//first time inserting
        			 outer.put(previousWord,inner);
        			 }
        			 
        			 else
            		 {
            			WordData w= inner.get(bigram);
            			Integer count=w.count;
            			w.count=w.count+1;
            			//System.out.println("has already been encountered "+bigram+" with count "+count+" updating outer hashmap ,incrementing count to "+w.count);
            			inner.put(bigram,w);
            			outer.put(previousWord,inner);//updating outr hashmap
            		 }		 
        		 }
        	}
        	 previousWord=word;
        	 word=readNextWord();   	 
        	 
         }
         calculateBiGramProbability();
	}

	public void calculateBiGramProbability()
	{
		for (String key1 : outer.keySet()) {
		    System.out.println("-------------------Outer Key: " + key1 +"-----------------");
		    int totcount=0;
		    HashMap <String,WordData> map=outer.get(key1);
		    
		    for(String key2 : map.keySet())
		    {
		    	 totcount=totcount+map.get(key2).count;
		    }	 
		    
		    for(String key2 : map.keySet())
		    {
		    	WordData w=map.get(key2);
		    	w.probability=(float)w.count/(float)totcount;
		   	    System.out.println("Key: " + key2 + ", Count=" + w.count +" and P("+w.bigram+"/"+key1+")="+w.probability); 
		   	    //Hibernate Saving to DB Code
		   	    Transaction tx= session.beginTransaction();
		   	    BiGram_POJO db=new BiGram_POJO(w.bigram,w.count,w.probability);
		   	    //WordDataSpam_NonSpamPOJO db=new WordDataSpam_NonSpamPOJO(data.word, data.termFrequency, data.documentFrequency);
		   	    session.save(db);
		   	    tx.commit();
		   	    
		    }
		    System.out.println("--------------The total count for "+key1 +"is :"+totcount+"-------------");
		    
		    System.out.println();
		}
		session.close();
		
	}
	
	public  void calculate3GramOnInputFile(String inputFileName,String inputFilePath) throws FileNotFoundException
	{
		FileInputStream fstream = new FileInputStream(inputFilePath);  
        // Get the object of DataInputStream  
        DataInputStream in = new DataInputStream(fstream);  
        BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
		
     
        String firstword=readNextWord();
        String secondword=readNextWord();
        String previoustwoWords=firstword+" "+secondword;
        String word=readNextWord();
        String trigram=null;
        while(word!=null)
        {
       	 
        word = word.toLowerCase();
       	 if(word.endsWith("."))
             	word=word.substring(0, word.length()-1);
       	 
       	 if(word.endsWith(":"))
              	word=word.substring(0, word.length()-1);
             
       	 if(previoustwoWords!=null)
       	 {
       		trigram=previoustwoWords+" "+word;
       		 System.out.println("Trigram formed is "+trigram);
       		 System.out.println("Previous 2 words are "+previoustwoWords);
       		 
       		 if(!outer.containsKey(previoustwoWords))
       		 {
       			 HashMap <String,WordData> inner=new HashMap<String,WordData>();//inner is the hashmap on 2 words
       			 if(!inner.containsKey(trigram))
       			 {
       			 //System.out.println("encountered it for the first time "+trigram+" creating inner and outer hashmap");
       			 inner.put(trigram,new WordData(trigram));//first time inserting
       			 outer.put(previoustwoWords,inner);//first time inserting into outer hashmap
       			 } 
       		 }
       		 
       		 else
       		 {
       			 HashMap <String,WordData> inner=outer.get(previoustwoWords);
       			 //System.out.println("outer contains the key "+previoustwoWord); 
       			 
       			 if(!inner.containsKey(trigram))
       			 {
       			 //System.out.println("inserting bigram "+bigram+" updating outer hashmap");
       			 inner.put(trigram,new WordData(trigram));//first time inserting
       			 outer.put(previoustwoWords,inner);
       			 }
       			 
       			 else
           		 {
           			WordData w= inner.get(trigram);
           			Integer count=w.count;
           			w.count=w.count+1;
           			//System.out.println("has already been encountered "+bigram+" with count "+count+" updating outer hashmap ,incrementing count to "+w.count);
           			inner.put(trigram,w);
           			outer.put(previoustwoWords,inner);//updating outr hashmap
           		 }
       			 
       		 }
       			 
       			 
       	}
       	 
       	 firstword=secondword;
       	 secondword=word;
       	 previoustwoWords=firstword+" "+secondword;
       	
       	 word=readNextWord();   	 
       	 
        }
        calculateTriGramProbability();
	}
	 
	public void calculateTriGramProbability()
	{
		for (String key1 : outer.keySet()) {
		    System.out.println("-------------------Outer Key: " + key1 +"-----------------");
		    int totcount=0;
		    HashMap <String,WordData> map=outer.get(key1);
		    
		    for(String key2 : map.keySet())
		    {
		    	 totcount=totcount+map.get(key2).count;
		    }	 
		    
		    for(String key2 : map.keySet())
		    {
		    	WordData w=map.get(key2);
		    	w.probability=(float)w.count/(float)totcount;
		   	    System.out.println("Key: " + key2 + ", Count=" + w.count +" and P("+w.bigram+"/"+key1+")="+w.probability); 
		   	    //Hibernate Saving to DB Code
		   	    Transaction tx= session.beginTransaction();
		   	    TriGram_POJO db=new TriGram_POJO(w.bigram,w.count,w.probability);
		   	    //WordDataSpam_NonSpamPOJO db=new WordDataSpam_NonSpamPOJO(data.word, data.termFrequency, data.documentFrequency);
		   	    session.save(db);
		   	    tx.commit();
		   	    
		    }
		    System.out.println("--------------The total count for "+key1 +"is :"+totcount+"-------------");
		    
		    System.out.println();
		}
		session.close();
		
	}
	
	
	
	public static void main(String[] args) {
		  System.out.println("\n\n Plese select input files");
		  try {
	    	       if (TextIO.readUserSelectedFilesonebyone()==false) {
		            System.out.println("No input file selected.Exiting.");
		            System.exit(1);
	    	       }
		       }
		         	catch (Exception e) {
		                System.out.println("Sorry, an error has occurred.");
		                System.out.println("Error Message:  " + e.getMessage());
		                e.printStackTrace();
		             					}   
		  
		         	
	 				}
	 
	 private static String readNextWord() {
	      char ch = TextIO.peek(); // Look at next character in input.
	      while (ch != TextIO.EOF && ! Character.isLetterOrDigit(ch)) {
	         TextIO.getAnyChar();  // Read the character.
	         ch = TextIO.peek();   // Look at the next character.
	      }
	      if (ch == TextIO.EOF) // Encountered end-of-file
	         return null;
	      // At this point, we know that the next character, so read a word.
	      String word = "";  // This will be the word that is read.
	      while (true) {
	         word += TextIO.getAnyChar();  // Append the letter onto word.
	         ch = TextIO.peek();  // Look at next character.
	         if ( ch == '\'' ) {
	                // The next character is an apostrophe.  Read it, and
	                // if the following character is a letter, add both the
	                // apostrophe and the letter onto the word and continue
	                // reading the word.  If the character after the apostrophe
	                // is not a letter, the word is done, so break out of the loop.
	            TextIO.getAnyChar();   // Read the apostrophe.
	            ch = TextIO.peek();    // Look at char that follows apostrophe.
	            if (Character.isLetter(ch)) {
	               word += "\'" + TextIO.getAnyChar();
	               ch = TextIO.peek();  // Look at next char.
	            }
	            else
	               break;
	         }
	         if ( ! Character.isLetterOrDigit(ch)&&ch!='@'&&ch!='.'&&ch!='_'&&ch!='/'&&ch!=':') {
	                // If the next character is not a letter, the word is
	                // finished, so bread out of the loop.
	            break;
	         }
	         // If we haven't broken out of the loop, next char is a letter.
	      }
	      return word;  // Return the word that has been read.
	   }
	
	
	 public  void calculate3GramOnInputFile_laplaceSmoothing(String inputFileName,String inputFilePath) throws FileNotFoundException
		{
			FileInputStream fstream = new FileInputStream(inputFilePath);  
	        // Get the object of DataInputStream  
	        DataInputStream in = new DataInputStream(fstream);  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
			
	     
	        String firstword=readNextWord();
	        String secondword=readNextWord();
	        String previoustwoWords=firstword+" "+secondword;
	        String word=readNextWord();
	        String trigram=null;
	        while(word!=null)
	        {
	       	 
	        word = word.toLowerCase();
	       	 if(word.endsWith("."))
	             	word=word.substring(0, word.length()-1);
	       	 
	       	 if(word.endsWith(":"))
	              	word=word.substring(0, word.length()-1);
	             
	       	 if(previoustwoWords!=null)
	       	 {
	       		trigram=previoustwoWords+" "+word;
	       		 System.out.println("Trigram formed is "+trigram);
	       		 System.out.println("Previous 2 words are "+previoustwoWords);
	       		 
	       		 if(!outer.containsKey(previoustwoWords))
	       		 {
	       			 HashMap <String,WordData> inner=new HashMap<String,WordData>();//inner is the hashmap on 2 words
	       			 if(!inner.containsKey(trigram))
	       			 {
	       			 //System.out.println("encountered it for the first time "+trigram+" creating inner and outer hashmap");
	       			 inner.put(trigram,new WordData(trigram));//first time inserting
	       			 outer.put(previoustwoWords,inner);//first time inserting into outer hashmap
	       			 //V++;
	       			 } 
	       		 }
	       		 
	       		 else
	       		 {
	       			 HashMap <String,WordData> inner=outer.get(previoustwoWords);
	       			 //System.out.println("outer contains the key "+previoustwoWord); 
	       			 
	       			 if(!inner.containsKey(trigram))
	       			 {
	       			 //System.out.println("inserting bigram "+bigram+" updating outer hashmap");
	       			 inner.put(trigram,new WordData(trigram));//first time inserting
	       			 outer.put(previoustwoWords,inner);
	       			 //V++;
	       			 }
	       			 
	       			 else
	           		 {
	           			WordData w= inner.get(trigram);
	           			Integer count=w.count;
	           			w.count=w.count+1;
	           			//System.out.println("has already been encountered "+bigram+" with count "+count+" updating outer hashmap ,incrementing count to "+w.count);
	           			inner.put(trigram,w);
	           			outer.put(previoustwoWords,inner);//updating outr hashmap
	           		 } 
	       		 }		 
	       	}
	       	 
	       	 firstword=secondword;
	       	 secondword=word;
	       	 previoustwoWords=firstword+" "+secondword;
	       	
	       	 word=readNextWord();   	 
	       	 
	        }
	        //System.out.println("Vocabulary size is "+V);
	        calculateTriGramProbability_laplacesmoothing_spam();
		}
		 
		public void calculateTriGramProbability_laplacesmoothing_spam()
		{
			for (String key1 : outer.keySet()) {
			    System.out.println("-------------------Outer Key: " + key1 +"-----------------");
			    int totcount=0;
			    HashMap <String,WordData> map=outer.get(key1);
			    int V=0;//Vocabulary
			    
			    for(String key2 : map.keySet())
			    {
			    	 totcount=totcount+map.get(key2).count;
			    	 System.out.println("incrementing vocabulary by one");
			    	 V++;//unique records
			    	 System.out.println("V="+V);
			    }	
			    
			    V++; // In order to compensate unseen ngrams
			    System.out.println("V now ="+V);
			    
			    
			    for(String key2 : map.keySet())
			    {
			    	WordData w=map.get(key2);
			    	//laplace smoothing
			    	//
			    	w.probability=(float)(w.count+1)/(float)(totcount+V);
			    	int icount=w.count+1;
			    	System.out.println("count incremented by one"+icount+" totcount "+totcount+" Vocubulary "+V );
			    	
			   	    System.out.println("Key: " + key2 + ", Count=" + icount +" and P("+w.bigram+"/"+key1+")="+w.probability); 
			   	    //Hibernate Saving to DB Code
			   	    Transaction tx= session.beginTransaction();
			   	    
			   	    //comment for non spam
			   	    TriGram_POJO_LaplaceSmoothing_NonSpam db=new TriGram_POJO_LaplaceSmoothing_NonSpam(w.bigram,w.count,w.probability,w.count+1,V);
			   	    //###TriGram_POJO_LaplaceSmoothing_Spam db=new TriGram_POJO_LaplaceSmoothing_Spam(w.bigram,w.count,w.probability,w.count+1,V);
			   	   
			   	    session.save(db);
			   	    tx.commit();
			   	    
			    }
			    
			    Transaction tx= session.beginTransaction();
			    //# comment for nonspam
		   	    TriGram_POJO_LaplaceSmoothing_NonSpam db=new TriGram_POJO_LaplaceSmoothing_NonSpam(key1+" <unknown>",0,(float)1/(float)(totcount+V),1,V);
		   	    //####TriGram_POJO_LaplaceSmoothing_Spam db=new TriGram_POJO_LaplaceSmoothing_Spam(key1+" <unknown>",0,(float)1/(float)(totcount+V),1,V);
		   	    session.save(db);
		   	    tx.commit();
		   	    
			    System.out.println("--------------The total count for "+key1 +"is :"+totcount+"-------------");
			    
			    System.out.println();
			}
			session.close();
			
		}
	 
		
		 public  void calculate3GramOnInputFile_goodturing(String inputFileName,String inputFilePath) throws FileNotFoundException
			{
				FileInputStream fstream = new FileInputStream(inputFilePath);  
		        // Get the object of DataInputStream  
		        DataInputStream in = new DataInputStream(fstream);  
		        BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
				
		     
		        String firstword=readNextWord();
		        String secondword=readNextWord();
		        String previoustwoWords=firstword+" "+secondword;
		        String word=readNextWord();
		        String trigram=null;
		        while(word!=null)
		        {
		       	 
		        word = word.toLowerCase();
		       	 if(word.endsWith("."))
		             	word=word.substring(0, word.length()-1);
		       	 
		       	 if(word.endsWith(":"))
		              	word=word.substring(0, word.length()-1);
		             
		       	 if(previoustwoWords!=null)
		       	 {
		       		trigram=previoustwoWords+" "+word;
		       		 System.out.println("Trigram formed is "+trigram);
		       		 System.out.println("Previous 2 words are "+previoustwoWords);
		       		 
		       		 if(!outer.containsKey(previoustwoWords))
		       		 {
		       			 HashMap <String,WordData> inner=new HashMap<String,WordData>();//inner is the hashmap on 2 words
		       			 if(!inner.containsKey(trigram))
		       			 {
		       			 //System.out.println("encountered it for the first time "+trigram+" creating inner and outer hashmap");
		       			 inner.put(trigram,new WordData(trigram));//first time inserting
		       			 outer.put(previoustwoWords,inner);//first time inserting into outer hashmap
		       			 //V++;
		       			 } 
		       		 }
		       		 
		       		 else
		       		 {
		       			 HashMap <String,WordData> inner=outer.get(previoustwoWords);
		       			 //System.out.println("outer contains the key "+previoustwoWord); 
		       			 
		       			 if(!inner.containsKey(trigram))
		       			 {
		       			 //System.out.println("inserting bigram "+bigram+" updating outer hashmap");
		       			 inner.put(trigram,new WordData(trigram));//first time inserting
		       			 outer.put(previoustwoWords,inner);
		       			 //V++;
		       			 }
		       			 
		       			 else
		           		 {
		           			WordData w= inner.get(trigram);
		           			Integer count=w.count;
		           			w.count=w.count+1;
		           			//System.out.println("has already been encountered "+bigram+" with count "+count+" updating outer hashmap ,incrementing count to "+w.count);
		           			inner.put(trigram,w);
		           			outer.put(previoustwoWords,inner);//updating outr hashmap
		           		 }
		       			 
		       		 }
		       			 
		       			 
		       	}
		       	 
		       	 firstword=secondword;
		       	 secondword=word;
		       	 previoustwoWords=firstword+" "+secondword;
		       	
		       	 word=readNextWord();   	 
		       	 
		        }
		        //System.out.println("Vocabulary size is "+V);
		        calculateTriGramProbability_goodturing_nonspam();
			}
			 
			public void calculateTriGramProbability_goodturing_spam()
			{
				for (String key1 : outer.keySet()) {
				    System.out.println("-------------------Outer Key: " + key1 +"-----------------");
				    int totcount=0;
				    
				    HashMap <String,WordData> map=outer.get(key1);
				    
				    for(String key2 : map.keySet())
				    {
				    	 totcount=totcount+map.get(key2).count;
				    }
				    
				    float sumprobability=0;//added for good turing
				    
				    for(String key2 : map.keySet())
				    {
				    	float nc;
				    	float nc_plus_1;
				    	
				    	System.out.println("the count for "+key2+" is :"+map.get(key2).count); 
				    	System.out.println("c="+map.get(key2).count);
				    	nc_plus_1=(float)E(map.get(key2).count+1);
				    	nc=(float)E(map.get(key2).count);
				    	
				    	System.out.println("Nc+1="+nc_plus_1);
				    	System.out.println("Nc="+nc);
				    	
				    	float newcount;
				    	if(nc_plus_1!=0)
				    	{
				    		newcount=(float)(map.get(key2).count+1)*nc_plus_1/nc;
				    	}
				    	else //if higher order  frequency is 0 we do not do anything
				    	{
				    		newcount=(float)map.get(key2).count;
				    	}
				    	
				    	float probability=newcount/(float)totcount;
				    	
				    	sumprobability=sumprobability+probability;
				    	
				    	Transaction tx= session.beginTransaction();
				   	    //TriGram_POJO_GoodTuring_NonSpam db=new TriGram_POJO_GoodTuring_NonSpam(key2,map.get(key2).count,probability,newcount,(int)nc,(int)nc_plus_1);
				   	    TriGram_POJO_GoodTuring_Spam db=new TriGram_POJO_GoodTuring_Spam(key2,map.get(key2).count,probability,newcount,(int)nc,(int)nc_plus_1);
				   	    
				   	    
				   	    session.save(db);
				   	    tx.commit();
				    }	 
				    
				    float nc_plus_1=(float)E(1);
			    	float nc=(float)E(0);
			    	
				    Transaction tx= session.beginTransaction();
			   	    //TriGram_POJO_GoodTuring_NonSpam db=new TriGram_POJO_GoodTuring_NonSpam(key2,map.get(key2).count,probability,newcount,(int)nc,(int)nc_plus_1);
			   	    TriGram_POJO_GoodTuring_Spam db=new TriGram_POJO_GoodTuring_Spam(key1+" <unknown>",0,1-sumprobability,-1,-1,(int)nc_plus_1);    
			   	    session.save(db);
			   	    tx.commit();
			    
				    
				    
				    //System.out.println("--------------The total count for "+key1 +"is :"+totcount+"-------------");
				    
				    System.out.println();
				}
				session.close();
				
			}
			
			

			public void calculateTriGramProbability_goodturing_nonspam()
			{
				for (String key1 : outer.keySet()) {
				    System.out.println("-------------------Outer Key: " + key1 +"-----------------");
				    int totcount=0;
				    
				    HashMap <String,WordData> map=outer.get(key1);
				    
				    for(String key2 : map.keySet())
				    {
				    	 totcount=totcount+map.get(key2).count;
				    }
				    
				    float sumprobability=0;//added for good turing
				    
				    for(String key2 : map.keySet())
				    {
				    	float nc;
				    	float nc_plus_1;
				    	
				    	System.out.println("the count for "+key2+" is :"+map.get(key2).count); 
				    	System.out.println("c="+map.get(key2).count);
				    	nc_plus_1=(float)E(map.get(key2).count+1);
				    	nc=(float)E(map.get(key2).count);
				    	
				    	System.out.println("Nc+1="+nc_plus_1);
				    	System.out.println("Nc="+nc);
				    	
				    	float newcount;
				    	if(nc_plus_1!=0)
				    	{
				    	newcount=(float)(map.get(key2).count+1)*nc_plus_1/nc;
				    	}
				    	else
				    	{
				    		newcount=(float)map.get(key2).count;
				    	}
				    	
				    	float probability=newcount/(float)totcount;
				    	
				    	sumprobability=sumprobability+probability;
				    	
				    	Transaction tx= session.beginTransaction();
				  
				   	    TriGram_POJO_GoodTuring_NonSpam db=new TriGram_POJO_GoodTuring_NonSpam(key2,map.get(key2).count,probability,newcount,(int)nc,(int)nc_plus_1);
				   	    
				   	    
				   	    session.save(db);
				   	    tx.commit();
				    }	 
				    
				    float nc_plus_1=(float)E(1);
			    	float nc=(float)E(0);
			    	
				    Transaction tx= session.beginTransaction();
			   	  
				    TriGram_POJO_GoodTuring_NonSpam db=new TriGram_POJO_GoodTuring_NonSpam(key1+" <unknown>",0,1-sumprobability,-1,-1,(int)nc_plus_1);    
			   	    session.save(db);
			   	    tx.commit();
			    
			   	    System.out.println();
				}
				session.close();
				
			}

			

			private int E(int K) {
				int answer=0;
			for (String key1 : outer.keySet()) {
				
					//System.out.println("-------------------Outer Key: " + key1 +"-----------------");
				    HashMap <String,WordData> map=outer.get(key1);
				    
				    for(String key2 : map.keySet())
				    {
				    	if(map.get(key2).count==K)
				    	{
				    	 answer++;
				    	}
				    }	 
				}
				
				return answer;
			}
		
		 
			
		
	 
	
	//anikhil@adobe.com
	//india$12
	

}
