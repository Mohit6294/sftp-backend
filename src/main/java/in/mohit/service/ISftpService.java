package in.mohit.service;

import in.mohit.model.FileModal;

import java.io.InputStream;
import java.util.List;

public interface ISftpService {
	
	
	String readFile(String remoteDirectory, String remoteFilename);

	 List<FileModal> listFilesInDirectory(String remoteDirectory);

	 FileModal uploadFile(String remoteDirectory, String filename, InputStream fileInputStream,Long fileSize);

	void deleteFile(String remoteDirectory, String fileName);

	void downloadFile(String remoteFilePath, String localFilePath);
	byte[] downloadPdf(String remoteFilePath);

}
