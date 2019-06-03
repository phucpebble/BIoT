package it.hp.biot.IoT;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.hp.biot.ImageClassify.CameraActivity;
import it.hp.biot.MainActivity;
import it.hp.biot.R;


public class bluetoothRaspberrypi extends AppCompatActivity {
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

    Button btnIoTBack;
    TextView txtTemperature, txtHumidity;
    String[] arrayData = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupBouncyCastle();
        setContentView(R.layout.activity_iot);

        btnIoTBack = (Button) findViewById(R.id.btnIoTBack);
        txtHumidity = (TextView)findViewById(R.id.txtHumidity);
        txtTemperature = (TextView)findViewById(R.id.txtTemperature);

        btnIoTBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send the result back to Main_activity
                Intent intentBack = new Intent(bluetoothRaspberrypi.this, MainActivity.class);
                intentBack.putExtra("arrayDataTempHum", arrayData);
                setResult(RESULT_OK, intentBack);
                try {
                    closeBluetooth();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                //startActivity(intentBack);

            }
        });

        findBluetooth();
        connectBluetooth("temp_humidity");

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
                Toast.makeText(bluetoothRaspberrypi.this, "Null device ", Toast.LENGTH_LONG).show();
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
                                    //Log.d("AAA", "Loop for i  " + i);


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

                                                //Add result back
                                                String[] splitStr = data.split("\\s+");
                                                arrayData[0] = splitStr[0];
                                                arrayData[1] = splitStr[1];
                                                txtTemperature.setText("Temperature got from IoT is: " + arrayData[0]);
                                                txtHumidity.setText("Humidity got from IoT is " +arrayData[1]);


                                                Log.d("AAA", "arrayData[0] is " + arrayData[0]);
                                                Log.d("AAA", "arrayData[1] is " + arrayData[1]);



                                                /*try {
                                                    closeBluetooth();
                                                    //finish();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }*/


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
    public void closeBluetooth() throws IOException {
        workDone = false;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        //myLabel.setText("Bluetooth Closed");
        Log.d("AAA", "Bluetooth has closed !");
    }


}
