package translator;

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
		Path path = Paths.get(userHome + "/nand2tetris/projects/07/MemoryAccess/StaticTest");
		
		VMParser parser = null;
		VMCodeWriter cwriter = null;
		
		if (path.toFile().isDirectory() && Files.exists(path)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.{vm}")) {
					cwriter = new VMCodeWriter(path);
		    		for (Path file: stream) {
		    			parser = new VMParser(file);
		    			cwriter.setFileName(file.getFileName().toString());
		    			while (parser.hasMoreCommands() == true) {
		    				parser.advance();
		    				if (parser.commandType(parser.command) == "C_ARITHMETIC") {
		    					cwriter.writeArithmetic(parser.command);
		    				}
		    				if (parser.commandType(parser.command) == "C_PUSH" || parser.commandType(parser.command) == "C_POP") {
		    					cwriter.writePushPop(parser.command, parser.arg1, parser.arg2);
		    				}
		    				if (parser.commandType(parser.command) == "C_LABEL") {
		    					cwriter.writeLabel(parser.command);
		    				}
		    				if (parser.commandType(parser.command) == "C_GOTO") {
		    					cwriter.writeGoto(parser.command);
		    				}
		    				if (parser.commandType(parser.command) == "C_IF") {
		    					cwriter.writeIf(parser.command);
		    				}
		    				if (parser.commandType(parser.command) == "C_FUNCTION") {
		    					cwriter.writeFunction(parser.arg1, parser.arg2);
		    				}
		    				if (parser.commandType(parser.command) == "C_RETURN") {
		    					cwriter.writeReturn();
		    				}
		    				if (parser.commandType(parser.command) == "C_CALL") {
		    					cwriter.writeCall(parser.arg1, parser.arg2);
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
	    		cwriter.setFileName(path.getFileName().toString());
	    		while (parser.hasMoreCommands() == true) {
	    			parser.advance();
	    			if (parser.commandType(parser.command) == "C_ARITHMETIC") {
	    				cwriter.writeArithmetic(parser.command);
	    			}
	    			if (parser.commandType(parser.command) == "C_PUSH" || parser.commandType(parser.command) == "C_POP") {
	    				cwriter.writePushPop(parser.command, parser.arg1, parser.arg2);
	    			}
    				if (parser.commandType(parser.command) == "C_LABEL") {
    					cwriter.writeLabel(parser.command);
    				}
    				if (parser.commandType(parser.command) == "C_GOTO") {
    					cwriter.writeGoto(parser.command);
    				}
    				if (parser.commandType(parser.command) == "C_IF") {
    					cwriter.writeIf(parser.command);
    				}
    				if (parser.commandType(parser.command) == "C_FUNCTION") {
    					cwriter.writeFunction(parser.arg1, parser.arg2);
    				}
    				if (parser.commandType(parser.command) == "C_RETURN") {
    					cwriter.writeReturn();
    				}
    				if (parser.commandType(parser.command) == "C_CALL") {
    					cwriter.writeCall(parser.arg1, parser.arg2);
    				}
	    		}
	    		cwriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			while (parser.hasMoreCommands() == true) {
				parser.advance();
			}
		} else {
			System.out.println("Cannot find file or directory @ path: " + path.getFileName());
		}
	}

}
	