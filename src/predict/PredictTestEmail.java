
package predict;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import frequency.WordDataNonSpamPOJO;
import frequency.WordDataSpamPOJO;
import frequency.WordDataSpam_NonSpamPOJO;
import frequency.HibernateUtil;





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
public class PredictTestEmail {
	
	static Session session = HibernateUtil.getSessionFactory().openSession();
   /**
    * Represents the data we need about a word:  the word and
    * the number of times it has been encountered.
    */
   private static class WordData {
      String word;
      int count;
      WordData(String w) {
         // Constructor for creating a WordData object when
         // we encounter a new word.
         word = w;
         count = 1;  // The initial value of count is 1.
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


   public static void main(String[] args) {
      System.out.println("\n\nThis program will ask you to select an input file");
      System.out.println("It will make a prediction if the file is spam or not");
      System.out.print("Press return to begin.");
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
             System.exit(0);  // Might be necessary, because of use of file dialogs.
   		}
     
      public static void predict(String inputFileName,String inputFilePath) throws IOException
      {
         
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
          TextIO.putln(words.size() + " unique words found in file:\n");

          int wordcount=0;
          for ( WordData data : wordsByFrequency )
          {
        	  wordcount=wordcount+data.count*1;
          }
          TextIO.putln(wordcount + " words found in file:\n");
       
          
           TextIO.putln("\n\nList of words by frequency of occurence:\n");
          
           float scoreCF=0;
           float scoreDF=0;
           for ( WordData data : wordsByFrequency )
          {
             TextIO.putln();
        	 TextIO.putln(data.word + " (" + data.count + ")");
             Transaction tx= session.beginTransaction();
             String SQL_QUERY="select word,CTF,DF from frequency.WordDataSpam_NonSpamPOJO where word=:wordinQuery";
             Query query=session.createQuery(SQL_QUERY);
             query.setParameter("wordinQuery", data.word);
             
             for(Iterator it=query.iterate();it.hasNext();){
            	 Object[] row = (Object[]) it.next();
            	 TextIO.putln(data.word+" has word count "+ data.count+ " and the word has CF = "+row[1]+"and DF is = "+row[2]);
            	 TextIO.putln("Before CF Score "+ scoreCF);
            	 TextIO.putln("count * CF=" + data.count*(Float)row[1]);
            	 scoreCF=scoreCF+data.count*(Float)row[1];
            	 TextIO.putln("After CF Score "+scoreCF);
            	 
            	 TextIO.putln("Before DF Score "+ scoreDF);
            	 TextIO.putln("count * DF=" + data.count*(Float)row[2]);
            	 scoreDF=scoreDF+data.count*(Float)row[2];
            	 TextIO.putln("After DF Score "+scoreDF);
             }
             
             }
           	  Transaction tx1= session.beginTransaction();
    		  Scores db=new Scores(inputFileName,scoreCF,scoreDF);
    		  System.out.println(inputFileName+scoreCF+scoreDF);
    		  session.save(db);
    		  tx1.commit();
    
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
