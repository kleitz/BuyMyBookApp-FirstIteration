package com.EasyPeasy.buymybook;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.EasyPeasy.buymybook.CommunicationClass.DownloadJSON;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSearchListAdaptor extends BaseAdapter{

	private ArrayList listData; //listData format:
	private LayoutInflater layoutInflater;
	private String imgUrl;
	public CustomSearchListAdaptor(Context context, ArrayList listData){
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}
	public CustomSearchListAdaptor(Context context, ArrayList listData,String imgUrl){
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		this.imgUrl = imgUrl;
	}
	public CustomSearchListAdaptor(LayoutInflater inflater,ArrayList listData){
		layoutInflater = inflater;
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
	        if (convertView == null) {
	            convertView = layoutInflater.inflate(R.layout.searchlistinglayout, null);
	            holder = new ViewHolder();
	            holder.titleView = (TextView) convertView.findViewById(R.id.search_manual_btitle);
	            holder.authorView = (TextView) convertView.findViewById(R.id.search_manual_author);
	            holder.priceView = (TextView) convertView.findViewById(R.id.search_manual_price);
	            holder.condition = (TextView) convertView.findViewById(R.id.search_manual_condition);
	            holder.image = (ImageView) convertView.findViewById(R.id.image);
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder) convertView.getTag();
	        }
	        //output text
	        SearchListItem item = (SearchListItem)listData.get(position);
	        holder.titleView.setText(item.getTitle());
	        holder.authorView.setText("Author: "+item.getAuthor());
	        holder.priceView.setText("Price: $"+item.getPrice());
	        //imgUrl = item.getUrl();
	        String condition;
	        switch(Integer.parseInt(item.getCondition())){
	        	case 0:
	        		condition = new String("Decomposed");
	        		break;
	        	case 1:
	        		condition = new String("Poor");
	        		break;
	        	case 2:
	        		condition = new String("Fair");
	        		break;
	        	case 3:
	        		condition = new String("Good");
	        		break;
	        	case 4:
	        		condition = new String("Very Good");
	        		break;
	        	case 5:
	        		condition = new String("As New");
	        		break;
	        	default:
	        		condition = new String("N/A");
	        		break;
	        		
	        }//switch
	        holder.condition.setText("Condition: "+ condition);
	      if(imgUrl != null){
			CommunicationClass c = new CommunicationClass(imgUrl);
			c.new DownloadImageTask(holder.image).execute(imgUrl);
	      }
	
	
	       // holder.image.setImageResource(R.drawable.clearthinking);
	        return convertView;
	}
	static class ViewHolder {
        TextView titleView;
        TextView authorView;
        TextView priceView;
        TextView condition;
        ImageView image;
    }
}
