import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TollCalculatorTest {

	private TollCalculator testee;
	
	@BeforeEach
	void setUp() throws Exception {
		testee = new TollCalculator();
	}

	@Test
	public void getTollFeeMaxOneFeePerHour() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar1 = new GregorianCalendar();
		calendar1.set(2024, 0, 10, 8, 0);
		Calendar calendar2 = new GregorianCalendar();
		calendar2.set(2024, 0, 10, 8, 30);
		
		
		assertEquals(13, testee.getTollFee(car, calendar1.getTime(), calendar2.getTime()));
	}

	@Test
	public void getTollFeeMaxAmountPerDay() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar1 = new GregorianCalendar(); // 8
		calendar1.set(2024, 0, 10, 6, 0);
		Calendar calendar2 = new GregorianCalendar(); // 18
		calendar2.set(2024, 0, 10, 7, 0);
		Calendar calendar3 = new GregorianCalendar(); // 13
		calendar3.set(2024, 0, 10, 8, 0);
		Calendar calendar4 = new GregorianCalendar(); // 8
		calendar4.set(2024, 0, 10, 9, 0);
		Calendar calendar5 = new GregorianCalendar(); // 18 
		calendar5.set(2024, 0, 10, 16, 0);
		
		assertEquals(60, testee.getTollFee(car, calendar1.getTime(), calendar2.getTime(), calendar3.getTime(), calendar4.getTime(), calendar5.getTime()));
	}

	@Test
	public void getTollFeeTaxa1() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 5, 59);
		assertEquals(0, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 6, 0);
		assertEquals(8, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 6, 29);
		assertEquals(8, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa2() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 6, 30);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 6, 59);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa3() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 7, 0);
		assertEquals(18, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 7, 59);
		assertEquals(18, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa4() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 8, 0);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 8, 29);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa5() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 8, 30);
		assertEquals(8, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 14, 59);
		assertEquals(8, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa6() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 15, 0);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 15, 29);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa7() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 15, 30);
		assertEquals(18, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 16, 59);
		assertEquals(18, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa8() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 17, 0);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 17, 59);
		assertEquals(13, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void getTollFeeTaxa9() {
		Vehicle car = new Vehicle(VehicleType.CAR);
		Calendar calendar = new GregorianCalendar();

		calendar.set(2024, 0, 10, 18, 0);
		assertEquals(8, testee.getTollFee(calendar.getTime(), car));

		calendar.set(2024, 0, 10, 18, 30);
		assertEquals(0, testee.getTollFee(calendar.getTime(), car));
	}

	@Test
	public void isInRange() {
		assertFalse(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(5, 59)));
		assertTrue(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(6, 0)));
		assertFalse(testee.isInRange(LocalTime.of(6, 0), LocalTime.of(6, 29), LocalTime.of(6, 30)));
	}

	@Test
	public void isTollFreeNoVehicle() {
		assertTrue(testee.isTollFreeVehicle(null));
	}

	@Test
	public void isTollFreeVehicle() {
		assertFalse(testee.isTollFreeVehicle(new Vehicle(VehicleType.CAR)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.DIPLOMAT)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.EMERGENCY)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.FOREIGN)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.MILITARY)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.MOTORBIKE)));
		assertTrue(testee.isTollFreeVehicle(new Vehicle(VehicleType.TRACTOR)));
	}

	@Test
	public void isTollFreeDateSaturday() {
		Calendar calendar = new GregorianCalendar();
		calendar.setWeekDate(2024, 10,Calendar.SATURDAY);
		
		assertTrue(testee.isTollFreeDate(calendar.getTime()));
	}

	@Test
	public void isTollFreeDateSunday() {
		Calendar calendar = new GregorianCalendar();
		calendar.setWeekDate(2024, 10, Calendar.SUNDAY);
		
		assertTrue(testee.isTollFreeDate(calendar.getTime()));
	}
	
	@Test
	public void isTollFreeDate() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(2024, 0, 1);
		
		assertTrue(testee.isTollFreeDate(calendar.getTime()));
	}

	@Test
	public void isNotTollFreeDate() throws IOException, URISyntaxException {
		Calendar calendar = new GregorianCalendar();
		calendar.set(2024, 0, 2);
		
		assertFalse(testee.isTollFreeDate(calendar.getTime()));
	}

}
