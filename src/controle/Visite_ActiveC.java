package controle;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.List ;

import metier.Doctor ;
import metier.Patient ;
import presentation.Patient_ActiveP ;
import presentation.Visite_ActiveP ;
import rdv.Visite ;

public class Visite_ActiveC {
   private Visite_ActiveP presentation ;
   private Visite abstraction ;
   private List<Patient> patients ;
   private List<Doctor> doctors ;
   private VisitesRecord_C visitesRecordC ;

   public Visite_ActiveC (VisitesRecord_C visitesRecord) {
      presentation = new Visite_ActiveP (this, visitesRecord.getPresentation ().getStage ()) ;
      visitesRecordC = visitesRecord ;
   }
   
   public void setAbstraction (Visite visite) {
      abstraction = visite ;
      // premier rôle du contrôle : obtention des données pour la présentation 
      Date date = visite.getDate () ;
      String patient = null ;
      float price = visite.getPrice ();
      if (visite.getPatient () != null) {
         patient = visite.getPatient ().getLastName () + " " + visite.getPatient ().getFirstName () ;
      }
      String doctor = null;
      if (visite.getDoctor () != null) {
         doctor = visite.getDoctor ().getLastName () + " " + visite.getDoctor ().getFirstName () ;
      }
      // c'est également ici que le composant joue son rôle de contrôleur en obtenant les bonnes listes de patients et docteurs à présenter à l'utilisateur
      patients = visitesRecordC.getPatients () ;
      doctors = visitesRecordC.getDoctors ();
      // et en les traduisant ensuite en listes de String à afficher avant de mettre à jour sa présentation
      List<String> patientsString = new ArrayList<String> () ;
      for (Patient a : patients) {
         patientsString.add (a.getLastName () + " " + a.getFirstName ()) ;
      }
      List<String> doctorsString = new ArrayList<String> () ;
      for (Doctor b : doctors) {
         doctorsString.add (b.getLastName () + " " + b.getFirstName ()) ;
      }
      presentation.update (date, patient,price, doctor, patientsString , doctorsString ) ;
   }
   
   public void activate () {
      presentation.show () ;
   }

   public Visite getAbstraction () {
      return abstraction ;
   }

   public Visite_ActiveP getPresentation () {
      return presentation ;
   }

  

   //-------------------------------------------------------------------------------------
   // méthode d'appel en provenance de la présentation pour signifier qu'une édition a été réalisée
   //-------------------------------------------------------------------------------------

   public void editionComplete () {
      // il faut récupérer toutes les informations éditées dans la préentation et les transférer vers l'abstraction 
      int doctor = presentation.getDoctor () ;
      if (doctor < doctors.size ()) {
         abstraction.setDoctor (doctors.get (doctor)) ;
      } else {
         abstraction.setDoctor (null) ;
      }
      int patient = presentation.getPatient () ;
      if (patient < patients.size ()) {
         abstraction.setPatient (patients.get (patient)) ;
      } else {
         abstraction.setPatient (null) ;
      }
      abstraction.setDate (presentation.getDate ()) ;
      
      abstraction.setPrice (Float.parseFloat (presentation.getPrice ())) ;
      
      // on doit ensuite prévenir le gestionnaire de patients que l'édition est terminée avec succès
      visitesRecordC.updateVisite (this) ;
   }


  


   
   
}
