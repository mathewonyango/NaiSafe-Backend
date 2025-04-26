package com.safenai.safenai.repository;

import com.safenai.safenai.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByUserId(Long userId);
    
    @Query(value = "SELECT * FROM incidents i WHERE " +
            "ST_DistanceSphere(ST_MakePoint(:longitude, :latitude), ST_MakePoint(i.longitude, i.latitude)) <= :radius AND " +
            "i.timestamp >= :startTime ORDER BY i.timestamp DESC", 
            nativeQuery = true)
    List<Incident> findNearbyIncidents(@Param("latitude") Double latitude, 
                                      @Param("longitude") Double longitude, 
                                      @Param("radius") Double radiusInMeters,
                                      @Param("startTime") LocalDateTime startTime);
    
    @Query(value = "SELECT * FROM incidents WHERE timestamp >= :startTime ORDER BY timestamp DESC", 
            nativeQuery = true)
    List<Incident> findRecentIncidents(@Param("startTime") LocalDateTime startTime);
    
    @Query(value = "SELECT COUNT(*), DATE_TRUNC('day', timestamp) as day FROM incidents " +
            "WHERE timestamp >= :startDate AND timestamp <= :endDate " +
            "GROUP BY DATE_TRUNC('day', timestamp) ORDER BY day",
            nativeQuery = true)
    List<Object[]> getIncidentCountByDay(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    @Query(value = "SELECT * FROM incidents WHERE " +
            "severity = 'HIGH' AND timestamp >= :startTime " +
            "ORDER BY timestamp DESC LIMIT 10", 
            nativeQuery = true)
    List<Incident> findRecentHighSeverityIncidents(@Param("startTime") LocalDateTime startTime);
    //get incident by id
    @Query(value = "SELECT * FROM incidents WHERE id = :id", nativeQuery = true)
    Incident getIncidentById(@Param("id") Long id);
}