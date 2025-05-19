// #include <SoftwareSerial.h>
// SoftwareSerial BTSerial(3, 2); //tx rx

// const int trigPin = 8;
// const int echoPin = 7;
// long duration;
// int distance;
// bool closetclosed=true;


// void setup() {
//   pinMode(trigPin, OUTPUT);
//   pinMode(echoPin, INPUT);
//   Serial.begin(9600); // same baude rate as the HC05 module
//   BTSerial.begin(9600); // au lieu de 9600

//   Serial.println("Bluetooth prêt !");
// }


// void loop() {
//   digitalWrite(trigPin, LOW);
//   delayMicroseconds(2);
//   digitalWrite(trigPin, HIGH);
//   delayMicroseconds(10);
//   digitalWrite(trigPin, LOW);

//   duration = pulseIn(echoPin, HIGH);
//   distance = duration * 0.034 / 2;

//   Serial.print("Distance: ");
//   Serial.println(distance);

//   if (distance > 50 and closetclosed) {
//     Serial.println("ALERT");
//     BTSerial.println("ALERT");
    
//     closetclosed = false;
//   }else if (distance<=50){
//     closetclosed = true;
//   }
  


//   delay(1000);
// }
#include <SoftwareSerial.h>
SoftwareSerial BTSerial(3, 2); //tx rx

const int trigPin = 8;
const int echoPin = 7;
long duration;
int distance;
bool closetclosed=true;
#define RED_PIN    9
#define GREEN_PIN  10
#define BLUE_PIN   11


void setup() {
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);
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
    Serial.print("Distance: ");
    Serial.println(distance);
    
    
    closetclosed = false;
  }else if (distance<=50){
    closetclosed = true;
    if (closetclosed) {
    analogWrite(RED_PIN, 0);
    analogWrite(GREEN_PIN, 0);
    analogWrite(BLUE_PIN, 0);
}
    Serial.print("Distance: ");
    Serial.print("closet closed");
  }

  if (closetclosed==false) {
    Serial.print("closet open");
    colorFade(255, 0, 0, 0, 255, 0);   // Red to Green
    colorFade(0, 255, 0, 0, 0, 255);   // Green to Blue
    colorFade(0, 0, 255, 255, 0, 0);   // Blue to Red
  }
  


  delay(500);
}

void colorFade(int r1, int g1, int b1, int r2, int g2, int b2) {
  for (int i = 0; i <= 255; i++) {
    if (closetclosed) return;
    int r = map(i, 0, 255, r1, r2);
    int g = map(i, 0, 255, g1, g2);
    int b = map(i, 0, 255, b1, b2);

    analogWrite(RED_PIN, r);
    analogWrite(GREEN_PIN, g);
    analogWrite(BLUE_PIN, b);

    delay(10); // smoothness
  }
}


