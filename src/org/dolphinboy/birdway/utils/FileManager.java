package org.dolphinboy.birdway.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.dolphinboy.birdway.R;

import android.os.Environment;

public class FileManager {

	public Properties readFile() throws IOException {
		
		
		return null;
	}
	
	public void saveFile() throws IOException {
		
	}
	
	/**
	 * 把字符串写入文件
	 * @param filename
	 * @param context
	 * @throws IOException
	 */
	public void saveFiletoSDCard(String filename, String context) throws IOException {
		String path = Environment.getExternalStorageDirectory().getPath() + R.string.app_name;
		File file = new File(path, filename);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(context.getBytes());
		fos.close();
	}
}
