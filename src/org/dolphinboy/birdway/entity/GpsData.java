package org.dolphinboy.birdway.entity;

import java.io.Serializable;

/**
 * GPS实体类
 * @author DolphinBoy
 *
 */
public class GpsData implements Serializable {
	public final static int PROVIDER_GPS = 0;
	public final static int PROVIDER_NET = 1;
	public final static int PROVIDER_APN = 2;
	
	private Integer id;
	private double longitude;    //经度
	private double latitude;     //纬度
	private double altitude;     //海拔
	private float accuracy;      //精确度
	private float bear;          //偏离正北方的度数
	private float speed;         //速度
	private long gpstime;        //GPS时间
	private long recordtime;    //采集时间，(new Date()).getTime();要能够得到当地时间，并且能转换为ISODate
//	private String sendtime;      //发送时间--不在本地持久化 (暂时意义不大)
	private int provider;       //位置提供者，默认为GPS=0
	private int state = 0;       //标识此条数据是否已经上传到服务
	
	public int getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	public float getBear() {
		return bear;
	}
	public void setBear(float bear) {
		this.bear = bear;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public long getGpstime() {
		return gpstime;
	}
	public void setGpstime(long gpstime) {
		this.gpstime = gpstime;
	}
	public long getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(long recordtime) {
		this.recordtime = recordtime;
	}
	//	public String getSendtime() {
//		return sendtime;
//	}
//	public void setSendtime(String sendtime) {
//		this.sendtime = sendtime;
//	}
	public int getProvider() {
		return provider;
	}
	public void setProvider(int provider) {
		this.provider = provider;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
