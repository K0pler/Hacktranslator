package translator;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class VMParser {
	
	Path path;
	Scanner filescanner;
	String line;
	String command;
	String arg1;
	int arg2;
	
	public VMParser(Path path) throws FileNotFoundException {
		this.path = path;
	    filescanner = new Scanner(path.toFile());
	}
	
	public boolean hasMoreCommands() {
		return filescanner.hasNextLine();
	}
	
	public void advance() {
		line = filescanner.nextLine();
		if (line.startsWith("//") || line.isEmpty() && hasMoreCommands()) {
			advance();
		} else {
			line = line.trim();
			String[] lineList = line.split(" ");
			command = commandType(lineList[0]);
			 if (lineList.length == 3) {
				 arg1 = lineList[1];
				 arg2 = Integer.parseInt(lineList[2]);
			 }
		}
		
	}
	
	public String commandType(String command) {
		
		HashMap<String, String> commandmap = new HashMap<String, String>();
		
		commandmap.put("add", "C_ARITHMETIC");
		commandmap.put("push", "C_PUSH");
		commandmap.put("pop", "C_POP");
		commandmap.put("MD", "011");
		commandmap.put("A", "100");
		commandmap.put("AM", "101");
		commandmap.put("AD", "110");
		commandmap.put("AMD", "111");
		
		return commandmap.get(command);
	}
	
	public String arg1() {
		return arg1;
	}
	
	public int arg2() {
		return arg2;
	}

}
