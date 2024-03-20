
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
	public int getTollFee(Vehicle vehicle, Date... dates) {
		Date intervalStart = dates[0];
		int totalFee = 0;
		for (Date date : dates) {
			int nextFee = getTollFee(date, vehicle);
			int tempFee = getTollFee(intervalStart, vehicle);

			TimeUnit timeUnit = TimeUnit.MINUTES;
			long diffInMillies = date.getTime() - intervalStart.getTime();
			long minutes = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

			if (minutes <= 60) {
				if (totalFee > 0)
					totalFee -= tempFee;
				if (nextFee >= tempFee)
					tempFee = nextFee;
				totalFee += tempFee;
			} else {
				totalFee += nextFee;
				intervalStart = date; // This was a bug. Next intervalStart was not set.
			}
		}
		if (totalFee > 60)
			totalFee = 60;
		return totalFee;
	}

	public int getTollFee(final Date date, Vehicle vehicle) {
		if(isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
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

	protected boolean isInRange(LocalTime startTime, LocalTime endTime, LocalTime time) {
		if (time.isBefore(startTime) || time.isAfter(endTime)) {
			return false;
		}
		return true;
	}

	protected boolean isTollFreeVehicle(Vehicle vehicle) {
		if (vehicle == null)
			return true;
		return vehicle.getType().isTollFree();
	}
  
	protected boolean isTollFreeDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
			return true;

		return holidays.contains(new Holiday(year, month, day));
	}

}

