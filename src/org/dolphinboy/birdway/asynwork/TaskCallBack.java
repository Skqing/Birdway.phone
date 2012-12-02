package org.dolphinboy.birdway.asynwork;


/**
 * 
 * @author DolphinBoy
 *
 */
public interface TaskCallBack {
	/**
	 * 注入数据
	 * @param gpsdata
	 */
	public void gpsConnected(Object data);
	
	/**
	 * 请求超时
	 */
	public void gpsConnectedTimeOut();
}
