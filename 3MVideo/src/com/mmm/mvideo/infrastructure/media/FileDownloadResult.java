package com.mmm.mvideo.infrastructure.media;

import java.io.File;
import java.io.Serializable;

/**
 * @author A38C1ZZ
 * 
 */
public class FileDownloadResult implements Serializable {

	private static final long serialVersionUID = -6727976228806966709L;
	public static final int FILE_IS_EXISTED = 0;
	public static final int FILE_DOWNLOAD_SUCC = 1;
	public static final int FILE_DOWNLOAD_FAILED = 2;
	public static final int FILE_IS_DOWNDOADING = 3;
	private int status;
	private File downloadedFile;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public File getDownloadedFile() {
		return downloadedFile;
	}

	public void setDownloadedFile(File downloadedFile) {
		this.downloadedFile = downloadedFile;
	}
}
