package com.givemetreat.common;

import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Component
public class FileManagerService {

	public static final String FILE_UPLOAD_PATH = "C:\\Users\\syjnk\\7_PersonalProject\\prct_personal\\workspace_prjctQ3\\images/";
	
	public static String uploadFile(MultipartFile file, String loginId) {
		String directoryName = loginId + "_" + System.currentTimeMillis();
		String filePath = FILE_UPLOAD_PATH + directoryName + "/";
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			return null;
		}
		
		String nameEncoded = "";
		try {
			byte[] bytes = file.getBytes();
			// ISO-8859-1 방식으로 파일명(한글도 있을 수 있는)을 인코딩
			nameEncoded = new String(file.getOriginalFilename().getBytes("utf-8"), "8859_1");
			log.info("[FileManagerService: uploadFile()] namedEncoded:{}", nameEncoded);
			
			//URLDecoder.decode(nameEncoded, "UTF-8")이란 반대 방식도 추후 추가해야!
			
			//TODO 화질과 해상도 낮춘 imageThumbnail도 별도로 생성해서 path 반환해야함! 
			
			Path path = Paths.get(filePath + nameEncoded);
			Files.write(path, bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
			// 이미지 업로드 실패
			return null;
		}
		
		return "/images/" + directoryName + "/" + nameEncoded;
	}
	
	public static List<String> uploadImageWithThumbnail(MultipartFile file, String loginId) {
		String directoryName = loginId + "_" + System.currentTimeMillis();
		String filePath = FILE_UPLOAD_PATH + directoryName + "/";
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			return null;
		}
		
		String nameEncoded = "";
		String nameThumbnail = "";
		try {
			byte[] bytes = file.getBytes();
			// ISO-8859-1 방식으로 파일명(한글도 있을 수 있는)을 인코딩
			nameEncoded = new String(file.getOriginalFilename().getBytes("utf-8"), "8859_1");
			log.info("[FileManagerService: uploadFile()] namedEncoded:{}", nameEncoded);
			
			//URLDecoder.decode(nameEncoded, "UTF-8")이란 반대 방식도 추후 추가해야!

			Path path = Paths.get(filePath + nameEncoded);
			Files.write(path, bytes);
			
			//Thumbnail 생성하여 같은 폴더에 저장
			nameThumbnail = "thumbnail_" + nameEncoded;
			
			BufferedImage imageOrigin = ImageIO.read(new File(filePath + nameEncoded));
			//화질과 해상도 낮춘 thumbnail 
			Thumbnails.of(imageOrigin)
					.size(200, 200)
					.keepAspectRatio(true)
					.allowOverwrite(true)
					.toFile(new File(filePath + nameThumbnail));
		} catch (IOException e) {
			e.printStackTrace();
			// 이미지 업로드 실패
			return null;
		}
		
		List<String> listPathImages = new ArrayList<>();
		String imagePathProfile = "/images/" + directoryName + "/" + nameEncoded;
		String imagePathThumbnale = "/images/" + directoryName + "/" + nameThumbnail;
		
		listPathImages.add(imagePathProfile);
		listPathImages.add(imagePathThumbnale);
		return listPathImages;
	}
	
	public void deleteFile(String imagePath) {
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		if(Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[FileManagerService] Trial to delete file has failed. path:{}", path.toString());
				return;
			}
		}
		
		path = path.getParent();
		if(Files.exists(path)) {
			
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[FileManagerService] Trial to delete directory has failed. path:{}", path.toString());
			}
			
		}
	}
	
	
}
