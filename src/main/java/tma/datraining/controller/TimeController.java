package tma.datraining.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.dto.TimeDTO;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;

@RestController
public class TimeController {

	@Autowired
	private TimeService timeSer;

	@Autowired
	private SalesService salesSer;

	@RequestMapping(value = { "/times" }, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public List<TimeDTO> getTimes() {
		List<TimeDTO> list = convertDTOList(timeSer.list());
		return list;
	}

	@RequestMapping(value = { "/time/{timId}" }, method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO getTime(@PathVariable("timeId") String timeId) {
		TimeDTO time = null;
		try {
			time = convertDTO(timeSer.get(UUID.fromString(timeId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		
		return time;
	}

	@RequestMapping(value = { "/time" }, method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO saveTime(@RequestBody TimeDTO tim) {
		Time time = converTime(tim);
		timeSer.save(time);
		return tim;
	}

	@RequestMapping(value = { "/time" }, method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public TimeDTO updateTime(@RequestBody TimeDTO tim) {
		Time time = converTime(tim);
		Set<Sales> sales = new HashSet<>();
		sales.addAll(salesSer.findByTime(time));
		time.setSales(sales);
		timeSer.update(time.getTimeId(), time);
		TimeDTO timeDTO = convertDTO(timeSer.get(time.getTimeId()));
		return timeDTO;
	}

	@RequestMapping(value = { "/time/{timeId}" }, method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public void deleteTime(@PathVariable("timeId") String timeId) {
		TimeDTO time = null;
		try {
			time = convertDTO(timeSer.get(UUID.fromString(timeId)));
		} catch (IllegalArgumentException e) {
			throw new NotFoundDataException("");
		}
		timeSer.delete(time.getTimeId());
		System.out.println("Delete time : " + timeId);
	}

	public TimeDTO convertDTO(Time time) {
		TimeDTO temp = null;
		if(time == null) {
			throw new NotFoundDataException("");
		}
		temp = new TimeDTO(time.getTimeId(), time.getMonth(), time.getQuarter(), time.getYear(), time.getCreateAt(),
				time.getModifiedAt());
		return temp;
	}

	public List<TimeDTO> convertDTOList(List<Time> list) {
		List<TimeDTO> list2 = new ArrayList<>();
		list.forEach(e -> list2.add(convertDTO(e)));
		return list2;
	}

	public Time converTime(TimeDTO time) {
		Time tim = null;
		tim = new Time(time.getMonth(), time.getQuarter(), time.getYear(), time.getCreateAt(), time.getModifiedAt());
		tim.setTimeId(time.getTimeId());
		return tim;
	}
}
