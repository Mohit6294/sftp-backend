package in.mohit.controller;

import in.mohit.model.ApiResponse;
import in.mohit.model.FileModal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.mohit.service.ISftpService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:3000")
public class SftpController {

	@Autowired
	private ISftpService sftpService;

	@GetMapping
	public ResponseEntity<List<FileModal>> listFiles(@RequestParam(defaultValue = "/",required = false) String remoteDirectory) {
		return  ResponseEntity.ok(sftpService.listFilesInDirectory(remoteDirectory));
	}

	@GetMapping("/details")
	public String readFile(@RequestParam("filename") String filename,
						   @RequestParam(defaultValue = "/",required = false) String remoteDirectory) {
		return sftpService.readFile(remoteDirectory, filename);
	}

	@PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(MultipartFile file, @RequestParam(value = "directory"
			,defaultValue = "/",required = false) String directory) {

		// Get input stream from MultipartFile and upload it to the specified SFTP directory
        try {
			sftpService.uploadFile(directory, file.getOriginalFilename(), file.getInputStream());
			return ResponseEntity.status(HttpStatus.CREATED).body("Uploaded the file to SFTP Server");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse> deleteFile(@RequestParam("filename") String filename
			, @RequestParam(value = "remoteDirectory",required = false,defaultValue = "/") String remoteDirectory) {
		try {
			sftpService.deleteFile(remoteDirectory, filename);
			return ResponseEntity.ok(new ApiResponse(HttpStatus.OK,HttpStatus.OK.getReasonPhrase(), "Deleted Successfully"));
		} catch (Exception e) {
			return ResponseEntity.ok(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "File deletion failed"));
		}
	}
	@GetMapping("/download")
	public String downloadFile(@RequestParam(value = "remoteFilePath") String remoteFilePath,
							   @RequestParam(value = "localFilePath",defaultValue = "/Downloads",required = false) String localFilePath) {
		try {
			sftpService.downloadFile(remoteFilePath, localFilePath);
			return "File downloaded successfully!";
		} catch (Exception e) {
			return "Error downloading file: " + e.getMessage();
		}
	}

	@GetMapping("/download-pdf")
	public ResponseEntity<byte[]> downloadPdf(@RequestParam("filePath") String remoteFilePath) {
		try {
			remoteFilePath = remoteFilePath.replace("%"," ");
			byte[] pdfBytes = sftpService.downloadPdf(remoteFilePath);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			String[] arr =  remoteFilePath.split("/");
			headers.setContentDispositionFormData("attachment", arr[arr.length-1]);

			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}






}
