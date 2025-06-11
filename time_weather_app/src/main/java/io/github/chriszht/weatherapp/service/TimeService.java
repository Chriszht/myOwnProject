package io.github.chriszht.weatherapp.service;

import io.github.chriszht.weatherapp.dto.TimeResponse;
import io.github.chriszht.weatherapp.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeService {

    private final ApiService apiService;

    @Value("${time.api.url}")
    private String timeApiUrl;

    public TimeService(ApiService apiService) {
        this.apiService = apiService;
    }

    public TimeResponse getTime(String location) {
        try {
            // 这里简化处理，实际应用中可能需要地理编码服务将地点转换为时区
            String timezone = "Asia/Shanghai"; // 默认值，实际应根据地点获取
            String url = timeApiUrl + "/" + timezone;

            JsonNode response = apiService.fetchData(url);

            TimeResponse timeResponse = new TimeResponse();
            timeResponse.setTimezone(response.path("timezone").asText());
            timeResponse.setDatetime(formatDateTime(response.path("datetime").asText()));
            timeResponse.setDayOfWeek(getDayOfWeek(response.path("day_of_week").asInt()));
            timeResponse.setAbbreviation(response.path("abbreviation").asText());

            return timeResponse;
        } catch (Exception e) {
            throw new ApiException("获取时间数据失败: " + e.getMessage(), e);
        }
    }

    private String formatDateTime(String dateTime) {
        // 示例: 2023-05-15T14:30:00.123456+08:00 -> 2023-05-15 14:30:00
        return dateTime.substring(0, 19).replace("T", " ");
    }

    private String getDayOfWeek(int day) {
        String[] days = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return days[day % 7];
    }
}