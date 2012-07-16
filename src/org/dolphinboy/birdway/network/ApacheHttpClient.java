package org.dolphinboy.birdway.network;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class ApacheHttpClient {

	public String httpGet(String uri) {
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();
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
		HttpClient httpclient = new DefaultHttpClient();
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
}
