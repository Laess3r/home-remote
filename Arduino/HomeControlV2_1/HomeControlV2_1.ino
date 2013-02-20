
/*********************************************************************************
 *  H O M E   C O N T R O L   V 2
 *
 *  Control remote plugs, get temperatures and control LCD over serial interface
 *
 *  (C) 2012 by Stefan Huber
 *********************************************************************************/

/**
 * SERIAL TRANSFER PROTOCOL: 
 * -------------------------
 * 
 * first byte zero -> send to LCD
 * 					-> following bytes sent to LCD as message
 * 
 * first byte one -> handle Remote
 * 					-> 2nd byte: plug ID (A,B,C,D..)
 * 					-> 3rd byte: plug status (off 0/ on 1)
 * 					-> following bytes sent to LCD as message
 * 
 * first byte two -> handle temp sensor
 * 					-> 2nd byte: temp sensor nr (A,B..)
 * 					-> following bytes sent to LCD as message
 */
#include <RCSwitch.h>
#include <LiquidCrystal.h>
#include <dht11.h>
#include <OneWire.h>
#include <DallasTemperature.h>

#define OUTSIDE_TEMP_PIN A4
#define RemotePin A2
#define PEEP A3
#define DHT11PIN A1

OneWire oneWire(OUTSIDE_TEMP_PIN);
DallasTemperature sensors(&oneWire);
dht11 DHT11;
RCSwitch mySwitch = RCSwitch();
LiquidCrystal lcd(8, 9, 4, 5, 6, 7); //TODO dim? ev pin10
int inputCounter;
char bufferedInput[200];
int col;

void setup() {
  pinMode(PEEP, OUTPUT);
  Serial.begin(9600);

  sensors.begin();
  lcd.begin(16, 2);
  lcd.clear();
  lcd.write("HomeControl v2.f");
  lcd.setCursor(0, 1);
  lcd.write("       by sHuber");
  lcd.setCursor(0, 0);
  digitalWrite(PEEP,HIGH);
  pinMode(RemotePin, OUTPUT);
  mySwitch.enableTransmit(RemotePin);

  sound(4);
}

void loop()
{
  if (Serial.available()) {
    
    DHT11.read(DHT11PIN);
    
    delay(50);
    readToBuffer();
    int type = bufferedInput[0];

    if(type == 0){  //TODO use #DEFINE for this !!! 
      // direct write to LCD
      writeToLcd(1);
    }
    else if(type == 1){
      // handle remote plug
      handlePlug();
    } 
    else if (type == 2){
      // handle temp sensor
      handleTemp();
    }
  }
}

void sound(int count){
  int i =0;
  for(i = 0; i<count;i++){
    digitalWrite(PEEP,HIGH);
    delay(1);
    digitalWrite(PEEP,LOW);
    if(count > 1)
      delay(200);
  }
}

void handleTemp(){
  
  DHT11.read(DHT11PIN);

  char tempSensorId = bufferedInput[1];
  writeToLcd(2);

  switch(tempSensorId){
  case 'A':
    Serial.print("TA");
    Serial.print((float)DHT11.temperature, 2);
    Serial.print("TH");
    Serial.println((float)DHT11.humidity, 2);
    break;
  case 'B':
    Serial.print("TB");
    sensors.requestTemperatures();
    Serial.println(sensors.getTempCByIndex(0));
    break;
  case 'X':
    sensors.requestTemperatures();
    Serial.print("XTA");
      Serial.print((float)DHT11.temperature, 2);
      Serial.print("TH");
      Serial.print((float)DHT11.humidity, 2);
    Serial.print("TB");
    Serial.println(sensors.getTempCByIndex(0));
    break;
  default:
    break;
  }
}

void handlePlug(){
  sound(1);
  int plugId = bufferedInput[1]; // A, B, C, D
  int isEnabled = bufferedInput[2]; // 1 is true, 0 is false
  int plugNr = 0;
  char* prefix;
  switch(plugId){
  case 'A':
  case 'a':
    plugNr = 1; 
    prefix = "01011";
    break;
  case 'B':
  case 'b':
    plugNr = 2; 
    prefix = "01011";
    break;
  case 'C':
  case 'c':
    plugNr = 3; 
    prefix = "01011";
    break;
  case 'D':
  case 'd':
    plugNr = 4; 
    prefix = "01011";
    break;
  case 'E':
  case 'e':
    plugNr = 5; 
    prefix = "01011";
    break;
  case 'X':
  case 'x':
    plugNr = 1; 
    prefix = "01001";
    break;
  default:
    break;
  }
  if(isEnabled == 1){
    mySwitch.switchOn(prefix, plugNr); 
  } 
  else{
    mySwitch.switchOff(prefix, plugNr);
  }
  
  writeToLcd(3);
  Serial.println("ok");
}

// Buffer methods
void readToBuffer(){
  clearBuffer();
  inputCounter = 0;
  while (Serial.available() > 0) {
    bufferedInput[inputCounter] = Serial.read();
    inputCounter++;
  }
  if (inputCounter == 127 || inputCounter == 128)
    while (Serial.available() > 0) {
      bufferedInput[inputCounter] = Serial.read();
      inputCounter++;
    }
}

void writeToLcd(int offset){
  lcd.clear();
  lcd.setCursor(0,0);
  int position = 0;
  int lineBreak = 0;
  int nextLine = 0;
  for(position = 0; position < inputCounter; position++){

    char chara = bufferedInput[position+offset];

    if(chara == '\0'){
      // lcd.write(chara);
      break;
    }
    if(chara == 10){
      nextLine = 1;
      lineBreak++;
      continue; 
    }

    if(nextLine == 0){
      lcd.setCursor(position,0);
      lineBreak++;
    }
    else{
      lcd.setCursor(position-lineBreak,1);
    }
    lcd.write(chara);
    //    lcd.setCursor(position+1,1);  
  }
}

void clearBuffer()
{
  for (inputCounter=0;inputCounter<150;inputCounter++) {
    bufferedInput[inputCounter] = '\0';
  }
}

