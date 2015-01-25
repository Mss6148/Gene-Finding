import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FilePrinter {

	//Constructor
	public FilePrinter(){
		
	}	//End Constructor
	
	public void printIt(ArrayList<Double> scores, String filename){
		
		String printString = new String();
		printString = "";
		
		for(int i = 0; i < scores.size(); i++){
			printString += scores.get(i);
			printString += (System.getProperty("line.separator"));
		}
		
		try {
			File file = new File(filename);
			if (!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(printString);
			bw.close();
			System.out.println("File: " + filename + " successfully written");
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}	//End Method printIt(ArrayList<Double>,String)
	
	public void printIt(String output, String filename){
		
		try {
			File file = new File(filename);
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(output);
			bw.close();
			System.out.println("File: " + filename + " successfully written");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
}	//End FileWriter
