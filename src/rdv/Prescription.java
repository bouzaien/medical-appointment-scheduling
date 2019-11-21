package rdv;

import java.util.Date ;

public class Prescription {
   private int prescriptionID;
   private String medicament;
   private Visite visite;
   private Date date;
   private int duree;
   private float posologie;
   private String modalites;
   
   public Prescription (int prescriptionID, String medicament, Visite visite, Date date, int duree, float posologie, String modalites) {
      this.prescriptionID = prescriptionID;
      this.medicament = medicament;
      this.visite = visite;
      this.date = date;
      this.duree = duree;
      this.posologie = posologie;
      this.modalites = modalites;
   }
   public Prescription () {
      this.prescriptionID=-1 ;
      this.medicament = new String();
      this.visite = new Visite();
      this.date = new Date();
      this.duree = -1;
      this.posologie = -1;
      this.modalites = new String();
   }

   public int getPrescriptionID () {
      return prescriptionID ;
   }

   public void setPrescriptionID (int prescriptionID) {
      this.prescriptionID = prescriptionID ;
   }

   public String getMedicament () {
      return medicament ;
   }

   public void setMedicament (String medicament) {
      this.medicament = medicament ;
   }

   public Visite getVisite () {
      return visite ;
   }

   public void setVisite (Visite visite) {
      this.visite = visite ;
   }

   public Date getDate () {
      return date ;
   }

   public void setDate (Date date) {
      this.date = date ;
   }

   public int getDuree () {
      return duree ;
   }

   public void setDuree (int duree) {
      this.duree = duree ;
   }

   public float getPosologie () {
      return posologie ;
   }

   public void setPosologie (float posologie) {
      this.posologie = posologie ;
   }

   public String getModalites () {
      return modalites ;
   }

   public void setModalites (String modalites) {
      this.modalites = modalites ;
   }

}
