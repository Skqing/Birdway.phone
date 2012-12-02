package org.dolphinboy.birdway.activity;

import java.util.ArrayList;
import java.util.List;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.comps.BirdwayAppalication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
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
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.PoiOverlay;

public class BaiduMapActivity extends MapActivity {
	private static final String TAG = "BaiduMapActivity";
	
	private MapView mapview;
	private BMapManager bmapmanager;
	//添加一些控件
	private MapController mapctrl = null;
	
	//位置、周边、范围、公交、驾乘、
	private MKSearch mksearch = null;
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		super.setContentView(R.layout.baidumap);
//		myself = BaiduMapActivity.this;
		
		mapview = (MapView) this.findViewById(R.id.bmapview);
		//初始化百度地图管理器
		bmapmanager = new BMapManager(this);
		BirdwayAppalication app = (BirdwayAppalication)this.getApplication();
		//初始化地图
		bmapmanager.init(BirdwayAppalication.bmapkey, new MKGeneralListener() {  //初始化事件监听
			@Override
			public void onGetNetworkState(int ps) {
//				if (ps == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(BaiduMapActivity.this, ":-( 网络连接出错啦!", Toast.LENGTH_SHORT).show();
//				}
			}
			@Override
			public void onGetPermissionState(int ns) {  //这里要处理一下
				if (ns == MKEvent.ERROR_PERMISSION_DENIED) {
					Toast.makeText(BaiduMapActivity.this, "此应用的Key授权失败,请联系开发人员!", 1).show();
				}
			}
		});
//		bmapmanager.start();
		super.initMapActivity(bmapmanager);  //初始化activity
		mapview.setBuiltInZoomControls(true);  //启用内置缩放控件
		//设置在缩放动画过程中也显示overlay,默认为不绘制
		mapview.setDrawOverlayWhenZooming(true);
		
		//用给定的经纬度构造一个 GeoPoint,单位是微度 (度 * 1E6)
		//默认为北京，其实默认值最坏应该从配置参数中或者数据库中查询中查询
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
		mapctrl = mapview.getController();  //得到控制器,可以用它控制和驱动平移和缩放
		mapctrl.setCenter(gpoint);  //设置地图中心点 
		mapctrl.setZoom(12);  //设置地图 zoom级别
//		mapview.setOnTouchListener(new OnTouchListener() {  //这个方法会让地图的移动和缩放功能失效，并且得到的不是一个点，而是一个范围
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			GeoPoint gpoint = mapview.getProjection().fromPixels(v.getLeft(), v.getTop());
//			Log.i(TAG, "点击界面得到的坐标——>经度："+gpoint.getLongitudeE6()+"#纬度："+gpoint.getLatitudeE6());
//			return true;
//		}
//	});
		//添加覆盖物
		mapview.getOverlays().add(new MyselfOverlay(gpoint));
		
		
		/**
		mapview.setTraffic(true);  //显示交通状况
		mapview.setSatellite(true);  //显示卫星地图
		
		//实例化化检索工具对象
		mksearch = new MKSearch();
		mksearch.init(bmapmanager, new MapSearchListener());
		//1.关键字，2.中心点，3.范围半径
		mksearch.poiSearchNearBy("KFC", new GeoPoint((int) (39.915*1E6), (int) (116.404*1E6)), 5000);
		*/
	}

	//覆盖物——自己的位置
	private class MyselfOverlay extends Overlay {
		//覆盖物所在地点
		private GeoPoint gpoint = null;
		private Paint paint = new Paint();  //创建一个画笔工具
		
		public MyselfOverlay(GeoPoint gpoint) {
			super();
			this.gpoint = gpoint;
		}
		@Override
		public void draw(Canvas canvas, MapView mapview, boolean bln) {
			super.draw(canvas, mapview, bln);
			Point point = mapview.getProjection().toPixels(gpoint, null);
			canvas.drawText("*这里是天安门", point.x, point.y, paint);
		}
		
	}
	//覆盖物——其他人的位置
	private class OthersOverlay extends ItemizedOverlay<OverlayItem> {
		private List<GeoPoint> gPointList = null;
		public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OthersOverlay(Drawable draw, List<GeoPoint> gPointList) {
			super(draw);
			this.gPointList = gPointList;
		}

		public OthersOverlay(Drawable draw) {
			super(draw);
		}

		@Override
		protected OverlayItem createItem(int itemno) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
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
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			
		}
		Log.i(TAG, "保存activity状态");
		super.onSaveInstanceState(outState);
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
		if (bmapmanager != null) {  //销毁地图管理器
			bmapmanager.destroy();
			bmapmanager = null;
		}
		super.onDestroy();
	}

	@Override
	public void onNewIntent(Intent arg0) {
		// TODO Auto-generated method stub
		super.onNewIntent(arg0);
	}
	@Override
	protected void onPause() {
		if (bmapmanager != null) {  //暂停地图管理器
			bmapmanager.stop();
		}
		super.onPause();
	}
	@Override
	protected void onResume() {
		if (bmapmanager != null) {  //重启地图管理器
			bmapmanager.start();
		}
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	//添加菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 11, 1, "数据");
//		menu.add(0, 12, 2, "关闭");
//		menu.add(0, 13, 3, "退出");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	//处理菜单选中事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_data:  //启动数据管理页面
			Intent intent = new Intent(BaiduMapActivity.this, DataManageActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_close:  //关闭activity但是不退出后台服务
			this.finish();
			break;
		case R.id.menu_exit:  //退出后台服务
			break;
		}
		return true;
	}

}
