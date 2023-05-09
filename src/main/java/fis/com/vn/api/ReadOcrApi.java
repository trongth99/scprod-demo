package fis.com.vn.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fis.com.vn.api.entities.Params;
import fis.com.vn.common.FileHandling;
import fis.com.vn.common.StringUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.ReturnRest;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;
import fis.com.vn.ocr.CallApi;
import fis.com.vn.ocr.DoanhNghiep;
import fis.com.vn.ocr.EsignService;
import fis.com.vn.ocr.OcrThongTinDoanhNghiep;
import fis.com.vn.ocr.RespTaxpayer;
import fis.com.vn.ocr.ThongTinDoanhNghiep;
import fis.com.vn.ocr.ThueDnHoCaThe;


@RestController
public class ReadOcrApi  extends BaseApi{
	@Autowired ComponentApi componentApi;
	@Autowired EsignService esignService;
	@Autowired OcrThongTinDoanhNghiep ocrThongTinDoanhNghiep;
	
	@Autowired CallApi callApi;
	
	@PostMapping(value = {"/api/public/all/doc-noi-dung-ocr"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public String docNoiDungOCr(HttpServletRequest req) throws ValidException , Exception, CheckException{
		Params params = getParamsFromInputstream(req);
		System.err.println("vao đây");
		req.setAttribute("soDienThoai",  params.getSoDienThoai());
		req.setAttribute("hoVaTen",  params.getHoVaTen());
		req.setAttribute("soCmt",  params.getSoCmt());
		req.setAttribute("soHopDong",  params.getSoHopDong());
		req.setAttribute("codeTransaction",  params.getCodeTransaction());
		
		boolean kiemTraLoaiGiayToMatTruocVaMatSau = false;

		return componentApi.docNoiDungOCr(params, req.getHeader(Contains.MA_TO_CHUC_HEADER), kiemTraLoaiGiayToMatTruocVaMatSau);
	}
	
	@PostMapping(value = {"/api/public/all/pr/thong-tin-doanh-nghiep"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public String thongTinDoanhNghiepPr(HttpServletRequest req, @RequestParam("fileDangKyKinhDoanh") MultipartFile fileDangKyKinhDoanh, @RequestParam Map<String, String> allParams) throws ValidException , Exception, CheckException{
		Params params = new Params();
		System.err.println("aaaa");
		
		if(fileDangKyKinhDoanh == null || StringUtils.isEmpty(fileDangKyKinhDoanh.getOriginalFilename())) throw new ValidException(getMessage("Truyền lên file đăng ký kinh doanh"));
		if(StringUtils.isEmpty(allParams.get("loaiGiayTo"))) throw new ValidException(getMessage("Truyền lên loại giấy tờ"));
		
		String base64Img = Base64.getEncoder().encodeToString(fileDangKyKinhDoanh.getBytes());
		params.setNoiDungFile(base64Img);
		params.setLoaiGiayTo(allParams.get("loaiGiayTo"));
		params.setTenFile(allParams.get("tenFile"));
		
		if(StringUtils.isEmpty(allParams.get("tenFile"))) {
			FileHandling fileHandling = new FileHandling();
			params.setTenFile(fileHandling.getTypeFileFromName(fileDangKyKinhDoanh.getOriginalFilename()));
		}
		 
		ThongTinDoanhNghiep thongTinDoanhNghiep = layThongTinDoanhNghiep(params, layMaKhachHang(req));
		
		if(params.getTenFile() != null && params.getTenFile().equals("pdf")) {
			String str = esignService.kiemTraChuKyApi(fileDangKyKinhDoanh);
			thongTinDoanhNghiep.setThongTinChuKy(str);
		}
		
		return ReturnRest.successNull(thongTinDoanhNghiep);
	}
	
	private ThongTinDoanhNghiep layThongTinDoanhNghiep(Params params, String maKhachHang) throws ValidException, CheckException, Exception {
		if(StringUtils.isEmpty(params.getNoiDungFile())) throw new ValidException(getMessage("Truyền lên ảnh giấy tờ của doanh nghiệp."));
		
		ThongTinDoanhNghiep thongTinDoanhNghiep = null;
		RespTaxpayer respTaxpayer = null;
		if(params.getLoaiGiayTo().equals("dn")) {
			List<DoanhNghiep> thueDnHoCaThes = new ArrayList<DoanhNghiep>();
			thongTinDoanhNghiep = ocrThongTinDoanhNghiep.getThongTin(params.getNoiDungFile(), params.getTenFile(), params.getLoaiGiayTo(), maKhachHang);
			if(!StringUtils.isEmpty(thongTinDoanhNghiep.getMaSoDoanhNghiep())) {
				//thueDnHoCaThes = doanhNghiepRepository.findByMst(thongTinDoanhNghiep.getMaSoDoanhNghiep());
				thueDnHoCaThes = callApi.getThongtinDn(thongTinDoanhNghiep.getMaSoDoanhNghiep());
				
			}
			
			if(StringUtils.isEmpty(thongTinDoanhNghiep.getMaSoDoanhNghiep())) throw new CheckException(getMessage("Không đọc được thông tin mã số doanh nghiệp"));
			//respTaxpayer = taxRequest.getInfoTaxResp(thongTinDoanhNghiep.getMaSoDoanhNghiep(), Contains.MA_TRUY_VAN_THEO_MST);//thue
			respTaxpayer = callApi.getThongTinThue(thongTinDoanhNghiep.getMaSoDoanhNghiep());
			
			
			if(thueDnHoCaThes.size() <= 0 && respTaxpayer == null) throw new CheckException(getMessage("Mã số doanh nghiệp không tồn tại")); 
			if(thueDnHoCaThes.size() > 0) {
				DoanhNghiep thueDnHoCaThe = thueDnHoCaThes.get(0);
				
				String von = "";
				if(thueDnHoCaThe.getTongVon() != null && thueDnHoCaThe.getTongVon().compareTo(new BigDecimal("0")) != 0) {
					von = thueDnHoCaThe.getTongVon().toPlainString();
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				thongTinDoanhNghiep.setTenDoanhNghiepCp(thueDnHoCaThe.getTenCty());
				thongTinDoanhNghiep.setDiaChiCp(thueDnHoCaThe.getDiaChi());
				thongTinDoanhNghiep.setLoaiHinhDoanhNghiepCp(thayTenLoaiHinhDoanhNghiep(thueDnHoCaThe.getLoaiHinhDn()));
				
				thongTinDoanhNghiep.setTrangThai(thueDnHoCaThe.getTrangThai());
				
				thongTinDoanhNghiep.setVonDieuLeCp(von);
				thongTinDoanhNghiep.setQuyMoDoanhNghiep(thueDnHoCaThe.getSoLaoDong()+"");
				thongTinDoanhNghiep.setNgayDangKyCp(dateFormat.parse(thueDnHoCaThe.getNgayCap()));
				thongTinDoanhNghiep.setMaSoThue(thueDnHoCaThe.getMst());
				thongTinDoanhNghiep.setMaSoDoanhNghiepCp(thueDnHoCaThe.getMst());
				thongTinDoanhNghiep.setHoVaTenCp(thueDnHoCaThe.getTenNguoiDaiDien());
			}
			
			
		} else {
			List<ThueDnHoCaThe> thueDnHoCaThes = new ArrayList<ThueDnHoCaThe>();
			
			thongTinDoanhNghiep = ocrThongTinDoanhNghiep.getThongTin(params.getNoiDungFile(), params.getTenFile(), params.getLoaiGiayTo(), maKhachHang);
			
			if(!StringUtils.isEmpty(thongTinDoanhNghiep.getMaSoDoanhNghiep())) {
				//thueDnHoCaThes = thueDnHoCaTheRepository.findBySoGpkd(thongTinDoanhNghiep.getMaSoDoanhNghiep());
			}
			
			if(!StringUtils.isEmpty(thongTinDoanhNghiep.getSoGiayTo()) && thueDnHoCaThes.size() <= 0) {
//				thueDnHoCaThes = thueDnHoCaTheRepository.findByCmt(thongTinDoanhNghiep.getSoGiayTo());
			}
//			respTaxpayer = taxRequest.getInfoTaxResp(thongTinDoanhNghiep.getSoGiayTo(), Contains.MA_TRUY_VAN_THEO_SO_GIAY_TO);
			
//			if(thueDnHoCaThes.size() <= 0 && respTaxpayer == null) throw new CheckException(getMessage("Mã số doanh nghiệp không tồn tại")); 
			
			if(thueDnHoCaThes.size() > 0) {
				ThueDnHoCaThe thueDnHoCaThe = thueDnHoCaThes.get(0);
				
				String von = "";
				if(thueDnHoCaThe.getVon() != null && thueDnHoCaThe.getVon().compareTo(new BigDecimal("0")) != 0) {
					von = thueDnHoCaThe.getVon().toPlainString();
				}
				
				thongTinDoanhNghiep.setTenDoanhNghiepCp(thueDnHoCaThe.getTen());
				thongTinDoanhNghiep.setDiaChiCp(thueDnHoCaThe.getDiaChi());
				thongTinDoanhNghiep.setLoaiHinhDoanhNghiepCp(thueDnHoCaThe.getLhDnghiep());
				
				thongTinDoanhNghiep.setTrangThai(convertTT(thueDnHoCaThe.getTrangThai()));
				
				thongTinDoanhNghiep.setVonDieuLeCp(von);
				thongTinDoanhNghiep.setQuyMoDoanhNghiep(thueDnHoCaThe.getSoLaoDong()+"");
				thongTinDoanhNghiep.setNgayDangKyCp(thueDnHoCaThe.getNgayCapGpkd());
				thongTinDoanhNghiep.setMaSoThue(thueDnHoCaThe.getTin());
				thongTinDoanhNghiep.setMaSoDoanhNghiepCp(thueDnHoCaThe.getTin());
				thongTinDoanhNghiep.setHoVaTenCp(thueDnHoCaThe.getNguoiDdpl());
				
			}
		}
		
		
		if(respTaxpayer != null) {
			thongTinDoanhNghiep.setTenDoanhNghiepCp(respTaxpayer.getNnt().getTenNnt());
			thongTinDoanhNghiep.setDiaChiCp(respTaxpayer.getNnt().getMotaDiachi());
			thongTinDoanhNghiep.setTrangThai(convertTT(respTaxpayer.getNnt().getTrangThai()));
			thongTinDoanhNghiep.setMaSoThue(respTaxpayer.getNnt().getMst());
		}
		
		return thongTinDoanhNghiep;
	}
	

	
	
	private String thayTenLoaiHinhDoanhNghiep(String loaiHinhDn) {
		switch (loaiHinhDn) {
		case "CP":
			return "Công ty cổ phần";
		case "TNHH từ 2 TV":
			return "Công ty trách nhiệm hữu hạn hai thành viên trở lên";
		case "TNHH 1 TV":
			return "Công ty trách nhiệm hữu hạn một thành viên";
		case "DNTN":
			return "Doanh nghiệp tư nhân";
		default:
			return loaiHinhDn;
		}
	}
	public String convertTT(String tt) {
		switch (tt) {
		case "01":
			return "Ngừng hoạt động, đã thu hồi GCN ĐKT, hoàn thành các thủ tục đóng MST";
		case "02":
			return "Đang làm thủ tục chuyển địa điểm";
		case "03":
			return "Ngừng hoạt động, đang làm thủ tục đóng MST";
		case "04":
			return "Đang hoạt động, chưa cấp GCN ĐKT";
		case "05":
			return "Tạm nghỉ kinh doanh có thời hạn";
		case "00":
			return "Đang hoạt động, đã được cấp GCN ĐKT";	
		case "06":
			return "Ngừng hoạt động, không ở địa điểm đăng ký kinh doanh";	
		default:
			break;
		}
		return null;
	}
}
