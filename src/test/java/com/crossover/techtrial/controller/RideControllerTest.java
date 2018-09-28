/**
 * 
 */
package com.crossover.techtrial.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;

/**
 * @author emu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RideControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private RideController rideController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  RideRepository rideRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
  }
  
  @Test
  public void testRide() throws Exception {
    HttpEntity<Object> riderObj = getHttpEntity(
        "{\"driver\": 6, \"rider\": 3, \"distance\": 3, \"startTime\":\"2018-08-08T12:12:12\", " 
            + " \"endTime\":\"2018-08-08T12:12:12\" }");
    ResponseEntity<Ride> response = template.postForEntity(
        "/api/ride", riderObj, Ride.class);
    rideRepository.deleteById(response.getBody().getId());
    Assert.assertEquals(200,response.getStatusCode().value());
  }

  @Test
  public void testRideList() throws Exception {
    ResponseEntity<List<TopDriverDTO>> response = template.getForEntity(
        "/api/top-rides?startTime=2017-08-08T12:12:12&endTime=2019-08-08T12:12:12",  null,List.class);
    Assert.assertEquals(200,response.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

}
