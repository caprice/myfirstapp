package com.mmm.mvideo.infrastructure.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.business.service.MMMVideoRetrieveService;

public class HttpDownloader {

	private Context context;
	private URL url = null;
	private static final int TIME_OUT = 10 * 1000;

	public HttpDownloader(Context context) {
		this.context = context;
	}

	/**
	 * read a text file from urlStr and return the whole text content.
	 * 
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(TIME_OUT);
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public FileDownloadResult downFile(MMMVideoItem item, String path) {
		return this.downFile(item, path, null);
	}

	/**
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return -1:file download error 0:file download successfully 1:file is
	 *         existed
	 */
	public FileDownloadResult downFile(MMMVideoItem item, String path, String fileName) {
		FileDownloadResult result = new FileDownloadResult();
		MMMVideoRetrieveService service = new MMMVideoRetrieveService(context);
		String urlStr = item.getUrl();
		boolean isFileDownloading = service.isFileDownloading(urlStr);
		if (isFileDownloading) {
			result.setStatus(FileDownloadResult.FILE_IS_DOWNDOADING);
			return result;
		}
		boolean isDownloadingFileExisted = service.isDownloadingFileExisted(urlStr);
		InputStream inputStream = null;
		if (fileName == null) {
			fileName = FileUtil.unifyFileName(urlStr);
		}
		try {
			if (isDownloadingFileExisted) {
				result.setDownloadedFile(new File(path + fileName));
				result.setStatus(FileDownloadResult.FILE_IS_EXISTED);
				return result;
			}
			// start to download
			service.saveFileDownloadinglog(urlStr);
			inputStream = getInputStreamFromURL(urlStr);
			boolean resultFile = FileUtil.write2SDFromInput(path, fileName, inputStream);
			if (!resultFile) {
				result.setStatus(FileDownloadResult.FILE_DOWNLOAD_FAILED);
			} else {
				item.setLocalPath(path + fileName);
				service.saveDownloadFile(item);
				result.setStatus(FileDownloadResult.FILE_DOWNLOAD_SUCC);
			}
			// delete the downloading log
			service.deleteDownloadingLog(urlStr);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FileDownloadResult.FILE_DOWNLOAD_FAILED);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(TIME_OUT);
			inputStream = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}
}