package org.myhstry.core;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	void init() throws IOException;

    void store(MultipartFile file) throws Exception;

}
