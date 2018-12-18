package org.myhstry.core.paf;
import java.io.IOException;

class UserData {
	
	static final int size = 768;
	
	String name;
	String address1;
	String address2;
	String address3;
	String address4;
	String country;
	String phone;
	String submitterAFN;
	String submitterEmail;
	long submitterUId;
	long defaultLanguage;
		
	
	public UserData(LEndianRandomAccessFile file) throws IOException {
		name = file.readString(84);
		address1 = file.readString(84);
		address2 = file.readString(84);
		address3 = file.readString(84);
		address4 = file.readString(84);
		country = file.readString(84);
		phone = file.readString(52);
		submitterAFN = file.readString(28);
		submitterUId = file.readLongLE();
		defaultLanguage = file.readLongLE();
		
		//read extra space - 96 bytes
		file.readString(96);
	}
	
	public String toString() {
		return "Name is " + name;
	}
}