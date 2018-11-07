package org.myhstry.core.paf;
import java.io.IOException;

class PAFFileBlocks {
	
	static final int size = 716;
	
	int mapCount;
	int mapBlockCount;
	
	int indiCount;
	int indiBlockCount;
	int firstIndi;
	int firstFreeIndi;
	int indiFreeCount;
	
	int indiIdxCount;
	int indiIdxBlockCount;
	int firstIndiIdx;
	int firstFreeIndiIdx;
	int indiIdxFreeCount;
	
	int indiNameCount;
	int indiNameBlockCount;
	int indiNameStart;
	int indiNameOffset;
	int indiNameHighOffset;
	int firstFreeIndiName;
	int indiNameFreeCount;
	
	int nameCount;
	int nameBlockCount;
	int nameStart;
	int nameOffset;
	int nameHighOffset;
	
	int otherNameCount;
	int otherNameBlockCount;
	int otherNameStart;
	int otherNameOffset;
	int otherNameHighOffset;
	
	int marriageCount;
	int marriageBlockCount;
	int firstMarriage;
	int firstFreeMarriage;
	int marriageFreeCount;
	
	int noteCount;
	int noteBlockCount;
	int firstNote;
	int firstFreeNote;
	int noteFreeCount;
	
	int citationCount;
	int citationBlockCount;
	int firstCitation;
	int firstFreeCitation;
	int citationFreeCount;
	
	int sourceCount;
	int sourceBlockCount;
	int firstSource;
	int firstFreeSource;
	int sourceFreeCount;
	
	int repositoryCount;
	int repositoryBlockCount;
	int firstRepository;
	int firstFreeRepository;
	int repositoryFreeCount;
	
	int familyLinkCount;
	int familyLinkBlockCount;
	int firstFamilyLink;
	int firstFreeFamilyLink;
	int familyLinkFreeCount;
	
	int eventTypeCount;
	int eventTypeBlockCount;
	int firstEventType;
	int firstFreeEventType;
	int eventTypeFreeCount;
	
	int mediaCount;
	int mediaBlockCount;
	int firstMedia;
	int firstFreeMedia;
	int mediaFreeCount;
	
	int eventCount;
	int eventBlockCount;
	int firstEvent;
	int firstFreeEvent;
	int eventFreeCount;
	
	int contactCount;
	int contactBlockCount;
	int firstContact;
	int firstFreeContact;
	int contactFreeCount;
		
	public PAFFileBlocks(LEndianRandomAccessFile file) throws IOException {
		mapCount = file.readIntLE();
		mapBlockCount = file.readIntLE();
		
		indiCount = file.readIntLE();
		indiBlockCount = file.readIntLE();
		firstIndi = file.readIntLE();
		firstFreeIndi = file.readIntLE();
		indiFreeCount = file.readIntLE();
		
		indiIdxCount = file.readIntLE();
		indiIdxBlockCount = file.readIntLE();
		firstIndiIdx = file.readIntLE();
		firstFreeIndiIdx = file.readIntLE();
		indiIdxFreeCount = file.readIntLE();
		
		indiNameCount = file.readIntLE();
		indiNameBlockCount = file.readIntLE();
		indiNameStart = file.readIntLE();
		indiNameOffset = file.readIntLE();
		indiNameHighOffset = file.readIntLE();
		firstFreeIndiName = file.readIntLE();
		indiNameFreeCount = file.readIntLE();
		
		nameCount = file.readIntLE();
		nameBlockCount = file.readIntLE();
		nameStart = file.readIntLE();
		nameOffset = file.readIntLE();
		nameHighOffset = file.readIntLE();
		
		otherNameCount = file.readIntLE();
		otherNameBlockCount = file.readIntLE();
		otherNameStart = file.readIntLE();
		otherNameOffset = file.readIntLE();
		otherNameHighOffset = file.readIntLE();
		
		marriageCount = file.readIntLE();
		marriageBlockCount = file.readIntLE();
		firstMarriage = file.readIntLE();
		firstFreeMarriage = file.readIntLE();
		marriageFreeCount = file.readIntLE();
		
		noteCount = file.readIntLE();
		noteBlockCount = file.readIntLE();
		firstNote = file.readIntLE();
		firstFreeNote = file.readIntLE();
		noteFreeCount = file.readIntLE();
		
		citationCount = file.readIntLE();
		citationBlockCount = file.readIntLE();
		firstCitation = file.readIntLE();
		firstFreeCitation = file.readIntLE();
		citationFreeCount = file.readIntLE();
		
		sourceCount = file.readIntLE();
		sourceBlockCount = file.readIntLE();
		firstSource = file.readIntLE();
		firstFreeSource = file.readIntLE();
		sourceFreeCount = file.readIntLE();
		
		repositoryCount = file.readIntLE();
		repositoryBlockCount = file.readIntLE();
		firstRepository = file.readIntLE();
		firstFreeRepository = file.readIntLE();
		repositoryFreeCount = file.readIntLE();
		
		familyLinkCount = file.readIntLE();
		familyLinkBlockCount = file.readIntLE();
		firstFamilyLink = file.readIntLE();
		firstFreeFamilyLink = file.readIntLE();
		familyLinkFreeCount = file.readIntLE();
		
		eventTypeCount = file.readIntLE();
		eventTypeBlockCount = file.readIntLE();
		firstEventType = file.readIntLE();
		firstFreeEventType = file.readIntLE();
		eventTypeFreeCount = file.readIntLE();
		
		mediaCount = file.readIntLE();
		mediaBlockCount = file.readIntLE();
		firstMedia = file.readIntLE();
		firstFreeMedia = file.readIntLE();
		mediaFreeCount = file.readIntLE();
		
		eventCount = file.readIntLE();
		eventBlockCount = file.readIntLE();
		firstEvent = file.readIntLE();
		firstFreeEvent = file.readIntLE();
		eventFreeCount = file.readIntLE();
		
		contactCount = file.readIntLE();
		contactBlockCount = file.readIntLE();
		firstContact = file.readIntLE();
		firstFreeContact = file.readIntLE();
		contactFreeCount = file.readIntLE();
		
		//read 400 spare bytes
		byte[] extra = new byte[400];
		file.readFully(extra);
	}

	public String toString() {
		return "Map Count is " + mapCount + ", Map Block Count is " + mapBlockCount + ", Contact Free Count is " + contactFreeCount;
	}
}