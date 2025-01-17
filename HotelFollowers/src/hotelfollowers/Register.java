package hotelfollowers;

import reading.Reader;
import client.*;

import java.util.ArrayList;

public class Register {
	
	private static Reader reader = new Reader();
	private static Hotel hotelFollowers;
	
	public static void main(String[] args) {
		
		System.out.println("Bienvenido al Hotel Followers!! \n"
				         + "Somos expertos en la mejor atención y servicio de hospedaje.");
		hotelFollowers = new Hotel();
		int menuChoice = 0;
		do {
			menuChoice = getMenuChoice();
			executeChoice(menuChoice);
		} while(menuChoice != 5);
		
		
	}
	
	public static String showMenu() {
		
		return "Seleccione la opción de acerdo a sus necesidades: \n \n"
				+ "1. Registrar clientes \n"
				+ "2. Realizar una reserva \n"
				+ "3. Ver registro de clientes \n"
				+ "4. Ver reservas \n"
				+ "5. Salir \n";
	}
	
	public static int getMenuChoice() {
		
		System.out.println(showMenu());
		return reader.readIntRange(1, 5, "Selección no válida", "Ingrese el número adecuado a la opción dada \n" +showMenu());
	}
	
	public static void executeChoice(int menuChoice) {
		
		switch(menuChoice) {
		
			case 1 -> hotelFollowers.clientRegister();
			case 2 -> hotelFollowers.doReservation();
			case 3 -> showClientList();
			case 4 -> showBookings();
		}
	}
	
	public static void showClientList() {
		
		ArrayList<Client> clients = hotelFollowers.getClients();
		
		if(clients == null || clients.size()==0) {
			System.out.println("Aun no se tienen clientes registrados");
			return;
		} else {
			
			System.out.println("Se tiene registro de " + clients.size() + " clientes");
			
			for(Client client:clients) {
		
				System.out.println(client + "\n");
			}
		}
	}
	
	public static void showBookings() {
		
		ArrayList<Booking> bookings = hotelFollowers.getBookings();
		
		if(bookings == null || bookings.size()==0) {
			System.out.println("En el momento no existen reservas registradas");
			return;
		} else {
			
			System.out.println("Se tiene registro de " + bookings.size() + " reservas");
			
			for(Booking booking:bookings) {
		
				System.out.println(booking + "\n");
			}
		}
		
	}
		

}
