package presentation;

import java.util.Date ;

import javafx.scene.control.Label ;
import javafx.scene.layout.GridPane ;
import rdv.Visite ;

public class Prescription_P extends GridPane {
   private Label medicamentLabel ;
   private Label doctorLabel ;
   private Label patientLabel ;
   private Label dateLabel ;
   private Label dureeLabel ;
   private Label posologieLabel ;
   private Label modalitesLabel ;
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le controle et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Prescription_P () {
      Label medicamentTitle = new Label ("Medicament : ") ;
      Label doctorTitle = new Label ("Doctor : ") ;
      Label patientTitle = new Label ("Patient : ") ;
      Label dateTitle = new Label ("Date : ") ;
      Label dureeTitle = new Label ("Duree : ") ;
      Label posologieTitle = new Label ("Posologie : ") ;
      Label modalitesTitle = new Label ("Modalites : ") ;
      medicamentLabel = new Label () ;
      doctorLabel = new Label () ;
      patientLabel = new Label () ;
      dateLabel = new Label () ;
      dureeLabel = new Label () ;
      posologieLabel = new Label () ;
      modalitesLabel = new Label () ;
      addRow (0, medicamentTitle, medicamentLabel) ;
      addRow (1, doctorTitle, doctorLabel) ;
      addRow (2, patientTitle, patientLabel) ;
      addRow (3, dateTitle, dateLabel) ;
      addRow (4, dureeTitle, dureeLabel) ;
      addRow (5, posologieTitle, posologieLabel) ;
      addRow (6, modalitesTitle, modalitesLabel) ;      
   }
   
   public void update (String medicament, Visite visite, Date date, int duree, float posologie,
         String modalites) {
      medicamentLabel.setText (medicament) ;
      doctorLabel.setText (visite.getDoctor().getFirstName () + ' ' + visite.getDoctor().getLastName ()) ;
      patientLabel.setText (visite.getPatient().getFirstName () + ' ' + visite.getPatient().getLastName ()) ;
      dateLabel.setText (date.toString()) ;
      dureeLabel.setText (Integer.toString (duree)) ;
      posologieLabel.setText (Float.toString (posologie)) ;
      modalitesLabel.setText (modalites) ;
   }

}
