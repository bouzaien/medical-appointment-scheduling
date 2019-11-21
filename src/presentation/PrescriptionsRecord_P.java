
package presentation;

import java.util.List ;


import controle.PrescriptionsRecord_C ;
import dao.MedicalRecordException ;
import javafx.beans.value.ChangeListener ;
import javafx.beans.value.ObservableValue ;
import javafx.collections.FXCollections ;
import javafx.collections.ObservableList ;
import javafx.collections.transformation.FilteredList ;
import javafx.collections.transformation.SortedList ;
import javafx.event.ActionEvent ;
import javafx.event.EventHandler ;
import javafx.scene.control.Button ;
import javafx.scene.control.ListView ;
import javafx.scene.control.Tab ;
import javafx.scene.control.TextField ;
import javafx.scene.layout.BorderPane ;
import javafx.scene.layout.FlowPane ;
import javafx.scene.layout.GridPane ;
import javafx.scene.layout.StackPane ;
import javafx.scene.paint.Color ;
import javafx.scene.shape.Rectangle ;
import javafx.stage.Stage ;

public class PrescriptionsRecord_P extends Tab {
   private ObservableList<String> PrescriptionsNames ;
   private ListView<String> PrescriptionsList ;
   private int currentIndex ;
   private BorderPane globalPane ;
   private Button editPrescriptionButton ;
   private Button deletePrescriptionButton ;
   private Stage stage ;

   //-------------------------------------------------------------------------------------
   // constructeur : on mémorise seulement le stage principal
   //-------------------------------------------------------------------------------------
   public PrescriptionsRecord_P (Stage stage) {
      this.stage = stage ;    
   }

   //-------------------------------------------------------------------------------------
   // initialisation : on organise la présentation en accord avec le contenu à gérer
   //-------------------------------------------------------------------------------------   
   public void initialize (PrescriptionsRecord_C control, List<String> Prescriptions) {
      // liste des noms des patients à lister
      PrescriptionsNames = FXCollections.observableArrayList (Prescriptions) ;
      // remplissage du widget de présentation des noms avec la liste des noms des docteurs
      PrescriptionsList = new ListView<String> (PrescriptionsNames) ;
      PrescriptionsList.setPrefWidth (360);
      // ajout d'un listener sur la liste pour savoir quand un patient est sélectionné ou pas
      PrescriptionsList.getSelectionModel().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            if (! PrescriptionsList.getSelectionModel ().isEmpty ()) {
               // on essaie de trouver l'index de l'élément sélectionné
               currentIndex = PrescriptionsList.getSelectionModel ().getSelectedIndex () ;
               // on le transfère au contrôle pour mémorisation
               control.selectPrescription (currentIndex) ;
            } else {
               // aucun docteur n'est plus sélectionné : on le signale au contrôle
               control.selectNoPrescription () ;
            }
         }
      }) ;

      // ajout de boutons New/Edit/Delete et des gestionnaires d'événements associés
      // c'est à chaque fois le controleur associé qui va gérer l'action à déclencher
      FlowPane buttons = new FlowPane () ;
      
      FilteredList<String> filteredData = new FilteredList<> (PrescriptionsNames,p -> true) ;
      TextField txt = new TextField();
      txt.setPromptText("Search..");
      // 2. Set the filter Predicate whenever the filter changes.
      txt.textProperty().addListener((observable, oldValue, newValue) -> {
          filteredData.setPredicate(myObject -> {
              // If filter text is empty, display all persons.
              if (newValue == null || newValue.isEmpty()) {
                  return true;
              }

              // Compare first name and last name field in your object with filter.
              String lowerCaseFilter = newValue.toLowerCase();

              if (String.valueOf(myObject).toLowerCase().contains(lowerCaseFilter)) {
                  return true;
                  // Filter matches first name.

              } else {
              

              return false;}
              // Does not match.
          });
      });
      // 3. Wrap the FilteredList in a SortedList. 
      SortedList<String> sortedData = new SortedList<>(filteredData);

      // 5. Add sorted (and filtered) data to the table.
      PrescriptionsList.setItems(sortedData);
      
      Button newPrescriptionButton = new Button () ;
      newPrescriptionButton.setPrefWidth (120);
      newPrescriptionButton.setText ("New Prescription...") ;
      newPrescriptionButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            try {
               control.createPrescription () ;
            } catch (MedicalRecordException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }) ;
      editPrescriptionButton = new Button () ;
      editPrescriptionButton.setPrefWidth (120);
      editPrescriptionButton.setText ("Edit Prescription...") ;
      editPrescriptionButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            try {
               control.editPrescription () ;
            } catch (MedicalRecordException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }) ;
      deletePrescriptionButton = new Button () ;
      deletePrescriptionButton.setPrefWidth (120);
      deletePrescriptionButton.setText ("Delete...");
      deletePrescriptionButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.deletePrescription () ;
         }
      }) ;
      disableActionOnSelection () ;
      buttons.getChildren ().addAll (newPrescriptionButton, editPrescriptionButton, deletePrescriptionButton) ;
      
      txt.setPrefWidth (360);
      txt.setMaxWidth(360);
      globalPane = new BorderPane () ;
      globalPane.setLeft (PrescriptionsList) ;
      globalPane.setBottom (buttons);
      globalPane.setTop(txt);
      globalPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #aed6f1 ,#5499c7)");

      setText ("Prescriptions médicales");
      setContent(globalPane) ;
      setClosable (false);


   }

   public Stage getStage () {
      return stage ;
   }


   //-------------------------------------------------------------------------------------
   // méthodes appelées par le contrôle associé suite à des actions de l'utilisateur ou à des changements d'états
   //-------------------------------------------------------------------------------------

   //-------------------------------------------------------------------------------------
   // désélection d'un patient
   //-------------------------------------------------------------------------------------

   public void selectNoPrescription () {
      PrescriptionsList.getSelectionModel ().clearSelection () ;
      globalPane.setCenter (null) ;
      globalPane.autosize () ;
   }

   //-------------------------------------------------------------------------------------
   // sélection d'un patient : deux méthodes pour être plus précis dans ce que l'on demande
   //-------------------------------------------------------------------------------------

   public void selectPrescription (Prescription_P currentPrescription) {
      GridPane pane = new GridPane() ;
      Rectangle rectangle = new Rectangle () ;
      rectangle.setWidth (500);
      rectangle.setHeight (120);
      rectangle.setFill (Color.ANTIQUEWHITE);
      StackPane stack = new StackPane() ;
      stack.getChildren ().addAll (rectangle,currentPrescription) ;
      pane.add (stack, 2, 2);
      globalPane.setCenter (pane);
   }

   public void selectPrescription (String currentPrescriptionName) {
      PrescriptionsList.getSelectionModel ().select (currentPrescriptionName) ;
   }

   //-------------------------------------------------------------------------------------
   // activation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void enableActionOnSelection () {
      editPrescriptionButton.setDisable (false) ;
      deletePrescriptionButton.setDisable (false) ;      
   }

   //-------------------------------------------------------------------------------------
   // désactivation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void disableActionOnSelection () {
      editPrescriptionButton.setDisable (true) ;
      deletePrescriptionButton.setDisable (true) ;                        
   }

   //-------------------------------------------------------------------------------------
   // désactivation de la fonction de suppression
   //-------------------------------------------------------------------------------------
   public void disableDeleteOnSelection () {
      deletePrescriptionButton.setDisable (true) ;      
   }
   public List<String> getNames (){
      return this.PrescriptionsNames;
   }

}

