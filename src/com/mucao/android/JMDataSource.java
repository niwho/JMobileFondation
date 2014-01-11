package com.mucao.android;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;

enum AdminLayerIndex {   
	  PROVINCE, CITY, COUNTY 
}; 

enum ShiyiLayerIndex {   
	  CISHIYI, SHIYI   
};  

class JMPasture{
    public String                   name_;
    public String                   pasture_url_;
    
    public String                   raster_layer_;
    public int						raster_layer_id_;
    
    public String[][]			 	vector_layer_ = new String[3][2];
    public int[][]					vector_layer_id_ = new int[3][2];
};

class JMGrassFamily{
    public String                   family_;
    public String                   family_url_;
    public List<JMPasture>          pasture_list_ = new LinkedList<JMPasture>();
};

public class JMDataSource {
	
	//Service URL
	private String                  service_url_;
	
    //Pasture
	private List<JMGrassFamily>     family_list_ = new LinkedList<JMGrassFamily>();
	private JMPasture               active_pasture_;
 
	//Admin
	public String[]					admin_layer_ = new String[3];
	public int[]					admin_layer_id_ = new int[3];
	
	private ArcGISDynamicMapServiceLayer 	dynamicLayer_;
			
	////////////////////////////////////////////////////////////////////////////////
	public String getService_url() {
		return service_url_;
	}

	public void setService_url(String service_url_) {
		this.service_url_ = service_url_;
	}

	public JMPasture getActivepasture() {
		return active_pasture_;
	}

	public void setActivePasure(JMPasture active_pasture_) {
		this.active_pasture_ = active_pasture_;
	}

	public List<JMGrassFamily> getFamily_list() {
		return family_list_;
	}

	public void setFamily_list(List<JMGrassFamily> family_list) {
		this.family_list_ = family_list;
	}
	
	public ArcGISDynamicMapServiceLayer getDynamicLayer() {
		return dynamicLayer_;
	}

	public void setDynamicLayer(ArcGISDynamicMapServiceLayer dynamicLayer) {
		this.dynamicLayer_ = dynamicLayer;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	public Boolean init(String service_url,ArcGISDynamicMapServiceLayer dynamic_layer){
		Log.v(JMFinal.g_tag_datasource_,"Init");
		
		//
		setService_url(service_url);
		
		//
		setDynamicLayer(dynamic_layer);
			
		//
		return refreshAdministrative() && refreshPasture();
    }
	
	public Boolean switchPasture(String name){
		
		Log.v(JMFinal.g_tag_datasource_,"Switch Pasture");
		
		setActivePasure(GetPastureByName(name));
		
		return true;
    }
	
	public static  String getPastureUrl(JMPasture pasture){
		String url ="";
		
		return url;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	protected Boolean refreshAdministrative(){
		Log.v(JMFinal.g_tag_datasource_,"Refresh Administrative");
		
		ArcGISLayerInfo adminGroups[] = dynamicLayer_.getLayers();
		for(int i= 0 ;i < adminGroups.length ;i++){
			if(adminGroups[i].getName() == "行政图")
			{
				List<ArcGISLayerInfo> adminGroup = adminGroups[i].getLayers();	
				for(int j= 0 ;j < adminGroup.size() ;j++){	
					ArcGISLayerInfo pastureGroup = adminGroup.get(j);
					if(pastureGroup.getName().indexOf("省") != -1){
						admin_layer_[AdminLayerIndex.PROVINCE.ordinal()] = pastureGroup.getName();
						admin_layer_id_[AdminLayerIndex.PROVINCE.ordinal()] = pastureGroup.getId();
					}	
					else if(pastureGroup.getName().indexOf("市") != -1){
						admin_layer_[AdminLayerIndex.CITY.ordinal()] = pastureGroup.getName();
						admin_layer_id_[AdminLayerIndex.CITY.ordinal()] = pastureGroup.getId();
					}	
					else if(pastureGroup.getName().indexOf("县") != -1){
						admin_layer_[AdminLayerIndex.COUNTY.ordinal()] = pastureGroup.getName();
						admin_layer_id_[AdminLayerIndex.COUNTY.ordinal()] = pastureGroup.getId();
					}				
				}
			}		
		}
		
		return true;
	}
	
	protected Boolean refreshPasture(){
		
		Log.v(JMFinal.g_tag_datasource_,"Refresh Pasture");
		
		ArcGISLayerInfo familyGroups[] = dynamicLayer_.getLayers();
		
		for(int i= 0 ;i < familyGroups.length ;i++){
			
			if(familyGroups[i].getName() == "行政图")
				continue;
			
			List<ArcGISLayerInfo> familyGroup = familyGroups[i].getLayers();	
			JMGrassFamily family = new JMGrassFamily();		
			family.family_ = familyGroups[i].getName();
			family.family_url_ = service_url_ + "\\" + family.family_;
			
			for(int j= 0 ;j < familyGroup.size() ;j++){	
				
				List<ArcGISLayerInfo> pastureGroup = familyGroup.get(j).getLayers();
				JMPasture pasture = new JMPasture();
				pasture.name_ = familyGroup.get(j).getName();
				pasture.pasture_url_ = family.family_url_ + "\\" + pasture.name_;
				
				//pasure
				for(int k= 0 ;k < pastureGroup.size() ;k++){
					
					ArcGISLayerInfo lyrInfo = pastureGroup.get(k);
					
					Log.v(JMFinal.g_tag_datasource_,"Layer Info["+k+"]="+ lyrInfo.toString());		
					int admin_index = -1, shiyi_index = -1;
					
					//admin index
					if(lyrInfo.getName().indexOf("省") != -1)
					{
						admin_index = AdminLayerIndex.PROVINCE.ordinal();
					}
					else if(lyrInfo.getName().indexOf("市") != -1)
					{
						admin_index = AdminLayerIndex.CITY.ordinal();
					}
					else if(lyrInfo.getName().indexOf("县") != -1)
					{
						admin_index = AdminLayerIndex.COUNTY.ordinal();
					}
					
					//shiyi type
					if(lyrInfo.getName().indexOf("次适宜") != -1)
					{
						shiyi_index = ShiyiLayerIndex.CISHIYI.ordinal();
					}
					else if(lyrInfo.getName().indexOf("适宜") != -1)
					{
						shiyi_index = ShiyiLayerIndex.SHIYI.ordinal();
					}
				
					if(admin_index != -1 && shiyi_index != -1)
					{
						pasture.vector_layer_[admin_index][shiyi_index] = lyrInfo.getName();
						pasture.vector_layer_id_[admin_index][shiyi_index] = lyrInfo.getId();
					}
					else
					{
						pasture.raster_layer_ = lyrInfo.getName();
						pasture.raster_layer_id_ = lyrInfo.getId();
					}
				}
				
				for(i = 0; i < 3 ;i++)
					for(j = 0; j < 2 ;j++)
					Log.v(JMFinal.g_tag_datasource_,"Layer Name ="+ pasture.vector_layer_[i][j]);		

				family.pasture_list_.add(pasture);		
			}	 
			
			this.family_list_.add(family);
		}
		
		return true;
    }
	
	 protected JMPasture GetPastureByName(String pasture_name)
     {
		 for(int i = 0; i < this.family_list_.size();i++){
			 JMGrassFamily family = this.family_list_.get(i);
			 for(int j = 0; j < family.pasture_list_.size();j++ ){
				 if(family.pasture_list_.get(j).name_ == pasture_name)
					 return family.pasture_list_.get(j);
			 }
		 }
         
         return new JMPasture();
     }
}
