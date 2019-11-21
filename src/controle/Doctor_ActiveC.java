package controle ;

import java.util.ArrayList ;
import java.util.List ;

import metier.Doctor ;
import metier.Patient ;
import presentation.Doctor_ActiveP ;

//-------------------------------------------------------------------------------------
// classe de contrôle d'un doctor "actif" (éditable)
//-------------------------------------------------------------------------------------

public class Doctor_ActiveC {

   private Doctor_ActiveP presentation ;
   private Doctor abstraction ;
   private DoctorRecord_C doctorsRecordC ;
   
   public Doctor_ActiveC (DoctorRecord_C doctorsRecord) {
      presentation = new Doctor_ActiveP (this, doctorsRecord.getPresentation ().getStage ()) ;
      doctorsRecordC = doctorsRecord ;
   }
   
   public void setAbstraction (Doctor doctor) {
      abstraction = doctor ;
      // premier rôle du contrôle : obtention des données pour la présentation 
   
      String lastName = doctor.getLastName () ;
      String firstName = doctor.getFirstName () ;
      String speciality = doctor.getSpeciality () ;
      String tel = doctor.getPhoneNumber () ;
      String rpps = doctor.getRpps () ;
      String address = doctor.getAddress () ;
      
      // et en les traduisant ensuite en listes de String à afficher avant de mettre à jour sa présentation
      presentation.update (lastName, firstName, speciality, rpps, tel, address) ;
   }
   
   public void activate () {
      presentation.show () ;
   }

   public Doctor getAbstraction () {
      return abstraction ;
   }

   public Doctor_ActiveP getPresentation () {
      return presentation ;
   }

   //-------------------------------------------------------------------------------------
   // traduction des listes de doctors et de docteurs en listes de String à afficher
   //-------------------------------------------------------------------------------------
   private List<String> getPatientsNames (List<Patient> authorizedPatients) {
      List<String> names = new ArrayList<String> () ;
      for (Patient patient : authorizedPatients) {
         names.add (patient.getLastName () + " " + patient.getFirstName ()) ;
      }
      return names ;
   }

   //-------------------------------------------------------------------------------------
   
   private List<String> getDoctorsNames (List<Doctor> doctors) {
      List<String> names = new ArrayList<String> () ;
      for (Doctor doctor : doctors) {
         names.add (doctor.getLastName () + " " + doctor.getFirstName ()) ;
      }
      return names ;
   }

   //-------------------------------------------------------------------------------------
   // méthode d'appel en provenance de la présentation pour signifier qu'une édition a été réalisée
   //-------------------------------------------------------------------------------------

   public void editionComplete () {
      // il faut récupérer toutes les informations éditées dans la présentation et les transférer vers l'abstraction 
      abstraction.setLastName (presentation.getLastName ()) ;
      abstraction.setFirstName (presentation.getFirstName ()) ;
      abstraction.setSpeciality (presentation.getSpeciality ()) ;
      abstraction.setRpps (presentation.getRpps ()) ;
      abstraction.setPhoneNumber (presentation.getPhoneNumber ()) ;
      abstraction.setAddress (presentation.getAddress ()) ;
      // on doit ensuite prévenir le gestionnaire de doctors que l'édition est terminée avec succès
      doctorsRecordC.updateDoctor (this) ;
   }

   @Override
   public String toString () {
      return (abstraction.getLastName () + " " + abstraction.getFirstName ()) ;
   }

}
