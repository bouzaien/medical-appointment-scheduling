package metier ;
/**
 * Representation informatique d'un patient.
 * 
 * @author Philippe TANGUY
 */

//-------------------------------------------------------------------------------------
// Classe métier
//-------------------------------------------------------------------------------------


  public class Patient
  {
    private int     patientID;
    private String  lastName ;
    private String  firstName ;
    private String  socialSecurityNumber ;
    private String  dateOfBirth ;
    private Patient attachment ;
    private Doctor  referentDoctor ;
    private String  gender ;

    
    public Patient () {
       patientID            = -1; // indique que le patient n'est pas stocké dans la base.
       lastName             = new String () ;
       firstName            = new String () ;
       socialSecurityNumber = new String () ;
       gender               = new String () ;
    }

    public Patient(int patientID, String socialSecurityNumber, String gender, String dateOfBirth,
                   String lastName, String firstName,
                   Patient attachment, Doctor referentDoctor)
    {
      this.patientID            = patientID;
      this.lastName             = lastName;
      this.firstName            = firstName;
      this.socialSecurityNumber = socialSecurityNumber;
      this.gender               = gender ;
      this.dateOfBirth          = dateOfBirth;
      this.attachment           = attachment;
      this.referentDoctor       = referentDoctor;
    }
    
    public int     getPatientID()            { return patientID;            }
    public String  getLastName()             { return lastName;             }
    public String  getFirstName()            { return firstName;            }
    public String  getSocialSecurityNumber() { return socialSecurityNumber; }
    public String  getDateOfBirth()             { return dateOfBirth;          }
    public Patient getAttachment()           { return attachment;           }
    public Doctor  getReferentDoctor()       { return referentDoctor;       }
    public String  getGender()               { return gender ;              }

    public void setPatientID(int patientID)                          { this.patientID = patientID;                       }
    public void setLastName(String lastName)                         { this.lastName             = lastName;             }
    public void setFirstName(String firstName)                       { this.firstName            = firstName;            }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }
    public void setDateOfBirth(String dateOfBirth)                   { this.dateOfBirth          = dateOfBirth;          }
    public void setAttachment(Patient attachment)                    { this.attachment           = attachment;           }
    public void setReferentDoctor(Doctor referentDoctor)             { this.referentDoctor       = referentDoctor;       }
    public void setGender (String gender)                            { this.gender               = gender ;              }

    @Override
    public String toString()
    {
       return "Patient [lastName=" + lastName + ", firstName=" + firstName
             + ", socialSecurityNumber=" + socialSecurityNumber + ", date of birth =" + dateOfBirth + ", attachment="
             + attachment + ", referentDoctor=" + referentDoctor + "]";
    }

}
