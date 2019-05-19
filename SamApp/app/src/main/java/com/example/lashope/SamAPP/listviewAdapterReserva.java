package com.example.lashope.SamAPP;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lashope.SamAPP.Models.Reserva;

import java.util.ArrayList;

public class listviewAdapterReserva extends BaseAdapter {

    public ArrayList<Reserva> productList;
    Activity activity;

    public listviewAdapterReserva(ArrayList<Reserva> productList, Activity activity) {
        super();
        this.productList = productList;
        this.activity = activity;
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
        TextView mAudiovisual;
        TextView mHora;
        TextView mFecha;
        TextView mMaestro;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_reserva, null);
            holder = new ViewHolder();
            holder.mAudiovisual = (TextView) convertView.findViewById(R.id.sNo);
            holder.mHora = (TextView) convertView.findViewById(R.id.product);
            holder.mFecha = (TextView) convertView.findViewById(R.id.category);
            holder.mMaestro = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reserva item = productList.get(position);
        holder.mAudiovisual.setText(item.getAudiovisual().toString());
        holder.mHora.setText(item.getHora().toString());
        holder.mFecha.setText(item.getFecha().toString());
        holder.mMaestro.setText(item.getMaestro().toString());

        return convertView;
    }
}
