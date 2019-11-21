package controle;

import java.util.ArrayList ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Set ;

import javafx.collections.FXCollections ;
import metier.Doctor ;
import metier.Patient ;
import presentation.VisitesRecord_P ;
import rdv.Visite ;
import rdv.VisitesRecord ;

public class VisitesRecord_C {
   private Visite currentVisite ;
   private Visite_C currentVisite_C ;
   private Visite_ActiveC currentVisite_ActiveC ;
   private VisitesRecord_P presentation ;
   private VisitesRecord abstraction ;
   
   public VisitesRecord_C (VisitesRecord abstraction, VisitesRecord_P presentation) {
      this.abstraction = abstraction ;
      this.presentation = presentation ;
      // dans cette version on va minimiser les créations de composans de contrôle :
      // on les crée ici et on mettra à jour leurs abstractions quand ce sera nécessaire 
      currentVisite_C = new Visite_C () ;
      currentVisite_ActiveC = new Visite_ActiveC (this) ;
      presentation.initialize (this, getVisitesNames ()) ;
      presentation.disableActionOnSelection () ;
   }


   //=====================================================================================
   // méthodes appelées par la présentation suite aux interactions de l'utilisateur
   //=====================================================================================
   private List<String> getVisitesNames () {
      List<String> visites = new ArrayList<String> () ;

      
      for (Visite visite : abstraction.getVisites ()) {
         visites.add (visite.getDate().toString () +": " + visite.getPatient ().getFirstName () + " " + visite.getPatient ().getLastName ()) ;
      }

      
      return visites ;
   }
   //-------------------------------------------------------------------------------------
   // sélection d'une visite : on récupère l'indice du patient dans notre ArrayList de patients
   //-------------------------------------------------------------------------------------

   public void selectVisite (int selectedIndex) {
      // on note quel est la visite associé à l'index de la présentation
      currentVisite = abstraction.getVisites ().get (selectedIndex) ;
      // on met à jour la présentation en demandant l'affichage de l'aperçu du patient
      currentVisite_C.setAbstraction (currentVisite);
      presentation.selectVisite (currentVisite_C.getPresentation ()) ;
      // et en activant les moyens d'édition et de destruction du patient sélectionné
      presentation.enableActionOnSelection () ;
   }   

   //-------------------------------------------------------------------------------------
   // désélection d'un patient 
   //-------------------------------------------------------------------------------------

   public void selectNoVisite () {
      currentVisite = null ;
      // il n'y a plus de patient sélectionné, il faut faire disparaitre l'aperçu et désactiver les boutons d'actions 
      presentation.selectNoVisite () ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // suppression d'un patient dans la liste : on l'enlève dans les 2 structures pour garder la correspondance des indices
   //-------------------------------------------------------------------------------------

   public void deleteVisite () {
      abstraction.deletevisite (currentVisite) ;
      presentation.initialize (this, getVisitesNames ()) ;
      presentation.disableActionOnSelection () ;
   }

   //-------------------------------------------------------------------------------------
   // édition du patient courant : on va appeler un dialogue et y mettre une présentation active de patient
   //-------------------------------------------------------------------------------------

   public void editVisite () {
      currentVisite_ActiveC.setAbstraction (currentVisite) ;
      currentVisite_ActiveC.activate () ;      
   }

   //-------------------------------------------------------------------------------------
   // création d'un patient : on va appeler un dialogue et y mettre une nouvelle présentation active de patient
   //-------------------------------------------------------------------------------------

   public void createVisite () {
      Visite visite = new Visite () ;
      currentVisite_ActiveC.setAbstraction (visite) ;
      currentVisite_ActiveC.activate () ;
   }


   //-------------------------------------------------------------------------------------
   // mise à jour des caractéristiques d'un patient : c'est peut-être un nouveau patient...
   //-------------------------------------------------------------------------------------

   public void updateVisite (Visite_ActiveC visitToUpdate) {
      // si le patient est connu, on le met à jour
      Visite visite1=visitToUpdate.getAbstraction ();
      //visite1.setPrice (0);
     /* if (abstraction.containsVisite (visite1)) {
         abstraction.updateVisite (visitToUpdate.getAbstraction ()) ;
      } else {
         // sinon on l'ajoute...*/
         abstraction.addVisite (visitToUpdate.getAbstraction ()) ;
      
      // ensuite on réinitialise la présentation pour tenir compte des éventuelles modifications
      presentation.initialize (this, getVisitesNames ()) ;
      currentVisite_C.setAbstraction (visitToUpdate.getAbstraction ());
      presentation.selectVisite (currentVisite_C.getPresentation ()) ;
      presentation.selectVisite (visitToUpdate.toString ()) ;
   }

   //=====================================================================================
   // méthodes relai vers l'abstraction
   //=====================================================================================
   
   public List<Visite> getVisites () {
      return abstraction.getVisites () ;
   }

   public List<Doctor> getDoctors (VisitesRecord abstraction) {
      return getDoctors (abstraction) ;
   }

   //=====================================================================================
   // méthode relai vers la présentation
   //=====================================================================================

   public VisitesRecord_P getPresentation () {
      return this.presentation ;
   }

   public List<Patient> getPatients () {
      return abstraction.getPatients (currentVisite) ;
   }

   public List<Doctor> getDoctors () {
      return abstraction.getDoctors (currentVisite) ;
   }

  
}




