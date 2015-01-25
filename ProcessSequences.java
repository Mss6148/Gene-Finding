import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;


public class ProcessSequences {
	
	ArrayList<String> attributes = new ArrayList<String>();
	ArrayList<String> sequences = new ArrayList<String>();
	ArrayList<String> sequenceBuild = new ArrayList<String>();
	
	Hashtable<String, Hashtable<String, Double>> codonFrequencies = new Hashtable<String, Hashtable<String, Double>>();
	Hashtable<String, Integer> codonTransitionCounts = new Hashtable<String, Integer>();
	
	String codon = new String();
	String nextCodon = new String();
	String complementarySequence;
	String reverseSequence;
	String tempSequence;
	boolean isNORF;
	
	int removed = 0;
	int numCodonTransitions = 0;
	int errorCounter = 0;

	public ProcessSequences(ArrayList<String> attributes, ArrayList<String> sequences, boolean isNORF){
		this.attributes = attributes;
		this.sequences = sequences;
		this.isNORF = isNORF;
		processCounterclockwise();
	}
	
	// --------------------- Method: processCounterclockwise --------------------
	public void processCounterclockwise(){
		
		// NORF file does not necessarily begin or end with a start codon
		// so I am relying on each sequence's attributes to determine if the
		// needs to be reverse-complementary.
		if(isNORF){
			
			for(int i = 0; i < sequences.size(); i++){
				if(attributes.get(i).contains("Counter")){
					reverseSequence = "";
					complementarySequence = "";
					tempSequence = sequences.get(i);
					
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
								System.out.println("Error: Invalid nucleotide: " + sequences.get(i).charAt(k) + " on sequence " + i);
							}	//End else
						}	//End for-loop (k)
						sequences.set(i, complementarySequence);
				}	//End if -statement
			}	// End for-loop
		} 	// if(isNORF)
		
		// ORF files begin with a start codon or end with a reverse-complementary start codon,
		// meaning we can create a more robust (non title dependent) solution for determining
		// if a sequence needs to be reverse-complementary.
		else{
			
			for(int i = 0 ; i< sequences.size(); i++){
			
				if(sequences.get(i).endsWith("CAT") ||
				   sequences.get(i).endsWith("CAC") ||
				   sequences.get(i).endsWith("CAA"))  {
					
					reverseSequence = "";
					complementarySequence = "";
					tempSequence = sequences.get(i);
					
					for(int j = tempSequence.length() - 1; j >= 0; j--){
						reverseSequence += tempSequence.charAt(j);
					}
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
							System.out.println("Error: Invalid nucleotide: " + sequences.get(i).charAt(k) + " on sequence " + (i+1));
						}	//End else
					}
					sequenceBuild.add(complementarySequence);
				}
				else if(sequences.get(i).startsWith("ATG") ||
						sequences.get(i).startsWith("GTG") ||
						sequences.get(i).startsWith("TTG"))  {
					
					sequenceBuild.add(sequences.get(i));
				}
				else{
					
					removed ++;
					
				}	// End else
			}	// End outer for-loop
			sequences.clear();
			sequences = sequenceBuild;
			
		}	//End else (!isNORF)
		
		System.out.println("Sequence Size: " + sequences.size());
		System.out.println("Attributes Size: " + attributes.size());
		System.out.println("Removed: " + removed);
			
		processTrainingSet(sequences, attributes);
		
	}	// End Method processCounterclockwise
	
	// -------------------- Method: processTrainingSet ------------------------------------
	public void processTrainingSet(ArrayList<String> sequences, ArrayList<String> attributes){
		
		codon = "";
		
		for(int i = 0; i < sequences.size(); i++){
			int position = 0;
			while(position + 6 <= sequences.get(i).length()){
				
				codon = sequences.get(i).substring(position, position + 3);
				nextCodon = sequences.get(i).substring(position+3, position+6);
				
				if(!codonFrequencies.containsKey(codon)){
					Hashtable<String,Double> temp = new Hashtable<String,Double>();
					temp.put(nextCodon, 1.0);
					codonFrequencies.put(codon, temp);
					
					codonTransitionCounts.put(codon, 1);
				}
				else if(codonFrequencies.containsKey(codon)){
					
					codonTransitionCounts.put(codon, codonTransitionCounts.get(codon)+ 1);
					
					if(codonFrequencies.get(codon).containsKey(nextCodon)){
						codonFrequencies.get(codon).put(nextCodon, codonFrequencies.get(codon).get(nextCodon)+1);
						
					}
					else if(!codonFrequencies.get(codon).containsKey(nextCodon)){
						codonFrequencies.get(codon).put(nextCodon, 1.0);
						
					}
				}
				
				numCodonTransitions ++;
				position += 3;
			}	//End while 
		}	//End for-loop
		
		calculateFrequencies();
	}	//End Method processTrainingSet
	
	
	// --------------------- Method: calculateFrequencies ------------------------------------------------
	public void calculateFrequencies(){
		
		for(String fromCodon : codonFrequencies.keySet()){
			double sumFrequency = 0;
			for(String toCodon : codonFrequencies.get(fromCodon).keySet()){
				int numTransitions = codonTransitionCounts.get(fromCodon);
				double tempFrequency = (codonFrequencies.get(fromCodon).get(toCodon)/numTransitions);
				sumFrequency += tempFrequency;
				
				codonFrequencies.get(fromCodon).put(toCodon, codonFrequencies.get(fromCodon).get(toCodon) / numTransitions);
			}
		}
		
	}	//End Method calculateFrequencies
	
	
	// ----------------------- Method: getCodonFrequencies --------------------------------------
	public Hashtable<String, Hashtable<String,Double>> getCodonFrequencies(){
		return codonFrequencies;
	}
	
	public ArrayList<String> getSequences(){
		return sequences;
	}
	
}	//End Class ProcessSequences
