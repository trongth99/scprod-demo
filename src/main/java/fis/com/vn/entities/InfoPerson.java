package fis.com.vn.entities;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Data;

@Data
public class InfoPerson {
	String id;
	String phone;
	String email;
	String hoVaTen;
	String hoTen;
	String soCmt;
	String namSinh;
	String noiCap;
	String hoKhau;
	String ngayCap;
	String ngayHetHan;
	String anhMatTruoc;
	String anhChuKy;
	String anhMatSau;
	String kiemTra;
	String tokenCheck;
	String checkMain;
	String file;
	String loai;

	String quocTich;
	String visa;
	String maSoThue;
	String tinhTrangCuTru;
	String diaChiNha;
	String mobile;
	String vanPhong;
	String diaChi;
	String email2;
	String time;

	String taoLenh;
	String baoCao;
	String chapThuanLenh;
	String chapThuanLenhDongThoi;
	String hanmuc;
	String hanmuctext;
	String typemoney;
	String hanmucLoai;
	String hanmuctextLoai;
	String typemoneyLoai;
	ArrayList<String> files;

	String editStatus;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoPerson other = (InfoPerson) obj;
		return Objects.equals(tokenCheck, other.tokenCheck);
	}

}
