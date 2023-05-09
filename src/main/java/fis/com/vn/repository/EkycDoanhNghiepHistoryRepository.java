package fis.com.vn.repository;






import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;


import fis.com.vn.table.EkycDoanhNghiepTableHistory;

@Repository
public interface EkycDoanhNghiepHistoryRepository extends CrudRepository<EkycDoanhNghiepTableHistory, Long> {
	
	@Query(value = "select * from ekyc_doanh_nghiep_history e where e.id_dn= ?1",
			countQuery = "select count(1) from ekyc_doanh_nghiep_history e where e.id_dn= ?1", nativeQuery = true)
	Page<EkycDoanhNghiepTableHistory> selectParams(Long id_dn,Pageable pageable );
	
}
