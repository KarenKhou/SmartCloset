// const int trigPin = 9;
// const int echoPin = 7;
// long duration;
// int distance;

// #include <SoftwareSerial.h>

// SoftwareSerial BTSerial(11, 10); // RX | TX (pins connectés à TXD et RXD du HC-05)

// void setup() {
//   pinMode(trigPin, OUTPUT);
//   pinMode(echoPin, INPUT);
//   Serial.begin(9600); 
//   BTSerial.begin(9600); // au lieu de 9600

//   Serial.println("Bluetooth prêt !");
// }


// void loop() {
//   //digitalWrite(trigPin, LOW);
//   // delayMicroseconds(2);
//   // digitalWrite(trigPin, HIGH);
//   // delayMicroseconds(10);
//   // digitalWrite(trigPin, LOW);

//   // duration = pulseIn(echoPin, HIGH);
//   // distance = duration * 0.034 / 2;

//   // Serial.print("Distance: ");
//   // Serial.println(distance);

//   // if (distance < 50) {
//   //   Serial.println("ALERT"); // C’est cette info que tu peux capter sur ton app
//   // }
  
//   BTSerial.println("Hello from Arduino");
//   delay(1000);
//   if (BTSerial.available()) {
//     char c = BTSerial.read();
//     BTSerial.print("Reçu : ");
//     BTSerial.println(c);
//   }


//   delay(500);
// }


#include <SoftwareSerial.h>
SoftwareSerial BTSerial(3, 2); // RX, TX (HC-05 TXD → D3, RXD ← D2 via divider)

void setup() {
  BTSerial.begin(9600); // Use the same baud rate as HC-05
}

void loop() {
  BTSerial.println("Hello from Arduino 👋");
  delay(1000);
}
