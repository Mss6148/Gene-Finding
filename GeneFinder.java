import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class GeneFinder {

		public static void main(String[] args){
			
			//----------------- Objects and variables -----------------------------------
			
			double avgORFScore = 0;
			double avgNORFScore = 0;
			
			boolean isNORF;
			
			String testfile = new String();
			String ORFfile = new String();
			String NORFfile = new String();
			String ORFfilename = new String();
			String NORFfilename = new String();
			
			Scanner scanner = new Scanner(System.in);
			
			ArrayList<String> attributesORF = new ArrayList<String>();
			ArrayList<String> sequencesORF = new ArrayList<String>();
			ArrayList<Double> ORFScores = new ArrayList<Double>();
			Hashtable<String, Hashtable<String,Double>> frequenciesORF = new Hashtable<String, Hashtable<String, Double>>();
			
			ArrayList<String> attributesNORF = new ArrayList<String>();
			ArrayList<String> sequencesNORF = new ArrayList<String>();
			ArrayList<Double> NORFScores = new ArrayList<Double>();
			Hashtable<String, Hashtable<String,Double>> frequenciesNORF = new Hashtable<String, Hashtable<String, Double>>();
			
			ArrayList<String> testSequences = new ArrayList<String>();
			ArrayList<String> testAttributes = new ArrayList<String>();
			//---------------------- Method --------------------------------------------
			
			// Prompt user for an ORF data file with file extension .txt then creates a new
			// GeneReader object, and calls the get methods to retrieve the processed data.
			System.out.println("Enter ORF data file (.txt) name: ");
			ORFfile = scanner.next();
			isNORF = false;
			
			GeneReader grORF = new GeneReader(ORFfile);
			attributesORF = grORF.getAttributes();
			sequencesORF = grORF.getSequences();
			
			ProcessSequences psORF = new ProcessSequences(attributesORF, sequencesORF, isNORF);
			frequenciesORF = psORF.getCodonFrequencies();
			sequencesORF = psORF.getSequences();
			
			// Prompt user for a NORF data file with file extension .txt then creates a new
			// GeneReader object, and calls the get methods to retrieve the processed data.
			System.out.println("Enter NORF data file (.txt) name");
			NORFfile = scanner.next();
			isNORF = true;
			
			GeneReader grNORF = new GeneReader(NORFfile);
			attributesNORF = grNORF.getAttributes();
			sequencesNORF = grNORF.getSequences();
			
			ProcessSequences psNORF = new ProcessSequences(attributesNORF, sequencesNORF, isNORF);
			frequenciesNORF = psNORF.getCodonFrequencies();
			sequencesNORF = psNORF.getSequences();		
			
			// Analyze sequences
			AnalyzeSequences as = new AnalyzeSequences(frequenciesORF, frequenciesNORF, sequencesORF, sequencesNORF);
			as.AnalyzeORF();
			as.AnalyzeNORF();
			as.calculateORFAverage();
			as.calculateNORFAverage();
			avgORFScore = as.getAvgORFScore();
			avgNORFScore = as.getAvgNORFScore();

			// Get processed sequence scores
			ORFScores = as.getORFScores();
			NORFScores = as.getNORFScores();
			
			// Prompt user for test set data with file extension .txt.
			System.out.println("Enter test set data file (.txt) name");
			testfile = scanner.next();
			
			GeneReader grTest = new GeneReader(testfile);
			testSequences = grTest.getSequences();
			testAttributes = grTest.getAttributes();
			
			ProcessTestSet pts = new ProcessTestSet(testSequences,testAttributes, frequenciesORF, frequenciesNORF, avgORFScore, avgNORFScore );
			pts.processSet();
			pts.scoreTestSequences();
			
			/*
			// Prompt user for output filenames and send files to be written
			FilePrinter fp = new FilePrinter();
			
			System.out.println("Enter an output filename for ORF scores: ");
			ORFfilename = scanner.next();
			
			System.out.println("Enter an output filename for NORF scores: ");
			NORFfilename = scanner.next();
			
			fp.printIt(ORFScores, ORFfilename);
			fp.printIt(NORFScores, NORFfilename);
			*/
			
		}	// End main method
}

