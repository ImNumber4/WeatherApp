package WeatherApp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import WeatherApp.web.dao.WeatherDataDao;
import WeatherApp.web.exception.DependencyDataMissingException;
import WeatherApp.web.exception.DependencyFailureException;
import WeatherApp.web.exception.InvalidInputException;
import WeatherApp.web.model.WeatherData;

public class WeatherController implements Controller {

	protected final Log logger = LogFactory.getLog(getClass());

	private WeatherDataDao weatherDataDao;
	
	public void setWeatherDataDao(WeatherDataDao weatherDataDao){
		this.weatherDataDao = weatherDataDao;
	}
	
	private static final String DEPENDENCY_FAILURE_MESSAGE = "The server is having some dependency failure to get the weather data. Please try again later.";
	private static final String ZIP_NOT_FOUNT_MESSAGE = "zipcode not found";
	private static final String INVALID_ZIP_CODE_FORMAT_MESSAGE = "invalid zip code format";

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String responseString = "";
		if (request != null && request.getParameter("zip") != null) {
			String zipCode = request.getParameter("zip");
			try {
				WeatherData weatherData = this.weatherDataDao.getWeatherDataForZip(zipCode);
				if(weatherData == null)
					 responseString = DEPENDENCY_FAILURE_MESSAGE;
				else
					responseString = weatherData.getWeatherDataString();
			} catch (InvalidInputException e) {
				responseString = INVALID_ZIP_CODE_FORMAT_MESSAGE;
			} catch (DependencyDataMissingException e) {
				responseString = ZIP_NOT_FOUNT_MESSAGE;
			} catch (DependencyFailureException e) {
				responseString = DEPENDENCY_FAILURE_MESSAGE;
			}
		}

		ModelAndView view = new ModelAndView("weather.jsp");
		view.addObject("currentWeatherData", responseString);
		return view;
	}

}
