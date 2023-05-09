package fis.com.vn.entities;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ParamsDoc {

	String username;

	String password;

	String soHD;

	ArrayList<Signer> Signers;

	String trangThai;

}
