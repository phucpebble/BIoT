package it.hp.biot;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class MapInforWindowAdaptor implements InfoWindowAdapter {
    private Context context;

    public MapInforWindowAdaptor(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //convertView = inflater.inflate(layout,null);
        View v = inflater.inflate(R.layout.map_infor_window, null);

        TextView txtDesc = (TextView) v.findViewById(R.id.txtDesc);
        TextView textAdd = (TextView) v.findViewById(R.id.txtAddress);

        LatLng latLng = marker.getPosition();
        TextView txtLat = (TextView) v.findViewById(R.id.txtLat);
        TextView txtLong = (TextView) v.findViewById(R.id.txtLong);

        ImageView imgFarm = (ImageView) v.findViewById(R.id.farmImg);
        //Setting latitude
        txtLat.setText("Latitude: " + latLng.latitude);
        //Seting Longitude
        txtLong.setText("Longitude: " + latLng.longitude);
        //Set description of farm
        txtDesc.setText("The best supply organic food in NZ!");
        //Log.d("AAA","Vi tri: " + latLng.latitude);
        return v;
    }
}
