/**
 * 
 */
package fis.com.vn.ocr;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Data
public class DoanhNghiep {
	private long id;
	String mst;
	String tenCty;
	String trangThai;
	String ngayCapMst;
	String noiCapMst;
	String diaChi;
	String email;
	String soDienThoai;
	BigDecimal tongVon;
	String loaiHinhDn;
	String giamDoc;
	String soDienThoaiGiamDoc;
	String keToanTruong;
	String sdtKeToanTruong;
	int soLaoDong;
	String soGiayPhepKinhDoanh ;
	String ngayCap;
	String tenNguoiDaiDien;
	String emailNguoiDaiDien;
	String soDienThoaiNguoiDaiDien;
	String nghanhNghe;
	String soGiayToNguoiDaiDien;
	String ngayCapNguoiDaiDien;
	String noiCapNguoiDaiDien;
	int loai;
}
