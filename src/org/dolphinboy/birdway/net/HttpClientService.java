package org.dolphinboy.birdway.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dolphinboy.birdway.entity.GpsData;
import org.dolphinboy.birdway.utils.FileManager;

import android.util.Log;

public class HttpClientService {
	private static final String TAG = "HttpClientService";
	public static SimpleDateFormat DATE_FMT_HALFFULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String REAL_DATA_UPLOAD_URI = "http://birdway.cnodejs.net/mobile/upload?token=";
//	private static final String REAL_DATA_UPLOAD_URI_LOCAL = "http://10.0.2.2:8099/rtdata?token=";
	private static final String REAL_DATA_UPLOAD_URI_LOCAL = "http://192.168.1.104:8099/rtdata?token=";
	
	private static final String useruri = "http://birdway.cnodejs.net/mobile/userid";
	private static final String useruri_test = "http://10.0.2.2:8099/mobile/userid";
	
	/**
	 * 实时坐标上传
	 * @throws Exception 
	 */
	public static int smartSendByGet(String token, GpsData gpsdata) throws Exception {
		Log.i(TAG, "begin smartSendByGet!");
		String jsonstr = FileManager.toJsonObjStr(gpsdata);
		URL url = new URL(REAL_DATA_UPLOAD_URI_LOCAL+token+"&data="+jsonstr);
		Log.i(TAG, "url:"+url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setConnectTimeout(30 * 1000);
		return conn.getResponseCode();
	}
	
	/**
	 * 实时坐标上传
	 * @throws Exception 
	 */
	public static boolean smartSendByGet(String token, Object[] data) throws Exception {
		Date date = new Date();
		String datastr =  DATE_FMT_HALFFULL.format(date);
		String data_json = "{\"lon\":\""+data[0]+"\",\"lat\":\""+data[1]+"\",alt\""+data[2]+"\",\"colt\":\""+data[3]+"\",\"sendt\":\""+datastr+"\"}";
		//这个组JSON的工作还是交给GSON吧
//		String jsonstr = gson.toJson(bakpermlist);
		URL url = new URL(REAL_DATA_UPLOAD_URI+token+"&data="+data_json);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setConnectTimeout(5 * 1000);
		if(conn.getResponseCode()==200){  //或许不需要下面的方法解析内容来得到返回码，可以直接使用头文件中的状态码来表示返回结果
//			InputStream inStream = conn.getInputStream();//通过输入流获取html数据
//			byte[] backdata = readInputStream(inStream);//得到html的二进制数据
//			String backno = new String(backdata);
//			Log.i(TAG, "得到的用户token"+datastr);
			return true;
		}
		return false;
	}
	
	public static String sendPostBackData(Map<String, String> params, String encoding) throws Exception{
		// title=dsfdsf&timelength=23&method=save
		Log.i(TAG, "begin sendPostBackData!");
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), encoding)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//得到实体的二进制数据
		URL url = new URL(useruri);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//如果通过post提交数据，必须设置允许对外输出数据
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		if(conn.getResponseCode()==200){
			InputStream inStream = conn.getInputStream();//通过输入流获取html数据
			byte[] backdata = readInputStream(inStream);//得到html的二进制数据
			String datastr = new String(backdata);
			Log.i(TAG, "得到的用户token"+datastr);
			return datastr;
		}
		return null;
	}
	
	public static boolean sendPost(String path, Map<String, String> params, String enc) throws Exception{
		// title=dsfdsf&timelength=23&method=save
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//得到实体的二进制数据
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//如果通过post提交数据，必须设置允许对外输出数据
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		if(conn.getResponseCode()==200){
			return true;
		}
		return false;
	}
	
	public static String httpGet(String uri) {
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
	
	public static String httpPost (String uri, List<NameValuePair> params, String encoding) {
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
