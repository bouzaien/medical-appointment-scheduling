package controle ;

import metier.Doctor ;
import presentation.Doctor_P ;


//-------------------------------------------------------------------------------------
// classe de controle d'un patient : elle fait le lien entre le métier et la présentation
//-------------------------------------------------------------------------------------

public class Doctor_C {
   
   private Doctor_P presentation ;
   private Doctor abstraction ; // pas absolument indispensable ici, mais bonne habitude à prendre
   
   //-------------------------------------------------------------------------------------
   // constructeur : crée simplement un composant de présentaaaaaaaation prêt à l'emploi
   //-------------------------------------------------------------------------------------
   public Doctor_C () {
      presentation = new Doctor_P () ;
   }

   //-------------------------------------------------------------------------------------
   // mise à jour de l'abstraction : transforme les données métier en types de base à transmettre à la présentation
   // et mise à jour de la présentation avec ces données transformées
   //-------------------------------------------------------------------------------------
   public void setAbstraction (Doctor doctor) {
      abstraction = doctor ; 
      String lastName = abstraction.getLastName () ;
      String firstName = abstraction.getFirstName () ;
      String speciality = abstraction.getSpeciality () ;
      String rpps = abstraction.getRpps () ;
      String tel = abstraction.getPhoneNumber () ;
      String address = abstraction.getAddress ();
      
      presentation.update (lastName, firstName, speciality, rpps, tel , address) ;
   }

   public Doctor_P getPresentation () {
      return presentation ;
   }

}