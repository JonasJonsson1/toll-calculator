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

class VehicleTest {

	@Test
	public void isTollFree() {
		assertFalse(new Vehicle(VehicleType.CAR).isTollFree());
		assertTrue(new Vehicle(VehicleType.DIPLOMAT).isTollFree());
		assertTrue(new Vehicle(VehicleType.EMERGENCY).isTollFree());
		assertTrue(new Vehicle(VehicleType.FOREIGN).isTollFree());
		assertTrue(new Vehicle(VehicleType.MILITARY).isTollFree());
		assertTrue(new Vehicle(VehicleType.MOTORBIKE).isTollFree());
		assertTrue(new Vehicle(VehicleType.TRACTOR).isTollFree());
	}

}
