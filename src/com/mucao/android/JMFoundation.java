package com.mucao.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
	
    //添加menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem findItem = menu.add(0,0,0,R.string.find);
		findItem.setIcon(R.drawable.ic_menu_search);
		
		MenuItem positionItem = menu.add(0,1,1,R.string.gpslogging);
		positionItem.setIcon(R.drawable.ic_menu_gps);
		
		MenuItem queryItem = menu.add(0,2,2,R.string.query);
		queryItem.setIcon(R.drawable.ic_menu_directions);
		
		SubMenu subMenu=menu.addSubMenu(0,3,3,R.string.more);
		subMenu.setIcon(android.R.drawable.ic_menu_more);
		
	    subMenu.add(0,4,0,"GPS设置");
	    subMenu.add(0,5,1,"系统帮助");
	    subMenu.add(0,6,2,"版权声明");
	    subMenu.add(0,7,3,"添加标注");
	    
	    //setMenuBackgroud();
		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		 switch(item.getItemId())
		    {
		      case 0:
		    	  //搜索设置
		         //SearchView search = new SearchView(this,this.mapView);
		         ///search.ShowQueryWin();
		         
		        break;
		      case 1:
		    	  //GPS跟踪
		    	    //Intent intent = new Intent(this, ControlTracking.class);
		            //startActivity(intent);
				
		        break;
		      case 2:
		    	  //路线查询
		    	    //Intent intent1 = new Intent(this, GoogleMapRoutes.class);
		            //startActivity(intent1);
		    	  
		    	 // mapView.removeLocationOverlay(overlay);
		          break;
		      case 3:
		          
		          break;
		      case 4:
		    	  
		    	  
		          break;
		      case 5:
		    		
		          break;
		      case 6:
		      {}
		  		
		          break;
		      case 7:
		    	 // double lat = 30.581429;//佳丽广场
			     // double lng = 114.285877;
			      double lat = 30.581429;//佳丽广场
			      double lng = 114.285877;
			      
		    	  //mapView.addLabel(lng,lat,"佳丽广场",Html.fromHtml("佳丽广场"),R.drawable.poi_2);
		          break;
		    }
		    return true;
	}
	
}