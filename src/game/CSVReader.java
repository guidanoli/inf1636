package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader {

	public static ArrayList<ArrayList<String>> read(String filepath, boolean ignoreFirstRow) {
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        for(int i = 0 ; i < values.length; i++)
		        	if(values[i].equals(""))
		        		values[i] = null;
		        lines.add(new ArrayList<String>(Arrays.asList(values)));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		if( ignoreFirstRow && lines.size() > 0 ) lines.remove(0);
		return lines;
	}
	
}
