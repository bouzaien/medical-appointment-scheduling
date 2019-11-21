package presentation ;

import javafx.geometry.Pos ;
import javafx.scene.control.Label ;
import javafx.scene.layout.GridPane ;

//-------------------------------------------------------------------------------------
// classe de présentation d'un patient : elle hérite d'un GridPane
//-------------------------------------------------------------------------------------

public class Doctor_P extends GridPane {

   private Label addressLabel ;
   private Label telLabel ;
   private Label lastNameLabel ;
   private Label firstNameLabel ;
   private Label specialtyLabel ;
   private Label rppsLabel;
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le controle et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Doctor_P () {
      Label addressTitle = new Label ("Address : ") ;
      Label telTitle = new Label ("Tel : ") ;
      Label lastNameTitle = new Label ("Last Name : ") ;
      Label firstNameTitle = new Label ("First Name : ") ;
      Label specialtyTitle = new Label ("Speciality :") ;
      Label rppsTitle= new Label ("Rpps :");
      telLabel = new Label () ;
      rppsLabel = new Label();
      addressLabel = new Label () ;
      lastNameLabel = new Label () ;
      firstNameLabel = new Label () ;
      specialtyLabel = new Label () ;

      addRow (0, lastNameTitle, lastNameLabel) ;
      addRow (1, firstNameTitle, firstNameLabel) ;
      addRow (2, specialtyTitle, specialtyLabel) ;
      addRow (3, rppsTitle, rppsLabel) ;
      addRow (4, telTitle, telLabel) ;
      addRow (5, addressTitle,addressLabel) ;      
   }
   
   public void update (String lastName, String firstName,String specialty, String rpps,
         String tel, String address  ) {
      telLabel.setText (tel) ;
      rppsLabel.setText (rpps);
      addressLabel.setText (address) ;
      lastNameLabel.setText (lastName) ;
      firstNameLabel.setText (firstName) ;
      specialtyLabel.setText (specialty) ;
   }

}