package presentation ;

import java.time.LocalDate ;
import java.util.List ;

import controle.Patient_ActiveC ;
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

//-------------------------------------------------------------------------------------
// classe de présentation active (éditable) d'un patient :
// elle hérite maintenant d'un Stage pour réduire le nombre de classes de présentation
//-------------------------------------------------------------------------------------

public class Patient_ActiveP extends Stage {

   private TextField socialSecurityNumberField ;
   private HBox genderRadio ;
   private TextField lastNameField ;
   private TextField firstNameField ;
   private ChoiceBox<String> attachmentChoice ;
   private ChoiceBox<String> referentDoctorChoice ;
   private DatePicker datePicker ;
   private RadioButton maleRB ;
   private RadioButton femaleRB ;

   private int currentReferentDoctorIndex ;
   private int currentAttachmentIndex ;

   private Patient_ActiveC control ;
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le patient et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Patient_ActiveP (Patient_ActiveC patient_ActiveC, Stage application) {
      // la fenêtre est déclarée modale de façon à bloquer les interactions avec la fenêtre "mère"
      initModality(Modality.WINDOW_MODAL);
      // la fenêtre mère est la présentation principale de l'application
      initOwner (application) ;

      control = patient_ActiveC ;
      
      // mise en place des composants de présentation : on lres remplira plus tard via un appel à la méthode update
      
      Label socialSecurityNumberTitle = new Label ("Social Security Number : ") ;
      Label genderTitle = new Label ("Gender : ") ;
      Label dateOfBirthTitle = new Label ("Date of birth : ") ;
      Label lastNameTitle = new Label ("Last Name : ") ;
      Label firstNameTitle = new Label ("First Name : ") ;
      Label attachmentTitle = new Label ("Attachement : ") ;
      Label referentTitle = new Label ("Referent Doctor :") ;
      socialSecurityNumberField = new TextField () ;
      genderRadio = new HBox () ;
      lastNameField = new TextField () ;
      firstNameField = new TextField () ;
      datePicker = new DatePicker () ;
      maleRB = new RadioButton("M") ;
      femaleRB = new RadioButton("F") ;

      GridPane patientPane = new GridPane () ;
      patientPane.addRow (0, socialSecurityNumberTitle, socialSecurityNumberField) ;
      
      final ToggleGroup group = new ToggleGroup () ;
      
      maleRB.setToggleGroup (group) ;
      genderRadio.getChildren().add (maleRB) ;
      femaleRB.setToggleGroup (group) ;
      genderRadio.getChildren ().add (femaleRB) ;
      patientPane.addRow (1, genderTitle, genderRadio);
      patientPane.addRow (2, dateOfBirthTitle, datePicker);
      patientPane.addRow (3, lastNameTitle, lastNameField);
      patientPane.addRow (4, firstNameTitle, firstNameField);
      
      attachmentChoice = new ChoiceBox<String> () ;

      attachmentChoice.getSelectionModel ().select (currentAttachmentIndex) ;
      attachmentChoice.getSelectionModel ().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentAttachmentIndex = attachmentChoice.getSelectionModel ().getSelectedIndex () ;
         }

      }) ;

      patientPane.addRow (5, attachmentTitle, attachmentChoice) ;
      
      referentDoctorChoice = new ChoiceBox<String> () ;
      referentDoctorChoice.getSelectionModel ().select (currentReferentDoctorIndex) ;
      //Un ChangeListener est un objet qui peut être ajouté à n'importe quelle propriété de n'importe quel objet.
      //Il permet de détecter un changement de cette propriété et de déclencher une action lors de ce changement.
      //ici on utilise un ChangeListener pour modifier le currentReferentDoctorIndex avec l'index selectionné dans le choiceBox
    
      referentDoctorChoice.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentReferentDoctorIndex = referentDoctorChoice.getSelectionModel ().getSelectedIndex ();
         }

      }) ;

      patientPane.addRow (6, referentTitle, referentDoctorChoice) ;

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
      globalPane.addRow (0, patientPane) ;
      // ajout du container de boutons dans le container global 
      globalPane.addRow (1,  buttons);
      // création de la scène globale à partir du container
      Scene scene = new Scene (globalPane) ;
      // définition de cette scène comme scène du composant stage
      setScene (scene) ;
      setTitle ("Add/Edit Patient") ;

   }

   //-------------------------------------------------------------------------------------
   // rempli le contenu de l'affichage avec les bonnes valeurs
   //-------------------------------------------------------------------------------------
   public void update (String socialSecurityNumber, String gender,
         String dateOfBirth, String lastName, String firstName, String attachment, String referent,
         List<String> attachmentsNames, List<String> doctorsNames) {

      socialSecurityNumberField.setText (socialSecurityNumber) ;
      lastNameField.setText (lastName) ;
      firstNameField.setText (firstName) ;
      if (gender != null) {
         if (gender.equals ("Male")) {
            maleRB.setSelected (true) ;
         } else if (gender.equals ("Female")) {
            femaleRB.setSelected (true) ;
         } else {
            maleRB.setSelected (false) ;
            femaleRB.setSelected (false) ;
         }
      }
      if (dateOfBirth != null) {
         String [] dateOfBirthTable = dateOfBirth.split ("/") ;
         int dayOfMonth = Integer.parseInt (dateOfBirthTable [0]) ; 
         int month = Integer.parseInt (dateOfBirthTable [1]) ; 
         int year = Integer.parseInt (dateOfBirthTable [2]) ; 
         datePicker.setValue (LocalDate.of (year, month, dayOfMonth)) ;
      } else {
         datePicker.setValue (null) ;
      }
      if (attachment != null) {
         currentAttachmentIndex = attachmentsNames.indexOf (attachment) ;
      } else {
         currentAttachmentIndex = attachmentsNames.size () ;
      }
      ObservableList<String> attachments = FXCollections.observableArrayList (attachmentsNames) ;
      attachments.add (null) ;
      attachmentChoice.setItems (attachments) ;
      attachmentChoice.getSelectionModel ().select (currentAttachmentIndex) ;

      if (referent != null) { 
         currentReferentDoctorIndex = doctorsNames.indexOf (referent) ;
      } else {
         currentReferentDoctorIndex = doctorsNames.size () ;
      }
      ObservableList<String> possibleReferentDoctors = FXCollections.observableArrayList (doctorsNames) ;
      possibleReferentDoctors.add (null) ;
      
      referentDoctorChoice.setItems (possibleReferentDoctors) ;
      referentDoctorChoice.getSelectionModel ().select (currentReferentDoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode de mise à jour du patient associé : on fait les transformations nécessaires
   // pour récupérer des éléments graphiques ce qui permet de mettre à jour un patient
   // et on les transmet au controle via une méthode dédiée 
   //-------------------------------------------------------------------------------------
   
   public String getLastName () {
      return (lastNameField.getText ()) ;
   }
   
   public String getFirstName () {
      return (firstNameField.getText ()) ;
   }
   
   public String getDateOfBirth () {
      LocalDate date = datePicker.getValue () ;
      return (date.getDayOfMonth ()+"/"+date.getMonthValue ()+"/"+date.getYear ()) ;
   }
   
   public String getGender () {
      String genderText = new String () ;
      if (maleRB.isSelected ()) {
         genderText = "Male" ;
      } else if (femaleRB.isSelected ()) {
         genderText = "Female" ;
      } else {
         genderText = "Unknown" ;
      }
      return (genderText) ;
   }
   
   public String getSSN () {
      return (socialSecurityNumberField.getText ()) ;
   }
   
   public int getAttachment () {
      return (currentAttachmentIndex) ;
   }
   
   public int getReferent () {
      return (currentReferentDoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode relai vers le controle associé
   //-------------------------------------------------------------------------------------
   
   public Patient_ActiveC getControl () {
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
