package org.myhstry.core.paf;
import java.io.IOException;

class PAF5OrdinanceEvent
{
	public PAF5Date date = new PAF5Date();
	public long placeNameOffset;
	public int citationRin;
	byte isTemple;
	
	public void read(LEndianRandomAccessFile file) throws IOException
	{
		date.read(file);
		placeNameOffset = file.readIntLE();
		citationRin = file.readIntLE();
		isTemple = file.readByte();
	}
}