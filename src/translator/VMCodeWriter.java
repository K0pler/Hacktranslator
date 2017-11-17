package translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VMCodeWriter {
	
	Path path;
	
	BufferedWriter out = null;
	
	public VMCodeWriter(Path path) {
		this.path = path;
		setFileName(path.getFileName() + ".asm");
		
		try {
			out = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	public void setFileName(String fileName) {
		this.path = Paths.get(path.toString() + "/" + fileName);
	}
	
	public void writeArithmetic(String command) throws IOException {
		if (command.equals("add")) {
			out.write("@SP" + "\n"
					+ "M=M-1" + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "@SP" + "\n"
					+ "M=M-1" + "\n"
					+ "A=M" + "\n"
					+ "M=M+D" + "\n"
					+ "@SP" + "\n"
					+ "M=M+1" + "\n");
		}
		if (command.equals("eq")) {
			out.write("@SP" + "\n"
					+ "M=M-1" + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "@SP" + "\n"
					+ "M=M-1" + "\n"
					+ "A=M" + "\n"
					+ "M=D-M" + "\n"
					+ "D=M" + "\n"
					+ "@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=D" + "\n"
					+ "@SP" + "\n"
					+ "M=M+1" + "\n");
		}
	}
	
	public void writePushPop(String command, String segment, int index) throws IOException {
		if (command.equals("push")) {
			out.write("@" + index + "\n"
					 + "D=A" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + "@SP" + "\n"
					 + "M=M+1" + "\n");
		}
		if (command.equals("pop")) {
			out.write(command + " " + segment + " " +index + "\n");
		}
	}
	
	public void close() throws IOException {
		out.flush();
		out.close();
	}

}
