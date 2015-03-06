package WeatherApp.web.dao;

import java.io.IOException;

import junit.framework.TestCase;
import WeatherApp.web.exception.DependencyDataMissingException;
import WeatherApp.web.exception.DependencyFailureException;
import WeatherApp.web.model.WeatherData;

public class WeatherDataDaoTests extends TestCase {

	public static WeatherDataDao makeMockDao(String rawResponseString) {
		return new WeatherDataDao() {
			protected String getResponseStringForZip(String urlString)
					throws IOException {
				return rawResponseString;
			}

		};
	}

	public void testNormal() throws Exception {
		WeatherDataDao dao = makeMockDao("{\"current_observation\":{\"display_location\":{\"city\":\"Seattle\", \"state_name\":\"Washington\"}, \"temp_f\":65.0}}");

		WeatherData result = dao.getWeatherDataForZip("98178");
		assertNotNull(result);
		assertEquals(result.getCity(), "Seattle");
		assertEquals(result.getState(), "Washington");
		assertEquals(result.getTempF(), 65.0, 0.01);
	}

	public void testNoOberservation() throws Exception {
		WeatherDataDao dao = makeMockDao("{}");
		boolean hasException = false;
		try {
			dao.getWeatherDataForZip("98178");
		} catch (DependencyFailureException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}
	
	public void testNoLocation() throws Exception {
		WeatherDataDao dao = makeMockDao("{\"current_observation\":{\"temp_f\":65.0}}");

		boolean hasException = false;
		try {
			dao.getWeatherDataForZip("98178");
		} catch (DependencyDataMissingException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}
	
	public void testNoCity() throws Exception {
		WeatherDataDao dao = makeMockDao("{\"current_observation\":{\"display_location\":{\"state_name\":\"Washington\"}, \"temp_f\":65.0}}");

		boolean hasException = false;
		try {
			dao.getWeatherDataForZip("98178");
		} catch (DependencyDataMissingException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}
	
	public void testNoState() throws Exception {
		WeatherDataDao dao = makeMockDao("{\"current_observation\":{\"display_location\":{\"city\":\"Seattle\"}, \"temp_f\":65.0}}");

		boolean hasException = false;
		try {
			dao.getWeatherDataForZip("98178");
		} catch (DependencyDataMissingException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}
	
	public void testNoTemp() throws Exception {
		WeatherDataDao dao = makeMockDao("{\"current_observation\":{\"display_location\":{\"city\":\"Seattle\", \"state_name\":\"Washington\"}}}");

		boolean hasException = false;
		try {
			dao.getWeatherDataForZip("98178");
		} catch (DependencyDataMissingException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}

}
