
package com.pasture.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.pasture.android.JMFoundation;
import com.pasture.android.R;

public class JMFoundation extends Activity {
    /** Called when the activity is first created. */
	JMDataSource					datasource_ = null;
	List<String> 					pasture_name_list = null;

	String 							dynamic_url_ = "http://192.168.1.107/ArcGIS/rest/services/JMobileServer/MapServer";
	
	MapView 						map_view_ = null;
	ArcGISDynamicMapServiceLayer  	dynamic_layer_ = null;
	int 							layer_id_;
	
	private static final int 		DIALOG_ABOUT_ID = 1;
	
	Button layer_btn = null;
	Button query_btn = null;
	
	EditText txtQueryString = null;
	
	GraphicsLayer mGraphicsLayer;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.map_view_ = (MapView) findViewById(R.id.map);
		
		this.layer_btn = (Button) findViewById(R.id.layers);
		this.query_btn = (Button) findViewById(R.id.places);
		
		this.txtQueryString = (EditText) findViewById(R.id.search_edit);
		
		this.dynamic_layer_ = new ArcGISDynamicMapServiceLayer(this.dynamic_url_);
		
		this.map_view_.addLayer(this.dynamic_layer_);
		
		this.map_view_.setOnStatusChangedListener(new OnStatusChangedListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public void onStatusChanged(Object source, STATUS status) {
                if (status.equals(STATUS.INITIALIZATION_FAILED)) {
                	Log.v(JMFinal.g_tag_foundation_,"INITIALIZATION_FAILED");
                }
                ;

                if (status.equals(STATUS.INITIALIZED)) {
                    Log.v(JMFinal.g_tag_foundation_,"INITIALIZED");
                }
                ;
                if (status.equals(STATUS.LAYER_LOADED)) {
                    if(map_view_.isLoaded()){
    					Log.v(JMFinal.g_tag_foundation_,"Load OK... ");
    					Log.v(JMFinal.g_tag_foundation_,"Init Datasource...");
    					datasource_ =  new JMDataSource();
    					if(datasource_.init(dynamic_url_, dynamic_layer_))
    					{
    						pasture_name_list = datasource_.getAllPastureName();
							Log.v(JMFinal.g_tag_foundation_,"Pasture Count: "+pasture_name_list.size());
    						if(pasture_name_list.size() > 0)
    							datasource_.switchPasture(pasture_name_list.get(0));
    						
    						Log.v(JMFinal.g_tag_foundation_,"Init Datasource OK");
    					}					
    				}
                }
                ;

                if (status.equals(STATUS.LAYER_LOADING_FAILED)) {
                    Log.v(JMFinal.g_tag_foundation_,"图层加载失败");
                }
                ;
            }
        });
		
		this.layer_btn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					
					JMAdapterView adapter = new JMAdapterView(JMFoundation.this, pasture_name_list);
					new AlertDialog.Builder(JMFoundation.this)
						.setTitle("图层切换")
						.setAdapter(adapter,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								datasource_.switchPasture(pasture_name_list.get(which));	
								JMPasture pasture = datasource_.getActivepasture();
								String    pasture_url = JMDataSource.getPastureUrl(pasture, 0, 0);
								Log.v(JMFinal.g_tag_foundation_,"Pasture URL: "+pasture_url);
								
								JMFoundation.this.map_view_.zoomin();
							};
							})
						.show();
				}
		});
		
		this.query_btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				
				String keyQuery=txtQueryString.getText().toString();
				Query query=new Query();
				query.setWhere("NAME like '%" + keyQuery + "%'");
				query.setReturnGeometry(true);
				
				JMPasture pasture = datasource_.getActivepasture();
				String  pasture_url = JMDataSource.getPastureUrl(pasture, 0, 0);
				
				
				String queryUrl = pasture_url;
				QueryTask queryTask=new QueryTask(queryUrl);

				FeatureSet fs = null;//结果集
				try {
					fs=queryTask.execute(query);//执行查询
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				GraphicsLayer graphicsLayer = GetGraphicLayer();
				if(fs!=null && graphicsLayer.isInitialized() && graphicsLayer.isVisible()){
						Graphic[] grs = fs.getGraphics();
						if(grs.length>0){
						SimpleFillSymbol symbol =new SimpleFillSymbol(
							Color.RED);
						//设定呈现方式
						graphicsLayer.setRenderer(new SimpleRenderer(symbol));
						//添加 graphic带图层，这时，会自动用刚刚指定的“呈现方式”来呈现
						graphicsLayer.removeAll();//移除以前的
						graphicsLayer.addGraphics(grs);

						}

					}

				}

	});
	
	}
	private GraphicsLayer GetGraphicLayer(){
		if(mGraphicsLayer==null){
			mGraphicsLayer=new GraphicsLayer();
			
			this.map_view_.addLayer(mGraphicsLayer);
			this.map_view_.setExtent(mGraphicsLayer.getExtent());
		}
		return mGraphicsLayer;
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