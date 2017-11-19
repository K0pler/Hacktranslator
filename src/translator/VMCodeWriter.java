package translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VMCodeWriter {
	
	Path path;
	
	String SPdec = "@SP" + "\n" + "M=M-1" + "\n";
	String SPinc = "@SP" + "\n" + "M=M+1" + "\n";
	
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
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=M+D" + "\n"
					+ SPinc);
		}
		if (command.equals("eq")) {
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=D-M" + "\n"
					+ "(IF_TRUE)" + "\n"
					+ "@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=-1" + "\n"
					+ "D=-1" + "\n"
					+ "@IF_TRUE" + "\n"
					+ "D;JEQ" + "\n"
					+ "D=D-1" + "\n"
					+ "@END" + "\n"
					+ "0;JMP" + "\n"
					+ "(END)" + "\n"
					+ SPinc);
		}
	}
	
	public void writePushPop(String command, String segment, int index) throws IOException {
		if (command.equals("push")) {
			out.write("//" + command + segment + index + "\n"
					 + "@" + index + "\n"
					 + "D=A" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
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
