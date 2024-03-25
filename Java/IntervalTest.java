import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class IntervalTest {

	@Test
	public void isInInterval() {
		Interval testee = new Interval(LocalDateTime.of(2024, 1, 10, 5, 0), LocalDateTime.of(2024, 1, 10, 5, 59));
		
		assertFalse(testee.isInInterval(LocalDateTime.of(2024, 1, 10, 4, 59)));
		assertTrue(testee.isInInterval(LocalDateTime.of(2024, 1, 10, 5, 0)));
		assertTrue(testee.isInInterval(LocalDateTime.of(2024, 1, 10, 5, 59)));
		assertFalse(testee.isInInterval(LocalDateTime.of(2024, 1, 10, 6, 0)));
	}

}
