package rdv;

import java.time.LocalDate ;

import java.util.Date ;

import metier.Doctor ;
import metier.Patient ;

public class Visite
{
   private Doctor doctor;
   private Patient patient ;
   private float price;
   private Date date;
   
   public Visite () {
      date = new Date() ;
      price = 0 ;
   }

   public Visite(Doctor doctor, Patient patient, float price, Date date)
   {
     this.doctor = doctor;
     this.patient = patient;
     this.price = price;
     this.date = date;
    }
   
   public Doctor getDoctor () {
      return doctor ;
   }

   public void setDoctor (Doctor doctor) {
      this.doctor = doctor ;
   }

   public Patient getPatient () {
      return patient ;
   }

   public void setPatient (Patient patient) {
      this.patient = patient ;
   }

   public float getPrice () {
      return price ;
   }

   public void setPrice (float price) {
      this.price = price ;
   }

   public Date getDate () {
      return date ;
   }

   public void setDate (Date date) {
      this.date = date ;
   }
   public String getHour () {
      return null;
   }

   public int getPatientID () {
      return patient.getPatientID () ;
   }
   
}
