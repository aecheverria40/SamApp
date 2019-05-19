package com.example.lashope.SamAPP;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lashope.SamAPP.Models.Proyector;

import java.util.ArrayList;

public class listviewAdapter extends BaseAdapter {
    public ArrayList<Proyector> productList;
    Activity activity;

    public listviewAdapter(Activity activity, ArrayList<Proyector> productList) {
        super();
        this.activity = activity;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mSNo;
        TextView mProduct;
        TextView mCategory;
        TextView mPrice;
        ImageView mPic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.mSNo = (TextView) convertView.findViewById(R.id.sNo);
            holder.mProduct = (TextView) convertView.findViewById(R.id.product);
            holder.mCategory = (TextView) convertView
                    .findViewById(R.id.category);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Proyector item = productList.get(position);
        holder.mSNo.setText(item.getMarca().toString());
        holder.mProduct.setText(item.getModelo().toString());
        holder.mCategory.setText(item.getDisponible().toString());
        holder.mPrice.setText(item.getMaestro().toString());

        return convertView;
    }

}