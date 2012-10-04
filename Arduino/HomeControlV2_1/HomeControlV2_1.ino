
/*********************************************************************************
 *  H O M E   C O N T R O L   V 2
 *
 *  Control remote plugs, get temperatures and control LCD over serial interface
 *
 *  (C) 2012 by Stefan Huber
 *********************************************************************************/

/**
 * *
 * Gleichzeitig Daten an Display senden?
 * 
 * So auf die Art: Wenn das erste incoming Byte 0 ist, dann wird Display geupdated mit Daten aus folgenden bytes
 * 
 * wenn das erste byte 1 ist, ist das zweite byte der fernbedienungs-code
 * 
 * webb das erste byte 2 ist, ist das zweite byte die nummer des temp-sensors
 * 
 * Werte byte 2:
 * 
 * (wenn byte 1 == 1)
 * byte 2:
 * A, B, C, D, E, F, G ....
 * byte 3: 
 * 1, 0 (on, off)
 * 
 * (wenn byte 1 == 2)
 * 1 : Temp sensor 1
 * 2 : Temp sensor 2
 */

#include <RCSwitch.h>
#include <LiquidCrystal.h>

RCSwitch mySwitch = RCSwitch();
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
int inputCounter;
char bufferedInput[200];
int col;
//todo temp sensor library

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
  if (Serial.available()) {
    delay(100);
    readToBuffer();

    int type = bufferedInput[0]; //1 -> plug, 2 -> temp sensor

    if(type == 1){
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
    }
          Serial.println("Finished");
  }
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




