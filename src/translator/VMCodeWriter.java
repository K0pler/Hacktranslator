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
	int Scounter = 0;
	int eqInc = 0;
	int ltInc = 0;
	int gtInc = 0;
	
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
			while (Scounter > 1) {
			out.write("//" + command + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=M+D" + "\n"
					+ SPinc);
			Scounter--;
			}
		}
		if (command.equals("sub")) {
			while (Scounter > 1) {
			out.write("//" + command + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=M-D" + "\n"
					+ SPinc);
			Scounter--;
			}
		}
		if (command.equals("eq")) {
			out.write("//" + command + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=D-M" + "\n"
					+ "M=-1" + "\n"
					+ "@EQ_TRUE" + eqInc + "\n"
					+ "D;JEQ" + "\n"
					+ "@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=0" + "\n"
					+ "(EQ_TRUE" + eqInc + ")" + "\n"
					+ SPinc);
			eqInc++;
			Scounter = 0;
		}
		if (command.equals("lt")) {		
			out.write("//" + command + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M-D" + "\n"
					+ "M=-1" + "\n"
					+ "@LT_TRUE" + ltInc + "\n"
					+ "D;JLT" + "\n"
					+ "@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=0" + "\n"
					+ "(LT_TRUE" + ltInc + ")" + "\n"
					+ SPinc);
			ltInc++;
			Scounter = 0;
		}
		if (command.equals("gt")) {		
			out.write("//" + command + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "D=M-D" + "\n"
					+ "M=-1" + "\n"
					+ "@GT_TRUE" + gtInc + "\n"
					+ "D;JGT" + "\n"
					+ "@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=0" + "\n"
					+ "(GT_TRUE" + gtInc + ")" + "\n"
					+ SPinc);
			gtInc++;
			Scounter = 0;
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
			Scounter++;
		}
		if (command.equals("pop")) {
			out.write(command + " " + segment + " " +index + "\n");
			Scounter--;
		}
	}
	
	public void close() throws IOException {
		out.flush();
		out.close();
	}

}
