package hotelfollowers;

import reading.Reader;

import java.util.Date;

import java.util.ArrayList;
import java.io.*;
import client.*;
import datemanagement.DateManagement;

public class Hotel {
	
	private static final String ROOMS_FILE = "rooms.txt";
	private static Reader reader = new Reader();
	private static DateManagement datemanager = new DateManagement();
	
	private ArrayList<Room> individualRooms;
	private ArrayList<Room> doubleRooms;
	private ArrayList<Room> familiarRooms;
	private ArrayList<Client> clients;
	private ArrayList<Booking> bookings;
	
	public Hotel() {
		
		individualRooms = new ArrayList<Room>();
		doubleRooms = new ArrayList<Room>();
		familiarRooms = new ArrayList<Room>();
		clients = new ArrayList<Client>();
		bookings = new ArrayList<Booking>();
		uploadRooms();
		
	}

	public ArrayList<Booking> getBookings() {
		return bookings;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public String readInfo(BufferedReader buffer) {
		
		String line;
		try {
			line = buffer.readLine();
			return line.substring(line.indexOf(":")+2);
		} catch (IOException e) {
			
			e.printStackTrace();
			return "";
		}
		
	}
	
	public void uploadRooms() {
		
		String pathName = new File("").getAbsolutePath() +"\\";
		pathName += ROOMS_FILE;
		
		try {
			FileReader fReader = new FileReader(new File(pathName));
			BufferedReader buffer = new BufferedReader(fReader);
			String line = " ";
			
			while(line != null) {
				
				short roomNo = Short.parseShort(readInfo(buffer));
				String type = readInfo(buffer);
				String status = readInfo(buffer);
				float cost = Float.parseFloat(readInfo(buffer));
				String specs = readInfo(buffer);
				boolean availability = false;
				if(status.equals("Disponible")) {
					availability = true;
				}
				
				readInfo(buffer);
				
				switch(type) {
					case "Individual" -> individualRooms.add(new Room(type, roomNo, availability, cost, specs));
					case "Doble" -> doubleRooms.add(new Room(type, roomNo, availability, cost, specs));
					case "Familiar" -> familiarRooms.add(new Room(type, roomNo, availability, cost, specs));
				}
				
			}	
		} catch(Exception e) {
			//System.out.println("End of file");
			return;
		}
	}
	
	public void clientRegister() {
		
		System.out.println("Ingrese por favor el número de clientes que desea registrar:");
		
		int totalClients = reader.readPosInt("Valor no válido", "El número de clientes a registrar debe ser un entero positivo");
		
		PrincipalClient principalClient;
		ArrayList<Client> guestList = new ArrayList<Client>();
		ArrayList<Room> availableRooms;
		ArrayList<Room> reservedRooms = new ArrayList<Room>();
		
		Date entryDate; 
		Date exitDate;
		
		Booking toRegisterBook = null;
		
		System.out.print("Realice el registro del cliente principal o encargado de la facturación \n");
		principalClient = (PrincipalClient) addClient(true);
		
		for(int i=1;i<totalClients;i++) {
			System.out.println("Registre al acompañante " + i);
			guestList.add((CompanionClient) addClient(false));
		}
		
		principalClient.setGuestList(guestList);
		
		if(principalClient.hasReservation()) {
			ArrayList<Booking> bookings = principalClient.getBookings();
			
			for(Booking booking:bookings) {
				if(booking.getEntryDate().compareTo(new Date())==0) {
					
					System.out.println("El cliente tiene una reserva para la fecha de ingreso");
					toRegisterBook = booking;
					
					break;
				}
			}
			if(toRegisterBook != null) {
				reservedRooms = toRegisterBook.getReservedRooms();
				entryDate = toRegisterBook.getEntryDate();
				exitDate = toRegisterBook.getExitDate();
			} else {
				
				System.out.println("El cliente tiene reservas pero no para la fecha de ingreso");
				entryDate = new Date(); 

				exitDate = datemanager.readAndValidateDate("Ingrese por favor la fecha hasta la cual se hospedará el cliente", 
							"La fecha de salida debe ser una fecha posterior a la fecha de ingreso", entryDate);
			}
			
		} else {
			
			entryDate = new Date(); 

			exitDate = datemanager.readAndValidateDate("Ingrese por favor la fecha hasta la cual se hospedará el cliente", 
						"La fecha de salida debe ser una fecha posterior a la fecha de ingreso", entryDate);
			
			availableRooms = getAvailableRooms(totalClients);
			int accommodatedClients = 0;
			
			while(accommodatedClients < totalClients) {
				accommodatedClients += selectRoom(availableRooms, reservedRooms);
			}
		}
		
		assignRooms(reservedRooms, principalClient, entryDate, exitDate, toRegisterBook);
		
	}
	
	public void assignRooms(ArrayList<Room> reservedRooms, PrincipalClient principalClient, Date entry, Date exit, 
			                        Booking booking) {
		
		ArrayList<Client> guestList = new ArrayList<Client>();
		
		for(Client client:principalClient.getGuestList()) {
			guestList.add(client);
		}
		guestList.add(principalClient);
		
		if(booking == null) {
			Booking inmediatlyBooking = new Booking((short) (guestList.size()+1), entry, exit, reservedRooms);
			inmediatlyBooking.calculateCost();
			principalClient.makeBook(inmediatlyBooking, true);
		
		} else {
			principalClient.makeBook(booking, false);
		}
		
		principalClient.setBookings(new ArrayList<Booking>());
		
		for(Room room:reservedRooms) {
			
			int assignedClients = 0;
			System.out.println("Asigne los huespedes para la habitación # " + room.getRoomNo());
			System.out.println("Esta es una habitación " + room.getType() + " y será ocupada por "
					            + room.getReservedOccupancy() + " personas"); 
			
			while(assignedClients < room.getReservedOccupancy()) {
				
				for(Client client:guestList) {
					
					String answer = reader.readAnswer("Desea asignar a " + client.getName() +
							        " a esta habitación? Si/No", "si", "no", "Respuesta no valida, escriba si o no");
					if(answer.equals("si")) {
						if(client.checkIn(room, entry, exit)) {
							assignedClients += 1;
						};
					}
				}
			}
		}
	}
	
	public boolean clientExist(String id) {
		
		if(clients == null) {
			return false;
		}
		
		for(Client client:clients) {
			if(client.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public Client addClient(boolean isPrincipal) {
		
		
		String id = reader.validateOnlyNumeric("Ingrese por favor el documento de identidad del cliente:", "Documento no valido",
				                               "Ingrese solo el número del documento sin espacios comas o guiones");
		
		if(!clientExist(id)) {
			
			String name = reader.validateOnlyText("Ingrese por favor el nombre del cliente", "Nombre no valido",
	                "No incluya caracteres numéricos o del tipo *, +, /, -");
			
			String address = reader.readString("Ingrese por favor la dirección del cliente");
			
			String phoneNo = reader.validateOnlyNumeric("Ingrese por favor el numéro de telefono del cliente:", "Número de telefono no valido",
	                                "Ingrese solo el número del documento sin espacios comas o guiones");
			
			Client client;
			if(isPrincipal) {
				
				client = new PrincipalClient(name, id, address, phoneNo);
			
			} else {
				
				client = new CompanionClient(name, id, address, phoneNo);
			}
			clients.add(client);
			return client;
		} else {
			return searchClientById(id);
		}
		
	}
	
	public Client searchClientById(String id) {
		
		if(clients == null) {
			return null;
		}
		for(Client client:clients) {
			if(client.getId().equals(id)) {
				return client;
			}
		}
		return null;
	}
	
	public ArrayList<Room> getAvailableRooms(int totalClients){
		
		ArrayList<Room> availableRooms;
		
		if(totalClients == 1) {
			availableRooms = showAvailableRooms("Individual");
			
		} else if(totalClients == 2) {			
			availableRooms = showAvailableRooms("Individual","Doble");
			
		} else {			
			availableRooms = showAvailableRooms("Individual", "Doble", "Familiar");
		}
		return availableRooms;
		
	}
	public void doReservation() {
		
		System.out.println("Ingrese por favor el número total de personas que se hospeadarán en el hotel");
		
		int totalClients = reader.readPosInt("Valor no válido","El numero de clientes debe ser entero y positivo");
		int accommodatedClients = 0;
		
		ArrayList<Room> availableRooms = getAvailableRooms(totalClients);
		ArrayList<Room> reservedRooms = new ArrayList<Room>();
		
		while(accommodatedClients < totalClients) {
			accommodatedClients += selectRoom(availableRooms, reservedRooms);
		}
		
		Date entryDate = datemanager.readAndValidateDate("Ingrese por favor la fecha en la cual se hospedará el cliente", 
				                             "La fecha de ingreso no puede ser anterior a la fecha actual", new Date()); 
		
		Date exitDate = datemanager.readAndValidateDate("Ingrese por favor la fecha hasta la cual se hospedará el cliente", 
				                       "La fecha de salida debe ser una fecha posterior a la fecha de ingreso", entryDate);
		
		Booking booking = new Booking((short) totalClients, entryDate, exitDate, reservedRooms);
		booking.calculateCost();
		this.bookings.add(booking);
		
		PrincipalClient client = (PrincipalClient) addClient(true);
		
		client.makeBook(booking, false);
		client.setEntryDate(entryDate);
		client.setExitDate(exitDate);
		
		System.out.println("Reserva realizada con exito.");
		String answer = reader.readAnswer("¿Desea ver los detalles de la reserva? Si/No", "si", "no", "Debe responder si o no");
		if(answer.equals("si")) {
			System.out.println(booking);
		}

	}
	
	public int selectRoom(ArrayList<Room> availableRooms, ArrayList<Room> reservedRooms) {
		
		System.out.println("Seleccione una de las habitaciones disponibles");
		int roomNo = 0;
		Room room = null;
		do {
			roomNo = reader.readIntRange(100, 516, "Error, no es un número correcto de habitación",
                    "Este hotel tiene habitaciones entre 101-115 hasta 501-515");
			room = roomByNo(availableRooms, roomNo);
			
		} while(room == null);
		
		System.out.println("Ha seleccionado una habitación de tipo: " + room.getType() + " con una capacidad de: "
				            + room.getCapacity() + " y ocupación minima de " + room.getMinOccupancy() + "\n"
				            + "Por favor ingrese la cantidad de huespuedes para los cuales desea reservar dicha haitación:");
		
		int occupancy = reader.readIntRange((int) room.getMinOccupancy(), room.getCapacity()+1, "Asignación no valida", 
				                "La asignación debe ser un valor entero y no puede superar la capacidad de la habitación");
		
		room.setReservedOccupancy((short) occupancy);
		room.setStatus(false);
		availableRooms.remove(room);
		
		reservedRooms.add(room);
		
		System.out.println("Felicitaciones! ha reservado la habitación " + room.getRoomNo());
		
		return occupancy;
	}
	
	public Room roomByNo(ArrayList<Room> rooms, int roomNo) {
		
		for(Room room:rooms) {
			
			if(room.getRoomNo()==roomNo) {
				return room;
			}
		}
		return null;
	}
	
	public ArrayList<Room> showAvailableRooms(String ... types) {
		
		ArrayList<Room> availableRooms = new ArrayList<Room>();
		
		for(String type:types) {
			
			switch(type) {
			
				case "Individual":
					showSpecs("Individual");
					listAvailableRooms(type, availableRooms);
					break;
	
				case "Doble":
					showSpecs("Doble");
					listAvailableRooms(type, availableRooms);
					break;
				case "Familiar":
					showSpecs("Familiar");
					listAvailableRooms(type, availableRooms);
					break;
			}	
		}
		
		return availableRooms;
	}
	
	public void showSpecs(String type) {
		
		int i = 1;
		switch(type) {
			case "Individual":
				
				System.out.println("A continuación se muestran los diferentes tipos de comodidades"
                        +" de las habitaciones individuales \n");
				
				for(Room room:individualRooms) {
					
					
					System.out.print(i+  "\t" + room.showSpecs() + "\n");
					i++;
					if(i == 6) break;
				}
				
				break;
				
			case "Doble":
				
				System.out.println("A continuación se muestran los diferentes tipos de comodidades"
                        +" de las habitaciones dobles \n");
				
				for(Room room:doubleRooms) {
										
					System.out.print(i+  "\t" + room.showSpecs() + "\n");
					i++;
					if(i == 5) break;
				}
				
				break;
			case "Familiar":
				
				System.out.println("A continuación se muestran los diferentes tipos de comodidades"
                        +" de las habitaciones familiares \n");
				
				for(Room room:familiarRooms) {
					
					System.out.print(i+  ". " + room.showSpecs() + "\n");
					i++;
					if(i == 7) break;
				}
				
				break;
		}
	}
	
	public void listAvailableRooms(String type, ArrayList<Room> availableRooms) {
		
		String[] options;
		
		switch(type) {
		
			case "Individual":
				
				options = new String[5];
				
				for(Room room:individualRooms) {
					int num = room.getRoomNo();
					int index = num % 5;
					if(room.isAvailable()) {
						if(options[index]==null) {
							options[index] = num + " ";
						} else {
							options[index] += num + " ";
						}
						availableRooms.add(room);
					}
				}
				
				break;
				
			case "Doble":
				
				options = new String[4];
				
				for(Room room:doubleRooms) {
					int num = room.getRoomNo();
					int index = (num-5) % 4;
					if(room.isAvailable()) {
						if(options[index]==null) {
							options[index] = num + " ";
						} else {
							options[index] += num + " ";
						}
						availableRooms.add(room);
					}
				}
				
				break;
				
			case "Familiar":
				
				options = new String[6];
				
				for(Room room:familiarRooms) {
					int num = room.getRoomNo();
					int index = (num-9) % 6;
					if(room.isAvailable()) {
						if(options[index]==null) {
							options[index] = num + " ";
						} else {
							options[index] += num + " ";
						}
						availableRooms.add(room);
					}
				}
				break;
				
			default:
				options = new String[0];
					
		}
		
		showList(options);
	}
	
	public void showList(String[] options) {
		
		for(int i=1; i <options.length; i++) {
			
			System.out.println("Habitaciones disponibles con comodidades descritas en " + i +": \n");
			System.out.println(options[i] + "\n");
		}
		
		System.out.println("Habitaciones disponibles con comodidades descritas en " + options.length +": \n");
		System.out.println(options[0] + "\n");
		
	}

}
