#include <Pixy2.h>
#include <Wire.h>

Pixy2 pixy;
//built in class from arduino, strongly suggest looking at it on their website
//it is not a complicated class



//this is provided by the pixy creators, you will have to go to the arduino sketch editor, 
//click sketch, include library, and import the pixy .zip files

//SETUP THE DEVICES

//plug sda on RoboRIO into A4
//plug scl on RoboRIO into A5
//connect the two grounds


  String piOutput = "none";//string to be sent to the robot
               
  String input = "blank";  //string received from the robot
  const String PIXY = "pi";

void setup(){
  Serial.begin(115200);
  Serial.println("setup");  
  Wire.begin(4);                // join i2c bus with address #4 as a slave device
  Wire.onReceive(receiveEvent); // Registers a function to be called when a slave device receives a transmission from a master
  Wire.onRequest(requestEvent); // Register a function to be called when a master requests data from this slave device
    

  pixy.init();
}

void loop(){

  uint16_t blocks = pixy.ccc.getBlocks();//use this line to get every available object the pixy sees
  //^^^not sure what exactly this is for, honestly
  int biggest = -1;
  double area = 0, temp;
  for(int i=0;i<blocks;i++){
    if(pixy.ccc.blocks[i].m_signature == 3){ //if checking for an object and have more than one "type" or color to choose from
                         //use this line and choose the signature or "color" you want
      temp = pixy.ccc.blocks[i].m_width * pixy.ccc.blocks[i].m_height;
      if(temp > area){
        area = temp;
        biggest = i;
      }
    }
      //that loops finds the biggest object since sometimes the object you are looking for becomes 
      //broken up into multiple, smaller, objects
  }
  if(!blocks){
    piOutput = "none"; //if no blocks tell roborio there are none 
  }else{
    piOutput = String(pixy.ccc.blocks[biggest].m_x / 78.0);  //turns into a percent of the screen 
    piOutput += "|";                //inserts a "pipe" so robrio can split the numbers later
    piOutput += String(pixy.ccc.blocks[biggest].m_y / 51.0);
    piOutput += "|";          
    piOutput += String(area); 
    Serial.println(piOutput);

  }

  delay(70); //gives time for everything to process
}

void requestEvent(){//called when RoboRIO request a message from this device
  Wire.write(piOutput.c_str()); //writes data to the RoboRIO, converts it to string
  Serial.println("request");
}

void receiveEvent(int bytes){//called when RoboRIO "gives" this device a message

}
