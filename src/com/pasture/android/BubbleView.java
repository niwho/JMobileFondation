package com.pasture.android;

import com.esri.android.map.MapView;
import com.pasture.android.R;
import com.pasture.android.R.id;
import com.pasture.android.R.layout;
import com.pasture.android.R.style;

import android.app.Activity;
import android.content.Context;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * ���ݴ�����
 * @author Administrator
 * 
 */
public class BubbleView{

    private MapView mapView;
    private View foot_popunwindwow;
    private PopupWindow mPopupWindow;
    //TextView textView;
    TextView textView;
	public BubbleView(Context context,MapView mapView) {
		
         this.mapView = mapView;
         LayoutInflater LayoutInflater = (LayoutInflater)((JMFoundation)context)
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     foot_popunwindwow = LayoutInflater
			.inflate(R.layout.bubble_view, null);
	     
	    // mPopupWindow = new PopupWindow(foot_popunwindwow,
			//		100, LayoutParams.WRAP_CONTENT,true);
	     

	}
	 /**
	* �ر����ݿ�
	*/
	 public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
			Log.d("PopWin", "dismiss ok");
			}
	}
	 /**
	* ��ʾ����
	* @param target Ŀ�����
	*/
	 public void ShowBubble(int x,int y) {
		 
		 mPopupWindow = new PopupWindow(foot_popunwindwow,
					100, LayoutParams.WRAP_CONTENT);
		 
		 //mPopupWindow.showAsDropDown(target,-this.getWidth()/2+25,-this.getHeight()-25);
		 
		 mPopupWindow.showAtLocation(mapView, Gravity.LEFT | Gravity.TOP, x, y);
		 mPopupWindow.setAnimationStyle(R.style.PopupAnimation);

		 //mPopupWindow.showAtLocation(target,
		//		 Gravity.CENTER, 0, -10);
		 mPopupWindow.update();
	 }
	 /**
	 * ������ݱ���
	 * @param title ����
     */	  
	 public void setTitle(String title)
	 {
		 TextView titleView = (TextView)foot_popunwindwow.findViewById(R.id.title);
		 titleView.setText(title);
	 }
	 /**
	 * �������������Ϣ
	 * @param text ��������
	 */	  
	 public void setText(String text)
	 {
		 textView = (TextView)foot_popunwindwow.findViewById(R.id.text);
		 textView.setText(text);
		 textView.invalidate();
		 textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	 }
	 /**
	 * ��ȡ���ݿ�Ŀ��
	 */	  
	public int getWidth()
	{
		return mPopupWindow.getWidth();
	}
	/**
	 * ��ȡ���ݿ�ĸ߶�
	 */
	public int getHeight()
	{
		return mPopupWindow.getHeight();
		//return mPopupWindow.getWidth();
	}
	
	

}
