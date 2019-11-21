package rdv;

import java.util.ArrayList ;
import java.util.List ;

import dao.DoctorDAO ;
import dao.MedicalRecordException ;
import dao.PatientDAO ;
import dao.VisiteDAO ;
import metier.Doctor ;
import metier.Patient ;

public class VisitesRecord { 
 //=====================================================================================
// classe de gestion des visites :
// elle pourrait être paramétrée par des éléments permettant de récupérer un stockage
//=====================================================================================

   private VisiteDAO  visiteDAO ;
     
   public VisitesRecord () {
      visiteDAO = new VisiteDAO () ;
   }

   public List<Visite> getVisites () {
      List<Visite> visites = new ArrayList<Visite> () ;
      try {
         visites =  visiteDAO.findAll () ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
      return visites ;
   }

   
     
   public boolean containsVisite (Visite visite) {
      boolean contains = false ;
      try {
         contains = (VisiteDAO.find (visite.getDate (),visite.getPatient (),visite.getDoctor ())!=null) ;
         System.out.println (contains) ;
         
      } catch (MedicalRecordException e) {
      }
      
      return contains ;
   }
   
   public void addVisite (Visite visite) {
      try {
         System.out.println ("yo") ;
         visiteDAO.create (visite);
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void updateVisite (Visite visite) {
      try {
         visite = visiteDAO.update (visite);
         
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }     
   }

   public void deletevisite (Visite visite) {
      try {
         visiteDAO.delete (visite) ;
      } catch (MedicalRecordException e) {
         e.printStackTrace();
      }
   }

   public List<Doctor> getDoctors (Visite visite)  {
         List<Doctor> doctors = new ArrayList <Doctor> () ;
          for (Visite a : getVisites ()) {
             doctors.add (a.getDoctor ()) ;
                  }return doctors;
               }
   
   public List<Patient> getPatients (Visite visite)  {
      List<Patient> patients = new ArrayList <Patient> () ;
       for (Visite a : getVisites ()) {
          patients.add (a.getPatient ()) ;
               }return patients;
            
   }
}
   

   


