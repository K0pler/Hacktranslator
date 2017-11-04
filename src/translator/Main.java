package translator;

import java.io.File;
import java.io.FileNotFoundException;
import translator.VMParser;

public class Main {

	public static void main(String[] args) {
		
		VMParser parser = null;
		try {
			parser = new VMParser(new File(("/home/hans/Programmering/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (parser.hasMoreCommands() == true) {
			parser.advance();
		}	
	}

}
	