package org.myhstry.core.paf;
import java.io.IOException;

public class PAFFile {

	private static final int PAF5_MAP_TOTALS_SIZE = 8192;
	private static final int PAF5_CLUSTER_SIZE = 67108864;
	private static final int PAF5_BLOCK_SIZE = 8192;
	private static final int PAF5_MAX_CLUSTERS = 31;
	private static final int PAF5_MAX_BLOCK_TYPES = 32;
	private static final int PAF5_MAP_RECORD_SIZE = 8192;
	
	static short[][] mapBlocks = new short[PAF5_MAX_CLUSTERS][];
	static short dBMap[] = new short[PAF5_MAP_RECORD_SIZE / 2];

	private PAFHeader header;

	public PAFFile(LEndianRandomAccessFile file) throws IOException {
		GedcomRecord record = new GedcomRecord();
		
		header = new PAFHeader(file);

		if (!loadMapBlocks(file))
			throw new IOException("File has invalid database map Block(s).");

		loadDBMap(file);

		getIndividuals(file, record);
		
		getFamilies(file, record);
		
		record.linkRecord();
	}

	private void getFamilies(LEndianRandomAccessFile file, GedcomRecord record) throws IOException {			
			int maxRin = header.getFileBlocks().marriageCount;
			
			for (int i = 1; i <= maxRin; ++i)
			{
				Family fam = getFamily(file, i);
				if (fam != null)
				{
					System.out.println(fam.toString());
					//add fams and famc ids in for individuals in marriage
					if (fam.husbandId != null)
					{
						Individual husband = record.getIndividuals().get(fam.husbandId);
						System.out.println("Found Husband: " + husband.toString());
						if (husband != null)
							husband.famsIds.add(fam.id);
					}
					if (fam.wifeId != null)
					{
						Individual wife = record.getIndividuals().get(fam.wifeId);
						if (wife != null)
							wife.famsIds.add(fam.id);
					}
					for(String childId : fam.childrenXRefIds)
					{
						Individual child = record.getIndividuals().get(childId);
						if (child != null)
							child.famcIds.add(fam.id);
					}			
				} else
					maxRin++;
					
			}
		
	}

	private Family getFamily(LEndianRandomAccessFile file, int mRin) throws IOException {
		Family fam = new Family("F" + mRin);
		PAF5MarriageRecord marriageRecord = new PAF5MarriageRecord();
		loadFromDatabase(file, marriageRecord, mRin);
		//check for deleted record
		if (marriageRecord.divorcedStatus == 'D')
			return null;
		
		//husband
		if (marriageRecord.husbandRin != 0)
			fam.husbandId = "I" + marriageRecord.husbandRin;
		
		//wife
		if (marriageRecord.wifeRin != 0)
			fam.wifeId = "I" + marriageRecord.wifeRin;
		
		//marriage event
		if (marriageRecord.marriageEvent.placeNameOffset != 0 ||
				!marriageRecord.marriageEvent.date.dateString.equals(""))
		{
			fam.marriage = new Event(EventType.MARRIAGE);
			if (marriageRecord.marriageEvent.placeNameOffset != 0)
			{
				PAF5NameRecord marriagePlace = new PAF5NameRecord();
				file.seek(marriageRecord.marriageEvent.placeNameOffset);
				marriagePlace.read(file);
				fam.marriage.place = marriagePlace.text;
			}
			if (!marriageRecord.marriageEvent.date.dateString.equals(""))
				fam.marriage.date = marriageRecord.marriageEvent.date.dateString;
		}
		
		
		//children
		PAF5FamilyLinkRecord famLink = new PAF5FamilyLinkRecord();
		int curChildLinkRin = marriageRecord.firstChildLinkRin;
		while (curChildLinkRin > 0)
		{
			loadFromDatabase(file, famLink, curChildLinkRin);
			if (famLink.linkType == 'D')
				break;
			if (famLink.rin == 0)
				break;
			fam.childrenXRefIds.add("I" + famLink.rin);
			curChildLinkRin = famLink.nextSiblngRin;
			if (curChildLinkRin == marriageRecord.firstChildLinkRin)
				break;
		}
		
		//todo - sealing status info 
		
		return fam;
	}

	private void getIndividuals(LEndianRandomAccessFile file, GedcomRecord record) throws IOException {
		int maxRin = header.getFileBlocks().indiCount;

		for (int i = 1; i <= maxRin; ++i) {
			Individual indi = getIndividual(file, i);
			if (indi != null) {
				record.addIndividual(indi.id, indi);
				System.out.println("Individual " + i + ": " + indi.toString());
			} else
				maxRin++;

		}

	}

	private Individual getIndividual(LEndianRandomAccessFile file, int rin) throws IOException {
		Individual indi = new Individual("I" + rin);
		PAF5IndiRecord indiRecord = new PAF5IndiRecord();
		loadFromDatabase(file, indiRecord, rin);
		//name
		PAF5IndiNameRecord nameRecord = new PAF5IndiNameRecord();
		file.seek(indiRecord.nameOffset);
		nameRecord.read(file);
		parseIndiName(indi, nameRecord.text);
		//gender
		if (indiRecord.sex == 'M')
			indi.gender = Gender.MALE;
		else if (indiRecord.sex == 'F')
			indi.gender = Gender.FEMALE;
		else if (indiRecord.sex == 'U')
			indi.gender = Gender.UNKNOWN;
		else
			return null; //deleted record

		//birth
		if (indiRecord.birth.placeNameOffset != 0 ||
				!indiRecord.birth.date.dateString.equals(""))
		{
			indi.birth = new Event(EventType.BIRTH);
			if (indiRecord.birth.placeNameOffset != 0)
			{
				PAF5NameRecord birthPlace = new PAF5NameRecord();
				file.seek(indiRecord.birth.placeNameOffset);
				birthPlace.read(file);
				indi.birth.place = birthPlace.text;
				
			}
			if (!indiRecord.birth.date.dateString.equals(""))
				indi.birth.date = indiRecord.birth.date.dateString;
			indi.birth.parseDateParts();
		}
		
		//death
		if (indiRecord.death.placeNameOffset != 0 ||
				!indiRecord.death.date.dateString.equals(""))
		{
			indi.death = new Event(EventType.DEATH);
			if (indiRecord.death.placeNameOffset != 0)
			{
				PAF5NameRecord deathPlace = new PAF5NameRecord();
				file.seek(indiRecord.death.placeNameOffset);
				deathPlace.read(file);
				indi.death.place = deathPlace.text;
			}
			if (!indiRecord.death.date.dateString.equals(""))
				indi.death.date = indiRecord.death.date.dateString;
			indi.death.parseDateParts();
		}
		
		//todo - photo and ordinances
		
		return indi;
	}

	private void parseIndiName(Individual indi, String text) {
		indi.givenName = getGivenName(text);
		indi.surname = getSurname(text);
		indi.nameSuffix = getNameSuffix(text);
	}

	private String getNameSuffix(String nameline) {
		int start = nameline.lastIndexOf("/");
		if(start != -1 && start < nameline.length()-1)
			return nameline.substring(start+1);
		
		else return "";
	}

	private String getSurname(String nameline) {
		int start = nameline.indexOf("/");
		int end = nameline.lastIndexOf("/");

		if(start != -1 && end != -1 && start < nameline.length()-1 && start != end){
			return nameline.substring(start+1, end);
		}
		else if(start != -1 && end == -1 && start < nameline.length()-1 && start != end){
			return nameline.substring(start+1);
		}
		else return "";
	}

	private String getGivenName(String nameline) {
		int end = nameline.indexOf("/");
		if(end != -1)
			return nameline.substring(0,end);
		else return nameline.trim();
	}

	private RecordObject loadFromDatabase(LEndianRandomAccessFile file, RecordObject obj, int rin) throws IOException {
		long filePos = findRecordPos(obj.type, rin);
		if (filePos == 0)
			return null;
		file.seek(filePos);
		obj.read(file);
		return obj;
	}
	
	private static long findRecordPos(RecordObject.RecordType type, long rin) {

		PAFCBR loc = findCBR(rin, type);

		if (rin < 1 || loc == null)
			return 0;

		return PAFHeader.size + PAF5_MAP_TOTALS_SIZE + loc.cluster * PAF5_CLUSTER_SIZE + loc.block * PAF5_BLOCK_SIZE
				+ loc.record * type.size;
	}

	private static PAFCBR findCBR(long rin, RecordObject.RecordType type) {
		PAFCBR result = new PAFCBR();
		long gotSoFar;
		long recordsLeft;
		
		gotSoFar = findWhichCluster((int) rin, type, result);
		if (result.cluster < 0 || result.cluster >= PAF5_MAX_CLUSTERS)
			return null;
		
		recordsLeft = rin - gotSoFar;
		result.block = findWhichBlock(result.cluster, rin, type);
		if (result.block < 1 || result.block > PAF5_BLOCK_SIZE)
			return null;
		
		result.record = (int)(recordsLeft - type.numPerBlock*((recordsLeft - 1)/type.numPerBlock)) - 1;
		if (result.record < 0 || result.record > PAF5_BLOCK_SIZE)
			return null;
		
		return result;
	}

	private static int findWhichBlock(int cluster, long rin, RecordObject.RecordType type) {
		short[] mapBlock = mapBlocks[cluster];
		int tmp = mapBlock[type.typeId - 1] + (int)((rin - 1) / type.numPerBlock);
		
		return mapBlock[tmp];
	}

	private static long findWhichCluster(int rin, RecordObject.RecordType type, PAFCBR result) {
		long gotSoFar = 0;
		
		for (result.cluster = 0;  rin > gotSoFar + 
			(long)dBMap[(int)PAF5_MAX_BLOCK_TYPES * result.cluster + type.typeId - 1] * 
			(long)type.numPerBlock;)
		{
			gotSoFar += (long)dBMap[PAF5_MAX_BLOCK_TYPES * result.cluster + type.typeId - 1] *
				(long)type.numPerBlock;
			result.cluster++;
			if (result.cluster >= PAF5_MAX_CLUSTERS)
			{
				gotSoFar=0;
				break;
			}
		}
				
		return gotSoFar;
	}

	private void loadDBMap(LEndianRandomAccessFile file) throws IOException {
		

		file.seek(PAFHeader.size);
		for (int i = 0; i < dBMap.length; ++i) {
			dBMap[i] = file.readShortLE();
			System.out.println("DB Map " + i + ": " + dBMap[i]);
		}
	}

	private boolean loadMapBlocks(LEndianRandomAccessFile file) throws IOException {
		boolean retVal = true;
		for (int i = 0; i < header.getFileBlocks().mapBlockCount; ++i)
			retVal &= loadMapBlock(file, i);
		return retVal;
	}

	private boolean loadMapBlock(LEndianRandomAccessFile file, int cluster) throws IOException {
		// Load the Map Block
		byte[] mapBlockTmp = new byte[PAF5_BLOCK_SIZE];
		long filePos = PAFHeader.getSize() + PAF5_MAP_TOTALS_SIZE + cluster * PAF5_CLUSTER_SIZE;
		file.seek(filePos);
		file.readFully(mapBlockTmp);

		System.out.println("Map Block Tmp: " + mapBlockTmp[0]);

		if (mapBlockTmp[0] != RecordObject.RecordType.MAP.typeId)
			return false;
		System.out.println("Map Type is OK");

		// Allocate space for the pre-compiled Map Block
		int clusterCount = 0;
		while (clusterCount <= cluster) {
			System.out.println("Setting up map blocks for " + clusterCount);
			mapBlocks[clusterCount] = new short[PAF5_MAX_BLOCK_TYPES + PAF5_BLOCK_SIZE];
			++clusterCount;
		}
		short[] mapBlock = mapBlocks[cluster];

		// Pre-compile it to a table of indexes so we don't have to walk it
		int[] blockCount = new int[PAF5_MAX_BLOCK_TYPES];
		// Count number of each kind of block
		for (int i = 0; i < PAF5_BLOCK_SIZE; ++i) {
			if (mapBlockTmp[i] > 0 && mapBlockTmp[i] < PAF5_MAX_BLOCK_TYPES + 1)
				blockCount[mapBlockTmp[i] - 1]++;
		}

		// Set Front Indexes to point to where block location will be stored
		int tmp = 0;
		for (int i = 0; i < PAF5_MAX_BLOCK_TYPES; ++i) {
			mapBlock[i] = (short) (tmp + PAF5_MAX_BLOCK_TYPES);
			tmp += blockCount[i];
		}

		// Now fill the chart
		int[] foundBlocks = new int[PAF5_MAX_BLOCK_TYPES];
		for (int i = 0; i < PAF5_BLOCK_SIZE; ++i) {
			tmp = mapBlockTmp[i] - 1;
			if (tmp >= 0 && tmp < PAF5_MAX_BLOCK_TYPES) {
				mapBlock[mapBlock[tmp] + foundBlocks[tmp]] = (short) i;
				foundBlocks[tmp]++;
			}
		}

		return true;
	}
}
