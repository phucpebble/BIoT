# Applying blockchain for IoT to help manage food safety on food supply chain

### Overview

BIoT is a solution combining cutting-edege technologies such ash blockchain, IoT, machine learning to deal with food safety issue on a supply chain. The demo app on Java Android demonstrates how to improve the transparency and accessibility of food information from a farm to customers.

## Project structure

- Hardware: Raspberry Pi 3B+, sensor temperature and humidity DHT11 
- Data processing: Machine learning – Tensorflow, Blockchain – web3j 
- Cloud: Ethereum Ropsten testnet and Infura
- Protocol & connection: Bluetooth, HTTPS, QR code. 
- Programming language: Python, Java Android, Solidity (Ethereum smart contract).

### Prerequisites

* Hardware installation

The physical connection between Raspberry and sensor humidity and temperature (DHT11):
![IoT and sensor connection](https://github.com/henryphamit/BIoT/blob/master/demo/Sensor_IoT.png)
The actual image of raspberry pi and DHT sensor with breadboard:
![real image of equipments](https://github.com/henryphamit/BIoT/blob/master/demo/realImage.png)
 

* Blockchain cloud setup
- Metamask wallet

 ![Wallet](https://github.com/henryphamit/BIoT/blob/master/demo/MetaMaskWallet.png)
 
- Ropsten Ethereum testnet

![](https://github.com/henryphamit/BIoT/blob/master/demo/InfuraProjectsetup.png)

### Demo

Video demo link: https://lnkd.in/gDXNFDB

![screen shoot](https://github.com/henryphamit/BIoT/blob/master/demo/demoimages.png)


The demo flow:
In the beginning, the farmer scans food and gets automatically food name (applied machine learning), other information can be collected through IoT device (Raspberry) and sensors such as Humidity, Temperature and GPS.
Combining with farm information, the data of food is created on Ethereum blockchain. And then, a QR code is generated based on the address of smart contract. Information of food on supply chain can be added continuously based on the events on supply chain.
In the end, the consumer can scan the QR code on food, traceback all food information on supply chain to know the quality of the product. All recorded data of food on Ethereum blockchain is immutable, so this prevents food fraud.

