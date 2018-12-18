package org.myhstry.core.paf;
import java.io.IOException;

class  PAF5IndiRecord extends RecordObject {
	
	public long nameOffset;
	public int firstMRin;
	public int firstFamLinkRin;
	public int noteRin;
	public int firstCitationRin;
	public long modifiedTime;
	public long submittedTime;
	public int firstUserEventRin;
	public int mediaRin;
	public int contactRin;
	public long relDescNameOffset;
	public int primaryMRin;
	byte[] uId = new byte[16];
	PAF5Event birth = new PAF5Event();
	PAF5Event christening = new PAF5Event();
	PAF5Event death = new PAF5Event();
	PAF5Event burial = new PAF5Event();
	PAF5OrdinanceEvent ldsBaptism = new PAF5OrdinanceEvent();
	PAF5OrdinanceEvent ldsEndowment = new PAF5OrdinanceEvent();
	char sex;
	byte numEventsWithSources;
	byte numSPEventsWtihSources;
			
	public PAF5IndiRecord() {
		type = RecordObject.RecordType.INDI;
		type.size = 221;
	}

	@Override
	public void read(LEndianRandomAccessFile file) throws IOException {
		nameOffset = file.readIntLE();
		firstMRin = file.readIntLE();
		firstFamLinkRin = file.readIntLE();
		noteRin = file.readIntLE();
		firstCitationRin = file.readIntLE();
		modifiedTime = file.readIntLE();
		submittedTime = file.readIntLE();
		firstUserEventRin = file.readIntLE();
		mediaRin = file.readIntLE();
		contactRin = file.readIntLE();
		relDescNameOffset = file.readIntLE();
		primaryMRin = file.readIntLE();
		file.readFully(uId);
		birth.read(file);
		christening.read(file);
		death.read(file);
		burial.read(file);
		ldsBaptism.read(file);
		ldsEndowment.read(file);
		byte[] byteBuf = new byte[1];
		byteBuf[0] = (byte)file.read();
		sex = new String(byteBuf, "ASCII").charAt(0);
		numEventsWithSources = file.readByte();
		numSPEventsWtihSources = file.readByte();
	}
}