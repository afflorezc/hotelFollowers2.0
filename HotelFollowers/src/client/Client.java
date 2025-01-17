package client;

import java.util.Date;

import hotelfollowers.Room;

public class Client {
	
	private static short counter;
	
	private short clientNo;
	private String name;
	private String id;
	private String address;
	private String phoneNo;
	private Date entryDate;
	private Date exitDate;
	private boolean lodged;
	
	public Client() {
		clientNo = ++counter;
	}

	public Client(String name, String id, String address, String phoneNo) {
		
		this.name = name;
		this.id = id;
		this.address = address;
		this.phoneNo = phoneNo;
		this.clientNo = ++counter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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

	
	public boolean isLodged() {
		return lodged;
	}

	public void setLodged(boolean lodged) {
		this.lodged = lodged;
	}
	
	public boolean checkIn(Room room, Date entry, Date exit) {
		
		if(room.getOccupancy() == room.getCapacity()) {
			return false;
		}
		room.addGuest(this);
		this.setEntryDate(entry);
		this.setExitDate(exit);
		if(room.isAvailable()) {
			room.setStatus(false);
		}
		return true;
		
	}

	@Override
	public String toString() {
		
		String info =  "Cliente N° " + clientNo + ":\n"
				+ "Nombre          : " + name + "\n" 
				+ "id              : " + id + "\n" 
				+ "Dirección       : " + address + "\n"
				+ "N° de telefono  : " + phoneNo + "\n" 
				+ "Fecha de entrada: " + entryDate + "\n";
		
		if(isLodged()) {
			info += "Contrato hasta  : " + exitDate +"\n";
		} else {
			info += "Fecha de salida : " + exitDate +"\n";
		}
		
		return info;
	}

}
