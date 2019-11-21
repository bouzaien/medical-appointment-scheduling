package controle;

import java.util.Date ;


import presentation.Prescription_P ;
import rdv.Prescription ;
import rdv.Visite ;

public class Prescription_C {
   private Prescription_P presentation ;
   private Prescription abstraction ; // pas absolument indispensable ici, mais bonne habitude à prendre
   
   //-------------------------------------------------------------------------------------
   // constructeur : crée simplement un composant de présentaaaaaaaation prêt à l'emploi
   //-------------------------------------------------------------------------------------
   public Prescription_C () {
      presentation = new Prescription_P () ;
   }

   //-------------------------------------------------------------------------------------
   // mise à jour de l'abstraction : transforme les données métier en types de base à transmettre à la présentation
   // et mise à jour de la présentation avec ces données transformées
   //-------------------------------------------------------------------------------------
   public void setAbstraction (Prescription prescription) {
      abstraction = prescription ; 
      String medicament = abstraction.getMedicament () ;
      Visite visite = abstraction.getVisite ();
      Date date = abstraction.getDate () ;
      int duree = abstraction.getDuree ();
      float posologie =abstraction.getPosologie () ;
      String modalites =abstraction.getModalites ();
      
      presentation.update (medicament, visite, date, duree, posologie, modalites) ;
   }

   public Prescription_P getPresentation () {
      return presentation ;
   }
}
