package client;

import hotelfollowers.Room;
import datemanagement.DateManagement;

import java.util.Date;
import java.util.ArrayList;

public class Booking {
	
	private static short counter = 0;
	private static DateManagement dateManagement = new DateManagement();
	
	private short bookingId;
	private short totalClients;
	private Date entryDate;
	private Date exitDate;
	private float lodgingCost;
	private ArrayList<Room> reservedRooms;
	
	public Booking() {
		this.bookingId = ++counter;

	}

	public Booking(short totalClients, Date entryDate, Date exitDate, ArrayList<Room> reservedRooms) {
		
		this.totalClients = totalClients;
		this.entryDate = entryDate;
		this.exitDate = exitDate;
		this.reservedRooms = reservedRooms;
		this.bookingId = ++counter;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public float getLodgingCost() {
		return lodgingCost;
	}

	public void setLodgingCost(float lodgingCost) {
		this.lodgingCost = lodgingCost;
	}

	public ArrayList<Room> getReservedRooms() {
		return reservedRooms;
	}

	public void setReservedRooms(ArrayList<Room> reservedRooms) {
		this.reservedRooms = reservedRooms;
	}
	
	public void calculateCost() {
		
		lodgingCost = 0.0f;
		long days = dateManagement.daysBetweenDates(entryDate, exitDate);
		
		for(Room room: reservedRooms) {
			
			float reservedOccupancy = (float) room.getReservedOccupancy();
			float minOccupancy = room.getMinOccupancy();
			float cost = room.getCost();
			if(reservedOccupancy < minOccupancy ) {
				lodgingCost += minOccupancy*cost;
			} else {
				lodgingCost += reservedOccupancy*cost;
			}
		}
		
		lodgingCost *= days;
	}

	@Override
	public String toString() {
		
		String info = "Reserva N° " + bookingId + "\n \n"
				+ "Total de huespedes   : " + totalClients + "\n"
				+ "Fecha de ingreso     : " + entryDate + "\n"
				+ "Fecha de salida      : " + exitDate + "\n"
				+ "Costo hospedaje      : " + lodgingCost + "\n"
				+ "Cantidad habitaciones: " + reservedRooms.size() + "\n \n"
				+ "Detalles de Habitaciones \n \n";
		
		for(Room room:reservedRooms) {
			
			info += "Número de habitación: " + room.getRoomNo() + "\n"
				  + "Tipo de habitación  : " + room.getType()	+ "\n"
				  + "Huespedes asignados : " + room.getReservedOccupancy() + "\n \n";
				  
		}
		
		return info;
	}
	
	
		
}
