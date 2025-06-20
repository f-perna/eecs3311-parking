package models.parkingSpace;

import java.util.UUID;

import models.parkingLot.ParkingLot;

public class ParkingSpace {

	public static enum ParkingSpaceStatus {
		AVAILABLE, BOOKED
	}

	private UUID id;
	private ParkingLot parkingLot;
	private ParkingSpaceStatus status = ParkingSpaceStatus.AVAILABLE;
	private String name;
	private boolean enabled = true;

	public ParkingSpace(ParkingLot parkingLot, String name) {
		this.id = UUID.randomUUID();
		this.parkingLot = parkingLot;
		this.name = name;
	}

	public ParkingSpace(UUID id, ParkingLot parkingLot, ParkingSpaceStatus status, String name, boolean enabled) {
		this.parkingLot = parkingLot;
		this.id = id;
		this.status = status;
		this.name = name;
		this.enabled = enabled;
	}

	public UUID getID() {
		return id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ParkingLot getLot() {
		return parkingLot;
	}

	public ParkingSpaceStatus getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public void setStatus(ParkingSpaceStatus parkingStatus) {
		this.status = parkingStatus;
	}

	public boolean isBookable() {
		// Note: This only checks the logical status in the system.
		// Physical car presence is checked separately in
		// ParkingSpaceService.getAvailableSpaces()
		return this.status == ParkingSpaceStatus.AVAILABLE && this.enabled;
	}

	public void checkOut() {
		this.setStatus(ParkingSpaceStatus.AVAILABLE);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ParkingSpace other = (ParkingSpace) obj;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
