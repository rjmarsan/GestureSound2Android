package com.rj.research.uiuc.gesturesound.android;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class FileClient2 {
	public static interface FileClientCallback {
		public void fileStartedUpload(File f);
		public void fileFinishedUpload(File f);
		public void fileStartedDownload(File f);
		public void fileFinishedDownload(File f);
	}

	
	public FileClientCallback callback;
	public FileClient2(FileClientCallback callback) {
		//upload(31337, new File("testfile.txt"));
		this.callback = callback;

	}
	
	public void uploadAndDownload(String address, final int port, File infile, File outfile) {
		try {
		    Socket sock = new Socket(address,port);
		    System.out.println("CLIENT: Starting connection NOW");
		    InputStream is = sock.getInputStream();
		    OutputStream os = sock.getOutputStream();
		    uploadedFile(os, infile);
		    System.out.println("CLIENT: upload complete. downloading");
		    downloadFile(is, outfile);
			System.out.println("CLIENT: Closing socket. ");
			is.close();
			os.close();
		    sock.close();
			System.out.println("CLIENT: Finishing connection NOW");
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void uploadedFile(OutputStream os, File f)  throws Exception {
		if (callback != null) callback.fileStartedUpload(f);
		System.out.println("CLIENT[UPLOAD]: New file: "+f);
		ByteStream.toStream(os, f);
		System.out.println("CLIENT[UPLOAD]: upload complete");
		if (callback != null) callback.fileFinishedUpload(f);
	}
	
	public void downloadFile(InputStream is, File file)  throws Exception {
		if (callback != null) callback.fileStartedDownload(file);
		System.out.println("CLIENT [DOWNLOAD]: New file: "+file);
		ByteStream.toFile(is, file);
		System.out.println("CLIENT [DOWNLOAD]: download complete");
		if (callback != null) callback.fileFinishedDownload(file);
	}

	
}

