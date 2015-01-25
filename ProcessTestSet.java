import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class ProcessTestSet {
	
	double avgORFScore;
	double avgNORFScore;
	double decidingScore;
	
	ArrayList<String> testSequences = new ArrayList<String>();
	ArrayList<String> testAttributes = new ArrayList<String>();
	ArrayList<String> sequenceBuild = new ArrayList<String>();
	
	ArrayList<String> finalORFAttributes = new ArrayList<String>();
	ArrayList<Double> finalORFScores = new ArrayList<Double>();
	
	ArrayList<String> finalNORFAttributes = new ArrayList<String>();
	ArrayList<Double> finalNORFScores = new ArrayList<Double>();
	
	Hashtable<String, Hashtable<String,Double>> frequenciesORF = new Hashtable<String, Hashtable<String,Double>>();
	Hashtable<String, Hashtable<String,Double>> frequenciesNORF = new Hashtable<String, Hashtable<String,Double>>();
	
	String complementarySequence;
	String reverseSequence;
	String tempSequence;

	// Constructor ProcessTestSet
	public ProcessTestSet(ArrayList<String> sequences, ArrayList<String> attributes,
		   Hashtable<String, Hashtable<String,Double>> ORFFrequencies, Hashtable<String,Hashtable<String,Double>> NORFFrequencies,
		   double avgORFScore, double avgNORFScore) {
		this.testSequences = sequences;
		this.testAttributes = attributes;
		this.frequenciesORF = ORFFrequencies;
		this.frequenciesNORF = NORFFrequencies;
		this.avgORFScore = avgORFScore;
		this.avgNORFScore = avgNORFScore;
		
		decidingScore = ((this.avgORFScore + this.avgNORFScore) /2);
		
	}	// End Constructor
	
	public void processSet(){
		
		for(int i = 0; i < testSequences.size(); i++){
			
			if(testSequences.get(i).endsWith("CAT") ||
			   testSequences.get(i).endsWith("CAC") ||
			   testSequences.get(i).endsWith("CAA"))  {
				
				complementarySequence = "";
				reverseSequence = "";
				tempSequence = testSequences.get(i);
				
				for(int j = tempSequence.length() -1 ; j >= 0; j--){
					reverseSequence += tempSequence.charAt(j);
				}	//End reverse sequence for-loop
				for(int k = 0; k < reverseSequence.length(); k++){
					if(reverseSequence.charAt(k) == 'A'){
						complementarySequence += 'T';
					}
					else if(reverseSequence.charAt(k) == 'T'){
						complementarySequence += 'A';
					}
					else if(reverseSequence.charAt(k) == 'C'){
						complementarySequence += 'G';
					}
					else if(reverseSequence.charAt(k) == 'G'){
						complementarySequence += 'C';
					}
					else{
						System.out.println("Error: Invalid nucleotide: " + testSequences.get(i).charAt(k) + " on sequence " + i);
					}	//End else
				}	//End for-loop (k)
				sequenceBuild.add(complementarySequence);
			}	// End if-statement
			else {
				sequenceBuild.add(testSequences.get(i));
			}	//End else
		}	// End for-loop
		
		testSequences.clear();
		testSequences = sequenceBuild;
		
	}	// End Method processSet
	
	public void scoreTestSequences(){
		
		Scanner scanner = new Scanner(System.in);
		
		String ORFOutputFile = new String();
		String NORFOutputFile = new String();
		
		String codon = new String();
		String nextCodon = new String();
		String ORFOutput = new String();
		String NORFOutput = new String();
		
		System.out.println("Enter test set ORF output file name: ");
		ORFOutputFile = scanner.next();
		
		System.out.println("Enter test set NORF output file name: ");
		NORFOutputFile = scanner.next();
		
		for(int i = 0; i < testSequences.size(); i++){
		
			int position = 0;
			int counter = 1;
			double sumORF = 0;
			double sumNORF = 0;
			
			while(position + 6 <= testSequences.get(i).length()){
				
				codon = testSequences.get(i).substring(position, position+3);
				nextCodon = testSequences.get(i).substring(position+3, position+6);
				
				if(frequenciesORF.containsKey(codon)){
					if(frequenciesORF.get(codon).containsKey(nextCodon)){	
						double tempORFFrequency = Math.log(frequenciesORF.get(codon).get(nextCodon)) / Math.log(2);
						sumORF += tempORFFrequency;
					}
				}
				
				if(frequenciesNORF.containsKey(codon)){
					if(frequenciesNORF.get(codon).containsKey(nextCodon)){
						double tempNORFFrequency = Math.log(frequenciesNORF.get(codon).get(nextCodon)) / Math.log(2);
						sumNORF += tempNORFFrequency;
					}
				}
			
				counter ++;
				position += 3;
			}	// End while-loop
			double scoreORF = (sumORF/testSequences.get(i).length());
			double scoreNORF = (sumNORF/testSequences.get(i).length());
			double score = (scoreORF - scoreNORF);
			
			if(score > decidingScore){
				finalORFAttributes.add(testAttributes.get(i));
				finalORFScores.add(score);
			}
			else if(score < decidingScore){
				finalNORFAttributes.add(testAttributes.get(i));
				finalNORFScores.add(score);
			}
			else{
				finalORFAttributes.add(testAttributes.get(i));
				finalORFScores.add(score);
			}
			
			
		}	// End for-loop
		
		ORFOutput = "Test set ORFS: ";
		ORFOutput += (System.getProperty("line.separator"));
		NORFOutput = "Test set NORFS: ";
		NORFOutput += (System.getProperty("line.separator"));
		
		for(int i = 0; i < finalORFScores.size(); i++){
			ORFOutput += finalORFAttributes.get(i);
			ORFOutput += "\t";
			ORFOutput += finalORFScores.get(i);
			ORFOutput += (System.getProperty("line.separator"));
		}
		
		for(int i = 0; i < finalNORFScores.size(); i++){
			NORFOutput += finalNORFAttributes.get(i);
			NORFOutput += "\t";
			NORFOutput += finalNORFScores.get(i);
			NORFOutput += (System.getProperty("line.separator"));
		}
		
		FilePrinter fp = new FilePrinter();
		fp.printIt(ORFOutput, ORFOutputFile);
		fp.printIt(NORFOutput, NORFOutputFile);
		
	}	// End Method scoreTestSet
}	// End ProcessTestSet Class
