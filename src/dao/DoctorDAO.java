package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metier.Doctor;
import metier.Patient ;

/**
 * DAO associé aux médecins.
 * Doit assurer les fonctionnalités CRUD : Create, Retrieve, Update et Delete.
 *
 * @author Philippe TANGUY - révisé par Thierry Duval pour usage simplifié avec TP IHM
 */
public class DoctorDAO {

 
private Doctor create (String rpps, String lastName,
        String firstName, String address , String phoneNumber , String speciality ) throws MedicalRecordException {
     try {
        String sql = "insert into medecin values (DEFAULT, ?, ?, ?, ?, ?, ?)" ;
        Connection conn = DBUtil.getConnection () ;
        
        // Insertion du nouveau docteur
        PreparedStatement ps1 = conn.prepareStatement (sql) ;
        ps1.setString (1, rpps) ;
        ps1.setString (2, lastName) ;
        ps1.setString (3, firstName) ;
        ps1.setString (4, address) ;
        ps1.setString (5, phoneNumber) ;
        ps1.setString (6, speciality); 
        ps1.executeUpdate () ;
        ps1.close () ;
        
        // Récupération de l'ID du nouveau médecin
        String sql2 = "select currval('medecin_medecin_id_seq') as medecin_id";
        PreparedStatement ps2 = conn.prepareStatement (sql2);
        ResultSet rs = ps2.executeQuery();
        rs.next();
        int doctorID = rs.getInt("medecin_id");

        conn.close () ;
        return new Doctor (doctorID, rpps, lastName, firstName, address, phoneNumber,
              speciality) ;
     } catch (SQLException sqle) {
        throw new MedicalRecordException (sqle.getMessage ()) ;
     }
  }

  public Doctor create (Doctor doctor) throws MedicalRecordException {
     return create (doctor.getRpps (),
           doctor.getLastName (),
           doctor.getFirstName (),
           doctor.getAddress (),
           doctor.getPhoneNumber (),
           doctor.getSpeciality ());
  }
  
  private static Doctor update (int doctorID,
        String rpps,
        String lastName, String firstName,
        String address, String phoneNumber,
        String speciality) throws MedicalRecordException {
     try {
        String sql = "update medecin set rpps=?, nom=?, prenom=?, adresse=?, telephone=?, specialite=? where medecin_id=?" ;
        Connection conn = DBUtil.getConnection () ;
        PreparedStatement ps = conn.prepareStatement (sql) ;
        ps.setString (1, rpps) ;
        ps.setString (2, lastName) ;
        ps.setString (3, firstName) ;
        ps.setString (4, address) ;
        ps.setString (5, phoneNumber) ;
        ps.setString (6, speciality) ;
        ps.setInt(7, doctorID);
        ps.executeUpdate () ;
        ps.close () ;
        conn.close () ;
        return new Doctor (doctorID, rpps, lastName, firstName, address, phoneNumber, speciality) ;
     } catch (SQLException sqle) {
        throw new MedicalRecordException (sqle.getMessage ()) ;
     }
  }
  
  public static Doctor update (Doctor doctor) throws MedicalRecordException {
     return update (doctor.getDoctorID (), doctor.getRpps (),
           doctor.getLastName (),
           doctor.getFirstName (),
           doctor.getAddress (),
           doctor.getPhoneNumber (),
           doctor.getSpeciality ());
  }
  
//-----------------------------------------------------------------------------
  // DELETE
  // -----------------------------------------------------------------------------

  private void delete (String rpps) throws MedicalRecordException {
     try {
        String sql = "delete from medecin where rpps=?" ;
        Connection conn = DBUtil.getConnection () ;
        PreparedStatement ps = conn.prepareStatement (sql) ;
        ps.setString (1, rpps) ;
        ps.executeUpdate () ;
        ps.close () ;
        conn.close () ;
     } catch (SQLException sqle) {
        throw new MedicalRecordException (sqle.getMessage ()) ;
     }
  }

  public void delete (Doctor doctor) throws MedicalRecordException {
     delete (doctor.getRpps ()) ;
  }


  //-----------------------------------------------------------------------------
  // RETRIEVE
  //-----------------------------------------------------------------------------

   /**
    * Recherche d'un médecin par son ID.
    *
    * @param lastName
    * @param prenom
    * @return
    * @throws MedicalRecordException
    */
    public Doctor find (int doctorID) throws MedicalRecordException {
       try {
          Doctor doctor = null ;
          String sql = "select * from medecin where medecin_id=?" ;
          Connection conn = DBUtil.getConnection () ;
          PreparedStatement ps = conn.prepareStatement (sql) ;
          ps.setInt(1, doctorID) ;
          ResultSet rs = ps.executeQuery () ;
          while (rs.next ()) {
             doctor = new Doctor (rs.getInt("medecin_id"), rs.getString ("rpps"), rs.getString ("nom"), rs.getString ("prenom"),
                   rs.getString ("adresse"), rs.getString ("telephone"), rs.getString ("specialite")) ;
          }
          rs.close () ;
          ps.close () ;
          conn.close () ;
          return doctor ;
       } catch (SQLException sqle) {
          throw new MedicalRecordException (sqle.getMessage ()) ;
       }
    }


   public List<Doctor> findAll () throws MedicalRecordException {
      try {
         List<Doctor> doctors = new ArrayList<> () ;
         String sql = "select * from medecin order by prenom" ;
         Connection conn = DBUtil.getConnection () ;
         PreparedStatement ps = conn.prepareStatement (sql) ;
         ResultSet rs = ps.executeQuery () ;
         while (rs.next ()) {
            Doctor doctor = new Doctor (rs.getInt("medecin_id"), rs.getString ("rpps"), rs.getString ("nom"), rs.getString ("prenom"),
                  rs.getString ("adresse"), rs.getString ("telephone"), rs.getString ("specialite")) ;
            doctors.add (doctor) ;
         }
         rs.close () ;
         ps.close () ;
         conn.close () ;
         return doctors ;
      } catch (SQLException sqle) {
         throw new MedicalRecordException (sqle.getMessage ()) ;
      }
   }

}
