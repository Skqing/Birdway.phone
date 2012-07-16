package org.dolphinboy.birdway.entity;

import java.io.Serializable;
import java.util.Date;

public class GPSData implements Serializable {
	public int datatype;     //数据类型
	public double latitude;     //纬度
	public double longitude;    //经度
	public double altitude;  //海拔
	public float bear;      //偏离正北方的度数
	public double speed;     //速度
	public String gpstime;     //GPS时间
	
	public int getDatatype() {
		return datatype;
	}
	public void setDatatype(int datatype) {
		this.datatype = datatype;
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
	public float getBear() {
		return bear;
	}
	public void setBear(float bear) {
		this.bear = bear;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public String getGpstime() {
		return gpstime;
	}
	public void setGpstime(String gpstime) {
		this.gpstime = gpstime;
	}
}
