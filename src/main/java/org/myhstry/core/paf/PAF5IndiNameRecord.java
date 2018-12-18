package org.myhstry.core.paf;
import java.io.IOException;

class  PAF5IndiNameRecord extends RecordObject {
	
	public long nextOffset;
	public long previousOffset;
	public long ownerRin;
	byte nameType;
	short textLength;
	String text;
	
	public PAF5IndiNameRecord() {
		type = RecordType.INDI_NAME;
		type.size = 271; //variable on read (based on textLength 
	}

	@Override
	public void read(LEndianRandomAccessFile file) throws IOException {
		nextOffset = file.readIntLE();
		previousOffset = file.readIntLE(); 
		ownerRin = file.readIntLE();
		nameType = file.readByte();
		textLength = file.readShortLE();
		text = file.readString(textLength+1);
	}
}