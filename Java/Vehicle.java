
public class Vehicle {

	private VehicleType vehicleType;
	
	private Vehicle() {}
	
	public Vehicle(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public VehicleType getType() {
		return vehicleType;
	}

	public boolean isTollFree() {
		return vehicleType.isTollFree();
	}
}
