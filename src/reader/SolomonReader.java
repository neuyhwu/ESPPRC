package reader;

import model.Customer;
import model.EspprcInstance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SolomonReader {
	private EspprcInstance instance;
	private String file;

	public SolomonReader(EspprcInstance instance, String file){
		this.instance = instance;
		this.file = file;
	}

	public void read() {
		try {
			FileReader reader = new FileReader(this.file);
			BufferedReader br = new BufferedReader(reader);

			int counter = 0;
			String line;
			while ((line = br.readLine()) != null) {
				counter++;
				this.readInstace(line, counter);
			}

			br.close();
			reader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File " + file + " not found!");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private void readCustomer(String[] tokens) {
		Customer customer = new Customer( Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]) );
		
		customer.setCustomerId( Integer.parseInt(tokens[0]) );
		customer.setDemand( Integer.parseInt(tokens[3]) );
		customer.setStart( Double.parseDouble(tokens[4]) );
		customer.setEnd( Double.parseDouble(tokens[5]) );
		customer.setServiceTime( Double.parseDouble(tokens[6]) );
		
		// Add the node to the instance
		this.instance.getNodes()[customer.getCustomerId()] = customer;
	}

	private void readVehicle(String[] tokens) {
		this.instance.setVehicles( Integer.parseInt(tokens[0]) );
		this.instance.setCapacity( Double.parseDouble(tokens[1]) );
	}

	private void readInstace(String line, int counter) {

		line = line.replace("\r", "");
		line = line.trim();
		String[] tokens = line.split(" +");

		if (counter == 5) {
			readVehicle(tokens);
		}
		else if (counter == 10) {
			// origin node
			if(this.file.contains("_25")) {
				this.instance.setNodes(new Customer[26]);
			}
			else if(this.file.contains("_50")) {
				this.instance.setNodes(new Customer[51]);
			}
			else {
				this.instance.setNodes(new Customer[101]);
			}
			readCustomer(tokens);
		}
		else if (counter > 10 && tokens.length == 7){
			// customers
			readCustomer(tokens);
		}
	}
	
	public EspprcInstance getInstance() {
		return instance;
	}

	public void setInstance(EspprcInstance instance) {
		this.instance = instance;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}