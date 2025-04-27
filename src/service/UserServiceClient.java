package service;

import java.rmi.Naming;
import java.rmi.RemoteException;

import models.User;
import rmi.UserRemoteService;

public class UserServiceClient {
    private UserRemoteService userService;

    public UserServiceClient() {
        try {
            userService = (UserRemoteService) Naming.lookup("rmi://localhost:1099/UserService");
        } catch (Exception e) {
            System.err.println("Error connecting to UserRemoteService: " + e.getMessage());
        }
    }

    public boolean register(String name, String email, String password, String role) {
        try {
            userService.register(name, email, password, role);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during register: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error during register: " + e.getMessage());
            return false;
        }
    }


    public User login(String email, String password) {
        try {
            return userService.login(email, password);
        } catch (RemoteException e) {
            System.err.println("RemoteException during login: " + e.getMessage());
            return null;
        }
    }

    public User getUserById(int id) {
        try {
            return userService.getUserById(id);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getUserById: " + e.getMessage());
            return null;
        }
    }

    public User getStudentByStudentTestId(int studentTestId) {
        try {
            return userService.getStudentByStudentTestId(studentTestId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getStudentByStudentTestId: " + e.getMessage());
            return null;
        }
    }
}
