package org.myhstry.core.paf;
import java.io.IOException;

class PAF5Event
{
	public PAF5Date date = new PAF5Date();
	public long placeNameOffset;
	public long ageAtNameOffset;
	public int citationRin;
	
	public void read(LEndianRandomAccessFile file) throws IOException
	{
		date.read(file);
		placeNameOffset = file.readIntLE();
		ageAtNameOffset = file.readIntLE();
		citationRin = file.readIntLE();
	}
}