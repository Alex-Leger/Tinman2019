//SETUP THE DEVICES

//plug sda on RoboRIO into A4
//plug scl on RoboRIO into A5
//connect the two grounds


#include <Pixy2.h>      //this is provided by the pixy creators, you will have to go to the arduino sketch editor,
#include <Wire.h>       //built in class from arduino, strongly suggest looking at it on their website
#include <FastLED.h>

#define LED_PIN     7
#define NUM_LEDS    92

Pixy2 pixy;
CRGB leds[NUM_LEDS];

String piOutput = String(0);//string to be sent to the robot
String input = "blank";  //string received from the robot
const String PIXY = "pi";

int error;

void setup() {
  Serial.begin(115200);
  Serial.println("setup");
  Wire.begin(4);                // join i2c bus with address #4 as a slave device
  Wire.onReceive(receiveEvent); // Registers a function to be called when a slave device receives a transmission from a master
  Wire.onRequest(requestEvent); // Register a function to be called when a master requests data from this slave device
  FastLED.addLeds<WS2812, LED_PIN, GRB>(leds, NUM_LEDS);
  pixy.init();
  
  for (int i = 0; i < 92; i++)    //Powers on all LEDs and sets them to green
  {
    leds[i] = CRGB(0, 200, 0);
    FastLED.show();
    delay(30);
  }
}

void loop() {
  uint16_t blocks = pixy.ccc.getBlocks();//use this line to get every available object the pixy sees
  //^^^unsigned 16 bit integer 
  pixy.ccc.getBlocks();
  int Lclosest = 0;
  int Lclosestdiffs = 1000;
  int Rclosest = 0;
  int Rclosestdiffs = 1000;

  if (blocks < 2)
  {
    piOutput = String(-9999); //if less than 2 blocks, output -9999
  }
  else
  {
    for (int i = 0; i < pixy.ccc.numBlocks; i++) {
      if (pixy.ccc.blocks[i].m_x > pixy.frameWidth / 2) {
        continue;
      }
      int diff = (pixy.frameWidth / 2) - pixy.ccc.blocks[i].m_x;    //
      if (diff > Lclosestdiffs) {
        Lclosest = i;
        Lclosestdiffs = diff;
      }
    }
    for (int i = 0; i < pixy.ccc.numBlocks; i++) {
      if (pixy.ccc.blocks[i].m_x < pixy.frameWidth / 2) {
        continue;
      }
      int diff = (pixy.ccc.blocks[i].m_x - pixy.frameWidth / 2);
      if (diff > Rclosestdiffs) {
        Rclosest = i;
        Rclosestdiffs = diff;
      }
    }
    error =  pixy.frameWidth / 2 - ((pixy.ccc.blocks[Rclosest].m_x + pixy.ccc.blocks[Lclosest].m_x) / 2);
    piOutput = error;
  }


  delay(70); //gives time for everything to process
}

void requestEvent() { //called when RoboRIO request a message from this device
  Wire.write(piOutput.c_str()); //writes data to the RoboRIO, converts it to string
  Serial.println("request");
}

void receiveEvent(int bytes) { //called when RoboRIO "gives" this device a message

}
