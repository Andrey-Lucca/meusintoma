package br.com.meusintoma.modules.chronicdisease.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.chronicdisease.entity.DiseaseTypeEntity;
import br.com.meusintoma.modules.chronicdisease.enums.ApprovalStatus;

public interface DiseaseTypeRepository extends JpaRepository<DiseaseTypeEntity, UUID> {

    @Query("""
                SELECT CASE WHEN COUNT(dt) > 10 THEN true ELSE false END
                FROM diseaseType dt
                WHERE dt.patientId = :patientId
                  AND dt.approvalStatus = :status
            """)
    public boolean isPatientMoreThanTenRequestPending(@Param("patientId") UUID patientId,
            @Param("status") ApprovalStatus status);
}
