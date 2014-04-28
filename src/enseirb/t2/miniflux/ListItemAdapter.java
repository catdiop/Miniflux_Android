<<<<<<< HEAD
package enseirb.t2.miniflux;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projet.miniflux.R;

public class ListItemAdapter extends ArrayAdapter<Item>{
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return super.isEnabled(position);
	}
	private LayoutInflater inflater;
	private List<Item> items;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=inflater.inflate(R.layout.activity_item_row, null);
		Item item=items.get(position);
		if(item!=null) {
		TextView title=(TextView)row.findViewById(R.id.title);
		title.setText(item.getTitle());
		
		TextView description=(TextView)row.findViewById(R.id.description);
		description.setText(item.getDescription());
		
		TextView uri=(TextView)row.findViewById(R.id.uri);
		uri.setText(item.getUri());
		}
		return row;
	}
	public ListItemAdapter(Context context, List<Item> items) {
		super(context, R.layout.activity_item_row, items);
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}
}
=======
package enseirb.t2.miniflux;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projet.miniflux.R;

public class ListItemAdapter extends ArrayAdapter<Item>{
	private LayoutInflater inflater;
	private List<Item> items;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=inflater.inflate(R.layout.activity_item_row, null);
		Item item=items.get(position);
		if(item!=null) {
		TextView title=(TextView)row.findViewById(R.id.title);
		title.setText(item.getTitle());
		
		TextView description=(TextView)row.findViewById(R.id.description);
		description.setText(item.getDescription());
		
		TextView uri=(TextView)row.findViewById(R.id.uri);
		uri.setText(item.getUri());
		}
		return row;
	}
	
	public ListItemAdapter(Context context, List<Item> items) {
		super(context, R.layout.activity_item_row, items);
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}
	
}
>>>>>>> 84d3f384b0fd531574ea5943cde47e1b88bc9544
