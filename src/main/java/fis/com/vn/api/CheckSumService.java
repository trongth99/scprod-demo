package fis.com.vn.api;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.com.vn.contains.Contains;
import fis.com.vn.repository.EkycDoanhNghiepRepository;
import fis.com.vn.table.EkycDoanhNghiepTable;

@Service
public class CheckSumService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckSumService.class);
	
	@Autowired
	EkycDoanhNghiepRepository ekycDoanhNghiepRepository;
	
	public void save(EkycDoanhNghiepTable ekycDoanhNghiepTable) {
		String checkSum = "";
		if(ekycDoanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
			checkSum = getSHA256Hash(ekycDoanhNghiepTable.getSsNoiDung());
		}else {
			checkSum = getSHA256Hash(ekycDoanhNghiepTable.getNoiDung());
		}
		
		ekycDoanhNghiepTable.setCheckSum(checkSum);
		ekycDoanhNghiepRepository.save(ekycDoanhNghiepTable);
	}
	
	public static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
	public static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }
}
