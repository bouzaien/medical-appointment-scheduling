package controle ;

import metier.Patient ;
import presentation.Patient_P ;

//-------------------------------------------------------------------------------------
// classe de controle d'un patient : elle fait le lien entre le métier et la présentation
//-------------------------------------------------------------------------------------

public class Patient_C {
   
   private Patient_P presentation ;
   private Patient abstraction ; // pas absolument indispensable ici, mais bonne habitude à prendre
   
   //-------------------------------------------------------------------------------------
   // constructeur : crée simplement un composant de présentaaaaaaaation prêt à l'emploi
   //-------------------------------------------------------------------------------------
   public Patient_C () {
      presentation = new Patient_P () ;
   }

   //-------------------------------------------------------------------------------------
   // mise à jour de l'abstraction : transforme les données métier en types de base à transmettre à la présentation
   // et mise à jour de la présentation avec ces données transformées
   //-------------------------------------------------------------------------------------
   public void setAbstraction (Patient patient) {
      abstraction = patient ; 
      String socialSecurityNumber = abstraction.getSocialSecurityNumber () ;
      String gender = abstraction.getGender () ;
      String dateOfBirth = abstraction.getDateOfBirth () ;
      String lastName = abstraction.getLastName () ;
      String firstName = abstraction.getFirstName () ;
      String attachement = new String () ;
      if (abstraction.getAttachment () != null) {
         attachement = abstraction.getAttachment ().getLastName () + " " + abstraction.getAttachment ().getFirstName () ;
      }
      String referent = new String () ;
      if (abstraction.getReferentDoctor () != null) {
         referent = abstraction.getReferentDoctor ().getLastName () + " " + abstraction.getReferentDoctor ().getFirstName () ;
      }
      presentation.update (socialSecurityNumber, gender, dateOfBirth, lastName, firstName, attachement, referent) ;
   }

   public Patient_P getPresentation () {
      return presentation ;
   }

}
