package fis.com.vn.api;

import java.io.File;



import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import okhttp3.Response;


@Component
public class CallApiChuKy {
	
	private final Logger LOGGER = LoggerFactory.getLogger(CallApiChuKy.class);

	public OkHttpClient getOkHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
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

		OkHttpClient client = newBuilder.build();

		return client;
	}


	public String checkChuKySo(String file) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
					MediaType mediaType = MediaType.parse("text/plain");
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("file",file,
					    RequestBody.create(MediaType.parse("application/octet-stream"),
					    new File(file)))
					  .build();
					Request request = new Request.Builder()
					  .url("http://api-poc-eid.paas.xplat.fpt.com.vn/api/public/all/esign/kiem-tra-chu-ky")
					  .method("POST", body)
					  .addHeader("token", "8ad61aae-af60-4d6c-91c6-5271e35caf69")
					  .addHeader("code", "test")
					  .addHeader("Cookie", "6d639332a8d17611beebf13d50ed94cf=62038cbda3e985d2d165d61e8838a039")
					  .build();
					Response response = client.newCall(request).execute();
					String text = response.body().string();
					response.body().close();
					response.close();

					return text;

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
}
