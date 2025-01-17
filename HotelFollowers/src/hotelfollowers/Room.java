package hotelfollowers;

import client.*;
import java.util.ArrayList;

public class Room {
	
	String type;
	short roomNo;
	boolean available;
	float cost;
	short reservedOccupancy;
	short capacity;
	float minOccupancy;
	String specs;
	ArrayList<Client> guests;
	
	public Room() {
	
	}

	public Room(String type, short roomNo, boolean status, float cost, String specs) {
	
		this.type = type;
		this.roomNo = roomNo;
		this.available = status;
		this.cost = cost;
		this.specs = specs;
		this.setCapacity();
		this.setMinOccupancy();
		guests = new ArrayList<Client>();
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public short getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(short roomNo) {
		this.roomNo = roomNo;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setStatus(boolean status) {
		this.available = status;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public short getCapacity() {
		return capacity;
	}
	
	
	public final void setCapacity() {
		
		switch(this.type) {
		
			case "Individual" -> this.capacity = 1;
			case "Doble" -> this.capacity = 2;
			case "Familiar" -> this.capacity = 6;
		}
	}
		
	public short getReservedOccupancy() {
		return reservedOccupancy;
	}

	public void setReservedOccupancy(short reservedOccupancy) {
		this.reservedOccupancy = reservedOccupancy;
	}

	public float getMinOccupancy() {
		return minOccupancy;
	}

	public final void setMinOccupancy() {
		
		switch(this.type) {
		
			case "Individual" -> this.minOccupancy = 1.0f;
			case "Doble" -> this.minOccupancy = 1.5f;
			case "Familiar" -> this.minOccupancy = 3.0f;
		}
	}
	
	public short getOccupancy() {
		return (short) guests.size();
	}
	
	public boolean reserveRoom(short reservedOccupancy) {
		
		if(this.getCapacity() < reservedOccupancy) {
			return false;
		}
		
		this.available = false;
		this.reservedOccupancy = reservedOccupancy;
		return true;
	}

	public ArrayList<Client> getGuests() {
		return guests;
	}

	public void addGuest(Client guest) {
		this.guests.add(guest);
	}
	
	public String showSpecs() {
		
		return "Comodidades: "  + specs;
		
	}
	@Override
	public String toString() {
		return "Room [type=" + type + ", roomNo=" + roomNo + ", available=" + available + ", cost=" + cost
				+ ", reservedOccupancy=" + reservedOccupancy + ", capacity=" + capacity + ", minOccupancy="
				+ minOccupancy + "]";
	}
	
	
}
