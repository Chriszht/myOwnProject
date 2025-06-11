package io.github.chriszht.weatherapp.service;

import io.github.chriszht.weatherapp.dto.WeatherResponse;
import io.github.chriszht.weatherapp.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final ApiService apiService;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    public WeatherService(ApiService apiService) {
        this.apiService = apiService;
    }

    public WeatherResponse getWeather(String location) {
        try {
            String url = String.format("%s?q=%s&appid=%s&units=metric&lang=zh_cn",
                    weatherApiUrl, location, weatherApiKey);

            JsonNode response = apiService.fetchData(url);

            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setLocation(response.path("name").asText());
            weatherResponse.setTemperature(response.path("main").path("temp").asDouble());
            weatherResponse.setFeelsLike(response.path("main").path("feels_like").asDouble());
            weatherResponse.setHumidity(response.path("main").path("humidity").asInt());

            JsonNode weatherNode = response.path("weather").get(0);
            weatherResponse.setDescription(weatherNode.path("description").asText());
            weatherResponse.setIcon(weatherNode.path("icon").asText());

            weatherResponse.setWindSpeed(response.path("wind").path("speed").asDouble());

            return weatherResponse;
        } catch (Exception e) {
            throw new ApiException("获取天气数据失败: " + e.getMessage(), e);
        }
    }
}