package WeatherApp.web;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import WeatherApp.web.dao.WeatherDataDaoTests;

public class WeatherControllerTests extends TestCase {

    public void testHandleRequestView() throws Exception{		
    	WeatherController controller = new WeatherController();
    	controller.setWeatherDataDao(WeatherDataDaoTests.makeMockDao(""));
        ModelAndView modelAndView = controller.handleRequest(null, null);		
        assertEquals("weather.jsp", modelAndView.getViewName());
    }
}