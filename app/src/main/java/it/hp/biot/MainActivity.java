//Blockchain IoT - Henry Pham April 2019
package it.hp.biot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.hp.biot.ImageClassify.ClassifierActivity;
import it.hp.biot.IoT.bluetoothRaspberrypi;
import it.hp.biot.QRcode.QrGenerate;
import it.hp.biot.QRcode.QrReader;
import it.hp.biot.Ethereum.SmartContract;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    TextView txtLat;
    Button tempButton;
    Button btnGenerateBlockchain;

    SupportMapFragment supportMapFragment;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    //Tensorflow and Image classification
    Button btnImageClassify;
    public int REQUEST_CODE_INTENT_FOODSCAN = 123;
    public int REQUEST_CODE_INTENT_HUMIDITY_TEMPERTURE = 456;
    public static String txtFoodname="";

    //Declare for Humidity and Temperature
    public String[] arrayTempHum =new String[100];
    private static String txtFarmName ="ABC fruit farm";
    private static String txtFarmAddress ="55 Wellesley street, Auckland";
    private static String txtTemperature="";
    private static String txtHumidity ="";
    private static LatLng latLng;
    SmartContract smartContract;
    private String smartcontractAddress ="";

    public Context getContext(){return this.getContext();}
    public static String getTxtFarmName() { return txtFarmName; }
    public static String getTxtFarmAddress() { return txtFarmAddress; }
    public static String getTxtTemperature() { return txtTemperature; }
    public static String getTxtHumidity() {
        return txtHumidity;
    }
    public static String getTxtFoodname() {
        return txtFoodname;
    }
    public static LatLng getLatLng() { return latLng; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupBouncyCastle();
        setContentView(R.layout.activity_main);

        initViews();

        //Process for Image classification Tensorflow
        btnImageClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action;
                Uri uri;
                Intent intent = new Intent(MainActivity.this, ClassifierActivity.class);
                //startActivity(intent);
                startActivityForResult(intent,REQUEST_CODE_INTENT_FOODSCAN );
            }
        });

        //walletPath = getFilesDir().getAbsolutePath();
        //walletDir = new File(walletPath);

        // start temp button handler
        tempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click
                Intent intentIoT = new Intent(MainActivity.this, bluetoothRaspberrypi.class );

                startActivityForResult(intentIoT, REQUEST_CODE_INTENT_HUMIDITY_TEMPERTURE);

            }
        });


        //Process for Blockchain part
        btnGenerateBlockchain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Connect to Ethereum
                smartContract = new SmartContract();
                smartContract.connectToEthNetwork();

                //Temporary uncomment to test Bluetooth
              //  deploySmartContract();
                smartContract.deploySmartContract();
                smartcontractAddress = smartContract.getContractAddress();
                Log.d("AAA", "New address contract is after deploy " + smartcontractAddress);
                //End of onCreate
            }
        });

    }


    private void initViews() {

        //Connect bluetooth and read data from Raspberry Pi
        txtLat = (TextView) findViewById(R.id.txtLat);
        tempButton = (Button) findViewById(R.id.tempButton);
        //Tensorflow
        btnImageClassify = (Button)findViewById(R.id.btnClassifyFood);
        btnGenerateBlockchain = (Button)findViewById(R.id.btnGenerateBlockchain);

    }


    public void readQr(View view) {
        Intent intent = new Intent(MainActivity.this, QrReader.class);
        startActivity(intent);


        //Log.d("AAA", "New address contract at scan step is " + smartContract.getContractAddress());
        //smartContract.readSmartContract(smartContract.getContractAddress());
    }

    public void generateQr(View view) {
        Intent intent = new Intent(MainActivity.this, QrGenerate.class);
        intent.putExtra("FoodName_TimeScan", txtFoodname + " at ABC farm is scanned at " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

        startActivity(intent);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Clear to be sure new things for IoT read event
        //mMap.clear();

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-36, 170);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       // locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            // return;
        }

        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (lastLocation != null) {
            Log.d("AAA", "Location is not null");
            latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            MarkerOptions option=new MarkerOptions();
            //option.title("ABC Fruit Farm"); //This will be overrided by customized adaptor
            option.snippet("Food Name");  //This will be overrided by customized adaptor
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            option.position(latLng);

            Marker currentMarker= mMap.addMarker(option);
            //currentMarker.showInfoWindow();
            mMap.setInfoWindowAdapter(new MapInforWindowAdaptor(this, txtFarmName, txtFarmAddress, txtFoodname, txtTemperature, txtHumidity));
            currentMarker.showInfoWindow();


       }
        else {
            Log.d("AAA", "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_INTENT_FOODSCAN && resultCode == RESULT_OK && data != null)
        {
            //Function to get data send back from Camera & Tensorflow result
            txtFoodname = data.getStringExtra("strResulIntent");
            //Toast.makeText(this, txtFoodname, Toast.LENGTH_SHORT).show();

            //Google map part: Update Farm with food dectected information from Tensorflow
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
            supportMapFragment.getMapAsync(this);
        }

        if (requestCode == REQUEST_CODE_INTENT_HUMIDITY_TEMPERTURE && resultCode == RESULT_OK && data != null)
        {
            //Get Temperature and Humidity from Raspberry
            //Intent intentTempHum = getIntent();
            arrayTempHum = data.getStringArrayExtra("arrayDataTempHum");
            //Toast.makeText(this, txtFoodname, Toast.LENGTH_SHORT).show();
            Log.d("AAA", "arrayTempHum[0] is " + arrayTempHum[0]);
            Log.d("AAA", "arrayTempHum[1] is " + arrayTempHum[1]);

            txtTemperature = arrayTempHum[0];
            txtHumidity = arrayTempHum[1];
            Log.d("AAA", "txtHumidity is " + txtHumidity);


            //Google map part: Update Farm with food dectected information from Tensorflow
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
            supportMapFragment.getMapAsync(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
