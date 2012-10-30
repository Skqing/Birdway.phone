package org.dolphinboy.birdway.activity;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.service.BirdwayService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.PoiOverlay;

public class BaiduMapActivity extends MapActivity {
	private static final String TAG = "BaiduMapActivity";
	
	private MapView mapview;
	private BMapManager mapmanager;
	private String mapkey = "9fe961116643c046c354aa0add050d59";
	//添加一些控件
	private MapController mapcontroller;
	
	//位置、周边、范围、公交、驾乘、
	private MKSearch mksearch;
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.baidumap);
//		myself = BaiduMapActivity.this;
		//启动项目后台服务
//		Intent intent = new Intent(context, BirdwayService.class);
//		startService(intent);
		
		mapview = (MapView) this.findViewById(R.id.bmapView);
		//初始化百度地图管理器
		mapmanager = new BMapManager(this);
		//初始化地图
		mapmanager.init(mapkey, new MKGeneralListener() {  //监听授权状态
			@Override
			public void onGetNetworkState(int ps) {
				if (ps == 300) {
					Toast.makeText(BaiduMapActivity.this, "此应用的Key授权失败，请联系开发人员!", 1).show();
				}
			}

			@Override
			public void onGetPermissionState(int ns) {
				
			}
		});
		initMapActivity(mapmanager);  //初始化activity
		mapview.setBuiltInZoomControls(true);
		mapcontroller = mapview.getController();  //得到控制器
		//设置初始化中心点
		GeoPoint gpoint = new GeoPoint((int) (39.915*1E6), (int) (116.404*1E6));
		
		
		//用我上一次的位置初始化中心点
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			gpoint.setLatitudeE6((int) (location.getLatitude()*1E6));
			gpoint.setLongitudeE6((int) (location.getLongitude()*1E6));
		}
//		Toast.makeText(context, "GPS已关闭，请开启GPS！", Toast.LENGTH_SHORT).show();
		/**
		mapcontroller.setCenter(gpoint);
		mapcontroller.setZoom(12);
		
		mapview.setTraffic(true);  //显示交通状况
		mapview.setSatellite(true);  //显示卫星地图
		
		//添加覆盖物
		mapview.getOverlays().add(new MapOverlay());
		
		//实例化化检索工具对象
		mksearch = new MKSearch();
		mksearch.init(mapmanager, new MapSearchListener());
		//1.关键字，2.中心点，3.范围半径
		mksearch.poiSearchNearBy("KFC", new GeoPoint((int) (39.915*1E6), (int) (116.404*1E6)), 5000);
		*/
		//启动后台服务
		Intent intent = new Intent(this, BirdwayService.class);
		startService(intent);
	}

	//生成覆盖物
	private class MapOverlay extends Overlay {
		//覆盖物所在地点
		private GeoPoint gpoint = new GeoPoint((int) (39.915*1E6), (int) (116.404*1E6));
		private Paint paint = new Paint();  //创建一个画笔工具
		@Override
		public void draw(Canvas canvas, MapView mapview, boolean bln) {
			super.draw(canvas, mapview, bln);
			Point point = mapview.getProjection().toPixels(gpoint, null);
			canvas.drawText("*这里是天安门", point.x, point.y, paint);
		}
		
		
		
	}
	
	private class MapSearchListener implements MKSearchListener {
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			
		}
		
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			
		}
		
		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			
		}
		
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int error) {
			if (result == null) {
				return;
			}
//			PoiOverlay poioverlay = new PoiOverlay((Activity) context, mapview);
			PoiOverlay poioverlay = new PoiOverlay(BaiduMapActivity.this, mapview);
			poioverlay.setData(result.getAllPoi());
			mapview.getOverlays().add(poioverlay);
			mapview.invalidate();  //刷新地图
		}
		
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int arg1) {
			
		}
		
		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			
		}
		
		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			
		}
	}
	
	@Override
	public boolean initMapActivity(BMapManager arg0) {
		// TODO Auto-generated method stub
		return super.initMapActivity(arg0);
	}

	@Override
	protected boolean isLocationDisplayed() {
		// TODO Auto-generated method stub
		return super.isLocationDisplayed();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mapmanager != null) {  //销毁地图管理器
			mapmanager.destroy();
			mapmanager = null;
		}
	}

	@Override
	public void onNewIntent(Intent arg0) {
		// TODO Auto-generated method stub
		super.onNewIntent(arg0);
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (mapmanager != null) {  //暂停地图管理器
			mapmanager.stop();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (mapmanager != null) {  //重启地图管理器
			mapmanager.start();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}

}
