package com.monlio.featureswitch.service;

import java.util.List;

import com.monlio.featureswitch.DTO.FeatureDTO;

public interface FeatureService {
    boolean addOrUpdate_UserWithFeature(FeatureDTO featureDTO);

    boolean checkAccess(String email, String featurename);
}
