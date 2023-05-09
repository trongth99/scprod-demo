package fis.com.vn.ocr;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
public class ThueDnHoCaThe {
	private long id;
	String tin;
	String trangThai;
	Date ngayCap;
	String ten;
	String diaChi;
	String cmt;
	String noiCap;
	Date ngayCapMot;
	String email;
	String sdt;
	BigDecimal von;
	String lhDnghiep;
	String nnkd;
	String ketoanTruong;
	int soLaoDong;
	Date ngayCapGpkd;
	String soGpkd;
	String nguoiDdpl;
}
