package controle ;

import java.util.ArrayList ;
import java.util.List ;

import metier.Doctor ;
import metier.DoctorsRecord ;
import metier.Patient ;
import metier.PatientsRecord;
import presentation.DoctorsRecord_P ;
import presentation.PatientsRecord_P ;

//=====================================================================================
// classe de controle de la liste des docteurs
//=====================================================================================

public class DoctorRecord_C {

   private Doctor currentDoctor ;
   private Doctor_C currentDoctor_C ;
   private Doctor_ActiveC currentDoctor_ActiveC ;
   private DoctorsRecord_P presentation ;
   private DoctorsRecord abstraction ;
 
   public DoctorRecord_C (DoctorsRecord abstraction, DoctorsRecord_P presentation) {
      this.abstraction = abstraction ;
      this.presentation = presentation ;
      // dans cette version on va minimiser les créations de composans de contrôle :
      // on les crée ici et on mettra à jour leurs abstractions quand ce sera nécessaire 
      currentDoctor_C = new Doctor_C () ;
      currentDoctor_ActiveC = new Doctor_ActiveC (this) ;
      presentation.initialize (this, getDoctorsNames ()) ;
      presentation.disableActionOnSelection () ;
   }
   
  

   //-------------------------------------------------------------------------------------
   // méthode utilitaire pour construire une liste de String à partir d'une liste de Docteurs
   //-------------------------------------------------------------------------------------

   private List<String> getDoctorsNames () {
      List<String> doctors = new ArrayList<String> () ;
      for (Doctor doctor : abstraction.getDoctors ()) {
         doctors.add (doctor.getLastName () + " " + doctor.getFirstName ()) ;
      }
      return doctors ;
   }

   //=====================================================================================
   // méthodes appelées par la présentation suite aux interactions de l'utilisateur
   //=====================================================================================

   //-------------------------------------------------------------------------------------
   // sélection d'un patient : on récupère l'indice du patient dans notre ArrayList de patients
   //-------------------------------------------------------------------------------------

   public void selectDoctor (int selectedIndex) {
      // on note quel est le patient associé à l'index de la présentation
      currentDoctor = abstraction.getDoctors ().get (selectedIndex) ;
      // on met à jour la présentation en demandant l'affichage de l'aperçu du patient
      currentDoctor_C.setAbstraction (currentDoctor);
      presentation.selectDoctor (currentDoctor_C.getPresentation ()) ;
      // et en activant les moyens d'édition et de destruction du patient sélectionné
      presentation.enableActionOnSelection () ;
      // si quelqu'un est rattaché à ce patient on empêche quand même de le détruire
      if (abstraction.patientHasThisDoctor (currentDoctor)) {
         presentation.disableDeleteOnSelection () ;
      }
   }

   //-------------------------------------------------------------------------------------
   // désélection d'un patient 
   //-------------------------------------------------------------------------------------

   public void selectNoDoctor () {
      currentDoctor = null ;
      // il n'y a plus de docteur sélectionné, il faut faire disparaitre l'aperçu et désactiver les boutons d'actions 
      presentation.selectNoDoctor () ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // suppression d'un docteur dans la liste : on l'enlève dans les 2 structures pour garder la correspondance des indices
   //-------------------------------------------------------------------------------------

   public void deleteDoctor () {
      abstraction.deleteDoctor (currentDoctor) ;
      presentation.initialize (this, getDoctorsNames ()) ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // édition du patient courant : on va appeler un dialogue et y mettre une présentation active de patient
   //-------------------------------------------------------------------------------------

   public void editDoctor () {
      currentDoctor_ActiveC.setAbstraction (currentDoctor) ;
      currentDoctor_ActiveC.activate () ;      
   }

   //-------------------------------------------------------------------------------------
   // création d'un patient : on va appeler un dialogue et y mettre une nouvelle présentation active de patient
   //-------------------------------------------------------------------------------------

   public void createDoctor () {
      Doctor Doctor = new Doctor () ;
      currentDoctor_ActiveC.setAbstraction (Doctor) ;
      currentDoctor_ActiveC.activate () ;
   }


   //-------------------------------------------------------------------------------------
   // mise à jour des caractéristiques d'un patient : c'est peut-être un nouveau patient...
   //-------------------------------------------------------------------------------------

   public void updateDoctor (Doctor_ActiveC DoctorToUpdate) {
      // si le patient est connu, on le met à jour
      if (abstraction.containsDoctor (DoctorToUpdate.getAbstraction ())) {
         abstraction.updateDoctor (DoctorToUpdate.getAbstraction ()) ;
      } else {
         // sinon on l'ajoute...
         abstraction.addDoctor (DoctorToUpdate.getAbstraction ()) ;
      }
      // ensuite on réinitialise la présentation pour tenir compte des éventuelles modifications
      presentation.initialize (this, getDoctorsNames ()) ;
      currentDoctor_C.setAbstraction (DoctorToUpdate.getAbstraction ());
      presentation.selectDoctor (currentDoctor_C.getPresentation ()) ;
      presentation.selectDoctor (DoctorToUpdate.toString ()) ;
   }

   //=====================================================================================
   // méthodes relai vers l'abstraction
   //=====================================================================================

   
   public List<Doctor> getDoctors () {
      return abstraction.getDoctors () ;
   }

   public boolean patientHasThisDoctor ( Doctor doctor) {
      return ( abstraction.patientHasThisDoctor(doctor));
      
   }

   //=====================================================================================
   // méthode relai vers la présentation
   //=====================================================================================

   public DoctorsRecord_P getPresentation () {
      return presentation ;
   }
   
   

}