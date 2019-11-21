package controle;

import java.util.ArrayList ;
import java.util.List ;

import dao.MedicalRecordException ;
import metier.Doctor ;
import metier.Patient ;
import presentation.PrescriptionsRecord_P ;
import rdv.Prescription ;

import rdv.PrescriptionsRecord ;

public class PrescriptionsRecord_C {
   private Prescription currentPrescription ;
   private Prescription_C currentPrescription_C ;
   private Prescription_ActiveC currentPrescription_ActiveC ;
   private PrescriptionsRecord_P presentation ;
   private PrescriptionsRecord abstraction ;

   public PrescriptionsRecord_C (PrescriptionsRecord abstraction, PrescriptionsRecord_P presentation) {
      this.abstraction = abstraction ;
      this.presentation = presentation ;
      // dans cette version on va minimiser les créations de composans de contrôle :
      // on les crée ici et on mettra à jour leurs abstractions quand ce sera nécessaire 
      currentPrescription_C = new Prescription_C () ;
      currentPrescription_ActiveC = new Prescription_ActiveC (this) ;
      presentation.initialize (this, getPrescriptionNames ()) ;
      presentation.disableActionOnSelection () ;
   }


   //=====================================================================================
   // méthodes appelées par la présentation suite aux interactions de l'utilisateur
   //=====================================================================================
   private List<String> getPrescriptionNames () {
      List<String> Prescriptions = new ArrayList<String> () ;
      for (Prescription prescription : abstraction.getPrescriptions ()) {
         Prescriptions.add (prescription.getDate ()+" - "+prescription.getVisite ().getPatient ().getFirstName ()+" "+prescription.getVisite ().getPatient ().getLastName ()+": "+prescription.getMedicament () ) ;
      }
      return Prescriptions ;
   }
   //-------------------------------------------------------------------------------------
   // sélection d'un patient : on récupère l'indice du patient dans notre ArrayList de patients
   //-------------------------------------------------------------------------------------

   public void selectPrescription (int selectedIndex) {
      // on note quel est le patient associé à l'index de la présentation
      currentPrescription = abstraction.getPrescriptions ().get (selectedIndex) ;
      // on met à jour la présentation en demandant l'affichage de l'aperçu du patient
      currentPrescription_C.setAbstraction (currentPrescription);
      presentation.selectPrescription (currentPrescription_C.getPresentation ()) ;
      // et en activant les moyens d'édition et de destruction du patient sélectionné
      presentation.enableActionOnSelection () ;
   }   

   //-------------------------------------------------------------------------------------
   // désélection d'un patient 
   //-------------------------------------------------------------------------------------

   public void selectNoPrescription () {
      currentPrescription = null ;
      // il n'y a plus de patient sélectionné, il faut faire disparaitre l'aperçu et désactiver les boutons d'actions 
      presentation.selectNoPrescription () ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // suppression d'un patient dans la liste : on l'enlève dans les 2 structures pour garder la correspondance des indices
   //-------------------------------------------------------------------------------------

   public void deletePrescription () {
      abstraction.deleteprescription (currentPrescription) ;
      presentation.initialize (this, getPrescriptionNames ()) ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // édition du patient courant : on va appeler un dialogue et y mettre une présentation active de patient
   //-------------------------------------------------------------------------------------

   public void editPrescription () throws MedicalRecordException  {
      currentPrescription_ActiveC.setAbstraction (currentPrescription) ;
      currentPrescription_ActiveC.activate () ;      
   }

   //-------------------------------------------------------------------------------------
   // création d'un patient : on va appeler un dialogue et y mettre une nouvelle présentation active de patient
   //-------------------------------------------------------------------------------------

   public void createPrescription () throws MedicalRecordException {
      Prescription patient = new Prescription () ;
      currentPrescription_ActiveC.setAbstraction (patient) ;
      currentPrescription_ActiveC.activate () ;
   }


   //-------------------------------------------------------------------------------------
   // mise à jour des caractéristiques d'un patient : c'est peut-être un nouveau patient...
   //-------------------------------------------------------------------------------------

   public void updatePrescription (Prescription_ActiveC prescriptionToUpdate) {
      // si le patient est connu, on le met à jour
      if (abstraction.containsPrescription (prescriptionToUpdate.getAbstraction ())) {
         abstraction.updatePrescription (prescriptionToUpdate.getAbstraction ()) ;
      } else {
         // sinon on l'ajoute...
         abstraction.addPrescription (prescriptionToUpdate.getAbstraction ()) ;
      }
      // ensuite on réinitialise la présentation pour tenir compte des éventuelles modifications
      presentation.initialize (this, getPrescriptionNames ()) ;
      currentPrescription_C.setAbstraction (prescriptionToUpdate.getAbstraction ());
      presentation.selectPrescription (currentPrescription_C.getPresentation ()) ;
      presentation.selectPrescription (prescriptionToUpdate.toString ()) ;
   }

   //=====================================================================================
   // méthodes relai vers l'abstraction
   //=====================================================================================

   public List<Prescription> getPrescription () {
      return abstraction.getPrescriptions () ;
   }



   //=====================================================================================
   // méthode relai vers la présentation
   //=====================================================================================

   public PrescriptionsRecord_P getPresentation () {
      return this.presentation ;
   }


   public List<Patient> getPatients () { return abstraction.getPatients
         (currentPrescription) ; }

   public List<Doctor> getDoctors () { return abstraction.getDoctors
         (currentPrescription) ; }



}




