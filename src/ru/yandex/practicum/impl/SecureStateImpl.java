package ru.yandex.practicum.impl;

import model.condition.OwnChildOnlyCondition;
import model.condition.TeacherPresentCondition;
import model.journal.JournalInterface;
import model.journal.SchoolJournal;
import model.location.*;
import model.location.Classroom;
import model.permission.*;
import model.role.*;
import model.user.User;
import model.user.UserInterface;
import ru.yandex.practicum.SecureState;

import service.auth.AuthenticationService;
import service.auth.AuthorizationService;
import service.state.SchoolState;

import java.util.*;

public class SecureStateImpl implements SecureState {
    private final Map<String, User> users = loadUsers();
    private final AuthenticationService authService = new AuthenticationService(users);
    private final AuthorizationService authzService = new AuthorizationService();
    private final SchoolState schoolState = new SchoolState();
    private UserInterface currentUser = null;

    private final List<String> actionHistory = new ArrayList<>();
    private final Map<String, Location> locations = initializeLocations();
    private final JournalInterface journal = initializeJournal();

    @Override
    public List<Object> getAreaList() {
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Location> entry : locations.entrySet()) {
            String locationName = entry.getKey();
            Set<UserInterface> occupants = schoolState.getUsersInLocation(entry.getValue());
            result.add(locationName + ": " + occupants);
        }
        return result;
    }

    @Override
    public List<Object> getActionHistory() {
        return new ArrayList<>(actionHistory);
    }

    @Override
    public String doAction(String action, String... arguments) {
        switch (action.toUpperCase()) {
            case "LOGIN":
                return handleLogin(arguments);
            case "E":
                return handleEnter(arguments);
            case "L":
                return handleLeave();
            case "J":
                return handleJournal();
            case "Q":
                return handleLogout();
            case "H":
                return handleHistory();
            case "X":
                return handleExit();
            default:
                logAction("Unknown command: " + action);
                return "Error";
        }
    }

    private String handleLogin(String[] args) {
        if (args.length < 2) {
            logAction("Login failed: missing credentials");
            return null;
        }
        String login = args[0];
        String password = args[1];

        User user = authService.authenticate(login, password);
        if (user != null) {
            currentUser = user;
            schoolState.setCurrentUser(user);
            logAction("Login successful: " + user.getName());
            return user.getName();
        } else {
            logAction("Login failed: invalid credentials for " + login);
            return null;
        }
    }

    private String handleEnter(String[] args) {
        if (currentUser == null) {
            logAction("Enter failed: not logged in");
            return "Error";
        }
        if (args.length < 1) {
            logAction("Enter failed: no area specified");
            return "Error";
        }

        String areaCode = args[0];
        String subArg = args.length > 1 ? args[1] : null;

        Location location = getLocationByCode(areaCode, subArg);
        if (location == null) {
            logAction("Enter failed: invalid area code");
            return "Error";
        }

        if (!location.canUserEnter(currentUser)) {
            logAction(currentUser.getName() + " denied access to " + location.getLocation());
            return "Access denied";
        }

        Permission permission = createEnterPermission(currentUser, location);
        SchoolState context = schoolState;

        if (authzService.authorize(currentUser, permission, location, context)) {
            Location prev = currentUser.getCurrentLocation();
            if (prev != null) {
                prev.userLeaves(currentUser);
                schoolState.removeUserFromLocation(currentUser, prev);
            }
            location.userEnters(currentUser);
            schoolState.addUserToLocation(currentUser, location);
            currentUser.setCurrentLocation(location);
            schoolState.setCurrentLocation(location);
            logAction(currentUser.getName() + " entered " + location.getLocation());
            return "OK";
        } else {
            logAction(currentUser.getName() + " denied access to " + location.getLocation());
            return "Access denied";
        }
    }

    private String handleLeave() {
        if (currentUser == null) {
            logAction("Leave failed: not logged in");
            return "Error";
        }
        Location currentLoc = currentUser.getCurrentLocation();
        if (currentLoc != null) {
            currentLoc.userLeaves(currentUser);
            schoolState.removeUserFromLocation(currentUser, currentLoc);
            logAction(currentUser.getName() + " left " + currentLoc.getLocation());
            currentUser.setCurrentLocation(null);
            schoolState.setCurrentLocation(null);
        }
        return "OK";
    }

    private String handleJournal() {
        if (currentUser == null) {
            logAction("Journal view failed: not logged in");
            return "Error";
        }
        Roles role = currentUser.getRole().getRole();

        switch (role) {
            case GUARD -> {
                logAction(currentUser.getName() + " denied access to journal (guard)");
                return "Access denied";
            }
            case PARENT -> {
                String childName = currentUser.getChildrenNames().isEmpty() ? null : currentUser.getChildrenNames().get(0);
                if (childName == null) {
                    logAction(currentUser.getName() + " has no child bound to parent account");
                    return "No child bound to this parent";
                }
                UserInterface child = users.values().stream()
                        .filter(u -> u.getName().equals(childName))
                        .findFirst()
                        .orElse(null);
                if (child == null) {
                    logAction("Child user not found in DB: " + childName);
                    return "Child user not found";
                }

                Permission p = new WatchJournal(List.of(new OwnChildOnlyCondition()));
                if (authzService.authorize(currentUser, p, child, schoolState)) {
                    logAction(currentUser.getName() + " viewed journal of " + child.getName());
                    return journal.getGrades(child).toString();
                }
                logAction(currentUser.getName() + " denied access to child's journal");
                return "Access denied";
            }
            case STUDENT -> {
                Location loc = currentUser.getCurrentLocation();
                Permission p = new WatchJournal(List.of(new TeacherPresentCondition()));
                if (loc != null && authzService.authorize(currentUser, p, loc, schoolState)) {
                    logAction(currentUser.getName() + " viewed journal (teacher present)");
                    return journal.toString();
                }
                logAction(currentUser.getName() + " denied access to journal (no teacher present)");
                return "Access denied";
            }
            default -> {
                Permission p = new WatchJournal(List.of());
                if (authzService.authorize(currentUser, p, journal, schoolState)) {
                    logAction(currentUser.getName() + " viewed journal");
                    return journal.toString();
                }
                logAction(currentUser.getName() + " denied access to journal");
                return "Access denied";
            }
        }
    }

    private String handleLogout() {
        if (currentUser != null) {
            logAction("Logout: " + currentUser.getName());
            currentUser = null;
            schoolState.setCurrentUser(null);
        }
        return "OK";
    }

    private String handleHistory() {
        logAction("History requested by " + (currentUser != null ? currentUser.getName() : "Anonymous"));
        return "OK";
    }

    private String handleExit() {
        logAction("Program exit");
        return "OK";
    }

    private void logAction(String message) {
        actionHistory.add(message);
    }

    private Map<String, User> loadUsers() {
        User owl = User.createUser("owl", "Сова", "🦉", "pass123", new Chief(), null);

        User piglet = User.createUser("piglet", "Пятачок", "🐷", "pass123", new Teacher(), null);
        User rabbit = User.createUser("rabbit", "Кролик", "🐰", "pass123", new Teacher(), null);
        User tigger = User.createUser("tigger", "Тигра", "🐯", "pass123", new Teacher(), null);
        User eeyore = User.createUser("eeyore", "Иа-Иа", "🐴", "pass123", new Teacher(), null);

        User pooh = User.createUser("pooh", "Винни Пух", "🐻", "pass123", new Guard(), null);
        User robin = User.createUser("robin", "Кристофер Робин", "🧑", "pass123", new Admin(), null);

        User roo = User.createUser("roo", "Крошка Ру", "🦘", "pass123", new Student(), null);
        User kanga = User.createUser("kanga", "Кенга", "🦘", "pass123", new Parent(), null, java.util.List.of("Крошка Ру"));

        Map<String, User> map = new HashMap<>();
        map.put(owl.getLogin(), owl);
        map.put(piglet.getLogin(), piglet);
        map.put(rabbit.getLogin(), rabbit);
        map.put(tigger.getLogin(), tigger);
        map.put(eeyore.getLogin(), eeyore);
        map.put(pooh.getLogin(), pooh);
        map.put(robin.getLogin(), robin);
        map.put(roo.getLogin(), roo);
        map.put(kanga.getLogin(), kanga);
        return map;
    }

    private Map<String, Location> initializeLocations() {
        Location school = new School();
        Location cabin = new DirectorCabin();
        Location room = new TeachersRoom();
        Location classA = new Classroom(1);
        Location classB = new Classroom(2);
        Location classC = new Classroom(3);
        Location classD = new Classroom(4);

        Map<String, Location> map = new HashMap<>();
        map.put("school", school);
        map.put("cabin", cabin);
        map.put("room", room);
        map.put("classA", classA);
        map.put("classB", classB);
        map.put("classC", classC);
        map.put("classD", classD);
        return map;
    }

    private JournalInterface initializeJournal() {
        SchoolJournal j = new SchoolJournal();
        UserInterface teacher = users.get("piglet");
        UserInterface roo = users.get("roo");
        if (teacher != null && roo != null) {
            j.addGrade(teacher, roo, 5);
            j.addGrade(teacher, roo, 4);
        }
        return j;
    }

    private Location getLocationByCode(String areaCode, String subArg) {
        switch (areaCode) {
            case "1": return locations.get("school");
            case "2": return locations.get("cabin");
            case "3": return locations.get("room");
            case "4":
                if (subArg != null) {
                    return locations.get("class" + subArg);
                }
            default:
                return null;
        }
    }

    private Permission createEnterPermission(UserInterface user, Location location) {
        switch (location.getLocation()) {
            case SCHOOL:
                return new EnterSchool(List.of());
            case CABIN:
                if (user.getRole().getRole() == Roles.TEACHER) {
                    return new EnterCabin(List.of(new model.condition.DirectorAbsentCondition()));
                }
                return new EnterCabin(List.of());
            case ROOM:
                return new EnterRoom(List.of());
            case CLASS:
                if (user.getRole().getRole() == Roles.STUDENT) {
                    return new EnterClass(List.of(new TeacherPresentCondition()));
                }
                return new EnterClass(List.of());
            default:
                return new EnterSchool(List.of());
        }
    }

    private Permission createWatchJournalPermission() {
        return new WatchJournal(List.of());
    }
}