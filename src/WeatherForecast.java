import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class WeatherForecast {

    /**
     * Retrieves the 7-day hourly forecast and prints temperatures at 3-hour intervals.
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) throws IOException {
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=39.168804&longitude=-86.536659&hourly=temperature_2m&temperature_unit=fahrenheit&timezone=EST";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IOException("Request failed: " + code);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        conn.disconnect();

        JsonElement root = JsonParser.parseString(sb.toString());
        JsonArray times = root.getAsJsonObject().get("hourly").getAsJsonObject().get("time").getAsJsonArray();
        JsonArray temps = root.getAsJsonObject().get("hourly").getAsJsonObject().get("temperature_2m").getAsJsonArray();


        System.out.println("7-Day Forecast in Fahrenheit:");
        for (int day = 0; day < 7; day++) {
            int dayStart = day * 24;
            String dateTime = timeArray.get(dayStart).getAsString();
            String date = dateTime.split("T")[0];
            System.out.println("Forecast for " + date + ":");
            for (int h = 0; h < 24; h += 3) {
                int index = dayStart + h;
                String time = timeArray.get(index).getAsString().split("T")[1];
                double temp = tempArray.get(index).getAsDouble();
                System.out.println("  " + time + ": " + temp + "\u00B0F");
            }
        }
    }
}
