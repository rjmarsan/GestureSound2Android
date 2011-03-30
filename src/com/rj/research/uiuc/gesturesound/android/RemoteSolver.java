package com.rj.research.uiuc.gesturesound.android;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

import wekinator.controller.WekinatorManager;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;


public class RemoteSolver {
	public final static String ENDPOINT = "http://10.0.0.7:5000";
//	public final static int ENDPOINT_PORT = 5555;
	public final static String UPLOAD_ACTION = "/upload";
	public final static String FTP_ENDPOINT = "10.0.0.7";
	public final static int FTP_PORT = 21;

	public WekinatorManager solve(WekinatorManager weki, Context context) throws Exception {
		System.out.println("Starting remote solver");
		File savefolder = new File(context.getCacheDir(), "training"+weki.hashCode());
		File oldsave = weki.getSaveDir();
		System.out.println("saving untrained weki");
		weki.save(savefolder);
		weki.setSaveDir(oldsave);
		System.out.println("finished saving untrained weki, zipping");
		File savezip = new File(context.getCacheDir(), "training"+weki.hashCode()+".zip");
		File outsavezip = new File(context.getCacheDir(), "training__solved.zip");
		zip(savefolder, savezip);
		System.out.println("finished zipping untrained weki");
		System.out.println("uploading...");
		String checkurl = upload(savezip, ENDPOINT+UPLOAD_ACTION);
		System.out.println("complete.");
		
		String outurl = null;
		while (outurl == null && checkurl != null) {
			System.out.println("polling.");
			outurl = checkStatus(ENDPOINT+checkurl);
			Thread.sleep(1000);
		}
		System.out.println("training complete! downloading...");
		downloadZip(ENDPOINT+outurl, outsavezip);
		System.out.println("download complete! unzipping again");
		unzip(outsavezip, savefolder);
		System.out.println("Unzip complete. creating new weki with current params...");
		WekinatorManager wekinew =  new WekinatorManager(savefolder);
		wekinew.setSaveDir(oldsave);
		return wekinew;
	}
	
	private String upload(File file, String url) throws Exception {
		System.out.println("Uploading...");
		ftpUpload(FTP_ENDPOINT, FTP_PORT, "rj", "", "~/Documents/py/rjresearchserv/static/files/", file);
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(new URI(url+"/"+file.getName().substring(0, file.getName().length()-4)));
        try{
            HttpResponse response = client.execute(request);
            String s =  request(response);
    		JSONObject obj = new JSONObject(s);
    		if (obj.getBoolean("status") ) {
    			return obj.getString("checkurl");
    		}
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        return null;

	}
	
	
	
    public String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        Log.d("RemoteSolver", "Result: "+result);
        return result;
    }


	private String checkStatus(String url) throws URISyntaxException {
        HttpClient client = new DefaultHttpClient();
        System.out.println("Check status url: "+url);
        HttpGet request = new HttpGet(new URI(url));
        try{
            HttpResponse response = client.execute(request);
            String s =  request(response);
    		JSONObject obj = new JSONObject(s);
    		if (obj.getBoolean("status") ) {
    			return obj.getString("url");
    		}
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        return null;
	}
	
	private File downloadZip(String url, File file) throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient http = AndroidHttpClient.newInstance("MyApp");
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse response = http.execute(method);	
        InputStream in = response.getEntity().getContent();
        FileOutputStream fileout = new FileOutputStream(file);
        int len1 = 0;
        byte[] buffer = new byte[1024];
        while ( (len1 = in.read(buffer)) > 0 ) {
            fileout.write(buffer,0, len1);
        }
        fileout.close();
		return file;
	}
	

	private void zip(File indir, File outfile) {
		// These are the files to include in the ZIP file
		String[] filenames = indir.list();
		System.out.println("WRiting to "+outfile+"   files: ");
		for (int i=0;i<filenames.length;i++) {
			System.out.println("    "+new File(indir,filenames[i]).getPath());
		}

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
		    // Create the ZIP file
//		    String outFilename = "outfile.zip";
		    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outfile));

		    // Compress the files
		    for (int i=0; i<filenames.length; i++) {
		    	File f = new File(indir,filenames[i]);
		    	if (!f.exists())
		    		System.out.println("Error! "+f+" does not exist!");
		        FileInputStream in = new FileInputStream(f);

		        // Add ZIP entry to output stream.
		        out.putNextEntry(new ZipEntry(filenames[i]));

		        // Transfer bytes from the file to the ZIP file
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }

		        // Complete the entry
		        out.closeEntry();
		        in.close();
		    }

		    // Complete the ZIP file
		    out.close();
		} catch (IOException e) {
			System.out.println("Error!!!!!!!!!!");
			e.printStackTrace();
		}		
	}


	private void unzip(File zipfile, File outdir) {
		try {
			ZipFile zip;
			System.out.println("making "+zipfile+" into:zipfile "+zipfile.canRead()+" "+zipfile.exists());
			zip = new ZipFile(zipfile);
			unzipFileIntoDirectory(zip, outdir);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
	public static void unzipFileIntoDirectory(ZipFile zipFile,
			File jiniHomeParentDir) {
		Enumeration files = zipFile.entries();
		File f = null;
		FileOutputStream fos = null;
		System.out.println("Unzipping "+zipFile+" into:"+jiniHomeParentDir);
		while (files.hasMoreElements()) {
			try {
				ZipEntry entry = (ZipEntry) files.nextElement();
				InputStream eis = zipFile.getInputStream(entry);
				byte[] buffer = new byte[1024];
				int bytesRead = 0;

				f = new File(jiniHomeParentDir.getAbsolutePath()
						+ File.separator + entry.getName());

				if (entry.isDirectory()) {
					f.mkdirs();
					continue;
				} else {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}

				fos = new FileOutputStream(f);

				while ((bytesRead = eis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}
	
	private void ftpUpload(String location, int port, String username, String passwd, String path, File file) {
        FTPClient con = new FTPClient();
        try
        {
        	System.out.println("Running ftp client");
            con.connect(location, port);
            if (con.login(username, passwd))
            {
                con.enterLocalPassiveMode(); // important!
                FileInputStream in = new FileInputStream(file);
                boolean result = con.storeFile(path+file.getName(), in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        try
        {
            con.logout();
            con.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

	}

}
