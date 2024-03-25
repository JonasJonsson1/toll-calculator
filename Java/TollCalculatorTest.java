import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TollCalculatorTest {

	private TollCalculator testee;
	
	@BeforeEach
	void setUp() throws Exception {
		testee = new TollCalculator();
	}

	@Test
	public void getTollFeeForVehicleNoVehicle() {
		LocalDateTime date1 = LocalDateTime.of(2024, 1, 10, 8, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 1, 10, 8, 30);
		
		assertEquals(0, testee.getTollFeeForVehicle(Optional.ofNullable(null), date1, date2));
	}

	@Test
	public void getTollFeeForVehicleTollFree() {
		Vehicle car = new Vehicle(VehicleType.DIPLOMAT);
		LocalDateTime date1 = LocalDateTime.of(2024, 1, 10, 8, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 1, 10, 8, 30);
		
		assertEquals(0, testee.getTollFeeForVehicle(Optional.ofNullable(car), date1, date2));
	}

	@Test
	public void getTollFeeForVehicleMaxOneFeePerHour() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		LocalDateTime date1 = LocalDateTime.of(2024, 1, 10, 8, 0); // 8
		LocalDateTime date2 = LocalDateTime.of(2024, 1, 10, 8, 30); // 13

		assertEquals(13, testee.getTollFeeForVehicle(Optional.of(car), date1, date2));
	}

	@Test
	public void getTollFeeForVehicleMaxOnePerHourAndOneMoreHour() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		LocalDateTime date = LocalDateTime.of(2024, 1, 10, 6, 1); // 8
		LocalDateTime date2 = LocalDateTime.of(2024, 1, 10, 7, 1); // 18
		LocalDateTime date3 = LocalDateTime.of(2024, 1, 10, 8, 0); // 13
		
		assertEquals(31, testee.getTollFeeForVehicle(Optional.of(car), date, date2, date3));
	}

	@Test
	public void getTollFeeForVehicleMaxAmountPerDay() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		LocalDateTime date1 = LocalDateTime.of(2024, 1, 10, 6, 1); // 8
		LocalDateTime date2 = LocalDateTime.of(2024, 1, 10, 7, 3); // 18
		LocalDateTime date3 = LocalDateTime.of(2024, 1, 10, 8, 5); // 13		
		LocalDateTime date4 = LocalDateTime.of(2024, 1, 10, 9, 7); // 8
		LocalDateTime date5 = LocalDateTime.of(2024, 1, 10, 16, 9);	// 18	
		
		assertEquals(60, testee.getTollFeeForVehicle(Optional.of(car), date1, date2, date3, date4, date5));
	}

	@Test
	public void getTollFeeTaxa1() {
		assertEquals(0, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 5, 59)));
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 6, 0)));
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 6, 29)));
	}

	@Test
	public void getTollFeeTaxa2() {
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 6, 30)));
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 6, 59)));
	}

	@Test
	public void getTollFeeTaxa3() {
		assertEquals(18, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 7, 0)));
		assertEquals(18, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 7, 59)));
	}

	@Test
	public void getTollFeeTaxa4() {
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 8, 0)));
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 8, 29)));
	}

	@Test
	public void getTollFeeTaxa5() {
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 8, 30)));
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 14, 59)));
	}

	@Test
	public void getTollFeeTaxa6() {
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 15, 0)));
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 15, 29)));
	}

	@Test
	public void getTollFeeTaxa7() {
		assertEquals(18, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 15, 30)));
		assertEquals(18, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 16, 59)));
	}

	@Test
	public void getTollFeeTaxa8() {
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 17, 0)));
		assertEquals(13, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 17, 59)));
	}

	@Test
	public void getTollFeeTaxa9() {
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 18, 0)));
		assertEquals(8, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 18, 29)));
		assertEquals(0, testee.getTollFee(LocalDateTime.of(2024, 1, 10, 18, 30)));
	}

	@Test
	public void isTollFreeDateSaturday() {
		assertTrue(testee.isTollFreeDate(LocalDateTime.of(2024, 1, 6, 18, 0)));
	}

	@Test
	public void isTollFreeDateSunday() {
		assertTrue(testee.isTollFreeDate(LocalDateTime.of(2024, 1, 7, 18, 0)));
	}
	
	@Test
	public void isTollFreeHoliday() {
		assertTrue(testee.isTollFreeDate(LocalDateTime.of(2024, 1, 1, 18, 0)));
	}

	@Test
	public void isNotTollFree() throws IOException, URISyntaxException {
		assertFalse(testee.isTollFreeDate(LocalDateTime.of(2024, 1, 2, 12, 0)));
	}

	@Test
	public void isInRange() {
		assertFalse(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(5, 59)));
		assertTrue(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(6, 0)));
		assertFalse(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(6, 30)));
	}

}
