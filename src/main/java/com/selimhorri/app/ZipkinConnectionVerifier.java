package com.selimhorri.app;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ZipkinConnectionVerifier implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ZipkinConnectionVerifier.class);

	@Value("${spring.zipkin.base-url:http://localhost:9411/}")
	private String zipkinBaseUrl;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		verifyZipkinConnection();
	}

	private void verifyZipkinConnection() {
		try {
			// Limpiar la URL (remover trailing slash si existe)
			String cleanUrl = zipkinBaseUrl.endsWith("/") 
				? zipkinBaseUrl.substring(0, zipkinBaseUrl.length() - 1) 
				: zipkinBaseUrl;
			
			URL url = new URL(cleanUrl + "/api/v2/services");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int responseCode = connection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				logger.info("✅ Zipkin connection successful! Connected to: {}", zipkinBaseUrl);
				logger.info("✅ Zipkin is ready to receive traces from CLOUD-CONFIG service");
			} else {
				logger.warn("⚠️ Zipkin connection returned status code: {} for URL: {}", responseCode, zipkinBaseUrl);
			}
			
			connection.disconnect();
		} catch (Exception e) {
			logger.error("❌ Failed to connect to Zipkin at: {}", zipkinBaseUrl, e);
			logger.warn("⚠️ Tracing to Zipkin may not work. Check your SPRING_ZIPKIN_BASE_URL configuration.");
		}
	}
}

