package fis.com.vn;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import okhttp3.OkHttpClient;

@SpringBootApplication
public class TemplateApplication {
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {

		SpringApplication.run(TemplateApplication.class, args);

//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//		System.out.println(dateFormat.format(new Date()));
//		System.out.println(dateFormat.format(new Date()));

		// MessageDigest md = MessageDigest.getInstance("SHA-256"); //SHA, MD2, MD5,
		// SHA-256, SHA-384...
//		String sha256hash = getSHA256Hash("{\r\n"
//				+ "  \"fileBusinessRegistration\": \"/opt/log/web/23_2_2023/16ee975d-99f3-4503-9e93-9c2d52444664.pdf\",\r\n"
//				+ "  \"fileAppointmentOfChiefAccountant\": \"/opt/log/web/23_2_2023/e18d3e4e-d1c3-44a5-a90a-e883269e63ba.pdf\",\r\n"
//				+ "  \"fileBusinessRegistrationCertificate\": \"\",\r\n"
//				+ "  \"fileDecisionToAppointChiefAccountant\": \"\",\r\n"
//				+ "  \"fileInvestmentCertificate\": \"/opt/log/web/23_2_2023/42d65d8c-5786-4138-879b-3902d2047b72.pdf\",\r\n"
//				+ "  \"fileCompanyCharter\": \"/opt/log/web/23_2_2023/c7891969-d5cb-4edb-b294-8293da175f34.pdf\",\r\n"
//				+ "  \"fileSealSpecimen\": \"\",\r\n"
//				+ "  \"fileFatcaForms\": \"/opt/log/web/23_2_2023/d4bbfba6-0dec-4797-abbf-48bfa9fcc42b.pdf\",\r\n"
//				+ "  \"fileOthers\": \"\",\r\n"
//				+ "  \"nameOfTheAccountHolder\": \"Phạm Mai Vũ\",\r\n"
//				+ "  \"dateAccountOpening\": \"\",\r\n"
//				+ "  \"nameCompany\": \"CÔNG TY TNHH HỆ THÔNG THÔNG TIN FPT\",\r\n"
//				+ "  \"registeredAddress\": \"Tầng 22 tòa nhà Keangnam Landmark72, E6 đường Phạm Hùng, Phường Mễ Trì,  Quận Nam Từ Liêm, Thành phố Hà Nội, Việt Nam\",\r\n"
//				+ "  \"operatingAddress\": \"Tầng 22 tòa nhà Keangnam Landmark72, E6 đường Phạm Hùng, Phường Mễ Trì,  Quận Nam Từ Liêm, Thành phố Hà Nội, Việt Nam\",\r\n"
//				+ "  \"countryOfIncorporation\": \"Vietnam\",\r\n"
//				+ "  \"registrationNumber\": \"0104128565\",\r\n"
//				+ "  \"straight2BankGroupID\": \"\",\r\n"
//				+ "  \"mailingAddress\": \"Botanjca Premier, 108 Hồng Hà, F2, Tân Bình\",\r\n"
//				+ "  \"swiftBankIDCode\": \"\",\r\n"
//				+ "  \"mobileOfficeTelephone\": \"+84977666800\",\r\n"
//				+ "  \"contactPerson\": \"VU\",\r\n"
//				+ "  \"emailAddress\": \"vupham53@gmail.com\",\r\n"
//				+ "  \"listAccount\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"accountType\": \"Payment account/ Tài khoản thanh toán\",\r\n"
//				+ "      \"currency\": \"VND\",\r\n"
//				+ "      \"accountTitle\": \"\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"registeringEmailAddress\": \"vupham53@gmail.com\",\r\n"
//				+ "  \"shortName\": \"FIS., CORP\",\r\n"
//				+ "  \"nameInEnglish\": \"Phạm Mai Vũ\",\r\n"
//				+ "  \"faxNumber\": \"\",\r\n"
//				+ "  \"taxCode\": \"0104128565\",\r\n"
//				+ "  \"applicableAccountingSystems\": \"Vietnamese Accounting Regime/Chế độ kế toán Việt Nam\",\r\n"
//				+ "  \"taxMode\": \"Direct / Trực tiếp khai nộp thuế\",\r\n"
//				+ "  \"residentStatus\": \"Resident / Người Cư Trú\",\r\n"
//				+ "  \"businessActivities\": \"DN SME\",\r\n"
//				+ "  \"yearlyAveragenumber\": \"20\",\r\n"
//				+ "  \"totalSalesTurnover\": \"1231231123\",\r\n"
//				+ "  \"totalCapital\": \"213231123\",\r\n"
//				+ "  \"agreeToReceive\": \"yes\",\r\n"
//				+ "  \"legalRepresentator\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"id\": \"fe4536f\",\r\n"
//				+ "      \"phone\": \"0946742998\",\r\n"
//				+ "      \"email\": \"daidienphapluat1@gmail.com\",\r\n"
//				+ "      \"hoTen\": \"Nguyen Tri Hieu\",\r\n"
//				+ "      \"tokenCheck\": \"396d2658-a0ac-4a9d-ac8a-4f32240744c2\",\r\n"
//				+ "      \"checkMain\": \"yes\",\r\n"
//				+ "      \"time\": \"1677142414711\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"chiefAccountant\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"id\": \"723963b\",\r\n"
//				+ "      \"phone\": \"+84977666800\",\r\n"
//				+ "      \"email\": \"vupham53@gmail.com\",\r\n"
//				+ "      \"hoVaTen\": \"PHẠM MAI VŨ\",\r\n"
//				+ "      \"hoTen\": \"Phạm Mai Vũ\",\r\n"
//				+ "      \"soCmt\": \"079085009683\",\r\n"
//				+ "      \"namSinh\": \"05/03/1985\",\r\n"
//				+ "      \"noiCap\": \"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\",\r\n"
//				+ "      \"hoKhau\": \"224 NGHĨA PHÁT PHƯỜNG 07, TÂN BÌNH, TP HỒ CHÍ MINH\",\r\n"
//				+ "      \"ngayCap\": \"11/01/2022\",\r\n"
//				+ "      \"ngayHetHan\": \"05/03/2025\",\r\n"
//				+ "      \"anhMatTruoc\": \"/opt/log/web/23_2_2023/3ae47294-aa15-4c08-b2d2-36e7575ad572.jpg\",\r\n"
//				+ "      \"anhChuKy\": \"/opt/log/web/23_2_2023/00b7fc41-c6b4-42ac-a611-5585b443e57d.png\",\r\n"
//				+ "      \"anhMatSau\": \"/opt/log/web/23_2_2023/c379db18-c692-4830-a88d-7a55b69a93d6.jpg\",\r\n"
//				+ "      \"kiemTra\": \"update\",\r\n"
//				+ "      \"tokenCheck\": \"0da58eac-69f4-4c9e-94d1-edb9ee0388f2\",\r\n"
//				+ "      \"loai\": \"Chief Account / Kế toán trưởng\",\r\n"
//				+ "      \"quocTich\": \"VIỆT NAM\",\r\n"
//				+ "      \"visa\": \"\",\r\n"
//				+ "      \"maSoThue\": \"\",\r\n"
//				+ "      \"tinhTrangCuTru\": \"Resident\",\r\n"
//				+ "      \"diaChiNha\": \"0977666800\",\r\n"
//				+ "      \"mobile\": \"0977666800\",\r\n"
//				+ "      \"vanPhong\": \"0977666800\",\r\n"
//				+ "      \"email2\": \"vupham53@gmail.com\",\r\n"
//				+ "      \"time\": \"1677142417351\",\r\n"
//				+ "      \"editStatus\": \"no\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"haveAChiefAccountant\": \"no\",\r\n"
//				+ "  \"haveAcccountHolder\": \"yes\",\r\n"
//				+ "  \"listOfLeaders\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"id\": \"d87f414\",\r\n"
//				+ "      \"phone\": \"+84977666800\",\r\n"
//				+ "      \"email\": \"vupham53@gmail.com\",\r\n"
//				+ "      \"hoVaTen\": \"PHẠM MAI VŨ\",\r\n"
//				+ "      \"hoTen\": \"Phạm Mai Vũ\",\r\n"
//				+ "      \"soCmt\": \"079085009683\",\r\n"
//				+ "      \"namSinh\": \"05/03/1985\",\r\n"
//				+ "      \"noiCap\": \"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\",\r\n"
//				+ "      \"hoKhau\": \"224 NGHĨA PHÁT PHƯỜNG 07, TÂN BÌNH, TP HỒ CHÍ MINH\",\r\n"
//				+ "      \"ngayCap\": \"11/01/2022\",\r\n"
//				+ "      \"ngayHetHan\": \"05/03/2025\",\r\n"
//				+ "      \"anhMatTruoc\": \"/opt/log/web/23_2_2023/bc039b4a-41da-4edc-9b11-94e223d4c391.jpg\",\r\n"
//				+ "      \"anhChuKy\": \"/opt/log/web/23_2_2023/e4eb1aa2-a5af-4d10-8fd4-5f52d7c12aec.png\",\r\n"
//				+ "      \"anhMatSau\": \"/opt/log/web/23_2_2023/3783704b-0ca0-49a1-b1a6-ed657b33f368.jpg\",\r\n"
//				+ "      \"kiemTra\": \"update\",\r\n"
//				+ "      \"tokenCheck\": \"32a5c7fa-0538-48fa-a4f5-20ca9075a827\",\r\n"
//				+ "      \"quocTich\": \"VIỆT NAM\",\r\n"
//				+ "      \"visa\": \"\",\r\n"
//				+ "      \"maSoThue\": \"\",\r\n"
//				+ "      \"tinhTrangCuTru\": \"Resident\",\r\n"
//				+ "      \"diaChiNha\": \"0977666800\",\r\n"
//				+ "      \"mobile\": \"+84977666800\",\r\n"
//				+ "      \"vanPhong\": \"0977666800\",\r\n"
//				+ "      \"email2\": \"vupham53@gmail.com\",\r\n"
//				+ "      \"time\": \"1677142423524\",\r\n"
//				+ "      \"editStatus\": \"no\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"personAuthorizedAccountHolder\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"id\": \"d264d22\",\r\n"
//				+ "      \"phone\": \"+84977666800\",\r\n"
//				+ "      \"email\": \"daidienphapluat1@gmail.com\",\r\n"
//				+ "      \"hoTen\": \"Nguyen Tri Hieu \",\r\n"
//				+ "      \"tokenCheck\": \"ce8648c8-6aad-43cf-bb3d-984a6f2dfe4a\",\r\n"
//				+ "      \"time\": \"1677142453572\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"specialInstructions\": \"\",\r\n"
//				+ "  \"token\": \"5700f3df-f789-4ee8-a510-fc30f1295043\",\r\n"
//				+ "  \"allInOne\": \"no\",\r\n"
//				+ "  \"status\": \"fail\",\r\n"
//				+ "  \"userDesignation\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"id\": \"cccd324\",\r\n"
//				+ "      \"email\": \"vupham53@gmail.com\",\r\n"
//				+ "      \"hoTen\": \"Phạm Mai Vũ\",\r\n"
//				+ "      \"soCmt\": \"079085009683\",\r\n"
//				+ "      \"tokenCheck\": \"c10e19ba-aa9b-48ce-89f9-b40e0ba8ecdd\",\r\n"
//				+ "      \"taoLenh\": \"Y\",\r\n"
//				+ "      \"baoCao\": \"N\",\r\n"
//				+ "      \"chapThuanLenh\": \"N\",\r\n"
//				+ "      \"chapThuanLenhDongThoi\": \"N\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"registerUser\": \"All users\",\r\n"
//				+ "  \"specialInstructionsUser\": \"\",\r\n"
//				+ "  \"typeDocument\": \"1\"\r\n"
//				+ "}");
//        System.out.println(sha256hash);

//		OkHttpClient client = getOkHttpClient();
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, "{\r\n    \"maSoDoanhNghiep\":\"0104128565\"\r\n}");
//		Request request = new Request.Builder()
//		  .url("http://api-poc-eid.paas.xplat.fpt.com.vn/api/public/all/lay-thong-tin-thue")
//		  .method("POST", body)
//		  .addHeader("token", "9c50b61b-c149-4e4b-9f23-34a956cac278")
//		  .addHeader("code", "SCTEST")
//		  .addHeader("Content-Type", "application/json")
//		  .addHeader("Cookie", "6d639332a8d17611beebf13d50ed94cf=f0a364f5a9634b8057bb6d4da7a5b667")
//		  .build();
//		Response response = client.newCall(request).execute();
//		String text = response.body().string();
//		JSONObject jsonObject = new JSONObject(text);
//		System.err.println(jsonObject.toString());
//		//List<DoanhNghiep> thueDnHoCaThes = new ArrayList<DoanhNghiep>();
//		RespTaxpayer respTaxpayer = new RespTaxpayer();
//		respTaxpayer = new Gson().fromJson(jsonObject.get("data").toString(), RespTaxpayer.class);
//		
//		System.out.println(respTaxpayer.toString());
//		response.body().close();
//		response.close();

	}

	private static String checksum(InputStream filepath, MessageDigest md) throws IOException {

		// file hashing with DigestInputStream
		try (DigestInputStream dis = new DigestInputStream(filepath, md)) {
			while (dis.read() != -1)
				; // empty loop to clear the data
			md = dis.getMessageDigest();
		}

		// bytes to hex
		StringBuilder result = new StringBuilder();
		for (byte b : md.digest()) {
			result.append(String.format("%02x", b));
		}
		return result.toString();

	}

	private static String getSHA256Hash(String data) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			return bytesToHex(hash); // make it printable
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash).toLowerCase();
	}

	private static OkHttpClient getOkHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
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

		OkHttpClient client = newBuilder.connectTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)
				.readTimeout(100, TimeUnit.SECONDS).build();

		return client;
	}

}
