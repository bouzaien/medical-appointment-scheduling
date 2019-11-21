package metier;

import java.util.ArrayList;
import java.util.List;

import dao.DoctorDAO;
import dao.MedicalRecordException;
import dao.PatientDAO;

//=====================================================================================
// classe de gestion des patients et des docteurs :
// elle pourrait être paramétrée par des éléments permettant de récupérer un stockage
//=====================================================================================

public class PatientsRecord {
   
   private DoctorDAO  doctorDAO ;
   private PatientDAO patientDAO ;
     
   public PatientsRecord () {
      doctorDAO = new DoctorDAO () ;
      patientDAO = new PatientDAO () ;
   }

   public List<Patient> getPatients () {
      List<Patient> patients = new ArrayList<Patient> () ;
      try {
         patients =  patientDAO.findAll () ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
      return patients ;
   }

   public List<Doctor> getDoctors () {
      List<Doctor> doctors = new ArrayList<Doctor> () ;
      try {
         doctors =  doctorDAO.findAll () ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
      return doctors ;
   }

   public boolean containsPatient (Patient patient) {
      boolean contains = false ;
      try {
         contains = (patientDAO.find (patient.getPatientID()) != null) ;
      } catch (MedicalRecordException e) {
      }
      return contains ;
   }
   
   public void addPatient (Patient patientToCreate) {
      try {
         patientDAO.create (patientToCreate);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void updatePatient(Patient patientToUpdate) {
      try {
         patientToUpdate = patientDAO.update (patientToUpdate);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void deletePatient (Patient patientToDelete) {
      try {
         
         patientDAO.delete (patientToDelete) ;
         
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
   }


   public boolean somebodyIsAttachedToThisPatient (Patient currentPatient) {
      boolean somebodyIsAttached = false ;
      for (Patient patient : getPatients ()) {
         if (patient.getAttachment () != null) {
            if (currentPatient.getSocialSecurityNumber ().equals (patient.getAttachment ().getSocialSecurityNumber ())) {
               somebodyIsAttached = true ;
            }
         }
      }
      return somebodyIsAttached ;
   }

   public List<Patient> getPatientsToWhomThisPatientCanBeAttached (Patient patient) {
      List<Patient> authorized = new ArrayList <Patient> () ;
      // un patient qui a des personnes rattachées à lui ne peut pas avoir d'attachement : on recherche donc si quelqu'un est déjà attaché à notre patient
      if (! somebodyIsAttachedToThisPatient (patient)) {
         // si ce n'est pas le cas, on va chercher à qui on peut le ratatcher : les personnes pas déjà rattachées à quelqu'un d'autre
         for (Patient a : getPatients ()) {
            // un patient ne peut pas être rattaché à lui-même
            if (! a.getSocialSecurityNumber ().equals(patient.getSocialSecurityNumber ())) {
               // un patient ne peut pas non plus être rattaché à un patient lui-même déjà rattaché à quelqu'un
               if (a.getAttachment () == null) {
                  authorized.add (a) ;
               }
            }
         }
      }
      return (authorized) ;
   }

}
