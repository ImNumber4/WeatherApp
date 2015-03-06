package WeatherApp.web.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import WeatherApp.web.exception.DependencyDataMissingException;
import WeatherApp.web.exception.DependencyFailureException;
import WeatherApp.web.exception.InvalidInputException;
import WeatherApp.web.model.WeatherData;

public class WeatherDataDao {

	protected String getResponseStringForZip(String urlString)
			throws IOException {
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	protected boolean isZipCodeValid(String zipCode) {
		if(zipCode == null) return false;
		String validExpression = "\\d{5,5}";

		return zipCode.matches(validExpression);
	}

	public WeatherData getWeatherDataForZip(String zipCode) throws InvalidInputException, DependencyDataMissingException, DependencyFailureException {

		boolean isValidZip = isZipCodeValid(zipCode);
		if (!isValidZip)
			throw new InvalidInputException();

		String url = "http://api.wunderground.com/api/ed044d75b91fb500/conditions/q/"
				+ zipCode + ".json";
		String rawStringData;
		try {
			rawStringData = getResponseStringForZip(url);
		} catch (IOException e) {
			throw new DependencyFailureException();
		}
		String city;
		String state;
		Double tempF;
		JSONObject responseObj = (JSONObject) JSONValue.parse(rawStringData);
		if (responseObj != null) {
			JSONObject observation = (JSONObject) responseObj
					.get("current_observation");
			if (observation != null) {
				// we could return different error message if
				// display_location/display_location.city/display_location.state_name/temp_f
				// are missing
				JSONObject locationObj = (JSONObject) observation
						.get("display_location");
				if (locationObj != null) {
					city = (String) locationObj.get("city");
					if (city == null)
						throw new DependencyDataMissingException();
					state = (String) locationObj.get("state_name");
					if(state == null)
						throw new DependencyDataMissingException();
				} else {
					throw new DependencyDataMissingException();
				}
				tempF = (Double) observation.get("temp_f");
				if (tempF == null)
					throw new DependencyDataMissingException();
				return new WeatherData(city, state, tempF);
			} else {
				JSONObject error = (JSONObject) responseObj.get("error");
				if (error != null)
					throw new InvalidInputException();
				else
					throw new DependencyFailureException();
			}
		} else {
			throw new DependencyFailureException();
		}
	}

}
