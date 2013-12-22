package com.mucao.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.esri.android.map.LayerView;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.mucao.android.JMFoundation;
import com.mucao.android.R;

public class JMFoundation extends Activity {
    /** Called when the activity is first created. */
	private MapView map = null;
	//Dynamic layer URL from ArcGIS online
	String dynamicMapURL = "http://192.168.1.102/ArcGIS/rest/services/mucao/MapServer";
	//String dynamicMapURL = "http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer";
	//Layer id for dynamic layer
	
	int usaLayerId;
	
	private Button buttonZoomIn = null; 
	private Button buttonZoomOut = null; 


	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.map = (MapView) findViewById(R.id.map);
		
		//Creates a dynamic layer using service URL 
		ArcGISDynamicMapServiceLayer dynamicLayer = new ArcGISDynamicMapServiceLayer(
				this, this.dynamicMapURL);
		//Adds layer into the 'MapView'
		this.map.addLayer(dynamicLayer);
		this.usaLayerId = 1234;
		dynamicLayer.setId(this.usaLayerId);
		
		this.buttonZoomIn = (Button) findViewById(R.id.buttonZoomIn); 
		this.buttonZoomOut = (Button) findViewById(R.id.buttonZoomOut); 
		
		this.buttonZoomIn.setOnClickListener(new OnClickListener(){ 
			public void onClick(View v) {
				JMFoundation.this.map.zoomin(); 
			}
		}); 
		 
		this.buttonZoomOut.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) {
				JMFoundation.this.map.zoomout(); 
		 } 
		 }); 

		//Sets 'OnSingleTapListener' to 'MapView'
		this.map.setOnSingleTapListener(new OnSingleTapListener() {

			private static final long serialVersionUID = 1L;

			
			public void onSingleTap(float x, float y) {
				//Determines if the map is loaded
				if (JMFoundation.this.map.isLoaded()) {

					//Retrieves the dynamic layer
					LayerView<?> layer = JMFoundation.this.map.getLayerById(JMFoundation.this.usaLayerId);

					//Toggles the dynamic layer's visibility
					if (layer.getVisibility() == ViewGroup.VISIBLE) {
						layer.setVisibility(ViewGroup.INVISIBLE);
					} else {
						layer.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
		});
		

	}
}