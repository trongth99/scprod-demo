package fis.com.vn.ocr;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OcrDoanhNghiepResp {
	List<DoanhNghiep> data;
	int status;
	String message;
	
	public void add(DoanhNghiep doanhNghiep) {
		if(data == null) data = new ArrayList<>();
		data.add(doanhNghiep);
	}
}
