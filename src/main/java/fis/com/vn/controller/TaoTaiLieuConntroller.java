package fis.com.vn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fis.com.vn.common.EnDeCryption;
import fis.com.vn.entities.ParamsDoc;
import fis.com.vn.repository.TaiLieuKySoRepository;
import fis.com.vn.table.TaiLieuKySo;

@Controller
public class TaoTaiLieuConntroller extends BaseController {

	@Autowired
	TaiLieuKySoRepository taiLieuKySoRepository;

	@Autowired
	EnDeCryption enDeCryption;

	@GetMapping(value = "/tao-tai-lieu")
	public String kySo(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		forwartParams(allParams, model);

		String imgFolderLog = "/image/kyso/hopdongvay_motkttseabank.pdf";

		System.err.println("hjasdbghjas: " + imgFolderLog);
		model.addAttribute("urlFile", imgFolderLog);
		return "demo/tailieu/kyso";
	}

	@PostMapping(value = "/tao-tai-lieu/luu")
	@ResponseBody
	public String saveDoc(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			@RequestBody String data) {

		JSONObject jsonObject = new JSONObject();
		try {
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			System.err.println("data: " + data.toString());
			ParamsDoc paramsDoc = gson.fromJson(data, ParamsDoc.class);
			TaiLieuKySo taiLieuKySo = new TaiLieuKySo();
			taiLieuKySo.setUsername(paramsDoc.getUsername());
			taiLieuKySo.setPassword(paramsDoc.getPassword());
			taiLieuKySo.setSoHD(paramsDoc.getSoHD());

			taiLieuKySo.setTrangThai(paramsDoc.getTrangThai());

			taiLieuKySoRepository.save(taiLieuKySo);
			jsonObject.put("token", token);
			jsonObject.put("status", 200);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

//	@GetMapping(value = "/viewpdf/byte/{path}", produces = MediaType.APPLICATION_PDF_VALUE)
//	@ResponseBody
//	public ResponseEntity<byte[]> getImage(@PathVariable("path") String path) {
//		return getImage1(path);
//	}
//
//	public ResponseEntity<byte[]> getImage1(String path) {
//		try {
//			String pathImg = enDeCryption.decrypt(path);
//			File file = new File(pathImg);
//
//			byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(file));
//			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	@GetMapping(value = "/viewpdf/byte", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getImage2(@RequestParam Map<String, String> allParams) {
		try {
			File file = new File(allParams.get("path"));

			byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(file));
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(value = "/viewpdf")
	public String viewpdf(Model model) {
		return "viewpdf";
	}

}
