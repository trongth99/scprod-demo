package fis.com.vn.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.io.IOException;

import fis.com.vn.common.StringUtils;

@Component
public class EncryptionAES {
	@Autowired ConfigProperties configProperties;
	private static byte[] key;
	String innitvector = "ab2b7261-ce5c-43ef-8292-0c72ae074a5e";
	String iv = "12119897-1cee-4388-9d4c-a3c9e8ea98ed";
	
public static void main(String[] args) throws IOException, FileNotFoundException, java.io.IOException {
		
		EncryptionAES encryptionAES = new EncryptionAES();
		byte[] bytes = encryptionAES.sha256("snfjthguyrnvjbnh");
		File outputFile =new File("C:\\Users\\trongth7\\Downloads\\initsha.txt");
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
		    outputStream.write(bytes);
		}
		
		String innitvector = "acf84687-9e78-48a4-9b30-31be5511d305";
		//byte[] allBytes = Files.readAllBytes(Paths.get("C:\\Users\\chinhvd4\\Downloads\\api\\initsha.txt"));
//		FileUtils.writeByteArrayToFile(new File("C:\\Users\\kevin\\Downloads\\sha.txt"), encryptionAES.sha256(innitvector));
		String str = "{\"fileBusinessRegistration\":\"/opt/log/web/23_2_2023/16ee975d-99f3-4503-9e93-9c2d52444664.pdf\",\"fileAppointmentOfChiefAccountant\":\"/opt/log/web/23_2_2023/e18d3e4e-d1c3-44a5-a90a-e883269e63ba.pdf\",\"fileBusinessRegistrationCertificate\":\"\",\"fileDecisionToAppointChiefAccountant\":\"\",\"fileInvestmentCertificate\":\"/opt/log/web/23_2_2023/42d65d8c-5786-4138-879b-3902d2047b72.pdf\",\"fileCompanyCharter\":\"/opt/log/web/23_2_2023/c7891969-d5cb-4edb-b294-8293da175f34.pdf\",\"fileSealSpecimen\":\"\",\"fileFatcaForms\":\"/opt/log/web/23_2_2023/d4bbfba6-0dec-4797-abbf-48bfa9fcc42b.pdf\",\"fileOthers\":\"\",\"nameOfTheAccountHolder\":\"Phạm Mai Vũ\",\"dateAccountOpening\":\"\",\"nameCompany\":\"CÔNG TY TNHH HỆ THÔNG THÔNG TIN FPT\",\"registeredAddress\":\"Tầng 22 tòa nhà Keangnam Landmark72, E6 đường Phạm Hùng, Phường Mễ Trì,  Quận Nam Từ Liêm, Thành phố Hà Nội, Việt Nam\",\"operatingAddress\":\"Tầng 22 tòa nhà Keangnam Landmark72, E6 đường Phạm Hùng, Phường Mễ Trì,  Quận Nam Từ Liêm, Thành phố Hà Nội, Việt Nam\",\"countryOfIncorporation\":\"Vietnam\",\"registrationNumber\":\"0104128565\",\"straight2BankGroupID\":\"\",\"mailingAddress\":\"Botanjca Premier, 108 Hồng Hà, F2, Tân Bình\",\"swiftBankIDCode\":\"\",\"mobileOfficeTelephone\":\"+84977666800\",\"contactPerson\":\"VU\",\"emailAddress\":\"vupham53@gmail.com\",\"listAccount\":[{\"accountType\":\"Payment account/ Tài khoản thanh toán\",\"currency\":\"VND\",\"accountTitle\":\"\"}],\"registeringEmailAddress\":\"vupham53@gmail.com\",\"shortName\":\"FIS., CORP\",\"nameInEnglish\":\"Phạm Mai Vũ\",\"faxNumber\":\"\",\"taxCode\":\"0104128565\",\"applicableAccountingSystems\":\"Vietnamese Accounting Regime/Chế độ kế toán Việt Nam\",\"taxMode\":\"Direct / Trực tiếp khai nộp thuế\",\"residentStatus\":\"Resident / Người Cư Trú\",\"businessActivities\":\"DN SME\",\"yearlyAveragenumber\":\"20\",\"totalSalesTurnover\":\"1231231123\",\"totalCapital\":\"213231123\",\"agreeToReceive\":\"yes\",\"legalRepresentator\":[{\"id\":\"fe4536f\",\"phone\":\"0946742998\",\"email\":\"daidienphapluat1@gmail.com\",\"hoTen\":\"Nguyen Tri Hieu\",\"tokenCheck\":\"396d2658-a0ac-4a9d-ac8a-4f32240744c2\",\"checkMain\":\"yes\",\"time\":\"1677142414711\"}],\"chiefAccountant\":[{\"id\":\"723963b\",\"phone\":\"+84977666800\",\"email\":\"vupham53@gmail.com\",\"hoVaTen\":\"PHẠM MAI VŨ\",\"hoTen\":\"Phạm Mai Vũ\",\"soCmt\":\"079085009683\",\"namSinh\":\"05/03/1985\",\"noiCap\":\"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\",\"hoKhau\":\"224 NGHĨA PHÁT PHƯỜNG 07, TÂN BÌNH, TP HỒ CHÍ MINH\",\"ngayCap\":\"11/01/2022\",\"ngayHetHan\":\"05/03/2025\",\"anhMatTruoc\":\"/opt/log/web/23_2_2023/3ae47294-aa15-4c08-b2d2-36e7575ad572.jpg\",\"anhChuKy\":\"/opt/log/web/23_2_2023/00b7fc41-c6b4-42ac-a611-5585b443e57d.png\",\"anhMatSau\":\"/opt/log/web/23_2_2023/c379db18-c692-4830-a88d-7a55b69a93d6.jpg\",\"kiemTra\":\"update\",\"tokenCheck\":\"0da58eac-69f4-4c9e-94d1-edb9ee0388f2\",\"loai\":\"Chief Account / Kế toán trưởng\",\"quocTich\":\"VIỆT NAM\",\"visa\":\"\",\"maSoThue\":\"\",\"tinhTrangCuTru\":\"Resident\",\"diaChiNha\":\"0977666800\",\"mobile\":\"0977666800\",\"vanPhong\":\"0977666800\",\"email2\":\"vupham53@gmail.com\",\"time\":\"1677142417351\",\"editStatus\":\"no\"}],\"haveAChiefAccountant\":\"no\",\"haveAcccountHolder\":\"yes\",\"listOfLeaders\":[{\"id\":\"d87f414\",\"phone\":\"+84977666800\",\"email\":\"vupham53@gmail.com\",\"hoVaTen\":\"PHẠM MAI VŨ\",\"hoTen\":\"Phạm Mai Vũ\",\"soCmt\":\"079085009683\",\"namSinh\":\"05/03/1985\",\"noiCap\":\"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\",\"hoKhau\":\"224 NGHĨA PHÁT PHƯỜNG 07, TÂN BÌNH, TP HỒ CHÍ MINH\",\"ngayCap\":\"11/01/2022\",\"ngayHetHan\":\"05/03/2025\",\"anhMatTruoc\":\"/opt/log/web/23_2_2023/bc039b4a-41da-4edc-9b11-94e223d4c391.jpg\",\"anhChuKy\":\"/opt/log/web/23_2_2023/e4eb1aa2-a5af-4d10-8fd4-5f52d7c12aec.png\",\"anhMatSau\":\"/opt/log/web/23_2_2023/3783704b-0ca0-49a1-b1a6-ed657b33f368.jpg\",\"kiemTra\":\"update\",\"tokenCheck\":\"32a5c7fa-0538-48fa-a4f5-20ca9075a827\",\"quocTich\":\"VIỆT NAM\",\"visa\":\"\",\"maSoThue\":\"\",\"tinhTrangCuTru\":\"Resident\",\"diaChiNha\":\"0977666800\",\"mobile\":\"+84977666800\",\"vanPhong\":\"0977666800\",\"email2\":\"vupham53@gmail.com\",\"time\":\"1677142423524\",\"editStatus\":\"no\"}],\"personAuthorizedAccountHolder\":[{\"id\":\"d264d22\",\"phone\":\"+84977666800\",\"email\":\"daidienphapluat1@gmail.com\",\"hoTen\":\"Nguyen Tri Hieu \",\"tokenCheck\":\"ce8648c8-6aad-43cf-bb3d-984a6f2dfe4a\",\"time\":\"1677142453572\"}],\"specialInstructions\":\"\",\"token\":\"5700f3df-f789-4ee8-a510-fc30f1295043\",\"allInOne\":\"no\",\"status\":\"fail\",\"userDesignation\":[{\"id\":\"cccd324\",\"email\":\"vupham53@gmail.com\",\"hoTen\":\"Phạm Mai Vũ\",\"soCmt\":\"079085009683\",\"tokenCheck\":\"c10e19ba-aa9b-48ce-89f9-b40e0ba8ecdd\",\"taoLenh\":\"Y\",\"baoCao\":\"N\",\"chapThuanLenh\":\"N\",\"chapThuanLenhDongThoi\":\"N\"}],\"registerUser\":\"All users\",\"specialInstructionsUser\":\"\",\"typeDocument\":\"1\"}";
		//String data = encryptionAES.encryptIVint(str, "9ee07212-47e3-4c22-ab1e-63ed44f21333");
		String data = encryptionAES.encrypt(str);
		
//		String data = "ppxS34T8cMxukw+psA3mCOKY3Tj4M/Q+jqUUHr5eVuuHRxcyNyip65Q2b1M4Ova8ljU/fei/bNWt/k44RIxiTRx5QWg7tJe1pzqSPd8QHqe+diRaIQbbk2z+vAzpTBnJsCbEn27aQ4nomRKWv5mGHEgHB5noue509zxSPFu3dT9rdT6PJ/Ds3Uc2E/3hD8MdkRWuWjRuDVya0pN05VOLdluc9HVNda0Vv3/XDmQT/WkSgg4Vy6UyI01PpEGRBpyPeT1d7Umb/AnA0sb0yOYm+7DvsGvYvWyqrBcrVUDE9wSWMlDp/MOUF1rXnQNhV/ptVlzMf+stemLjQrzcitYXz2Cv4hWxYOS6LMqIFshir71ViyGa2RFWFsyFJlMa+4SvS7WImel10e/TaLVFGg4VmzDj/jod+sXKI0WUIwt3SnFbslZC/QtSH+njbQ6tN41vgfUDkSDXcgNiIY+I6+DlbIJf0BgayJFlDlApYqPsUVQOY7yxkHhGKgJ9Wcs9LVd/7+Jgb9zf2DQhqrMNBWqmSPeGHYayCoFd+mk44ujPOYyk";
		System.out.println(encryptionAES.encrypt(str));
		System.out.println(encryptionAES.decrypt(data));
	}
	
public String encryptIVint(String value, String iv) {
	try {
		key = Files.readAllBytes(Paths.get("C:\\Users\\trongth7\\Downloads\\api\\initsha.txt"));
		
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv.getBytes()));
		
		byte[] encrypted = cipher.doFinal(value.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	return null;
}
public String decryptIVInit(String encrypted, String iv) {
	try {
		key = Files.readAllBytes(Paths.get("C:\\Users\\trongth7\\Downloads\\api\\initsha.txt"));
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv.getBytes()));
		byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

		return new String(original, "UTF-8");
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	return null;
}
	public byte[] sha256(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] encodedhash = digest.digest(
	        		value.getBytes(StandardCharsets.UTF_8));
			
	        return encodedhash;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public String sha256Encode(String value) {
		try {
	        return Base64.getEncoder().encodeToString(sha256(value));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String decrypt(String encrypted) {
		try {
			key = sha256(innitvector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv.getBytes()));
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

			return new String(original, "UTF-8");
		} catch (Exception ex) {
		}

		return null;
	}
	
	public String encrypt(String value) {
		try {
			if(StringUtils.isEmpty(value)) return null;
			
			key = sha256(innitvector);
			
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv.getBytes()));
			
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
		}
		return null;
	}
}
