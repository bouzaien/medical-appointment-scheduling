package controle;

import java.util.Date ;


import metier.Patient ;
import presentation.Patient_P ;
import presentation.Visite_P ;
import rdv.Visite ;

public class Visite_C {
   private Visite_P presentation ;
   private Visite abstraction ; // pas absolument indispensable ici, mais bonne habitude à prendre
   
   //-------------------------------------------------------------------------------------
   // constructeur : crée simplement un composant de présentaaaaaaaation prêt à l'emploi
   //-------------------------------------------------------------------------------------
   public Visite_C () {
      presentation = new Visite_P () ;
   }

   //-------------------------------------------------------------------------------------
   // mise à jour de l'abstraction : transforme les données métier en types de base à transmettre à la présentation
   // et mise à jour de la présentation avec ces données transformées
   //-------------------------------------------------------------------------------------
   public void setAbstraction (Visite visite) {
      abstraction = visite ; 
      Date date = abstraction.getDate () ;
      float price = abstraction.getPrice () ;

      String patient = new String () ;
      if (abstraction.getPatient () != null) {
         patient = abstraction.getPatient ().getLastName () + " " + abstraction.getPatient ().getFirstName () ;
      }
      String doctor = new String () ;
      if (abstraction.getDoctor () != null) {
         doctor = abstraction.getDoctor ().getLastName () + " " + abstraction.getDoctor ().getFirstName () ;
      }
      presentation.update (doctor, patient, price, date) ;
   }

   public Visite_P getPresentation () {
      return presentation ;
   }


}
