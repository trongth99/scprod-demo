package fis.com.vn.ocr;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.Language;

@Component
public class EsignService {
	@Autowired Language language;
	@Autowired ConfigProperties configProperties;
	
	public String notificationTemplate = "[FPT-CA] Ma xac thuc (OTP) cua Quy khach la {AuthorizeCode}. Vui long dien ma so nay de ky Hop dong Dien Tu va khong cung cap OTP cho bat ky ai";
    public String notificationSubject = "[FPT-CA] Ma xac thuc (OTP)";
	
	public String kiemTraChuKyApi(MultipartFile fileDangKyKinhDoanh) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost(configProperties.getConfig().getLink_kiem_tra_chu_ky_so());
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			builder.addBinaryBody("file", fileDangKyKinhDoanh.getBytes(), ContentType.create("application/pdf"), fileDangKyKinhDoanh.getOriginalFilename() );
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			System.out.println(text);
			return text;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
