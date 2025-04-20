package service;

import models.Certificate;
import rmi.CertificateRemoteService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class CertificateServiceClient {
    private CertificateRemoteService certificateService;

    public CertificateServiceClient() {
        try {
            certificateService = (CertificateRemoteService) Naming.lookup("rmi://localhost:1099/CertificateService");
        } catch (Exception e) {
            System.err.println("Error connecting to CertificateRemoteService: " + e.getMessage());
        }
    }

    public boolean issueCertificate(int studentId, int testId, double score) {
        try {
            certificateService.issueCertificate(studentId, testId, score);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during issueCertificate: " + e.getMessage());
            return false;
        }
    }

    public Certificate getCertificateById(int id) {
        try {
            return certificateService.getCertificateById(id);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getCertificateById: " + e.getMessage());
            return null;
        }
    }

    public List<Certificate> getCertificatesByStudentId(int studentId) {
        try {
            return certificateService.getCertificatesByStudentId(studentId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getCertificatesByStudentId: " + e.getMessage());
            return null;
        }
    }

    public boolean invalidateCertificate(int certificateId) {
        try {
            certificateService.invalidateCertificate(certificateId);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during invalidateCertificate: " + e.getMessage());
            return false;
        }
    }
}
