
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TollCalculator {

	private Set<Holiday> holidays; 
	
	public TollCalculator() throws IOException, URISyntaxException {
		init();
	}
	
	private void init() throws IOException, URISyntaxException {
		ObjectMapper mapper = new ObjectMapper();
		Path path = Path.of(ClassLoader.getSystemResource("holidays.json").toURI());
		String json = Files.readString(path);
		Holiday[] holidayArray = mapper.readValue(json, Holiday[].class);
		holidays = new HashSet<Holiday>(Arrays.asList(holidayArray));
	}
	
	/**
	 * Calculate the total toll fee for one day
	 *
	 * @param vehicle - the vehicle
	 * @param dates   - date and time of all passes on one day
	 * @return - the total toll fee for that day
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public int getTollFeeForVehicle(Optional<Vehicle> vehicle, LocalDateTime... dates) {
		if (vehicle.isEmpty() || vehicle.get().isTollFree()) {
			return 0;
		}
		LocalDateTime intervalStart = dates[0];
		LocalDateTime intervalEnd = dates[0].plusHours(1);
		int totalFee = 0;

		Map<Interval, Integer> intervals = new HashMap<Interval, Integer>();
		for (int i = 0; i < dates.length; i++) {
		   int nextFee = getTollFee(dates[i]);
		   if ((dates[i].isEqual(intervalStart) || dates[i].isAfter(intervalStart)) && (dates[i].isBefore(intervalEnd) || dates[i].isEqual(intervalEnd))) {
				Interval intervalKey = new Interval(intervalStart, intervalEnd);
				if (intervals.containsKey(intervalKey)) {
					Integer fee = intervals.get(intervalKey);
					if (nextFee > fee) {
						fee = nextFee;
					}
					intervals.put(new Interval(intervalStart, intervalEnd), fee);
				} else {
					intervals.put(new Interval(intervalStart, intervalEnd), nextFee);
				}
			} else {
				intervalStart = dates[i];
				intervalEnd = dates[i].plusHours(1);
				intervals.put(new Interval(intervalStart, intervalEnd), nextFee);
			}
		}
			
		for (Interval interval : intervals.keySet()) { 
			totalFee += intervals.get(interval);
		}
		
		if (totalFee > 60) {
			totalFee = 60;
		}
		return totalFee;
	}

	protected int getTollFee(final LocalDateTime date) {
		if (isTollFreeDate(date)) {
			return 0;
		}
		int hour = date.getHour();
		int minute = date.getMinute();
		LocalTime time = LocalTime.of(hour, minute);
    
		if (isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), time)) return 8;
		else if (isInRange(LocalTime.of(6, 30), LocalTime.of(6, 59), time)) return 13;
		else if (isInRange(LocalTime.of(7, 0), LocalTime.of(7, 59), time)) return 18;
		else if (isInRange(LocalTime.of(8, 0), LocalTime.of(8, 29), time)) return 13;
		else if (isInRange(LocalTime.of(8, 30), LocalTime.of(14, 59), time)) return 8; // Here was bug. All minutes under 30 not considered.
		else if (isInRange(LocalTime.of(15, 0), LocalTime.of(15, 29), time)) return 13; //  
		else if (isInRange(LocalTime.of(15, 30), LocalTime.of(16, 59), time)) return 18; // Should be from 15.30 (15.00 - 15.29 taken care on row above) 
		else if (isInRange(LocalTime.of(17, 0), LocalTime.of(17, 59), time)) return 13;
		else if (isInRange(LocalTime.of(18, 0), LocalTime.of(18, 29), time)) return 8;
		else return 0;
	}

	protected boolean isTollFreeDate(LocalDateTime date) {
		int year = date.getYear();
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();

		DayOfWeek dayOfWeek = date.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
			return true;

		return holidays.contains(new Holiday(year, month, day));
	}

	protected boolean isInRange(LocalTime startTime, LocalTime endTime, LocalTime time) {
		if (time.isBefore(startTime) || time.isAfter(endTime)) {
			return false;
		}
		return true;
	}

	
	private class Interval {
		public LocalDateTime start;
		public LocalDateTime end;
		public int highest;
		
		public Interval(LocalDateTime start, LocalDateTime end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(end, start);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Interval other = (Interval) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(end, other.end) && Objects.equals(start, other.start);
		}

		private TollCalculator getEnclosingInstance() {
			return TollCalculator.this;
		}
	}
	
}

