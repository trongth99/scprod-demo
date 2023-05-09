package fis.com.vn.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class TaiLieuKySo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255, message = "Tên đăng nhập dài không quá 255 ký tự")
	@Column(name = "username", length = 255)
	@NotBlank(message = "Tên đăng nhập không được để trống")
	String username;

	@Size(max = 255, message = "Mật khẩu dài không quá 255 ký tự")
	@Column(name = "password", length = 255)
	@NotBlank(message = "Mật khẩu không được để trống")
	String password;

	@Size(max = 255, message = "Số hợp đồng dài không quá 255 ký tự")
	@Column(name = "soHD", length = 255)
	@NotBlank(message = "Số hợp đồng không được để trống")
	String soHD;

	@Column(name = "noiDungFileKy")
	String noiDungFileKy;

	@Column(name = "trangThai")
	String trangThai;

}
