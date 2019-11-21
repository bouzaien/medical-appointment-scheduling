package presentation;

import java.time.Instant ;
import java.time.LocalDate ;
import java.time.ZoneId ;
import java.util.ArrayList ;
import java.util.Date ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Set ;

import controle.Visite_ActiveC ;
import javafx.beans.value.ChangeListener ;
import javafx.beans.value.ObservableValue ;
import javafx.collections.FXCollections ;
import javafx.collections.ObservableList ;
import javafx.event.ActionEvent ;
import javafx.event.EventHandler ;
import javafx.scene.Scene ;
import javafx.scene.control.Button ;
import javafx.scene.control.ChoiceBox ;
import javafx.scene.control.DatePicker ;
import javafx.scene.control.Label ;
import javafx.scene.control.RadioButton ;
import javafx.scene.control.TextField ;
import javafx.scene.control.ToggleGroup ;
import javafx.scene.layout.FlowPane ;
import javafx.scene.layout.GridPane ;
import javafx.scene.layout.HBox ;
import javafx.stage.Modality ;
import javafx.stage.Stage ;

public class Visite_ActiveP extends Stage {
   private TextField priceField ;
   private ChoiceBox<String> patientChoice ;
   private ChoiceBox<String> doctorChoice ;
   private DatePicker datePicker ;

   private int currentPatientIndex ;
   private int currentDoctorIndex ;

   private Visite_ActiveC control ;
   
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le patient et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Visite_ActiveP (Visite_ActiveC visite_ActiveC , Stage application) {
      // la fenêtre est déclarée modale de façon à bloquer les interactions avec la fenêtre "mère"
      initModality(Modality.WINDOW_MODAL);
      // la fenêtre mère est la présentation principale de l'application
      initOwner (application) ;

      control = visite_ActiveC ;
      
      // mise en place des composants de présentation : on les remplira plus tard via un appel à la méthode update
      
      Label date = new Label ("Date: ") ;
      Label patientTitle = new Label ("Patient: ") ;
      Label priceTitle = new Label ("Prix consultation: ") ;
      Label doctorTitle = new Label ("Doctor: ") ;
      

      datePicker = new DatePicker () ;
      priceField = new TextField () ;
      patientChoice = new ChoiceBox<String> () ;
      doctorChoice = new ChoiceBox<String> () ;
     
      

      

      GridPane visitePane = new GridPane () ;
     

      visitePane.addRow (3, date, datePicker);
      visitePane.addRow (1, patientTitle, patientChoice) ;
      visitePane.addRow (0, doctorTitle, doctorChoice) ;
      visitePane.addRow (2, priceTitle, priceField) ;

      
      patientChoice.getSelectionModel ().select (currentPatientIndex) ;
      patientChoice.getSelectionModel ().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentPatientIndex = patientChoice.getSelectionModel ().getSelectedIndex () ;
         }

      }) ;
      
     
      
      doctorChoice.getSelectionModel ().select (currentDoctorIndex) ;
      //Un ChangeListener est un objet qui peut être ajouté à n'importe quelle propriété de n'importe quel objet.
      //Il permet de détecter un changement de cette propriété et de déclencher une action lors de ce changement.
      //ici on utilise un ChangeListener pour modifier le currentReferentDoctorIndex avec l'index selectionné dans le choiceBox
    
      doctorChoice.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentDoctorIndex = doctorChoice.getSelectionModel ().getSelectedIndex ();
         }

      }) ;
      
      
      
      
      
      
      
      

      // composant pour organiser les boutons OK et Cancel // le choix du FlowPane est arbitraire
      FlowPane buttons = new FlowPane () ;
      // bouton associé à l'abandon du dialogue
      Button cancelButton = new Button () ;
      cancelButton.setText ("Cancel") ;
      // association de l'action au clic sur le bouton via un gestionnaire d'événement
           cancelButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            handleCancel () ;
         }
      }) ;
      // bouton associé au succès du dialogue
      Button okButton = new Button () ;
      okButton.setText ("OK") ;
      // association de l'action au clic sur le bouton via un gestionnaire d'événement
      okButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            handleOK () ;
         }
      }) ;
      // ajout des boutons dans le container
      buttons.getChildren ().addAll (okButton, cancelButton) ;
      // création du container global
      GridPane globalPane = new GridPane () ;
      // ajout de l'éditeur de docteur dans le container global
      globalPane.addRow (0, visitePane) ;
      // ajout du container de boutons dans le container global 
      globalPane.addRow (1,  buttons);
      // création de la scène globale à partir du container
      Scene scene = new Scene (globalPane) ;
      // définition de cette scène comme scène du composant stage
      setScene (scene) ;
      setTitle ("Add/Edit visite") ;

   }

   //-------------------------------------------------------------------------------------
   // rempli le contenu de l'affichage avec les bonnes valeurs
   //-------------------------------------------------------------------------------------
   public void update (Date date, String patient, float price, String doctor, List<String> patientesNames, List<String> doctoresNames) {
      LocalDate datee = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            
    //  priceField.setText(Float.toString (price)) ;
      priceField.setText(Float.toString (price)) ;
      
      datePicker.setValue (datee) ;
      
     /* Set set = new HashSet() ;
      set.addAll(patientsNames) ;
      ArrayList patientesNames = new ArrayList(set) ;*/
      if (patient != null) {
         



         currentPatientIndex = patientesNames.indexOf (patient) ;
         
      } else {
         currentPatientIndex = patientesNames.size ();
      }
   /*   Set sete = new HashSet() ;
      sete.addAll(doctorsNames) ;
      ArrayList doctoresNames = new ArrayList(sete) ;
 */
      if (doctor != null) {
         currentDoctorIndex = doctoresNames.indexOf (doctor) ;
      } else {
         currentDoctorIndex = doctoresNames.size () ;
      }
      ObservableList<String> patients = FXCollections.observableArrayList (patientesNames) ;
      patients.add (null) ;
      patientChoice.setItems (patients) ;
      patientChoice.getSelectionModel ().select (currentPatientIndex) ;

      
      ObservableList<String> doctors = FXCollections.observableArrayList (doctoresNames) ;
      doctors.add (null) ;
      
      doctorChoice.setItems (doctors) ;
      doctorChoice.getSelectionModel ().select (currentDoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode de mise à jour du patient associé : on fait les transformations nécessaires
   // pour récupérer des éléments graphiques ce qui permet de mettre à jour un patient
   // et on les transmet au controle via une méthode dédiée 
   //-------------------------------------------------------------------------------------
   

   
  
   
   public Date getDate () {
      //LocalDate date = datePicker.getValue () ;
      //return (date.getDayOfMonth ()+"/"+date.getMonthValue ()+"/"+date.getYear ()) 
      Date datte = java.sql.Date.valueOf (datePicker.getValue ()) ;
      return datte;
   }
   
   public String getPrice () {
      return (priceField.getText ()) ;
   }
   
 
   
   public int getPatient () {
      return (currentPatientIndex) ;
   }
   
   public int getDoctor () {
      return (currentDoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode relai vers le controle associé
   //-------------------------------------------------------------------------------------
   
   public Visite_ActiveC getControl () {
      return control ;
   }
   
   //-------------------------------------------------------------------------------------
   // méthode appelée en cas d'abandon du dialogue : on se contente de fermer la fenêtre
   //-------------------------------------------------------------------------------------
   protected void handleCancel () {
      close () ;
   }

   //-------------------------------------------------------------------------------------
   // méthode appelée en cas de succès du dialogue : on modifie le patient avec les données saisies dans le formulaire
   //-------------------------------------------------------------------------------------
   protected void handleOK () {
      // on prévient le controle que les modifications sont à prendre en compte 
      control.editionComplete () ;
      // on ferme la fenêtre
      close () ;
   }

}
