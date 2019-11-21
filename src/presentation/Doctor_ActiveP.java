package presentation ;

import java.time.LocalDate ;
import java.util.List ;

import controle.Doctor_ActiveC ;
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

public class Doctor_ActiveP extends Stage {

   private TextField lastNameField ;
   private TextField firstNameField ;
   private TextField specialityField  ;
   private TextField rppsField ;
   private TextField telField  ;
   private TextField addressField ;


   private Doctor_ActiveC control ;
   
   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le patient et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Doctor_ActiveP (Doctor_ActiveC doctor_ActiveC, Stage application) {
      // la fenêtre est déclarée modale de façon à bloquer les interactions avec la fenêtre "mère"
      initModality(Modality.WINDOW_MODAL);
      // la fenêtre mère est la présentation principale de l'application
      initOwner (application) ;

      control = doctor_ActiveC ;
      
      // mise en place des composants de présentation : on lres remplira plus tard via un appel à la méthode update
      
     
      Label lastNameTitle = new Label ("Last Name : ") ;
      Label firstNameTitle = new Label ("First Name : ") ;
      Label specialityTitle = new Label ("Speciality : ") ;
      Label rppsTitle = new Label ("Rpps : ") ;
      Label telTitle = new Label ("Phone number :") ;
      Label addressTitle = new Label ("Address :") ;
      telField = new TextField () ;
      lastNameField = new TextField () ;
      firstNameField = new TextField () ;
      specialityField = new TextField () ;
      rppsField = new TextField () ;
      addressField = new TextField () ;

      GridPane doctorPane = new GridPane () ;
      doctorPane.addRow (3, rppsTitle,rppsField);
      doctorPane.addRow (2, specialityTitle, specialityField);
      doctorPane.addRow (0, lastNameTitle, lastNameField);
      doctorPane.addRow (1, firstNameTitle, firstNameField);
      doctorPane.addRow (4, addressTitle, addressField);
      doctorPane.addRow (5, telTitle, telField);
      
      
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
      globalPane.addRow (0, doctorPane) ;
      // ajout du container de boutons dans le container global 
      globalPane.addRow (1,  buttons);
      // création de la scène globale à partir du container
      Scene scene = new Scene (globalPane) ;
      // définition de cette scène comme scène du composant stage
      setScene (scene) ;
      setTitle ("Add/Edit Doctor") ;

   }

   //-------------------------------------------------------------------------------------
   // rempli le contenu de l'affichage avec les bonnes valeurs
   //-------------------------------------------------------------------------------------
   public void update (String lastName, String firstName, String speciality, String rpps,
         String tel, String address) {

     
      lastNameField.setText (lastName) ;
      firstNameField.setText (firstName) ;
      specialityField.setText (speciality) ;
      telField.setText (tel) ;
      rppsField.setText (rpps) ;
      addressField.setText (address) ;
      
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

   public  String getSpeciality() {
      return specialityField.getText () ;
   }

  

   public String getRpps () {
      return rppsField.getText () ;
   }

  
   public String getPhoneNumber() {
      return telField.getText () ;
   }

   

   public String getAddress () {
      return addressField.getText () ;
   }

  

   //-------------------------------------------------------------------------------------
   // méthode relai vers le controle associé
   //-------------------------------------------------------------------------------------
   

   public Doctor_ActiveC getControl () {
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