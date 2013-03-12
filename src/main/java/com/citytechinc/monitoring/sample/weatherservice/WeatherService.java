package com.citytechinc.monitoring.sample.weatherservice;

/**
 *
 * For example, for use in conjunction with the "WeatherServiceMonitor".
 *
 * @author CITYTECH, INC. 2013
 *
 */
public interface WeatherService {

    /**
     *
     * @param zipCode
     * @return
     */
    public Forecast getForecastForZipCode(String zipCode);
}
