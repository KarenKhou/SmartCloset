#include <SoftwareSerial.h>
SoftwareSerial BTSerial(3, 2); //tx rx

const int trigPin = 9;
const int echoPin = 7;
long duration;
int distance;
bool closetclosed=true;


void setup() {
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  Serial.begin(9600); // same baude rate as the HC05 module
  BTSerial.begin(9600); // au lieu de 9600

  Serial.println("Bluetooth prêt !");
}


void loop() {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  duration = pulseIn(echoPin, HIGH);
  distance = duration * 0.034 / 2;

  Serial.print("Distance: ");
  Serial.println(distance);

  if (distance > 50 and closetclosed) {
    Serial.println("ALERT");
    BTSerial.println("ALERT");
    
    closetclosed = false;
  }else if (distance<=50){
    closetclosed = true;
  }
  


  delay(1000);
}

