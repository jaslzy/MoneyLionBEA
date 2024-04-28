package com.monlio.featureswitch.DTO;

public class FeatureDTO {

    private String useremail;
    private String featurename;
    private Boolean canAccess; //private boolean canAccess

    public FeatureDTO(String useremail, String featurename, Boolean canAccess){
        this.useremail = useremail;
        this.featurename = featurename;
        this.canAccess = canAccess;
    }

    public void setUserEmail(String useremail){
        this.useremail = useremail;
    }

    public String getUserEmail(){
        return this.useremail;
    }

    public void setFeatureName(String featurename){
        this.featurename = featurename;
    }

    public String getFeatureName(){
        return this.featurename;
    }

    public void setCanAccess(Boolean canAccess){
        this.canAccess = canAccess;
    }

    public Boolean getCanAccess(){
        return this.canAccess;
    }
}

