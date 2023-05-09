package fis.com.vn.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.ocr.GetApiAI;
import fis.com.vn.repository.TemplateOcrRepository;

@Component
public class OcrParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(OcrParser.class);
	@Autowired ConfigProperties configProperties;
	@Autowired GetApiAI getApiAI;
	@Autowired TemplateOcrRepository templateOcrRepository;
	
	public String getOcr(String base64Img, Integer loai, String maKhachHang, String logId) {
		return getOcr(base64Img, loai, 1, false, "", "1", maKhachHang, logId);
	}
	
	public String getOcr(String base64Img, Integer loai, Integer page, boolean layDuDuLieu, String keyExtracttable, String speedup, String maKhachHang, String logId) {
		
		try {
			FileHandling fileHandling = new FileHandling();
        	String pathFileLog = fileHandling.save(base64Img, configProperties.getConfig().getImage_folder_log());
			LOGGER.info("Ocr img: "+pathFileLog);
		} catch (Exception e) {
			LOGGER.error("getOcr log: ", e);
		}
		
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			String url = getApiAI.getUrlOcrFull();
			
			HttpPost uploadFile = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			LOGGER.info("Link ocr: "+url);
			
			// This attaches the file to the POST:
			builder.addBinaryBody(
			    "file",
			    Base64.getDecoder().decode(base64Img),
			    ContentType.MULTIPART_FORM_DATA,
			    "abc.jpg"
			);
			if(loai != null)
				builder.addTextBody("type", loai+"", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			else
				builder.addTextBody("type", "1", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("is_filter", "0", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			if(!StringUtils.isEmpty(keyExtracttable)) {
				builder.addTextBody("ocr_key", keyExtracttable, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			}
			
			LOGGER.info("Speedup: {}", speedup);
			LOGGER.info("Link ocr: {}", configProperties.getConfig().getLink_ocr());
			
			builder.addTextBody("speedup", speedup, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			
			luuLogSuDungBang(text, maKhachHang, logId);
			
			if(layDuDuLieu) return text;
			JSONObject jsonObject = new JSONObject(text);
			String data = jsonObject.getString("text").trim();
			data = " xuongDong <page"+page+"> xuongDong " + data +" xuongDong </page"+page+"> xuongDong ";
			//LOGGER.info("getJsonOcr: {}", data.replace("\\n", " xuongDong ").replace("\\r", " xuongDong ").replace("\n", " xuongDong ").replace("\r", " xuongDong "));
			return data;
		} catch (Exception e) {
			LOGGER.error("getOcr: ", e.getMessage());
		}
		
		return null;
	}
	public String ocrFromLink(String link, String base64Img, String check) {
		try {
			try {
				FileHandling fileHandling = new FileHandling();
	        	String pathFileLog = fileHandling.save(base64Img, configProperties.getConfig().getImage_folder_log());
				LOGGER.info("Ocr img: "+pathFileLog);
			} catch (Exception e) {
				LOGGER.error("getOcr log: ", e);
			}
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost(link);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			// This attaches the file to the POST:
			builder.addBinaryBody(
			    "file",
			    Base64.getDecoder().decode(base64Img),
			    ContentType.MULTIPART_FORM_DATA,
			    "abc.jpg"
			);
			if(check != null)
				builder.addTextBody("check", check, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			
			LOGGER.info("getJsonOcr: {}", text.replace("\\n", " ").replace("\\r", " ").replace("\n", " ").replace("\r", " "));
			return text;
		} catch (Exception e) {
			LOGGER.error("getOcr: ", e.getMessage());
		}
		
		return null;
	}
	public String ocrPdf(String base64Encode, int loai, String maKhachHang, String logId) {
		return ocrPdf(base64Encode, loai, -1, "", "1", maKhachHang, logId);
	}
	public String ocrPdf(String base64Encode, int loai, int numberPagebreak, String keyExtracttable, String speedup, String maKhachHang, String logId) {
		String str = "";
		try {
			InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encode));
			PDDocument document = PDDocument.load(targetStream);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			LOGGER.info("Pdf page: {}", document.getNumberOfPages());
			
			for (int page = 0; page < document.getNumberOfPages(); ++page)
			{ 
				baos = new ByteArrayOutputStream();
		    	Utils.layAnhTuTrangPdf(pdfRenderer, page, baos);
			    
			    String base64Img = Base64.getEncoder().encodeToString(baos.toByteArray());
	            
	            str += " " + getOcr(base64Img, loai, page+1, false, keyExtracttable, speedup, maKhachHang, logId);
	            if(numberPagebreak == page) break;
			}
			document.close();
		} catch (Exception e) {
			LOGGER.error("ocrPdf: {}", e.getMessage());
		}
		
		return str;
	}
	
	public String ocrPdfAll(String base64Encode, int loai, String keyExtracttable, String maKhachHang, String logId) {
		return ocrPdfAllPage(base64Encode, loai, keyExtracttable, "", false, maKhachHang, logId);
	}
	
	public String ocrPdfAllPage(String base64Encode, int loai, String keyExtracttable, String page, boolean gpdn, String maKhachHang, String logId) {
		String str = "";
		String strTable = "";
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String url = getApiAI.getUrlOcrFull();
			HttpPost uploadFile = new HttpPost(url);
			
			LOGGER.info("Link ocr: {}", url);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			// This attaches the file to the POST:
			builder.addBinaryBody(
			    "file",
			    Base64.getDecoder().decode(base64Encode),
			    ContentType.MULTIPART_FORM_DATA,
			    "abc.jpg"
			);
			builder.addTextBody("type", loai+"", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("is_filter", "0", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			if(gpdn) {
				builder.addTextBody("dkkd", "1", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			}
			if(!StringUtils.isEmpty(keyExtracttable)) {
				builder.addTextBody("ocr_key", keyExtracttable, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			}
			
			if(!StringUtils.isEmpty(page)) {
				builder.addTextBody("page", page, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			}
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			System.out.println(text);
			
			luuLogSuDungBang(text, maKhachHang, logId);
			
			JSONObject jsonObject = new JSONObject(text);
			
			for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
				try {
					JSONObject object = jsonObject.getJSONArray("data").getJSONObject(i);
					String data = object.getString("text");
					str += " xuongDong <page"+(i+1)+"> xuongDong " + data +" xuongDong </page"+(i+1)+"> xuongDong ";
					strTable += object.getString("tables_text");
				} catch (Exception e) {
				}
			}
			String strAll = str + strTable;
			//LOGGER.info("getJsonOcr: {}", strAll.replace("\\n", " xuongDong ").replace("\\r", " xuongDong ").replace("\n", " xuongDong ").replace("\r", " xuongDong "));
			return  strAll;
		} catch (Exception e) {
			LOGGER.error("getOcr: ", e.getMessage());
		}
		
		return null;
	}
	
	private void luuLogSuDungBang(String text, String maKhachHang, String logId) {
	}

	public int numberPageInPdf(String base64Encode) {
		try {
			InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encode));
			PDDocument document = PDDocument.load(targetStream);
			
			LOGGER.info("Pdf page: {}", document.getNumberOfPages());
			int numberPage = document.getNumberOfPages();
			document.close();
			
			return numberPage;
		} catch (Exception e) {
			LOGGER.error("number Pdf: {}", e.getMessage());
		}
		
		return 0;
	}
	
	@SuppressWarnings("unused")
	public String pdfToImgFirst(String base64Encode) {
		try {
			InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encode));
			PDDocument document = PDDocument.load(targetStream);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			LOGGER.info("Pdf page: {}", document.getNumberOfPages());
			
			for (int page = 0; page < document.getNumberOfPages(); ++page)
			{ 
			    baos = new ByteArrayOutputStream();
			    Utils.layAnhTuTrangPdf(pdfRenderer, page, baos);
			    
			    String base64Img = Base64.getEncoder().encodeToString(baos.toByteArray());
			    
	            return base64Img;
			}
			document.close();
		} catch (Exception e) {
			LOGGER.error("ocrPdf: {}", e.getMessage());
		}
		
		return null;
	}

//	public String ocrPdfDetectTable(String base64Encode, int loai, int numberPagebreak, String keyExtracttable, String speedup) {
//		String str = "";
//		try {
//			InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encode));
//			PDDocument document = PDDocument.load(targetStream);
//			PDFRenderer pdfRenderer = new PDFRenderer(document);
//			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			LOGGER.info("Pdf page: {}", document.getNumberOfPages());
//			
//			for (int page = 0; page < document.getNumberOfPages(); ++page)
//			{ 
//				baos = new ByteArrayOutputStream();
//		    	Utils.layAnhTuTrangPdf(pdfRenderer, page, baos);
//			    
//			    String base64Img = Base64.getEncoder().encodeToString(baos.toByteArray());
//			    boolean checkBang = coBangTrongAnh(base64Img);
//			    System.out.println("checkBang: "+checkBang);
//			    if(checkBang)
//			    	str += " " + getOcr(base64Img, 0, page+1, false, keyExtracttable, speedup);
//			}
//			document.close();
//		} catch (Exception e) {
//			LOGGER.error("ocrPdf: {}", e.getMessage());
//		}
//		
//		return str;
//	}
}
