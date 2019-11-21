package dao;

import java.sql.Connection ;
import java.util.Date ;
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.util.ArrayList ;
import java.util.List ;


import rdv.Prescription ;
import rdv.Visite ;

public class PrescriptionDAO {
   private static DoctorDAO doctorDAO ;
   private static PatientDAO patientDAO ;
   
   public PrescriptionDAO () {
      doctorDAO = new DoctorDAO () ;
      patientDAO = new PatientDAO();
   }
   
   private Prescription create (String medicament, Visite visite, Date date, int duree, float posologie, String modalites) throws MedicalRecordException {
      try {
        
     
            
            
         String sql = "insert into prescription values (DEFAULT, ?, ?, ?, ?, ?, ?, ?)" ;
         Connection conn = DBUtil.getConnection () ;
         
         // Insertion du nouveau patient
         PreparedStatement ps1 = conn.prepareStatement (sql) ;
         ps1.setDate (4, (java.sql.Date)date ) ;
         ps1.setFloat (6, posologie) ;
         if(visite == null) 
           ps1.setNull(3, java.sql.Types.INTEGER);
         else 
           ps1.setInt (3, visite.getPatient().getPatientID()) ;
         if(visite == null) 
           ps1.setNull(2, java.sql.Types.INTEGER);
         else 
           ps1.setInt (2, visite.getDoctor().getDoctorID()) ;
         ps1.setString (1, medicament) ;
         ps1.setInt (5, duree) ;
         ps1.setString (7, modalites) ;
         ps1.executeUpdate () ;
         ps1.close () ;
         
         String sql2 = "select currval('prescription_prescription_id_seq') as prescription_id";
         PreparedStatement ps2 = conn.prepareStatement (sql2);
         ResultSet rs = ps2.executeQuery();
         rs.next();
         int prescriptionID = rs.getInt("prescription_id");
         conn.close();
         
         return new Prescription (prescriptionID, medicament, visite, date, duree, posologie, modalites);
      }
         
       catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }
   
  public Prescription create (Prescription prescription) throws MedicalRecordException {
     
    
      return create (prescription.getMedicament (),
                     prescription.getVisite (),
                     prescription.getDate (),
                     prescription.getDuree (),
                     prescription.getPosologie (),
                     prescription.getModalites ()) ;
                     
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
   public static Prescription find (int prescriptionID) throws MedicalRecordException {
      try {
         Prescription prescription = null ;
         String sql = "select * from prescription where prescription_id=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setInt (1, prescriptionID) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            prescription = new Prescription (rs.getInt("prescription_id"),
                                   rs.getString ("medicament"),
                                   VisiteDAO.find (rs.getDate("date_visite"), patientDAO.find(rs.getInt ("patient")), doctorDAO.find (rs.getInt("medecin"))),
                                   rs.getDate ("date_visite"),
                                   rs.getInt ("duree"),
                                   rs.getFloat ("posologie"),
                                   rs.getString ("modalites"));                               
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return prescription ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   

   public List<Prescription> findAll () throws MedicalRecordException {
      try {
         List<Prescription> prescriptions = new ArrayList<> () ;
         String sql = "select * from prescription order by date_visite" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            Prescription prescription = new Prescription(rs.getInt ("prescription_id"),
                  rs.getString ("medicament"),
                  (rs.getInt("medecin")*rs.getInt ("patient") == 0 ? null : VisiteDAO.find (rs.getDate ("date_visite"), patientDAO.find(rs.getInt ("patient")), doctorDAO.find (rs.getInt("medecin")))),
                  rs.getDate ("date_visite"),
                  rs.getInt ("duree"),
                  rs.getFloat ("posologie"),
                  rs.getString ("modalites"));
            prescriptions.add(prescription) ;
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return prescriptions ;
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
   private Prescription update (int prescriptionID, String medicament, Visite visite, Date date, int duree, float posologie, String modalites) throws MedicalRecordException {
      try {
         String sql = "update visite set medicament=?, medecin=?, patient=?, date_visite=?, duree=?, posologie=?, modalites=? where prescription_id=? " ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps1 = conn.prepareStatement (sql) ;
         
         ps1.setString (4, "to_date('" + date + "','dd/mm/yyyy')") ;
         ps1.setFloat (6, posologie) ;
         if(visite.getPatient () == null) 
           ps1.setNull(3, java.sql.Types.INTEGER);
         else 
           ps1.setInt (3, visite.getPatient ().getPatientID()) ;
         if(visite.getDoctor () == null) 
           ps1.setNull(2, java.sql.Types.INTEGER);
         else 
           ps1.setInt (2, visite.getDoctor ().getDoctorID()) ;
         ps1.setString (1, medicament) ;
         ps1.setInt (5, duree) ;
         ps1.setString (7, modalites) ;
         ps1.setInt (8, prescriptionID);
         
         ps1.executeUpdate () ;
         ps1.close () ;
         conn.close () ;
         return new Prescription (prescriptionID, medicament, visite, date, duree, posologie, modalites) ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public Prescription update (Prescription prescription) throws MedicalRecordException {
      return update (prescription.getPrescriptionID(),
            prescription.getMedicament (),
            prescription.getVisite (),
            prescription.getDate (),
            prescription.getDuree (),
            prescription.getPosologie (),
            prescription.getModalites ()) ;
   }

   // -----------------------------------------------------------------------------
   // DELETE
   // -----------------------------------------------------------------------------

   private void delete (int prescriptionID) throws MedicalRecordException {
      try {
         String sql = "delete from prescription where prescription_id=?" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ps.setInt (1, prescriptionID) ;
         ps.executeUpdate () ;
         ps.close () ;
         conn.close () ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

   public void delete (Prescription prescription) throws MedicalRecordException {
      delete (prescription.getPrescriptionID()) ;
   }
}
