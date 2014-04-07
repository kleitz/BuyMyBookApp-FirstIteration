package com.EasyPeasy.buymybook;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomPeronalListingAdapter extends BaseAdapter{

	private ArrayList listData; //listData format:
	private LayoutInflater layoutInflater;
	
	public CustomPeronalListingAdapter(Context context, ArrayList listData){
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}
	public CustomPeronalListingAdapter(LayoutInflater inflater,ArrayList listData){
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
	public View getView(int position, View convertView, final ViewGroup parent) {
		 final ViewHolder holder;
		 ImageView deleteImg = null;
	        if (convertView == null) {
	            convertView = layoutInflater.inflate(R.layout.peronal_listing_layout, null);
	            holder = new ViewHolder();
	            holder.titleView = (TextView) convertView.findViewById(R.id.search_manual_btitle);
	            holder.authorView = (TextView) convertView.findViewById(R.id.search_manual_author);
	            holder.priceView = (TextView) convertView.findViewById(R.id.search_manual_price);
	            holder.condition = (TextView) convertView.findViewById(R.id.search_manual_condition);
	            //holder.book_img = (ImageView) convertView.findViewById(R.id.book_img);
	            holder.delete_img = (ImageView) convertView.findViewById(R.id.delete_img);
	            
	            deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder) convertView.getTag();
	        }
	        //output text
	        SearchListItem item = (SearchListItem)listData.get(position);
	        holder.titleView.setText(item.getTitle());
	        holder.authorView.setText("Author: "+item.getAuthor());
	        holder.priceView.setText("Price: $"+item.getPrice());
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
	        //holder.book_img.setImageResource(R.drawable.clearthinking);
	        
	        if (deleteImg != null) {
		        holder.delete_img.setImageResource(R.drawable.ic_list_remove);
		        deleteImg.setOnClickListener(new OnClickListener() {
		        	@Override
		        	public void onClick(View v) {
		        		 //holder.book_img = null;
		        		Toast.makeText(
		        			parent.getContext(), 
		        			"You are trying to delete this item. Code is in CustomPeronalListingAdapter.java", 
		        			Toast.LENGTH_SHORT)
		        		.show();
		        	}
		        });
	        }
	        
	        return convertView;
	}
	static class ViewHolder {
        TextView titleView;
        TextView authorView;
        TextView priceView;
        TextView condition;
        ImageView book_img;
        ImageView delete_img;
    }
}
