package frequency;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;



import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *  This program will read words from an input file, and count the
 *  number of occurrences of each word.  The word data is written to
 *  an output file twice, once with the words in alphabetical order
 *  and once with the words ordered by number of occurrences.  The
 *  user specifies the input file and the output file.
 *
 *  The program demonstrates several parts of Java's framework for
 *  generic programming:  TreeMap, List sorting, Comparators, etc.
 */
public class WordCount {

	 static TreeMap<String,WordData> finalReportTreeMap_SPAM = new TreeMap<String,WordData>();
	 static TreeMap<String,WordData> finalReportTreeMap_NONSPAM = new TreeMap<String,WordData>();
	 static TreeMap<String,WordData> finalReportTreeMap_SPAM_NONSPAM = new TreeMap<String,WordData>();
	 static int countfinalblock=0;
	 static int noofSpamdocuments=0;
	 static int noofnonSpamdocuments;
	 static int noofdocuments;
	 static Session session = HibernateUtil.getSessionFactory().openSession();
	 //static Session session=SessionFactory.openSession();
	 


   /**
    * Represents the data we need about a word:  the word and
    * the number of times it has been encountered.
    */
   private static class WordData { 
      String word;
      int count;
      float termFrequency;
      float documentFrequency;
      WordData(String w) {
         // Constructor for creating a WordData object when
         // we encounter a new word.
         word = w;
         count = 1;  // The initial value of count is 1.
         termFrequency=0;
         documentFrequency=1.0f;
      }
      
      WordData(String w,float tf) {
          // Constructor for creating a WordData object when
          // we encounter a new word.
          word = w;
          count = 1;  // The initial value of count is 1.
          termFrequency=tf;
          documentFrequency=1.0f;
       }
      
   } // end class WordData


   /**
    * A comparator for comparing objects of type WordData according to 
    * their counts.  This is used for sorting the list of words by frequency.
    */
   private static class CountCompare implements Comparator<WordData> {
      public int compare(WordData data1, WordData data2) {
         return data2.count - data1.count;
             // The return value is positive if data2.count > data1.count.
             // I.E., data1 comes after data2 in the ordering if there
             // were more occurrences of data2.word than of data1.word.
             // The words are sorted according to decreasing counts.
      }
   } // end class CountCompare
   
   /**
    * A comparator for comparing objects of type WordData according to 
    * their term frequency.  This is used for sorting the list of words by term frequency.
    */
   
   private static class TermFrequencyCompare implements Comparator<WordData> {
	      public int compare(WordData data1, WordData data2) {
	         if(data2.termFrequency - data1.termFrequency>0)
	        	 return 1;
	         else
	        	 return-1;
	             // The return value is positive if data2.count > data1.count.
	             // I.E., data1 comes after data2 in the ordering if there
	             // were more occurrences of data2.word than of data1.word.
	             // The words are sorted according to decreasing counts.
	      }
	   } // end class CountCompare
   

   private static class DocumentFrequencyCompare implements Comparator<WordData> {
	      public int compare(WordData data1, WordData data2) {
	         if(data2.documentFrequency - data1.documentFrequency>0)
	        	 return 1;
	         else
	        	 return-1;
	             // The return value is positive if data2.count > data1.count.
	             // I.E., data1 comes after data2 in the ordering if there
	             // were more occurrences of data2.word than of data1.word.
	             // The words are sorted according to decreasing counts.
	      }
	   } // end class CountCompare




   public static void main(String[] args) {
	   
	   

      System.out.println("\n\n   This program will ask you to select an input file");
      System.out.println("It make a list of all the words that occur in the file");
      System.out.println("along with the number of time that each word occurs.");
      System.out.println("This list will be output twice, first in alphabetical,");
      System.out.println("then in order of frequency of occurrence with the most");
      System.out.println("common word at the top and the least common at the end.");
      System.out.println("   After reading the input file, the program asks you to");
      System.out.println("select an output file.  If you select a file, the list of");
      System.out.println("words will be written to that file; if you cancel, the list");
      System.out.println("be written to standard output.  All words are converted to");
      System.out.println("lower case.\n\n");
      System.out.print("Press return to begin.SELECT SPAM EMAILS ...");
      TextIO.getln();  // Wait for user to press return.

      try {
    	   
    	  
         if (TextIO.readUserSelectedFilesonebyone()==false) {
            System.out.println("No input file selected.  Exiting.");
            System.exit(1);
         }
         
         //TextIO.readUserSelectedFilesonebyone();
      }
         	
         	
         	catch (Exception e) {
                System.out.println("Sorry, an error has occurred.");
                System.out.println("Error Message:  " + e.getMessage());
                e.printStackTrace();
             }
         	
         	System.out.print("Press return to begin.SELECT NON SPAM EMAILS ...");
         	//TextIO.getln();  // Wait for user to press return.

         	try {
                if (TextIO.readUserSelectedFilesonebyone()==false) {
                   System.out.println("No input file selected.  Exiting.");
                   System.exit(1);
                }
                
                //TextIO.readUserSelectedFilesonebyone();
             	}	
                	catch (Exception e) {
                       System.out.println("Sorry, an error has occurred.");
                       System.out.println("Error Message:  " + e.getMessage());
                       e.printStackTrace();
                    }
             System.exit(0);  // Might be necessary, because of use of file dialogs.
   }
      
   
   
      static void perform(String inputFileName,String inputFilePath) throws IOException
      {
    	  TreeMap<String,WordData> finalReportTreeMap= null;
    	  
    	  
    	  if(inputFilePath.contains("/Spam"))
    	  {
    		  System.out.println("in Spam");
    		  finalReportTreeMap=finalReportTreeMap_SPAM;
    		  noofSpamdocuments++;
    		  noofdocuments=noofSpamdocuments;
    	  }
    	  
    	  if (inputFilePath.contains("/NonSpam"))
    	  {
    		  System.out.println("in non spam");
    		  finalReportTreeMap=finalReportTreeMap_NONSPAM;
    		  noofnonSpamdocuments++;
    		  noofdocuments=noofnonSpamdocuments;
  
    	  }
    	  
    	  
    	  // Create a TreeMap to hold the data.  Read the file and record
          // data in the map about the words that are found in the file.
    	  Properties pro = new Properties();
    	  String filePath="/home/ankur/workspace/SpamFilter/src/frequency/listofcommonwords.properties";
          FileInputStream in = new FileInputStream(filePath);
          pro.load(in);
          //System.out.println("All key are given: " + pro.keySet());
          String ignorewords = pro.getProperty("COMMONWORDS");
          //String commonWords[]=p.split(",");
 
                  
          TreeMap<String,WordData> words = new TreeMap<String,WordData>();
          String word = readNextWord();
          while (word != null) {        	 
             word = word.toLowerCase();  // convert word to lower case
             
             if(word.endsWith("."))
             	word=word.substring(0, word.length()-1);
             
             
             
             System.out.print(" "+word);
             if(ignorewords.contains(word))
             {
             	//System.out.println("Ignoring word "+word);
             	word = readNextWord();
             	continue;
             }
             WordData data = words.get(word);
             if (data == null)
                words.put( word, new WordData(word) );
             else
                data.count++;
             word = readNextWord();
          }
          
          System.out.println("Number of different words found in file excluding commonly found words:  " 
                + words.size());
          System.out.println();
          if (words.size() == 0) {
             System.out.println("No words found in file.");
             System.out.println("Exiting without saving data.");
             System.exit(0);
          }
          
          // Copy the word data into an array list, and sort the list
          // into order of decreasing frequency.
          
          ArrayList<WordData> wordsByFrequency = new ArrayList<WordData>( words.values() );
          Collections.sort( wordsByFrequency, new CountCompare() );
          
     
          // Output the data from the map and from the list.
         
          String outputFileName=inputFilePath.split(inputFileName)[0]+"result/"+inputFileName+".output";
          System.out.println(outputFileName);

          TextIO.writeUserSelectedFile(outputFileName); // If user cancels, output automatically
                                          // goes to standard output.
          
          if(!inputFileName.contains("Final"))//if it is not final output
          {
		          TextIO.putln(words.size() + " unique words found in file:\n");
		          /*TextIO.putln("List of words in alphabetical order" 
		                + " (with counts in parentheses):\n");
		          for ( WordData data : words.values() )
		             TextIO.putln("   " + data.word + " (" + data.count + ")");*/
		          int wordcount=0;
		          for ( WordData data : wordsByFrequency )
		          {
		        	  wordcount=wordcount+data.count*1;
		          }
		          
		          TextIO.putln(wordcount + " words found in file:\n");
		          TextIO.putln("\n\nList of words by frequency of occurence and Term Frequency Normalized=(no of times a word appeares in a document)/(no of words in document):\n");
		          
		          
		          for ( WordData data : wordsByFrequency )
		          {
		        	  float termFrequencyNormalized=(float)data.count/(float)wordcount;
		        	  TextIO.putln("   " + data.word + " (" + data.count + ")"+" (" + termFrequencyNormalized + ")");
		        	    
		        	  // For Final Result file , term frequency
		        	  WordData dataobj = finalReportTreeMap.get(data.word);
		              if (dataobj == null)
		            	  finalReportTreeMap.put(data.word,new WordData(data.word,termFrequencyNormalized));
		              else
		              {
		                 dataobj.termFrequency=dataobj.termFrequency+termFrequencyNormalized;
		              	 dataobj.documentFrequency++;	
		              }
		              	 //Final Report Ends
		        	  
		          }
          }   
          
          else//output for final file only
          {
        	  TextIO.putln(words.size() + " unique words found in file:\n");
        	   	
        	  ArrayList<WordData> wordsByTermFrequencyNormalized = new ArrayList<WordData>(finalReportTreeMap.values());
              Collections.sort( wordsByTermFrequencyNormalized, new TermFrequencyCompare());
              
              
              TextIO.putln("------------------Ordering by Cumumlative Term Frequency---------------");
        	  for(WordData data : wordsByTermFrequencyNormalized)
        	  {
        		  TextIO.putln("   " + data.word + " Cumulative Term Frequency (" + data.termFrequency + ")"+" Appeared in documents "+ data.documentFrequency+"( "+ (noofdocuments-1) +" )");
        		  data.documentFrequency=(float)data.documentFrequency/(float)(noofdocuments-1); //Normalizing Document Frequency
        		  
        		  //saving to db
        		  if(inputFilePath.contains("/Spam")){
        		  Transaction tx= session.beginTransaction();
        		  WordDataSpamPOJO db=new WordDataSpamPOJO(data.word,data.termFrequency,data.documentFrequency);
        		  System.out.println(db.getWord()+db.getCTF()+db.getDF());
        		  session.save(db);
        		  tx.commit();
        		  }
        		  if(inputFilePath.contains("/NonSpam")){
        			  Transaction tx= session.beginTransaction();
        			  WordDataNonSpamPOJO db=new WordDataNonSpamPOJO(data.word,data.termFrequency,data.documentFrequency);
            		  System.out.println(db.getWord()+db.getCTF()+db.getDF());
            		  session.save(db);
            		  tx.commit();
            		  }
        	  }
        	  
    		  
    		  
        	  
        	  TextIO.putln("------------------Ordering by Document Frequency---------------");
        	  Collections.sort( wordsByTermFrequencyNormalized, new DocumentFrequencyCompare());
        	  for(WordData data : wordsByTermFrequencyNormalized)
        	  {
        		  TextIO.putln("   " + data.word + " Document Frequency (" + data.documentFrequency + ")"+" Term Frequency "+ data.termFrequency);
        	  }
        	 
        	  countfinalblock++;
        	  
        	  if(countfinalblock==2) // Both the TreeMaps have been created , output for spam and nonspam has been delivered.now it is time to create final output file
        	  {
        		  
        		  for(Map.Entry<String, WordData> entry:finalReportTreeMap_NONSPAM.entrySet()) {
        			  String key = entry.getKey();
        			  WordData value_NONSPAM = entry.getValue();
        			  WordData value;;
        			  
        			  if(finalReportTreeMap_SPAM.containsKey(key))
        			  {
        				  WordData value_SPAM=finalReportTreeMap_SPAM.get(key);
        				  System.out.println("Analyzing Key value"+key);
        				  System.out.println("Value in NON SPAM TREE "+value_NONSPAM.termFrequency);
        				  System.out.println("Value in SPAM Tree "+value_SPAM.termFrequency);
        				 
        				  value=new WordData(key);
        				  value.count=value_NONSPAM.count-value_SPAM.count;
        				  value.documentFrequency=value_NONSPAM.documentFrequency-value_SPAM.documentFrequency;
        				  value.termFrequency=(float)value_NONSPAM.termFrequency-(float)value_SPAM.termFrequency;
        				  //value.word=value_SPAM.word;
        				  
        				  finalReportTreeMap_SPAM_NONSPAM.put(key,value);
        				  
        				  //Removing from SPAM Tree
        				  finalReportTreeMap_SPAM.remove(key);
        			  }
        			  
        			  else
        			  {
        				  System.out.println("Analyzing Key value"+key);
        				  System.out.println("Value in NON SPAM TREE "+value_NONSPAM.termFrequency);
        				  System.out.println("Value in SPAM Tree "+0);
        				 
        				  value=new WordData(key);
        				  value=value_NONSPAM; // it is non spam        				  
        				  finalReportTreeMap_SPAM_NONSPAM.put(key,value);
        	
        			  }
		  
        			} //end of non spam for loop
        		  
        		  for(Map.Entry<String, WordData> entry:finalReportTreeMap_SPAM.entrySet()) {
        			  String key = entry.getKey();
        			  WordData value_SPAM = entry.getValue();
        			  WordData value=new WordData(key);
        			  
        			  System.out.println("Analyzing Key value"+key);
    				  System.out.println("Value in NON SPAM TREE "+0);
    				  System.out.println("Value in SPAM Tree "+value_SPAM.termFrequency);
    				  
    				  value.count=value_SPAM.count*-1;
    				  value.documentFrequency=value_SPAM.documentFrequency*-1;
    				  value.termFrequency=value_SPAM.termFrequency*-1;
    				  
    				  
    				  
    				 finalReportTreeMap_SPAM_NONSPAM.put(key,value);
        		  	}//end of for loop
        		  
        		  TextIO.writeUserSelectedFile("/home/ankur/Emails/FinalCompiled.txt.output");
  
        		  ArrayList<WordData> words_all = new ArrayList<WordData>(finalReportTreeMap_SPAM_NONSPAM.values());
                  Collections.sort( words_all, new TermFrequencyCompare());
                  
                  TextIO.putln("NON SPAM = +ve");
                  TextIO.putln("SPAM=-ve");
                  TextIO.putln("Anything too positive will mean it is non spam and anything too negative means it is spam");
                  
                  TextIO.putln("------------------Ordering by Cumumlative Term Frequency---------------");
            	  for(WordData data : words_all)
            	  {
            		   TextIO.putln("   " + data.word + " Cumulative Term Frequency (" + data.termFrequency + ")"+" Appeared in documents "+ data.documentFrequency);
            		  //Storing in DB
            		  Transaction tx= session.beginTransaction();
            		  WordDataSpam_NonSpamPOJO db=new WordDataSpam_NonSpamPOJO(data.word, data.termFrequency, data.documentFrequency);
            		  System.out.println(db.getWord()+db.getCTF()+db.getDF());
            		  session.save(db);
            		  tx.commit();
            	  }
            	  session.close();
            	  
            	  TextIO.putln("------------------Ordering by Document Frequency---------------");
            	  Collections.sort( words_all, new DocumentFrequencyCompare());
            	  for(WordData data : words_all)
            	  {
            		  TextIO.putln("   " + data.word + " Document Frequency (" + data.documentFrequency + ")"+" Term Frequency "+ data.termFrequency);
            	  }
        		  
        		  
        	  }//end of if loop countfinal==2
        	  
        	  
        		  
          }
          
          System.out.println("The total no of Spam documents is"+noofSpamdocuments);
          System.out.println("The total no of non Spam documents is"+noofnonSpamdocuments);
          System.out.println("\n\nDone.\n\n");

       }
       
      
      
      

   /**
    * Read the next word from TextIO, if there is one.  First, skip past
    * any non-letters in the input.  If an end-of-file is encountered before 
    * a word is found, return null.  Otherwise, read and return the word.
    * A word is defined as a sequence of letters.  Also, a word can include
    * an apostrophe if the apostrophe is surrounded by letters on each side.
    * @return the next word from TextIO, or null if an end-of-file is encountered
    */
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


} // end class WordCount

