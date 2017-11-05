package translator;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class VMParser {
	
	Path path;
	Scanner filescanner;
	String line;
	
	public VMParser(Path path) throws FileNotFoundException {
		this.path = path;
	    filescanner = new Scanner(path.toFile());
	}
	
	public boolean hasMoreCommands() {
		return filescanner.hasNextLine();
	}
	
	public void advance() {
		line = filescanner.nextLine();
		System.out.println(line);
	}
	
	public String commandType(String command) {
		
		HashMap<String, String> commandmap = new HashMap<String, String>();
		
		commandmap.put("add", "C_ARITHMETIC");
		commandmap.put("M", "001");
		commandmap.put("D", "010");
		commandmap.put("MD", "011");
		commandmap.put("A", "100");
		commandmap.put("AM", "101");
		commandmap.put("AD", "110");
		commandmap.put("AMD", "111");
		
		return commandmap.get(command);
	}
	
	public String arg1() {
		return "arg1";
	}
	
	public String arg2() {
		return "arg2";
	}

}
