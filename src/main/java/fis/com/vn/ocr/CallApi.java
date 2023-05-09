package fis.com.vn.ocr;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javax.net.ssl.SSLContext;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.entities.EkycDoanhNghiep;
import net.bytebuddy.asm.Advice.Return;


@Component
public class CallApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CallApi.class);
	int timeOutRequest = 2*60*1000;
	@Autowired ConfigProperties configProperties;
	@Autowired GetApiAI getApiAI;
	
	public String sendRequest(String base64Image, String url) {
		try {
			byte[] byteImage = Base64.getDecoder().decode(base64Image);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpPost uploadFile = new HttpPost(url);
			uploadFile.setConfig(timeout().build());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			builder.addBinaryBody("file", byteImage, ContentType.MULTIPART_FORM_DATA, "abc.jpg" );
			builder.addTextBody("check", "{ \"check_photocopied\": true, \"check_corner_cut\": true, \"check_emblem\": true, \"check_embossed_stamp\": true, \"check_avatar\": true, \"check_replacement_avatar\": true, \"check_recaptured\": true, \"check_exprity_date\": true, \"check_red_stamp\": true, \"check_embossed_stamp\": true, \"check_rfp\": true, \"check_lfp\": true, \"check_glare\": true, \"check_frame\":true }", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			return text;
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	
	public String requestCmtCccd(String base64Image) {
		String url = getApiAI.getUrlOcr();
         System.out.println("url cmt cccd: "+url);
		return sendRequest(base64Image, url);
	}
	
	
	public String postRequest(String data, String url) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(data, "UTF-8");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			LOGGER.info(responseString);
			
			return responseString;
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	public String postRequestOcrAsync(MultipartFile file, String url, String ocrType, String ocrKey) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpPost uploadFile = new HttpPost(url);
			uploadFile.setConfig(timeout().build());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			builder.addBinaryBody("file", file.getBytes(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename() );
			builder.addTextBody("ocr_type", ocrType, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("ocr_key", ocrKey, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("file_type", file.getOriginalFilename().toLowerCase().endsWith("pdf")?"PDF":"IMAGE", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			JSONObject jsonObject = new JSONObject(text);
			if(jsonObject.getInt("error") == 0) return jsonObject.getString("result");
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	private RequestConfig.Builder timeout() {
		RequestConfig.Builder requestConfig = RequestConfig.custom();
		requestConfig.setConnectTimeout(timeOutRequest);
		requestConfig.setConnectionRequestTimeout(timeOutRequest);
		requestConfig.setSocketTimeout(timeOutRequest);
		
		return requestConfig;
	}
	
	private OkHttpClient getOkHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
		newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
		newBuilder.hostnameVerifier((hostname, session) -> true);

		OkHttpClient client = newBuilder
			    .connectTimeout(100, TimeUnit.SECONDS)
			    .writeTimeout(100, TimeUnit.SECONDS)
			    .readTimeout(100, TimeUnit.SECONDS)
			    .build();

		return client;
	}
	public List<DoanhNghiep> getThongtinDn(String maSoDoanhNghiep) throws KeyManagementException, NoSuchAlgorithmException{
		try {
			OkHttpClient client = getOkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\r\n    \"maSoDoanhNghiep\":\""+maSoDoanhNghiep+"\"\r\n}");
			Request request = new Request.Builder()
			  .url("http://api-poc-eid.paas.xplat.fpt.com.vn/api/public/all/lay-thong-tin-doanh-nghiep")
			  .method("POST", body)
			  .addHeader("token", "9c50b61b-c149-4e4b-9f23-34a956cac278")
			  .addHeader("code", "SCTEST")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Cookie", "6d639332a8d17611beebf13d50ed94cf=f0a364f5a9634b8057bb6d4da7a5b667")
			  .build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();
			JSONObject jsonObject = new JSONObject(text);
			System.err.println(jsonObject.toString());
			//List<DoanhNghiep> thueDnHoCaThes = new ArrayList<DoanhNghiep>();
			
			ObjectMapper mapper = new ObjectMapper();

			List<DoanhNghiep> dns = new ArrayList<>();

			dns = Arrays.asList(mapper.readValue(jsonObject.get("data").toString(), DoanhNghiep[].class));
			
			//System.out.println(dns.get(0));
			response.body().close();
			response.close();
			return dns;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("getInforDn error: {}", e);
		}
	
		return null;
	}
	
	public RespTaxpayer getThongTinThue(String maSoDoanhNghiep) throws KeyManagementException, NoSuchAlgorithmException{
		
		try {
			 OkHttpClient client = getOkHttpClient();
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, "{\r\n    \"maSoDoanhNghiep\":\""+maSoDoanhNghiep+"\"\r\n}");
				Request request = new Request.Builder()
				  .url("http://api-poc-eid.paas.xplat.fpt.com.vn/api/public/all/lay-thong-tin-thue")
				  .method("POST", body)
				  .addHeader("token", "9c50b61b-c149-4e4b-9f23-34a956cac278")
				  .addHeader("code", "SCTEST")
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Cookie", "6d639332a8d17611beebf13d50ed94cf=f0a364f5a9634b8057bb6d4da7a5b667")
				  .build();
				Response response = client.newCall(request).execute();
				String text = response.body().string();
				JSONObject jsonObject = new JSONObject(text);
				System.err.println(jsonObject.toString());
				//List<DoanhNghiep> thueDnHoCaThes = new ArrayList<DoanhNghiep>();
				RespTaxpayer respTaxpayer = new RespTaxpayer();
				respTaxpayer = new Gson().fromJson(jsonObject.get("data").toString(), RespTaxpayer.class);
				
				//System.out.println(respTaxpayer.toString());
				response.body().close();
				response.close();
				return respTaxpayer;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("getThongTinThue error: {}", e);
		}
		
		       
		
		return null;
	}
	
	
//	public OcrDoanhNghiepResp listDoanhNghiep(JSONObject jsonObject){
//		
//		OcrDoanhNghiepResp ocrDoanhNghiepResp = new OcrDoanhNghiepResp();
//		
//		JSONArray jsondn = jsonObject.getJSONArray("data");
//		for (int i = 0; i < jsondn.length(); i++) {
//			DoanhNghiep doanhNghiep = new DoanhNghiep();
//			doanhNghiep.setId(jsondn.getJSONObject(i).getInt("id"));
//			doanhNghiep.setMst(jsondn.getJSONObject(i).getString("mst"));
//			doanhNghiep.setTenCty(jsondn.getJSONObject(i).getString("tenCty"));
//			doanhNghiep.setTrangThai(jsondn.getJSONObject(i).getString("trangThai"));
//			doanhNghiep.setNgayCapMst(jsondn.getJSONObject(i).getString("ngayCapMst"));
//			doanhNghiep.setNoiCapMst(jsondn.getJSONObject(i).getString("noiCapMst"));
//			doanhNghiep.setDiaChi(jsondn.getJSONObject(i).getString("diaChi"));
//			doanhNghiep.setEmail(jsondn.getJSONObject(i).getString("email"));
//			doanhNghiep.setSoDienThoai(jsondn.getJSONObject(i).getString("soDienThoai"));
//			doanhNghiep.setTongVon(jsondn.getJSONObject(i).getBigDecimal("tongVon"));
//			doanhNghiep.setLoaiHinhDn(jsondn.getJSONObject(i).getString("loaiHinhDn"));
//			doanhNghiep.setGiamDoc(jsondn.getJSONObject(i).getString("giamDoc"));
//			doanhNghiep.setSoDienThoaiGiamDoc(jsondn.getJSONObject(i).getString("soDienThoaiGiamDoc"));
//			doanhNghiep.setKeToanTruong(jsondn.getJSONObject(i).getString("keToanTruong"));
//			doanhNghiep.setSdtKeToanTruong(jsondn.getJSONObject(i).getString("sdtKeToanTruong"));
//			doanhNghiep.setSoLaoDong(jsondn.getJSONObject(i).getInt("soLaoDong"));
//			doanhNghiep.setSoGiayPhepKinhDoanh(jsondn.getJSONObject(i).getString("soGiayPhepKinhDoanh"));
//			doanhNghiep.setNgayCap(jsondn.getJSONObject(i).getString("ngayCap"));
//			doanhNghiep.setTenNguoiDaiDien(jsondn.getJSONObject(i).getString("tenNguoiDaiDien"));
//			doanhNghiep.setEmailNguoiDaiDien(jsondn.getJSONObject(i).getString("emailNguoiDaiDien"));
//			doanhNghiep.setSoDienThoaiNguoiDaiDien(jsondn.getJSONObject(i).getString("soDienThoaiNguoiDaiDien"));
//			doanhNghiep.setNghanhNghe(jsondn.getJSONObject(i).getString("nghanhNghe"));
//			doanhNghiep.setSoGiayToNguoiDaiDien(jsondn.getJSONObject(i).getString("soGiayToNguoiDaiDien"));
//			doanhNghiep.setNoiCapNguoiDaiDien(jsondn.getJSONObject(i).getString("noiCapNguoiDaiDien"));
//			doanhNghiep.setId(jsondn.getJSONObject(i).getInt("loai"));
//			
////			ocrDoanhNghiepResp.add(doanhNghiep);
//			
//		}
//		
//		return ocrDoanhNghiepResp;
//		
//	}
	
	
}
