package org.myhstry.core.paf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class LEndianRandomAccessFile extends RandomAccessFile {

	public LEndianRandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public LEndianRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}
	
	/**
	 * @return next four bytes of the stream as a Little Endian integer
	 * @throws IOException 
	 */
	public int readIntLE() throws IOException {
		int b4 = read();
		int b3 = read();
		int b2 = read();
		int b1 = read();
		return b4 + (b3 << 8) + (b2 << 16) + (b1 << 24);
	}
	
	public short readShortLE() throws IOException {
		int b2 = read();
		int b1 = read();
		return (short)(b2 + (b1 << 8));
	}
	
	public long readLongLE() throws IOException {
		int b8 = read();
		int b7 = read();
		int b6 = read();
		int b5 = read();
		int b4 = read();
		int b3 = read();
		int b2 = read();
		int b1 = read();
			
		return (b8 + ((long)b7 << 8) + ((long)b6 << 16) + ((long)b5 << 24) + 
				((long)b4 << 32) + ((long)b3 << 40) + ((long)b2 << 48) +
				((long)b1 << 56));
	}
	
	public String readString(int length) throws IOException {
		byte[] buffer = new byte[length];
		readFully(buffer);
		
		int charCount = 0;
		for(int i = 0; i < buffer.length; ++i)
		{
			if(buffer[i] != 0)
				++charCount;
			else
				break;
		}
			
		try {
			return new String(buffer, 0, charCount, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
