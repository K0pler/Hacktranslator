package translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class VMCodeWriter {
	
	Path path;
	String fileName;
	
	String SPdec = "@SP" + "\n" + "M=M-1" + "\n";
	String SPinc = "@SP" + "\n" + "M=M+1" + "\n";
	int eqInc = 0;
	int ltInc = 0;
	int gtInc = 0;
	
	BufferedWriter out = null;
	
	public VMCodeWriter(Path path) {
		this.path = Paths.get(path.toString() + "/" + path.getFileName() + ".asm");
		this.fileName = path.getFileName() + ".";
		setFileName(fileName + "asm");
		
		try {
			out = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void writeArithmetic(String command) throws IOException {
		if (command.equals("add")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=M+D" + "\n"
					+ SPinc);
		}
		if (command.equals("sub")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=M-D" + "\n"
					+ SPinc);
		}
		if (command.equals("neg")) {
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "M=-D" + "\n"
					+ SPinc);
		}
		if (command.equals("and")) {
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=D&M" + "\n"
					+ SPinc);
		}
		if (command.equals("or")) {
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "M=D|M" + "\n"
					+ SPinc);
		}
		if (command.equals("not")) {
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "M=!D" + "\n"
					+ SPinc);
		}
		if (command.equals("eq")) {
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
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
		}
		if (command.equals("lt")) {		
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
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
		}
		if (command.equals("gt")) {		
			out.write("//" + command + "\n");
			out.write(SPdec + "\n"
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
		}
	}
	
	public void writePushPop(String command, String segment, int index) throws IOException {
		if (command.equals("push") && segment.equals("constant")) {
			out.write("//" + command + segment + index + "\n"
					 + "@" + index + "\n"
					 + "D=A" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
		} else if (command.equals("push") && segment.equals("temp")) {
			int temp = index + 5;
			out.write("//" + command + segment + index + "\n"
					 + "@" + temp + "\n"
					 + "D=M" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
		} else if (command.equals("push") && segment.equals("pointer")) {
			int pointer = index + 3;
			out.write("//" + command + segment + index + "\n"
					 + "@" + pointer + "\n"
					 + "D=M" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
		} else if (command.equals("push") && segment.equals("static")) {
			out.write("//" + command + segment + index + "\n"
					 + "@" + fileName + index + "\n"
					 + "D=M" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
		} else if (command.equals("push")) {
			out.write("//" + command + segment + index + "\n"
					 + "@" + index + "\n"
					 + "D=A" + "\n"
					 + "@" + getDest(segment) + "\n"
					 + "A=M+D" + "\n"
					 + "D=M" + "\n"
					 + "@SP" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n"
					 + SPinc);
		} else if (command.equals("pop") && segment.equals("temp")) {
			int temp = index + 5;
			out.write("//" + command + segment + index + "\n"
					 + SPdec
					 + "A=M" + "\n"
					 + "D=M" + "\n"
					 + "@" + temp + "\n"
					 + "M=D" + "\n");
		} else if (command.equals("pop") && segment.equals("pointer")) {
			int pointer = index + 3;
			out.write("//" + command + segment + index + "\n"
					 + SPdec
					 + "A=M" + "\n"
					 + "D=M" + "\n"
					 + "@" + pointer + "\n"
					 + "M=D" + "\n");
		} else if (command.equals("pop") && segment.equals("static")) {
			out.write("//" + command + segment + index + "\n"
					 + SPdec
					 + "A=M" + "\n"
					 + "D=M" + "\n"
					 + "@" + fileName + index + "\n"
					 + "M=D" + "\n");
		} else if (command.equals("pop")) {
			out.write("//" + command + segment + index + "\n"
					 + "@" + index + "\n"
					 + "D=A" + "\n"
					 + "@" + getDest(segment) + "\n"
					 + "D=M+D" + "\n"
					 + "@R13" + "\n"
					 + "M=D" + "\n"
					 + SPdec
					 + "A=M" + "\n"
					 + "D=M" + "\n"
					 + "@R13" + "\n"
					 + "A=M" + "\n"
					 + "M=D" + "\n");
		}
	}
	
	public void close() throws IOException {
		out.flush();
		out.close();
	}
	
	public String getDest(String s) {
		
		HashMap<String, String> destmap = new HashMap<String, String>();
		
		destmap.put("local", "LCL");
		destmap.put("this", "THIS");
		destmap.put("that", "THAT");
		destmap.put("argument", "ARG");
		
		return destmap.get(s);
	}

}
