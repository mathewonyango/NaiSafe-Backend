package com.safenai.safenai.repository;

import com.safenai.safenai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
    Optional<User> findByGoogleId(String googleId);
    
    Optional<User> findByFacebookId(String facebookId);
    
    @Query(value = "SELECT * FROM users u WHERE " +
            "ST_DistanceSphere(ST_MakePoint(:longitude, :latitude), ST_MakePoint(u.home_longitude, u.home_latitude)) <= u.alert_radius * 1000 OR " +
            "ST_DistanceSphere(ST_MakePoint(:longitude, :latitude), ST_MakePoint(u.work_longitude, u.work_latitude)) <= u.alert_radius * 1000", 
            nativeQuery = true)
    List<User> findUsersInAlertRadius(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
   
}