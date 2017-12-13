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
	int retAdr = 0;
	
	BufferedWriter out = null;
	
	public VMCodeWriter(Path path) {
		this.path = Paths.get(path.toString() + "/" + path.getFileName() + ".asm");
		
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
	
	public void writeInit() throws IOException {
		out.write("//" + "Init" + "\n");
		out.write("@256" + "\n"
				+ "D=A" + "\n"
				+ "@SP" + "\n"
				+ "M=D" + "\n");
		writeCall("Sys.init", 0);
	}
	
	public void writeLabel(String label) throws IOException {
		out.write("//" + label + "\n");
		out.write("(" + label + ")\n");
	}
	
	public void writeGoto(String label) throws IOException {
		out.write("//GOTO " + label + "\n");
		out.write("@" + label + "\n"
				+ "0;JMP" + "\n");
	}
	
	public void writeIf(String label) throws IOException {
		out.write("//IF " + label + "\n");
		out.write("@SP" + "\n"
				+ "M=M-1" + "\n"
				+ "A=M" + "\n"
				+ "D=M" + "\n"
				+ "@" + label + "\n"
				+ "D;JNE" + "\n");
	}
	
	public void writeCall(String functionName, int numArgs) throws IOException {
		out.write("//CALL " + functionName + numArgs + "\n");
		// In the course of implementing the code of f
		// (the caller), we arrive to the command call g nArgs.
		// we assume that nArgs arguments have been pushed
		// onto the stack. What do we do next?
		// We generate a symbol, lets call it returnAddress;
		out.write("@retAdr" + retAdr + "\n");
		// Next, we effect the following logic:
		//push returnAddress // saves the return address
		out.write("D=A" + "\n"
				+ "@SP" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n"
				+ SPinc);
		//push LCL // saves the LCL of f
		out.write("@LCL" + "\n"
				+ "D=M" + "\n"
				+ "@SP" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n"
				+ SPinc);
		//push ARG // saves the ARG of f
		out.write("@ARG" + "\n"
				+ "D=M" + "\n"
				+ "@SP" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n"
				+ SPinc);
		//push THIS // saves the THIS of f
		out.write("@THIS" + "\n"
				+ "D=M" + "\n"
				+ "@SP" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n"
				+ SPinc);
		//push THAT // saves the THAT of f
		out.write("@THAT" + "\n"
				+ "D=M" + "\n"
				+ "@SP" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n"
				+ SPinc);
		//ARG = SP-nArgs-5 // repositions SP for g
		out.write("@SP" + "\n"
				+ "A=M" + "\n");
		for (int i = 0; i < numArgs; i++) {
			out.write("A=A-1" + "\n");
		}
		out.write("A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "D=A" + "\n"
				+ "@ARG" + "\n"
				+ "M=D" + "\n");
		//LCL = SP // repositions LCL for g
		out.write("@SP" + "\n"
				+ "D=M" + "\n"
				+ "@LCL" + "\n"
				+ "M=D" + "\n");
		//goto g // transfers control to g
		out.write("@" + functionName + "\n"
				+ "0;JMP" + "\n");
		//returnAddress: // the generated symbol
		out.write("(retAdr" + retAdr + ")" + "\n");
		retAdr++;
	}
	
	public void writeReturn() throws IOException {
		out.write("//" + "RETURN" + "\n");
		// In the course of implementing the code of g,
		// we arrive to the command return.
		// We assume that a return value has been pushed
		// onto the stack.
		// We effect the following logic:
		//frame = LCL // frame is a temp. variable
		out.write("@LCL" + "\n"
				+ "D=M" + "\n"
				+ "@R13" + "\n"
				+ "M=D" + "\n");
		//retAddr = *(frame-5) // retAddr is a temp. variable
		out.write("@R13" + "\n"
				+ "A=M-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "D=M" + "\n"
				+ "@R14" + "\n"
				+ "M=D" + "\n");
		//*ARG = pop // repositions the return value
		// for the caller
		out.write("@SP" + "\n"
				+ "AM=M-1" + "\n"
				+ "D=M" + "\n"
				+ "@ARG" + "\n"
				+ "A=M" + "\n"
				+ "M=D" + "\n");
		//SP=ARG+1 // restores the caller’s SP
		out.write("@ARG" + "\n"
				+ "D=M+1" + "\n"
				+ "@SP" + "\n"
				+ "M=D" + "\n");
		//THAT = *(frame-1) // restores the caller’s THAT
		out.write("@R13" + "\n"
				+ "A=M-1" + "\n"
				+ "D=M" + "\n"
				+ "@THAT" + "\n"
				+ "M=D" + "\n");
		//THIS = *(frame-2) // restores the callers THIS
		out.write("@R13" + "\n"
				+ "A=M-1" + "\n"
				+ "A=A-1" + "\n"
				+ "D=M" + "\n"
				+ "@THIS" + "\n"
				+ "M=D" + "\n");
		//ARG = *(frame-3) // restores the callers ARG
		out.write("@R13" + "\n"
				+ "A=M-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "D=M" + "\n"
				+ "@ARG" + "\n"
				+ "M=D" + "\n");
		//LCL = *(frame-4) // restores the callers LCL
		out.write("@R13" + "\n"
				+ "A=M-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "A=A-1" + "\n"
				+ "D=M" + "\n"
				+ "@LCL" + "\n"
				+ "M=D" + "\n");
		//goto retAddr // goto returnAddress
		out.write("@R14" + "\n"
				+ "A=M" + "\n"
				+ "0;JMP" + "\n");
	}
	
	public void writeFunction(String functionName, int numLocals) throws IOException {
		out.write("//FUNCTION " + functionName + numLocals + "\n");
		out.write("(" + functionName + ")" + "\n");
		for (int i = 0; i < numLocals; i++) {
			out.write("@SP" + "\n"
					+ "A=M" + "\n"
					+ "M=0" + "\n"
					+ SPinc);
		}
	}
	
	public void writeArithmetic(String command) throws IOException {
		if (command.equals("add")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "MD=M+D" + "\n"
					+ SPinc);
		}
		if (command.equals("sub")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "MD=M-D" + "\n"
					+ SPinc);
		}
		if (command.equals("neg")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "MD=-D" + "\n"
					+ SPinc);
		}
		if (command.equals("and")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "MD=D&M" + "\n"
					+ SPinc);
		}
		if (command.equals("or")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ SPdec
					+ "A=M" + "\n"
					+ "MD=D|M" + "\n"
					+ SPinc);
		}
		if (command.equals("not")) {
			out.write("//" + command + "\n");
			out.write(SPdec
					+ "A=M" + "\n"
					+ "D=M" + "\n"
					+ "MD=!D" + "\n"
					+ SPinc);
		}
		if (command.equals("eq")) {
			out.write("//" + command + "\n");
			out.write(SPdec
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
					+ "MD=0" + "\n"
					+ "(EQ_TRUE" + eqInc + ")" + "\n"
					+ SPinc);
			eqInc++;
		}
		if (command.equals("lt")) {		
			out.write("//" + command + "\n");
			out.write(SPdec
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
					+ "MD=0" + "\n"
					+ "(LT_TRUE" + ltInc + ")" + "\n"
					+ SPinc);
			ltInc++;
		}
		if (command.equals("gt")) {		
			out.write("//" + command + "\n");
			out.write(SPdec
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
					+ "MD=0" + "\n"
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
