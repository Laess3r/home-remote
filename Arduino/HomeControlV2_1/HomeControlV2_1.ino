
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

RCSwitch mySwitch = RCSwitch();
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
int inputCounter;
char bufferedInput[200];
int col;

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  lcd.clear();
  lcd.write("HomeControl v2.b");
  lcd.setCursor(0, 1);
  lcd.write("       by sHuber");
  lcd.setCursor(0, 0);

  pinMode(3, OUTPUT);
  mySwitch.enableTransmit(3);
}

void loop()
{
  //    aLM335 tempsensorA (A1, 4 ); 
  //  aLM335 tempsensorB (A2, 4 );

  if (Serial.available()) {
    delay(100);
    readToBuffer();

    int type = bufferedInput[0];

    if(type == 0){
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

void handleTemp(){
  char tempSensorId = bufferedInput[1];

  float temp_in_kelvin_A = analogRead(1) * 0.004882812 * 100;
  float temp_in_celsius_A = temp_in_kelvin_A - 2.5 - 273.15;
  float temp_in_kelvin_B = analogRead(2) * 0.004882812 * 100;
  float temp_in_celsius_B = temp_in_kelvin_B - 2.5 - 273.15;

    writeToLcd(2);

  switch(tempSensorId){
  case 'A':
    Serial.print("TA");
    Serial.println(temp_in_celsius_A);
    break;
  case 'B':
    Serial.print("TB");
    Serial.println(temp_in_celsius_B);
    break;
    case 'X':
    Serial.print("XTA");
    Serial.print(temp_in_celsius_A);
    Serial.print("TB");
    Serial.println(temp_in_celsius_B);
    break;
  default:
    break;
  }
}

void handlePlug(){

  int plugId = bufferedInput[1]; // A, B, C, D
  int isEnabled = bufferedInput[2]; // 1 is true, 0 is false
  int plugNr = 0;
  switch(plugId){
  case 'A':
  case 'a':
    plugNr = 1; 
    break;
  case 'B':
  case 'b':
    plugNr = 2; 
    break;
  case 'C':
  case 'c':
    plugNr = 3; 
    break;
  case 'D':
  case 'd':
    plugNr = 4; 
    break;
  default:
    break;
  }
  if(isEnabled == 1){
    mySwitch.switchOn("11111", plugNr); 
  } 
  else{
    mySwitch.switchOff("11111", plugNr);
  }
  writeToLcd(3);
  Serial.println("Plug updated");
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

