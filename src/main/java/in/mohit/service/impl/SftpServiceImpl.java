package in.mohit.service.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import in.mohit.model.FileModal;
import in.mohit.util.DateFormatter;
import org.apache.sshd.sftp.client.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.InputStreamCallback;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import in.mohit.service.ISftpService;


@Service
public class SftpServiceImpl implements ISftpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpServiceImpl.class);
	
	
	@Autowired
	private SftpRemoteFileTemplate sftpRemoteFileTemplate;

    @Autowired
    private DefaultSftpSessionFactory sftpSessionFactory;


	
	@Override
	public String readFile(String remoteDirectory, String remoteFilename) {
		StringBuilder fileContent = new StringBuilder();

        sftpRemoteFileTemplate.get(remoteDirectory + "/" + remoteFilename, (InputStreamCallback) inputStream -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while reading the file");
                throw new RuntimeException("Error reading file from SFTP", e);
            }
        });

        return fileContent.toString();
	}


    @Override
    public List<FileModal> listFilesInDirectory(String remoteDirectory) {
        List<FileModal> fileNames = new ArrayList<>();
        try {
            sftpRemoteFileTemplate.execute(session -> {
                SftpClient.DirEntry[] files = session.list(remoteDirectory);  // List all files in directory
                for (SftpClient.DirEntry file : files) {
                    // Add file names to the list
                    if (!Objects.equals(file.getFilename(), ".") && !Objects.equals(file.getFilename(), "..")) {
                        LocalDateTime ldt = DateFormatter.parseCustomDateString(new Date(file.getAttributes().getModifyTime().toMillis()).toString());
                        FileModal fileModal = new FileModal(file.getFilename()
                                , file.getAttributes().getSize()
                                , ldt.toString().replace("T", " ")
                                , file.getAttributes().getType() == 2);
                        fileNames.add(fileModal);
                    }

                }
                return null;
            });
        }catch (Exception e){
            LOGGER.error("Some error is happening ",e);
        }
        return fileNames;
    }

    @Override
    public void uploadFile(String remoteDirectory, String fileName, InputStream fileInputStream) {
        sftpRemoteFileTemplate.execute(session -> {
            session.write(fileInputStream, remoteDirectory + "/" + fileName);
            return null;
        });
    }

    @Override
    public void deleteFile(String remoteDirectory, String fileName)  {
        sftpRemoteFileTemplate.execute(session -> {
            session.remove(remoteDirectory + "/" + fileName);
            return null;
        });
    }

    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) {
        // Download the file using SFTP
        sftpRemoteFileTemplate.execute(session -> {
            try (InputStream inputStream = session.readRaw(remoteFilePath);
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(localFilePath))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }
            return null;
        });
    }


    public byte[] downloadPdf(String remoteFilePath) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        sftpRemoteFileTemplate.execute(session -> {
            InputStream inputStream = session.readRaw(remoteFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return null;  // return something if needed
        });

        return outputStream.toByteArray();
    }


}
