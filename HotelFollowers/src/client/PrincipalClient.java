package client;

import hotelfollowers.Room;

import java.util.ArrayList;
import java.util.Date;

public class PrincipalClient extends Client {
	
	boolean reservation;	
	private ArrayList<Client> guestList;
	private ArrayList<Booking> bookings;

	public PrincipalClient() {
		super();
	}

	public PrincipalClient(String name, String id, String address, String phoneNo) {
		
		super(name, id, address, phoneNo);
		guestList = new ArrayList<Client>();
		bookings = new ArrayList<Booking>();
	}
	
	public boolean hasReservation() {
		return reservation;
	}

	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}

	public ArrayList<Client> getGuestList() {
		return guestList;
	}

	public void setGuestList(ArrayList<Client> guestList) {
		this.guestList = guestList;
	}
	
	public ArrayList<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings;
	}

	
	public void makeBook(Booking booking, boolean inRegister) {
		
		this.bookings.add(booking);
		this.setReservation(true);
		// Billing
		float cost = booking.getLodgingCost();
		System.out.println("El costo total del hospedaje es de: " + cost);
		if(inRegister) {
			System.out.println("El cliente ha cancelado el 100% del total, correspondiente a " + cost);
		} else {
			System.out.println("El cliente ha cancelado el 50% del total, correspondiente a " + 0.5*cost);
		}
		
		
	}
	
	public void modifyBook(Booking oldBooking, Booking newBooking) {
		
		if(this.bookings.contains(oldBooking)) {
			this.bookings.remove(oldBooking);
			this.bookings.add(newBooking);
		}
	}
	
	@Override
	public boolean checkIn(Room room, Date entry, Date exit) {
		
		if(super.checkIn(room, entry, exit)) {
			//billing
			return true;
		} else {
			return false;
		}
	}
	
}
