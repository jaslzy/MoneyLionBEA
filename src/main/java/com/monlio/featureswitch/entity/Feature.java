package com.monlio.featureswitch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "feature")
public class Feature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "user_email")
    private String useremail;

    @Column (name = "feature_name")
    private String featurename;

    @Column (name = "canAccess")
    private boolean canAccess;

    public Feature(){
    }

    public Feature(String useremail, String featurename, boolean canaccess){
        this.useremail = useremail;
        this.featurename = featurename;
        this.canAccess = canaccess;
    }

    public void setID(Long id){
        this.id = id;
    }

    public Long getId() {
        return this.id;
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

    public void setCanAccess(boolean canAccess){
        this.canAccess = canAccess;
    }

    public boolean getCanAccess(){
        return this.canAccess;
    }
}
