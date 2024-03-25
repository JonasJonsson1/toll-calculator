import java.time.LocalDateTime;
import java.util.Objects;

public class Interval {
	public LocalDateTime start;
	public LocalDateTime end;
	
	public Interval(LocalDateTime start, LocalDateTime end) {
		this.start = start;
		this.end = end;
	}
	
	protected boolean isInInterval(LocalDateTime date) {
	   return ((date.isEqual(start) || date.isAfter(start)) && (date.isBefore(end) || date.isEqual(end)));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(end, start);
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
		return Objects.equals(end, other.end) && Objects.equals(start, other.start);
	}

}
