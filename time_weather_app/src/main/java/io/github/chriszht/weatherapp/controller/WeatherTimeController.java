package io.github.chriszht.weatherapp.controller;

import io.github.chriszht.weatherapp.dto.LocationDto;
import io.github.chriszht.weatherapp.dto.TimeResponse;
import io.github.chriszht.weatherapp.dto.WeatherResponse;
import io.github.chriszht.weatherapp.service.TimeService;
import io.github.chriszht.weatherapp.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WeatherTimeController {

    private final WeatherService weatherService;
    private final TimeService timeService;

    public WeatherTimeController(WeatherService weatherService, TimeService timeService) {
        this.weatherService = weatherService;
        this.timeService = timeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("locationDto", new LocationDto());
        return "index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute LocationDto locationDto, Model model) {
        try {
            WeatherResponse weather = weatherService.getWeather(locationDto.getLocation());
            TimeResponse time = timeService.getTime(locationDto.getLocation());

            model.addAttribute("weather", weather);
            model.addAttribute("time", time);
            model.addAttribute("location", locationDto.getLocation());
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", "无法获取数据，请检查地点名称或稍后再试");
            return "error";
        }
    }
}