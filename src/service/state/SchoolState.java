package service.state;

import model.location.Location;
import model.user.UserInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class SchoolState{
    public SchoolState(){};
    private UserInterface currentUser;
    private Location currentLocation;
    private Map<Location, Set<UserInterface>> locationOccupants = new HashMap<>();
    private Map<String, String> parentChildMap = new HashMap<>();
    private Map<Location, UserInterface> teacherInLocation = new HashMap<>();

    public UserInterface getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInterface currentUser) {
        this.currentUser = currentUser;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Map<Location, Set<UserInterface>> getLocationOccupants() {
        return locationOccupants;
    }

    public Set<UserInterface> getUsersInLocation(Location location) {
        return locationOccupants.getOrDefault(location, new HashSet<>());
    }

    public void addUserToLocation(UserInterface user, Location location) {
        locationOccupants.computeIfAbsent(location, k -> new HashSet<>()).add(user);
    }

    public void removeUserFromLocation(UserInterface user, Location location) {
        Set<UserInterface> occupants = locationOccupants.get(location);
        if (occupants != null) {
            occupants.remove(user);
            if (occupants.isEmpty()) {
                locationOccupants.remove(location);
            }
        }
    }

    public void setParentChild(String parentName, String childName) {
        parentChildMap.put(parentName, childName);
    }

    public String getChildForParent(String parentName) {
        return parentChildMap.get(parentName);
    }

    public void setTeacherInLocation(Location location, UserInterface teacher) {
        teacherInLocation.put(location, teacher);
    }

    public UserInterface getTeacherInLocation(Location location) {
        return teacherInLocation.get(location);
    }

    public boolean isTeacherPresent(Location location) {
        return teacherInLocation.containsKey(location);
    }
}