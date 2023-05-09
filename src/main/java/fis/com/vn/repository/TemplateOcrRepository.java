/**
 * 
 */
package fis.com.vn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.TemplateOcr;

/**
 * @author ChinhVD4
 *
 */
public interface TemplateOcrRepository extends CrudRepository<TemplateOcr, Long>{

	/**
	 * @param code
	 * @return
	 */
	TemplateOcr findByTen(String code);

	/**
	 * @return
	 */
	@Query(value = "SELECT * FROM template_ocr where mo_ta is not null", nativeQuery = true)
	List<TemplateOcr> select();

}
