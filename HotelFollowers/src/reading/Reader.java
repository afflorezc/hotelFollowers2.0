package reading;

import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * Clase que utiliza el Scanner de java para leer los ingresos mmediante texto de los usuarios
 * Define unicamente funciones de lectura de diferentes tipos de datos
 */

public class Reader {
    
    Scanner sc = new Scanner(System.in);
    
    /*
     *  Se realiza una lectura de entrada de texto validada como un valor numérico entero
     */
    public int readInt(String message, String errorMessage, String inErrorMessage){
        System.out.println(message + ": ");
        validateInt(errorMessage, inErrorMessage);
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }
    /*
     * Se realiza una lectura de entrada de texto y se valida como un valor numérico real
     * devuelto como un tipo de doble precisión
     */
    public double readDouble(String message, String errorMessage, String suggestionMessage){
        System.out.println(message + ": ");
        validateDouble(errorMessage, suggestionMessage);
        double dato = sc.nextDouble();
        sc.nextLine();
        return dato;
    }
    
    /*
     * Se realiza una lectura de entrada de texto y se valida como un valor numérico real
     * devuelto como un tipo de simple precisión (float)
     */
    public float readFloat(String message, String errorMessage, String suggestionMessage){
        System.out.println(message + ": ");
        validateFloat(errorMessage, suggestionMessage);
        float dato = sc.nextFloat();
        sc.nextLine();
        return dato;
    }
    /*
     *  Lectura de valores reales que sean solo mayores a cero devuelto como un flotante de 
     *  precisión simple o float
     */
    public float readPosFloat(String errorMessage,
            String suggestionMessage) {
    	
    	float number = 0;
    	do {

    	validateFloat(errorMessage, suggestionMessage);
    	number = sc.nextFloat();

    	} while (number <= 0);
    	
    	sc.nextLine();
    	return number;
    }
    /*
     * Se valida la lectura de un valor numérico real en un rango deseado sin incluir los extremos
     * se devuelve el valor como un tipo float
     */
    public float readFloatRange(float start, float end, String errorMessage,
            String suggestionMessage) {
    	float number = 0.0f;
    	do {
    			validateFloat(errorMessage, suggestionMessage);
    			number = sc.nextFloat();

    		} while (number < start && number > end );
    	
    	sc.nextLine();
    	return number;
    }
    /*
     * Lectura simple de una cadena de caracteres hasta que se encuentre un salto de linea
     */
    public String readString(String message){
        System.out.println(message +": ");
        
        return sc.nextLine();

    }
    

	public Date readDate(String message, String errorMessage) {
    	
    	Date date;
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	
    	System.out.println(message + " use el formato dd/mm/aaaa");
    	
    	String stringDate = sc.nextLine();
    	
    	while(invalidDateFormat(stringDate)) {
    		
    		System.out.println(errorMessage);
    		System.out.println("Recuerde que debe escribir con el siguiente formato: dd/mm/aaaa");
    		stringDate = sc.nextLine();
    	}
    	
    	 do{
    		try {
        		
        		date = format.parse(stringDate);
        	} catch(ParseException e) {
        		System.out.println("No se pudo establecer la fecha dada");
        		date = new Date();
        	}
        
    	}while(date.equals(new Date()));   	
    	
    	 sc.nextLine();
    	return date;
    	
    }
	
	public boolean invalidDateFormat(String strDate) {
		
		try {
			
			if(!strDate.substring(2,3).equals("/")) { return true;}
			
			if(!strDate.substring(5,6).equals("/")) { return true;}
			
			Integer.parseInt(strDate.substring(0,2));
			Integer.parseInt(strDate.substring(3,4));
			Integer.parseInt(strDate.substring(6));
			
			return false;
			
		} catch(Exception e) {
			return true;
		}
	}
    
    /*
     * Realiza la lectura de una respuesta del usuario ante una pregunta con dos opciones de 
     * respuesta de tipo si o no
     */
    public String readAnswer(String question, String yes, String no, String errorMessage) {
    	
    	System.out.println(question);
    	
    	yes.toLowerCase();
    	no.toLowerCase();
    	
    	String answer = sc.nextLine();
    	answer.toLowerCase();
    	
    	while(!answer.equals(yes) && !answer.equals(no)) {
    		
    		System.out.println(errorMessage);
    		answer = sc.nextLine();
    		answer.toLowerCase();
    	}
    	
    	sc.nextLine(); 
    	return answer;
    }
    
    /*
     *  Realiza una lectura de ingreso de texto y la acepta si solo es alfabetica, es decir, si no presenta
     *  signos operativos ni valores númericos (validación de ingreso correcto de nombres)
     */
    public String validateOnlyText(String message, String errorMessage, String suggestionMessage) {
    	
    	System.out.println(message);
    	String word = sc.nextLine();
    	
    	while(isAlphaNumeric(word)) {
    		
    		System.out.println(errorMessage);
    		System.out.println(suggestionMessage);
    		word = sc.nextLine();
    	}
    	
    	sc.nextLine();
    	return word;
    }
    
    public String validateOnlyNumeric(String message, String errorMessage, String suggestionMessage) {
    	
    	System.out.println(message);
    	String word = sc.next();
    	
    	while(hasLetters(word)) {
    		
    		System.out.println(errorMessage);
    		System.out.println(suggestionMessage);
    		word = sc.next();
    	}
    	
    	sc.nextLine();
    	return word;
    	
    }
    
    public boolean hasLetters(String word) {
    	
    	for(int i=0; i< word.length(); i++) {
    		
    		char let[] = {word.charAt(i)};
    		String letter = new String(let);
    		try {
    			
    			Integer.parseInt(letter);
    			
    		} catch(Exception e) {
    			
    			return true;
    		}
    	}
    	return false;
    }
    
    /*
     * Evalua si una cadena de texto es alfanumérico, es decir, si combina letras, números y/o símbolos
     * operativos como +, -, * y /
     */
    public boolean isAlphaNumeric(String word) {
    	
    	String[] numerics = {"0", "1", "2", "3", "4",
    			             "5", "6", "7", "8", "9", "/",
    			             "*", "-", "+"};
    	
    	for(String num:numerics) {
    		
    		if(word.indexOf(num) != -1) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /*
     * Validación de un valor entero mediante un bucle   
     */
    public void validateInt(String errorMessage, String suggestionMessage) {
    	
    	while (!sc.hasNextInt()) {
            System.out.println(errorMessage);
            System.out.println(suggestionMessage);
            sc.next();
        }
    }
    /*
     * Validación de un valor numérico real que se devuelve como de tipo float
     */
    public void validateFloat(String errorMessage, String suggestionMessage){
        while (!sc.hasNextFloat()) {
            System.out.println(errorMessage);
            System.out.println(suggestionMessage);
            sc.next();
        }
    }
    /*
     * Validación de un valor numérico real que se devuelve como de tipo doble
     */
    public void validateDouble(String errorMessage, String suggestionMessage){
        while (!sc.hasNextDouble()) {
            System.out.println(errorMessage);
            System.out.println(suggestionMessage);
            sc.next();
        }
    }
    /*
     * Realiza una lectura de un valor numérico entero dentro de un rango establecido sin
     * incluir los extremos.
     */
    public int readIntRange(int start, int end, String errorMessage,
                           String suggestionMessage) {
    	
        int number = 0;
        do {
            
            validateInt(errorMessage, suggestionMessage);
            number = sc.nextInt();
            
        } while (number < start && number > end );
        
        sc.nextLine();
        return number;
    }
    /*
     * Lectura de un valor numérico entero mayor a cero
     */
    public int readPosInt(String errorMessage,
                           String suggestionMessage) {
    	
        int number = 0;
        do {
            validateInt(errorMessage, suggestionMessage);
            number = sc.nextInt();
            
        } while (number <= 0);
        
        sc.nextLine();
        return number;
    }
}
