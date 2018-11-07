package org.myhstry.core.paf;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

class PAF5Date
{
	byte[] dateInfo = new byte[12];
	String dateString = "";
	
	public static int getJulianDay(int year, int month, int day)
	{
		int result = day - 32075
	      + 1461 * ( year + 4800 - ( 14 - month ) / 12 )/4
	      + 367 * ( month - 2 + ( ( 14 - month ) / 12 ) * 12 ) / 12
	      - 3 * ( ( year + 4900 - ( 14 - month ) / 12 ) / 100 ) / 4;
		
		return result;
	}
	
	public int getJulianDayDate1()
	{
		int julianDay = (dateInfo[4] & 0xFF) +
						((dateInfo[5] & 0xFF) << 8) +
						((dateInfo[6] & 0xFF) << 16);
		
		//read julian date flags
		
		
		return julianDay;
	}
	
	public int getJulianDayDate2()
	{
		int julianDay = (dateInfo[9] & 0xFF) +
						((dateInfo[10] & 0xFF) << 8) +
						((dateInfo[11] & 0xFF) << 16);
		
		return julianDay;
	}
	
	public static Calendar getCalendarFromJulianDay(int julianDay)
	{
		int jd = julianDay;
		int jdate_tmp;
		int y, m, d;
		
		jdate_tmp = jd - 1721119;
        y = (4 * jdate_tmp - 1)/146097;
        jdate_tmp = 4 * jdate_tmp - 1 - 146097 * y;
        d = jdate_tmp/4;
        jdate_tmp = (4 * d + 3)/1461;
        d = 4 * d + 3 - 1461 * jdate_tmp;
        d = (d + 4)/4;
        m = (5 * d - 3)/153;
        d = 5 * d - 3 - 153 * m;
        d = (d + 5) / 5;
        y = 100 * y + jdate_tmp;
        if(m < 10) {
                m += 3;
        } else {
                m -= 9;
                ++y;
        }
        
        Calendar cal = new GregorianCalendar(y, m-1, d);
        
        return cal;
	}
	
	public void read(LEndianRandomAccessFile file) throws IOException
	{
		file.readFully(dateInfo);
		int dateRecordDescriptor = dateInfo[0] & 0x07;
		int date1Type = (dateInfo[0] >>> 4) & 0x03;
		int date2Type = (dateInfo[0] >>> 6) & 0x03;
		
		if (dateRecordDescriptor == 0)// no date
			return;
		
//		int dateStatusOrModifier = (dateInfo[1]) & 0xFF;
		
		String date1String = null;
		String date2String = null;
		boolean date1NoDay = false;
		boolean date1NoMonth = false;
		boolean date1NoYear = false;
		boolean date2NoDay = false;
		boolean date2NoMonth = false;
		boolean date2NoYear = false;
		int julianDayDate1 = 0;
		int julianDayDate2 = 0;
		
		
		if (dateRecordDescriptor == 1) //date status (e.g. BIC, etc)
		{
			//check flags in dateStatusOrModifier
			dateString = "Date Status";
			return;
		}
		
		if (date1Type == 1)//julian date
		{
			//read date flags
			julianDayDate1 = getJulianDayDate1();
			date1NoDay = (((dateInfo[3] >> 5) & 0x1) == 1)? true : false;
			date1NoMonth = (((dateInfo[3] >> 6) & 0x1) == 1)? true : false;
			date1NoYear = (((dateInfo[3] >> 7) & 0x1) == 1)? true : false;
//			int eraId = (dateInfo[2] & 0xFF) + 
//					((dateInfo[3] & 0x0F) << 8);
			date1String = getJulianDateString(julianDayDate1, date1NoDay, date1NoMonth, date1NoYear);
		} else if (date1Type == 2) //non-standard date
		{
			//get date text from name record
			long oldFilePos = file.getFilePointer();
			int nameOffset = (dateInfo[2] & 0xFF) +
				((dateInfo[3] & 0xFF) << 8) +
				((dateInfo[4] & 0xFF) << 16) +
				((dateInfo[5] & 0xFF) << 24);
			PAF5NameRecord dateName = new PAF5NameRecord();
			file.seek(nameOffset);
			dateName.read(file);
			file.seek(oldFilePos);
			date1String = dateName.text;
		}
		
		if (dateRecordDescriptor == 3 || dateRecordDescriptor == 4)
		{
			if (date2Type == 1)//julian date
			{
				julianDayDate2 = getJulianDayDate2();
				date2NoDay = (((dateInfo[3] >> 5) & 0x1) == 1)? true : false;
				date2NoMonth = (((dateInfo[3] >> 6) & 0x1) == 1)? true : false;
				date2NoYear = (((dateInfo[3] >> 7) & 0x1) == 1)? true : false;
//				int eraId = (dateInfo[7] & 0xFF) + 
//				((dateInfo[8] & 0x0F) << 8);
				date2String = getJulianDateString(julianDayDate2, date2NoDay, date2NoMonth, date2NoYear);
			} else if (date2Type == 2) //non-standard date
			{
				//get date text from name record
				long oldFilePos = file.getFilePointer();
				int nameOffset = (dateInfo[2] & 0xFF) +
					((dateInfo[3] & 0xFF) << 8) +
					((dateInfo[4] & 0xFF) << 16) +
					((dateInfo[5] & 0xFF) << 24);
				PAF5NameRecord dateName = new PAF5NameRecord();
				file.seek(nameOffset);
				dateName.read(file);
				file.seek(oldFilePos);
				date2String = dateName.text;
			}
		}
				
		
		switch (dateRecordDescriptor)
		{
		case 2://single date
			dateString = date1String;
			break;
		case 3://split date
			//NOTE: PAF5 only allows one field of day/month/year to differ
			Calendar cal1 = getCalendarFromJulianDay(julianDayDate1);
			Calendar cal2 = getCalendarFromJulianDay(julianDayDate2);
			boolean first = true;
			if (!date1NoDay)
			{
				first = false;
				DateFormat format = new SimpleDateFormat("d");
				if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH) && !date2NoDay)
				{
					dateString += format.format(cal1.getTime()) + "/";
					dateString += format.format(cal2.getTime());
				} else
					dateString += format.format(cal1.getTime());
			}
			if (!date1NoMonth)
			{
				if (!first)
					dateString += " ";
				first = false;
				DateFormat format = new SimpleDateFormat("MMM");
				if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) && !date2NoMonth)
				{
					dateString += format.format(cal1.getTime()) + "/";
					dateString += format.format(cal2.getTime());
				} else
					dateString += format.format(cal1.getTime());
			}
			if (!date1NoYear)
			{
				if (!first)
					dateString += " ";
				first = false;
				DateFormat format = new SimpleDateFormat("yyyy");
				if ((cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
						(cal1.get(Calendar.ERA) != cal2.get(Calendar.ERA))) && !date2NoYear)
				{
					if (cal1.get(Calendar.ERA) == GregorianCalendar.BC)
						format = new SimpleDateFormat("yyyy G");
					dateString += format.format(cal1.getTime()) + "/";
					if (cal2.get(Calendar.ERA) == GregorianCalendar.BC)
						format = new SimpleDateFormat("yyyy G");
					else
						format = new SimpleDateFormat("yyyy");
					dateString += format.format(cal2.getTime());
				} else
					dateString += format.format(cal1.getTime());
			}			
			break;
		case 4://date range
			dateString = "from " + date1String + " to " + date2String;
			break;
		};	
	}

	private String getJulianDateString(int julianDayDate, boolean noDay, boolean noMonth, boolean noYear) {
		Calendar cal = getCalendarFromJulianDay(julianDayDate);
		boolean first = true;
		String formatString = "";
		if (!noDay)
		{
			first = false;
			formatString += "d";
		}
		if (!noMonth)
		{
			if (!first)
				formatString += " ";
			first = false;
			formatString += "MMM";	
		}
		if (!noYear)
		{
			if (!first)
				formatString += " ";
			first = false;
			formatString += "yyyy";
		}
		if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
			formatString += " G";
		DateFormat format = new SimpleDateFormat(formatString);;
		return format.format(cal.getTime());
		
	}
}