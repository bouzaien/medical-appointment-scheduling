package metier ;
/**
 * Representation informatique d'un medecin.
 * 
 * @author Philippe TANGUY
 */
public class Doctor
{
  private int    doctorID;
  private String rpps;  // RPPS number is unique: Répertoire Partagé des Professionnels de Santé
  private String lastName;
  private String firstName;
  private String address;
  private String phoneNumber;
  private String speciality;
  
  public Doctor() { }
  public Doctor(int doctorID,
                String rpps,
                String lastName, String firstName,
                String address, String phoneNumber,
                String speciality)
  {
    this.doctorID    = doctorID;
    this.rpps        = rpps;
    this.lastName    = lastName;
    this.firstName   = firstName;
    this.address     = address;
    this.phoneNumber = phoneNumber;
    this.speciality  = speciality;
  }

  public int    getDoctorID()    { return doctorID;    }
  public String getRpps()        { return rpps;        }
  public String getLastName()    { return lastName;    }
  public String getFirstName()   { return firstName;   }
  public String getAddress()     { return address;     }
  public String getPhoneNumber() { return phoneNumber; }
  public String getSpeciality()  { return speciality;  }

  public void setDoctorID(int doctor_id)         { this.doctorID   = doctor_id;    }
  public void setRpps(String rpps)               { this.rpps        = rpps;        }
  public void setLastName(String lastName)       { this.lastName    = lastName;    }
  public void setFirstName(String firstName)     { this.firstName   = firstName;   }
  public void setAddress(String address)         { this.address     = address;     }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
  public void setSpeciality(String speciality)   { this.speciality  = speciality;  }

  @Override
  public String toString()
  {
     return "Doctor [rpps=" + rpps + ", lastName=" + lastName + ", firstName=" + firstName
           + ", address=" + address + ", phoneNumber=" + phoneNumber
           + ", speciality=" + speciality + "]";
  } 
}
