/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.service.PersonService;
import com.crossover.techtrial.service.RideService;

/**
 * RideController for Ride related APIs.
 * 
 * @author crossover
 *
 */
@RestController
public class RideController {

	@Autowired
	RideService rideService;

	@Autowired
	PersonService personService;

	@PostMapping(path = "/api/ride")
	public ResponseEntity<Ride> createNewRide(@RequestBody Ride ride) {
		Person rider = personService.findById(ride.getRider().getId());
		Person driver = personService.findById(ride.getDriver().getId());
		ride.setDriver(driver);
		ride.setRider(rider);
		return ResponseEntity.ok(rideService.save(ride));
	}

	@GetMapping(path = "/api/ride/{ride-id}")
	public ResponseEntity<Ride> getRideById(@PathVariable(name = "ride-id", required = true) Long rideId) {
		Ride ride = rideService.findById(rideId);
		if (ride != null)
			return ResponseEntity.ok(ride);
		return ResponseEntity.notFound().build();
	}

	/**
	 * This API returns the top 5 drivers with their email,name, total minutes,
	 * maximum ride duration in minutes. Only rides that starts and ends within the
	 * mentioned durations should be counted. Any rides where either start or
	 * endtime is outside the search, should not be considered.
	 * 
	 * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
	 * 
	 * @return
	 */
	@GetMapping(path = "/api/top-rides")
	public ResponseEntity<List<TopDriverDTO>> getTopDriver(@RequestParam(value = "max", defaultValue = "5") Long count,
			@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
			@RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
		List<TopDriverDTO> topDrivers = new ArrayList<TopDriverDTO>();
		/**
		 * Your Implementation Here. And Fill up topDrivers Arraylist with Top
		 * 
		 */

		List<Ride> list = rideService.findByStartTimeAfterAndEndTimeBefore(startTime.toString(), endTime.toString());

		// maximum ride duration in minutes.
		Map<Person, Optional<Ride>> maxRide = list.stream().collect(
				Collectors.groupingBy(Ride::getDriver, Collectors.maxBy(Comparator.comparing(Ride::getRideTime))));

		// total ride
		Map<Person, Long> totalRide = list.stream()
				.collect(Collectors.groupingBy(Ride::getDriver, Collectors.summingLong(Ride::getRideTime)));
				
		// Average distance
		Map<Person, Double> averageDistanceMap = list.stream()
				.collect(Collectors.groupingBy(Ride::getDriver, Collectors.averagingLong(Ride::getDistance)));


		Person person;
		Iterator<Person> iter = totalRide.keySet().iterator();
		while (iter.hasNext()) {
			person = iter.next();
			TopDriverDTO top = new TopDriverDTO();
			top.setName(person.getName());
			top.setEmail(person.getEmail());
			top.setAverageDistance(averageDistanceMap.get(person));
			top.setMaxRideDurationInSecods(maxRide.get(person).get().getRideTime() * 60);
			top.setTotalRideDurationInSeconds(totalRide.get(person).longValue() * 60);
			topDrivers.add(top);
		}
		
		Collections.sort(topDrivers,  (TopDriverDTO o1, TopDriverDTO o2 ) -> o2.getTotalRideDurationInSeconds().compareTo(o1.getTotalRideDurationInSeconds()));		
		List<TopDriverDTO> topLimit = topDrivers.stream().limit(count).collect(Collectors.toList());
		
		return ResponseEntity.ok(topLimit);

	}

}
