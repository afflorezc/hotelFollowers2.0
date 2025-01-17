package datemanagement;

import reading.Reader;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateManagement {
	
	private static Reader reader = new Reader();
	
	public Date readAndValidateDate(String inputMessage, String errorMessage, Date comparisonDate) {
		
		Date date;
		
		do {
			date = reader.readDate(inputMessage, "Fecha invalida");
			if(date.compareTo(comparisonDate) <= 0) {
				System.out.println(errorMessage);
				}
		} while(date.compareTo(comparisonDate) <=0);
		
		return date;
	}
	
	public long daysBetweenDates(Date date1, Date date2) {
		
		return TimeUnit.MILLISECONDS.toDays(date2.getTime()-date1.getTime());
	}

}
