//Blockchain IoT - Henry Pham April 2019
package it.hp.biot;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Credentials;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import it.hp.biot.BIoT;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Web3j web3j;
    private GoogleMap mMap;

    //FIXME: Add your own password here
    private final String password = "PrivateKey";//16FB976643702D2E530D0ABA9A2E38B8E44E0A3B6B3C330CD41BB794BDC96DA6"; //Copy from datamask
    private final static String privateKeyRopsten = "16FB976643702D2E530D0ABA9A2E38B8E44E0A3B6B3C330CD41BB794BDC96DA6"; //Privekey export form Menu, DataMask
    private final static String contractAddressRopsten = "0x6f338949af6df038d715a5e07d10f3a08cb0d68e";
    private final static String toAddress = "0x81b7e08f65bdf5648606c89998a9cc8164397647";

    //private final String password = "medium";
    private String walletPath;
    private File walletDir;
    private org.web3j.crypto.Credentials credentials = org.web3j.crypto.Credentials.create(privateKeyRopsten);
    private int minimumGasLimit = 21000;
    private BigInteger gasLimit = new BigInteger(String.valueOf(minimumGasLimit));
    BIoT contract;

    //Define for Bluetooth
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33; //Character !, used to define the end of sending data string from RaspberryPi, example: "20oC !"
    InputStream mmInputStream;
    OutputStream mmOutputStream;
    Thread workerThread;
    volatile boolean workDone = false;
    int readBufferPosition = 0;
    byte[] readBuffer;



    TextView txtLat;
    Button tempButton;
    SupportMapFragment supportMapFragment;
    private static final int REQUEST_LOCATION_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupBouncyCastle();
        setContentView(R.layout.activity_main);

        initViews();


        //walletPath = getFilesDir().getAbsolutePath();
        //walletDir = new File(walletPath);

        // start temp button handler
      /*  tempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click
                findBluetooth();
                connectBluetooth("temp_humidity");
            }
        });*/

        //Connect to Ethereum
           connectToEthNetwork();
           //Temporary uncomment to test Bluetooth
        //   deploySmartContract();

        //End of onCreate
    }


    private void initViews() {

        //Connect bluetooth and read data from Raspberry Pi
        txtLat = (TextView) findViewById(R.id.txtLat);
        tempButton = (Button) findViewById(R.id.tempButton);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        supportMapFragment.getMapAsync(this);

    }


    public void readQr(View view) {
        Intent intent = new Intent(MainActivity.this, QrReader.class);
        startActivity(intent);
    }

    public void generateQr(View view) {
        Intent intent = new Intent(MainActivity.this, QrGenerate.class);
        startActivity(intent);
    }


    public void connectToEthNetwork() {
        toastAsync("Connecting to Ethereum network...");
        // FIXME: Add your own API key here

        web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/47c63d5fab244e4ba2311dd0a9ce35eb"));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();

            if (!clientVersion.hasError()) {
                Log.d("AAA", "Connected to Ethereum");
                //toastAsync("Connected!");
            } else {
                //toastAsync(clientVersion.getError().getMessage());
                Log.d("AAA", "Error Connection to Ethereum" + clientVersion.getError().getMessage());
            }
        } catch (Exception e) {
            Log.d("AAA", "Exception Connection to Ethereum" + e.getMessage());
            toastAsync(e.getMessage());
        }
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }


    public void createWallet() {
        try {
            WalletUtils.generateNewWalletFile(password, walletDir);
            Log.d("AA", "Wallet generated ");
            toastAsync("Wallet generated");
        } catch (Exception e) {
            Log.d("AA", "Error Wallet generated " + e.getMessage());
            //toastAsync(e.getMessage());
        }
    }

    public void getAddress() {
        try {
            //org.web3j.crypto.Credentials credentials = WalletUtils.loadCredentials(password,  walletDir);
            Log.d("AA", "Address of wallet is " + credentials.getAddress());
            //toastAsync("Your address is " + credentials.getAddress());
        } catch (Exception e) {
            toastAsync(e.getMessage());
        }
    }

    public void sendTransaction() {
        try {
            //org.web3j.crypto.Credentials credentials = WalletUtils.loadCredentials(password, walletDir);
            Log.d("AAA", "Value of web3 is " + web3j.toString());
            TransactionReceipt receipt = Transfer.sendFunds(web3j, credentials, toAddress, new BigDecimal(1), Convert.Unit.ETHER).sendAsync().get();
            Log.d("AAA", "Transaction complete ");
            //toastAsync("Transaction complete: " + receipt.getTransactionHash() );
        } catch (Exception e) {
            Log.d("AAA", "Exception in Transaction  " + e.getMessage());
            //toastAsync(e.getMessage());
        }
    }

    public void deploySmartContract() {

        // Now lets deploy a smart contract
        Log.d("AAA", "Deploying smart contract");
        //Gas price must < = Gas Limit

        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        //((DefaultGasProvider) contractGasProvider).GAS_PRICE = BigInteger.valueOf(1000);
        Log.d("AAA", "GAS_Price is " + contractGasProvider.getGasPrice().toString());
        Log.d("AAA", "GAS_LIMIT is " + contractGasProvider.getGasLimit().toString());


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    contract = BIoT.deploy(
                            web3j,
                            credentials,
                            ((DefaultGasProvider) contractGasProvider).GAS_PRICE,
                            //BigInteger.valueOf(1000),
                            ((DefaultGasProvider) contractGasProvider).GAS_LIMIT

                    ).send();

                    String contractAddress = contract.getContractAddress();
                    Log.d("AAA", "Smart contract deployed to address " + contractAddress);
                    Log.d("AAA", "Start update Humidity ");
                    contract.setHumidity(BigInteger.valueOf(19)).send();
                    Log.d("AAA", "Completed updating Humidity ");
                    Log.d("AAA", "Update value of Temperature after updating is " + contract.getHumidity().send());

                    Log.d("AAA", "Start update Temperature ");
                    contract.setTemperature(BigInteger.valueOf(18)).send();
                    Log.d("AAA", "Value of Temperature after updating is " + contract.getTemperature().send());

                    //Update value of Humidity and Temperature


                } catch (Exception e) {
                    Log.d("AAA", "Error in deploying contract " + e.getMessage().toString());
                    //e.printStackTrace();
                }

            }
        });
        thread.start();


    }


    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
        });
    }


    // this will find a bluetooth printer device
    void findBluetooth() {

        try {

            //Working with bluetooth
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Log.d("AAA", "No bluetooth adapter available");
                //myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // raspberrypi is the name of the bluetooth RaspberryPi
                    // we got this name from the list of paired devices
                    if (device.getName().equals("raspberrypi")) {
                        Log.d("AAA", "The paired device is " + device.getName());
                        mmDevice = device;
                        break;
                    }
                }
            }
            // myLabel.setText("Bluetooth device found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Connect bluetooth and send the command
    public void connectBluetooth(String msg2send) {
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {

            if (mmDevice != null) {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                if (!mmSocket.isConnected()) {
                    Log.d("AAA", "Connecting device ");
                    mmSocket.connect();
                }

                String msg = msg2send;
                //msg += "\n";
                mmOutputStream = mmSocket.getOutputStream();
                mmOutputStream.write(msg.getBytes());

                mmInputStream = mmSocket.getInputStream();
                listenForDataBluetooth();
            } else {
                Toast.makeText(MainActivity.this, "Null device ", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            Log.d("AAA", "Error on send BTMesg is " + e.getMessage().toString());
        }

    }


    void listenForDataBluetooth() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character

            workDone = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !workDone) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            Log.d("AAA", "Value of bytesAvailable is  " + bytesAvailable);

                            if (bytesAvailable > 0) {
                                Log.d("AAA", "bytesAvailable > 0 " + bytesAvailable);

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {
                                    Log.d("AAA", "Loop for i  " + i);


                                    byte b = packetBytes[i];
                                    Log.d("AAA", "delimiter is   " + delimiter + "and b is " + b);
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // Get the data is sent from Bluetooth Raspberry
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("AAA", "Data received is " + data);
                                                //myLabel.setText(data);
                                                workDone = true;
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            workDone = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Close bluetooth
    void closeBluetooth() throws IOException {
        workDone = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        //myLabel.setText("Bluetooth Closed");
        Log.d("AAA", "Bluetooth has closed !");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            MarkerOptions option=new MarkerOptions();
            option.title("Fruit farm for Food supply chain"); //This will be overrided by customized adaptor
            option.snippet("ABC farm is the one biggest farm in NZ");  //This will be overrided by customized adaptor
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            option.position(latLng);
            Marker currentMarker= mMap.addMarker(option);

            //setting again new customise InforWindow Adaptor
            mMap.setInfoWindowAdapter(new MapInforWindowAdaptor(this));
            currentMarker.showInfoWindow();

        }
        else {
            Log.d("AAA", "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }


    }

}
