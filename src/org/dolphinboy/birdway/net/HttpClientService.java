package org.dolphinboy.birdway.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientService {
	private static final String uploaddatauri = "http://birdway.cnodejs.net/mobile/upload?user=dolphinboy&data=";
	private static final String uploaddatauri_test = "http://192.168.0.100/mobile/upload?data=";
	/**
	 * 实时坐标上传
	 * @throws Exception 
	 */
	public void smartHttpGet(String sdata) throws Exception {
		URL url = new URL(uploaddatauri+sdata);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();//通过输入流获取html数据
		byte[] data = readInputStream(inStream);//得到html的二进制数据
		String html = new String(data);
		System.out.println(html);
	}
	
	public String httpGet(String uri) {
		String response = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int statuscode = httpresponse.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpresponse.getEntity());
			} else {
				response = "" + statuscode;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}
	
	public String httpPost (String uri, List<NameValuePair> params, String encoding) {
		String response = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(uri);
		encoding = encoding==null?HTTP.UTF_8:encoding;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, encoding));
			HttpResponse httpresponse = httpclient.execute(httppost);
			int statuscode = httpresponse.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpresponse.getEntity());
			} else {
				response = "" + statuscode;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}
	
	/**
	 * 把输入流转换为字节流
	 * @param inStream
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
