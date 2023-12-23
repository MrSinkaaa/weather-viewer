package ru.mrsinkaaa.dto;

import lombok.Getter;

@Getter
public enum WeatherCode {

    CLEAR("clear.svg"),
    RAIN("rain.svg"),
    CLOUDS("clouds.svg"),
    THUNDERSTORM("thunderstorm.svg"),
    SNOW("snow.svg"),
    MIST("mist.svg"),
    WIND("wind.svg"),
    FOG("fog.svg"),
    HAZE("haze.svg"),
    DUST("dust.svg"),
    TORNADO("tornado.svg"),
    SQUALL("mist.svg"),
    ASH("mist.svg"),
    DRIZZLE("drizzle.svg"),
    SMOKE("smoke.svg");


    private final String icon;

    WeatherCode(String icon) {
        this.icon = icon;
    }
}
