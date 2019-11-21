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

public class DoctorsRecord {
   
   private DoctorDAO  doctorDAO ;
   private PatientDAO patientDAO ;
   
     
   public DoctorsRecord () {
      doctorDAO = new DoctorDAO () ;
      patientDAO = new PatientDAO () ;
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
   
   public List<Patient> getPatients () {
      List<Patient> patients = new ArrayList<Patient> () ;
      try {
         patients =  patientDAO.findAll () ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
      return patients ;
   }

 
   
   public void addDoctor(Doctor DoctorToCreate) {
      try {
         doctorDAO.create (DoctorToCreate);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void updateDoctor(Doctor doctorToUpdate) {
      try {
         doctorToUpdate = DoctorDAO.update (doctorToUpdate);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void deleteDoctor (Doctor doctorToDelete) {
      try {
         doctorDAO.delete (doctorToDelete) ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
   }


  

   public boolean containsDoctor (Doctor doctor) {
      boolean contains = false ;
      try {
         contains = (doctorDAO.find (doctor.getDoctorID()) != null) ;
      } catch (MedicalRecordException e) {
      }
      return contains ;
   }


   public boolean patientHasThisDoctor (Doctor doctor) {
      boolean patientHasThisDoctor = false ;
      for (Patient patient : getPatients() ) {
         if (patient.getReferentDoctor ()!= null) {
            if (patient.getReferentDoctor ().getRpps ().equals (doctor.getRpps ()) ) {
               patientHasThisDoctor = true ;
            }
         }
      }
      return patientHasThisDoctor ;
   }
   
  


   


}
