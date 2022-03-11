package it.filippetti.sispi.spring;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

	private static final Logger logger = LoggerFactory.getLogger(HashMapConverter.class);

	@Override
	public String convertToDatabaseColumn(Map<String, Object> mapToConvert) {
		String customerInfoJson = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		if (mapToConvert != null) {
			try {
				customerInfoJson = objectMapper.writeValueAsString(mapToConvert);
			} catch (final JsonProcessingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return customerInfoJson;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		Map<String, Object> customerInfo = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		if (dbData != null) {
			try {
				customerInfo = objectMapper.readValue(dbData, Map.class);
			} catch (final IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return customerInfo;
	}

}
