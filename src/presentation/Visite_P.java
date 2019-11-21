package presentation;

import java.util.Date ;


import javafx.scene.control.Label ;
import javafx.scene.layout.GridPane ;
import metier.Doctor ;
import metier.Patient ;

public class Visite_P extends GridPane {
   
   private Label DoctorLabel ;
   private Label PatientLabel ;
   private Label priceLabel ;
   private Label dateLabel ;
   
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le controle et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Visite_P () {
      Label DoctorTitle  = new Label ("Doctor : ") ;
      Label PatientTitle = new Label ("Patient : ") ;
      Label priceTitle = new Label ("Price : ") ;
      Label dateTitle = new Label ("Date : ") ;
      DoctorLabel = new Label () ;
      PatientLabel = new Label () ;
      priceLabel = new Label () ;
      dateLabel = new Label () ;
      
      addRow (0, DoctorTitle, DoctorLabel) ;
      addRow (1, PatientTitle, PatientLabel) ;
      addRow (2, priceTitle, priceLabel) ;
      addRow (3, dateTitle, dateLabel) ;  
   }
   
   public void update (String doctor, String patient, float price,  Date date) {
      DoctorLabel.setText (doctor) ;
      PatientLabel.setText (patient) ;
      priceLabel.setText (Float.toString(price)) ;
      dateLabel.setText (date.toString ()) ;
   }

}
