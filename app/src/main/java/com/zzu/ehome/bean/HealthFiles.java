package com.zzu.ehome.bean;

import java.io.Serializable;

/**
 * Created by Mersens on 2016/11/25.
 */

public class HealthFiles implements Serializable{
    public String maritalStatusNames = "";//婚姻状况
    public String medicineAllergyNames = "";//药物过敏
    public String pastMedicalHistoryNames = "";//既往病史
    public String familyNames = "";
    public String geneticHistoryNmaes = "";//遗传病史
    public String drinkStateNames = "";//喝酒状况
    public String smokeStateNames = "";//吸烟状况
    public String bloodtype="";

    public HealthFiles(){
        this(new Builder());
    }

    public  HealthFiles(Builder builder ){
        this.maritalStatusNames=builder.maritalStatusNames;
        this.medicineAllergyNames=builder.medicineAllergyNames;
        this.pastMedicalHistoryNames=builder.pastMedicalHistoryNames;
        this.familyNames=builder.familyNames;
        this.geneticHistoryNmaes=builder.geneticHistoryNmaes;
        this.drinkStateNames=builder.drinkStateNames;
        this.smokeStateNames=builder.smokeStateNames;
        this.bloodtype=builder.bloodtype;
    }

    @Override
    public String toString() {
        return "HealthFiles{" +
                "maritalStatusNames='" + maritalStatusNames + '\'' +
                ", medicineAllergyNames='" + medicineAllergyNames + '\'' +
                ", pastMedicalHistoryNames='" + pastMedicalHistoryNames + '\'' +
                ", familyNames='" + familyNames + '\'' +
                ", geneticHistoryNmaes='" + geneticHistoryNmaes + '\'' +
                ", drinkStateNames='" + drinkStateNames + '\'' +
                ", smokeStateNames='" + smokeStateNames + '\'' +
                ", bloodtype='" + bloodtype + '\'' +
                '}';
    }
    public static  final class Builder implements Serializable{
        public String maritalStatusNames = "";//婚姻状况
        public String medicineAllergyNames = "";//药物过敏
        public String pastMedicalHistoryNames = "";//既往病史
        public String familyNames = "";
        public String geneticHistoryNmaes = "";//遗传病史
        public String drinkStateNames = "";//喝酒状况
        public String smokeStateNames = "";//吸烟状况
        public String bloodtype="";

        public Builder(){}

        public Builder addMaritalStatus(String maritalStatusNames){
            this.maritalStatusNames=maritalStatusNames;
            return this;
        }

        public  Builder addMedicineAllergy(String medicineAllergyNames){
            this.medicineAllergyNames=medicineAllergyNames;
            return this;
        }

        public Builder addPastMedicalHistory(String pastMedicalHistoryNames){
            this.pastMedicalHistoryNames=pastMedicalHistoryNames;
            return  this;
        }

        public Builder addfamilyNames(String familyNames){
            this.familyNames=familyNames;
            return this;
        }

        public  Builder addGeneticHistory(String geneticHistoryNmaes){
            this.geneticHistoryNmaes=geneticHistoryNmaes;
            return this;

        }

        public  Builder addDrinkStateNames(String drinkStateNames){
            this.drinkStateNames=drinkStateNames;
            return this;
        }

        public Builder addSmokeStateNames(String smokeStateNames){
            this.smokeStateNames=smokeStateNames;
            return this;
        }

        public Builder addBloodtype(String bloodtype){
            this.bloodtype=bloodtype;
            return this;
        }

        public HealthFiles build(){
            return new HealthFiles(this);
        }
    }
}
