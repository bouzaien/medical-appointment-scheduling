package rdv;

import java.util.ArrayList ;
import java.util.List ;

import dao.MedicalRecordException ;
import dao.PrescriptionDAO ;
import metier.Doctor ;
import metier.Patient ;


public class PrescriptionsRecord {
   private PrescriptionDAO  prescriptionDAO ;

   public PrescriptionsRecord () {
      prescriptionDAO = new PrescriptionDAO () ;
   }

   public List<Prescription> getPrescriptions () {
      List<Prescription> prescriptions = new ArrayList<Prescription> () ;
      try {
         prescriptions =  prescriptionDAO.findAll () ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
      return prescriptions ;
   }



   public boolean containsPrescription (Prescription prescription) {
      boolean contains = false ;
      try {
         contains = (PrescriptionDAO.find (prescription.getPrescriptionID ())!=null) ;
      } catch (MedicalRecordException e) {
      }
      return contains ;
   }

   public void addPrescription (Prescription prescription) {
      try {
         prescriptionDAO.create (prescription);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void updatePrescription (Prescription prescription) {
      try {
         prescription = prescriptionDAO.update (prescription);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void deleteprescription (Prescription prescription) {
      try {
         prescriptionDAO.delete (prescription) ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
   }

   public List<Patient> getPatients (Prescription prescription) { List<Patient>
   patients = new ArrayList <Patient> () ; for (Prescription a : getPrescriptions
         ()) { patients.add (a.getVisite ().getPatient ()) ; }return patients; }

   public List<Doctor> getDoctors (Prescription prescription) { List<Doctor>
   doctors = new ArrayList <Doctor> () ; for (Prescription a : getPrescriptions
         ()) { doctors.add (a.getVisite ().getDoctor ()) ; }return doctors; }


}
