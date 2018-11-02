package tma.datraining.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.dto.TimeDTO;
import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.QTime;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.model.cassandra.CassTime;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;
import tma.datraining.service.cassandra.CassTimeService;
import tma.datraining.util.LogUtil;

@RestController
@RequestMapping("/time")
public class TimeController {

	@Autowired
	private TimeService timeSer;

	@Autowired
	private SalesService salesSer;

	@Autowired
	private CassTimeService cassSer;
	
	@Autowired
	private DateTimeToTimestampConverter converter;
	
	private static final Logger LOG = LogManager.getLogger(TimeController.class);
	
	@GetMapping(value="/convert",produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getConvertTime(){
		LogUtil.info(LOG, "Request convert data from Cassandra");
		cassSer.list().forEach(e -> timeSer.save(convertCassToJPA(e)));
		List<TimeDTO> list = new ArrayList<>();
		timeSer.list().forEach(e -> list.add(convertDTO(e)));
		return list;
	}
	
	@GetMapping(value = { "/list" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimes() {
		LogUtil.info(LOG, "Request list Time");
		List<TimeDTO> list = new ArrayList<>();
		timeSer.list().forEach(e->list.add(convertDTO(e)));
		return list;
	}

	@GetMapping(value = { "/{timeId}" }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO getTime(@PathVariable("timeId") String timeId) {
		LogUtil.info(LOG, "Request time with id: " + timeId);
		TimeDTO time = null;
		try {
			time = convertDTO(timeSer.get(UUID.fromString(timeId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		return time;
	}
	
	@GetMapping(value = { "/year/{year}" }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimesByYear(@PathVariable("year") String year) {
		LogUtil.info(LOG, "Request time by year: " + year);
		int numYear = 0;
		try {
			numYear = Integer.parseInt(year);
		}
		catch(NumberFormatException | NullPointerException e) {
			throw new NotFoundDataException("");
		}
		List<Time> list = timeSer.findByYear(numYear);
		List<TimeDTO> listResult = new ArrayList<>();
		list.forEach(e -> listResult.add(convertDTO(e)));
		return listResult;
	}

	@PostMapping(value = { "/add" },produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO saveTime(@RequestBody TimeDTO tim,Principal principal) {
		LogUtil.info(LOG, "Request add a new time");
		if(tim.getMonth()<=0 || tim.getMonth()>12 ||tim.getQuarter()<=0 || tim.getQuarter()>4 || tim.getYear()<=0) {
			throw new BadRequestException("");
		}
		tim.setTimeId(UUID.randomUUID());
		Timestamp temp = new Timestamp(System.currentTimeMillis());
		tim.setCreateAt(temp);
		tim.setModifiedAt(temp);
		Time time = converTime(tim);
		timeSer.save(time);
		tim = convertDTO(time);
		return tim;
	}

	@PutMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO updateTime(@RequestBody TimeDTO tim, Principal principal) {
		LogUtil.info(LOG, "Request update time with id: " + tim.getTimeId());
		Time time = timeSer.get(tim.getTimeId());
		if(time==null) {
			throw new NotFoundDataException("Time id " + tim.getTimeId());
		}
		if(tim.getMonth()<=0 || tim.getMonth()>12 ||tim.getQuarter()<=0 || tim.getQuarter()>4 || tim.getYear()<=0) {
			throw new BadRequestException("");
		}
		Timestamp temp = new Timestamp(System.currentTimeMillis());
		time.setMonth(tim.getMonth());
		time.setQuarter(tim.getQuarter());
		time.setYear(tim.getYear());
		time.setModifiedAt(temp);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByTime(time));
		time.setSales(sales);
		timeSer.update(time.getTimeId(), time);
		tim.setCreateAt(time.getCreateAt());
		tim.setModifiedAt(tim.getModifiedAt());
		return tim;
	}

	@RequestMapping(value = { "/delete/{timeId}" }, method=RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteTime(@PathVariable("timeId") String timeId, Principal principal) {
		LogUtil.info(LOG, "Request delete time with id :" + timeId);
		TimeDTO time = null;
		try {
			time = convertDTO(timeSer.get(UUID.fromString(timeId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		salesSer.findByTime(timeSer.get(UUID.fromString(timeId))).forEach(e -> salesSer.delete(e.getSalesId()));
		timeSer.delete(time.getTimeId());
		return "Delete success TIME{ " + timeId + "}";
	}

	//QueryDSL
	//
	@GetMapping(value = { "/list/queryDsl" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimesByQueryDsl(@QuerydslPredicate(root=Time.class) Predicate predicate) {
		LogUtil.info(LOG, "Request list Time by queryDsl.");
		List<TimeDTO> list = new ArrayList<>();
		timeSer.getListTimeByQueryDsl(predicate).forEach(e->list.add(convertDTO(e)));
		return list;
	}
	
	@GetMapping(value = { "/list/queryDsl/month" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimesWithMonthByQueryDsl() {
		LogUtil.info(LOG, "Request list Time by queryDsl.");
		List<TimeDTO> list = new ArrayList<>();
		timeSer.getTimesByQueryDslSortingMonth().forEach(e->list.add(convertDTO(e)));
		return list;
	}
	
	@GetMapping(value = { "/list/queryDsl/year" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimesWithYearByQueryDsl() {
		LogUtil.info(LOG, "Request list Time by queryDsl.");
		List<TimeDTO> list = new ArrayList<>();
		timeSer.getTimesByQueryDslSortingByYear().forEach(e->list.add(convertDTO(e)));
		return list;
	}
	
	@GetMapping(value = { "/list/queryDsl/quarter" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimesWithQuarterByQueryDsl() {
		LogUtil.info(LOG, "Request list Time by queryDsl.");
		List<TimeDTO> list = new ArrayList<>();
		timeSer.getTimesByQueryDslSortingByQuarter().forEach(e->list.add(convertDTO(e)));
		return list;
	}
	
	@PutMapping(value = { "/queryDsl/update" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO updateTimeByQueryDsl(@RequestBody TimeDTO tim, Principal principal) {
		LogUtil.info(LOG, "Request update time with id: " + tim.getTimeId());
		QTime qTime = QTime.time;
		Predicate predicate = qTime.timeId.eq(tim.getTimeId());
		Time time = timeSer.getTimeByQueryDsl(predicate);
		if(time==null) {
			throw new NotFoundDataException("Time id " + tim.getTimeId());
		}
		if(tim.getMonth()<=0 || tim.getMonth()>12 ||tim.getQuarter()<=0 || tim.getQuarter()>4 || tim.getYear()<=0) {
			throw new BadRequestException("");
		}
		Timestamp temp = new Timestamp(System.currentTimeMillis());
		time.setMonth(tim.getMonth());
		time.setQuarter(tim.getQuarter());
		time.setYear(tim.getYear());
		time.setModifiedAt(temp);
		timeSer.updateByQueryDsl(tim.getTimeId(), time);
		tim.setCreateAt(time.getCreateAt());
		tim.setModifiedAt(tim.getModifiedAt());
		return tim;
	}

	@RequestMapping(value = { "/queryDsl/delete/{timeId}" }, method=RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String deleteTimeByQueryDsl(@PathVariable("timeId") String timeId, Principal principal) {
		LogUtil.info(LOG, "Request delete time with id :" + timeId);
		salesSer.findByTime(timeSer.get(UUID.fromString(timeId))).forEach(e -> salesSer.delete(e.getSalesId()));
		timeSer.delete(UUID.fromString(timeId));
		return "Delete success TIME{ " + timeId + "}.";
	}
	
	//Convert
	//Time to DTO
	public TimeDTO convertDTO(Time e) {
		TimeDTO time = new TimeDTO();
		if (e == null) {
			throw new NotFoundDataException("");
		}
		time.setTimeId(e.getTimeId());
		time.setMonth(e.getMonth());
		time.setQuarter(e.getQuarter());
		time.setYear(e.getYear());
		time.setCreateAt((e.getCreateAt()));
		time.setModifiedAt(e.getModifiedAt());
		return time;
	}

	//DTO to time
	public Time converTime(TimeDTO time) {
		Time tim = null;
		tim = new Time(time.getMonth(), time.getQuarter(), time.getYear(), time.getCreateAt(), time.getModifiedAt());
		tim.setTimeId(time.getTimeId());
		return tim;
	}
	
	//Cass to DTO
	private Time convertCassToJPA(CassTime e) {
		if(e== null) {
			throw new NotFoundDataException("");
		}
		Time time = new Time();
		time.setTimeId(e.getTimeId());
		time.setMonth(e.getMonth());
		time.setQuarter(e.getQuarter());
		time.setYear(e.getYear());
		time.setCreateAt(converter.convert(e.getCreateAt()));
		time.setModifiedAt(converter.convert(e.getModifiedAt()));
		return time;
	}
	
	
}
