package tma.datraining.converter;

import java.util.Date;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateToTimestampConverter implements Converter<java.util.Date, java.sql.Timestamp> {

	@Override
	public Timestamp convert(Date source) {
		if (StringUtils.isBlank(source.toString()))
			return null;
		return new Timestamp(source.getTime());
	}

}
