package presentation ;

import javafx.scene.control.Label ;
import javafx.scene.layout.GridPane ;

//-------------------------------------------------------------------------------------
// classe de présentation d'un patient : elle hérite d'un GridPane
//-------------------------------------------------------------------------------------

public class Patient_P extends GridPane {

   private Label socialSecurityNumberLabel ;
   private Label genderLabel ;
   private Label dateOfBirthLabel ;
   private Label referentLabel ;
   private Label lastNameLabel ;
   private Label firstNameLabel ;
   private Label attachementLabel ;
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le controle et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Patient_P () {
      Label socialSecurityNumberTitle = new Label ("Social Security Number : ") ;
      Label genderTitle = new Label ("Gender : ") ;
      Label dateOfBirthTitle = new Label ("Date of birth : ") ;
      Label lastNameTitle = new Label ("Last Name : ") ;
      Label firstNameTitle = new Label ("First Name : ") ;
      Label attachmentTitle = new Label ("Attachement : ") ;
      Label referentTitle = new Label ("Referent Doctor :") ;
      socialSecurityNumberLabel = new Label () ;
      genderLabel = new Label () ;
      dateOfBirthLabel = new Label () ;
      referentLabel = new Label () ;
      lastNameLabel = new Label () ;
      firstNameLabel = new Label () ;
      attachementLabel = new Label () ;
      addRow (0, socialSecurityNumberTitle, socialSecurityNumberLabel) ;
      addRow (1, genderTitle, genderLabel) ;
      addRow (2, dateOfBirthTitle, dateOfBirthLabel) ;
      addRow (3, lastNameTitle, lastNameLabel) ;
      addRow (4, firstNameTitle, firstNameLabel) ;
      addRow (5, attachmentTitle, attachementLabel) ;
      addRow (6, referentTitle, referentLabel) ;      
   }
   
   public void update (String socialSecurityNumber, String gender, String dateOfBirth, String lastName, String firstName,
         String attachement, String referent) {
      socialSecurityNumberLabel.setText (socialSecurityNumber) ;
      genderLabel.setText (gender) ;
      dateOfBirthLabel.setText (dateOfBirth) ;
      referentLabel.setText (referent) ;
      lastNameLabel.setText (lastName) ;
      firstNameLabel.setText (firstName) ;
      attachementLabel.setText (attachement) ;
   }

}
