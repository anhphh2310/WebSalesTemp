package tma.datraining.unitTest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import tma.datraining.controller.TimeController;
import tma.datraining.converter.DateTimeToTimestampConverter;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;
import tma.datraining.service.cassandra.CassTimeService;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebMvcTest(value = TimeController.class, secure = false)
public class TimeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TimeService timeService;

	@MockBean
	private CassTimeService cassTimeService;

	@MockBean
	private SalesService salesService;
	@MockBean
	private DateTimeToTimestampConverter converter;

	MediaType[] m = new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML };
	UUID id1 = UUID.fromString("accdb0f3-e347-4722-b6bd-51dd81f2aad2");
	UUID id2 = UUID.fromString("af2343f7-7fb2-48cb-98a2-f633b909d280");

	@Ignore
	@Test
	public void givetTimes_whenGetAllTimes_thenReturn() throws Exception {
		List<Time> list = new ArrayList<>();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id1);
		Time time2 = new Time(9, 4, 2018, time, time);
		time2.setTimeId(id2);
		list.add(time1);
		list.add(time2);
		given(this.timeService.list()).willReturn(list);
		this.mockMvc.perform(get("/time/list").accept(m)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2))).andExpect(jsonPath("$[1].month", is(9)));
	}
	
	@Ignore
	@Test
	public void giveTimes_whenGetTimeById_thenReturn() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id1);
		given(this.timeService.get(any(UUID.class))).willReturn(time1);
		this.mockMvc.perform(get("/time/" + id1).accept(m))
		.andExpect(jsonPath("$.quarter",is(3)));
	}
	
	@Ignore
	@Test
	public void giveTimes_whenGetTimesByMonth_thenReturn() throws Exception{
		List<Time> list = new ArrayList<>();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id1);
		list.add(time1);
		given(this.timeService.findByYear(anyInt())).willReturn(list);
		this.mockMvc.perform(get("/time/year/" + time1.getYear()).accept(m))
		.andExpect(jsonPath("$.length()",is(1))).andExpect(jsonPath("$[0].year", is(2018)));
		
	}
	
	@Ignore
	@Test
	public void giveTime_whenAddTime_thenReturn() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id1);
		given(this.timeService.save(any(Time.class))).willReturn(time1);
		this.mockMvc.perform(post("/time/add").contentType(MediaType.APPLICATION_JSON).content(createTimeJsonForAdd(time1)))
		.andExpect(status().isOk()).andDo(print());
	}
	
	@Ignore
	@Test
	public void giveTime_whenUpdateTime_thenReturn() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id2);
		List<Sales> sales = new ArrayList<>();
		given(this.timeService.get(any(UUID.class))).willReturn(time1);
		given(this.salesService.findByTime(any(Time.class))).willReturn(sales);
		given(this.timeService.update(eq(id2), any(Time.class))).willReturn(time1);
		this.mockMvc.perform(put("/time/update").contentType(MediaType.APPLICATION_JSON).content(createTimeJsonForUpdate(time1)))
		.andExpect(status().isOk()).andDo(print());	
	}
	
	@Ignore
	@Test
	public void giveId_whenDeleteTime_thenReturn() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id2);
		List<Sales> list = new ArrayList<>();
		given(this.timeService.get(any(UUID.class))).willReturn(time1);
		given(this.salesService.findByTime(any(Time.class))).willReturn(list);
		given(this.timeService.delete(any(UUID.class))).willReturn(id2);
		this.mockMvc.perform(delete("/time/delete/{id2}", id2).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andDo(print());
	}

	private String createTimeJsonForAdd(Time time) {
		return "{ \"month\": \"" + time.getMonth() + "\", " + "\"quarter\":\"" + time.getQuarter() + "\", "
				+ "\"year\":\"" + time.getYear() + "\"}";
	}

	private String createTimeJsonForUpdate(Time time) {
		return "{ \"timeId\": \"" + time.getTimeId() + "\", " + " \"month\": \"" + time.getMonth() + "\", "
				+ "\"quarter\":\"" + time.getQuarter() + "\", " + "\"year\":\"" + time.getYear() + "\"}";
	}
	
}
