package consultingtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Fahrzeugtypen
enum VehicleType {
    CAR, MOTORCYCLE
}

// Klasse für ein Fahrzeug
class Vehicle {
    private String id;
    private VehicleType type;

    public Vehicle(String id, VehicleType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public VehicleType getType() {
        return type;
    }
}

// Klasse für das Parkhaus
class Garage {
    private int numberOfFloors;
    private int parkingSpacesPerFloor;
    private Map<String, ParkingSpot> occupiedSpots;

    public Garage(int numberOfFloors, int parkingSpacesPerFloor) {
        this.numberOfFloors = numberOfFloors;
        this.parkingSpacesPerFloor = parkingSpacesPerFloor;
        this.occupiedSpots = new HashMap<>();
    }

    public void leaveAllVehicles() {
        for (Map.Entry<String, ParkingSpot> entry : occupiedSpots.entrySet()) {
            String vehicleId = entry.getKey();
            ParkingSpot parkedSpot = entry.getValue();
            System.out.println("Vehicle with number plate " + vehicleId + " left from: " + parkedSpot);
        }

        occupiedSpots.clear(); // Entfernt alle Fahrzeuge aus der Garage
    }

  //Überprüfen, ob das Fahrzeug bereits in der Garage geparkt ist
    public ParkingSpot parkVehicle(Vehicle vehicle) {  
        if (occupiedSpots.containsKey(vehicle.getId())) {
            System.out.println("Vehicle " + vehicle.getId() + " is already parked in the garage.");
           return null;
        }

        // Durchsuchen der Parkplätze nach einem freien Platz
        for (int floor = 1; floor <= numberOfFloors; floor++) {
            for (int spot = 1; spot <= parkingSpacesPerFloor; spot++) {
                ParkingSpot parkingSpot = new ParkingSpot(floor, spot);
                if (!occupiedSpots.containsValue(parkingSpot)) {
                    occupiedSpots.put(vehicle.getId(), parkingSpot);
                    return parkingSpot;
                }
            }
        }
        return null; // Kein freier Parkplatz gefunden
    }

    public ParkingSpot findVehicle(String vehicleId) {
        return occupiedSpots.get(vehicleId);
    }

    public void leaveGarage(String vehicleId) {
        ParkingSpot parkedSpot = occupiedSpots.remove(vehicleId);
        if (parkedSpot != null) {
            System.out.println("Vehicle with number plate " + vehicleId + " left from: " + parkedSpot);
        } else {
            System.out.println("Vehicle with number plate " + vehicleId + " not found in the garage.");
        }
    }

    public int getFreeParkingSpaces() {
        return numberOfFloors * parkingSpacesPerFloor - occupiedSpots.size();
    }
}

// Klasse für einen Parkplatz
class ParkingSpot {
    private int floor;
    private int spot;

    public ParkingSpot(int floor, int spot) {
        this.floor = floor;
        this.spot = spot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ParkingSpot that = (ParkingSpot) obj;
        return floor == that.floor && spot == that.spot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, spot);
    }

    @Override
    public String toString() {
        return "Floor: " + floor + ", Spot: " + spot;
    }
}

public class Main {
    public static void main(String[] args) {
        // Anzahl an Etagen, Anzahl an Plätzen
        Garage garage = new Garage(3, 12);

        // Ausgabe der Anzahl freier Parkplätze
        System.out.println("Free parking spaces: " + garage.getFreeParkingSpaces());

        // Erstellen von mehreren Fahrzeugen
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("Con360", VehicleType.CAR)); // Auto
        vehicles.add(new Vehicle("Sulting360", VehicleType.MOTORCYCLE)); // Motorrad
        vehicles.add(new Vehicle("B444", VehicleType.CAR)); // Auto
        vehicles.add(new Vehicle("B142", VehicleType.MOTORCYCLE)); // Motorrad
        vehicles.add(new Vehicle("Con360", VehicleType.CAR)); // Doppelt erstellt um zu testen, ob Auto doppelt geparkt wird

        // Einparken der Fahrzeuge in die Garage
        for (Vehicle vehicle : vehicles) {
            ParkingSpot spot = garage.parkVehicle(vehicle);
            if (spot != null) {
                System.out.println(vehicle.getType() + " " + vehicle.getId() + " parked at: " + spot);
            } else {
            	System.out.println("The vehicle with ID " + vehicle.getId() + " has already been parked.");

            }
        }

        // Beispiel: Suchen eines Autos
        Vehicle carToFind = vehicles.get(0); // Beispielhaft das erste Auto suchen
        ParkingSpot foundCarSpot = garage.findVehicle(carToFind.getId());
        if (foundCarSpot != null) {
            System.out.println("Vehicle with the ID: "+ carToFind.getId()+ " is at " + foundCarSpot);
        } else {
            System.out.println("Car not found in the garage.");
        }

        // Beispiel: Auto verlässt die Garage
        garage.leaveGarage("B444");
        System.out.println("Free parking spaces after car leaves: " + garage.getFreeParkingSpaces());

        // Beispiel: Alle Fahrzeuge aus der Garage entfernen
        garage.leaveAllVehicles();
        System.out.println("Free parking spaces after all vehicles leave: " + garage.getFreeParkingSpaces());
    }
}
