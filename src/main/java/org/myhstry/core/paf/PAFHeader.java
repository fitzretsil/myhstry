package org.myhstry.core.paf;
import java.io.IOException;

public class PAFHeader {
	
	static final int size = 4096;
	
	private String lastVersionUsed;
	private String earliestVersion;
	private String fileIdentifier;
	private short databaseVersion;
	
	private PAFFileBlocks fileBlocks;
	
	private int pedigreeIndiRecordNum;
	
	private UserData userData;
	
	private int[] lastSavedIndiRecordNums = new int[10];
	private int[] lastSavedCitationRecordNums = new int[10];
	private int[] lastSavedMarriageRecordNums = new int[10];
	private int lastViewedIndiRecordNum;
	private byte cLog;
	private short numTimesClosedSinceBackup;
	private int showRelationshipIndiRecordNum;
	
	//system time - last modified
	private short[] modifedTime = new short[8];
	
	private long databaseUId;
	private long auxValue1;
	private long auxValue2;
	private long flags1;
	private long flags2;
	private long flags3;
	private long flags4;
	private long flags5;

	public PAFHeader(LEndianRandomAccessFile file) throws IOException {
		lastVersionUsed = file.readString(4);
		System.out.println("Last PAF Version Used: " + lastVersionUsed);
		
		earliestVersion = file.readString(4);
		System.out.println("Earliest PAF Version that can read file: " + earliestVersion);
		
		fileIdentifier = file.readString(4);
		System.out.println("File Identifier: " + fileIdentifier);
		
		databaseVersion = file.readShortLE();
		System.out.println("Database Version: " + databaseVersion);
		
		fileBlocks = new PAFFileBlocks(file);
		System.out.println("File Blocks: " + fileBlocks.toString());
		
		pedigreeIndiRecordNum = file.readIntLE();
		System.out.println("Pedigree Individual Record Number: " + pedigreeIndiRecordNum);
		
		userData = new UserData(file);
		System.out.println("User Data: " + userData);
		
		for (int i = 0; i < 10; ++i) {
			lastSavedIndiRecordNums[i] = file.readIntLE();
			System.out.println("Last Saved Individual Record " + i + ": " + lastSavedIndiRecordNums[i]);
		}
		for (int i = 0; i < 10; ++i) {
			lastSavedCitationRecordNums[i] = file.readIntLE();
			System.out.println("Last Saved Citation Record " + i + ": " + lastSavedCitationRecordNums[i]);
		}
		for (int i = 0; i < 10; ++i) {
			lastSavedMarriageRecordNums[i] = file.readIntLE();
			System.out.println("Last Saved Marriage Record " + i + ": " + lastSavedMarriageRecordNums[i]);
		}
		
		lastViewedIndiRecordNum = file.readIntLE();
		System.out.println("Last Viewed Individual Record: " + lastViewedIndiRecordNum);
		
		cLog = file.readByte();
		System.out.println("cLog: " + cLog);
		
		numTimesClosedSinceBackup = file.readShortLE();
		System.out.println("numTimesClosedSinceBackup: " + numTimesClosedSinceBackup);
		
		showRelationshipIndiRecordNum = file.readIntLE();
		System.out.println("showRelationshipIndiRecordNum: " + showRelationshipIndiRecordNum);
		
		//system time - last modified
		for (int i = 0; i < 8; ++i) {
			modifedTime[i] = file.readShortLE();
			System.out.println("Modified time " + i + ": " + modifedTime[i]);
		}
		
		databaseUId = file.readLongLE();
		System.out.println("Database UID: " + databaseUId);
		
		auxValue1 = file.readLongLE();
		System.out.println("auxValue1: " + auxValue1);
		
		auxValue2 = file.readLongLE();
		System.out.println("auxValue2: " + auxValue2);
		
		flags1 = file.readLongLE();
		flags2 = file.readLongLE();
		flags3 = file.readLongLE();
		flags4 = file.readLongLE();
		flags5 = file.readLongLE();
		System.out.println("Flags: " + flags1 + " " + flags2 + " " + flags3 + " " + flags4 + " " + flags5 + " ");
	}

	public static int getSize() {
		return size;
	}

	public PAFFileBlocks getFileBlocks() {
		return fileBlocks;
	}
}
