package org.myhstry.core.paf;
import java.io.IOException;

public class Parser {

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World, I am going to try and parse a PAF file...");
		
		String filename = "diacks.paf";
		
		LEndianRandomAccessFile file = new LEndianRandomAccessFile(filename, "r");
		
		PAFFile paf = new PAFFile(file);
	}

}
