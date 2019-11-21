package presentation;

import java.util.ArrayList ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Set ;

import controle.VisitesRecord_C ;
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

public class VisitesRecord_P extends Tab {
   private ObservableList<String> visitesNames ;
   private ListView<String> visitesList ;
   private int currentIndex ;
   private BorderPane globalPane ;
   private Button editVisiteButton ;
   private Button deleteVisiteButton ;
   private Stage stage ;
 
   //-------------------------------------------------------------------------------------
   // constructeur : on mémorise seulement le stage principal
   //-------------------------------------------------------------------------------------
   public VisitesRecord_P (Stage stage) {
      this.stage = stage ;    
   }
   
   //-------------------------------------------------------------------------------------
   // initialisation : on organise la présentation en accord avec le contenu à gérer
   //-------------------------------------------------------------------------------------   
   public void initialize (VisitesRecord_C control, List<String> visites) {
      // liste des noms des patients à lister
      visitesNames = FXCollections.observableArrayList (visites) ;
      
      
      
      
      // remplissage du widget de présentation des noms avec la liste des noms des docteurs
      
      
      visitesList = new ListView<String> ( visitesNames) ;
      visitesList.setPrefWidth (360);

     
      
      // ajout d'un listener sur la liste pour savoir quand un patient est sélectionné ou pas
      visitesList.getSelectionModel().selectedItemProperty().addListener (new ChangeListener<String> () {
         @Override
         public void changed (ObservableValue < ? extends String> ov, String old_val, String new_val) {
            if (! visitesList.getSelectionModel ().isEmpty ()) {
               // on essaie de trouver l'index de l'élément sélectionné
               currentIndex = visitesList.getSelectionModel ().getSelectedIndex () ;
               // on le transfère au contrôle pour mémorisation
               control.selectVisite (currentIndex) ;
            } else {
               // aucun docteur n'est plus sélectionné : on le signale au contrôle
               control.selectNoVisite () ;
            }
         }
      }) ;
      
      // ajout de boutons New/Edit/Delete et des gestionnaires d'événements associés
      // c'est à chaque fois le controleur associé qui va gérer l'action à déclencher
      FlowPane buttons = new FlowPane () ;
      
      FilteredList<String> filteredData = new FilteredList<> (visitesNames,p -> true) ;
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
      visitesList.setItems(sortedData);
      
      
      Button newVisiteButton = new Button () ;
      newVisiteButton.setText ("New Visite...") ;
      newVisiteButton.setPrefWidth (120);

      newVisiteButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.createVisite () ;
         }
      }) ;
      editVisiteButton = new Button () ;
      editVisiteButton.setText ("Edit Visite...") ;
      editVisiteButton.setPrefWidth (120);

      editVisiteButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.editVisite () ;
         }
      }) ;
      deleteVisiteButton = new Button () ;
      deleteVisiteButton.setText ("Delete...");
      deleteVisiteButton.setPrefWidth (120);

      deleteVisiteButton.setOnAction (new EventHandler<ActionEvent> () {
         @Override
         public void handle (ActionEvent event) {
            control.deleteVisite () ;
         }
      }) ;
      disableActionOnSelection () ;
      buttons.getChildren ().addAll (newVisiteButton, editVisiteButton, deleteVisiteButton) ;
      
      txt.setPrefWidth (360);
      txt.setMaxWidth(360);
      globalPane = new BorderPane () ;
      globalPane.setLeft (visitesList) ;
      globalPane.setBottom (buttons);
      globalPane.setTop(txt);
      globalPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #aed6f1 ,#5499c7)");
      
      setText ("Visites médicales");
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

   public void selectNoVisite () {
      visitesList.getSelectionModel ().clearSelection () ;
      globalPane.setCenter (null) ;
      globalPane.autosize () ;
   }

   //-------------------------------------------------------------------------------------
   // sélection d'un patient : deux méthodes pour être plus précis dans ce que l'on demande
   //-------------------------------------------------------------------------------------

   public void selectVisite (Visite_P currentVisite) {
      GridPane pane = new GridPane() ;
      Rectangle rectangle = new Rectangle () ;
      rectangle.setWidth (500);
      rectangle.setHeight (70);
      rectangle.setFill (Color.ANTIQUEWHITE);
      StackPane stack = new StackPane() ;
      stack.getChildren ().addAll (rectangle,currentVisite) ;
      pane.add (stack, 2, 2);
      globalPane.setCenter (pane);
   }

   public void selectVisite (String currentVisiteName) {
      visitesList.getSelectionModel ().select (currentVisiteName) ;
   }

   //-------------------------------------------------------------------------------------
   // activation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void enableActionOnSelection () {
      editVisiteButton.setDisable (false) ;
      deleteVisiteButton.setDisable (false) ;      
   }

   //-------------------------------------------------------------------------------------
   // désactivation des fonctions d'édition et de suppression
   //-------------------------------------------------------------------------------------
   public void disableActionOnSelection () {
      editVisiteButton.setDisable (true) ;
      deleteVisiteButton.setDisable (true) ;                        
   }

   //-------------------------------------------------------------------------------------
   // désactivation de la fonction de suppression
   //-------------------------------------------------------------------------------------
   public void disableDeleteOnSelection () {
      deleteVisiteButton.setDisable (true) ;      
   }
   public List<String> getNames (){

      
      return this.visitesNames;
      
   }

}
