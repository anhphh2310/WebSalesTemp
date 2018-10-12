package tma.datraining.converter;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateTimeToTimestampConverter implements Converter<DateTime, Timestamp> {

	@Override
	public Timestamp convert(DateTime source) {
		// TODO Auto-generated method stub
		return new Timestamp(source.getMillis());
	}

}
