package com.github.colingan.infos.web.kindeditor.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.common.utils.FileUtil;
import com.github.colingan.infos.web.controller.BaseController;

@Controller
public class EditorController extends BaseController {

	// 最大文件大小
	private static final long MAX_FILE_SIZE = 1000000;
	// 允许的文件类型
	private static final Map<String, List<String>> VALID_FILE_EXT = new HashMap<String, List<String>>() {
		{
			put("image", new ArrayList<String>() {
				{
					add("gif");
					add("jpg");
					add("jpeg");
					add("png");
					add("bmp");
				}
			});

			put("file", new ArrayList<String>() {
				{
					add("xls");
					add("xlsx");
					add("et");
					add("txt");
					add("doc");
					add("docx");
				}
			});
		}
	};

	@Value("#[logout.url]")
	protected volatile String logout;

	@RequestMapping(value = "/editor/fileManage.do")
	@ResponseBody
	public Object fileManager(HttpServletRequest request,
			HttpServletResponse response) {
		String rootPath = BizConstants.EDITOR_FILE_PATH;
		String rootUrl = BizConstants.EDITOR_SAVE_URL;

		String dirName = request.getParameter("dir");
		if (dirName != null) {
			if (VALID_FILE_EXT.containsKey(dirName)) {
				return "invalid directory name find. " + dirName;
			}
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}

		String path = request.getParameter("path");
		path = path == null ? "" : path;
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0,
					currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0,
					str.lastIndexOf("/") + 1) : "";
		}
		// 排序形式 name or size or type
		String order = request.getParameter("order");
		order = order == null ? "name" : order.toLowerCase();

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			return "Access is not allowed.";
		}

		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			return "parameter is not valid. - " + path;
		}

		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			return "Directory does not exist. - " + currentPath;
		}

		// 遍历目录获取文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo",
							VALID_FILE_EXT.get("image").contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				fileList.add(hash);
			}
		}

		// sort
		if ("size".equals(order)) {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					Hashtable hashA = (Hashtable) o1;
					Hashtable hashB = (Hashtable) o2;
					if (((Boolean) hashA.get("is_dir"))
							&& !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir"))
							&& ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						if (((Long) hashA.get("filesize")) > ((Long) hashB
								.get("filesize"))) {
							return 1;
						} else if (((Long) hashA.get("filesize")) < ((Long) hashB
								.get("filesize"))) {
							return -1;
						} else {
							return 0;
						}
					}
				}

			});
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					Hashtable hashA = (Hashtable) o1;
					Hashtable hashB = (Hashtable) o2;
					if (((Boolean) hashA.get("is_dir"))
							&& !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir"))
							&& ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						return ((String) hashA.get("filetype"))
								.compareTo((String) hashB.get("filetype"));
					}
				}

			});
		} else {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					Hashtable hashA = (Hashtable) o1;
					Hashtable hashB = (Hashtable) o2;
					if (((Boolean) hashA.get("is_dir"))
							&& !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir"))
							&& ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						return ((String) hashA.get("filename"))
								.compareTo((String) hashB.get("filename"));
					}
				}

			});
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);

		return result;
	}

	@RequestMapping(value = "/editor/upload.do")
	@ResponseBody
	public Map<String, Object> upload(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!VALID_FILE_EXT.containsKey(dirName)) {
			LOGGER.error("editor upload dir invalid." + dirName);
			return getError("editor upload dir invalid.");
		}

		// 创建文件夹
		String savePath = BizConstants.EDITOR_FILE_PATH + dirName;
		String saveUrl = "";
		if ("image".equals(dirName)) {
			saveUrl = BizConstants.EDITOR_SAVE_URL + dirName + "/";
		} else {
			saveUrl = BizConstants.EDITOR_DOWNLOAD_URL + "?file=" + dirName
					+ "/";
		}
		File path = new File(savePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
		String date = format.format(new Date());

		path = new File(path, date);
		if (!path.exists()) {
			path.mkdirs();
		}
		saveUrl += date + "/";
		Map<String, MultipartFile> fileMap = request.getFileMap();
		if (fileMap == null || fileMap.isEmpty()) {
			LOGGER.error("no file select.");
			return getError("no file find.");
		}
		SimpleDateFormat completeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Random random = new Random(System.currentTimeMillis());
		for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			String fileName = entry.getValue().getOriginalFilename();
			long fileSize = entry.getValue().getSize();
			if (fileSize > MAX_FILE_SIZE) {
				return getError("file size should less than " + MAX_FILE_SIZE
						+ ", current is : " + fileSize);
			}
			String ext = FileUtil.getExtension(fileName);
			if (!VALID_FILE_EXT.get(dirName).contains(ext.toLowerCase())) {
				return getError("file extension not allowed." + ext);
			}
			String dest = completeFormat.format(new Date()) + "_"
					+ random.nextInt(1000) + "." + ext.toLowerCase();
			try {
				entry.getValue().transferTo(new File(path, dest));
			} catch (Exception e) {
				return getError("failed to save file. " + fileName);
			}

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("error", 0);
			model.put("url", saveUrl + dest);

			return model;
		}

		return null;
	}

	@RequestMapping(value = "/editor/download.do")
	public void downloadEditorAttachment(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String uri = BizConstants.EDITOR_FILE_PATH
				+ request.getParameter("file");
		String ext = FileUtil.getExtension(uri);
		if (!VALID_FILE_EXT.get("file").contains(ext)) {
			Writer os = new PrintWriter(response.getOutputStream());
			os.write("文件类型不支持！ - " + ext);
			os.flush();
			os.close();
			return;
		}

		int idx = uri.lastIndexOf(BizConstants.SLASH);
		String fileName = uri.substring(idx + 1);
		String path = uri.substring(0, idx);
		super.generalFileDownload(request, response, path, fileName);
	}

	private Map<String, Object> getError(String message) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", 1);
		model.put("message", message);
		return model;
	}

	@Override
	protected String getLogout() {
		return this.logout;
	}

}
