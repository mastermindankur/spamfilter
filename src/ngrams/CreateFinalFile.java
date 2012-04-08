package ngrams;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CreateFinalFile {
	
	String createFile(File selectedFiles[])
	{
		String outputFileName="";
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		FileWriter fstreamwrite;
		BufferedWriter out;
		
		for(int i=0;i<selectedFiles.length;i++)
		{
			
		try {
		      fstream = new FileInputStream(selectedFiles[i]);
		      in = new DataInputStream(fstream);
		      br = new BufferedReader(new InputStreamReader(in));
		      
		      outputFileName=selectedFiles[i].getAbsolutePath().split(selectedFiles[i].getName())[0]+"Final.txt";
		      //System.out.println("ouptput file name is "+outputFileName);
		      fstreamwrite = new FileWriter(outputFileName,true);
		      out = new BufferedWriter(fstreamwrite);
		      
		      
		      String str;
		      while ((str = br.readLine()) != null) {
		       // System.out.println(str);
		        out.write(str+"\n");
		      }
		      out.flush();
		      in.close();
		    } catch (Exception e) {
		      System.err.println(e);
		    }
		}//end of for loop
		
		return outputFileName;
	}

}
