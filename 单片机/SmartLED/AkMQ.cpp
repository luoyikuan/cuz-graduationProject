#include "AkMQ.h"

static WiFiClient AK_MQ_ESP_CLIENT;
static PubSubClient AK_MQTT_CLIENT(AK_MQ_ESP_CLIENT);
void AK_MQ_onMessageListener(char *topic, byte *payload, unsigned int length)
{
    StaticJsonDocument<256> msg;
    deserializeJson(msg, payload, length);
    AK_MQ_Callback(&msg);
}

AkMQ::AkMQ(String mqttServer, String mac)
{
    this->mqttServer = mqttServer;
    this->mac = mac;
}

void AkMQ::begin()
{
    AK_MQTT_CLIENT.setServer(this->mqttServer.c_str(), 1883);
    AK_MQTT_CLIENT.setCallback(AK_MQ_onMessageListener);
}

void AkMQ::loop()
{
    if (!AK_MQTT_CLIENT.connected())
    {
        this->reconnect();
    }
    AK_MQTT_CLIENT.loop();
}

void AkMQ::send(bool autoMode, bool ledState, uint32_t color, String light)
{
    String topic = "mcu/data/" + this->mac;

    StaticJsonDocument<256> json;
    json["auto"] = autoMode;
    json["state"] = ledState;
    json["color"] = color;
    json["light"] = light;

    String msg;
    serializeJson(json, msg);

    AK_MQTT_CLIENT.publish(topic.c_str(), msg.c_str());
}

void AkMQ::reconnect()
{
    while (!AK_MQTT_CLIENT.connected())
    {
        String clientId = "esp8266-" + this->mac;
        if (AK_MQTT_CLIENT.connect(clientId.c_str()))
        {
            String topic = "mcu/cmd/" + this->mac;
            AK_MQTT_CLIENT.subscribe(topic.c_str());
        }
        else
        {
            delay(1000);
        }
    }
}
