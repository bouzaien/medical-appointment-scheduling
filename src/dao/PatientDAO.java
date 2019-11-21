package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metier.Doctor;
import metier.Patient;

/**
* DAO associé aux patients.
* Assure les fonctionnalités CRUD : Create, Retrieve, Update et Delete.
* 
* @author Philippe TANGUY - révisé par Thierry Duval pour usage simplifié avec TP IHM
*/

public class PatientDAO {
   
   private DoctorDAO doctorDAO ;

   // -----------------------------------------------------------------------------
   // CREATE
   // -----------------------------------------------------------------------------

   public PatientDAO () {
      doctorDAO = new DoctorDAO () ;
   }

   /**
    * Création d'un nouveau patient dans le stockage.
    * 
    * @param socialSecurityNumber
    * @param gender
    * @param dateOfBirth
    * @param lastName
    * @param firstName
    * @param attachment
    * @param referentDoctor
    * @throws MedicalRecordException
    */

   private Patient create (String socialSecurityNumber, String gender, String dateOfBirth, String lastName,
         String firstName, Patient attachment, Doctor referentDoctor) throws MedicalRecordException {
      try {
         String sql = "insert into patient values (DEFAULT, ?, ?, ?, ?, ?, ?, ?)" ;
         Connection conn = DBUtil.getConnection () ;
         
         // Insertion du nouveau patient
         PreparedStatement ps1 = conn.prepareStatement (sql) ;
         ps1.setString (1, socialSecurityNumber) ;
         ps1.setString (2, gender) ;
         ps1.setString (3, dateOfBirth) ;
         ps1.setString (4, lastName) ;
         ps1.setString (5, firstName) ;
         if(attachment == null)
           ps1.setNull(6, java.sql.Types.INTEGER);
         else
           ps1.setInt (6, attachment.getPatientID()) ;
         if(referentDoctor == null)
           ps1.setNull(7, java.sql.Types.INTEGER);
         else
           ps1.setInt (7, referentDoctor.getDoctorID()) ;
         ps1.executeUpdate () ;
         ps1.close () ;
         
         // Récupération de l'ID du nouveau médecin
         
         String sql2 = "select currval('patient_patient_id_seq') as patient_id";
         PreparedStatement ps2 = conn.prepareStatement (sql2);
         ResultSet rs = ps2.executeQuery();
         rs.next();
         int patientID = rs.getInt("patient_id");

         conn.close () ;
         return new Patient (patientID, socialSecurityNumber, gender, dateOfBirth, lastName, firstName, attachment,
               referentDoctor) ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public Patient create (Patient patient) throws MedicalRecordException {
      return create (patient.getSocialSecurityNumber (),
                     patient.getGender (),
                     patient.getDateOfBirth (),
                     patient.getLastName (),
                     patient.getFirstName (),
                     patient.getAttachment (),
                     patient.getReferentDoctor ()) ;
   }

   // -----------------------------------------------------------------------------
   // RETRIEVE
   // -----------------------------------------------------------------------------

   /**
    * Recherche d'un patient par son ID.
    * 
    * @param patientID
    * @return
    * @throws MedicalRecordException
    */
   public Patient find (int patientID) throws MedicalRecordException {
      try {
         Patient patient = null ;
         String sql = "select * from patient where patient_id=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setInt (1, patientID) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
           patient = new Patient (rs.getInt("patient_id"),
                                   rs.getString ("numsecu"),
                                   rs.getString ("genre"),
                                   rs.getString ("date_naissance"),
                                   rs.getString ("nom"),
                                   rs.getString ("prenom"),
                                   find (rs.getInt ("rattachement")),
                                   doctorDAO.find (rs.getInt("medecin_referent"))); 
                                         
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return patient ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public List<Patient> findAttachedPatients (Patient parent) throws MedicalRecordException {
      try {
         List<Patient> patients = new ArrayList<> () ;
         String sql = "select * from patient where rattachement=? order by nom" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setString (1, parent.getSocialSecurityNumber ()) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            Patient patient = new Patient (rs.getInt("patient_id"),
                                           rs.getString ("numsecu"),
                                           rs.getString ("genre"),
                                           rs.getString ("date_naissance"),
                                           rs.getString ("nom"),
                                           rs.getString ("prenom"),
                                           find (rs.getInt("rattachement")),
                                           doctorDAO.find (rs.getInt("medecin_referent"))) ;
            patients.add (patient) ;
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return patients ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public List<Patient> findAll () throws MedicalRecordException {
      try {
         List<Patient> patients = new ArrayList<> () ;
         String sql = "select * from patient order by nom" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            Patient patient = new Patient (rs.getInt("patient_id"),
                                           rs.getString ("numsecu"),
                                           rs.getString ("genre"),
                                           rs.getString ("date_naissance"),
                                           rs.getString ("nom"),
                                           rs.getString ("prenom"),
                                           (rs.getInt("rattachement") == 0 ? null : find (rs.getInt("rattachement"))), // Valeur 0 correspond à la valeur null dans la BD
                                           (rs.getInt ("medecin_referent") == 0 ? null : doctorDAO.find (rs.getInt("medecin_referent")))) ; // Valeur 0 correspond à la valeur null dans la BD
            patients.add (patient) ;
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return patients ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   // -----------------------------------------------------------------------------
   // UPDATE
   // -----------------------------------------------------------------------------

   /**
    * Mise à jour dans le stockage des informations d'un patient.
    * 
    * @param socialSecurityNumber
    * @param gender
    * @param dateOfBirth
    * @param lastName
    * @param firstName
    * @param attachment
    * @param referentDoctor
    */
   private Patient update (int patientID, String socialSecurityNumber, String gender, String dateOfBirth, String lastName,
         String firstName, Patient attachment, Doctor referentDoctor) throws MedicalRecordException {
      try {
         String sql = "update patient set numsecu=?, genre=?, nom=?, prenom=?, date_naissance=?, rattachement=?, medecin_referent=? where patient_id=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setString (1, socialSecurityNumber) ;
         ps.setString (2, gender) ;
         ps.setString (3, lastName) ;
         ps.setString (4, firstName) ;
         ps.setString (5, dateOfBirth) ;
         if(attachment == null)
           ps.setNull(6, java.sql.Types.INTEGER);
         else
           ps.setInt (6, attachment.getPatientID()) ;
         if(referentDoctor == null)
           ps.setNull(7, java.sql.Types.INTEGER);
         else
           ps.setInt (7, referentDoctor.getDoctorID()) ;
         ps.setInt(8, patientID);
         ps.executeUpdate () ;
         ps.close () ;
         conn.close () ;
         return new Patient (patientID, socialSecurityNumber, gender, lastName, firstName, dateOfBirth, attachment,
               referentDoctor) ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public Patient update (Patient patient) throws MedicalRecordException {
      return update (patient.getPatientID(), patient.getSocialSecurityNumber (), patient.getGender (), patient.getDateOfBirth (),
            patient.getLastName (), patient.getFirstName (), patient.getAttachment (), patient.getReferentDoctor ()) ;
   }

   // -----------------------------------------------------------------------------
   // DELETE
   // -----------------------------------------------------------------------------

   private void delete (String socialSecurityNumber) throws MedicalRecordException {
      try {
         String sql = "delete from patient where numsecu=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setString (1, socialSecurityNumber) ;
         ps.executeUpdate () ;
         ps.close () ;
         conn.close () ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public void delete (Patient patient) throws MedicalRecordException {
      delete (patient.getSocialSecurityNumber ()) ;
   }

}
