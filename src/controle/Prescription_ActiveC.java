package controle ;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.List ;

import dao.MedicalRecordException ;
import dao.VisiteDAO ;
import metier.Doctor ;
import metier.Patient ;
import rdv.Prescription ;
import rdv.Visite ;
import presentation.Prescription_ActiveP ;

//-------------------------------------------------------------------------------------
// classe de contrôle d'un Prescription "actif" (éditable)
//-------------------------------------------------------------------------------------

public class Prescription_ActiveC {

   private Prescription_ActiveP presentation ;
   private Prescription abstraction ;
   private List<Patient> patients ;
   private List<Doctor> doctors ;
   private PrescriptionsRecord_C PrescriptionsRecordC ;

   public Prescription_ActiveC (PrescriptionsRecord_C PrescriptionsRecord) {
      presentation = new Prescription_ActiveP (this, PrescriptionsRecord.getPresentation ().getStage ()) ;
      PrescriptionsRecordC = PrescriptionsRecord ;
   }

   public void setAbstraction (Prescription prescription) throws MedicalRecordException {
      abstraction = prescription ;
      // premier rôle du contrôle : obtention des données pour la présentation 
      String medicament = prescription.getMedicament () ;
      Date date = prescription.getDate () ;
      int duree = prescription.getDuree () ;
      float posologie = prescription.getPosologie () ;
      String modalites = prescription.getModalites () ;
      Patient patient = null ;
      if (prescription.getVisite ().getPatient () != null) {
         patient = prescription.getVisite ().getPatient () ;
      }
      Doctor medecin = null ;
      if (prescription.getVisite () != null) {
         medecin = prescription.getVisite ().getDoctor () ;
      }
      Visite visite = VisiteDAO.find (date, patient, medecin);

      patients = PrescriptionsRecordC.getPatients ();
      List<String> patientsNames=new ArrayList<String>();
      for(Patient k:patients) {
         patientsNames.add (k.getFirstName ()+ " "+ k.getLastName ());
      }
         
      
      doctors = PrescriptionsRecordC.getDoctors ();
      List<String> doctorsNames=new ArrayList<String>();
      for(Doctor k:doctors) {
         doctorsNames.add (k.getFirstName ()+ " "+ k.getLastName ());
      }
      

      // c'est également ici que le composant joue son rôle de contrôleur en obtenant les bonnes listes de Prescriptions et docteurs à présenter à l'utilisateur

      presentation.update (medicament, visite, date, duree, posologie, modalites,patientsNames, doctorsNames ) ;
   }

   public void activate () {
      presentation.show () ;
   }

   public Prescription getAbstraction () {
      return abstraction ;
   }

   public Prescription_ActiveP getPresentation () {
      return presentation ;
   }

   //-------------------------------------------------------------------------------------
   // traduction des listes de Prescriptions et de docteurs en listes de String à afficher
   //-------------------------------------------------------------------------------------
   /*
    * private List<String> getPrescriptionsPatients (List<Prescription>
    * authorizedPrescriptions) { List<String> names = new ArrayList<String> () ;
    * for (Prescription Prescription : authorizedPrescriptions) { names.addAll
    * (Prescription.getVisite ().getPatient ()) ; } return names ; }
    */

   //-------------------------------------------------------------------------------------



   //-------------------------------------------------------------------------------------
   // méthode d'appel en provenance de la présentation pour signifier qu'une édition a été réalisée
   //-------------------------------------------------------------------------------------

   public void editionComplete () throws MedicalRecordException {
      // il faut récupérer toutes les informations éditées dans la préentation et les transférer vers l'abstraction 
      abstraction.setMedicament(presentation.getMedicament ()) ;
      abstraction.setModalites (presentation.getModalites ()) ;
      abstraction.setDate (presentation.getDate ()) ;
      abstraction.setPosologie (Float.parseFloat(presentation.getposologie ())) ;
      abstraction.setDuree (Integer.parseInt(presentation.getDuree()) );
      abstraction.setVisite (presentation.getVisite ());
      // on doit ensuite prévenir le gestionnaire de Prescriptions que l'édition est terminée avec succès
      PrescriptionsRecordC.updatePrescription (this) ;
   }

   @Override
   public String toString () {
      return (abstraction.getVisite().getDoctor().getFirstName () + " " + abstraction.getVisite ().getPatient ().getFirstName ()) ;
   }

}
