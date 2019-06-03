package it.hp.biot.QRcode;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import it.hp.biot.Ethereum.SmartContract;



public class QrReader extends Activity implements ZXingScannerView.ResultHandler {
private ZXingScannerView mScannerView;
        String TAG="QRREADER";
        SmartContract smartContractrd = new SmartContract();
        String strFoodInfor="";

@Override
public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view



}



@Override
public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        }

@Override
public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        }

@Override
public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        //Start to read Smartcontract with scanned Ethereum address
        smartContractrd.readSmartContract(rawResult.getText());
        while (true)
        {
                if (smartContractrd.checkResult())
                {
                        strFoodInfor = "Farm name: "+smartContractrd.getFarmname() + "\nFarm address: "+smartContractrd.getFarmaddress() + "\nFood name: "+smartContractrd.getFoodname() + "\nPosition: "+ smartContractrd.getPosition() + "\nHumidity: " +smartContractrd.getHumidity()+ "\nTemperature: " + smartContractrd.getTemperature();
                        Log.d("AAA", strFoodInfor);
                        // call the alert dialog
                        Alert(rawResult);
                        break;
                }

        }




        }


public void Alert(Result rawResult){


        //MainActivity.readSmartContract();
        AlertDialog.Builder builder = new AlertDialog.Builder(QrReader.this);
        builder.setTitle("Food supplychain scan");
        builder.setMessage(strFoodInfor)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
        // back to previous activity
        smartContractrd.resetResult();
        finish();

        }
        })
        .setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
        // User cancelled the dialog
        // If you would like to resume scanning, call this method below:
        smartContractrd.resetResult();
        mScannerView.resumeCameraPreview(QrReader.this);
        }
        });
        // Create the AlertDialog object and return it
        builder.create().show();
        }
        }