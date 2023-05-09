/**
 * 
 */
package fis.com.vn.ocr;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import fis.com.vn.common.OcrParser;
import fis.com.vn.contains.ContainsVision;
import fis.com.vn.exception.CheckException;
import fis.com.vn.repository.TemplateOcrRepository;
import fis.com.vn.table.TemplateOcr;

/**
 * @author ChinhVD4
 *
 */
@Component
public class OcrThongTinDoanhNghiep {
	@Autowired ReadContentOcr readContentOcr;
	@Autowired TemplateOcrRepository templateOcrRepository;
	@Autowired OcrParser ocrParser;
	
	public ThongTinDoanhNghiep getThongTin(String noiDungFileBase64, String duoiFile, String tenTemplate, String maKhachHang) throws CheckException {
		TemplateOcr templateOcr = templateOcrRepository.findByTen(tenTemplate);
		
		if(templateOcr == null) throw new CheckException("Không có template tương ứng");
		
		String noiDungOcr = "";
		String keyExtracttable = "";
		if(templateOcr.getLoai() == 0 || templateOcr.getLoai() == 1) {
			noiDungOcr = ocrParser.ocrPdfAllPage(noiDungFileBase64, templateOcr.getLoai(), keyExtracttable, "", true, maKhachHang, MDC.get("requestId"));
		} else {
			noiDungOcr += ocrParser.ocrPdfAllPage(noiDungFileBase64, 0, keyExtracttable, "", true, maKhachHang, MDC.get("requestId"));
		}
		ParseTemplaceOcr parseTemplaceOcr = new Gson().fromJson(templateOcr.getNoiDung(), ParseTemplaceOcr.class);
		return fromTemplateToTtdn(parseTemplaceOcr, noiDungOcr);
	} 
	
	public ThongTinDoanhNghiep fromTemplateToTtdn(ParseTemplaceOcr parseTemplaceOcr, String noiDungOcr) {
		ThongTinDoanhNghiep thongTinDoanhNghiep = new ThongTinDoanhNghiep();
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
		    gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
		        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		        @Override
		        public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
		                throws JsonParseException {
		            try {
		                return df.parse(json.getAsString());
		            } catch (Exception e) {
		                return null;
		            }
		        }
				
		    });
		    Gson gson = gsonBuilder.create();
			JSONObject jsonReturn = readContentOcr.fromTemplate(parseTemplaceOcr, noiDungOcr);
			
			System.out.println(jsonReturn.toString());
			
			thongTinDoanhNghiep = gson.fromJson(jsonReturn.toString(), ThongTinDoanhNghiep.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		thongTinDoanhNghiep.setLoaiGiayTo(convertLoaiGiayTo(thongTinDoanhNghiep.getLoaiGiayTo()));
		
		return thongTinDoanhNghiep;
	}
	
	private String convertLoaiGiayTo(String loaiGiayTo) {
		if(StringUtils.isEmpty(loaiGiayTo)) return null;
		
		if(loaiGiayTo.toLowerCase().indexOf("chứng minh nhân dân") != -1) {
			return ContainsVision.CHUNG_MINH_THU;
		} else if(loaiGiayTo.toLowerCase().indexOf("căn cước công dân") != -1) {
			return ContainsVision.CAN_CUOC_CONG_DAN;
		}  else if(loaiGiayTo.toLowerCase().indexOf("hộ chiếu") != -1) {
			return ContainsVision.HO_CHIEU;
		}
		
		return null;
	}
}
