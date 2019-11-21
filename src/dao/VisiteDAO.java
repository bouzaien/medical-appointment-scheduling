package dao;

import java.sql.Connection ;

import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.time.LocalDate ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Date;
import metier.Doctor ;
import metier.Patient ;
import presentation.Visite_ActiveP ;
import rdv.Visite ;

public class VisiteDAO {
   private static DoctorDAO doctorDAO ;
   private static PatientDAO patientDAO ;

   
   public VisiteDAO () {
      doctorDAO = new DoctorDAO () ;
      patientDAO = new PatientDAO();
   }
   
   
   private Visite create (Date date, float price, Patient patient, Doctor doctor) throws MedicalRecordException {
      try {
       
      

           /*String [] datesTable = date.split ("/") ;
            int dayOfMonth = Integer.parseInt (datesTable [0]) ; 
            System.out.println ("ok") ;
            int month = Integer.parseInt (datesTable [1]) ; 
            int year = Integer.parseInt (datesTable [2]) ; 
            
            LocalDate datee = LocalDate.of (year, month, dayOfMonth);*/
     
            
            
         String sql = "insert into visite values (?, ?, ?, ?)";
         Connection conn = DBUtil.getConnection () ;
         
         // Insertion du nouveau patient
         PreparedStatement ps1 = conn.prepareStatement (sql) ;
         
         ps1.setDate (3, (java.sql.Date)date);
        
         
         ps1.setFloat (4, price) ;
         
         if(patient == null) 
            ps1.setNull(2, java.sql.Types.INTEGER);
         else 
           ps1.setInt (2, patient.getPatientID()) ;
         if(doctor == null) 
           ps1.setNull(1, java.sql.Types.INTEGER);
         else 
           ps1.setInt (1, doctor.getDoctorID()) ;
         ps1.executeUpdate () ;
         ps1.close () ;
         
         conn.close () ;
         return new Visite (doctor, patient, price, date);
      }
         
       catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }
  public Visite create (Visite visite) throws MedicalRecordException {
     /*String [] datesTable = visite.getDate ().split ("/") ;
     int dayOfMonth = Integer.parseInt (datesTable [0]) ; 
     int month = Integer.parseInt (datesTable [1]) ; 
     int year = Integer.parseInt (datesTable [2]) ; 
     
     LocalDate datee = LocalDate.of (year, month, dayOfMonth);*/
    
      return create (visite.getDate (),
                     visite.getPrice (),
                     visite.getPatient(),
                     visite.getDoctor ()) ;
                     
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
   public static Visite find (Date date, Patient patient, Doctor doctor) throws MedicalRecordException {
      try {
         Visite visite = null ;
         String sql = "select * from visite where medecin=? OR patient=? OR date_visite=?" ;
         
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setInt (1, (int)doctor.getDoctorID());
         ps.setInt (2, (int)patient.getPatientID());
         ps.setDate(3, (java.sql.Date)date );
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {

            
            visite = new Visite ( doctorDAO.find (rs.getInt("medecin")),
                                  
                                   
                                   patientDAO.find(rs.getInt ("patient")),
                                   
                                   rs.getFloat("prix"),
                                   rs.getDate ("date_visite")
                                  ); 
                                         
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return visite ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   

   public List<Visite> findAll () throws MedicalRecordException {
      try {
         List<Visite> visites = new ArrayList<> () ;
         String sql = "select * from visite order by date_visite" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            Visite visite = new Visite( 
                                           (rs.getInt("medecin") == 0 ? null : doctorDAO.find (rs.getInt("medecin"))), // Valeur 0 correspond à la valeur null dans la BD
                                           (rs.getInt ("patient") == 0 ? null : patientDAO.find (rs.getInt("patient"))), // Valeur 0 correspond à la valeur null dans la BD
                                           rs.getFloat ("prix"),
                                           rs.getDate ("date_visite"))
                                           ;
            visites.add (visite) ;
            
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return visites ;
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
   private Visite update (Doctor doctor, Patient patient, float price, Date date) throws MedicalRecordException {
      try {
         String sql = "update visite set prix=? where medecin = ? and patient = ? and date_visite=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setFloat (1, price) ;
         
         ps.setDate (4, (java.sql.Date) date) ;
         
         if(doctor == null)
           ps.setNull(2, java.sql.Types.INTEGER);
         else
           ps.setInt (2, doctor.getDoctorID()) ;
         if(patient == null)
           ps.setNull(3, java.sql.Types.INTEGER);
         else
           ps.setInt (3, patient.getPatientID()) ;
         
         ps.executeUpdate () ;
         ps.close () ;
         conn.close () ;
         return new Visite (doctor, patient, price, date) ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public Visite update (Visite visite) throws MedicalRecordException {
      return update (visite.getDoctor(), visite.getPatient (), visite.getPrice (), visite.getDate ()) ;
   }

   // -----------------------------------------------------------------------------
   // DELETE
   // -----------------------------------------------------------------------------

   private void delete (Doctor doctor, Patient patient, Date date) throws MedicalRecordException {
      try {
         String sql = "delete from visite where medecin=? and patient=? and date_visite=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setInt (1, doctor.getDoctorID()) ;
         ps.setInt (2, patient.getPatientID()) ;
         ps.setDate (3, (java.sql.Date) date) ;
         ps.executeUpdate () ;
         ps.close () ;
         conn.close () ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public void delete (Visite visite) throws MedicalRecordException {
      delete (visite.getDoctor (), visite.getPatient (), visite.getDate ()) ;
   }

}
