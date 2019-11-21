package presentation ;

import java.util.List ;

import controle.DoctorRecord_C ;
import controle.PatientsRecord_C ;
import javafx.beans.value.ChangeListener ;
import javafx.beans.value.ObservableValue ;
import javafx.collections.FXCollections ;
import javafx.collections.ObservableList ;
import javafx.collections.transformation.FilteredList ;
import javafx.collections.transformation.SortedList ;
import javafx.event.ActionEvent ;
import javafx.event.EventHandler ;
import javafx.geometry.Pos ;
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
import metier.Doctor ;

//-------------------------------------------------------------------------------------
// classe de présentation de la liste des Doctors : c'est un onglet
//-------------------------------------------------------------------------------------

public class DoctorsRecord_P extends Tab {

   private ObservableList<String> DoctorsNames ;
   private ListView<String> DoctorsList ;
   private int currentIndex ;
   private BorderPane globalPane ;
   private Button editDoctorButton ;
   private Button deleteDoctorButton ;
   private Stage stage ;
 
   //-------------------------------------------------------------------------------------
   // constructeur : on mémorise seulement le stage principal
   //-------------------------------------------------------------------------------------
   public DoctorsRecord_P (Stage stage) {
      this.stage = stage ;    
   }
   
   //-------------------------------------------------------------------------------------
   // initialisation : on organise la présentation en accord avec le contenu à gérer
   //-------------------------------------------------------------------------------------   
   public void initialize (DoctorRecord_C control, List<String> doctors) {
      // liste des noms des docteurs à lister
      DoctorsNames = FXCollections.observableArrayList (doctors) ;
      // remplissage du widget de présentation des noms avec la liste des noms des docteurs
      DoctorsList = new ListView<String> (DoctorsNames) ;
      DoctorsList.setPrefWidth (360);
      FilteredList<String> filteredData = new FilteredList<> (DoctorsNames,p -> true) ;

      // ajout d'un listener sur la liste pour savoir quand un docteur est sélectionné ou pas
      DoctorsList.getSelectionModel().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            if (! DoctorsList.getSelectionModel ().isEmpty ()) {
               // on essaie de trouver l'index de l'élément sélectionné
               currentIndex = DoctorsList.getSelectionModel ().getSelectedIndex () ;
               // on le transfère au contrôle pour mémorisation
               control.selectDoctor (currentIndex) ;
            } else {
               // aucun docteur n'est plus sélectionné : on le signale au contrôle
               control.selectNoDoctor () ;
            }
         }
      }) ;
      
      // ajout de boutons New/Edit/Delete et des gestionnaires d'événements associés
      // c'est à chaque fois le controleur associé qui va gérer l'action à déclencher
      // ajout de boutons New/Edit/Delete et des gestionnaires d'événements associés
      // c'est à chaque fois le controleur associé qui va gérer l'action à déclencher
      FlowPane buttons = new FlowPane () ;
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
      DoctorsList.setItems(sortedData);
      Button newDoctorButton = new Button () ;
      
      newDoctorButton.setPrefWidth (120);

      newDoctorButton.setText ("New Doctor...") ;
      newDoctorButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.createDoctor () ;
         }
      }) ;
      editDoctorButton = new Button () ;
      editDoctorButton.setPrefWidth (120);
      editDoctorButton.setText ("Edit Doctor...") ;
      editDoctorButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.editDoctor () ;
         }
      }) ;

      // Clic sur le bouton editer : gestionnaire d'événement
      // Appeler un dialogue et mettre une presentation active du docteur (editDoctor: setAbstraction et Activate)
      //    setAbstraction : obtention des données de l'abstraction, traduction en chaine et les mettre dans la presentation
      //    Activate : Afficher la presentation (stage.show())
      // Clic sur le bouton OK : gestionnaire d'événement
      // Récupérer les information de la présentation et les transferer vers l'abstraction editionComplete)
      // maj/ajout du docteur
      // reinisialisation de la presentation

      deleteDoctorButton = new Button () ;
      deleteDoctorButton.setPrefWidth (120);

      deleteDoctorButton.setText ("Delete") ;
      deleteDoctorButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.deleteDoctor () ;
         }
      }) ;
      disableActionOnSelection () ;
      buttons.getChildren ().addAll (newDoctorButton, editDoctorButton, deleteDoctorButton) ;
      txt.setPrefWidth (360);
      txt.setMaxWidth(360);
      globalPane = new BorderPane () ;
      globalPane.setLeft (DoctorsList) ;
      globalPane.setBottom (buttons);
      globalPane.setTop(txt);
      globalPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #aed6f1 ,#5499c7)");

      setText ("Doctors") ;
      setContent (globalPane) ;
      setClosable (false) ;
   }
      


   //-------------------------------------------------------------------------------------
   // méthodes appelées par le contrôle associé suite à des actions de l'utilisateur ou à des changements d'états
   //-------------------------------------------------------------------------------------

   //-------------------------------------------------------------------------------------
   // désélection d'un Doctor
   //-------------------------------------------------------------------------------------

   public void selectNoDoctor () {
      
      DoctorsList.getSelectionModel ().clearSelection () ;
      globalPane.setCenter (null) ;
      globalPane.autosize () ;
   }

   //-------------------------------------------------------------------------------------
   // sélection d'un Doctor : deux méthodes pour être plus précis dans ce que l'on demande
   //-------------------------------------------------------------------------------------

   public void selectDoctor (Doctor_P currentDoctor) {
      GridPane pane = new GridPane() ;
      Rectangle rectangle = new Rectangle () ;
      rectangle.setWidth (500);
      rectangle.setHeight (105);
      rectangle.setFill (Color.ANTIQUEWHITE);
      StackPane stack = new StackPane() ;
      stack.getChildren ().addAll (rectangle,currentDoctor) ;
      pane.add (stack, 2, 2);
      globalPane.setCenter (pane);
   }

   public void selectDoctor (String currentDoctorName) {
      DoctorsList.getSelectionModel ().select (currentDoctorName) ;
   }

   //-------------------------------------------------------------------------------------
   // activation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void enableActionOnSelection () {
      editDoctorButton.setDisable (false) ;
      deleteDoctorButton.setDisable (false) ;      
   }

   //-------------------------------------------------------------------------------------
   // désactivation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void disableActionOnSelection () {
      editDoctorButton.setDisable (true) ;
      deleteDoctorButton.setDisable (true) ;                        
   }

   //-------------------------------------------------------------------------------------
   // désactivation de la fonction de suppression
   //-------------------------------------------------------------------------------------
   public void disableDeleteOnSelection () {
      deleteDoctorButton.setDisable (true) ;      
   }
   
   public Stage getStage () {
      return stage ;
   }


   }




   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

