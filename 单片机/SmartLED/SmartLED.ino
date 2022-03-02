#include <ESP8266WiFi.h>
#include <Ticker.h>

#include "AkServer.h"
#include "AkMQ.h"
#include "ColorLED.h"
#include "BH1750FVI.h"

const char *WIFI_SSID = "盘尼西林";
const char *WIFI_PWD = "19910524";

String SERVER_HOST = "http://192.168.31.127:80";
String TOKEN = "af3bda13d5f04ebaaaf47c467239d639";
String mqttServer = "192.168.31.127";

AkServer akServer(SERVER_HOST, WiFi.macAddress(), TOKEN);
AkMQ akMQ(mqttServer, WiFi.macAddress());
ColorLED colorLED(14, 12, 13);
BH1750FVI bh1750fvi;

Ticker ticker;
unsigned int countMQ = 0;
unsigned int countServer = 0;

const float luxThreshold = 50;
float lux = 0;
bool autoMode = true;

void setup()
{
    // Serial.begin(115200);

    /* WiFi 连接 */
    WiFi.mode(WIFI_STA);
    WiFi.begin(WIFI_SSID, WIFI_PWD);
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
    }

    /* 连接云服务器 */
    while (!akServer.registerMcu())
    {
        delay(500);
    }

    /* MQ初始化 */
    akMQ.begin();

    /* BH1750FVI初始化 */
    bh1750fvi.begin();

    /* 定时器 */
    ticker.attach(1, timer);
}

void loop()
{
    akMQ.loop();

    if (countMQ > 2)
    {
        lux = bh1750fvi.getLux();
        String luxStr = String(lux) + "lux";
        akMQ.send(autoMode, colorLED.getState(), colorLED.getColor(), luxStr);
        countMQ = 0;
    }

    if (countServer > 9)
    {
        lux = bh1750fvi.getLux();
        String luxStr = String(lux) + "lux";
        akServer.putData(luxStr);
        countServer = 0;
    }

    if (autoMode)
    {
        colorLED.setState(lux < luxThreshold);
    }
}

/**
 * @brief MQ回调
 *
 * @param msg
 */
void AK_MQ_Callback(StaticJsonDocument<256> *msg)
{
    String cmd = (*msg)["cmd"];
    if (cmd == "auto")
    {
        autoMode = (*msg)["data"];
    }
    else if (cmd == "state")
    {
        bool state = (*msg)["data"];
        colorLED.setState(state);
    }
    else if (cmd == "color")
    {
        uint32_t color = (*msg)["data"];
        colorLED.setColor(color);
    }
}

void timer()
{
    countMQ++;
    countServer++;
}
