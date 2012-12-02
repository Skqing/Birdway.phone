package org.dolphinboy.birdway.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.dolphinboy.birdway.entity.GpsData;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FileManager {
	private static final String TAG = "FileManager";
	private static String apphome = Environment.getExternalStorageDirectory().getPath() + "/birdway/";
	private static String ext = ".json";
	
//	lng	Number	地理经度。
//	lat	Number	地理纬度。
	
	public Properties readFile() throws IOException {
		
		
		return null;
	}
	
	/**
	 * 把字符串写入文件
	 * @param filename
	 * @param context
	 * @throws IOException
	 */
	public static void saveFiletoSDCard(String filename, String context) throws IOException {
		String path = Environment.getExternalStorageDirectory().getPath() + "/birdway";
		File file = new File(path, filename);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(context.getBytes());
		fos.close();
	}
	
	/**
	 * 保存GPS数据到SD卡
	 * @param gpsList
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void saveToJSONFile(List<GpsData> gpsList) throws ParseException, IOException {
		String jsonstr = listToObjStr(gpsList);
		Log.i(TAG, "jsonstr:"+jsonstr);
		String filename = DateUtils.formatDate(new Date(), DateUtils.DATE_FMT_HMSS_UU)+ext;
		FileOutputStream fos = null;
		try {
			File floder = new File(apphome);
			if (!floder.exists()) {
				floder.mkdirs();  //如果文件夹不存在，则创建文件夹
			}
			File file = new File(floder, filename);
			Log.i(TAG, "path:"+apphome+filename);
			if (!file.exists()) {
				file.createNewFile();  //如果文件不存在，则创建新文件
			}
			Log.i(TAG, "file create!");
			fos = new FileOutputStream(file);
			Log.i(TAG, "fos");
			fos.write(jsonstr.toString().getBytes());
			Log.i(TAG, "write");
		} catch (IOException e) {
			Log.i(TAG, "导出数据时保存文件出错!");
			Log.e(TAG, e.toString());
		} finally {
			fos.close();
		}
	}
	
	public static String toJsonObjStr(GpsData gpsData) {
		if (gpsData == null) {
			return null;
		}
		StringBuilder jsonstr = new StringBuilder();  //这个方法速度比较快，适不适合放在这里呢？
		jsonstr.append("{\"id\":").append(gpsData.getId()).append(",")
			.append("\"lng\":").append(gpsData.getLongitude()).append(",")
			.append("\"lat\":").append(gpsData.getLatitude()).append(",")
			.append("\"alt\":").append(gpsData.getAltitude()).append(",")
			.append("\"acc\":").append(gpsData.getAccuracy()).append(",")
			.append("\"bear\":").append(gpsData.getBear()).append(",")
			.append("\"speed\":").append(gpsData.getSpeed()).append(",")
			.append("\"gpst\":").append(gpsData.getSpeed()).append(",")
			.append("\"rect\":").append(gpsData.getRecordtime()).append(",")
			.append("\"prov\":").append(gpsData.getProvider()).append("}");
		return jsonstr.toString();
	}
	
	public static String toJsonArrStr(GpsData gpsData) {
		if (gpsData == null) {
			return null;
		}
		StringBuilder jsonstr = new StringBuilder();  //这个方法速度比较快，适不适合放在这里呢？
		//数组顺序为:
		jsonstr.append("[").append(gpsData.getId()).append(",")
			.append(gpsData.getLongitude()).append(",")
			.append(gpsData.getLatitude()).append(",")
			.append(gpsData.getAltitude()).append(",")
			.append(gpsData.getAccuracy()).append(",")
			.append(gpsData.getBear()).append(",")
			.append(gpsData.getSpeed()).append(",")
			.append(gpsData.getSpeed()).append(",")
			.append(gpsData.getRecordtime()).append(",")
			.append(gpsData.getProvider()).append("]");
		return jsonstr.toString();
	}
	
	/**
	 * 把GpsData列表转换为JSON对象字符串
	 * @param gpsList
	 * @return
	 */
	public static String listToObjStr(List<GpsData> gpsList) {
		if (gpsList==null || gpsList.size()==0)
			return null;
		StringBuilder jsonstr = null;
		if (gpsList != null && gpsList.size()>0) {
			jsonstr = new StringBuilder();
			jsonstr.append("[");
			int ino = gpsList.size();
			for (int i=0; i<ino; i++) {
				GpsData gpsdata = gpsList.get(i);
				jsonstr.append("{\"id\":").append(gpsdata.getId()).append(",")
				.append("\"longitude\":").append(gpsdata.getLongitude()).append(",")
				.append("\"latitude\":").append(gpsdata.getLatitude()).append(",")
				.append("\"altitude\":").append(gpsdata.getAltitude()).append(",")
				.append("\"accuracy\":").append(gpsdata.getAccuracy()).append(",")
				.append("\"bear\":").append(gpsdata.getBear()).append(",")
				.append("\"speed\":").append(gpsdata.getSpeed()).append(",")
				.append("\"gpstime\":").append(gpsdata.getGpstime()).append(",")
				.append("\"recordtime\":").append(gpsdata.getRecordtime()).append(",")
				.append("\"provider\":").append(gpsdata.getProvider()).append("}");
			}
			jsonstr.append("]");
		}
		return jsonstr.toString();
	}
	
	public static void main(String args[]) throws ParseException, JSONException {
//		String filename = DateUtils.formatDate(new Date(), DateUtils.DATE_FMT_MS);
//		System.out.println(filename);
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("aa", "aavalue");
		System.out.println(jsonobj.toString());
	}
	
}
