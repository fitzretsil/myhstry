package org.myhstry.core.paf;
import java.io.IOException;

class  PAF5NameRecord extends RecordObject {
	
	public long alphaLessOffset;
	public long alphaGreaterOffset;
	public long parentRecordOffset;
	byte nameType;
	String text;
	
	public PAF5NameRecord() {
		type = RecordObject.RecordType.NAME;
		type.size = 269; 
	}

	@Override
	public void read(LEndianRandomAccessFile file) throws IOException {
		alphaLessOffset = (long)file.readIntLE();
		alphaGreaterOffset = (long)file.readIntLE();
		parentRecordOffset = (long)file.readIntLE();
		nameType = file.readByte();
		text = file.readString(256);
	}
}