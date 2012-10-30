package org.dolphinboy.birdway.entity;

/**
 * GPS实体类
 * @author DolphinBoy
 *
 */
public class GpsData extends BaseEntity {
	public double latitude;     //纬度
	public double longitude;    //经度
	public double altitude;  //海拔
	public float bear;      //偏离正北方的度数
	public float speed;     //速度
	public String gpstime;     //GPS时间
	
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
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public String getGpstime() {
		return gpstime;
	}
	public void setGpstime(String gpstime) {
		this.gpstime = gpstime;
	}
}
