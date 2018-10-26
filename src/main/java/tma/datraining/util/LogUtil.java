package tma.datraining.util;

import org.apache.logging.log4j.Logger;

public class LogUtil {
	public static void debug(Logger log, String msg) {
		log.debug(msg);
	}
	
	public static void info(Logger log, String msg) {
		log.info(msg);
	}
	
	public static void error(Logger log, String msg) {
		log.error(msg);
	}
	
	public static void warn(Logger log, String msg) {
		log.warn(msg);
	}
}
