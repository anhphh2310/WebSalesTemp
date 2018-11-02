package tma.datraining.integrationTest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tma.datraining.controller.TimeController;
import tma.datraining.model.Time;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TimeControllerIntegrationTest {

	@Autowired
	private MockMvc mock;
	
	@Autowired
	protected WebApplicationContext weapp;
	
	@Autowired
	private TimeController timeController;
	
	MediaType[] m = new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML };
	private UUID id;
	
	@Before
	public void intit() throws Exception{
	//	this.mock = standaloneSetup(this.timeController).build();
		this.mock = MockMvcBuilders.webAppContextSetup(weapp).build();
		Time time1 = new Time(9,3,2018,null,null);
		mock.perform(post("/time/add").contentType(MediaType.APPLICATION_JSON).content(createTimeJsonForAdd(time1)));
		timeController.getTimes().forEach(e -> id = e.getTimeId());
	}

	@Ignore
	@Test
	public void testGetAllTimes() throws Exception{
		this.mock.perform(get("/time/list").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.length()",is(1))).andDo(print());
	}
	
	@Ignore
	@Test
	public void testGetTimeById() throws Exception{
		this.mock.perform(get("/time/{id}",id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andDo(print());
	}
	
	@Ignore
	@Test
	public void testGetTimeByYear() throws Exception{
		this.mock.perform(get("/time/year/2018").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andDo(print());
	}
	
	@Ignore
	@Test
	public void testAddTime() throws Exception{
		Time time = new Time(1,1,2018,null,null);
		mock.perform(post("/time/add").contentType(MediaType.APPLICATION_JSON).content(createTimeJsonForAdd(time)))
		.andExpect(status().isOk()).andDo(print());
		
	}
	
	@Ignore
	@Test
	public void testUpdateTest() throws Exception{
		Time time = new Time(1,1,2018,null,null);
		time.setTimeId(id);
		mock.perform(put("/time/update").contentType(MediaType.APPLICATION_JSON).content(createTimeJsonForUpdate(time)))
		.andExpect(status().isOk()).andDo(print());
		
	}
	
	@Ignore
	@Test
	public void testDeleteTimeTest() throws Exception{
		mock.perform(delete("/time/delete/{id}",id)).andExpect(status().isOk()).andDo(print());
	}
	
	private String createTimeJsonForAdd(Time time) {
		return "{ \"month\": \"" + time.getMonth() + "\", " + "\"quarter\":\"" + time.getQuarter() + "\", " + "\"year\":\"" + time.getYear() + "\"}";
	}
	
	private String createTimeJsonForUpdate(Time time) {
		return "{ \"timeId\": \"" + time.getTimeId() + "\", " + " \"month\": \"" + time.getMonth() + "\", " + "\"quarter\":\"" + time.getQuarter() + "\", " + "\"year\":\"" + time.getYear() + "\"}";
	}
	
}
