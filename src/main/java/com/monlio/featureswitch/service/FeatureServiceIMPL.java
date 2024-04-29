package com.monlio.featureswitch.service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.net.InetAddress;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monlio.featureswitch.DTO.FeatureDTO;
import com.monlio.featureswitch.entity.Feature;
import com.monlio.featureswitch.repository.FeatureRepo;

@Service
public class FeatureServiceIMPL implements FeatureService{

    // final private static Long defaultID = -1L; // This means -1
    // private Long idOfRecord = defaultID; 

    @Autowired
    private FeatureRepo featureRepo;

    @Override
    public boolean checkAccess(String email, String featurename){
        if(hasFailed_Get_Checks(email,featurename)){return false;}

        Optional<Feature> hasRecord = featureRepo.getExistingRecord(email, featurename);
        boolean isExisting = (hasRecord.isPresent());
        if (isExisting){
            return hasRecord.get().getCanAccess();
        }

        return false;
    }

    public boolean hasFailed_Get_Checks(String email, String featurename){
        if(hasFailedEmailCheck(email)){return true;}
        else if(hasFailedFeatureName(featurename)){return true;}

        return false;
    }

    @Override
    public boolean addOrUpdate_UserWithFeature(FeatureDTO featureDTO) {
        if(hasFailed_AddOrUpdate_Checks(featureDTO)){return false;}
       

        Feature feature = new Feature(
            featureDTO.getUserEmail(),
            featureDTO.getFeatureName(),
            featureDTO.getCanAccess()
        );

        // If true, then (to update the existing record): we shall retrieve and reuse the same id, instead of do nothing and end up with duplicate "email and featurename".
        // if false, then we shall do nothing (meaning we create a new record). 
        Optional<Feature> hasRecord = featureRepo.getExistingRecord(featureDTO.getUserEmail(),featureDTO.getFeatureName());
        boolean isExisting = (hasRecord.isPresent());
        if (isExisting){
            Long id = hasRecord.get().getId();
            feature.setID(id);
        }
        /*if(isExisting(featureDTO.getUserEmail(),featureDTO.getFeatureName())){
            Long id = featureRepo.getIDofRecord(featureDTO.getUserEmail(),featureDTO.getFeatureName());
            feature.setID(id);
        }
        */

        Feature savedFeature = featureRepo.save(feature);
        
        boolean hasDBUpdateSucceed = (savedFeature.getId() != null);
        return hasDBUpdateSucceed;
       
    }

    public boolean hasFailed_AddOrUpdate_Checks(FeatureDTO featureDTO){
        if(hasFailedEmailCheck(featureDTO.getUserEmail())){return true;}
        else if(hasFailedFeatureName(featureDTO.getFeatureName())){return true;}
        else if(hasFailedAccess(featureDTO.getCanAccess())){return true;}
        //else if(hasDuplicateRecord(featureDTO.getUserEmail(), featureDTO.getFeatureName())){return true;}

        return false;
    }

    public boolean isExisting(String email, String featurename){
        Optional<Feature> hasRecord = featureRepo.getExistingRecord(email, featurename);
        boolean isExisting = (hasRecord.isPresent());//boolean hasDuplicate = (hasRecord.isPresent());

        return isExisting;//return hasDuplicate;
    }

    public boolean hasFailedAccess(Boolean canAccess){
        if(canAccess == null){return true;}

        return false;
    }

    public boolean hasFailedFeatureName(String featurename){
        if(featurename == null || featurename.isEmpty() || featurename.trim().isEmpty() || featurename.length() > 255) {return true;}

        return false;
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean hasFailedEmailCheck(String email){
        if(email == null || email.length() > 255) {return true;}

        //start - Checks for format. Example: john-doe@example.com is invalid format. johndoe@example.com has valid format.
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        boolean hasInvalidFormat = (!matcher.matches());
        if(hasInvalidFormat){return true;}
        //end - Checks for format.
        
        
        /*//start - Checks for dns. Example: johndoe@notadomain123456789.com has invalid dns. johndoe@gmail.com has valid dns.
        try {
            InetAddress.getByName(email);

        } catch (Exception e) {
            return true;
        }
        //end - Checks for dns.
        */

        /*//javax.mail.internet.InternetAddress follows the email address specification defined in RFC 822 and RFC 5322, ensuring adherence to standard email address formats.
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (Exception ex) {
            return false;
        }*/

        return false;
    }
    
}
