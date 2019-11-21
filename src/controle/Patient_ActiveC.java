package controle ;

import java.util.ArrayList ;
import java.util.List ;

import metier.Doctor ;
import metier.Patient ;
import presentation.Patient_ActiveP ;

//-------------------------------------------------------------------------------------
// classe de contrôle d'un patient "actif" (éditable)
//-------------------------------------------------------------------------------------

public class Patient_ActiveC {

   private Patient_ActiveP presentation ;
   private Patient abstraction ;
   private List<Patient> authorizedAttachments ;
   private List<Doctor> doctors ;
   private PatientsRecord_C patientsRecordC ;
   
   public Patient_ActiveC (PatientsRecord_C patientsRecord) {
      presentation = new Patient_ActiveP (this, patientsRecord.getPresentation ().getStage ()) ;
      patientsRecordC = patientsRecord ;
   }
   
   public void setAbstraction (Patient patient) {
      abstraction = patient ;
      // premier rôle du contrôle : obtention des données pour la présentation 
      String socialSecurityNumber = patient.getSocialSecurityNumber () ;
      String gender = patient.getGender () ;
      String dateOfBirth = patient.getDateOfBirth () ;
      String lastName = patient.getLastName () ;
      String firstName = patient.getFirstName () ;
      String attachement = null ;
      if (patient.getAttachment () != null) {
         attachement = patient.getAttachment ().getLastName () + " " + patient.getAttachment ().getFirstName () ;
      }
      String referent = null ;
      if (patient.getReferentDoctor () != null) {
         referent = patient.getReferentDoctor ().getLastName () + " " + patient.getReferentDoctor ().getFirstName () ;
      }
      // c'est également ici que le composant joue son rôle de contrôleur en obtenant les bonnes listes de patients et docteurs à présenter à l'utilisateur
      authorizedAttachments = patientsRecordC.getPatientsToWhomThisPatientCanBeAttached (patient) ;
      doctors = patientsRecordC.getDoctors ();
      // et en les traduisant ensuite en listes de String à afficher avant de mettre à jour sa présentation
      presentation.update (socialSecurityNumber, gender, dateOfBirth, lastName, firstName, attachement, referent, getPatientsNames (authorizedAttachments), getDoctorsNames (doctors)) ;
   }
   
   public void activate () {
      presentation.show () ;
   }

   public Patient getAbstraction () {
      return abstraction ;
   }

   public Patient_ActiveP getPresentation () {
      return presentation ;
   }

   //-------------------------------------------------------------------------------------
   // traduction des listes de patients et de docteurs en listes de String à afficher
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
      // il faut récupérer toutes les informations éditées dans la préentation et les transférer vers l'abstraction 
      abstraction.setLastName (presentation.getLastName ()) ;
      abstraction.setFirstName (presentation.getFirstName ()) ;
      int referent = presentation.getReferent () ;
      if (referent < doctors.size ()) {
         abstraction.setReferentDoctor (doctors.get (referent)) ;
      } else {
         abstraction.setReferentDoctor (null) ;
      }
      int attachment = presentation.getAttachment () ;
      if (attachment < authorizedAttachments.size ()) {
         abstraction.setAttachment (authorizedAttachments.get (attachment)) ;
      } else {
         abstraction.setAttachment (null) ;
      }
      abstraction.setDateOfBirth (presentation.getDateOfBirth ()) ;
      abstraction.setGender (presentation.getGender ()) ;
      abstraction.setSocialSecurityNumber (presentation.getSSN ()) ;
      // on doit ensuite prévenir le gestionnaire de patients que l'édition est terminée avec succès
      patientsRecordC.updatePatient (this) ;
   }

   @Override
   public String toString () {
      return (abstraction.getLastName () + " " + abstraction.getFirstName ()) ;
   }

}
