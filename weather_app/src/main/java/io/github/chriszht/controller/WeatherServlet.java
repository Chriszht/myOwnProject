package io.github.chriszht.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "WeatherServlet", value = "/weather")
public class WeatherServlet extends HttpServlet {
    private static final String API_KEY = "4a325f3b116cc937e1abaf127fcb00ec"; // 替换为你的OpenWeatherMap API key
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String location = request.getParameter("location");

        try {
            String apiUrl = String.format(WEATHER_API_URL, location, API_KEY);
            String jsonResponse = fetchWeatherData(apiUrl);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            if (root.has("cod") && root.get("cod").asInt() == 200) {
                String city = root.get("name").asText();
                String country = root.get("sys").get("country").asText();
                double temp = root.get("main").get("temp").asDouble();
                String description = root.get("weather").get(0).get("description").asText();
                long timezone = root.get("timezone").asLong();

                // 计算当地时间
                ZoneId zoneId = ZoneId.of("UTC").normalized();
                if (timezone != 0) {
                    zoneId = ZoneId.of(String.format("UTC%+d", timezone / 3600)).normalized();
                }
                LocalDateTime localTime = LocalDateTime.now(zoneId);
                String formattedTime = localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // 设置响应内容类型
                response.setContentType("text/html");

                // 在doPost方法中，修改转发部分的代码：
                // 成功获取天气数据时
                RequestDispatcher dispatcher = request.getRequestDispatcher("/weather.html");
                // 添加参数到请求属性
                request.setAttribute("city", city);
                request.setAttribute("country", country);
                request.setAttribute("temp", temp);
                request.setAttribute("description", description);
                request.setAttribute("localTime", formattedTime);
                dispatcher.forward(request, response);

                // 错误时
                RequestDispatcher errorDispatcher = request.getRequestDispatcher("/error.html");
                request.setAttribute("error", "Location not found: " + location);
                errorDispatcher.forward(request, response);
            } else {
                request.setAttribute("error", "Location not found: " + location);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.html");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error fetching weather data for: " + location);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.html");
            dispatcher.forward(request, response);
        }
    }

    private String fetchWeatherData(String apiUrl) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);

        try (CloseableHttpResponse response = client.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}