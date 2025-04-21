package service;

import models.Course;
import rmi.CourseRemoteService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class CourseServiceClient {
    private CourseRemoteService courseService;

    public CourseServiceClient() {
        try {
            courseService = (CourseRemoteService) Naming.lookup("rmi://localhost:1099/CourseService");
        } catch (Exception e) {
            System.err.println("Error connecting to CourseRemoteService: " + e.getMessage());
        }
    }

    public boolean createCourse(Course course) {
        try {
            courseService.create(course);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during createCourse: " + e.getMessage());
            return false;
        }
    }

    public Course getCourseById(int id) {
        try {
            return courseService.getById(id);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getCourseById: " + e.getMessage());
            return null;
        }
    }

    public List<Course> getAllCourses() {
        try {
            return courseService.getAll();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllCourses: " + e.getMessage());
            return null;
        }
    }

    public boolean updateCourse(Course course) {
        try {
            courseService.update(course);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateCourse: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        try {
            courseService.delete(id);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteCourse: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getCoursesByProfessorId(int professorId) {
        try {
            return courseService.getCoursesByProfessorId(professorId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getCoursesByProfessorId: " + e.getMessage());
            return null;
        }
    }
}