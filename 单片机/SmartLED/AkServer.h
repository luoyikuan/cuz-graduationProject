#ifndef _AkServer_H
#define _AkServer_H

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

class AkServer
{
protected:
    String host;
    String mac;
    String token;
    bool isRegister = false;
    bool doPost(String uri, StaticJsonDocument<256> *httpEntity);

public:
    AkServer(String host, String mac, String token);
    bool getRegister();
    bool registerMcu();
    bool putData(String data);
    enum RESTfulCode
    {
        OK = 100,
        ERROR = -1
    };
};

#endif