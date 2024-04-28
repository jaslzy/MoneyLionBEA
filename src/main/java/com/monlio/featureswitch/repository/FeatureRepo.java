package com.monlio.featureswitch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monlio.featureswitch.entity.Feature;

@EnableJpaRepositories
@Repository
public interface FeatureRepo extends JpaRepository<Feature,Long>{

    @Query(value = "SELECT * FROM moneylion.feature WHERE user_email = :user_email AND feature_name = :feature_name limit 1;", nativeQuery = true)
    Optional<Feature> getExistingRecord(@Param("user_email") String user_email, @Param("feature_name") String feature_name);

    @Query(value = "SELECT id FROM moneylion.feature WHERE user_email = :user_email AND feature_name = :feature_name;", nativeQuery = true)
    Long getIDofRecord(@Param("user_email") String user_email, @Param("feature_name") String feature_name);

}
