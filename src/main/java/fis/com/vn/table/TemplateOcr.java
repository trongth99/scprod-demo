/**
 * 
 */
package fis.com.vn.table;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Entity
@Table(name = "template_ocr")
@Data
public class TemplateOcr {
	@Id
	long id;
	
	@Column(name = "ten")
	@Nationalized
	String ten ;
	
	@Column(name = "noi_dung")
	@Nationalized
	String noiDung ;
	
	@Column(name = "mo_ta")
	@Nationalized
	String moTa ;
	
	@Column(name = "nguoi_tao")
	long nguoiTao ;
	
	@Column(name = "ngay_tao")
	Date ngayTao ;
	
	@Column(name = "loai")
	Integer loai ;
	
	@Column(name = "quy_dinh")
	@Nationalized
	String quyDinh ;
}
