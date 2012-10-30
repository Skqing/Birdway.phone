package org.dolphinboy.birdway.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageRequest {

	public static byte[] getImage(String path) throws Exception {
		URL url = new URL("http://i3.itc.cn/20100707/76c_0969b700_d5b4_41cd_8243_9b486be92cc4_0.jpg");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
		return StreamTool.readInputStream(inStream);//得到图片的二进制数据
	}
	
	static class StreamTool {
		/**
		 * 从输入流中获取数据
		 * @param inStream 输入流
		 * @return
		 * @throws Exception
		 */
		public static byte[] readInputStream(InputStream inStream) throws Exception{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len=inStream.read(buffer)) != -1 ){
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			return outStream.toByteArray();
		}
	}
	
}
