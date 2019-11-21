package presentation ;

import java.time.Instant ;
import java.time.LocalDate ;
import java.time.ZoneId ;
import java.util.Date ;
import java.util.List ;


import controle.Prescription_ActiveC ;
import dao.DoctorDAO ;
import dao.MedicalRecordException ;
import dao.PatientDAO ;
import dao.VisiteDAO ;
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
import metier.Doctor ;
import metier.Patient ;
import rdv.Visite ;

//-------------------------------------------------------------------------------------
// classe de présentation active (éditable) d'un prescription :
// elle hérite maintenant d'un Stage pour réduire le nombre de classes de présentation
//-------------------------------------------------------------------------------------

public class Prescription_ActiveP extends Stage {

   private TextField medicamentField ;

   private TextField dureeField ;
   private TextField posologieField ;
   private ChoiceBox<String> doctorChoice ;
   private ChoiceBox<String> patientChoice ;
   private DatePicker datePicker ;
   private TextField modalitesField ;


   private int currentdoctorIndex ;
   private int currentpatientIndex ;
   

   private Prescription_ActiveC control ;
   private PatientDAO patient ;
   private DoctorDAO doctor ;

   //-------------------------------------------------------------------------------------
   // constructeur : mémorise le prescription et met en page l'affichage
   //-------------------------------------------------------------------------------------
   public Prescription_ActiveP (Prescription_ActiveC prescription_ActiveC, Stage application) {
      // la fenêtre est déclarée modale de façon à bloquer les interactions avec la fenêtre "mère"
      initModality(Modality.WINDOW_MODAL);
      // la fenêtre mère est la présentation principale de l'application
      initOwner (application) ;

      control = prescription_ActiveC ;

      // mise en place des composants de présentation : on lres remplira plus tard via un appel à la méthode update

      Label medicamentTitle = new Label ("medicament : ") ;
      Label dureeTitle = new Label ("duree : ") ;
      Label modalitesTitle = new Label ("modalites : ") ;
      Label posologieTitle = new Label ("posologie : ") ;
      Label dateTitle = new Label ("date_visite : ") ;
      Label medecinTitle = new Label ("medecin : ") ;
      Label patientTitle = new Label ("patient :") ;
      medicamentField = new TextField () ;

      dureeField = new TextField () ;
      modalitesField = new TextField () ;
      posologieField = new TextField() ;
      datePicker = new DatePicker () ;


      GridPane prescriptionPane = new GridPane () ;
      prescriptionPane.addRow (0, medicamentTitle, medicamentField) ;



      prescriptionPane.addRow (3, dateTitle, datePicker);
      prescriptionPane.addRow (4, dureeTitle, dureeField);
      prescriptionPane.addRow (5, modalitesTitle, modalitesField);
      prescriptionPane.addRow (6, posologieTitle, posologieField);


      patientChoice = new ChoiceBox<String> () ;

      patientChoice.getSelectionModel ().select (currentpatientIndex) ;
      patientChoice.getSelectionModel ().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentpatientIndex = patientChoice.getSelectionModel ().getSelectedIndex () ;
         }

      }) ;

      prescriptionPane.addRow (1, patientTitle, patientChoice) ;

      doctorChoice = new ChoiceBox<String> () ;
      doctorChoice.getSelectionModel ().select (currentdoctorIndex) ;
      //Un ChangeListener est un objet qui peut être ajouté à n'importe quelle propriété de n'importe quel objet.
      //Il permet de détecter un changement de cette propriété et de déclencher une action lors de ce changement.
      //ici on utilise un ChangeListener pour modifier le currentDoctorIndex avec l'index selectionné dans le choiceBox

      doctorChoice.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            currentdoctorIndex = doctorChoice.getSelectionModel ().getSelectedIndex ();
         }

      }) ;

      prescriptionPane.addRow (2, medecinTitle, doctorChoice) ;

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
      globalPane.addRow (0, prescriptionPane) ;
      // ajout du container de boutons dans le container global 
      globalPane.addRow (1,  buttons);
      // création de la scène globale à partir du container
      Scene scene = new Scene (globalPane) ;
      // définition de cette scène comme scène du composant stage
      setScene (scene) ;
      setTitle ("Add/Edit prescription") ;

   }

   //-------------------------------------------------------------------------------------
   // rempli le contenu de l'affichage avec les bonnes valeurs
   //-------------------------------------------------------------------------------------
   public void update ( String medicament, Visite visite, Date date, int duree,float posologie, String modalites,
         List<String> patients, List<String> doctors ) {

      medicamentField.setText (medicament) ;
      dureeField.setText(Integer.toString  (duree)) ;
      posologieField.setText(Float.toString(posologie) );
      modalitesField.setText (modalites);

      LocalDate datee = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
      datePicker.setValue (datee) ;

      if (visite.getPatient () != null) {
         currentpatientIndex = patients.indexOf (visite.getPatient ()) ;
      } else {
         currentpatientIndex = patients.size () ;
      }
      ObservableList<String> patientsNames = FXCollections.observableArrayList (patients) ;
      patients.add (null) ;
      patientChoice.setItems (patientsNames) ;
      patientChoice.getSelectionModel ().select (currentpatientIndex) ;

      if (visite.getDoctor() != null) { 
         currentdoctorIndex = doctors.indexOf (visite.getDoctor()) ;
      } else {
         currentdoctorIndex = doctors.size () ;
      }
      ObservableList<String> doctorsNames = FXCollections.observableArrayList (doctors) ;
      doctorsNames.add (null) ;

      doctorChoice.setItems (doctorsNames) ;
      doctorChoice.getSelectionModel ().select (currentdoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode de mise à jour du prescription associé : on fait les transformations nécessaires
   // pour récupérer des éléments graphiques ce qui permet de mettre à jour un prescription
   // et on les transmet au controle via une méthode dédiée 
   //-------------------------------------------------------------------------------------

   public String getMedicament () {
      return (medicamentField.getText ()) ;
   }

   public String getModalites () {
      return (modalitesField.getText ()) ;
   }

   public Date getDate () {
      Date datte = java.sql.Date.valueOf (datePicker.getValue ()) ;
      return datte;

   }
   public String getDuree () {
      return(dureeField.getText ());
   }
   public Visite getVisite() throws MedicalRecordException {
      VisiteDAO visiteDAO = new VisiteDAO() ;
      Date datte = java.sql.Date.valueOf (datePicker.getValue ()) ;

      return visiteDAO.find (datte, patient.find(currentpatientIndex), doctor.find (currentdoctorIndex));
   }



   public String getposologie () {
      return (posologieField.getText ()) ;
   }

   public int getAttachment () {
      return (currentpatientIndex) ;
   }

   public int getdoctor () {
      return (currentdoctorIndex) ;
   }

   //-------------------------------------------------------------------------------------
   // méthode relai vers le controle associé
   //-------------------------------------------------------------------------------------

   public Prescription_ActiveC getControl () {
      return control ;
   }

   //-------------------------------------------------------------------------------------
   // méthode appelée en cas d'abandon du dialogue : on se contente de fermer la fenêtre
   //-------------------------------------------------------------------------------------
   protected void handleCancel () {
      close () ;
   }

   //-------------------------------------------------------------------------------------
   // méthode appelée en cas de succès du dialogue : on modifie le prescription avec les données saisies dans le formulaire
   //-------------------------------------------------------------------------------------
   protected void handleOK () throws MedicalRecordException {
      // on prévient le controle que les modifications sont à prendre en compte 
      control.editionComplete () ;
      // on ferme la fenêtre
      close () ;
   }

}
