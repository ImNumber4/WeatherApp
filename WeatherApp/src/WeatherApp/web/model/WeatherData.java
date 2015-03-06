package WeatherApp.web.model;

public class WeatherData {

	private static final String N_A = "N/A";

	private String city;
	private String state;
	private double tempF;
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public double getTempF() {
		return tempF;
	}

	public WeatherData(String city, String state, double tempF) {
		this.city = city;
		this.state = state;
		this.tempF = tempF;
	}

	public String getWeatherDataString() {
		return (city == null ? N_A : city) + ","
				+ (state == null ? N_A : state) + ","
				+ tempF;
	}
}
