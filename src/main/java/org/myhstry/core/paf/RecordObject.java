package org.myhstry.core.paf;
import java.io.IOException;

public abstract class RecordObject {

	enum RecordType {

		NAME(1, 269), INDI(2, 221), MARRIAGE(3, 106), NOTE(2, 256), FAMILY_LINK(5, 46), CITATION(6, 141),
		SOURCE(7, 507), REPOSITORY(8, 497), INDI_NAME(9, 271), INDI_INDEX(10, 16), MAP(11, 8192), USER_EVENT(12, 59),
		EVENT_TYPE(13, 140), CONTACT(17, 57), OTHER_NAME(18, 269), MEDIA(20, 62);

		public int typeId;
		public int size; // size in database in bytes
		public int numPerBlock;
		public int BLOCK_SIZE = 8192;

		private RecordType(int typeId, int size) {
			this.typeId = typeId;
			this.numPerBlock = BLOCK_SIZE / size;
			this.size = size;
		}
	}

	RecordType type;

	abstract public void read(LEndianRandomAccessFile file) throws IOException;

}
