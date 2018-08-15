package com.hex.express.iwant.views;

import android.content.Context;
import android.view.LayoutInflater;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.hex.express.iwant.R;

public class PersonInfoWindow {
	private InfoWindow infoWindow;

	public PersonInfoWindow(Context paramContext, Marker paramMarker,
			InfoWindow.OnInfoWindowClickListener paramOnInfoWindowClickListener) {
		this.infoWindow = new InfoWindow(
				BitmapDescriptorFactory.fromView(LayoutInflater.from(
						paramContext).inflate(R.layout.personinfowindow, null)),
				paramMarker.getPosition(), -47, paramOnInfoWindowClickListener);
	}

	public InfoWindow getInfoWindow() {
		return this.infoWindow;
	}

	public void setInfoWindow(InfoWindow paramInfoWindow) {
		this.infoWindow = paramInfoWindow;
	}
}

/*
 * Location: C:\Users\zl\Desktop\expressV1.4_build18_dex2jar.jar Qualified Name:
 * com.hex.express.baidumap.PersonInfoWindow JD-Core Version: 0.6.0
 */