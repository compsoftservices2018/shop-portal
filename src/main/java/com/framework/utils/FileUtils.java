package com.framework.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.framework.reference.ReferenceUtils;

public class FileUtils {

	
	public static boolean uploadFile(HttpSession session, MultipartFile foFile, String fsUploadFileName, String fsSubFolder)
	{
		if (!foFile.isEmpty()) {
			try {
				byte[] bytes = foFile.getBytes();

				// Creating the directory to store file
				String lsRootPath = ReferenceUtils.getCnfigParamValue(session, "UPLOAD_FOLDER");
				File dir = new File(lsRootPath + File.separator + fsSubFolder );
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File lsServerFile = new File(dir.getAbsolutePath() + File.separator + fsUploadFileName);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(lsServerFile));
				stream.write(bytes);
				stream.close();
				System.out.println(dir.getAbsolutePath()
						+ File.separator + "Test2.txt");
				return true;
			} catch (Exception e) {
				e.getMessage();
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
}
