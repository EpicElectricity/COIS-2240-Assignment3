import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    loadData();
    private static RentalSystem instance;
    
    
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	}
    	return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }
    
    public void saveVehicle(Vehicle vehicle) {
    	File vehicles = new File("vehicles.txt");
    	if (vehicles.exists()) {
    		try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(vehicles, true));
				writer.write(vehicle.getLicensePlate());
				writer.write(vehicle.getMake());
				writer.write(vehicle.getModel());
				writer.write(vehicle.getYear());
				writer.write(vehicle.getStatus().toString());
				writer.close();
			} catch (IOException e) {
				System.out.println("Error writing file");
			}
    	}
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }
    
    public void saveCustomer(Customer customer) {
    	File customers = new File("customers.txt");
    	if (customers.exists()) {
    		try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(customers, true));
				writer.write(customer.getCustomerId());
				writer.write(customer.getCustomerName());
				writer.close();
			} catch (IOException e) {
				System.out.println("Error writing file");
			}
    	}

    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    
    
    public void saveRecord(RentalRecord record) {
    	File rentalRecords = new File("rentalRecords.txt");
    	if (rentalRecords.exists()) {
    		try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(rentalRecords, true));
	                    writer.write(record.getRecordType()); 
	                    writer.write(record.getVehicle().getLicensePlate());
	                    writer.write(record.getCustomer().getCustomerName());
	                    writer.write(record.getRecordDate().toString());
	                    writer.write(String.valueOf(record.getTotalAmount()));
				writer.close();
			} catch (IOException e) {
				System.out.println("Error writing file");
			}
    	}

    }

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    private void loadData() {
    	File rentalRecords = new File("rentalRecords.txt");
    	if (rentalRecords.exists()) {
    		try {
				BufferedReader reader = new BufferedReader(new FileReader(rentalRecords));
				rentalHistory = reader.read();
				reader.read();
				reader.close();
			} catch (IOException e) {
				System.out.println("Error reading file");
			}
    	}
    	File vehicles = new File("vehicles.txt");
    	if (vehicles.exists()) {
    		try {
				BufferedReader reader = new BufferedReader(new FileReader(vehicles));
				Vehicle <vehicles> = reader.read();
				reader.read();
				reader.close();
			} catch (IOException e) {
				System.out.println("Error reading file");
			}
    	}
    	File customers = new File("customers.txt");
    	if (customers.exists()) {
    		try {
				BufferedReader reader = new BufferedReader(new FileReader(customers));
				customers = reader.read();
				reader.read();
				reader.close();
			} catch (IOException e) {
				System.out.println("Error reading file");
			}
    	}

    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
}