package org.myhstry.core.paf;
import java.io.IOException;

public class PAF5MarriageRecord extends RecordObject {
	public long modifiedTime;
	public int husbandRin;
	public int wifeRin;
	public int firstChildLinkRin;
	public int husbandNextMRin;
	public int wifeNextMRin;
	public int firstUserEventRin;
	public int noteRin;
	public int citationRin;
	byte[] uId = new byte[16];
	PAF5Event marriageEvent = new PAF5Event();
	PAF5OrdinanceEvent ldsSealingEvent = new PAF5OrdinanceEvent();
	char divorcedStatus;
	
	public PAF5MarriageRecord() {
		type = RecordType.MARRIAGE;
		type.size = 106;
	}
	
	@Override
	public void read(LEndianRandomAccessFile file) throws IOException {
		modifiedTime = file.readIntLE();
		husbandRin = file.readIntLE();
		wifeRin = file.readIntLE();
		firstChildLinkRin = file.readIntLE();
		husbandNextMRin = file.readIntLE();
		wifeNextMRin = file.readIntLE();
		firstUserEventRin = file.readIntLE();
		noteRin = file.readIntLE();
		citationRin = file.readIntLE();
		file.readFully(uId);
		marriageEvent.read(file);
		ldsSealingEvent.read(file);
		byte[] byteBuf = new byte[1];
		byteBuf[0] = (byte)file.read();
		divorcedStatus = new String(byteBuf, "ASCII").charAt(0);
		byte[] buffer = new byte[8];
		file.readFully(buffer);
	}
}
