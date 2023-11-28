package ru.mrsinkaaa.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private static final WeatherAPI INSTANCE = new WeatherAPI();

    private WeatherAPI() {
    }

    public String sendGetRequest(String apiURL) throws IOException {

        URL url = new URL(apiURL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder response = new StringBuilder();

        while((inputLine = reader.readLine())!= null) {
            response.append(inputLine);
        }

        reader.close();

        return response.toString();
    }

    public static WeatherAPI getInstance() {
        return INSTANCE;
    }
}
