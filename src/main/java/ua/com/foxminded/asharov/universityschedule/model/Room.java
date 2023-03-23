package ua.com.foxminded.asharov.universityschedule.model;

import java.util.Objects;

public class Room extends AbstractEntity<Long> {
    public static final String ROOM_TABLE_NAME = "rooms";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_ADDRESS = "room_address";
    public static final String ROOM_CAPACITY = "room_capacity";

    private String address;
    private Integer capacity;

    public Room(Long id, String address, Integer capacity) {
        super(id);
        this.address = address;
        this.capacity = capacity;
    }

    public Room(String roomAddress, Integer roomCapacity) {
        this(null, roomAddress, roomCapacity);
    }

    public Room() {
        this(null, null, null);
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer roomCapacity) {
        this.capacity = roomCapacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(address, capacity);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        return Objects.equals(address, other.address) && Objects.equals(capacity, other.capacity);
    }

    @Override
    public String toString() {
        return "Room [address=" + address + ", capacity=" + capacity + "]";
    }

}
