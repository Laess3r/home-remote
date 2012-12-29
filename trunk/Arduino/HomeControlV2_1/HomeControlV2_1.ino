
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
#include <aLM335.h>

RCSwitch mySwitch = RCSwitch();
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
int inputCounter;
char bufferedInput[200];
int col;

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  lcd.clear();
  lcd.write("HomeControl v2.d");
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

void handleTemp(){
  
  aLM335 tempA(1,5); //readVcc()
  aLM335 tempB(2,5); //readVcc()
  
  char tempSensorId = bufferedInput[1];
    writeToLcd(2);

  switch(tempSensorId){
  case 'A':
    Serial.print("TA");
    Serial.println(tempA.getCelsius()-37);
    break;
  case 'B':
    Serial.print("TB");
    Serial.println(tempB.getCelsius()-36);
    break;
    case 'X':
    Serial.print("XTA");
    Serial.print(tempA.getCelsius()-37);
    Serial.print("TB");
    Serial.println(tempB.getCelsius()-36);
    break;
  default:
    break;
  }
}

void handlePlug(){

  int plugId = bufferedInput[1]; // A, B, C, D
  int isEnabled = bufferedInput[2]; // 1 is true, 0 is false
  int plugNr = 0;
  char* prefix;
  switch(plugId){
  case 'A':
  case 'a':
    plugNr = 1; 
    prefix = "11111";
    break;
  case 'B':
  case 'b':
    plugNr = 2; 
    prefix = "11111";
    break;
  case 'C':
  case 'c':
    plugNr = 3; 
    prefix = "11111";
    break;
  case 'D':
  case 'd':
    plugNr = 4; 
    prefix = "11111";
    break;
  case 'E':
  case 'e':
    plugNr = 5; 
    prefix = "11111";
    break;
  case 'X':
  case 'x':
    plugNr = 1; 
    prefix = "11110";
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


/**
long readVcc() {
  // Read 1.1V reference against AVcc
  // set the reference to Vcc and the measurement to the internal 1.1V reference
  #if defined(__AVR_ATmega32U4__) || defined(__AVR_ATmega1280__) || defined(__AVR_ATmega2560__)
    ADMUX = _BV(REFS0) | _BV(MUX4) | _BV(MUX3) | _BV(MUX2) | _BV(MUX1);
  #elif defined (__AVR_ATtiny24__) || defined(__AVR_ATtiny44__) || defined(__AVR_ATtiny84__)
    ADMUX = _BV(MUX5) | _BV(MUX0);
  #elif defined (__AVR_ATtiny25__) || defined(__AVR_ATtiny45__) || defined(__AVR_ATtiny85__)
    ADMUX = _BV(MUX3) | _BV(MUX2);
  #else
    ADMUX = _BV(REFS0) | _BV(MUX3) | _BV(MUX2) | _BV(MUX1);
  #endif  
 
  delay(2); // Wait for Vref to settle
  ADCSRA |= _BV(ADSC); // Start conversion
  while (bit_is_set(ADCSRA,ADSC)); // measuring
 
  uint8_t low  = ADCL; // must read ADCL first - it then locks ADCH  
  uint8_t high = ADCH; // unlocks both
 
  long result = (high<<8) | low;
 
  result = 1125300L / result; // Calculate Vcc (in mV); 1125300 = 1.1*1023*1000
  return result; // Vcc in millivolts
}
**/
