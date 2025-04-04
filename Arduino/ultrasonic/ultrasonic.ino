#include <SoftwareSerial.h>
SoftwareSerial BTSerial(3, 2); //tx rx

const int trigPin = 9;
const int echoPin = 7;
long duration;
int distance;


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

  if (distance < 50) {
    Serial.println("ALERT"); // C’est cette info que tu peux capter sur ton app
    BTSerial.println("ALERT");
  }
  


  delay(1000);
}

