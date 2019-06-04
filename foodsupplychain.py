import os
import Adafruit_DHT
import glob
import time
import RPi.GPIO as GPIO
from bluetooth import *

os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.OUT)

# Set sensor type : Options are DHT11,DHT22 or AM2302
sensor=Adafruit_DHT.DHT11
 
# Set GPIO sensor is connected to
gpio=17
 
# Use read_retry method. This will retry up to 15 times to
# get a sensor reading (waiting 2 seconds between each retry).
humidity, temperature = Adafruit_DHT.read_retry(sensor, gpio)


# Reading the DHT11 is very sensitive to timings and occasionally
# the Pi might fail to get a valid reading. So check if readings are valid.
if humidity is not None and temperature is not None:
  print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity))
else:
  print('Failed to get reading. Try again!')

server_sock=BluetoothSocket(RFCOMM)
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]
uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service(server_sock, "Foodsupplychain",
                service_id = uuid,
                service_classes = [uuid, SERIAL_PORT_CLASS],
                profiles = [SERIAL_PORT_PROFILE],
                #protocols =[OBEX_UUID]
)
while True:
    print "Waiting for connection on RFCOMM channel %d" % port
    client_sock, client_info = server_sock.accept()
    print "Accepted connection from ", client_info

    try:
        data = client_sock.recv(1024)
        if len(data) ==0: break
        print "received [%s]" % data

        if data == 'temp_humidity':
            #read temperature and humidity from the board
            data = '{0:0.1f}oC  {1:0.1f}%'.format(temperature, humidity)+'!'
        elif data == 'lightOn':
            GPIO.output(17,False)
            data ='Led on!'
        elif data == 'lightOff':
            GPIO.output(17,True)
            data ='led off!'
        else:
            data = 'WTF !'
        client_sock.send(data)
        print "sending [%s]" % data

    except IOError:
            pass
    except KeyboardInterrupt:
            print "disconnected"
            client_sock.close()
            server_sock.close()
            print "all done"
            break