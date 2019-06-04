package it.hp.biot.Ethereum;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import org.apache.commons.lang3.StringUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.Collections;

import it.hp.biot.BIoT;
import it.hp.biot.MainActivity;
import it.hp.biot.MyApplication;

public class SmartContract {

    private Web3j web3j;

    private static String farmName ="";
    private static String farmPosition ="";
    private static String farmAddress="";
    private static String foodName ="";

    private static String temperature ="";
    private static String humidity ="";
    private static String contractAddress="";

    //private final String password = "medium";
    private String walletPath;
    private File walletDir;
    //FIXME: Add your own password here
    private final String password = "PrivateKey";//16FB976643702D2E530D0ABA9A2E38B8E44E0A3B6B3C330CD41BB794BDC96DA6"; //Copy from datamask
    private final static String privateKeyRopsten = "16FB976643702D2E530D0ABA9A2E38B8E44E0A3B6B3C330CD41BB794BDC96DA6"; //Privekey export form Menu, DataMask
    private final static String contractAddressRopsten = "0x6f338949af6df038d715a5e07d10f3a08cb0d68e";
    private final static String toAddress = "0x81b7e08f65bdf5648606c89998a9cc8164397647";


    private org.web3j.crypto.Credentials credentials = org.web3j.crypto.Credentials.create(privateKeyRopsten);
    private int minimumGasLimit = 21000;
    private BigInteger gasLimit = new BigInteger(String.valueOf(minimumGasLimit));
    BIoT contract;
    private MainActivity activity = new MainActivity();
    private boolean isResultOK = false;


    public boolean checkResult(){return isResultOK;}
    public void resetResult(){isResultOK = false;}
    public static String getFarmname() { return farmName; }
    public static String getFarmaddress(){return farmAddress;}

    public static String getFoodname() {
        return foodName;
    }

    public static String getPosition() { return farmPosition; }

    public static String getTemperature() {
        return temperature;
    }

    public static String getHumidity() {
        return humidity;
    }

    public static String getContractAddress() {
        return contractAddress;
    }

    public void connectToEthNetwork() {

        web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/47c63d5fab244e4ba2311dd0a9ce35eb"));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();

            if (!clientVersion.hasError()) {
                Log.d("AAA", "Connected to Ethereum");
                //Toast.makeText(MyApplication.getAppContext(), "Connected successfully to Ethereum network!", Toast.LENGTH_SHORT).show();

            } else {
                //toastAsync(clientVersion.getError().getMessage());
                Log.d("AAA", "Error Connection to Ethereum" + clientVersion.getError().getMessage());
                //Toast.makeText(MyApplication.getAppContext(), "Error on connecting to Ethereum network is " + clientVersion.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("AAA", "Exception Connection to Ethereum" + e.getMessage());
            //Toast.makeText(MyApplication.getAppContext(), "Exception Connection to Ethereum!" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void createWallet() {
        try {
            WalletUtils.generateNewWalletFile(password, walletDir);
            Log.d("AA", "Wallet generated ");

        } catch (Exception e) {
            Log.d("AA", "Error Wallet generated " + e.getMessage());

        }
    }

    public void getAddress() {
        try {
            //org.web3j.crypto.Credentials credentials = WalletUtils.loadCredentials(password,  walletDir);
            Log.d("AA", "Address of wallet is " + credentials.getAddress());
            //toastAsync("Your address is " + credentials.getAddress());
        } catch (Exception e) {
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
        //Toast.makeText(MyApplication.getAppContext(), "Start to deploy the smart contract", Toast.LENGTH_SHORT).show();

        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        //((DefaultGasProvider) contractGasProvider).GAS_PRICE = BigInteger.valueOf(1000);
        Log.d("AAA", "GAS_Price is " + contractGasProvider.getGasPrice().toString());
        Log.d("AAA", "GAS_LIMIT is " + contractGasProvider.getGasLimit().toString());
        farmName = activity.getTxtFarmName();
        farmAddress = activity.getTxtFarmAddress();
        humidity = activity.getTxtHumidity();
        temperature = activity.getTxtTemperature();
        foodName = activity.getTxtFoodname();

        String latlong = "Lat: "+ String.format("%.01f",activity.getLatLng().latitude) + ", Long: " + String.format("%.01f",activity.getLatLng().longitude);


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

                    contractAddress = contract.getContractAddress();
                    //Toast.makeText(activity.getContext(), "The smart contract is created at address " + contractAddress, Toast.LENGTH_SHORT).show();

                    //byte[] myStringInByteFoodname = Numeric.hexStringToByteArray(asciiToHex(foodname));
                    contract.setFarm_FoodInfor(farmName, farmAddress, latlong, foodName ,humidity, temperature).send();
                    //byte[] myStringInByteLatLong = Numeric.hexStringToByteArray(asciiToHex(latlong));
                    //Toast.makeText(MyApplication.getAppContext(), "Food information has been created successfully in the smart contract! " + contractAddress, Toast.LENGTH_SHORT).show();

                    //contract.setTemperature(myStringInByte2).send();
                    //Need to save this address to QR code information
                    //hexToASCII(Numeric.toHexStringNoPrefix(contract.getFarmName().send())));
                    Log.d("AAA", "Smart contract deployed to address " + contractAddress);
                    Log.d("AAA", "Farm name original is  " + contract.getFarm_FoodInfor().send().getValue1());

                    //Update value of Humidity and Temperature

                } catch (Exception e) {
                    Log.d("AAA", "Error in deploying contract " + e.getMessage().toString());
                    //e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    public void readSmartContract(String smartcontractAddress){
        Thread threadread = new Thread(new Runnable() {
            @Override
            public void run() {
                connectToEthNetwork();
                ContractGasProvider contractGasProvider = new DefaultGasProvider();
                contract = BIoT.load(smartcontractAddress, web3j, credentials, ((DefaultGasProvider) contractGasProvider).GAS_PRICE,
                        //BigInteger.valueOf(1000),
                        ((DefaultGasProvider) contractGasProvider).GAS_LIMIT );
                //String humidity = String.valueOf(contract.getHumidity());
                //String temperature = String.valueOf(contract.getTemperature());
                //Update on the Google map?

                try {
                    //hexToASCII
                    //farmname = hexToASCII(Numeric.toHexStringNoPrefix(contract.getFarmName().send()));
                    farmName = contract.getFarm_FoodInfor().send().getValue1();
                    farmAddress = contract.getFarm_FoodInfor().send().getValue2();
                    farmPosition = contract.getFarm_FoodInfor().send().getValue3();
                    foodName = contract.getFarm_FoodInfor().send().getValue4();
                    humidity =contract.getFarm_FoodInfor().send().getValue5();
                    temperature = contract.getFarm_FoodInfor().send().getValue6();
                    //Toast.makeText(MyApplication.getAppContext(), "Read succesfully the food information on Ethereum smart contract", Toast.LENGTH_SHORT).show();

                    isResultOK = true;

                    Log.d("AAA", "Foodname reading from etherum contract is " + foodName);
                    Log.d("AAA", "Position reading from etherum contract is " + farmPosition);
                    Log.d("AAA", "Humidiy reading from etherum contract is " + humidity);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        threadread.start();

    }



    //Bytes32 -> byte[]
    public static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }

    public static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    // String to 64 length HexString (equivalent to 32 Hex lenght)
    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));

    }
}
