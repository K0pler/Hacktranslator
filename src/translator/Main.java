package translator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import translator.VMParser;

public class Main {

	public static void main(String[] args) {
		
		Path userHome = Paths.get(System.getProperty("user.home"));
		Path path = Paths.get(userHome + "/nand2tetris/projects/07/StackArithmetic/StackTest");
		
		VMParser parser = null;
		VMCodeWriter cwriter = null;
		
		if (path.toFile().isDirectory() && Files.exists(path)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.{vm}")) {
				cwriter = new VMCodeWriter(path);
		    	for (Path file: stream) {
		    		parser = new VMParser(file);
		    		while (parser.hasMoreCommands() == true) {
		    			parser.advance();
		    			if (parser.commandType(parser.command) == "C_ARITHMETIC") {
		    				cwriter.writeArithmetic(parser.command);
		    			}
		    			if (parser.commandType(parser.command) == "C_PUSH" || parser.command == "C_POP") {
		    				cwriter.writePushPop(parser.command, parser.arg1, parser.arg2);
		    			}
		    			
		    		}
		    	}
		    	cwriter.close();
			} catch (IOException | DirectoryIteratorException x) {
		    	// IOException can never be thrown by the iteration.
		    	// In this snippet, it can only be thrown by newDirectoryStream.
		    	System.err.println(x);
			}
		} else if (path.toFile().getName().endsWith(".vm") && Files.exists(path) && Files.isRegularFile(path)) {
			try {
				parser = new VMParser(path);
				cwriter = new VMCodeWriter(path.getParent());
	    		cwriter.setFileName("johny");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			while (parser.hasMoreCommands() == true) {
				parser.advance();
			}
		} else {
			System.out.println(path.getFileName());
		}
	}

}
	