package home.models;

public enum VehicleType {

    CAR("car"),
    MOTORCYCLE("motorcycle"),
    TRUCK("truck");

    private final String type;

    VehicleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static VehicleType getVehicleType(String type) {
        for (VehicleType vehicleType : VehicleType.values()) {
            if (type.strip().equalsIgnoreCase(vehicleType.getType())) {
                return vehicleType;
            }
        }
        return null;
    }

    public boolean in(VehicleType... vehicleTypes) {
        for (var vehicleType : vehicleTypes) {
            if (this == vehicleType) {
                return true;
            }
        }
        return false;
    }
}
