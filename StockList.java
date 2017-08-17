import java.io.*;
import java.util.*;

class Detail {      //detail means the part which keep the value of the hashmap.It controles the security name and the price
	String security_name;
	String price;

	public Detail(String security_name, String price) {    //constructor of detail
		this.security_name = security_name;
		this.price = price;
	}

	public void setPrice(String price) {		//setter of the price
		this.price = price;
	}
}


public class StockList {

	private Map<String, Detail> stockList1;     //use the map to keep the records
	private String [] fields;


	public StockList(String cvsFile, String symbol, String sname, String price)  {
		FileReader fileRd=null;
		BufferedReader reader=null;

		try {
			fileRd = new FileReader(cvsFile);
			reader = new BufferedReader(fileRd);


			String header = reader.readLine();
			fields = header.split(",");// keep field names


			int keyIndex = findIndexOf(symbol);      //keep the indexes of the headers (attributes)
			int valIndex = findIndexOf(sname);
			int priceIndex=findIndexOf(price);

			if(keyIndex == -1 || valIndex == -1 || priceIndex==-1)throw new IOException("CVS file does not have data");

			// get a new hash map
			stockList1 = new HashMap<String, Detail>();


			String [] tokens;    
			for(String line = reader.readLine();     //read all lines
				line != null;
				line = reader.readLine()) {
				tokens = line.split(",");     //split to patrs key,security name and price
				Detail detail=new Detail(tokens[valIndex],tokens[priceIndex]);
				stockList1.put(tokens[keyIndex], detail);
			}

			if(fileRd != null) fileRd.close();
			if(reader != null) reader.close();

			// I can catch more than one exceptions
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Malformed CSV file");
			System.out.println(e);
		}
	}

	private int findIndexOf(String key) {      //find the key is available and return its position in a line (it means 0/1/2)
		int i;
		for(i=0; i < fields.length; i++) {
			if (fields[i].equals(key))
				return i;
		}
		System.out.println(fields[i-1]+key);
		return -1;
	}

	// public interface
	public String[] findName_price(String key) {    //find the security name and price of an item from the hashmap if needed
		Detail details=stockList1.get(key);
		String array[]={details.security_name,details.price};
		return array;
	}

	public boolean findkey(String key){      //find if a key is contain in a hashmap
		return stockList1.containsKey(key);
	}

	public void setstockList1value(String symbol,String value) {      //set(change) the price of the corresponding symbol when needed.This is the hashmap updating part
		Detail detail=stockList1.get(symbol);
		detail.setPrice(value);
	}
}