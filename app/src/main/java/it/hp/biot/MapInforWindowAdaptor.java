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
    private String farmName, farmAddress;
    private String foodName ="Init food - Apple";
    private String temperature = "T";
    private String humidity= "H";

    public MapInforWindowAdaptor(Context context, String farmName, String farmAddress, String foodName, String temperature, String humidity) {
        this.context = context;
        this.farmName = farmName;
        this.farmAddress = farmAddress;
        this.foodName = foodName;
        this.temperature = temperature;
        this.humidity = humidity;

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

        //ImageView imgFarm = (ImageView) v.findViewById(R.id.farmImg);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDesc = (TextView) v.findViewById(R.id.txtDesc);
        TextView txtLatLong = (TextView) v.findViewById(R.id.txtLatLong);
        TextView textAdd = (TextView) v.findViewById(R.id.txtAddress);
        TextView txtTemperature = (TextView)v.findViewById(R.id.txtTemperature);
        TextView txtHumidity  = (TextView) v.findViewById(R.id.txtHumidity);

        LatLng latLng = marker.getPosition();
        //Get foodName
        txtTitle.setText("Farm Name: " + farmName);
        textAdd.setText("Farm Address: " + farmAddress);
        //Setting latitude
        txtLatLong.setText("Latitude: " + String.format("%.02f", latLng.latitude) + ", Longitude: " + String.format("%.02f", latLng.longitude));
        //Set description of farm
        txtDesc.setText("Food Name: "+ foodName);
        if ((temperature.length() > 1) && (humidity.length()> 1)){
            txtTemperature.setText("Temperature: "+ temperature);
            txtHumidity.setText("Humidity: "+ humidity);
            Log.d("AAA","Temperature: " + temperature + ", Humdity: " + humidity);

        }
        Log.d("AAA","Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
        Log.d("AAA","Marker. GetTitle: " + marker.getTitle());

        return v;
    }
}
