package fis.com.vn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fis.com.vn.table.TaiLieuKySo;

@Repository
public interface TaiLieuKySoRepository extends CrudRepository<TaiLieuKySo, Long> {

}
