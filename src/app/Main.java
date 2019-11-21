package app ;

import controle.PatientsRecord_C;
import controle.PrescriptionsRecord_C ;
import controle.DoctorRecord_C;
import controle.VisitesRecord_C;
import dao.DBUtil ;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane ;
import javafx.stage.Stage;
import metier.PatientsRecord;
import presentation.PatientsRecord_P ;
import presentation.PrescriptionsRecord_P ;
import metier.DoctorsRecord;
import presentation.DoctorsRecord_P ;
import rdv.PrescriptionsRecord ;
import rdv.VisitesRecord;
import presentation.VisitesRecord_P ;

public class Main extends Application {

   public static void main (String [] args) {
      DBUtil.setBase ("soins") ;
      DBUtil.setHost ("localhost") ;
      DBUtil.setNumPort ("5432") ;
      DBUtil.setUsername ("user") ;
      DBUtil.setPassword ("usr") ;
      launch (args) ;
   }

   @Override
   public void start (Stage primaryStage) throws Exception {
            
      PatientsRecord_C patientsRecord_C = new PatientsRecord_C (new PatientsRecord (), new PatientsRecord_P (primaryStage)) ;
      DoctorRecord_C doctorsRecord_C = new DoctorRecord_C (new DoctorsRecord (), new DoctorsRecord_P (primaryStage)) ;
      VisitesRecord_C visitesRecord_C = new VisitesRecord_C (new VisitesRecord () , new VisitesRecord_P (primaryStage)) ;
      PrescriptionsRecord_C prescriptionsRecord_C = new PrescriptionsRecord_C (new PrescriptionsRecord () , new PrescriptionsRecord_P (primaryStage)) ;
      // container principal de notre IMH : un TabPane pour accueillir des onglets
      // idéalement il faudrait faire une classe nommé MedicalRecord chargée de l'ensemble du projet 
      TabPane peopleTabPane = new TabPane () ;
      
      // ajout d'un premier onglet dans le TabPane
      peopleTabPane.getTabs ().addAll (patientsRecord_C.getPresentation ()) ;
      peopleTabPane.getTabs ().addAll (doctorsRecord_C.getPresentation());
      peopleTabPane.getTabs ().addAll (visitesRecord_C.getPresentation ()) ;
      peopleTabPane.getTabs ().addAll (prescriptionsRecord_C.getPresentation ());

      
      // association de la scène contenant le container principal en tant que scène de l'application
      primaryStage.setScene (new Scene (peopleTabPane)) ;
      primaryStage.setTitle ("Projet - BD IHM") ;
      primaryStage.setWidth (1200) ;
      primaryStage.show () ;
   }

}
