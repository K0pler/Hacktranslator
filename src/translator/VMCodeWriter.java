package translator;

import java.nio.file.Path;

public class VMCodeWriter {
	
	Path path;
	
	public VMCodeWriter(Path path) {
		this.path = path;
	}	
	
	public void setFileName(String fileName) {
		System.out.println(path);
	}
	
	public void writeArithmetic(String command) {
		
	}
	
	public void writePushPop(String command, String segment, int index) {
		
	}
	
	public void close() {
		
	}

}
