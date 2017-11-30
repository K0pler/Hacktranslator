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
			if (line.contains("/"))
				line = line.substring(0, line.indexOf("/"));
			line = line.trim();
			String[] lineList = line.split(" ");
			command = lineList[0].trim();
			if (lineList.length == 2) {
				 arg1 = lineList[1].trim();
			 }
			 if (lineList.length == 3) {
				 arg1 = lineList[1].trim();
				 arg2 = Integer.parseInt(lineList[2].trim());
			 }
		}
		
	}
	
	public String commandType(String command) {
		
		HashMap<String, String> commandmap = new HashMap<String, String>();
		
		commandmap.put("add", "C_ARITHMETIC");
		commandmap.put("sub", "C_ARITHMETIC");
		commandmap.put("neg", "C_ARITHMETIC");
		commandmap.put("eq", "C_ARITHMETIC");
		commandmap.put("gt", "C_ARITHMETIC");
		commandmap.put("lt", "C_ARITHMETIC");
		commandmap.put("and", "C_ARITHMETIC");
		commandmap.put("or", "C_ARITHMETIC");
		commandmap.put("not", "C_ARITHMETIC");
		commandmap.put("push", "C_PUSH");
		commandmap.put("pop", "C_POP");
		commandmap.put("label", "C_LABEL");
		commandmap.put("goto", "C_GOTO");
		commandmap.put("if-goto", "C_IF");
		commandmap.put("function", "C_FUNCTION");
		commandmap.put("return", "C_RETURN");
		commandmap.put("call", "C_CALL");
		
		return commandmap.get(command);
	}
	
	public String arg1() {
		return arg1;
	}
	
	public int arg2() {
		return arg2;
	}

}
