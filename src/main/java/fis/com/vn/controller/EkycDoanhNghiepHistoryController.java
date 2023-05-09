package fis.com.vn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.MediaTypeUtils;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.Language;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.EkycDoanhNghiep;
import fis.com.vn.entities.InfoPerson;
import fis.com.vn.entities.KtraDoanhNghiep;
import fis.com.vn.repository.EkycDoanhNghiepHistoryRepository;
import fis.com.vn.repository.EkycDoanhNghiepRepository;

import fis.com.vn.table.EkycDoanhNghiepTableHistory;

@Controller
public class EkycDoanhNghiepHistoryController  extends BaseController {
	@Autowired
	EkycDoanhNghiepRepository ekycDoanhNghiepRepository;
	@Autowired
	Language language;
	@Autowired
	EkycDoanhNghiepHistoryRepository  ekycDoanhNghiepHistoryRepository;
	
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	ServletContext servletContext;
	
	@GetMapping(value = "/ekyc-doanh-nghiep/danh-sach")
	public String getDanhSachEkycDoanhNghiep(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		
		try {
			model.addAttribute("download", isAllowUrl(req, "/download-ekyc-doanh-nghiep-history"));
			model.addAttribute("xemchitiet", isAllowUrl(req, "/xem-ekyc-doanh-nghiep-history"));
			
			if (StringUtils.isEmpty(allParams.get("id"))) {
				throw new Exception(language.getMessage("xem_that_bai"));
			}
			Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit") );
			Page<EkycDoanhNghiepTableHistory> ekycDnHistorys = ekycDoanhNghiepHistoryRepository.
					selectParams(Long.parseLong(allParams.get("id")), getPageable(allParams, paginate));
			model.addAttribute("currentPage", paginate.getPage());
			model.addAttribute("totalPage", ekycDnHistorys.getTotalPages());
			model.addAttribute("totalElement", ekycDnHistorys.getTotalElements());
			model.addAttribute("ekycDnHistorys", ekycDnHistorys.getContent());

		
			forwartParams(allParams, model);
			return "quantrikhachhang/doanhnghiep/danhsachhistory";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/ekyc-doanh-nghiep";
		}
	}
	@GetMapping(value = { "/download-ekyc-doanh-nghiep-history/start" })
	public ResponseEntity<InputStreamResource> downloadStart(Model model, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isEmpty(allParams.get("id"))) {
			throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
		}
		Optional<EkycDoanhNghiepTableHistory> userInfo = ekycDoanhNghiepHistoryRepository.findById(Long.valueOf(allParams.get("id")));
		if (!userInfo.isPresent()) {
			throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
		}

		EkycDoanhNghiepTableHistory doanhNghiepTableHistory = userInfo.get();

		if (!doanhNghiepTableHistory.getStatus().equals(Contains.TRANG_THAI_KY_THANH_CONG))
			throw new Exception(language.getMessage("khong_the_download"));

		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiepTableHistory.getNoiDung(), EkycDoanhNghiep.class);

		String zipFile = configProperties.getConfig().getImage_folder_log() + "web/download.zip";
		String[] srcFiles = {};
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileDangKy());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileKy());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileBusinessRegistration());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileAppointmentOfChiefAccountant());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileInvestmentCertificate());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileCompanyCharter());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileSealSpecimen());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileFatcaForms());
		srcFiles = append(srcFiles, ekycDoanhNghiep.getFileOthers());
		try {
			byte[] buffer = new byte[1024];
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (int i = 0; i < srcFiles.length; i++) {
				File srcFile = new File(srcFiles[i]);
				FileInputStream fis = new FileInputStream(srcFile);
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
		} catch (IOException ioe) {
			System.out.println("Error creating zip file: " + ioe);
		}

		MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, "download.zip");
		File fileZip = new File(zipFile);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(fileZip));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileZip.getName())
				.contentType(mediaType).contentLength(fileZip.length()).body(resource);
	}
	private <T> T[] append(T[] arr, T element) {
		if (!StringUtils.isEmpty(element)) {
			final int N = arr.length;
			arr = Arrays.copyOf(arr, N + 1);
			arr[N] = element;
		}
		return arr;
	}
	@GetMapping(value = { "/download-ekyc-doanh-nghiep-history" })
	public String download(Model model, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) throws Exception {
		try {
			if (StringUtils.isEmpty(allParams.get("id"))) {
				throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
			}
			Optional<EkycDoanhNghiepTableHistory> userInfo = ekycDoanhNghiepHistoryRepository
					.findById(Long.valueOf(allParams.get("id")));
			if (!userInfo.isPresent()) {
				throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
			}

			EkycDoanhNghiepTableHistory ekycDoanhNghiepTableHistory = userInfo.get();

			if (!ekycDoanhNghiepTableHistory.getStatus().equals(Contains.TRANG_THAI_KY_THANH_CONG))
				throw new Exception(language.getMessage("khong_the_download"));

			return "redirect:/download-ekyc-doanh-nghiep-history/start?id=" + allParams.get("id");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/ekyc-doanh-nghiep";
		}
	}
	@GetMapping(value = "/xem-ekyc-doanh-nghiep-history")
	public String getDoanhNghiep(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {

		try {
			if (StringUtils.isEmpty(allParams.get("id"))) {
				throw new Exception(language.getMessage("xem_that_bai"));
			}
			Optional<EkycDoanhNghiepTableHistory> userInfo = ekycDoanhNghiepHistoryRepository
					.findById(Long.valueOf(allParams.get("id")));
			if (!userInfo.isPresent()) {
				throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
			}

			EkycDoanhNghiepTableHistory ekycDoanhNghiepTableHistory = userInfo.get();

			EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(ekycDoanhNghiepTableHistory.getNoiDung(),EkycDoanhNghiep.class);
			KtraDoanhNghiep ktraDoanhNghiep = new Gson().fromJson(ekycDoanhNghiepTableHistory.getCheckNoiDung(),KtraDoanhNghiep.class);
//            if(StringUtils.isEmpty(ekycDoanhNghiep.getThongTinChuKy())) ekycDoanhNghiep.setThongTinChuKy("{}");

			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileBusinessRegistration())) {
				model.addAttribute("fileBusinessRegistration",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileBusinessRegistration())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant())) {
				model.addAttribute("fileAppointmentOfChiefAccountant", CommonUtils
						.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileBusinessRegistrationCertificate())) {
				model.addAttribute("fileBusinessRegistrationCertificate", CommonUtils
						.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileBusinessRegistrationCertificate())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant())) {
				model.addAttribute("fileDecisionToAppointChiefAccountant", CommonUtils
						.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileInvestmentCertificate())) {
				model.addAttribute("fileInvestmentCertificate",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileInvestmentCertificate())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileCompanyCharter())) {
				model.addAttribute("fileCompanyCharter",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileCompanyCharter())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileSealSpecimen())) {
				model.addAttribute("fileSealSpecimen",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileSealSpecimen())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileFatcaForms())) {
				model.addAttribute("fileFatcaForms",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileFatcaForms())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileOthers())) {
				model.addAttribute("fileOthers",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileOthers())));
			}

			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileKy())) {
				model.addAttribute("fileKy",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileKy())));
			}
			if (!StringUtils.isEmpty(ekycDoanhNghiep.getFileDangKy())) {
				model.addAttribute("fileDangKy",
						CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileDangKy())));
			}

			model.addAttribute("legalRepresentators", ekycDoanhNghiep.getLegalRepresentator());
			//if (ekycDoanhNghiep.getHaveAChiefAccountant().equals("yes")) {
				model.addAttribute("chiefAccountants", ekycDoanhNghiep.getChiefAccountant());
			//} else {
			//	model.addAttribute("chiefAccountants", ekycDoanhNghiep.getPersonAuthorizedChiefAccountant());
			//}
			model.addAttribute("personAuthorizedAccountHolders", ekycDoanhNghiep.getPersonAuthorizedAccountHolder());
			model.addAttribute("listOfLeaders", ekycDoanhNghiep.getListOfLeaders());
			System.out.println("ban lanh dao:"+ekycDoanhNghiep.getListOfLeaders());

			model.addAttribute("ekycDoanhNghiepTable", ekycDoanhNghiepTableHistory);
			model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);
			model.addAttribute("ktraDoanhNghiep", ktraDoanhNghiep);
			System.out.println("Ktra doanh nghiep:"+ ktraDoanhNghiep);
			model.addAttribute("ekycDoanhNghiepListAccount", ekycDoanhNghiep.getListAccount());

			model.addAttribute("checkLegalRepFinish", checkFinish(ekycDoanhNghiep.getLegalRepresentator()));
			model.addAttribute("checkChiefAccFinish", checkChiefAccFinish(ekycDoanhNghiep.getChiefAccountant()));
			model.addAttribute("checkPAuthorChiefFinish",checkPersonAuthorizedChiefFinish(ekycDoanhNghiep.getPersonAuthorizedAccountHolder()));
			model.addAttribute("checkListOfLeadersFinish",
					checkListOfLeadersFinish(ekycDoanhNghiep.getListOfLeaders()));
			
			forwartParams(allParams, model);
			return "quantrikhachhang/doanhnghiep/xemhistory";
		} catch (Exception e) {
			System.out.println("vao day à");
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/ekyc-doanh-nghiep";
		}
	}
	private Object checkListOfLeadersFinish(ArrayList<InfoPerson> listOfLeaders) {
		boolean check = false;
		if(listOfLeaders != null) {
			if (listOfLeaders.size() == 0) {
				check = false;
			}else if (listOfLeaders.size() > 0) {
				for (int i = 0; i < listOfLeaders.size(); i++) {
					if (listOfLeaders.get(i).getKiemTra() == null) {
						check = false;
					} else if (listOfLeaders.get(i).getKiemTra().equals("update")) {
						check = true;
					} else {
						check = false;
					}
					// check = true;
				}
			}
		}else {
			check = false;
		}
		
		return check;
	}

	private Object checkPersonAuthorizedChiefFinish(ArrayList<InfoPerson> personAuthorizedChief) {
		boolean check = false;
		if(personAuthorizedChief != null) {
			if (personAuthorizedChief.size() == 0) {
				check = false;
			}else if(personAuthorizedChief.size() > 0){
				for (int i = 0; i < personAuthorizedChief.size(); i++) {
					System.out.println(personAuthorizedChief.get(i).getKiemTra());
					if (personAuthorizedChief.get(i).getKiemTra() == null) {
						check = false;
					} else if (personAuthorizedChief.get(i).getKiemTra().equals("update")) {
						check = true;
					} else {
						check = false;
					}
					// check = true;
				}
			}
		}else {
			check = false;
		}
		
			
		
		return check;
	}

	private Object checkChiefAccFinish(ArrayList<InfoPerson> chiefAccountant) {
		boolean check = false;
		if(chiefAccountant != null) {
			if (chiefAccountant.size() == 0) {
				check = false;
			}else if(chiefAccountant.size() > 0) {
				for (int i = 0; i < chiefAccountant.size(); i++) {
					System.out.println(chiefAccountant.get(i).getKiemTra());
					if (chiefAccountant.get(i).getKiemTra() == null) {
						check = false;
					} else if (chiefAccountant.get(i).getKiemTra().equals("update")) {
						check = true;
					} else {
						check = false;
					}
					// check = true;
				}	
			}
		}else {
			check = false;
		}
		
			
		
		return check;
	}

	private Object checkFinish(ArrayList<InfoPerson> legalRepresentator) {
		boolean check = false;
		if(legalRepresentator != null) {
			if (legalRepresentator.size() == 0) {
				check = false;
			}else if(legalRepresentator.size() > 0) {
				for (int i = 0; i < legalRepresentator.size(); i++) {
					System.out.println(legalRepresentator.get(i).getKiemTra());
					if (legalRepresentator.get(i).getKiemTra() == null) {
						check = false;
					} else if (legalRepresentator.get(i).getKiemTra().equals("update")) {
						check = true;
					} else {
						check = false;
					}
					// check = true;
				}
			}
		}else {
			check = false;
		}
		
			
		
		return check;
	}

}
