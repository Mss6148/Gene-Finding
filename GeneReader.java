import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class GeneReader {
	
	String inFile = new String();
	
	String line;
	
	ArrayList<String> attributes = new ArrayList<String>();
	ArrayList<String> sequences = new ArrayList<String>();

	public GeneReader(String inFile) {
		this.inFile = inFile;
		readFile(inFile);
	}
	
	public void readFile(String file){
		try {
			Scanner scanner = new Scanner(new File(file));
			
			while(scanner.hasNextLine()){
				line = scanner.nextLine().trim();
				
				if(line.charAt(0) == '>'){
					attributes.add(line.substring(2));
				}	//End if-statement
				else{
					sequences.add(line);
				}	//End else-statement
			}	// End while-loop
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	//End catch
	
	
		
	}	//End method readFile
	
	public ArrayList<String> getSequences(){
		return sequences;
	}	//End method getSequences
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}	//End method getAttributes
}

