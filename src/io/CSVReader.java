package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@code CSVReader} class has the purpose of loading large amounts
 * of data from CSV files to internal data structures that would be to
 * unpleasant to be hard coded.
 * @author guidanoli
 *
 */
public class CSVReader {

	/**
	 * Reads CSV file and converts to an array of arrays of strings 
	 * @param filepath - absolute path to CSV file + extension
	 * @param ignoreFirstRow - in case first row is a header and should not
	 * be accounted for while parsing CSV data
	 * @return array of arrays of strings
	 */
	public static ArrayList<ArrayList<String>> read(String filepath, boolean ignoreFirstRow) {
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String UTF8 = new String(line.getBytes(),"UTF-8");
		        String[] values = UTF8.split(",");
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
