package io.github.chriszht.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "WeatherServlet", value = "/weather")
public class WeatherServlet extends HttpServlet {
    // 旧代码（不安全）
    private static final String API_KEY = "4a325f3b116cc937e1abaf127fcb00ec";
    // 新代码（从环境变量读取）
    //private static final String API_KEY = System.getenv("WEATHER_API_KEY");
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置统一的编码和内容类型
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 提前获取Dispatcher避免重复创建
        RequestDispatcher successDispatcher = request.getRequestDispatcher("/weather.jsp");
        RequestDispatcher errorDispatcher = request.getRequestDispatcher("/error.jsp");

        String location = request.getParameter("location");
        if (location == null || location.trim().isEmpty()) {
            request.setAttribute("error", "请输入有效的城市名称");
            errorDispatcher.forward(request, response);
            return;
        }

        try {
            // 配置HTTP请求超时
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(10000)
                    .build();

            // 创建HTTP客户端
            try (CloseableHttpClient client = HttpClients.custom()
                    .setDefaultRequestConfig(config)
                    .build()) {

                String apiUrl = String.format(WEATHER_API_URL,
                        URLEncoder.encode(location, "UTF-8"), API_KEY);
                HttpGet httpGet = new HttpGet(apiUrl);

                // 执行API请求
                try (CloseableHttpResponse apiResponse = client.execute(httpGet)) {
                    // 检查HTTP状态码
                    if (apiResponse.getStatusLine().getStatusCode() != 200) {
                        handleError(request, response, errorDispatcher,
                                "天气服务返回错误状态: " + apiResponse.getStatusLine().getStatusCode());
                        return;
                    }

                    String jsonResponse = EntityUtils.toString(apiResponse.getEntity());
                    JsonNode root = new ObjectMapper().readTree(jsonResponse);

                    if (root.has("cod") && root.get("cod").asInt() == 200) {
                        // 成功处理逻辑
                        request.setAttribute("city", root.get("name").asText());
                        request.setAttribute("country", root.get("sys").get("country").asText());
                        request.setAttribute("temp", root.get("main").get("temp").asDouble());
                        request.setAttribute("description",
                                root.get("weather").get(0).get("description").asText());

                        // 计算当地时间
                        long timezone = root.get("timezone").asLong();
                        ZoneId zoneId = timezone != 0 ?
                                ZoneId.of(String.format("UTC%+d", timezone / 3600)) : ZoneId.of("UTC");
                        String localTime = LocalDateTime.now(zoneId)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        request.setAttribute("localTime", localTime);

                        successDispatcher.forward(request, response);
                    } else {
                        handleError(request, response, errorDispatcher,
                                "城市未找到: " + location);
                    }
                }
            }
        } catch (java.net.SocketTimeoutException e) {
            handleError(request, response, errorDispatcher,
                    "连接天气服务超时，请稍后重试");
        } catch (IOException e) {
            handleError(request, response, errorDispatcher,
                    "网络错误: " + e.getMessage());
        } catch (Exception e) {
            handleError(request, response, errorDispatcher,
                    "系统错误: " + e.getClass().getSimpleName());
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             RequestDispatcher errorDispatcher, String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        errorDispatcher.forward(request, response);
    }
}