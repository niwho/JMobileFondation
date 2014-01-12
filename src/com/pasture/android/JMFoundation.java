
package com.pasture.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;

import com.esri.android.map.LayerView;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.pasture.android.JMFoundation;
import com.pasture.android.R;

public class JMFoundation extends Activity {
    /** Called when the activity is first created. */
	JMDataSource					datasource_ = null;
	
	String 							dynamic_url_ = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
//	String 							dynamic_url_ = "http://192.168.1.107/ArcGIS/rest/services/JMobileServer/MapServer";
	MapView 						map_view_ = null;
	ArcGISDynamicMapServiceLayer  	dynamic_layer_ = null;
	int 							layer_id_;
	
	private Button 					buttonZoomIn = null; 
	private Button 					buttonZoomOut = null; 
	
	private static final int 		DIALOG_ABOUT_ID = 1;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.map_view_ = (MapView) findViewById(R.id.map);
		
		this.dynamic_layer_ = new ArcGISDynamicMapServiceLayer(this, this.dynamic_url_);
		
		//Adds layer into the 'MapView'
		this.map_view_.addLayer(this.dynamic_layer_);
		
		this.layer_id_ = 1234;
		this.dynamic_layer_.setId(this.layer_id_);
		
		//
		this.map_view_.setOnLongPressListener(new OnLongPressListener() {
			private static final long serialVersionUID = 1L;

			public void onLongPress(float x, float y) {
				if(map_view_.isLoaded()){
					Log.v(JMFinal.g_tag_foundation_,"Load OK... ");
					Log.v(JMFinal.g_tag_foundation_,"Init Datasource...");
					datasource_ =  new JMDataSource();
					if(datasource_.init(dynamic_url_, dynamic_layer_))
					{
						datasource_.switchPasture("");
						Log.v(JMFinal.g_tag_foundation_,"Init Datasource OK");
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
		
	    subMenu.add(0,5,1,"系统帮助");
	    subMenu.add(0,6,2,"版权声明");
	    
	    //setMenuBackgroud();
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected Dialog onCreateDialog(final int id) {
		Dialog dialog;

		switch (id) {
		case DIALOG_ABOUT_ID:
			return new AlertDialog.Builder(JMFoundation.this).setIcon(R.drawable.icon)
					.setTitle(R.string.app_name).setMessage("该软件归北京农业局拥有")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int whichButton) {
						}
					}).create();

		default:
			dialog = null;
			break;
		}
		return dialog;
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
		    	  showDialog(DIALOG_ABOUT_ID);
		          break;
		      case 7:
		    	 // double lat = 30.581429;//佳丽广场
			     // double lng = 114.285877;
			     // double lat = 30.581429;//佳丽广场
			     // double lng = 114.285877;
			      
		    	 //mapView.addLabel(lng,lat,"佳丽广场",Html.fromHtml("佳丽广场"),R.drawable.poi_2);
		          break;
		    }
		 
		    return true;
	}
}