/**
 * 
 */
package co.phystech.aosorio.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.phystech.aosorio.services.GeneralSvc;

import spark.Request;

/**
 * @author AOSORIO
 *
 */
public class FileSvc {
	
	private final static Logger slf4jLogger = LoggerFactory.getLogger(FileSvc.class);
	
	public static String uploadFile(Request pRequest) throws IOException, ServletException {
		
		File uploadDir = null;
		Path tempFile = null;
		long timestamp = System.currentTimeMillis();
		
		try {
	
			String tmpDir = GeneralSvc.setupTmpDir("/tmp");
			uploadDir = new File( tmpDir );
			
			slf4jLogger.info("LOCAL_TMP_PATH=" + tmpDir);

			pRequest.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(tmpDir));

			Collection<Part> parts;
			parts = pRequest.raw().getParts();
			Part fPart = parts.iterator().next();
			slf4jLogger.info(fPart.getSubmittedFileName());
			
			InputStream input = fPart.getInputStream();

			String fileExt = null;
			
			try {
				
				fileExt = fPart.getSubmittedFileName().split(".")[1];
			} catch (Exception ioex) {
			
				fileExt = ".tmp";
			}

			tempFile = Files.createTempFile(uploadDir.toPath(), String.valueOf(timestamp), fileExt);

			Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
			
			slf4jLogger.info(tempFile.toString());
			
		} catch (IOException | ServletException ex ) {
			
			ex.printStackTrace();
			throw ex;
		}
		
		return tempFile.toString();
	
	}
		
	public static void deleteFile(String fileName) throws IOException {
		
		File tmpFile = new File(fileName);
		if( tmpFile.exists() ) {
			tmpFile.delete();
		}
	
	}

	
}
