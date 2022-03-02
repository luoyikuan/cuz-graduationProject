#include "AkServer.h"

/**
 * @brief Construct a new Ak Server:: Ak Server object
 *
 * @param host
 * @param mac
 * @param token
 */
AkServer::AkServer(String host, String mac, String token)
{
    this->host = host;
    this->mac = mac;
    this->token = token;
}

/**
 * @brief 通用 http post
 *
 * @param uri
 * @param httpEntity
 * @return true
 * @return false
 */
bool AkServer::doPost(String uri, StaticJsonDocument<256> *httpEntity)
{
    if ((WiFi.status() != WL_CONNECTED))
        return false;

    WiFiClient client;
    HTTPClient http;

    http.begin(client, this->host + uri);
    http.addHeader("Content-Type", "application/json");

    String jsonStr;
    serializeJson(*httpEntity, jsonStr);

    int httpCode = http.POST(jsonStr);
    bool isSuccess = (httpCode == HTTP_CODE_OK);
    if (isSuccess)
    {
        const String &resp = http.getString();
        deserializeJson(*httpEntity, resp);
    }
    http.end();
    return isSuccess;
}

/**
 * @brief 获取注册状态
 *
 * @return true
 * @return false
 */
bool AkServer::getRegister()
{
    return this->isRegister;
}

/**
 * @brief 注册单片机到云平台
 *
 * @return true 注册成功
 * @return false 注册失败
 */
bool AkServer::registerMcu()
{
    StaticJsonDocument<256> entity;
    entity["mac"] = this->mac;
    entity["token"] = this->token;

    return this->isRegister = this->doPost("/api/mcu/register", &entity) && (entity["code"].as<int>() == RESTfulCode::OK);
}

/**
 * @brief 上报数据到云平台
 *
 * @param data
 * @return true
 * @return false
 */
bool AkServer::putData(String data)
{
    if (!this->isRegister)
        return false;

    StaticJsonDocument<256> entity;
    entity["mac"] = this->mac;
    entity["token"] = this->token;
    entity["value"] = data;

    return this->isRegister = this->doPost("/api/mcu/data", &entity) && (entity["code"].as<int>() == RESTfulCode::OK);
}