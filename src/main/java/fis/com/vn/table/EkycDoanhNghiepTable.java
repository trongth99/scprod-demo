package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "ekyc_doanh_nghiep")
//@Table(name = "ekyc_doanh_nghiep", indexes = { 
//		@Index(name = "IDX_USERNAME", columnList = "username")
//		//@Index(name = "IDX_FULLNAME", columnList = "full_name"),
//		//@Index(name = "IDX_EMAIL", columnList = "email"), 
//		//@Index(name = "IDX_STATUS", columnList = "status") 
//		})
@Data
public class EkycDoanhNghiepTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	@Column(name = "noi_dung")
	@Nationalized
	private String noiDung;

	@Column(name = "check_noi_dung")
	@Nationalized
	private String checkNoiDung;

	@Column(name = "ss_noi_dung")
	@Nationalized
	private String ssNoiDung;

	@Column(name = "ma_doanh_nghiep")
	@Nationalized
	String maDoanhNghiep;

	@Column(name = "ten_doanh_nghiep")
	@Nationalized
	private String tenDoanhNghiep;

	@Column(name = "ngay_tao")
	Date ngayTao;

	@Column(name = "token")
	@Nationalized
	String token;

	@Column(name = "status")
	@Nationalized
	String status;

	@Column(name = "status_don_ky")
	@Nationalized
	String statusDonKy;

	@Column(name = "tenNguoiQuanLy")
	@Nationalized
	String tenNguoiQuanLy;

	@Column(name = "emailNguoiLienHe")
	@Nationalized
	String emailNguoiLienHe;

	@Column(name = "soDienThoaiNguoiLienHe")
	@Nationalized
	String soDienThoaiNguoiLienHe;

	@Column(name = "tenNguoiLienHe")
	@Nationalized
	String tenNguoiLienHe;

	@Column(name = "step")
	@Nationalized
	String step;

	@Nationalized
	@Size(max = 255, message = "Tên đăng nhập dài không quá 255 ký tự")
	@Column(name = "username", length = 30)
	String username;

	@Nationalized
	@Size(max = 255, message = "Tên đăng nhập dài không quá 255 ký tự")
	@Column(name = "emailLogin", length = 30)
	String emailLogin;

	@Nationalized

	@Column(name = "checkSum")

	String checkSum;
}
