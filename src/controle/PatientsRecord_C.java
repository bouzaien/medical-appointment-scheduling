package controle ;

import java.util.ArrayList ;
import java.util.List ;

import metier.Doctor ;
import metier.Patient ;
import metier.PatientsRecord;
import presentation.PatientsRecord_P ;

//=====================================================================================
// classe de controle de la liste des patients
//=====================================================================================

public class PatientsRecord_C {

   private Patient currentPatient ;
   private Patient_C currentPatient_C ;
   private Patient_ActiveC currentPatient_ActiveC ;
   private PatientsRecord_P presentation ;
   private PatientsRecord abstraction ;
 
   public PatientsRecord_C (PatientsRecord abstraction, PatientsRecord_P presentation) {
      this.abstraction = abstraction ;
      this.presentation = presentation ;
      // dans cette version on va minimiser les créations de composans de contrôle :
      // on les crée ici et on mettra à jour leurs abstractions quand ce sera nécessaire 
      currentPatient_C = new Patient_C () ;
      currentPatient_ActiveC = new Patient_ActiveC (this) ;
      presentation.initialize (this, getPatientsNames ()) ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // méthode utilitaire pour construire une liste de String à partir d'une liste de Patients
   //-------------------------------------------------------------------------------------

   private List<String> getPatientsNames () {
      List<String> patients = new ArrayList<String> () ;
      for (Patient patient : abstraction.getPatients ()) {
         patients.add (patient.getLastName () + " " + patient.getFirstName ()) ;
      }
      return patients ;
   }

   //=====================================================================================
   // méthodes appelées par la présentation suite aux interactions de l'utilisateur
   //=====================================================================================

   //-------------------------------------------------------------------------------------
   // sélection d'un patient : on récupère l'indice du patient dans notre ArrayList de patients
   //-------------------------------------------------------------------------------------

   public void selectPatient (int selectedIndex) {
      // on note quel est le patient associé à l'index de la présentation
      currentPatient = abstraction.getPatients ().get (selectedIndex) ;
      // on met à jour la présentation en demandant l'affichage de l'aperçu du patient
      currentPatient_C.setAbstraction (currentPatient);
      presentation.selectPatient (currentPatient_C.getPresentation ()) ;
      // et en activant les moyens d'édition et de destruction du patient sélectionné
      presentation.enableActionOnSelection () ;
      // si quelqu'un est rattaché à ce patient on empêche quand même de le détruire
      if (abstraction.somebodyIsAttachedToThisPatient (currentPatient)) {
         presentation.disableDeleteOnSelection () ;
      }
   }

   //-------------------------------------------------------------------------------------
   // désélection d'un patient 
   //-------------------------------------------------------------------------------------

   public void selectNoPatient () {
      currentPatient = null ;
      // il n'y a plus de patient sélectionné, il faut faire disparaitre l'aperçu et désactiver les boutons d'actions 
      presentation.selectNoPatient () ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // suppression d'un patient dans la liste : on l'enlève dans les 2 structures pour garder la correspondance des indices
   //-------------------------------------------------------------------------------------

   public void deletePatient () {
      abstraction.deletePatient (currentPatient) ;
      presentation.initialize (this, getPatientsNames ()) ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // édition du patient courant : on va appeler un dialogue et y mettre une présentation active de patient
   //-------------------------------------------------------------------------------------

   public void editPatient () {
      currentPatient_ActiveC.setAbstraction (currentPatient) ;
      currentPatient_ActiveC.activate () ;      
   }

   //-------------------------------------------------------------------------------------
   // création d'un patient : on va appeler un dialogue et y mettre une nouvelle présentation active de patient
   //-------------------------------------------------------------------------------------

   public void createPatient () {
      Patient patient = new Patient () ;
      currentPatient_ActiveC.setAbstraction (patient) ;
      currentPatient_ActiveC.activate () ;
   }


   //-------------------------------------------------------------------------------------
   // mise à jour des caractéristiques d'un patient : c'est peut-être un nouveau patient...
   //-------------------------------------------------------------------------------------

   public void updatePatient (Patient_ActiveC patientToUpdate) {
      // si le patient est connu, on le met à jour
      if (abstraction.containsPatient (patientToUpdate.getAbstraction ())) {
         abstraction.updatePatient (patientToUpdate.getAbstraction ()) ;
      } else {
         // sinon on l'ajoute...
         abstraction.addPatient (patientToUpdate.getAbstraction ()) ;
      }
      // ensuite on réinitialise la présentation pour tenir compte des éventuelles modifications
      presentation.initialize (this, getPatientsNames ()) ;
      currentPatient_C.setAbstraction (patientToUpdate.getAbstraction ());
      presentation.selectPatient (currentPatient_C.getPresentation ()) ;
      presentation.selectPatient (patientToUpdate.toString ()) ;
   }

   //=====================================================================================
   // méthodes relai vers l'abstraction
   //=====================================================================================

   public boolean somebodyIsAttachedToThisPatient (Patient patient) {
      return (abstraction.somebodyIsAttachedToThisPatient (patient)) ;
   }
   
   public List<Patient> getPatientsToWhomThisPatientCanBeAttached (Patient patient) {
      return (abstraction.getPatientsToWhomThisPatientCanBeAttached (patient)) ;
   }
   
   public List<Patient> getPatients () {
      return abstraction.getPatients () ;
   }

   public List<Doctor> getDoctors () {
      return abstraction.getDoctors () ;
   }

   //=====================================================================================
   // méthode relai vers la présentation
   //=====================================================================================

   public PatientsRecord_P getPresentation () {
      return presentation ;
   }

}
