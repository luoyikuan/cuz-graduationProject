#ifndef _AkMQ_H
#define _AkMQ_H

#include <ESP8266HTTPClient.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

class AkMQ
{
protected:
    String mac;
    String mqttServer;
    void reconnect();

public:
    AkMQ(String mqttServer, String mac);
    void begin();
    void loop();
    void send(bool autoMode, bool ledState, uint32_t color, String light);
};

void AK_MQ_Callback(StaticJsonDocument<256> *msg);
#endif