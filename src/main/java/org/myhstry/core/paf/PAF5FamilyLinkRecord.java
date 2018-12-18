package org.myhstry.core.paf;
import java.io.IOException;

public class PAF5FamilyLinkRecord extends RecordObject {

	public int mRin; //??marriage this individual is a child in??
	public int nextSiblngRin;
	public int nextParentsMRin;
	public int rin;
	PAF5OrdinanceEvent ldsSealingToParents = new PAF5OrdinanceEvent();
	char linkType;
	
	public PAF5FamilyLinkRecord() {
		type = RecordType.FAMILY_LINK;
		type.size = 46;
	}
	
	@Override
	public void read(LEndianRandomAccessFile file) throws IOException {
		mRin = file.readIntLE();
		nextSiblngRin = file.readIntLE();
		nextParentsMRin = file.readIntLE();
		rin = file.readIntLE();
		ldsSealingToParents.read(file);
		byte[] byteBuf = new byte[1];
		byteBuf[0] = (byte)file.read();
		linkType = new String(byteBuf, "ASCII").charAt(0);
		byte[] buffer = new byte[8];
		file.readFully(buffer);
	}

}
