package fis.com.vn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.html2pdf.HtmlConverter;

import fis.com.vn.api.CallApiChuKy;
import fis.com.vn.api.CheckSumService;
import fis.com.vn.api.ReadOcrApi;
import fis.com.vn.api.entities.ParamPathImage;
import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Email;
import fis.com.vn.common.FileHandling;
import fis.com.vn.common.PdfHandling;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.EncryptionAES;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.Account;
import fis.com.vn.entities.EkycDoanhNghiep;
import fis.com.vn.entities.FormInfo;
import fis.com.vn.entities.InfoPerson;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.esigncloud.ESignCloudConstant;
import fis.com.vn.esigncloud.eSignCall;
import fis.com.vn.esigncloud.datatypes.SignCloudMetaData;
import fis.com.vn.esigncloud.datatypes.SignCloudResp;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;
import fis.com.vn.ocr.Ocr;
import fis.com.vn.repository.BusinessRepository;
import fis.com.vn.repository.ConfigRepository;
import fis.com.vn.repository.EkycDoanhNghiepHistoryRepository;
import fis.com.vn.repository.EkycDoanhNghiepRepository;
import fis.com.vn.repository.LogApiDetailRepository;
import fis.com.vn.repository.LogApiRepository;
import fis.com.vn.repository.UserInfoRepository;
import fis.com.vn.table.EkycDoanhNghiepTable;
import fis.com.vn.table.EkycDoanhNghiepTableHistory;
import fis.com.vn.table.LogApi;
import fis.com.vn.table.LogApiDetail;
import fis.com.vn.thread.EkycDoanhNghiepThread;

@Controller
public class EkycDoanhNghiep2Controller extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EkycDoanhNghiepAdminController.class);

	@Value("${LINK_ADMIN}")
	protected String LINK_ADMIN;

	@Value("${PATH_PDF_FILL_FORM}")
	protected String PATH_PDF_FILL_FORM;

	@Value("${MOI_TRUONG}")
	protected String MOI_TRUONG;

	@Autowired
	UserInfoRepository userInfoRepository;
	@Autowired
	ConfigRepository configRepository;
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	EkycDoanhNghiepRepository ekycDoanhNghiepRepository;
	@Autowired
	EkycDoanhNghiepThread ekycDoanhNghiepThread;
	@Autowired
	Email email;
	@Autowired
	PdfHandling pdfHandling;
	@Autowired
	EncryptionAES encryptionAES;
	@Autowired
	LogApiRepository logApiRepository;
	@Autowired
	LogApiDetailRepository logApiDetailRepository;

	@Autowired
	CallApiChuKy caliApiChuKy;

	@Autowired
	EkycDoanhNghiepHistoryRepository ekycDoanhNghiepHistoryRepository;

	@Autowired
	BusinessRepository businessRepository;

	@Autowired
	ReadOcrApi readOcrApi;

	@Autowired
	CheckSumService checkSumService;

	public String notificationTemplate = "[FPT-CA] Ma xac thuc (OTP) cua Quy khach la {AuthorizeCode}. Vui long dien ma so nay de ky Hop dong Dien Tu va khong cung cap OTP cho bat ky ai";
	public String notificationSubject = "[FPT-CA] Ma xac thuc (OTP)";

	@PostMapping(value = "/ekyc-enterprise/send-mail-edit", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String sendMailEdit(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		JSONObject jsonResp = new JSONObject();

		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		JSONObject params = new JSONObject(text);
		System.out.println(params.get("emailContractPersion"));
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository
				.findByUsername(req.getSession().getAttribute("b_username").toString());

		if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
			ssNoiDung(req);
		}
		EkycDoanhNghiepTable doanhNghiep1 = doanhNghiep;
		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep1.getNoiDung(), EkycDoanhNghiep.class);
		doanhNghiep1.setEmailNguoiLienHe(params.get("emailContractPersion").toString());
		doanhNghiep1.setSoDienThoaiNguoiLienHe(params.get("phoneContractPersion").toString());
		doanhNghiep1.setTenNguoiLienHe(params.get("nameContractPersion").toString());

		doanhNghiep1.setStep("11");
		if (doanhNghiep1.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {
			if (ekycDoanhNghiep.getAllInOne().equals("yes")) {

			}
			guiMailEkyc(ekycDoanhNghiep, req, doanhNghiep);

			LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
					+ "&tokenCheck=" + ekycDoanhNghiep.getTokenCheck() + "&type="
					+ Base64.getEncoder().encodeToString(Contains.NGUOI_DAI_DIEN_PHAP_LUAT.getBytes()));

			LOGGER.info(
					"Send mail: " + LINK_ADMIN + "/ekyc-enterprise/update-file?token=" + ekycDoanhNghiep.getToken());

			if (!MOI_TRUONG.equals("dev")) {
				System.err.println(params.get("emailContractPersion").toString());
				System.err.println(ekycDoanhNghiep.getToken());

				guiMailBoSungThongTin(ekycDoanhNghiep, params.get("emailContractPersion").toString());
			}
		} else if (doanhNghiep1.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
			guiMainUpdateEkyc(ekycDoanhNghiep, req, doanhNghiep);
			if (!MOI_TRUONG.equals("dev")) {
				guiMailBoSungThongTin(ekycDoanhNghiep, params.get("emailContractPersion").toString());

			}
		}
		if (ekycDoanhNghiep.getStatusStep3() != null) {
			if (doanhNghiep1.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)
					&& ekycDoanhNghiep.getStatusStep3().equals("yes") && ekycDoanhNghiep.getEditStatusBld().equals("no")
					&& ekycDoanhNghiep.getEditStatusKtt().equals("no")
					&& ekycDoanhNghiep.getEditStatusNddpl().equals("no")
					|| ekycDoanhNghiep.getEditStatusNuq().equals("no")
					|| ekycDoanhNghiep.getEditStatusNuqKtt().equals("no")) {

				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
					if (ip.getCheckMain().equals("yes")) {
						LOGGER.info("Send mail end: " + LINK_ADMIN + "/ekyc-enterprise/esign?token="
								+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck());
						if (!MOI_TRUONG.equals("dev"))
							guiMailYeuKyAOF(ekycDoanhNghiep, ip);

					}
				}
				if (!MOI_TRUONG.equals("dev")) {
					guiMailBoSungThongTin(ekycDoanhNghiep, params.get("emailContractPersion").toString());

				}

			}
		} else if (doanhNghiep1.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)
				&& ((ekycDoanhNghiep.getHaveAcccountHolder().equals("no"))
						|| (ekycDoanhNghiep.getHaveAChiefAccountant().equals("no")))) {
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				if (ip.getCheckMain().equals("yes")) {
					LOGGER.info("Send mail end: " + LINK_ADMIN + "/ekyc-enterprise/esign?token="
							+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck());
					if (!MOI_TRUONG.equals("dev"))
						guiMailYeuKyAOF(ekycDoanhNghiep, ip);
				}
			}

		}

		checkSumService.save(doanhNghiep1);

		// EkycDoanhNghiep ekycDoanhNghiep = new
		// Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

		// ekycDoanhNghiepThread.startNoiDung(doanhNghiep1.getUsername(),
		// doanhNghiep1.getMaDoanhNghiep());
		// System.out.println("checkNoidung: " + doanhNghiep1.getCheckNoiDung());
		return jsonResp.toString();

	}

	public String ssNoiDung(HttpServletRequest req) throws JsonMappingException {
		Object username = req.getSession().getAttribute("b_username");
		if (username == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiepTable ekycDoanhNghiepTable = ekycDoanhNghiepRepository.findByUsername(username.toString());
		if (ekycDoanhNghiepTable == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiep ekycDoanhNghiep1 = new Gson().fromJson(ekycDoanhNghiepTable.getNoiDung(),
				EkycDoanhNghiep.class);
		EkycDoanhNghiep ekycDoanhNghiep2 = new Gson().fromJson(ekycDoanhNghiepTable.getSsNoiDung(),
				EkycDoanhNghiep.class);
		boolean check = false;
		if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant() == null)
			check = true;

		if (!ekycDoanhNghiep1.getNameOfTheAccountHolder().equals(ekycDoanhNghiep2.getNameOfTheAccountHolder())
				|| !ekycDoanhNghiep1.getDateAccountOpening().equals(ekycDoanhNghiep2.getDateAccountOpening())
				|| !ekycDoanhNghiep1.getNameCompany().equals(ekycDoanhNghiep2.getNameCompany())
				|| !ekycDoanhNghiep1.getRegisteredAddress().equals(ekycDoanhNghiep2.getRegisteredAddress())
				|| !ekycDoanhNghiep1.getOperatingAddress().equals(ekycDoanhNghiep2.getOperatingAddress())
				|| !ekycDoanhNghiep1.getCountryOfIncorporation().equals(ekycDoanhNghiep2.getCountryOfIncorporation())
				|| !ekycDoanhNghiep1.getStraight2BankGroupID().equals(ekycDoanhNghiep2.getStraight2BankGroupID())
				|| !ekycDoanhNghiep1.getMailingAddress().equals(ekycDoanhNghiep2.getMailingAddress())
				|| !ekycDoanhNghiep1.getSwiftBankIDCode().equals(ekycDoanhNghiep2.getSwiftBankIDCode())
				|| !ekycDoanhNghiep1.getMobileOfficeTelephone().equals(ekycDoanhNghiep2.getMobileOfficeTelephone())
				|| !ekycDoanhNghiep1.getContactPerson().equals(ekycDoanhNghiep2.getContactPerson())
				|| !ekycDoanhNghiep1.getEmailAddress().equals(ekycDoanhNghiep2.getEmailAddress())
				|| !ekycDoanhNghiep1.getListAccount().equals(ekycDoanhNghiep2.getListAccount())
				|| !ekycDoanhNghiep1.getShortName().equals(ekycDoanhNghiep2.getShortName())
				|| !ekycDoanhNghiep1.getNameInEnglish().equals(ekycDoanhNghiep2.getNameInEnglish())
				|| !ekycDoanhNghiep1.getFaxNumber().equals(ekycDoanhNghiep2.getFaxNumber())
				|| !ekycDoanhNghiep1.getTaxCode().equals(ekycDoanhNghiep2.getTaxCode())
				|| !ekycDoanhNghiep1.getApplicableAccountingSystems()
						.equals(ekycDoanhNghiep2.getApplicableAccountingSystems())
				|| !ekycDoanhNghiep1.getTaxMode().equals(ekycDoanhNghiep2.getTaxMode())
				|| !ekycDoanhNghiep1.getResidentStatus().equals(ekycDoanhNghiep2.getResidentStatus())
				|| !ekycDoanhNghiep1.getResidentStatus().equals(ekycDoanhNghiep2.getResidentStatus())
				|| !ekycDoanhNghiep1.getBusinessActivities().equals(ekycDoanhNghiep2.getBusinessActivities())
				|| !ekycDoanhNghiep1.getYearlyAveragenumber().equals(ekycDoanhNghiep2.getYearlyAveragenumber())
				|| !ekycDoanhNghiep1.getTotalSalesTurnover().equals(ekycDoanhNghiep2.getTotalSalesTurnover())
				|| !ekycDoanhNghiep1.getTotalCapital().equals(ekycDoanhNghiep2.getTotalCapital())
				|| !ekycDoanhNghiep1.getTotalCapital().equals(ekycDoanhNghiep2.getTotalCapital())
				|| !ekycDoanhNghiep1.getAgreeToReceive().equals(ekycDoanhNghiep2.getAgreeToReceive())) {
			ekycDoanhNghiep1.setNameOfTheAccountHolder(ekycDoanhNghiep2.getNameOfTheAccountHolder());
			ekycDoanhNghiep1.setNumber(ekycDoanhNghiep2.getNumber());
			ekycDoanhNghiep1.setDateAccountOpening(ekycDoanhNghiep2.getDateAccountOpening());
			ekycDoanhNghiep1.setNameCompany(ekycDoanhNghiep2.getNameCompany());
			ekycDoanhNghiep1.setRegisteredAddress(ekycDoanhNghiep2.getRegisteredAddress());
			ekycDoanhNghiep1.setOperatingAddress(ekycDoanhNghiep2.getOperatingAddress());
			ekycDoanhNghiep1.setCountryOfIncorporation(ekycDoanhNghiep2.getCountryOfIncorporation());
			ekycDoanhNghiep1.setStraight2BankGroupID(ekycDoanhNghiep2.getStraight2BankGroupID());
			ekycDoanhNghiep1.setMailingAddress(ekycDoanhNghiep2.getMailingAddress());
			ekycDoanhNghiep1.setSwiftBankIDCode(ekycDoanhNghiep2.getSwiftBankIDCode());
			ekycDoanhNghiep1.setMobileOfficeTelephone(ekycDoanhNghiep2.getMobileOfficeTelephone());
			ekycDoanhNghiep1.setContactPerson(ekycDoanhNghiep2.getContactPerson());
			ekycDoanhNghiep1.setEmailAddress(ekycDoanhNghiep2.getEmailAddress());
			ekycDoanhNghiep1.setListAccount(ekycDoanhNghiep2.getListAccount());
			ekycDoanhNghiep1.setShortName(ekycDoanhNghiep2.getShortName());
			ekycDoanhNghiep1.setNameInEnglish(ekycDoanhNghiep2.getNameInEnglish());
			ekycDoanhNghiep1.setFaxNumber(ekycDoanhNghiep2.getFaxNumber());
			ekycDoanhNghiep1.setTaxCode(ekycDoanhNghiep2.getTaxCode());
			ekycDoanhNghiep1.setApplicableAccountingSystems(ekycDoanhNghiep2.getApplicableAccountingSystems());
			ekycDoanhNghiep1.setTaxMode(ekycDoanhNghiep2.getTaxMode());
			ekycDoanhNghiep1.setResidentStatus(ekycDoanhNghiep2.getResidentStatus());
			ekycDoanhNghiep1.setBusinessActivities(ekycDoanhNghiep2.getBusinessActivities());
			ekycDoanhNghiep1.setYearlyAveragenumber(ekycDoanhNghiep2.getYearlyAveragenumber());
			ekycDoanhNghiep1.setTotalSalesTurnover(ekycDoanhNghiep2.getTotalSalesTurnover());
			ekycDoanhNghiep1.setTotalCapital(ekycDoanhNghiep2.getTotalCapital());
			ekycDoanhNghiep1.setAgreeToReceive(ekycDoanhNghiep2.getAgreeToReceive());
			ekycDoanhNghiep1.setStatusStep3("yes");
		} else {
			ekycDoanhNghiep1.setStatusStep3("no");
		}

		if (ekycDoanhNghiep2.getAllInOne().equals("no")) {
			if (ekycDoanhNghiep1.getListOfLeaders().size() <= ekycDoanhNghiep2.getListOfLeaders().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getListOfLeaders().size()) {
						ekycDoanhNghiep2.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().add(ekycDoanhNghiep2.getListOfLeaders().get(y));

					} else if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusBld("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());

						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusBld("no");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
					}

				}
			} else if (ekycDoanhNghiep1.getListOfLeaders().size() > ekycDoanhNghiep2.getListOfLeaders().size()) {

				for (int i = 0; i < ekycDoanhNghiep1.getListOfLeaders().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getListOfLeaders().get(i).getId(),
							ekycDoanhNghiep2.getListOfLeaders())) {
						ekycDoanhNghiep1.getListOfLeaders().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {

					if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());

						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
					}
				}

			}

			ekycDoanhNghiep1.setAllInOne(ekycDoanhNghiep2.getAllInOne());
			ekycDoanhNghiep1.setHaveAcccountHolder(ekycDoanhNghiep2.getHaveAcccountHolder());
			if (ekycDoanhNghiep1.getLegalRepresentator().size() <= ekycDoanhNghiep2.getLegalRepresentator().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getLegalRepresentator().size()) {
						ekycDoanhNghiep2.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().add(ekycDoanhNghiep2.getLegalRepresentator().get(y));

					} else if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
					}
				}

			} else if (ekycDoanhNghiep1.getLegalRepresentator().size() > ekycDoanhNghiep2.getLegalRepresentator()
					.size()) {

				for (int i = 0; i < ekycDoanhNghiep1.getLegalRepresentator().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getLegalRepresentator().get(i).getId(),
							ekycDoanhNghiep2.getLegalRepresentator())) {
						ekycDoanhNghiep1.getLegalRepresentator().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
					if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
					}
				}

			}

			ekycDoanhNghiep1.setHaveAChiefAccountant(ekycDoanhNghiep2.getHaveAChiefAccountant());
			if (ekycDoanhNghiep1.getChiefAccountant().size() <= ekycDoanhNghiep2.getChiefAccountant().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getChiefAccountant().size()) {
						ekycDoanhNghiep2.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getChiefAccountant().add(ekycDoanhNghiep2.getChiefAccountant().get(y));
						break;
					} else if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.setEditStatusKtt("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setId(ekycDoanhNghiep2.getChiefAccountant().get(y).getId());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusKtt("no");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
					}

				}
			} else if (ekycDoanhNghiep1.getChiefAccountant().size() > ekycDoanhNghiep2.getChiefAccountant().size()) {
				for (int i = 0; i < ekycDoanhNghiep1.getChiefAccountant().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getChiefAccountant().get(i).getId(),
							ekycDoanhNghiep2.getChiefAccountant())) {
						ekycDoanhNghiep1.getChiefAccountant().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
					if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
					}
				}
			}

			if (ekycDoanhNghiep2.getPersonAuthorizedAccountHolder() != null) {
				if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder() == null) {
					ekycDoanhNghiep2.setEditStatusNuq("yes");
					for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
						ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
					}
					updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);
				} else {
					if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size() <= ekycDoanhNghiep2
							.getPersonAuthorizedAccountHolder().size()) {
						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
							if (y + 1 > ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size()) {
								ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder()
										.add(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y));

							} else if (!ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getEmail().equals(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail())
									|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getPhone().equals(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNuq("yes");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
										.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEmail(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setHoTen(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setPhone(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone());

								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTokenCheck(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTokenCheck());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
										.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
										.setTime(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNuq("no");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("no");
							}

						}
					} else if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size() > ekycDoanhNghiep2
							.getPersonAuthorizedAccountHolder().size()) {
						for (int i = 0; i < ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size(); i++) {
							if (!checkItemEmpty(ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(i).getId(),
									ekycDoanhNghiep2.getPersonAuthorizedAccountHolder())) {
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().remove(i);
								i--;
							}

						}
						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
							if (!ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getEmail().equals(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail())
									|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getPhone().equals(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEmail(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setHoTen(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setPhone(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone());

								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTokenCheck(
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTokenCheck());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
										.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
										.setTime(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("no");
							}
						}
					}
				}
			}

			if (ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant() != null) {
				// System.out.println("sahjdsahj:
				// "+ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant());
				if (check == true) {
					ekycDoanhNghiep2.setEditStatusNuq("yes");
					for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
						ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
					}
					updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);
				} else {
					if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size() <= ekycDoanhNghiep2
							.getPersonAuthorizedChiefAccountant().size()
							&& ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant() != null) {
						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
							if (y + 1 > ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size()) {
								ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant()
										.add(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y));

							} else if (!ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getEmail().equals(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail())
									|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getPhone().equals(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNuqKtt("yes");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y)
										.setId(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getId());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEmail(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setHoTen(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setPhone(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTokenCheck(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTokenCheck());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTime(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNuqKtt("no");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("no");
							}

						}
					} else if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size() > ekycDoanhNghiep2
							.getPersonAuthorizedChiefAccountant().size()) {
						for (int i = 0; i < ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size(); i++) {
							if (!checkItemEmpty(ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(i).getId(),
									ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant())) {
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().remove(i);
								i--;
							}

						}
						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
							if (!ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getEmail().equals(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail())
									|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getPhone().equals(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEmail(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setHoTen(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setPhone(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone());

								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTokenCheck(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTokenCheck());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y)
										.setId(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getId());
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTime(
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("no");
							}
						}
					}
				}

			}

		}
		if (ekycDoanhNghiep2.getAllInOne().equals("yes")) {
			if (ekycDoanhNghiep1.getListOfLeaders().size() <= ekycDoanhNghiep2.getListOfLeaders().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getListOfLeaders().size()) {
						ekycDoanhNghiep2.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().add(ekycDoanhNghiep2.getListOfLeaders().get(y));

					} else if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusBld("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusBld("no");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
					}

				}
			} else if (ekycDoanhNghiep1.getListOfLeaders().size() > ekycDoanhNghiep2.getListOfLeaders().size()) {

				for (int i = 0; i < ekycDoanhNghiep1.getListOfLeaders().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getListOfLeaders().get(i).getId(),
							ekycDoanhNghiep2.getListOfLeaders())) {
						ekycDoanhNghiep1.getListOfLeaders().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {

					if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
							|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());

						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
						ekycDoanhNghiep1.getListOfLeaders().get(y)
								.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
					}
				}

			}

			ekycDoanhNghiep1.setAllInOne(ekycDoanhNghiep2.getAllInOne());
			ekycDoanhNghiep1.setHaveAcccountHolder(ekycDoanhNghiep2.getHaveAcccountHolder());
			if (ekycDoanhNghiep1.getLegalRepresentator().size() <= ekycDoanhNghiep2.getLegalRepresentator().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getLegalRepresentator().size()) {
						ekycDoanhNghiep2.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().add(ekycDoanhNghiep2.getLegalRepresentator().get(y));

					} else if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());

					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
					}

				}

			} else if (ekycDoanhNghiep1.getLegalRepresentator().size() > ekycDoanhNghiep2.getLegalRepresentator()
					.size()) {

				for (int i = 0; i < ekycDoanhNghiep1.getLegalRepresentator().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getLegalRepresentator().get(i).getId(),
							ekycDoanhNghiep2.getLegalRepresentator())) {
						ekycDoanhNghiep1.getLegalRepresentator().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
					if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
							|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
						ekycDoanhNghiep1.getLegalRepresentator().get(y)
								.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
					}
				}

			}

			ekycDoanhNghiep1.setHaveAChiefAccountant(ekycDoanhNghiep2.getHaveAChiefAccountant());
			if (ekycDoanhNghiep1.getChiefAccountant().size() <= ekycDoanhNghiep2.getChiefAccountant().size()) {
				for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
					if (y + 1 > ekycDoanhNghiep1.getChiefAccountant().size()) {
						ekycDoanhNghiep2.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getChiefAccountant().add(ekycDoanhNghiep2.getChiefAccountant().get(y));
						break;
					} else if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.setEditStatusKtt("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setId(ekycDoanhNghiep2.getChiefAccountant().get(y).getId());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusKtt("no");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
					}

				}
			} else if (ekycDoanhNghiep1.getChiefAccountant().size() > ekycDoanhNghiep2.getChiefAccountant().size()) {
				for (int i = 0; i < ekycDoanhNghiep1.getChiefAccountant().size(); i++) {
					if (!checkItemEmpty(ekycDoanhNghiep1.getChiefAccountant().get(i).getId(),
							ekycDoanhNghiep2.getChiefAccountant())) {
						ekycDoanhNghiep1.getChiefAccountant().remove(i);
						i--;
					}

				}
				for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
					if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
							.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
							|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
						ekycDoanhNghiep1.setEditStatusNddpl("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
						ekycDoanhNghiep1.getChiefAccountant().get(y)
								.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
					} else {
						ekycDoanhNghiep1.setEditStatusNddpl("no");
						ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
					}
				}
			}
		}

		if (ekycDoanhNghiep1.getUserDesignation().size() <= ekycDoanhNghiep2.getUserDesignation().size()) {
			for (int y = 0; y < ekycDoanhNghiep2.getUserDesignation().size(); y++) {
				if (y + 1 > ekycDoanhNghiep1.getUserDesignation().size()) {
					ekycDoanhNghiep2.getUserDesignation().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getUserDesignation().add(ekycDoanhNghiep2.getUserDesignation().get(y));

				} else if (!ekycDoanhNghiep1.getUserDesignation().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getEmail())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getSoCmt()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getSoCmt())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getChapThuanLenh()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenh())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getChapThuanLenhDongThoi()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenhDongThoi())) {
					ekycDoanhNghiep1.getUserDesignation().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setEmail(ekycDoanhNghiep2.getUserDesignation().get(y).getEmail());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setHoTen(ekycDoanhNghiep2.getUserDesignation().get(y).getHoTen());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setSoCmt(ekycDoanhNghiep2.getUserDesignation().get(y).getSoCmt());

					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setTaoLenh(ekycDoanhNghiep2.getUserDesignation().get(y).getTaoLenh());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setBaoCao(ekycDoanhNghiep2.getUserDesignation().get(y).getBaoCao());
					ekycDoanhNghiep1.getUserDesignation().get(y).setChapThuanLenhDongThoi(
							ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenhDongThoi());

					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setChapThuanLenh(ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenh());

				} else {
					ekycDoanhNghiep1.getUserDesignation().get(y).setEditStatus("no");
				}
			}

		} else if (ekycDoanhNghiep1.getUserDesignation().size() > ekycDoanhNghiep2.getUserDesignation().size()) {
			for (int i = 0; i < ekycDoanhNghiep1.getUserDesignation().size(); i++) {
				if (!checkItemEmpty(ekycDoanhNghiep1.getUserDesignation().get(i).getId(),
						ekycDoanhNghiep2.getUserDesignation())) {
					ekycDoanhNghiep1.getUserDesignation().remove(i);
					i--;
				}

			}
			for (int y = 0; y < ekycDoanhNghiep1.getUserDesignation().size(); y++) {
				if (!ekycDoanhNghiep1.getUserDesignation().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getEmail())
						|| !ekycDoanhNghiep1.getUserDesignation().get(y).getSoCmt()
								.equals(ekycDoanhNghiep2.getUserDesignation().get(y).getSoCmt())) {
					ekycDoanhNghiep1.getUserDesignation().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setEmail(ekycDoanhNghiep2.getUserDesignation().get(y).getEmail());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setHoTen(ekycDoanhNghiep2.getUserDesignation().get(y).getHoTen());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setSoCmt(ekycDoanhNghiep2.getUserDesignation().get(y).getSoCmt());

					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setTaoLenh(ekycDoanhNghiep2.getUserDesignation().get(y).getTaoLenh());
					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setBaoCao(ekycDoanhNghiep2.getUserDesignation().get(y).getBaoCao());
					ekycDoanhNghiep1.getUserDesignation().get(y).setChapThuanLenhDongThoi(
							ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenhDongThoi());

					ekycDoanhNghiep1.getUserDesignation().get(y)
							.setChapThuanLenh(ekycDoanhNghiep2.getUserDesignation().get(y).getChapThuanLenh());
				} else {
					ekycDoanhNghiep1.setEditStatusNddpl("no");
					ekycDoanhNghiep1.getUserDesignation().get(y).setEditStatus("no");
				}
			}
		}

		ekycDoanhNghiepTable.setNoiDung(new Gson().toJson(ekycDoanhNghiep1));

		checkSumService.save(ekycDoanhNghiepTable);

		return null;
	}

	@GetMapping(value = "/ekyc-enterprise/update-ekyc-business")
	public String updateEkycBusiness(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		ParamsKbank params = new ParamsKbank();
		System.out.println("sadsadasdasd: " + req.getSession().getAttribute("b_username"));
		Object username = req.getSession().getAttribute("b_username");
		if (username == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiepTable ekycDoanhNghiepTable = ekycDoanhNghiepRepository.findByUsername(username.toString());
		if (ekycDoanhNghiepTable == null)
			return "demo/doanhnghiep2/step/steperror";
		/////////////
		ekycDoanhNghiepTable.setNoiDung(ekycDoanhNghiepTable.getNoiDung());
		checkSumService.save(ekycDoanhNghiepTable);

		EkycDoanhNghiepTable ekycDoanhNghiepTable1 = ekycDoanhNghiepRepository.findByUsername(username.toString());
		EkycDoanhNghiep ekycDoanhNghiep = new EkycDoanhNghiep();
		if (ekycDoanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
			ekycDoanhNghiep = new Gson().fromJson(ekycDoanhNghiepTable1.getSsNoiDung(), EkycDoanhNghiep.class);
		} else {
			ekycDoanhNghiep = new Gson().fromJson(ekycDoanhNghiepTable1.getNoiDung(), EkycDoanhNghiep.class);
		}
		// LOGGER.info("ekycDoanhNghiepTable1: "+ekycDoanhNghiepTable1.toString());
		// LOGGER.info("ekycDoanhNghiep: "+ekycDoanhNghiep.toString());

		model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);
		model.addAttribute("ekycDoanhNghiepTable", ekycDoanhNghiepTable1);
		// model.addAttribute("agreeToReceive", ekycDoanhNghiep.getAgreeToReceive());
		// model.addAttribute("typeDocument", ekycDoanhNghiep.getTypeDocument());
		if (ekycDoanhNghiep.getListAccount() == null) {

			ArrayList<Account> accounts = new ArrayList<>();
			accounts.add(new Account());
			model.addAttribute("listAccount", accounts);
		} else if (ekycDoanhNghiep.getListAccount().size() > 0) {

			model.addAttribute("listAccount", ekycDoanhNghiep.getListAccount());
		}
		if (ekycDoanhNghiep.getUserDesignation() == null) {

			ArrayList<InfoPerson> infoPersons = new ArrayList<>();
			infoPersons.add(new InfoPerson());
			model.addAttribute("userDesignation", infoPersons);
		} else if (ekycDoanhNghiep.getUserDesignation().size() > 0) {

			model.addAttribute("userDesignation", ekycDoanhNghiep.getUserDesignation());
		}

		if (ekycDoanhNghiep.getChiefAccountant() == null) {

			ArrayList<InfoPerson> infoPersons = new ArrayList<>();
			infoPersons.add(new InfoPerson());
			model.addAttribute("chiefAccountant", infoPersons);
			model.addAttribute("haveAChiefAccountant", "no");
		} else if (ekycDoanhNghiep.getChiefAccountant().size() > 0) {

			model.addAttribute("chiefAccountant", ekycDoanhNghiep.getChiefAccountant());
			model.addAttribute("haveAChiefAccountant", ekycDoanhNghiep.getHaveAChiefAccountant());
		}

		if (ekycDoanhNghiep.getListOfLeaders() == null) {
			ArrayList<InfoPerson> infoPersons = new ArrayList<>();
			infoPersons.add(new InfoPerson());
			model.addAttribute("listOfLeaders", infoPersons);
		} else if (ekycDoanhNghiep.getListOfLeaders().size() > 0) {
			model.addAttribute("listOfLeaders", ekycDoanhNghiep.getListOfLeaders());
		}

		if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder() == null) {

			ArrayList<InfoPerson> infoPersons = new ArrayList<>();
			infoPersons.add(new InfoPerson());

			model.addAttribute("personAuthorizedAccountHolder", infoPersons);
		} else if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder().size() > 0) {

			model.addAttribute("personAuthorizedAccountHolder", ekycDoanhNghiep.getPersonAuthorizedAccountHolder());

		}
		if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant() == null) {

			ArrayList<InfoPerson> infoPersons = new ArrayList<>();
			infoPersons.add(new InfoPerson());
			model.addAttribute("personAuthorizedChiefAccountant", infoPersons);
		} else if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant().size() > 0) {
			model.addAttribute("personAuthorizedChiefAccountant", ekycDoanhNghiep.getPersonAuthorizedChiefAccountant());

		}

		if (ekycDoanhNghiep.getLegalRepresentator() == null) {
			ArrayList<InfoPerson> legalRepresentator = new ArrayList<>();

			legalRepresentator.add(new InfoPerson());
			model.addAttribute("haveAcccountHolder", "no");
			model.addAttribute("allInOne", "no");
			model.addAttribute("checkMain", "no");
			model.addAttribute("legalRepresentator", legalRepresentator);

		} else if (ekycDoanhNghiep.getLegalRepresentator().size() > 0) {

			model.addAttribute("haveAcccountHolder", ekycDoanhNghiep.getHaveAcccountHolder());
			model.addAttribute("allInOne", ekycDoanhNghiep.getAllInOne());
			model.addAttribute("legalRepresentator", ekycDoanhNghiep.getLegalRepresentator());

		}

		model.addAttribute("step", Integer.parseInt(ekycDoanhNghiepTable1.getStep()));

		model.addAttribute("fileBusinessRegistration",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileBusinessRegistration())));
		model.addAttribute("fileAppointmentOfChiefAccountant",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant())));
		model.addAttribute("fileBusinessRegistrationCertificate",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileBusinessRegistrationCertificate())));
		model.addAttribute("fileDecisionToAppointChiefAccountant",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant())));
		model.addAttribute("fileInvestmentCertificate",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileInvestmentCertificate())));
		model.addAttribute("fileCompanyCharter",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileCompanyCharter())));
		model.addAttribute("fileSealSpecimen", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileSealSpecimen())));
		model.addAttribute("fileFatcaForms", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileFatcaForms())));
		model.addAttribute("fileOthers", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileOthers())));

		model.addAttribute("fileBusinessRegistrationBase64",
				CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileBusinessRegistration())));
		model.addAttribute("fileAppointmentOfChiefAccountantBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileAppointmentOfChiefAccountant()))));
		model.addAttribute("fileBusinessRegistrationCertificateBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileBusinessRegistrationCertificate()))));
		model.addAttribute("fileDecisionToAppointChiefAccountantBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant()))));
		model.addAttribute("fileInvestmentCertificateBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileInvestmentCertificate()))));
		model.addAttribute("fileCompanyCharterBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileCompanyCharter()))));
		model.addAttribute("fileSealSpecimenBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileSealSpecimen()))));
		model.addAttribute("fileFatcaFormsBase64",
				CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileFatcaForms())));
		model.addAttribute("fileOthersBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileOthers()))));

		model.addAttribute("token", ekycDoanhNghiepTable1.getToken());
		model.addAttribute("statusDonKy", ekycDoanhNghiepTable1.getStatusDonKy());
		params.setToken(ekycDoanhNghiepTable1.getToken());
		params.setQueryParams(req.getQueryString());

		setParams(params, req);

		return "ekyc/ekycdn2";
	}

	@GetMapping(value = "/ekyc-enterprise/update-file")
	public String updateFile(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		ParamsKbank params = new ParamsKbank();
		String token = allParams.get("token");
		if (token == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(token);
		if (doanhNghiep == null)
			return "demo/doanhnghiep2/step/steperror";

		if (doanhNghiep.getStatus().equals("success"))
			return "demo/doanhnghiep2/step/steperror";

		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

		if (ekycDoanhNghiep.getStatus() != null
				&& ekycDoanhNghiep.getStatus().equals(Contains.TRANG_THAI_KY_THANH_CONG))
			return "demo/doanhnghiep2/step/stepinfofile";

		model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);
		model.addAttribute("userDesignation", ekycDoanhNghiep.getUserDesignation());
		model.addAttribute("listAccount", ekycDoanhNghiep.getListAccount());
		model.addAttribute("fileBusinessRegistration",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileBusinessRegistration())));
		model.addAttribute("fileAppointmentOfChiefAccountant",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant())));
		model.addAttribute("fileBusinessRegistrationCertificate",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileBusinessRegistrationCertificate())));
		model.addAttribute("fileDecisionToAppointChiefAccountant",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant())));
		model.addAttribute("fileInvestmentCertificate",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileInvestmentCertificate())));
		model.addAttribute("fileCompanyCharter",
				encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileCompanyCharter())));
		model.addAttribute("fileSealSpecimen", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileSealSpecimen())));
		model.addAttribute("fileFatcaForms", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileFatcaForms())));
		model.addAttribute("fileOthers", encode(encryptionAES.encrypt(ekycDoanhNghiep.getFileOthers())));

		model.addAttribute("fileBusinessRegistrationBase64",
				CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileBusinessRegistration())));
		model.addAttribute("fileAppointmentOfChiefAccountantBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileAppointmentOfChiefAccountant()))));
		model.addAttribute("fileBusinessRegistrationCertificateBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileBusinessRegistrationCertificate()))));
		model.addAttribute("fileDecisionToAppointChiefAccountantBase64", CommonUtils
				.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant()))));
		model.addAttribute("fileInvestmentCertificateBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileInvestmentCertificate()))));
		model.addAttribute("fileCompanyCharterBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileCompanyCharter()))));
		model.addAttribute("fileSealSpecimenBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileSealSpecimen()))));
		model.addAttribute("fileFatcaFormsBase64",
				CommonUtils.encodeFileToBase64Binary(new File(ekycDoanhNghiep.getFileFatcaForms())));
		model.addAttribute("fileOthersBase64",
				CommonUtils.encodeFileToBase64Binary(new File((ekycDoanhNghiep.getFileOthers()))));

		model.addAttribute("token", token);
		params.setToken(token);
		params.setQueryParams(req.getQueryString());

		setParams(params, req);

		return "ekyc/ekycdnUpdateFile";
	}

	private String encode(String value) {
		if (StringUtils.isEmpty(value))
			return null;
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	@PostMapping(value = "/ekyc-enterprise/update-file", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateFilePost(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject2 = new JSONObject();

		try {
			ParamsKbank params = getParams(req);
			EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
			if (doanhNghiep == null) {
				jsonObject2.put("status", 400);
			}

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

			EkycDoanhNghiep ekycDoanhNghiepDb = gson.fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);

			FileHandling fileHandling = new FileHandling();

			ekycDoanhNghiep.setFileBusinessRegistration(
					luuAnh(ekycDoanhNghiep.getFileBusinessRegistration(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileAppointmentOfChiefAccountant(
					luuAnh(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileBusinessRegistrationCertificate(
					luuAnh(ekycDoanhNghiep.getFileBusinessRegistrationCertificate(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileDecisionToAppointChiefAccountant(
					luuAnh(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileInvestmentCertificate(
					luuAnh(ekycDoanhNghiep.getFileInvestmentCertificate(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep
					.setFileCompanyCharter(luuAnh(ekycDoanhNghiep.getFileCompanyCharter(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileSealSpecimen(luuAnh(ekycDoanhNghiep.getFileSealSpecimen(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileFatcaForms(luuAnh(ekycDoanhNghiep.getFileFatcaForms(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileOthers(luuAnh(ekycDoanhNghiep.getFileOthers(), fileHandling, "abc.pdf"));

			updateObjectToObject(ekycDoanhNghiepDb, ekycDoanhNghiep);

			doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));

			checkSumService.save(doanhNghiep);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject2.put("status", 200);
		return jsonObject2.toString();
	}

	@GetMapping(value = { "/ekyc-enterprise/pdf-byte" }, produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getPdf(HttpServletResponse resp, @RequestParam Map<String, String> allParams,
			HttpServletRequest req) {
		try {
			ParamsKbank params = getParams(req);
			if (params == null)
				return null;
			if (!allParams.get("token").equals(params.getToken()))
				return null;
			String pathImg = encryptionAES.decrypt(allParams.get("path"));
			File file = new File(pathImg);

			byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(file));

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@GetMapping(value = "/ekyc-enterprise/upload")
	public String upload(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		ParamsKbank params = new ParamsKbank();
		String token = allParams.get("token");
		String tokenCheck = allParams.get("tokenCheck");
		if (token == null)
			return "demo/doanhnghiep2/step/steperror";
		if (tokenCheck == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(token);
		if (doanhNghiep == null)
			return "demo/doanhnghiep2/step/steperror";

		String loai = new String(Base64.getDecoder().decode(allParams.get("type")));
		params.setCode(loai);
		params.setToken(token);
		params.setTokenCheck(tokenCheck);
		params.setQueryParams(req.getQueryString());

		setParams(params, req);

		return "demo/doanhnghiep2/step/uploadfile";
	}

	@PostMapping(value = "/ekyc-enterprise/upload")
	public String postupload(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			@RequestParam("files") MultipartFile[] files) throws IOException {
		try {
			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:" + "/ekyc-enterprise/upload";

			EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
			if (doanhNghiep == null)
				return "demo/doanhnghiep2/step/steperror";

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

			if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, files);
					}
				}

			} else if (params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
				if (ekycDoanhNghiep.getHaveAChiefAccountant().equals("yes")) {
					for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
						if (ip.getTokenCheck().equals(params.getTokenCheck())) {
							updateInfo(ip, allParams, params, files);
						}
					}
				}
			} else if (params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, files);
					}
				}
			} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, files);
					}
				}
			} else if (params.getCode().equals(Contains.BAN_LANH_DAO)) {
				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, files);
					}
				}
			}

			doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
			checkSumService.save(doanhNghiep);

			model.addAttribute("success", "Upload file success");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Upload file fail");
		}

		return "demo/doanhnghiep2/step/uploadfile";
	}

	@GetMapping(value = "/ekyc-enterprise/esign")
	public String esign(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams)
			throws IOException {
		ParamsKbank params = new ParamsKbank();
		String token = allParams.get("token");
		String tokenCheck = allParams.get("tokenCheck");
		if (token == null)
			return "demo/doanhnghiep2/step/steperror";
		if (tokenCheck == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(token);
		if (doanhNghiep == null)
			return "demo/doanhnghiep2/step/steperror";

		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);
		model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);

		InfoPerson nguoiUyQuyen = layNguoiDaiDienPhapLuatThaoTac(ekycDoanhNghiep, allParams);

		if (nguoiUyQuyen == null)
			return "demo/doanhnghiep2/step/steperror";

		model.addAttribute("lanhDaos", ekycDoanhNghiep.getListOfLeaders());
		model.addAttribute("nguoiUyQuyens", ekycDoanhNghiep.getPersonAuthorizedAccountHolder());
		model.addAttribute("nguoiUyQuyen", nguoiUyQuyen);
		model.addAttribute("logo",
				"data:image/jpeg;base64," + CommonUtils.encodeFileToBase64Binary(new File("/image/logoSN.png")));

		String pathFileOpenAcc = configProperties.getConfig().getImage_folder_log() + UUID.randomUUID().toString()
				+ ".pdf";
		System.out.println(pathFileOpenAcc);
		pdfHandling.nhapThongTinForm(pathFileOpenAcc, ekycDoanhNghiep, PATH_PDF_FILL_FORM);

		model.addAttribute("openFormBase64", CommonUtils.encodeFileToBase64Binary(new File(pathFileOpenAcc)));

		params.setToken(token);

		setParams(params, req);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		model.addAttribute("date", simpleDateFormat.format(new Date()));

		return "ekyc/ekycdn23";
	}

	private InfoPerson layNguoiDaiDienPhapLuatThaoTac(EkycDoanhNghiep ekycDoanhNghiep, Map<String, String> allParams) {
		for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
			System.out.println("hjsadjhasdjh:   " + allParams.get("tokenCheck"));
			if (ip.getTokenCheck().equals(allParams.get("tokenCheck")) && ip.getCheckMain().equals("yes"))
				return ip;
		}
		return null;
	}

	@GetMapping(value = "/ekyc-enterprise/ekyc")
	public String ekyc(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		ParamsKbank params = new ParamsKbank();
		String token = allParams.get("token");
		System.err.println("token: " + allParams.get("token"));
		String tokenCheck = allParams.get("tokenCheck");
		System.err.println("token: " + allParams.get("tokenCheck"));
		if (token == null) {
			return "demo/doanhnghiep2/step/steperror";
		}

		if (tokenCheck == null) {
			return "demo/doanhnghiep2/step/steperror";
		}
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(token);
		if (doanhNghiep == null) {
			System.out.println("/ekyc-enterprise/ekyc3");
			return "demo/doanhnghiep2/step/steperror";
		}

		String loai = new String(Base64.getDecoder().decode(allParams.get("type")));
		params.setCode(loai);
		System.out.println("loai: " + loai);
		model.addAttribute("loai", loai);

		params.setToken(token);
		params.setTokenCheck(tokenCheck);
		params.setQueryParams(req.getQueryString());

		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

		InfoPerson infoPerson = layNguoiDaiDienPhapLuatThaoTac(ekycDoanhNghiep, allParams);

		if (daHetThoiGianLamEkyc(doanhNghiep, params, ekycDoanhNghiep, infoPerson)) {
			return "demo/doanhnghiep2/step/steperror";
		}

		setParams(params, req);

		return "demo/doanhnghiep2/step/step3";
	}

	private boolean daHetThoiGianLamEkyc(EkycDoanhNghiepTable doanhNghiep, ParamsKbank params,
			EkycDoanhNghiep ekycDoanhNghiep, InfoPerson infoPerson) {
		if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					if (hetThoiGianLamEkyc(ip, infoPerson))
						return true;
				}
			}

		} else if (params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
			if (ekycDoanhNghiep.getHaveAChiefAccountant().equals("yes")) {
				for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						if (hetThoiGianLamEkyc(ip, infoPerson))
							return true;
					}
				}
			}
		} else if (params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					if (hetThoiGianLamEkyc(ip, infoPerson))
						return true;
				}
			}
		} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)) {
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					if (hetThoiGianLamEkyc(ip, infoPerson))
						return true;
				}
			}
		} else if (params.getCode().equals(Contains.BAN_LANH_DAO)) {
			for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					if (hetThoiGianLamEkyc(ip, infoPerson))
						return true;
				}
			}
		}

		return false;
	}

	private boolean hetThoiGianLamEkyc(InfoPerson ip, InfoPerson infoPerson) {

		long thoiGianGuiMail = Long.valueOf(ip.getTime());
		long timeOut = configProperties.getConfig().getTimeout_nhan_link_ky() != null
				? Long.valueOf(configProperties.getConfig().getTimeout_nhan_link_ky())
				: 24;

		if (infoPerson != null) {
			timeOut = configProperties.getConfig().getTimeout_link_ky_form_cuoi() != null
					? Long.valueOf(configProperties.getConfig().getTimeout_link_ky_form_cuoi())
					: 24;
		}

		long thoiGianHetHan = timeOut * 60 * 60 * 1000L;
		long thoiGianhetHanEkyc = thoiGianGuiMail + thoiGianHetHan;
		if (thoiGianhetHanEkyc < System.currentTimeMillis())
			return true;

		return false;
		// return false;
	}

	@PostMapping(value = "/ekyc-enterprise/ekyc")
	public String step2(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			@RequestParam(name = "anhMatTruoc", required = false) MultipartFile anhMatTruoc,
			@RequestParam(name = "anhMatSau", required = false) MultipartFile anhMatSau,
			@RequestParam(name = "anhChuKy", required = false) MultipartFile anhChuKy)
			throws ValidException, CheckException, Exception {
		forwartParams(allParams, model);

		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:" + "/ekyc-enterprise/ekyc";

		String anhMatTruocBase64 = new String(Base64.getEncoder().encode(anhMatTruoc.getBytes()));
		String anhMatSauBase64 = new String(Base64.getEncoder().encode(anhMatSau.getBytes()));

		if (anhChuKy != null) {
			String anhChuKyBase64 = new String(Base64.getEncoder().encode(anhChuKy.getBytes()));

			params.setAnhChuKy(anhChuKyBase64);
		}

		long time1 = System.currentTimeMillis();
		ParamPathImage paramPathImage = taoPathParamsPathImg(anhMatTruocBase64, anhMatSauBase64, null, null);

		System.out.println("loai giai tơ : " + allParams.get("maGiayTo"));
		String codeTransaction = CommonUtils.layMaGiaoDich(1);
		params.setCodeTransaction(codeTransaction);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("anhMatTruoc", anhMatTruocBase64);
		if (allParams.get("maGiayTo").equals("cccd")) {
			jsonObject.put("anhMatSau", anhMatSauBase64);
		}

		jsonObject.put("maGiayTo", allParams.get("maGiayTo"));

		String respone = "";
		// if(!MOI_TRUONG.equals("dev")) {

		respone = postRequest(jsonObject.toString(), "/api/public/all/doc-noi-dung-ocr", params); // dev
		// }else {
		// System.err.println("vao 2");
		// respone = postRequest(jsonObject.toString(), "/public/all/doc-noi-dung-ocr",
		// params);//pro
		// }

		JSONObject object = new JSONObject(respone);
		System.err.println("object: " + object.toString());

		String logId = luuLogApi(time1, object.getInt("status"), "/api/public/all/doc-noi-dung-ocr", "POST",
				codeTransaction, respone);
		System.err.println("vao 1");
		luuChiTietLichSuApi(logId, respone, new Gson().toJson(paramPathImage));

		if (object.getInt("status") != 200) {
			model.addAttribute("error", object.getString("message"));

			return "demo/doanhnghiep2/step/step3";
		}

		JSONObject objectRespGiayTo = new JSONObject(respone);
		Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);

		params.setAnhMatTruoc(anhMatTruocBase64);
		params.setAnhMatSau(anhMatSauBase64);
		params.setRespGiayTo(respone);

		setParams(params, req);

		model.addAttribute("ocr", ocr);

		if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)
				|| params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
			return "demo/doanhnghiep2/step/step4";
		} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)
				|| params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)
				|| params.getCode().equals(Contains.BAN_LANH_DAO) || params.getCode().equals(Contains.Straight2bank)) {
			return "demo/doanhnghiep2/step/step51";
		} else {
			return "demo/doanhnghiep2/step/step5";
		}
	}

	private ParamPathImage taoPathParamsPathImg(String anhMatTruocBase64, String anhMatSauBase64,
			String anhCaNhanBase64, ArrayList<String> pathAnhVideos) {
		FileHandling fileHandling = new FileHandling();
		ParamPathImage paramPathImage = new ParamPathImage();
		if (!StringUtils.isEmpty(anhMatTruocBase64)) {
			String pathAnhMatTruoc = fileHandling.save(anhMatTruocBase64,
					configProperties.getConfig().getImage_folder_log());
			paramPathImage.setAnhMatTruoc(pathAnhMatTruoc);
		}
		if (!StringUtils.isEmpty(anhMatSauBase64)) {
			String pathAnhMatSau = fileHandling.save(anhMatSauBase64,
					configProperties.getConfig().getImage_folder_log());
			paramPathImage.setAnhMatSau(pathAnhMatSau);
		}
		if (!StringUtils.isEmpty(anhCaNhanBase64)) {
			String pathAnhCaNhan = fileHandling.save(anhCaNhanBase64,
					configProperties.getConfig().getImage_folder_log());
			paramPathImage.setAnhKhachHang(pathAnhCaNhan);
		}
		if (pathAnhVideos != null && pathAnhVideos.size() > 0) {
			paramPathImage.setAnhVideo(pathAnhVideos);
		}

		return paramPathImage;
	}

	@PostMapping(value = "/ekyc-enterprise/ekyc/step2")
	public String nhapThongTin(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {

		forwartParams(allParams, model);
		String listImage = allParams.get("listImage");
		String[] arr = listImage.split(",");
		JSONArray jsonArray = new JSONArray();
		String anhCaNhan = "";

		FileHandling fileHandling = new FileHandling();
		long time1 = System.currentTimeMillis();

		String codeTransaction = CommonUtils.layMaGiaoDich(1);

		ArrayList<String> pathAnhVideos = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			jsonArray.put(i, new JSONObject().put("anh", arr[i]).put("thoiGian", (i + 1)));
			if (StringUtils.isEmpty(anhCaNhan)) {
				anhCaNhan = arr[i];
			}

			String pathFileLog = fileHandling.save(arr[i], configProperties.getConfig().getImage_folder_log());
			pathAnhVideos.add(pathFileLog);
		}

		ParamPathImage paramPathImage = taoPathParamsPathImg(null, null, null, pathAnhVideos);

		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:" + "/ekyc-enterprise/ekyc";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("anhMatTruoc", params.getAnhMatTruoc());
		jsonObject.put("anhVideo", jsonArray);

		params.setCodeTransaction(codeTransaction);
		String respone = "";

		// if(MOI_TRUONG.equals("dev")) {
//			  respone = "{\r\n"
//						+ "	\"data\": 0.9541407179450689,\r\n"
//						+ "	\"message\": \"Thành công\",\r\n"
//						+ "	\"status\": 200\r\n"
//						+ "}";

		// }
//		  else {
		respone = postRequest(jsonObject.toString(), "/api/public/all/xac-thuc-khuon-mat", params);
//		    }

		// }

		JSONObject object = new JSONObject(respone);

		String logId = luuLogApi(time1, object.getInt("status"), "/api/public/all/xac-thuc-khuon-mat", "POST",
				codeTransaction, respone);
		System.err.println("logid: " + logId);
		luuChiTietLichSuApi(logId, respone, new Gson().toJson(paramPathImage));

		if (!MOI_TRUONG.equals("dev")) {
			if (object.getInt("status") != 200) {
				model.addAttribute("error", object.getString("message"));
				return "demo/doanhnghiep2/step/step4";
			}
		}

		JSONObject objectRespGiayTo = new JSONObject(params.getRespGiayTo());
		Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);

		model.addAttribute("params", params);
		model.addAttribute("ocr", ocr);

		params.setAnhCaNhan(anhCaNhan);
		setParams(params, req);
//		if (params.getCode().equals(Contains.BAN_LANH_DAO)
//				|| params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT) || params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
//			return "demo/doanhnghiep2/step/step51";
//		} else {
//			return "demo/doanhnghiep2/step/step5";
//		}

		return "demo/doanhnghiep2/step/step5";
	}

	@PostMapping(value = "/ekyc-enterprise/ekyc/step3")
	public String step3(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		forwartParams(allParams, model);

		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:" + "/ekyc-enterprise/ekyc";
		System.out.println("Token: " + params.getToken());
		System.err.println("param.getCode(): " + params.getCode());
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
		EkycDoanhNghiep ekycDoanhNghiep = new EkycDoanhNghiep();
		if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {

			ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getSsNoiDung(), EkycDoanhNghiep.class);
		} else if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {

			ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);
		}

		FileHandling fileHandling = new FileHandling();

		if (ekycDoanhNghiep.getAllInOne().equals("yes")) {
			if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						for (int i = 0; i < ekycDoanhNghiep.getChiefAccountant().size(); i++) {
							if (ip.getTokenCheck().equals(params.getTokenCheck())) {
								ekycDoanhNghiep.getChiefAccountant().get(i).setHoVaTen(allParams.get("hoVaTen"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setSoCmt(allParams.get("soCmt"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setNamSinh(allParams.get("namSinh"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setNoiCap(allParams.get("noiCap"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setHoKhau(allParams.get("hoKhau"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setNgayCap(allParams.get("ngayCap"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setNgayHetHan(allParams.get("ngayHetHan"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setQuocTich(allParams.get("quocTich"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setVisa(allParams.get("visa"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setMaSoThue(allParams.get("maSoThue"));
								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setTinhTrangCuTru(allParams.get("tinhTrangCuTru"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setDiaChiNha(allParams.get("diaChiNha"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setMobile(allParams.get("mobile"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setVanPhong(allParams.get("vanPhong"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setDiaChi(allParams.get("diaChi"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setEmail2(allParams.get("email2"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setHoKhau(allParams.get("noiTru"));
								ekycDoanhNghiep.getChiefAccountant().get(i).setEditStatus("no");

								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setAnhMatSau(luuAnh(params.getAnhMatSau(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setAnhChuKy(luuAnh(params.getAnhChuKy(), fileHandling, "abc.png"));
								ekycDoanhNghiep.getChiefAccountant().get(i)
										.setKiemTra(Contains.TRANG_THAI_THAO_TAC_THANH_CONG);

							}
						}
						for (int i = 0; i < ekycDoanhNghiep.getListOfLeaders().size(); i++) {
							if (ip.getTokenCheck().equals(params.getTokenCheck())) {
								ekycDoanhNghiep.getListOfLeaders().get(i).setHoVaTen(allParams.get("hoVaTen"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setSoCmt(allParams.get("soCmt"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setNamSinh(allParams.get("namSinh"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setNoiCap(allParams.get("noiCap"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setHoKhau(allParams.get("hoKhau"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setNgayCap(allParams.get("ngayCap"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setNgayHetHan(allParams.get("ngayHetHan"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setQuocTich(allParams.get("quocTich"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setVisa(allParams.get("visa"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setMaSoThue(allParams.get("maSoThue"));
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setTinhTrangCuTru(allParams.get("tinhTrangCuTru"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setDiaChiNha(allParams.get("diaChiNha"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setMobile(allParams.get("mobile"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setVanPhong(allParams.get("vanPhong"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setDiaChi(allParams.get("diaChi"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setEmail2(allParams.get("email2"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setHoKhau(allParams.get("noiTru"));
								ekycDoanhNghiep.getListOfLeaders().get(i).setEditStatus("no");
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setAnhMatSau(luuAnh(params.getAnhMatSau(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setAnhChuKy(luuAnh(params.getAnhChuKy(), fileHandling, "abc.png"));
								ekycDoanhNghiep.getListOfLeaders().get(i)
										.setKiemTra(Contains.TRANG_THAI_THAO_TAC_THANH_CONG);

							}
						}
					}
					guiMailHoanThanhEkyc(ip);
				}

			}

			if (params.getCode().equals(Contains.Straight2bank)) {
				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						System.err.println("hkjagshjdghjsadghj");
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}
				}
			}

		} else {
			if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}

				}

			} else if (params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
				// if (ekycDoanhNghiep.getHaveAChiefAccountant().equals("yes")) {
				for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}
					// }
				}
			} else if (params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}
				}
			} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}
				}
			} else if (params.getCode().equals(Contains.BAN_LANH_DAO)) {

				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						System.err.println("person1: " + ip.toString());
						updateInfo(ip, allParams, params, null);
						System.err.println("person2: " + ip.toString());
						guiMailHoanThanhEkyc(ip);
					}
				}
			} else if (params.getCode().equals(Contains.Straight2bank)) {
				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						updateInfo(ip, allParams, params, null);
						guiMailHoanThanhEkyc(ip);
					}

				}
			}
		}
		if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
			doanhNghiep.setSsNoiDung(new Gson().toJson(ekycDoanhNghiep));
			doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
		} else if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {
			doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
		}
		req.getSession().setAttribute("tokenStatus", doanhNghiep.getToken());

		checkSumService.save(doanhNghiep);

		System.out.println("co save nhe bay bee");

		if (kiemTraDaUpdateDuThongTin(ekycDoanhNghiep)) {

			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {

				if (ip.getCheckMain().equals("yes")) {
					if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {
						System.out.println("co save nhe bay bee11111");
						guiMailLegalRef(ekycDoanhNghiep, ip, Contains.NGUOI_DAI_DIEN_PHAP_LUAT);
					} else if (checkGuiMai(ekycDoanhNghiep)) {
						if (ip.getEditStatus().equals("no") && ip.getCheckMain().equals("yes")) {
							LOGGER.info("Send mail end: " + LINK_ADMIN + "/ekyc-enterprise/esign?token="
									+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck());
							if (!MOI_TRUONG.equals("dev"))
								guiMailYeuKyAOF(ekycDoanhNghiep, ip);
//									email.sendText(ip.getEmail(), "Email yêu cầu ký số đăng ký mở tài khoản",
//											"Vui lòng click vào <a href='" + LINK_ADMIN + "/ekyc-enterprise/esign?token="
//													+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck()
//													+ "'>Bắt đầu ký số</a>, để thực hiện ký số");

						} else if (ip.getEditStatus().equals("yes") && ip.getCheckMain().equals("yes")) {
							System.out.println("hjsdfshdj:   " + checkGuiMailChuTaiKhoan(ekycDoanhNghiep));
							if (checkGuiMailChuTaiKhoan(ekycDoanhNghiep)) {
								System.out.println("co vao dsaay khong nhỉ!!!!");
								guiMailLegalRef(ekycDoanhNghiep, ip, Contains.NGUOI_DAI_DIEN_PHAP_LUAT);
							}

						}
					}

				}
			}
		}

		setParams(params, req);

		allParams.put("tokenCheck", params.getTokenCheck());
		InfoPerson nguoiUyQuyen = layNguoiDaiDienPhapLuatThaoTac(ekycDoanhNghiep, allParams);

		if (nguoiUyQuyen != null) {
			System.out.println("hjdsfbshdj11111:" + params.getToken());
			model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);

			model.addAttribute("lanhDaos", ekycDoanhNghiep.getListOfLeaders());
			model.addAttribute("nguoiUyQuyens", ekycDoanhNghiep.getPersonAuthorizedAccountHolder());
			model.addAttribute("nguoiUyQuyen", nguoiUyQuyen);
			model.addAttribute("logo",
					"data:image/jpeg;base64," + CommonUtils.encodeFileToBase64Binary(new File("/image/logoSN.png")));
			model.addAttribute("tokens", params.getToken());
			try {

				String pathFileOpenAcc = configProperties.getConfig().getImage_folder_log()
						+ UUID.randomUUID().toString() + ".pdf";

				// String pathFileOpenAcc =
				// fileHandling.getFolder(configProperties.getConfig().getImage_folder_log()+code+"/")+UUID.randomUUID().toString()+".pdf";
				System.out.println(pathFileOpenAcc);
				pdfHandling.nhapThongTinForm(pathFileOpenAcc, ekycDoanhNghiep, PATH_PDF_FILL_FORM);

				model.addAttribute("openFormBase64", CommonUtils.encodeFileToBase64Binary(new File(pathFileOpenAcc)));
			} catch (Exception e) {
				e.printStackTrace();
			}

			params.setToken(token);

			setParams(params, req);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			model.addAttribute("date", simpleDateFormat.format(new Date()));

			return "ekyc/ekycdn23";
			// return "ekyc/ekycdn22";
		}

		return "demo/doanhnghiep2/step/success";
	}

	public Boolean checkGuiMai(EkycDoanhNghiep ekycDoanhNghiep) {

		// Boolean check = false;
//		for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
//			if (ip.getEditStatus().equals("yes"))
//				return false;
//		}
		for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
			if (ip.getEditStatus().equals("yes"))
				return false;
		}
		for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
			if (ip.getEditStatus().equals("yes"))
				return false;
		}
		for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
			if (ip.getEditStatus().equals("yes"))
				return false;
		}
		for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
			if (ip.getEditStatus().equals("yes"))
				return false;
		}
		for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
			if (ip.getEditStatus().equals("yes"))
				return false;
		}

		return true;
	}

	public Boolean checkGuiMailChuTaiKhoan(EkycDoanhNghiep ekycDoanhNghiep) {
		try {
			boolean check = true;
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {

				if (ip.getCheckMain().equals("no")) {
					if (ip.getEditStatus().equals("yes"))
						check = false;
				}
			}
			for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
				if (ip.getEditStatus().equals("yes"))
					return false;
			}
			if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
					if (ip.getEditStatus().equals("yes"))
						return false;
				}
			}

			if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
					if (ip.getEditStatus().equals("yes"))
						return false;
				}
			}

			if (ekycDoanhNghiep.getListOfLeaders() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (ip.getEditStatus().equals("yes"))
						return false;

				}
			}
			if (ekycDoanhNghiep.getUserDesignation() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getEditStatus().equals("yes"))
						return false;

				}
			}
			return check;
		} catch (Exception e) {
		}
		return false;
	}

	public Boolean checkChuTaiKhoan(InfoPerson infoPerson) {
		boolean check = true;

		if (infoPerson.getCheckMain().equals("yes") && infoPerson.getEditStatus().equals("yes")) {
			check = true;
		} else if (infoPerson.getEditStatus() == null) {
			check = true;
		} else if (infoPerson.getCheckMain().equals("yes") && infoPerson.getEditStatus().equals("no")) {
			check = false;
		}

		return check;
	}

	@GetMapping(value = "/ekyc-enterprise/ekyc/kyso")
	public String kyAOF(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model) {
		forwartParams(allParams, model);

		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:" + "/ekyc-enterprise/ekyc";
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);
		model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);

		model.addAttribute("lanhDaos", ekycDoanhNghiep.getListOfLeaders());
		model.addAttribute("nguoiUyQuyens", ekycDoanhNghiep.getPersonAuthorizedAccountHolder());
		model.addAttribute("nguoiUyQuyen", ekycDoanhNghiep.getLegalRepresentator());
		model.addAttribute("logo",
				"data:image/jpeg;base64," + CommonUtils.encodeFileToBase64Binary(new File("/image/logoSN.png")));
		model.addAttribute("tokens", params.getToken());
		try {

			String pathFileOpenAcc = configProperties.getConfig().getImage_folder_log() + UUID.randomUUID().toString()
					+ ".pdf";

			// String pathFileOpenAcc =
			// fileHandling.getFolder(configProperties.getConfig().getImage_folder_log()+code+"/")+UUID.randomUUID().toString()+".pdf";
			System.out.println(pathFileOpenAcc);
			pdfHandling.nhapThongTinForm(pathFileOpenAcc, ekycDoanhNghiep, PATH_PDF_FILL_FORM);

			model.addAttribute("openFormBase64", CommonUtils.encodeFileToBase64Binary(new File(pathFileOpenAcc)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		params.setToken(token);

		setParams(params, req);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		model.addAttribute("date", simpleDateFormat.format(new Date()));

		return "ekyc/ekycdn23";

	}

	private InfoPerson layNguoiDaiDien(ArrayList<InfoPerson> legalRepresentator) {
		for (InfoPerson infoPerson : legalRepresentator) {
			if (infoPerson.getCheckMain().equals("yes"))
				return infoPerson;
		}
		return null;
	}

	@PostMapping(value = "/ekyc-enterprise/ekyc/register-sign", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String registerSign(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		JSONObject jsonResp = new JSONObject();
		try {
			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:" + "/ekyc-enterprise/ekyc";
			EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
			EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

			InfoPerson infoPerson = layThongTinNguoiKy(ekycDoanhNghiep, params);

			if (infoPerson == null)
				throw new Exception();

			String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());

			JSONObject jsonObjectPr = new JSONObject(text);

			forwartParams(allParams, model);
			String nameFile = UUID.randomUUID().toString() + ".pdf";
			String pathPdf = KY_SO_FOLDER + "/" + nameFile;
			String agreementUUID = UUID.randomUUID().toString();

			ParamsKbank paramsKySo = new ParamsKbank();
			FormInfo formInfo = new FormInfo();
			formInfo.setHoVaTen(infoPerson.getHoVaTen());
			formInfo.setSoCmt(infoPerson.getSoCmt());
			formInfo.setDiaChi("Hà Nội");
			formInfo.setThanhPho("Hà Nội");
			formInfo.setQuocGia("Việt Nam");
			paramsKySo.setSoDienThoai(infoPerson.getPhone());
			paramsKySo.setFormInfo(formInfo);
			paramsKySo.setAnhMatTruoc(Utils.encodeFileToBase64Binary(new File(infoPerson.getAnhMatTruoc())));
			paramsKySo.setAnhMatSau(Utils.encodeFileToBase64Binary(new File(infoPerson.getAnhMatSau())));

			System.out.println(KY_SO_FOLDER);
			byte[] decodedImg = Base64.getDecoder()
					.decode(jsonObjectPr.getString("file").getBytes(StandardCharsets.UTF_8));

			Path destinationFile = Paths.get(KY_SO_FOLDER, nameFile);
			Files.write(destinationFile, decodedImg);

			System.out.println("PDF Created!");

			String jsonRegister = guiThongTinDangKyKySo(paramsKySo, agreementUUID);
			ObjectMapper objectMapper = new ObjectMapper();
			SignCloudResp signCloudRespRegister = objectMapper.readValue(jsonRegister, SignCloudResp.class);

			if (signCloudRespRegister.getResponseCode() != 0) {
				jsonResp.put("message", "Không đăng ký được chữ ký ");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			}
			String page = "1";
			String textPage = "Name of the Account";

			String jsonResponse = guiThongTinKySo(req, pathPdf, nameFile, agreementUUID, page, textPage);

			SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

			if (signCloudResp.getResponseCode() != 1007) {
				jsonResp.put("message", "Không gửi được chữ ký ");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			}

			if (!MOI_TRUONG.equals("dev")) {
				LOGGER.info("dienThoai SMS: {}", infoPerson.getPhone());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("dienThoai", infoPerson.getPhone());
				postRequest(jsonObject.toString(),
						"/public/gui-ma-otp-ky-so?code=" + signCloudResp.getAuthorizeCredential());
			}

			jsonResp.put("otp", signCloudResp.getAuthorizeCredential());
			jsonResp.put("maKy", signCloudResp.getBillCode());

//			postRequest(jsonObject.toString(), "/public/gui-ma-otp-ky-so?code=1234");
//			
//			jsonResp.put("otp", "1234");
//			jsonResp.put("maKy", "123456789");
			jsonResp.put("pathPdf", pathPdf);
			jsonResp.put("nameFile", nameFile);
			jsonResp.put("agreementUUID", agreementUUID);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResp.put("message", "Lỗi hệ thống");
			jsonResp.put("status", 400);

			return jsonResp.toString();
		}

		jsonResp.put("status", 200);

		return jsonResp.toString();
	}

	private InfoPerson layThongTinNguoiKy(EkycDoanhNghiep ekycDoanhNghiep, ParamsKbank params) {
		if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					return ip;
				}
			}
		} else if (params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
			for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					return ip;
				}
			}
		} else if (params.getCode().equals(Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					return ip;
				}
			}
		} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)) {
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					return ip;
				}
			}
		} else if (params.getCode().equals(Contains.BAN_LANH_DAO)) {
			for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					return ip;
				}
			}
		}
		return null;
	}

	@PostMapping(value = "/ekyc-enterprise/ekyc/step4", produces = MediaType.APPLICATION_JSON_VALUE)
	public String kySoOtpStep4(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		try {
			eSignCall service = new eSignCall();
			String jsonResponse = service.authorizeSingletonSigningForSignCloud(allParams.get("agreementUUID"),
					allParams.get("otpKySo"), allParams.get("maKy"));
			ObjectMapper objectMapper = new ObjectMapper();
			SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);
			if (signCloudResp.getResponseCode() == 0 && signCloudResp.getSignedFileData() != null) {
				String str = KY_SO_FOLDER + "/" + UUID.randomUUID().toString() + ".pdf";
				File file2 = new File(str);
				IOUtils.write(signCloudResp.getSignedFileData(), new FileOutputStream(file2));
				model.addAttribute("file", CommonUtils.encodeFileToBase64Binary(file2));
				model.addAttribute("success", "Ký số thành công");

				capNhatThongTin(req, str);

				return "demo/doanhnghiep2/step/kySoThanhCong";
			} else if (signCloudResp.getResponseCode() == 1004) {
				model.addAttribute("error", "Lỗi OTP");
			} else {
				model.addAttribute("error", "Ký số thất bại");
			}
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi hệ thống");
		}
		model.addAttribute("file",
				Utils.encodeFileToBase64Binary(new File(KY_SO_FOLDER + "/" + "ACCOUNT-OPENING-FORM_Dummy.pdf")));

		return "demo/doanhnghiep2/step/viewFileKySo";
	}

	private boolean capNhatThongTin(HttpServletRequest req, String filePath) {
		ParamsKbank params = getParams(req);
		if (params == null)
			return false;
		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);
		if (params.getCode().equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					ip.setFile(filePath);
				}
			}
		} else if (params.getCode().equals(Contains.KE_TOAN_TRUONG)) {
			if (ekycDoanhNghiep.getHaveAChiefAccountant().equals("yes")) {
				for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						ip.setFile(filePath);
					}
				}
			} else {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
					if (ip.getTokenCheck().equals(params.getTokenCheck())) {
						ip.setFile(filePath);
					}
				}
			}
		} else if (params.getCode().equals(Contains.NGUOI_DUOC_UY_QUYEN)) {
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					ip.setFile(filePath);
				}
			}
		} else if (params.getCode().equals(Contains.BAN_LANH_DAO)) {
			for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
				if (ip.getTokenCheck().equals(params.getTokenCheck())) {
					ip.setFile(filePath);
				}
			}
		}
		doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
		checkSumService.save(doanhNghiep);
		return true;

	}

	private boolean sendMailEkyc(ArrayList<InfoPerson> listInfoPersons, InfoPerson ipSave) {
		if (listInfoPersons.size() == 1)
			return false;
		boolean checkFullUpdate = true;
		for (InfoPerson infoPerson : listInfoPersons) {
			if (infoPerson.getKiemTra() == null && infoPerson.getCheckMain().equals("no"))
				checkFullUpdate = false;
		}

		if (ipSave.getCheckMain().equals("yes"))
			return false;

		if (checkFullUpdate)
			return true;

		return false;
	}

	private void updateInfo(InfoPerson ip, Map<String, String> allParams, ParamsKbank params, MultipartFile[] files)
			throws IOException {
		FileHandling fileHandling = new FileHandling();
		ip.setHoVaTen(allParams.get("hoVaTen"));
		ip.setSoCmt(allParams.get("soCmt"));
		ip.setNamSinh(allParams.get("namSinh"));
		ip.setNoiCap(allParams.get("noiCap"));
		ip.setHoKhau(allParams.get("hoKhau"));
		ip.setNgayCap(allParams.get("ngayCap"));
		ip.setNgayHetHan(allParams.get("ngayHetHan"));
		ip.setQuocTich(allParams.get("quocTich"));
		ip.setVisa(allParams.get("visa"));
		ip.setMaSoThue(allParams.get("maSoThue"));
		ip.setTinhTrangCuTru(allParams.get("tinhTrangCuTru"));
		ip.setDiaChiNha(allParams.get("diaChiNha"));
		ip.setMobile(allParams.get("mobile"));
		ip.setVanPhong(allParams.get("vanPhong"));
		ip.setDiaChi(allParams.get("diaChi"));
		ip.setEmail2(allParams.get("email2"));
		ip.setHoKhau(allParams.get("noiTru"));
		ip.setEditStatus("no");
		ip.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
		ip.setAnhMatSau(luuAnh(params.getAnhMatSau(), fileHandling, "abc.jpg"));
		ip.setAnhMatTruoc(luuAnh(params.getAnhMatTruoc(), fileHandling, "abc.jpg"));
		ip.setAnhChuKy(luuAnh(params.getAnhChuKy(), fileHandling, "abc.png"));
		ip.setKiemTra(Contains.TRANG_THAI_THAO_TAC_THANH_CONG);

		if (files != null) {
			ArrayList<String> listFiles = new ArrayList<>();
			for (MultipartFile multipartFile : files) {
				String base64 = Base64.getEncoder().encodeToString(multipartFile.getBytes());
				listFiles.add(luuAnh(base64, fileHandling, multipartFile.getOriginalFilename()));
			}
			ip.setFiles(listFiles);
		}

	}

	private boolean kiemTraDaUpdateDuThongTin(EkycDoanhNghiep ekycDoanhNghiep) {
		try {
			boolean check = true;
			if (ekycDoanhNghiep.getAllInOne().equals("yes")) {
//
//				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
//
//					if (ip.getCheckMain().equals("no")) {
//						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
//							check = false;
//					} else {
//						check = true;
//					}
//				}

				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getChapThuanLenh().equals("Y") || ip.getChapThuanLenhDongThoi().equals("Y")) {
						System.err.println("Infor: " + ip.toString());
						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG)) {
							System.err.println("hdsj1: " + check);
							return false;

							// break;
						}
					}
				}

				return check;
			} else if (ekycDoanhNghiep.getAllInOne().equals("no")) {
				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {

					if (ip.getCheckMain().equals("no")) {
						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
							check = false;
					} else {
						check = true;
					}
				}
				for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
					if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
						return false;
				}
				if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant() != null) {
					for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
							return false;
					}
				}

				if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder() != null) {
					for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
							return false;
					}
				}

				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
						return false;

				}

				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getChapThuanLenh().equals("Y") || ip.getChapThuanLenhDongThoi().equals("Y")) {
						if (!ip.getKiemTra().equals(Contains.TRANG_THAI_THAO_TAC_THANH_CONG))
							return false;
					}
				}

			}

			System.err.println("check: " + check);
			return check;
		} catch (Exception e) {
		}
		return false;
	}

	@PostMapping(value = "/ekyc-enterprise/thong-tin-doanh-nghiep", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ekycThongTinDoanhNghiep(HttpServletRequest req,
			@RequestParam("fileDangKyKinhDoanh") MultipartFile fileDangKyKinhDoanh,
			@RequestParam Map<String, String> allParams) throws IOException {
		if (khongTrongThoiGianXyLy(req)) {
			return "{\"status\":505}";
		}

		long time1 = System.currentTimeMillis();
		FileHandling fileHandling = new FileHandling();
		ParamPathImage paramPathImage = new ParamPathImage();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileDangKyKinhDoanh.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileDangKyKinhDoanh.getOriginalFilename());
		LOGGER.info("file đăng ký dinh doanh: " + pathFile.toString());
		paramPathImage.setFile(pathFile);
		String codeTransaction = CommonUtils.layMaGiaoDich(1);

		LOGGER.info("file đăng ký dinh doanh111: " + fileDangKyKinhDoanh.getOriginalFilename());
		String resp = "";
		// if(!MOI_TRUONG.equals("dev")) {
		resp = sendRequest(fileDangKyKinhDoanh, allParams, "/api/public/all/pr/thong-tin-doanh-nghiep",
				codeTransaction);
//		}else {
//			 resp = sendRequest(fileDangKyKinhDoanh, allParams, "/public/all/pr/thong-tin-doanh-nghiep",
//			codeTransaction);
//		}

		LOGGER.info("thong-tin-doanh-nghiep: " + resp);

		JSONObject jsonObject = new JSONObject(resp);
//		String logId = luuLogApi(time1, jsonObject.getInt("status"), "/public/all/pr/thong-tin-doanh-nghiep", "POST",
//				codeTransaction, jsonObject.toString());
		String logId = luuLogApi(time1, jsonObject.getInt("status"), "/api/public/all/pr/thong-tin-doanh-nghiep",
				"POST", codeTransaction, jsonObject.toString());
		luuChiTietLichSuApi(logId, jsonObject.toString(), new Gson().toJson(paramPathImage));

		return resp;
	}

	@PostMapping(value = "/ekyc-enterprise/check-chu-ky-so-kt", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ekycCheckChuKySoKeToan(HttpServletRequest req,
			@RequestParam("fileQuyetDinhBoNhiemKtt") MultipartFile fileQuyetDinhBoNhiemKtt,
			@RequestParam Map<String, String> allParams) throws IOException {
		if (khongTrongThoiGianXyLy(req)) {
			return "{\"status\":505}";
		}
		// System.out.println("file ktt: "+fileQuyetDinhBoNhiemKtt);

		FileHandling fileHandling = new FileHandling();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileQuyetDinhBoNhiemKtt.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileQuyetDinhBoNhiemKtt.getOriginalFilename());
		LOGGER.info("file ke toan truong: " + pathFile.toString());

		String str = pathFile.toString();

		String resp = caliApiChuKy.checkChuKySo(str);

		System.out.println("resp: " + resp.toString());
		return resp;
	}

	@PostMapping(value = "/ekyc-enterprise/check-chu-ky-so-charter", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ekycCheckChuKySoCompanyCharter(HttpServletRequest req,
			@RequestParam("fileCompanyCharter") MultipartFile fileCompanyCharter,
			@RequestParam Map<String, String> allParams) throws IOException {
		if (khongTrongThoiGianXyLy(req)) {
			return "{\"status\":505}";
		}
		// System.out.println("file CompanyCharter: "+fileCompanyCharter);
		FileHandling fileHandling = new FileHandling();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileCompanyCharter.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileCompanyCharter.getOriginalFilename());
		LOGGER.info("file CompanyCharter: " + pathFile.toString());

		String str = pathFile.toString();

		String resp = caliApiChuKy.checkChuKySo(str);

		System.out.println("resp: " + resp.toString());
		return resp;
	}

	@PostMapping(value = "/ekyc-enterprise/check-chu-ky-so-fatca", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ekycCheckChuKySoFatcaForms(HttpServletRequest req,
			@RequestParam("fileFatcaForms") MultipartFile fileFatcaForms, @RequestParam Map<String, String> allParams)
			throws IOException {
		if (khongTrongThoiGianXyLy(req)) {
			return "{\"status\":505}";
		}
		System.out.println("file FatcaForms: " + fileFatcaForms);

		FileHandling fileHandling = new FileHandling();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileFatcaForms.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileFatcaForms.getOriginalFilename());
		LOGGER.info("file FatcaForms: " + pathFile.toString());

		String str = pathFile.toString();

		String resp = caliApiChuKy.checkChuKySo(str);

		System.out.println("resp: " + resp.toString());
		return resp;
	}

	@PostMapping(value = "/ekyc-enterprise/check-chu-ky-so-share-list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ekycCheckChuKySoShareholderList(HttpServletRequest req,
			@RequestParam("fileInvestmentCertificate") MultipartFile fileFatcaForms,
			@RequestParam Map<String, String> allParams) throws IOException {
		if (khongTrongThoiGianXyLy(req)) {
			return "{\"status\":505}";
		}
		System.out.println("file FatcaForms: " + fileFatcaForms);

		FileHandling fileHandling = new FileHandling();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileFatcaForms.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileFatcaForms.getOriginalFilename());
		LOGGER.info("file FatcaForms: " + pathFile.toString());

		String str = pathFile.toString();

		String resp = caliApiChuKy.checkChuKySo(str);

		System.out.println("resp: " + resp.toString());
		return resp;
	}

	@PostMapping(value = "/ekyc-enterprise/thong-tin-ktt", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String thongTinKtt(HttpServletRequest req,
			@RequestParam("fileQuyetDinhBoNhiemKtt") MultipartFile fileQuyetDinhBoNhiemKtt,
			@RequestParam Map<String, String> allParams) throws IOException {

		long time1 = System.currentTimeMillis();
		FileHandling fileHandling = new FileHandling();
		ParamPathImage paramPathImage = new ParamPathImage();
		String pathFile = fileHandling.save(Base64.getEncoder().encodeToString(fileQuyetDinhBoNhiemKtt.getBytes()),
				configProperties.getConfig().getImage_folder_log(), fileQuyetDinhBoNhiemKtt.getOriginalFilename());
		paramPathImage.setFile(pathFile);
		String codeTransaction = CommonUtils.layMaGiaoDich(1);

		String resp = sendRequest(fileQuyetDinhBoNhiemKtt, allParams.get("loaiGiayToQuyetDinhBoNhiemKtt"),
				"/public/all/pr/ocr-template");

		// System.err.println("json: "+resp.toString());

		JSONObject jsonObject = new JSONObject(resp);
		String logId = luuLogApi(time1, jsonObject.getInt("status"), "/public/all/pr/ocr-template", "POST",
				codeTransaction, jsonObject.toString());
		luuChiTietLichSuApi(logId, jsonObject.toString(), new Gson().toJson(paramPathImage));

		return resp;
	}

	@GetMapping(value = "/ekyc-enterprise/language")
	public String nguoiDung(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		return "redirect:" + req.getContextPath() + req.getHeader("referer");
	}

	@GetMapping(value = "/ekyc-enterprise/index")
	public String ekycIndex(Model model) {

		model.addAttribute("urlApi", API_SERVICE);
		model.addAttribute("token", token);
		return "ekyc/index";
	}

	@GetMapping(value = "/ekyc-enterprise")
	public String ekyc(Model model, HttpServletRequest req) {
		Object username = req.getSession().getAttribute("b_username");
		if (StringUtils.isEmpty(username))
			return "redirect:/login-doanh-nghiep";

		ParamsKbank paramsKbank = new ParamsKbank();
		paramsKbank.setTimeStartStep(System.currentTimeMillis());
		setParams(paramsKbank, req);
		EkycDoanhNghiepTable ekycDoanhNghiepTable = ekycDoanhNghiepRepository.findByUsername(username.toString());
		if (ekycDoanhNghiepTable != null) {
			model.addAttribute("step", ekycDoanhNghiepTable.getStep() != null ? ekycDoanhNghiepTable.getStep() : 1);
			if (Integer.parseInt(ekycDoanhNghiepTable.getStep()) >= 3) {
				return "redirect:/ekyc-enterprise/update-ekyc-business";
			}
			System.out.println("step :" + ekycDoanhNghiepTable.getStep());
		}

		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(ekycDoanhNghiepTable.getNoiDung(), EkycDoanhNghiep.class);

		model.addAttribute("ekycDoanhNghiep", ekycDoanhNghiep);
		model.addAttribute("ekycDoanhNghiepTable", ekycDoanhNghiepTable);
		model.addAttribute("step", ekycDoanhNghiepTable.getStep());

		/*
		 * if(ekycDoanhNghiep.getUserDesignation() != null &&
		 * ekycDoanhNghiep.getUserDesignation().size() > 0) {
		 * model.addAttribute("userDesignation", ekycDoanhNghiep.getUserDesignation());
		 * } else {
		 */

		String haveAChiefAccountant = "no";
		String allInOne = "no";
		String haveAcccountHolder = "no";
		String agreeToReceive = "no";
		String checkMain = "no";
		ArrayList<Account> accounts = new ArrayList<>();
		accounts.add(new Account());
		model.addAttribute("listAccount", accounts);
		ArrayList<InfoPerson> infoPersons = new ArrayList<>();
		infoPersons.add(new InfoPerson());
		model.addAttribute("userDesignation", infoPersons);
		model.addAttribute("legalRepresentator", infoPersons);
		model.addAttribute("chiefAccountant", infoPersons);
		model.addAttribute("personAuthorizedAccountHolder", infoPersons);
		model.addAttribute("personAuthorizedChiefAccountant", infoPersons);
		model.addAttribute("haveAChiefAccountant", haveAChiefAccountant);
		model.addAttribute("allInOne", allInOne);
		model.addAttribute("haveAcccountHolder", haveAcccountHolder);
		model.addAttribute("agreeToReceive", agreeToReceive);
		model.addAttribute("checkMain", checkMain);
		// }

		req.getSession().setAttribute("userDesignation", infoPersons);
		req.getSession().setAttribute("legalRepresentator", infoPersons);
		req.getSession().setAttribute("chiefAccountant", infoPersons);
		req.getSession().setAttribute("listOfLeaders", infoPersons);
		req.getSession().setAttribute("personAuthorizedAccountHolder", infoPersons);
		req.getSession().setAttribute("personAuthorizedChiefAccountant", infoPersons);
		// req.getSession().setAttribute("haveAChiefAccountant", haveAChiefAccountant);
		model.addAttribute("urlApi", API_SERVICE);
		model.addAttribute("urlCompare", "/ekyc-enterprise");

		model.addAttribute("token", token);
		return "ekyc/ekycdn2";
	}

	@PostMapping(value = "/ekyc-enterprise/html-pdf", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String htmlPdf(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		JSONObject jsonResp = new JSONObject();
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		JSONObject jsonObjectPr = new JSONObject(text);

		String HTML = jsonObjectPr.getString("contentPdf");
		String nameFile = UUID.randomUUID().toString() + ".pdf";
		String pathPdf = KY_SO_FOLDER + "/" + nameFile;

		HtmlConverter.convertToPdf(HTML, new FileOutputStream(pathPdf));

		jsonResp.put("file", CommonUtils.encodeFileToBase64Binary(new File(pathPdf)));

		return jsonResp.toString();
	}

	@PostMapping(value = "/ekyc-enterprise/fill-form-pdf", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fillFormPdf(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		JSONObject jsonResp = new JSONObject();

		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:" + "/ekyc-enterprise/ekyc";

		EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params.getToken());
		if (doanhNghiep == null)
			return "demo/doanhnghiep2/step/steperror";
		EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

		String pathFileOpenAcc = configProperties.getConfig().getImage_folder_log() + UUID.randomUUID().toString()
				+ ".pdf";
		pdfHandling.nhapThongTinForm(pathFileOpenAcc, ekycDoanhNghiep, PATH_PDF_FILL_FORM);

		jsonResp.put("file", CommonUtils.encodeFileToBase64Binary(new File(pathFileOpenAcc)));

		return jsonResp.toString();
	}

	@ResponseBody
	public String docNoiDung(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject = new JSONObject(data);

		if (jsonObject.has("anhChanDung")) {
			FileHandling fileHandling = new FileHandling();
			long time1 = System.currentTimeMillis();
			String pathAnhChanDung = fileHandling.save(jsonObject.getString("anhChanDung"),
					configProperties.getConfig().getImage_folder_log());
			String pathAnhMatTruoc = fileHandling.save(jsonObject.getString("anhMatTruoc"),
					configProperties.getConfig().getImage_folder_log());
			ParamPathImage paramPathImage = new ParamPathImage();
			paramPathImage.setAnhKhachHang(pathAnhChanDung);
			paramPathImage.setAnhMatTruoc(pathAnhMatTruoc);

			JSONObject object = new JSONObject();
			object.put("anhMatTruoc", jsonObject.getString("anhMatTruoc"));
			object.put("anhKhachHang", jsonObject.getString("anhChanDung"));

			String codeTransaction = CommonUtils.layMaGiaoDich(1);

			String jsonCompare = postRequest(object.toString(), "/api/public/all/so-sanh-anh");
			JSONObject jsonObject3 = new JSONObject(jsonCompare);

			String logId = luuLogApi(time1, jsonObject3.getInt("status"), "/api/public/all/so-sanh-anh", "POST",
					codeTransaction, jsonCompare);
			luuChiTietLichSuApi(logId, jsonCompare, new Gson().toJson(paramPathImage));

			if (jsonObject3.getInt("status") != 200)
				return jsonObject3.toString();
		}
		String jsonOcr = postRequest(data, "/public/doc-noi-dung-ocr");
		JSONObject jsonObject2 = new JSONObject(jsonOcr);

		return jsonObject2.toString();
	}

	private String luuLogApi(long time1, int status, String uri, String method, String codeTransaction, String resp) {
		try {
			long time2 = System.currentTimeMillis();
			long timeHandling = time2 - time1;

			LogApi logApi = new LogApi();
			logApi.setLogId(UUID.randomUUID().toString());
			logApi.setTimeHandling(timeHandling);
			logApi.setDate(new Date());
			logApi.setStatus(status);
			logApi.setToken(token);
			logApi.setCode(code);
			logApi.setUri(uri);
			logApi.setMethod(method);
			logApi.setCodeTransaction(codeTransaction);
			logApi.setResponse(resp);
			logApiRepository.save(logApi);

			return logApi.getLogId();
		} catch (Exception e) {
		}

		return null;
	}

	private void luuChiTietLichSuApi(String requestId, String responseBody, Object images) {
		try {
			LogApiDetail logApiDetail = new LogApiDetail();
			logApiDetail.setLogId(requestId);
			logApiDetail.setResponse(responseBody);
			logApiDetail.setImages(images.toString());
			logApiDetailRepository.save(logApiDetail);

		} catch (Exception e) {
		}

	}

	@PostMapping(value = "/ekyc-enterprise/luu-tru-file", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruFile(HttpServletRequest req, @RequestBody String data) {
		try {
			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:" + "/ekyc-enterprise/ekyc";

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);
			FileHandling fileHandling = new FileHandling();

			// EkycDoanhNghiepTable doanhNghiep =
			// ekycDoanhNghiepRepository.findByToken(params.getToken());
			EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository
					.findByToken(req.getSession().getAttribute("tokenStatus").toString());

			if (doanhNghiep == null)
				return "demo/doanhnghiep2/step/steperror";

			EkycDoanhNghiep ekycDoanhNghiep2 = new EkycDoanhNghiep();
			if (doanhNghiep.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {

				ekycDoanhNghiep2 = gson.fromJson(doanhNghiep.getSsNoiDung(), EkycDoanhNghiep.class);
			} else {
				ekycDoanhNghiep2 = gson.fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);
			}

			doanhNghiep.setStatus(Contains.TRANG_THAI_KY_THANH_CONG);
			ekycDoanhNghiep2.setFileKy(luuAnh(ekycDoanhNghiep.getFileKy(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep2.setFileDangKy(luuAnh(ekycDoanhNghiep.getFileDangKy(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep2.setStatus(Contains.TRANG_THAI_KY_THANH_CONG);

			ekycDoanhNghiep.setDateAccountOpening(new Date());

			doanhNghiep.setStatusDonKy(Contains.TRANG_THAI_KY_THANH_CONG);

			doanhNghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep2));
			doanhNghiep.setSsNoiDung(new Gson().toJson(ekycDoanhNghiep2));

			// doanhNghiep.setCheckSum(getSHA256Hash(doanhNghiep.getNoiDung()));
			checkSumService.save(doanhNghiep);
			saveEkycDoanhNghiepTableHistory(doanhNghiep);

			sendAllMailSuccessSign(doanhNghiep, ekycDoanhNghiep2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject2 = new JSONObject();

		return jsonObject2.toString();
	}

	private void sendAllMailSuccessSign(EkycDoanhNghiepTable doanhNghiep, EkycDoanhNghiep ekycDoanhNghiep) {
		try {
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				guiMailKyThanhCong(ekycDoanhNghiep, ip, Contains.TRANG_THAI_KY_THANH_CONG);
			}

			for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
				guiMailKyThanhCong(ekycDoanhNghiep, ip, Contains.TRANG_THAI_KY_THANH_CONG);
			}
			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
				guiMailKyThanhCong(ekycDoanhNghiep, ip, Contains.TRANG_THAI_KY_THANH_CONG);
			}

			for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
				guiMailKyThanhCong(ekycDoanhNghiep, ip, Contains.TRANG_THAI_KY_THANH_CONG);
			}
			for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
				guiMailKyThanhCong(ekycDoanhNghiep, ip, Contains.TRANG_THAI_KY_THANH_CONG);
			}
		} catch (Exception e) {
		}
	}

	private void guiMailKyThanhCong(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {
		LOGGER.info("Send mail: ký thành công hợp đồng {}", ip.getEmail());
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(), "Quý khách đã hoàn tất thủ tục đăng ký mở tài khoản Doanh nghiệp.",
					"Kính gửi Quý Khách hàng, <br/> <br/>"
							+ "Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "sản phẩm của ngân hàng của chúng tôi.<br/><br/>"
							+ "Xin thông báo việc đăng ký mở tài khoản Doanh nghiệp đã hoàn tất. Chúng tôi sẽ tiến hành các thủ tục\r\n"
							+ "đăng ký và phản hồi Quý Khách hàng trong thời gian sớm nhất.<br/><br/>"
							+ "Trong trường hợp Quý khách có nhu cầu cập nhật thông tin, Quý khách có thể nhấn vào <a href='"
							+ LINK_ADMIN + "/ekyc-enterprise/upload?token=" + ekycDoanhNghiep.getToken()
							+ "&tokenCheck=" + ip.getTokenCheck() + "&type="
							+ Base64.getEncoder().encodeToString(type.getBytes()) + "'>đây</a><br/><br/> "
							+ "Trân trọng,<br/><br/>" + "Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)");

	}

//	@PostMapping(value = "/ekyc-enterprise/luu-tru-thong-tin", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public String luuTru(HttpServletRequest req, @RequestBody String data) {
//		JSONObject jsonObject2 = new JSONObject();
//
//		if (khongTrongThoiGianXyLy(req)) {
//			jsonObject2.put("status", 505);
//			return jsonObject2.toString();
//		}
//
//		try {
//			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
//			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);
//
//			
//			  if(!validateData(ekycDoanhNghiep)) { jsonObject2.put("status", 400); return
//			  jsonObject2.toString(); }
//			 
//
//			System.out.println(new Gson().toJson(ekycDoanhNghiep));
//			FileHandling fileHandling = new FileHandling();
//
//			ekycDoanhNghiep.setFileBusinessRegistration(
//					luuAnh(ekycDoanhNghiep.getFileBusinessRegistration(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileAppointmentOfChiefAccountant(
//					luuAnh(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileBusinessRegistrationCertificate(
//					luuAnh(ekycDoanhNghiep.getFileBusinessRegistrationCertificate(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileDecisionToAppointChiefAccountant(
//					luuAnh(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileInvestmentCertificate(
//					luuAnh(ekycDoanhNghiep.getFileInvestmentCertificate(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep
//					.setFileCompanyCharter(luuAnh(ekycDoanhNghiep.getFileCompanyCharter(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileSealSpecimen(luuAnh(ekycDoanhNghiep.getFileSealSpecimen(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileFatcaForms(luuAnh(ekycDoanhNghiep.getFileFatcaForms(), fileHandling, "abc.pdf"));
//			ekycDoanhNghiep.setFileOthers(luuAnh(ekycDoanhNghiep.getFileOthers(), fileHandling, "abc.pdf"));
//
//			ekycDoanhNghiep.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
//			ekycDoanhNghiep.setToken(UUID.randomUUID().toString());
//
//			EkycDoanhNghiepTable doanhNghiepTable = new EkycDoanhNghiepTable();
//			doanhNghiepTable.setMaDoanhNghiep(ekycDoanhNghiep.getTaxCode());
//			doanhNghiepTable.setTenDoanhNghiep(ekycDoanhNghiep.getNameCompany());
//			doanhNghiepTable.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
//			doanhNghiepTable.setNgayTao(new Date());
//			doanhNghiepTable.setToken(ekycDoanhNghiep.getToken());
//			doanhNghiepTable.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
//			doanhNghiepTable.setTenNguoiQuanLy(ekycDoanhNghiep.getRelationshipManagerName());
//
//			ekycDoanhNghiepRepository.save(doanhNghiepTable);
//
//			guiMailEkyc(ekycDoanhNghiep);
//			jsonObject2.put("token", ekycDoanhNghiep.getToken());
//			jsonObject2.put("status", 200);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return jsonObject2.toString();
//	}

	@PostMapping(value = "/ekyc-enterprise/luu-thong-tin-step2", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruStep1(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject2 = new JSONObject();

		if (khongTrongThoiGianXyLy(req)) {
			jsonObject2.put("status", 505);
			return jsonObject2.toString();
		}

		try {
			Object busername = req.getSession().getAttribute("b_username");

			if (busername == null)
				return "redirect:/login-doanh-nghiep";
			LOGGER.info("busername: " + busername.toString());

			EkycDoanhNghiepTable doanhNghiepTable = ekycDoanhNghiepRepository.findByUsername(busername.toString());

			if (doanhNghiepTable == null) {
				return "redirect:/login-doanh-nghiep";
			}
			// LOGGER.info("doanhNghiepTable: "+doanhNghiepTable.toString());

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			EkycDoanhNghiep ekycDoanhNghiepDb = new EkycDoanhNghiep();
			if (doanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiepDb = gson.fromJson(doanhNghiepTable.getSsNoiDung(), EkycDoanhNghiep.class);
			} else {
				ekycDoanhNghiepDb = gson.fromJson(doanhNghiepTable.getNoiDung(), EkycDoanhNghiep.class);
			}
			// EkycDoanhNghiep ekycDoanhNghiepDb =
			// gson.fromJson(doanhNghiepTable.getNoiDung(), EkycDoanhNghiep.class);
			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);

//			if (!validateDataStep1(ekycDoanhNghiep)) {
//				jsonObject2.put("status", 400);
//				return jsonObject2.toString();
//			}
			// LOGGER.info("ekycDoanhNghiep: "+ekycDoanhNghiep.toString());

			FileHandling fileHandling = new FileHandling();

			ekycDoanhNghiep.setFileBusinessRegistration(
					luuAnh(ekycDoanhNghiep.getFileBusinessRegistration(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileAppointmentOfChiefAccountant(
					luuAnh(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileBusinessRegistrationCertificate(
					luuAnh(ekycDoanhNghiep.getFileBusinessRegistrationCertificate(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileDecisionToAppointChiefAccountant(
					luuAnh(ekycDoanhNghiep.getFileDecisionToAppointChiefAccountant(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileInvestmentCertificate(
					luuAnh(ekycDoanhNghiep.getFileInvestmentCertificate(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep
					.setFileCompanyCharter(luuAnh(ekycDoanhNghiep.getFileCompanyCharter(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileSealSpecimen(luuAnh(ekycDoanhNghiep.getFileSealSpecimen(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileFatcaForms(luuAnh(ekycDoanhNghiep.getFileFatcaForms(), fileHandling, "abc.pdf"));
			ekycDoanhNghiep.setFileOthers(luuAnh(ekycDoanhNghiep.getFileOthers(), fileHandling, "abc.pdf"));

			ekycDoanhNghiep.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);

			if (doanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {
				ekycDoanhNghiep.setToken(UUID.randomUUID().toString());
				doanhNghiepTable.setToken(ekycDoanhNghiep.getToken());
			}

			doanhNghiepTable.setMaDoanhNghiep(ekycDoanhNghiep.getTaxCode());
			doanhNghiepTable.setTenDoanhNghiep(ekycDoanhNghiep.getNameCompany());
			// doanhNghiepTable.setNoiDung(new Gson().toJson(ekycDoanhNghiep));
			// doanhNghiepTable.setNgayTao(new Date());

			doanhNghiepTable.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
			doanhNghiepTable.setTenNguoiQuanLy(ekycDoanhNghiep.getRelationshipManagerName());
			// doanhNghiepTable.setUsername(busername);
			doanhNghiepTable.setStep("2");

			if (doanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {
				doanhNghiepTable.setStatusDonKy(Contains.TRANG_THAI_KY_THAT_BAI);
			}

			updateObjectToObject(ekycDoanhNghiepDb, ekycDoanhNghiep);
			// doanhNghiepTable.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));

			if (doanhNghiepTable.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				doanhNghiepTable.setSsNoiDung(new Gson().toJson(ekycDoanhNghiepDb));

			} else {
				doanhNghiepTable.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
			}
			checkSumService.save(doanhNghiepTable);

			req.getSession().setAttribute("token", doanhNghiepTable.getToken());

			jsonObject2.put("token", ekycDoanhNghiep.getToken());
			jsonObject2.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject2.toString();
	}

	@PostMapping(value = "/ekyc-enterprise/luu-thong-tin-step3", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruStep3(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject2 = new JSONObject();

		try {
			Object b_username = req.getSession().getAttribute("b_username");
			if (b_username == null)
				return "redirect:/login-doanh-nghiep";
			System.out.println("Token : " + token);
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			EkycDoanhNghiepTable doanhnghiepDb = ekycDoanhNghiepRepository.findByUsername(b_username.toString());

			if (doanhnghiepDb == null) {
				jsonObject2.put("status", 400);
				return jsonObject2.toString();
			}

			EkycDoanhNghiep ekycDoanhNghiep1 = new EkycDoanhNghiep();
			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiep1 = gson.fromJson(doanhnghiepDb.getSsNoiDung(), EkycDoanhNghiep.class);

				ekycDoanhNghiep1 = gson.fromJson(doanhnghiepDb.getSsNoiDung(), EkycDoanhNghiep.class);
				EkycDoanhNghiep ekycDoanhNghiep2 = gson.fromJson(data, EkycDoanhNghiep.class);
//				if (!validateDataStep2(ekycDoanhNghiep2)) {
//					jsonObject2.put("status", 400);
//					return jsonObject2.toString();
//				}
				EkycDoanhNghiepTable doanhnghiep = new EkycDoanhNghiepTable();
				doanhnghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep2));

				doanhnghiepDb.setMaDoanhNghiep(ekycDoanhNghiep2.getTaxCode());
				doanhnghiepDb.setTenDoanhNghiep(ekycDoanhNghiep2.getNameCompany());

				// doanhnghiepDb.setNgayTao(new Date());
				doanhnghiepDb.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
				doanhnghiepDb.setTenNguoiQuanLy(ekycDoanhNghiep2.getRelationshipManagerName());
				doanhnghiepDb.setStep("3");
				ekycDoanhNghiep1 = updateStep3(ekycDoanhNghiep1, ekycDoanhNghiep2);

				doanhnghiepDb.setSsNoiDung(new Gson().toJson(ekycDoanhNghiep1));
			} else if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THAT_BAI)) {

				ekycDoanhNghiep1 = gson.fromJson(doanhnghiepDb.getNoiDung(), EkycDoanhNghiep.class);
				EkycDoanhNghiep ekycDoanhNghiep2 = gson.fromJson(data, EkycDoanhNghiep.class);
//				if (!validateDataStep2(ekycDoanhNghiep2)) {
//					jsonObject2.put("status", 400);
//					return jsonObject2.toString();
//				}
				EkycDoanhNghiepTable doanhnghiep = new EkycDoanhNghiepTable();
				doanhnghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep2));

				doanhnghiepDb.setMaDoanhNghiep(ekycDoanhNghiep2.getTaxCode());
				doanhnghiepDb.setTenDoanhNghiep(ekycDoanhNghiep2.getNameCompany());

				// doanhnghiepDb.setNgayTao(new Date());
				doanhnghiepDb.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
				doanhnghiepDb.setTenNguoiQuanLy(ekycDoanhNghiep2.getRelationshipManagerName());
				doanhnghiepDb.setStep("3");
				updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);

				doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiep1));

			}

			// doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiep1));

			checkSumService.save(doanhnghiepDb);

			jsonObject2.put("token", token);
			jsonObject2.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject2.toString();
	}

	public EkycDoanhNghiep updateStep3(EkycDoanhNghiep ekycDoanhNghiep1, EkycDoanhNghiep ekycDoanhNghiep2) {
		if (
		// !ekycDoanhNghiep1.getNumber().equals(ekycDoanhNghiep2.getNumber())
		// !ekycDoanhNghiep1.getDateAccountOpening().equals(ekycDoanhNghiep2.getDateAccountOpening())
		!ekycDoanhNghiep1.getNameOfTheAccountHolder().equals(ekycDoanhNghiep2.getNameOfTheAccountHolder())
				|| !ekycDoanhNghiep1.getNameCompany().equals(ekycDoanhNghiep2.getNameCompany())
				|| !ekycDoanhNghiep1.getRegisteredAddress().equals(ekycDoanhNghiep2.getRegisteredAddress())
				|| !ekycDoanhNghiep1.getOperatingAddress().equals(ekycDoanhNghiep2.getOperatingAddress())
				|| !ekycDoanhNghiep1.getCountryOfIncorporation().equals(ekycDoanhNghiep2.getCountryOfIncorporation())
				|| !ekycDoanhNghiep1.getStraight2BankGroupID().equals(ekycDoanhNghiep2.getStraight2BankGroupID())
				|| !ekycDoanhNghiep1.getMailingAddress().equals(ekycDoanhNghiep2.getMailingAddress())
				|| !ekycDoanhNghiep1.getSwiftBankIDCode().equals(ekycDoanhNghiep2.getSwiftBankIDCode())
				|| !ekycDoanhNghiep1.getMobileOfficeTelephone().equals(ekycDoanhNghiep2.getMobileOfficeTelephone())
				|| !ekycDoanhNghiep1.getContactPerson().equals(ekycDoanhNghiep2.getContactPerson())
				|| !ekycDoanhNghiep1.getEmailAddress().equals(ekycDoanhNghiep2.getEmailAddress())
				|| !ekycDoanhNghiep1.getListAccount().equals(ekycDoanhNghiep2.getListAccount())
				|| !ekycDoanhNghiep1.getShortName().equals(ekycDoanhNghiep2.getShortName())
				|| !ekycDoanhNghiep1.getNameInEnglish().equals(ekycDoanhNghiep2.getNameInEnglish())
				|| !ekycDoanhNghiep1.getFaxNumber().equals(ekycDoanhNghiep2.getFaxNumber())
				|| !ekycDoanhNghiep1.getTaxCode().equals(ekycDoanhNghiep2.getTaxCode())
				|| !ekycDoanhNghiep1.getApplicableAccountingSystems()
						.equals(ekycDoanhNghiep2.getApplicableAccountingSystems())
				|| !ekycDoanhNghiep1.getTaxMode().equals(ekycDoanhNghiep2.getTaxMode())
				|| !ekycDoanhNghiep1.getResidentStatus().equals(ekycDoanhNghiep2.getResidentStatus())
				|| !ekycDoanhNghiep1.getResidentStatus().equals(ekycDoanhNghiep2.getResidentStatus())
				|| !ekycDoanhNghiep1.getBusinessActivities().equals(ekycDoanhNghiep2.getBusinessActivities())
				|| !ekycDoanhNghiep1.getYearlyAveragenumber().equals(ekycDoanhNghiep2.getYearlyAveragenumber())
				|| !ekycDoanhNghiep1.getTotalSalesTurnover().equals(ekycDoanhNghiep2.getTotalSalesTurnover())
				|| !ekycDoanhNghiep1.getTotalCapital().equals(ekycDoanhNghiep2.getTotalCapital())
				|| !ekycDoanhNghiep1.getTotalCapital().equals(ekycDoanhNghiep2.getTotalCapital())
				|| !ekycDoanhNghiep1.getAgreeToReceive().equals(ekycDoanhNghiep2.getAgreeToReceive())) {
			ekycDoanhNghiep1.setNameOfTheAccountHolder(ekycDoanhNghiep2.getNameOfTheAccountHolder());
			ekycDoanhNghiep1.setNumber(ekycDoanhNghiep2.getNumber());
			// ekycDoanhNghiep1.setDateAccountOpening(ekycDoanhNghiep2.getDateAccountOpening());
			ekycDoanhNghiep1.setNameCompany(ekycDoanhNghiep2.getNameCompany());
			ekycDoanhNghiep1.setRegisteredAddress(ekycDoanhNghiep2.getRegisteredAddress());
			ekycDoanhNghiep1.setOperatingAddress(ekycDoanhNghiep2.getOperatingAddress());
			ekycDoanhNghiep1.setCountryOfIncorporation(ekycDoanhNghiep2.getCountryOfIncorporation());
			ekycDoanhNghiep1.setStraight2BankGroupID(ekycDoanhNghiep2.getStraight2BankGroupID());
			ekycDoanhNghiep1.setMailingAddress(ekycDoanhNghiep2.getMailingAddress());
			ekycDoanhNghiep1.setSwiftBankIDCode(ekycDoanhNghiep2.getSwiftBankIDCode());
			ekycDoanhNghiep1.setMobileOfficeTelephone(ekycDoanhNghiep2.getMobileOfficeTelephone());
			ekycDoanhNghiep1.setContactPerson(ekycDoanhNghiep2.getContactPerson());
			ekycDoanhNghiep1.setEmailAddress(ekycDoanhNghiep2.getEmailAddress());
			ekycDoanhNghiep1.setListAccount(ekycDoanhNghiep2.getListAccount());
			ekycDoanhNghiep1.setShortName(ekycDoanhNghiep2.getShortName());
			ekycDoanhNghiep1.setNameInEnglish(ekycDoanhNghiep2.getNameInEnglish());
			ekycDoanhNghiep1.setFaxNumber(ekycDoanhNghiep2.getFaxNumber());
			ekycDoanhNghiep1.setTaxCode(ekycDoanhNghiep2.getTaxCode());
			ekycDoanhNghiep1.setApplicableAccountingSystems(ekycDoanhNghiep2.getApplicableAccountingSystems());
			ekycDoanhNghiep1.setTaxMode(ekycDoanhNghiep2.getTaxMode());
			ekycDoanhNghiep1.setResidentStatus(ekycDoanhNghiep2.getResidentStatus());
			ekycDoanhNghiep1.setBusinessActivities(ekycDoanhNghiep2.getBusinessActivities());
			ekycDoanhNghiep1.setYearlyAveragenumber(ekycDoanhNghiep2.getYearlyAveragenumber());
			ekycDoanhNghiep1.setTotalSalesTurnover(ekycDoanhNghiep2.getTotalSalesTurnover());
			ekycDoanhNghiep1.setTotalCapital(ekycDoanhNghiep2.getTotalCapital());
			ekycDoanhNghiep1.setAgreeToReceive(ekycDoanhNghiep2.getAgreeToReceive());
			ekycDoanhNghiep1.setStatusStep3("yes");
		} else {
			ekycDoanhNghiep1.setStatusStep3("no");
		}
		return ekycDoanhNghiep1;
	}

	@PostMapping(value = "/ekyc-enterprise/luu-thong-tin-step9", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruStep9(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject2 = new JSONObject();

		try {
			Object username = req.getSession().getAttribute("b_username");
			if (username == null)
				return "redirect:/login-doanh-nghiep";
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

			EkycDoanhNghiepTable doanhnghiepDb = ekycDoanhNghiepRepository.findByUsername(username.toString());

			if (doanhnghiepDb == null) {
				jsonObject2.put("status", 505);
				return jsonObject2.toString();
			}
			EkycDoanhNghiep ekycDoanhNghiepDb = new EkycDoanhNghiep();
			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiepDb = gson.fromJson(doanhnghiepDb.getSsNoiDung(), EkycDoanhNghiep.class);
			} else {
				ekycDoanhNghiepDb = gson.fromJson(doanhnghiepDb.getNoiDung(), EkycDoanhNghiep.class);
			}

			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);

			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {

				if (ekycDoanhNghiepDb.getUserDesignation().size() <= ekycDoanhNghiep.getUserDesignation().size()) {
					for (int y = 0; y < ekycDoanhNghiep.getUserDesignation().size(); y++) {
						if (y + 1 > ekycDoanhNghiepDb.getUserDesignation().size()) {
							ekycDoanhNghiep.getUserDesignation().get(y).setEditStatus("yes");
							ekycDoanhNghiepDb.getUserDesignation().add(ekycDoanhNghiep.getUserDesignation().get(y));

						} else if (!ekycDoanhNghiepDb.getUserDesignation().get(y).getHoTen()
								.equals(ekycDoanhNghiep.getUserDesignation().get(y).getHoTen())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getEmail()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getEmail())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getSoCmt()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getSoCmt())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getChapThuanLenh()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenh())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getChapThuanLenhDongThoi().equals(
										ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenhDongThoi())) {
							ekycDoanhNghiepDb.getUserDesignation().get(y).setEditStatus("yes");
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setEmail(ekycDoanhNghiep.getUserDesignation().get(y).getEmail());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setHoTen(ekycDoanhNghiep.getUserDesignation().get(y).getHoTen());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setSoCmt(ekycDoanhNghiep.getUserDesignation().get(y).getSoCmt());

							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setTaoLenh(ekycDoanhNghiep.getUserDesignation().get(y).getTaoLenh());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setBaoCao(ekycDoanhNghiep.getUserDesignation().get(y).getBaoCao());
							ekycDoanhNghiepDb.getUserDesignation().get(y).setChapThuanLenhDongThoi(
									ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenhDongThoi());

							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setChapThuanLenh(ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenh());

						} else {
							ekycDoanhNghiepDb.getUserDesignation().get(y).setEditStatus("no");
						}

					}
				} else if (ekycDoanhNghiepDb.getUserDesignation().size() > ekycDoanhNghiep.getUserDesignation()
						.size()) {

					for (int i = 0; i < ekycDoanhNghiepDb.getUserDesignation().size(); i++) {
						if (!checkItemEmpty(ekycDoanhNghiepDb.getUserDesignation().get(i).getId(),
								ekycDoanhNghiep.getUserDesignation())) {
							ekycDoanhNghiepDb.getUserDesignation().remove(i);
							i--;
						}

					}

					for (int y = 0; y < ekycDoanhNghiep.getUserDesignation().size(); y++) {
						if (!ekycDoanhNghiepDb.getUserDesignation().get(y).getHoTen()
								.equals(ekycDoanhNghiep.getUserDesignation().get(y).getHoTen())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getEmail()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getEmail())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getSoCmt()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getSoCmt())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getChapThuanLenh()
										.equals(ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenh())
								|| !ekycDoanhNghiepDb.getUserDesignation().get(y).getChapThuanLenhDongThoi().equals(
										ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenhDongThoi())) {
							ekycDoanhNghiepDb.getUserDesignation().get(y).setEditStatus("yes");
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setEmail(ekycDoanhNghiep.getUserDesignation().get(y).getEmail());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setHoTen(ekycDoanhNghiep.getUserDesignation().get(y).getHoTen());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setSoCmt(ekycDoanhNghiep.getUserDesignation().get(y).getSoCmt());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setTaoLenh(ekycDoanhNghiep.getUserDesignation().get(y).getTaoLenh());
							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setBaoCao(ekycDoanhNghiep.getUserDesignation().get(y).getBaoCao());
							ekycDoanhNghiepDb.getUserDesignation().get(y).setChapThuanLenhDongThoi(
									ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenhDongThoi());

							ekycDoanhNghiepDb.getUserDesignation().get(y)
									.setChapThuanLenh(ekycDoanhNghiep.getUserDesignation().get(y).getChapThuanLenh());
						} else {
							ekycDoanhNghiepDb.setEditStatusNddpl("no");
							ekycDoanhNghiepDb.getUserDesignation().get(y).setEditStatus("no");
						}
					}
				}

			} else {
				updateObjectToObject(ekycDoanhNghiepDb, ekycDoanhNghiep);
			}

			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				doanhnghiepDb.setSsNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
				// doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
			} else {
				doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
			}

			doanhnghiepDb.setStep("9");
			checkSumService.save(doanhnghiepDb);

			jsonObject2.put("status", 200);
		} catch (Exception e) {
			jsonObject2.put("status", 505);
		}

		return jsonObject2.toString();
	}

	@PostMapping(value = "/ekyc-enterprise/luu-thong-tin-step0", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruStep0(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject2 = new JSONObject();

		try {
			Object username = req.getSession().getAttribute("b_username");

			if (username == null)
				return "redirect:/login-doanh-nghiep";
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

			EkycDoanhNghiepTable doanhnghiepDb = ekycDoanhNghiepRepository.findByUsername(username.toString());

			EkycDoanhNghiep ekycDoanhNghiepDb = new EkycDoanhNghiep();
			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiepDb = gson.fromJson(doanhnghiepDb.getSsNoiDung(), EkycDoanhNghiep.class);
			} else {
				ekycDoanhNghiepDb = gson.fromJson(doanhnghiepDb.getNoiDung(), EkycDoanhNghiep.class);
			}

			EkycDoanhNghiep ekycDoanhNghiep = gson.fromJson(data, EkycDoanhNghiep.class);

			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiepDb.setTypeDocument(ekycDoanhNghiep.getTypeDocument());
			} else {
				ekycDoanhNghiepDb = gson.fromJson(data, EkycDoanhNghiep.class);
				// updateObjectToObject(ekycDoanhNghiepDb, ekycDoanhNghiep);

			}

			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				doanhnghiepDb.setSsNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
			} else {
				doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiepDb));
			}

			doanhnghiepDb.setStep("1");
			checkSumService.save(doanhnghiepDb);

			jsonObject2.put("status", 200);
		} catch (Exception e) {
			jsonObject2.put("status", 505);
		}

		return jsonObject2.toString();
	}

	public int search(ArrayList<InfoPerson> list, InfoPerson infoPerson) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(infoPerson.getId())) {
				return i;
			}
		}
		return -1;
	}

	@PostMapping(value = "/ekyc-enterprise/step{step}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String luuTruStep3(HttpServletRequest req, @RequestBody String data, @PathVariable("step") String step) {
		JSONObject jsonObject2 = new JSONObject();

		try {

			Object username = req.getSession().getAttribute("b_username");
			if (username == null)
				return "redirect:/login-doanh-nghiep";

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			EkycDoanhNghiepTable doanhnghiepDb = ekycDoanhNghiepRepository.findByUsername(username.toString());
			if (doanhnghiepDb == null) {
				jsonObject2.put("status", 505);
				return jsonObject2.toString();
			}
			EkycDoanhNghiep ekycDoanhNghiep1 = new EkycDoanhNghiep();
			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				ekycDoanhNghiep1 = gson.fromJson(doanhnghiepDb.getSsNoiDung(), EkycDoanhNghiep.class);
			} else {
				ekycDoanhNghiep1 = gson.fromJson(doanhnghiepDb.getNoiDung(), EkycDoanhNghiep.class);
			}

			EkycDoanhNghiep ekycDoanhNghiep2 = gson.fromJson(data, EkycDoanhNghiep.class);

//			EkycDoanhNghiepTable doanhnghiep = new EkycDoanhNghiepTable();
//			doanhnghiep.setNoiDung(new Gson().toJson(ekycDoanhNghiep2));

			doanhnghiepDb.setMaDoanhNghiep(ekycDoanhNghiep1.getTaxCode());
			doanhnghiepDb.setTenDoanhNghiep(ekycDoanhNghiep1.getNameCompany());

			// doanhnghiepDb.setNgayTao(new Date());
			doanhnghiepDb.setStatus(Contains.TRANG_THAI_KY_THAT_BAI);
			doanhnghiepDb.setTenNguoiQuanLy(ekycDoanhNghiep1.getRelationshipManagerName());
			doanhnghiepDb.setStep(step);

			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				if (step.equals("10")) {
					updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);
				}

				if (step.equals("6")) {
					if (ekycDoanhNghiep1.getListOfLeaders().size() <= ekycDoanhNghiep2.getListOfLeaders().size()) {
						for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
							if (y + 1 > ekycDoanhNghiep1.getListOfLeaders().size()) {
								ekycDoanhNghiep2.getListOfLeaders().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getListOfLeaders().add(ekycDoanhNghiep2.getListOfLeaders().get(y));

							} else if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
									|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusBld("yes");
								ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusBld("no");
								ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
							}

						}
					} else if (ekycDoanhNghiep1.getListOfLeaders().size() > ekycDoanhNghiep2.getListOfLeaders()
							.size()) {

						for (int i = 0; i < ekycDoanhNghiep1.getListOfLeaders().size(); i++) {
							if (!checkItemEmpty(ekycDoanhNghiep1.getListOfLeaders().get(i).getId(),
									ekycDoanhNghiep2.getListOfLeaders())) {
								ekycDoanhNghiep1.getListOfLeaders().remove(i);
								i--;
							}

						}
						for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
							if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
									|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
								ekycDoanhNghiep1.getListOfLeaders().get(y)
										.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
							}
						}

					}

				}
				if (step.equals("4")) {
					ekycDoanhNghiep1.setAllInOne(ekycDoanhNghiep2.getAllInOne());
					ekycDoanhNghiep1.setHaveAcccountHolder(ekycDoanhNghiep2.getHaveAcccountHolder());
					if (ekycDoanhNghiep1.getLegalRepresentator().size() <= ekycDoanhNghiep2.getLegalRepresentator()
							.size()) {
						for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
							if (y + 1 > ekycDoanhNghiep1.getLegalRepresentator().size()) {
								ekycDoanhNghiep2.getLegalRepresentator().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getLegalRepresentator()
										.add(ekycDoanhNghiep2.getLegalRepresentator().get(y));

							} else if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());

							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
							}

						}

					} else if (ekycDoanhNghiep1.getLegalRepresentator().size() > ekycDoanhNghiep2
							.getLegalRepresentator().size()) {

						for (int i = 0; i < ekycDoanhNghiep1.getLegalRepresentator().size(); i++) {
							if (!checkItemEmpty(ekycDoanhNghiep1.getLegalRepresentator().get(i).getId(),
									ekycDoanhNghiep2.getLegalRepresentator())) {
								ekycDoanhNghiep1.getLegalRepresentator().remove(i);
								i--;
							}

						}
						for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
							if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
									|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
											.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
								ekycDoanhNghiep1.getLegalRepresentator().get(y)
										.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
							}
						}

					}

				}
				if (step.equals("5")) {
					ekycDoanhNghiep1.setHaveAChiefAccountant(ekycDoanhNghiep2.getHaveAChiefAccountant());
					if (ekycDoanhNghiep1.getChiefAccountant().size() <= ekycDoanhNghiep2.getChiefAccountant().size()) {
						for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
							if (y + 1 > ekycDoanhNghiep1.getChiefAccountant().size()) {
								ekycDoanhNghiep2.getChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getChiefAccountant().add(ekycDoanhNghiep2.getChiefAccountant().get(y));
								break;
							} else if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
									|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
								ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.setEditStatusKtt("yes");
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setId(ekycDoanhNghiep2.getChiefAccountant().get(y).getId());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusKtt("no");
								ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
							}

						}
					} else if (ekycDoanhNghiep1.getChiefAccountant().size() > ekycDoanhNghiep2.getChiefAccountant()
							.size()) {
						for (int i = 0; i < ekycDoanhNghiep1.getChiefAccountant().size(); i++) {
							if (!checkItemEmpty(ekycDoanhNghiep1.getChiefAccountant().get(i).getId(),
									ekycDoanhNghiep2.getChiefAccountant())) {
								ekycDoanhNghiep1.getChiefAccountant().remove(i);
								i--;
							}

						}
						for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
							if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
									.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
									|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
											.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
									|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
											.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
								ekycDoanhNghiep1.setEditStatusNddpl("yes");
								ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
								ekycDoanhNghiep1.getChiefAccountant().get(y)
										.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
							} else {
								ekycDoanhNghiep1.setEditStatusNddpl("no");
								ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
							}
						}
					}

				}
				if (step.equals("7")) {
					if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder() == null) {
						// ArrayList<InfoPerson> list = new ArrayList<>();
						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
							ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");

						}

						updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);

					} else {
						if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size() <= ekycDoanhNghiep2
								.getPersonAuthorizedAccountHolder().size()) {
							for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
								if (y + 1 > ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size()) {
									ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder()
											.add(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y));

								} else if (!ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getHoTen()
										.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen())
										|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getEmail()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y)
														.getEmail())
										|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getPhone()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y)
														.getPhone())) {
									ekycDoanhNghiep1.setEditStatusNuq("yes");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
											.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEmail(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setHoTen(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setPhone(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTokenCheck(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTokenCheck());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
											.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTime(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTime());
								} else {
									ekycDoanhNghiep1.setEditStatusNuq("no");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("no");
								}

							}
						} else if (ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size() > ekycDoanhNghiep2
								.getPersonAuthorizedAccountHolder().size()) {
							for (int i = 0; i < ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().size(); i++) {
								if (!checkItemEmpty(ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(i).getId(),
										ekycDoanhNghiep2.getPersonAuthorizedAccountHolder())) {
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().remove(i);
									i--;
								}

							}
							for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().size(); y++) {
								if (!ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getHoTen()
										.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen())
										|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getEmail()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y)
														.getEmail())
										|| !ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).getPhone()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y)
														.getPhone())) {
									ekycDoanhNghiep1.setEditStatusNddpl("yes");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEmail(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getEmail());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setHoTen(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getHoTen());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setPhone(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getPhone());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTokenCheck(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTokenCheck());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y)
											.setId(ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getId());
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setTime(
											ekycDoanhNghiep2.getPersonAuthorizedAccountHolder().get(y).getTime());
								} else {
									ekycDoanhNghiep1.setEditStatusNddpl("no");
									ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(y).setEditStatus("no");
								}
							}
						}
					}

				}
				if (step.equals("8")) {
					if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant() == null) {

						for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
							ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
						}
						updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);

					} else {
						if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size() <= ekycDoanhNghiep2
								.getPersonAuthorizedChiefAccountant().size()) {
							for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
								if (y + 1 > ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size()) {
									ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant()
											.add(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y));

								} else if (!ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getHoTen()
										.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen())
										|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getEmail()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
														.getEmail())
										|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getPhone()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
														.getPhone())) {
									ekycDoanhNghiep1.setEditStatusNuqKtt("yes");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setId(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getId());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEmail(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setHoTen(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setPhone(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y)
											.setTokenCheck(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
													.getTokenCheck());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTime(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTime());
								} else {
									ekycDoanhNghiep1.setEditStatusNuqKtt("no");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("no");
								}

							}
						} else if (ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size() > ekycDoanhNghiep2
								.getPersonAuthorizedChiefAccountant().size()) {
							for (int i = 0; i < ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().size(); i++) {
								if (!checkItemEmpty(ekycDoanhNghiep1.getPersonAuthorizedAccountHolder().get(i).getId(),
										ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant())) {
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().remove(i);
									i--;
								}

							}
							for (int y = 0; y < ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().size(); y++) {
								if (!ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getHoTen()
										.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen())
										|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getEmail()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
														.getEmail())
										|| !ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).getPhone()
												.equals(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
														.getPhone())) {
									ekycDoanhNghiep1.setEditStatusNddpl("yes");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("yes");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEmail(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getEmail());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setHoTen(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getHoTen());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setPhone(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getPhone());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y)
											.setTokenCheck(ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y)
													.getTokenCheck());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setId(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getId());
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setTime(
											ekycDoanhNghiep2.getPersonAuthorizedChiefAccountant().get(y).getTime());
								} else {
									ekycDoanhNghiep1.setEditStatusNddpl("no");
									ekycDoanhNghiep1.getPersonAuthorizedChiefAccountant().get(y).setEditStatus("no");
								}
							}
						}
					}

				}
				if (step.equals("456")) {
					ekycDoanhNghiep1 = updateStep456(ekycDoanhNghiep1, ekycDoanhNghiep2);
				}

			} else {
				updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);
			}

			// updateObjectToObject(ekycDoanhNghiep1, ekycDoanhNghiep2);
			if (doanhnghiepDb.getStatusDonKy().equals(Contains.TRANG_THAI_KY_THANH_CONG)) {
				doanhnghiepDb.setSsNoiDung(new Gson().toJson(ekycDoanhNghiep1));
				;
			} else {
				doanhnghiepDb.setNoiDung(new Gson().toJson(ekycDoanhNghiep1));
			}
			// doanhnghiepDb.setSsNoiDung(new Gson().toJson(ekycDoanhNghiep1));
			checkSumService.save(doanhnghiepDb);
			jsonObject2.put("token", token);
			jsonObject2.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject2.toString();
	}

	public EkycDoanhNghiep updateStep456(EkycDoanhNghiep ekycDoanhNghiep1, EkycDoanhNghiep ekycDoanhNghiep2) {

		if (ekycDoanhNghiep1.getListOfLeaders().size() <= ekycDoanhNghiep2.getListOfLeaders().size()) {
			for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
				if (y + 1 > ekycDoanhNghiep1.getListOfLeaders().size()) {
					ekycDoanhNghiep2.getListOfLeaders().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getListOfLeaders().add(ekycDoanhNghiep2.getListOfLeaders().get(y));

				} else if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
						|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
					ekycDoanhNghiep1.setEditStatusBld("yes");
					ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");

					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());

					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
				} else {
					ekycDoanhNghiep1.setEditStatusBld("no");
					ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
				}

			}
		} else if (ekycDoanhNghiep1.getListOfLeaders().size() > ekycDoanhNghiep2.getListOfLeaders().size()) {

			for (int i = 0; i < ekycDoanhNghiep1.getListOfLeaders().size(); i++) {
				if (!checkItemEmpty(ekycDoanhNghiep1.getListOfLeaders().get(i).getId(),
						ekycDoanhNghiep2.getListOfLeaders())) {
					ekycDoanhNghiep1.getListOfLeaders().remove(i);
					i--;
				}

			}
			for (int y = 0; y < ekycDoanhNghiep2.getListOfLeaders().size(); y++) {
				if (!ekycDoanhNghiep1.getListOfLeaders().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail())
						|| !ekycDoanhNghiep1.getListOfLeaders().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone())) {
					ekycDoanhNghiep1.setEditStatusNddpl("yes");
					ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setEmail(ekycDoanhNghiep2.getListOfLeaders().get(y).getEmail());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setHoTen(ekycDoanhNghiep2.getListOfLeaders().get(y).getHoTen());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setPhone(ekycDoanhNghiep2.getListOfLeaders().get(y).getPhone());

					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getListOfLeaders().get(y).getTokenCheck());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setId(ekycDoanhNghiep2.getListOfLeaders().get(y).getId());
					ekycDoanhNghiep1.getListOfLeaders().get(y)
							.setTime(ekycDoanhNghiep2.getListOfLeaders().get(y).getTime());
				} else {
					ekycDoanhNghiep1.setEditStatusNddpl("no");
					ekycDoanhNghiep1.getListOfLeaders().get(y).setEditStatus("no");
				}
			}

		}

		ekycDoanhNghiep1.setAllInOne(ekycDoanhNghiep2.getAllInOne());
		ekycDoanhNghiep1.setHaveAcccountHolder(ekycDoanhNghiep2.getHaveAcccountHolder());
		if (ekycDoanhNghiep1.getLegalRepresentator().size() <= ekycDoanhNghiep2.getLegalRepresentator().size()) {
			for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
				if (y + 1 > ekycDoanhNghiep1.getLegalRepresentator().size()) {
					ekycDoanhNghiep2.getLegalRepresentator().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getLegalRepresentator().add(ekycDoanhNghiep2.getLegalRepresentator().get(y));

				} else if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
					ekycDoanhNghiep1.setEditStatusNddpl("yes");
					ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());

				} else {
					ekycDoanhNghiep1.setEditStatusNddpl("no");
					ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
				}

			}

		} else if (ekycDoanhNghiep1.getLegalRepresentator().size() > ekycDoanhNghiep2.getLegalRepresentator().size()) {

			for (int i = 0; i < ekycDoanhNghiep1.getLegalRepresentator().size(); i++) {
				if (!checkItemEmpty(ekycDoanhNghiep1.getLegalRepresentator().get(i).getId(),
						ekycDoanhNghiep2.getLegalRepresentator())) {
					ekycDoanhNghiep1.getLegalRepresentator().remove(i);
					i--;
				}

			}
			for (int y = 0; y < ekycDoanhNghiep2.getLegalRepresentator().size(); y++) {
				if (!ekycDoanhNghiep1.getLegalRepresentator().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone())
						|| !ekycDoanhNghiep1.getLegalRepresentator().get(y).getCheckMain()
								.equals(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain())) {
					ekycDoanhNghiep1.setEditStatusNddpl("yes");
					ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setId(ekycDoanhNghiep2.getLegalRepresentator().get(y).getId());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setEmail(ekycDoanhNghiep2.getLegalRepresentator().get(y).getEmail());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setHoTen(ekycDoanhNghiep2.getLegalRepresentator().get(y).getHoTen());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setPhone(ekycDoanhNghiep2.getLegalRepresentator().get(y).getPhone());

					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTokenCheck());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setCheckMain(ekycDoanhNghiep2.getLegalRepresentator().get(y).getCheckMain());
					ekycDoanhNghiep1.getLegalRepresentator().get(y)
							.setTime(ekycDoanhNghiep2.getLegalRepresentator().get(y).getTime());
				} else {
					ekycDoanhNghiep1.setEditStatusNddpl("no");
					ekycDoanhNghiep1.getLegalRepresentator().get(y).setEditStatus("no");
				}
			}

		}

		ekycDoanhNghiep1.setHaveAChiefAccountant(ekycDoanhNghiep2.getHaveAChiefAccountant());
		if (ekycDoanhNghiep1.getChiefAccountant().size() <= ekycDoanhNghiep2.getChiefAccountant().size()) {
			for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
				if (y + 1 > ekycDoanhNghiep1.getChiefAccountant().size()) {
					ekycDoanhNghiep2.getChiefAccountant().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getChiefAccountant().add(ekycDoanhNghiep2.getChiefAccountant().get(y));
					break;
				} else if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
						|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
					ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.setEditStatusKtt("yes");
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setId(ekycDoanhNghiep2.getChiefAccountant().get(y).getId());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
				} else {
					ekycDoanhNghiep1.setEditStatusKtt("no");
					ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
				}

			}
		} else if (ekycDoanhNghiep1.getChiefAccountant().size() > ekycDoanhNghiep2.getChiefAccountant().size()) {
			for (int i = 0; i < ekycDoanhNghiep1.getChiefAccountant().size(); i++) {
				if (!checkItemEmpty(ekycDoanhNghiep1.getChiefAccountant().get(i).getId(),
						ekycDoanhNghiep2.getChiefAccountant())) {
					ekycDoanhNghiep1.getChiefAccountant().remove(i);
					i--;
				}

			}
			for (int y = 0; y < ekycDoanhNghiep2.getChiefAccountant().size(); y++) {
				if (!ekycDoanhNghiep1.getChiefAccountant().get(y).getHoTen()
						.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen())
						|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getEmail()
								.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail())
						|| !ekycDoanhNghiep1.getChiefAccountant().get(y).getPhone()
								.equals(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone())) {
					ekycDoanhNghiep1.setEditStatusNddpl("yes");
					ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("yes");
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setEmail(ekycDoanhNghiep2.getChiefAccountant().get(y).getEmail());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setHoTen(ekycDoanhNghiep2.getChiefAccountant().get(y).getHoTen());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setPhone(ekycDoanhNghiep2.getChiefAccountant().get(y).getPhone());

					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setTokenCheck(ekycDoanhNghiep2.getChiefAccountant().get(y).getTokenCheck());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setLoai(ekycDoanhNghiep2.getChiefAccountant().get(y).getLoai());
					ekycDoanhNghiep1.getChiefAccountant().get(y)
							.setTime(ekycDoanhNghiep2.getChiefAccountant().get(y).getTime());
				} else {
					ekycDoanhNghiep1.setEditStatusNddpl("no");
					ekycDoanhNghiep1.getChiefAccountant().get(y).setEditStatus("no");
				}
			}
		}

		return ekycDoanhNghiep1;
	}

	public static boolean checkItemEmpty(String id, ArrayList<InfoPerson> arr) {
		boolean rs = false;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getId().equals(id)) {
				rs = true;
			}
		}

		return rs;

	}
//	private boolean validateData(EkycDoanhNghiep ekycDoanhNghiep) {
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileBusinessRegistration()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileCompanyCharter()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileFatcaForms()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getNameOfTheAccountHolder()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getNameCompany()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getRegisteredAddress()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getRegistrationNumber()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getMailingAddress()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getContactPerson()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getEmailAddress()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getResidentStatus()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getBusinessActivities()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getTotalSalesTurnover()))
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getTotalCapital()))
//			return false;
////		if(StringUtils.isEmpty(ekycDoanhNghiep.getAccountType())) return false;
////		if(StringUtils.isEmpty(ekycDoanhNghiep.getCurrency())) return false;
////		if(StringUtils.isEmpty(ekycDoanhNghiep.getCurrency())) return false;
//
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getListAccount()))
//			return false;
//		if (ekycDoanhNghiep.getListAccount().size() == 0)
//			return false;
//
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getLegalRepresentator()))
//			return false;
//		if (ekycDoanhNghiep.getLegalRepresentator().size() == 0)
//			return false;
//		if (StringUtils.isEmpty(ekycDoanhNghiep.getListOfLeaders()))
//			return false;
//		if (ekycDoanhNghiep.getListOfLeaders().size() == 0)
//			return false;
//		return true;
//	}

	private boolean validateDataStep1(EkycDoanhNghiep ekycDoanhNghiep) {
		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileBusinessRegistration()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileAppointmentOfChiefAccountant()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileCompanyCharter()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getFileFatcaForms()))
			return false;
		return true;
	}

	private boolean validateDataStep2(EkycDoanhNghiep ekycDoanhNghiep) {
		if (StringUtils.isEmpty(ekycDoanhNghiep.getNameOfTheAccountHolder()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getNameCompany()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getRegisteredAddress()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getRegistrationNumber()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getMailingAddress()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getContactPerson()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getEmailAddress()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getResidentStatus()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getBusinessActivities()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getTotalSalesTurnover()))
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getTotalCapital()))
			return false;

		if (StringUtils.isEmpty(ekycDoanhNghiep.getListAccount()))
			return false;
		if (ekycDoanhNghiep.getListAccount().size() == 0)
			return false;
		return true;
	}

	private boolean validateDataStep3(EkycDoanhNghiep ekycDoanhNghiep, String step) {
		if (step == "4") {
			if (StringUtils.isEmpty(ekycDoanhNghiep.getLegalRepresentator()))
				return false;
		}

		if (ekycDoanhNghiep.getLegalRepresentator().size() == 0)
			return false;
		if (StringUtils.isEmpty(ekycDoanhNghiep.getListOfLeaders()))
			return false;
		if (ekycDoanhNghiep.getListOfLeaders().size() == 0)
			return false;
		return true;
	}

	private boolean khongTrongThoiGianXyLy(HttpServletRequest req) {

		ParamsKbank params = getParams(req);
		if (params == null)
			return true;
//    	long timeOut = configProperties.getConfig().getTimeout_link_front() != null?Long.valueOf(configProperties.getConfig().getTimeout_link_front()):24;
//    	long thoiGianhetHan = timeOut*60*60*1000L + params.getTimeStartStep();
//    	if(thoiGianhetHan < System.currentTimeMillis()) return true;

		return false;

	}

	public void guiMainUpdateEkyc(EkycDoanhNghiep ekycDoanhNghiep, HttpServletRequest req,
			EkycDoanhNghiepTable ekycDoanhNghiepTable) {
		String fullName = req.getSession().getAttribute("b_fullName").toString();
		String userName = req.getSession().getAttribute("b_username").toString();
		String emailGui = ekycDoanhNghiepTable.getEmailLogin();
		try {

			for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
				if (ip.getChapThuanLenh().equals("Y") || ip.getChapThuanLenhDongThoi().equals("Y")) {
					if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getUserDesignation().size(), Contains.Straight2bank)) {
						if (ip.getEditStatus().equals("yes")) {
							guiMailStraight2Bank(ekycDoanhNghiep, ip, Contains.Straight2bank);

						}
					}
				}

			}
			for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
				System.out.println("gia trị : " + guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
						Contains.NGUOI_DAI_DIEN_PHAP_LUAT));
				if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
						Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {
					if (ip.getEditStatus().equals("yes")) {
						guiMailLegalRef(ekycDoanhNghiep, ip, Contains.NGUOI_DAI_DIEN_PHAP_LUAT);

					}
				}
			}

			for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
				if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(), Contains.KE_TOAN_TRUONG)) {
					if (ip.getEditStatus().equals("yes")) {
						guiMailKeToan(ekycDoanhNghiep, ip, Contains.KE_TOAN_TRUONG);

					}

				}
			}

			if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
					if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
							Contains.NGUOI_DUOC_UY_QUYEN)) {
						if (ip.getEditStatus().equals("yes")) {
							guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.NGUOI_DUOC_UY_QUYEN);
							guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.NGUOI_DUOC_UY_QUYEN, fullName,
									userName, emailGui);
						}
					}
				}

			}

			if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
					if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
							Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
						if (ip.getEditStatus().equals("yes")) {
							guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.UY_QUYEN_KE_TOAN_TRUONG);
							guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.UY_QUYEN_KE_TOAN_TRUONG, fullName,
									userName, emailGui);
						}
					}

				}
			}
			if (ekycDoanhNghiep.getListOfLeaders() != null) {
				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (guiMailEkycUpdate(ip, ekycDoanhNghiep.getLegalRepresentator().size(), Contains.BAN_LANH_DAO)) {
						if (ip.getEditStatus().equals("yes")) {
							guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.BAN_LANH_DAO);
							guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.BAN_LANH_DAO, fullName, userName,
									emailGui);

						}
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void guiMailEkyc(EkycDoanhNghiep ekycDoanhNghiep, HttpServletRequest req,
			EkycDoanhNghiepTable ekycDoanhNghiepTable) {
		String fullName = req.getSession().getAttribute("b_fullName").toString();
		String userName = req.getSession().getAttribute("b_username").toString();

		System.out.println("email lam ho: " + ekycDoanhNghiepTable.getEmailLogin());
		String emailGui = ekycDoanhNghiepTable.getEmailLogin();
		System.out.println("email lam ho:1111 ");
		boolean check = false;
		try {
			System.out.println("email lam ho:2222 ");
			if (ekycDoanhNghiep.getAllInOne().equals("yes")) {
				System.out.println("email lam ho:333 ");
				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getChapThuanLenh().equals("Y") || ip.getChapThuanLenhDongThoi().equals("Y")) {
						if (guiMailEkycAllInOne(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
								Contains.Straight2bank)) {
							System.err.println("1111111");
							guiMailStraight2Bank(ekycDoanhNghiep, ip, Contains.Straight2bank);
							check = true;

						}
					}

				}
				System.err.println("check: " + check);
				if (check == false) {
					for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
						System.out.println("email lam ho:55555 ");
						guiMailLegalRef(ekycDoanhNghiep, ip, Contains.NGUOI_DAI_DIEN_PHAP_LUAT);

					}

				}

			} else if (ekycDoanhNghiep.getAllInOne().equals("no")) {

				for (InfoPerson ip : ekycDoanhNghiep.getLegalRepresentator()) {
					if (guiMailEkyc(ip, ekycDoanhNghiep.getLegalRepresentator().size(),
							Contains.NGUOI_DAI_DIEN_PHAP_LUAT)) {

						guiMailLegalRef(ekycDoanhNghiep, ip, Contains.NGUOI_DAI_DIEN_PHAP_LUAT);
					}
				}

				for (InfoPerson ip : ekycDoanhNghiep.getChiefAccountant()) {
					if (guiMailEkyc(ip, ekycDoanhNghiep.getChiefAccountant().size(), Contains.KE_TOAN_TRUONG)) {

						guiMailKeToan(ekycDoanhNghiep, ip, Contains.KE_TOAN_TRUONG);
					}
				}
				for (InfoPerson ip : ekycDoanhNghiep.getListOfLeaders()) {
					if (guiMailEkyc(ip, ekycDoanhNghiep.getListOfLeaders().size(), Contains.BAN_LANH_DAO)) {

						guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.BAN_LANH_DAO);
						guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.BAN_LANH_DAO, fullName, userName,
								emailGui);
					}
				}
				if (ekycDoanhNghiep.getPersonAuthorizedChiefAccountant() != null) {
					for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedChiefAccountant()) {
						if (guiMailEkyc(ip, ekycDoanhNghiep.getPersonAuthorizedChiefAccountant().size(),
								Contains.UY_QUYEN_KE_TOAN_TRUONG)) {
							guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.UY_QUYEN_KE_TOAN_TRUONG);
							guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.UY_QUYEN_KE_TOAN_TRUONG, fullName,
									userName, emailGui);
						}
					}
				}

				if (ekycDoanhNghiep.getPersonAuthorizedAccountHolder() != null) {
					for (InfoPerson ip : ekycDoanhNghiep.getPersonAuthorizedAccountHolder()) {
						if (guiMailEkyc(ip, ekycDoanhNghiep.getPersonAuthorizedAccountHolder().size(),
								Contains.NGUOI_DUOC_UY_QUYEN)) {
							guiMailYeuCauXacThuc(ekycDoanhNghiep, ip, Contains.NGUOI_DUOC_UY_QUYEN);
							guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip, Contains.NGUOI_DUOC_UY_QUYEN, fullName,
									userName, emailGui);
						}
					}
				}
				for (InfoPerson ip : ekycDoanhNghiep.getUserDesignation()) {
					if (ip.getChapThuanLenh().equals("Y") || ip.getChapThuanLenhDongThoi().equals("Y")) {
						if (guiMailEkyc(ip, ekycDoanhNghiep.getLegalRepresentator().size(), Contains.Straight2bank)) {
							guiMailStraight2Bank(ekycDoanhNghiep, ip, Contains.Straight2bank);
							// guiMailYeuCauXacThucHo(ekycDoanhNghiep, ip ,Contains.Straight2bank ,
							// fullName,userName , emailGui);
						}
					}

				}

			}

		} catch (Exception e) {
		}
	}

	private void guiMailYeuCauXacThuc(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {
		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(),
					"Xác thực thông tin cá nhân cho việc đăng ký mở tài khoản Doanh nghiệp tại ngân\r\n"
							+ "hàng Standard Chartered Việt Nam ",
					"<div style='font-weight: bold;'>Kính gửi Quý Khách hàng,<div/><br/><br/>"
							+ "<div style='font-weight: normal;'>Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "sản phẩm của ngân hàng của chúng tôi.<br/><br/>"
							+ "Để tiến hành mở tài khoản Doanh nghiệp, Quý khách vui lòng cung cấp thông tin cá nhân theo hướng\r\n"
							+ "dẫn sau:<br/>" + "&emsp;-&ensp;Chuẩn bị hình chụp CMND/ CCCD/ hộ chiếu.<br/>"
							+ "&emsp;-&ensp;Truy cập vào đường link này: " + "<a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken() + "&tokenCheck="
							+ ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes())
							+ "'>Link eKYC</a>. <br/>"
							+ "&emsp;-&ensp;Tải lên hình CMND/ CCCD/ hộ chiếu và thực hiện các bước theo hướng dẫn.<br/><br/>"
							+ "Trong trường hợp Quý khách có nhu cầu cập nhật thông tin, Quý khách có thể nhấn vào <a href='"
							+ LINK_ADMIN + "/ekyc-enterprise/upload?token=" + ekycDoanhNghiep.getToken()
							+ "&tokenCheck=" + ip.getTokenCheck() + "&type="
							+ Base64.getEncoder().encodeToString(type.getBytes()) + "'>đây</a><br/><br/> "
							+ "Để được hỗ trợ thêm xin vui lòng liên hệ Chuyên viên Quan hệ Khách hàng <br/><br/> <div/>"
							+ "<div style='font-weight: bold;'>Trân trọng, <br/><br/><div/>"
							+ "<div style='font-weight: bold;'>Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)<div/> ");

	}

	private void guiMailKeToan(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {
		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(),
					"Xác thực thông tin cá nhân và ký điện tử cho việc đăng ký mở tài khoản Doanh\r\n"
							+ "nghiệp tại ngân hàng Standard Chartered Việt Nam",
					"<div style='font-weight: bold;'>Kính gửi Quý Khách hàng,<div/><br/><br/>"
							+ "<div style='font-weight: normal;'>Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "sản phẩm của ngân hàng của chúng tôi.<br/><br/>"
							+ "Để tiến hành mở tài khoản Doanh nghiệp, Quý khách vui lòng cung cấp thông tin cá nhân theo hướng dẫn sau: <br/>"
							+ "&emsp;&emsp;- Chuẩn bị:<br/>"
							+ "&emsp;&emsp;&emsp;&emsp;  o  Laptop/ điện thoại có camera  <br/> "
							+ "&emsp;&emsp;&emsp;&emsp;  o  Hình chụp CMND/CCCD/Hộ chiếu  <br/> "

							+ "&emsp;-&ensp;Truy cập vào đường link này: " + "<a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken() + "&tokenCheck="
							+ ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes())
							+ "'>Link eKYC</a>. <br/>"
							+ "&emsp;-&ensp;Tải lên hình CMND/ CCCD/ hộ chiếu và thực hiện các bước theo hướng dẫn.<br/><br/>"
							+ "Trong trường hợp Quý khách có nhu cầu cập nhật thông tin, Quý khách có thể nhấn vào <a href='"
							+ LINK_ADMIN + "/ekyc-enterprise/upload?token=" + ekycDoanhNghiep.getToken()
							+ "&tokenCheck=" + ip.getTokenCheck() + "&type="
							+ Base64.getEncoder().encodeToString(type.getBytes()) + "'>đây</a><br/><br/> "
							+ "Để được hỗ trợ thêm xin vui lòng liên hệ Chuyên viên Quan hệ Khách hàng <br/><br/> <div/>"
							+ "<div style='font-weight: bold;'>Trân trọng, <br/><br/><div/>"
							+ "<div style='font-weight: bold;'>Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)<div/> ");

	}

	private void guiMailYeuCauXacThucHo(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type, String fullName,
			String UserName, String emailGui) {
		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(emailGui,
					"" + type + " " + fullName + " của Công ty " + UserName + " đã nhận được email để thực hiện eKYC.",
					"Bạn có thể thực hiện hộ khách hàng tại " + "<a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken() + "&tokenCheck="
							+ ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes())
							+ "'> đây</a>. <br/>");

	}

	private void guiMailLegalRef(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {

		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(),
					"Xác thực thông tin cá nhân và ký điện tử cho việc đăng ký mở tài khoản Doanh\r\n"
							+ "nghiệp tại ngân hàng Standard Chartered Việt Nam",
					"<div style='font-weight: bold;'>Kính gửi Quý Khách hàng,<div/><br/><br/>"
							+ "<div style='font-weight: normal;'>Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "sản phẩm của ngân hàng của chúng tôi.<br/><br/>"
							+ "Để tiến hành mở tài khoản Doanh nghiệp, Quý khách vui lòng cung cấp thông tin cá nhân theo hướng\r\n"
							+ "dẫn sau:<br/>" + "&emsp;&emsp;- Chuẩn bị:<br/>"
							+ "&emsp;&emsp;&emsp;&emsp;  o  Laptop/ điện thoại có camera  <br/> "
							+ "&emsp;&emsp;&emsp;&emsp;  o  Hình chụp CMND/CCCD/Hộ chiếu  <br/> "

							+ "&emsp;-&ensp;Truy cập vào đường link này: " + "<a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken() + "&tokenCheck="
							+ ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes())
							+ "'>Link eKYC</a>. <br/>"
							+ "&emsp;-&ensp;Tải lên hình CMND/ CCCD/ hộ chiếu và thực hiện các bước theo hướng dẫn.<br/><br/>"
							+ "&emsp;-&ensp;Xem lại bộ hồ sơ đăng ký và tiến hành ký điện tử<br/><br/>"
							+ "Trong trường hợp Quý khách có nhu cầu cập nhật thông tin, Quý khách có thể nhấn vào <a href='"
							+ LINK_ADMIN + "/ekyc-enterprise/upload?token=" + ekycDoanhNghiep.getToken()
							+ "&tokenCheck=" + ip.getTokenCheck() + "&type="
							+ Base64.getEncoder().encodeToString(type.getBytes()) + "'>đây</a><br/><br/> "
							+ "Để được hỗ trợ thêm xin vui lòng liên hệ Chuyên viên Quan hệ Khách hàng <br/><br/> <div/>"
							+ "<div style='font-weight: bold;'>Trân trọng, <br/><br/><div/>"
							+ "<div style='font-weight: bold;'>Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)<div/> ");
		System.err.println("77777");

	}

	private void guiMailStraight2Bank(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {
		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(),
					"Xác thực thông tin cá nhân cho việc đăng ký người dùng ngân hàng điện tử\r\n" + "Straight2Bank",
					"<div style='font-weight: bold;'>Kính gửi Quý Khách hàng,<div/><br/><br/>"
							+ "<div style='font-weight: normal;'>Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "sản phẩm của ngân hàng của chúng tôi. <br/><br/>"
							+ "Để tiến hành mở tài khoản Doanh nghiệp, Quý khách vui lòng làm theo hướng dẫn sau:<br/>"
							+ "&emsp;-&ensp;Chuẩn bị hình chụp CMND/ CCCD/ hộ chiếu. <br/>"
							+ "&emsp;-&ensp;Truy cập vào đường link này: <a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken() + "&tokenCheck="
							+ ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes())
							+ "'>Link eKYC.</a>, <br/>" + "&emsp;-&ensp;Tải lên hình CMND/ CCCD/ hộ chiếu.<br/><br/>"
							+ "Trong trường hợp Quý khách có nhu cầu cập nhật thông tin, Quý khách có thể nhấn vào <a href='"
							+ LINK_ADMIN + "/ekyc-enterprise/upload?token=" + ekycDoanhNghiep.getToken()
							+ "&tokenCheck=" + ip.getTokenCheck() + "&type="
							+ Base64.getEncoder().encodeToString(type.getBytes()) + "'>đây</a> <br/><br/>"
							+ "Để được hỗ trợ thêm xin vui lòng liên hệ Chuyên viên Quan hệ Khách hàng <br/><br/> <div/>"
							+ "<div style='font-weight: bold;'>Trân trọng, <br/><br/><div/>"
							+ "<div style='font-weight: bold;'>Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)<div/> ");

	}

	private boolean guiMailEkyc(InfoPerson ip, int size, String loai) {
		if (loai.equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT) && ip.getCheckMain().equals("yes"))
			return false;

		return true;
	}

	private boolean guiMailEkycAllInOne(InfoPerson ip, int size, String loai) {
		if (loai.equals(Contains.ALL_IN_ONE) && ip.getCheckMain().equals("yes")) {
			return false;
		}

		return true;
	}

	private boolean guiMailEkycUpdate(InfoPerson ip, int size, String loai) {
		if (ip.getEditStatus().equals("yes") && loai.equals(Contains.NGUOI_DAI_DIEN_PHAP_LUAT)
				&& ip.getCheckMain().equals("yes")) {
			return false;
		}

		return true;
	}

	public void guiMailHoanThanhEkyc(InfoPerson ip) {
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(), "Quý khách đã hoàn thành thông tin eKYC",
					"Quý khách đã hoàn thành phần eKYC của mình.");

	}

	private void guiMailYeuCauChinhSua(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip, String type) {

		Timestamp a;
		LOGGER.info("Send mail: " + LINK_ADMIN + "/ekyc-enterprise/ekyc?token=" + ekycDoanhNghiep.getToken()
				+ "&tokenCheck=" + ip.getTokenCheck() + "&type=" + Base64.getEncoder().encodeToString(type.getBytes()));
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(), "Email yêu cầu ký số đăng ký mở tài khoản",
					"Vui lòng click vào <a href='" + LINK_ADMIN + "/ekyc-enterprise/esign?token="
							+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck()
							+ "'>Bắt đầu ký số</a>, để thực hiện ký số");

	}

	private void guiMailBoSungThongTin(EkycDoanhNghiep ekycDoanhNghiep, String emailGui) {
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(emailGui, "Bổ sung thông tin",
					"Khi cần bổ sung chứng từ, quý khách có thể nhấn vào <a href='" + LINK_ADMIN
							+ "/ekyc-enterprise/update-file?token=" + ekycDoanhNghiep.getToken() + "'>đây</a>");
	}

	private void guiMailYeuKyAOF(EkycDoanhNghiep ekycDoanhNghiep, InfoPerson ip) {
		if (!MOI_TRUONG.equals("dev"))
			email.sendText(ip.getEmail(), "Email yêu cầu ký số đăng ký mở tài khoản",
					"Vui lòng click vào <a href='" + LINK_ADMIN + "/ekyc-enterprise/esign?token="
							+ ekycDoanhNghiep.getToken() + "&tokenCheck=" + ip.getTokenCheck()
							+ "'>Bắt đầu ký số</a>, để thực hiện ký số");

	}

	@PostMapping(value = "/ekyc-enterprise/liveness", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String liveness(HttpServletRequest req, @RequestBody String data) {
		JSONObject jsonObject = new JSONObject(data);

		if (jsonObject.has("anhVideo")) {
			String listImage = jsonObject.getString("anhVideo");
			String[] arr = listImage.split(",");
			JSONArray jsonArray = new JSONArray();
			String anhCaNhan = "";
			for (int i = 0; i < arr.length; i++) {
				jsonArray.put(i, new JSONObject().put("anh", arr[i]).put("thoiGian", (i + 1)));
				if (StringUtils.isEmpty(anhCaNhan)) {
					anhCaNhan = arr[i];
				}
			}
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("anhMatTruoc", jsonObject.getString("anhMatTruoc"));
			jsonObject2.put("anhVideo", jsonArray);

			String respone = postRequest(jsonObject2.toString(), "/public/all/xac-thuc-khuon-mat");
			JSONObject object = new JSONObject(respone);

			return object.toString();
		}

		return "{'status': 400, 'message':'Không có ảnh video'}";
	}

	private String luuAnh(String base64Img, FileHandling fileHandling, String nameFile) {
		if (!StringUtils.isEmpty(base64Img)) {
			try {
				String path = fileHandling.save(base64Img, configProperties.getConfig().getImage_folder_log() + "web/",
						nameFile);
				return path;
			} catch (Exception e) {
			}
		}
		return "";
	}

	private String sendRequest(MultipartFile file, String codeTemplate, String url) {
		try {
			RequestConfig.Builder requestConfig = RequestConfig.custom();
			CloseableHttpClient httpClient = HttpClients.createDefault();

			HttpPost uploadFile = new HttpPost(API_SERVICE + url);
			uploadFile.setConfig(requestConfig.build());
			uploadFile.addHeader("token", token);
			uploadFile.addHeader("code", code);
//			uploadFile.addHeader("content-type", file.getContentType());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("code", codeTemplate, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addBinaryBody("file", file.getBytes(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());

			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());

			return text;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setParams(ParamsKbank params, HttpServletRequest req) {
		req.getSession().setAttribute("params", params);
	}

	public ParamsKbank getParams(HttpServletRequest req) {
		return (ParamsKbank) req.getSession().getAttribute("params");
	}

	@PostMapping(value = "/ekyc-enterprise/ky-so", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String kySoEkyc(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		JSONObject jsonResp = new JSONObject();
		try {
			String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());

			JSONObject jsonObjectPr = new JSONObject(text);
			forwartParams(allParams, model);
			String HTML = jsonObjectPr.getString("contentPdf");
			String nameFile = UUID.randomUUID().toString() + ".pdf";
			String pathPdf = KY_SO_FOLDER + "/" + nameFile;
			String agreementUUID = UUID.randomUUID().toString();

			ParamsKbank params = new ParamsKbank();
			FormInfo formInfo = new FormInfo();
			formInfo.setHoVaTen(jsonObjectPr.getString("hoVaTen"));
			formInfo.setSoCmt(jsonObjectPr.getString("soCmt"));
			formInfo.setDiaChi("Hà Nội");
			formInfo.setThanhPho("Hà Nội");
			formInfo.setQuocGia("Việt Nam");
			params.setSoDienThoai(jsonObjectPr.getString("soDienThoai"));
			params.setFormInfo(formInfo);
			params.setAnhMatTruoc(jsonObjectPr.getString("anhMatTruoc"));
			params.setAnhMatSau(jsonObjectPr.getString("anhMatSau"));

			if (StringUtils.isEmpty(jsonObjectPr.getString("file"))) {
				ParamsKbank params2 = getParams(req);
				if (params2 == null)
					return "redirect:" + "/ekyc-enterprise/ekyc";

				EkycDoanhNghiepTable doanhNghiep = ekycDoanhNghiepRepository.findByToken(params2.getToken());
				if (doanhNghiep == null)
					return "demo/doanhnghiep2/step/steperror";
				EkycDoanhNghiep ekycDoanhNghiep = new Gson().fromJson(doanhNghiep.getNoiDung(), EkycDoanhNghiep.class);

				pdfHandling.nhapThongTinForm(pathPdf, ekycDoanhNghiep, PATH_PDF_FILL_FORM);
			} else {
				System.out.println(KY_SO_FOLDER);
				byte[] decodedImg = Base64.getDecoder()
						.decode(jsonObjectPr.getString("file").getBytes(StandardCharsets.UTF_8));

				Path destinationFile = Paths.get(KY_SO_FOLDER, nameFile);
				Files.write(destinationFile, decodedImg);
			}

			System.out.println("PDF Created!");

			String jsonRegister = guiThongTinDangKyKySo(params, agreementUUID);
			ObjectMapper objectMapper = new ObjectMapper();
			SignCloudResp signCloudRespRegister = objectMapper.readValue(jsonRegister, SignCloudResp.class);

			if (signCloudRespRegister.getResponseCode() != 0) {
				jsonResp.put("message", "Không đăng ký được chữ ký ");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			}
			String page = "23";
			String textPage = "Đại diện cho Công ty";

			if (jsonObjectPr.has("page")) {
				page = jsonObjectPr.getString("page");
			}
			if (jsonObjectPr.has("textPage")) {
				textPage = jsonObjectPr.getString("textPage");
			}

			String jsonResponse = guiThongTinKySo(req, pathPdf, nameFile, agreementUUID, page, textPage);

			SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

			if (signCloudResp.getResponseCode() != 1007) {
				jsonResp.put("message", "Không gửi được chữ ký ");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			}

			if (!MOI_TRUONG.equals("dev")) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("dienThoai", params.getSoDienThoai());
				postRequest(jsonObject.toString(),
						"/public/gui-ma-otp-ky-so?code=" + signCloudResp.getAuthorizeCredential());
			}

			jsonResp.put("otp", signCloudResp.getAuthorizeCredential());
			jsonResp.put("maKy", signCloudResp.getBillCode());

//			postRequest(jsonObject.toString(), "/public/gui-ma-otp-ky-so?code=1234");
//			
//			jsonResp.put("otp", "1234");
//			jsonResp.put("maKy", "123456789");
			jsonResp.put("pathPdf", pathPdf);
			jsonResp.put("nameFile", nameFile);
			jsonResp.put("agreementUUID", agreementUUID);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResp.put("message", "Lỗi hệ thống");
			jsonResp.put("status", 400);

			return jsonResp.toString();
		}

		jsonResp.put("status", 200);

		return jsonResp.toString();
	}

	@PostMapping(value = "/ekyc-enterprise/ky-so-otp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String kySoOtp(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		JSONObject jsonResp = new JSONObject();
		try {
			String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
			JSONObject jsonObject = new JSONObject(text);

			eSignCall service = new eSignCall();
			String jsonResponse = service.authorizeSingletonSigningForSignCloud(jsonObject.getString("agreementUUID"),
					jsonObject.getString("otp"), jsonObject.getString("maKy"));
			ObjectMapper objectMapper = new ObjectMapper();
			SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);
			if (signCloudResp.getResponseCode() == 0 && signCloudResp.getSignedFileData() != null) {
				String str = jsonObject.getString("pathPdf");
				String str1 = str.split("\\/")[str.split("\\/").length - 1];
				File file2 = new File(str.replaceAll(str1, ".signed." + str1));
				String base64Img2 = CommonUtils.encodeFileToBase64Binary(file2);

				jsonResp.put("file", base64Img2);
				jsonResp.put("status", 200);

				return jsonResp.toString();
			} else if (signCloudResp.getResponseCode() == 1004) {
				jsonResp.put("message", "Lỗi OTP");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			} else {
				jsonResp.put("message", "Ký số thất bại");
				jsonResp.put("status", 400);

				return jsonResp.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResp.put("message", "Lỗi hệ thống");
			jsonResp.put("status", 400);

			return jsonResp.toString();
		}
	}

	private String guiThongTinDangKyKySo(ParamsKbank params, String agreementUUID) throws Exception {
		FormInfo formInfo = params.getFormInfo();

		eSignCall service = new eSignCall();
		byte[] frontSideOfIDDocument = Base64.getDecoder().decode(params.getAnhMatTruoc());
		byte[] backSideOfIDDocument = Base64.getDecoder().decode(params.getAnhMatSau());
		String json = service.prepareCertificateForSignCloud(agreementUUID, formInfo.getHoVaTen(), formInfo.getSoCmt(),
				formInfo.getSoCmt(), formInfo.getDiaChi(), formInfo.getThanhPho(), formInfo.getQuocGia(),
				frontSideOfIDDocument, backSideOfIDDocument, formInfo.getEmail(), params.getSoDienThoai());
		return json;
	}

	private String guiThongTinKySo(HttpServletRequest req, String pathPdf, String nameFile, String agreementUUID,
			String page, String textSign) throws Exception {
		byte[] fileData01;
		String mimeType01;
		SignCloudMetaData signCloudMetaDataForItem01;
		HashMap<String, String> singletonSigningForItem01;

		fileData01 = IOUtils.toByteArray(new FileInputStream(pathPdf));
		mimeType01 = ESignCloudConstant.MIMETYPE_PDF;

		signCloudMetaDataForItem01 = new SignCloudMetaData();
		// -- SingletonSigning (Signature properties for customer)
		singletonSigningForItem01 = new HashMap<>();
		singletonSigningForItem01.put("COUNTERSIGNENABLED", "False");
		singletonSigningForItem01.put("PAGENO", page);
		singletonSigningForItem01.put("POSITIONIDENTIFIER", textSign);
		singletonSigningForItem01.put("RECTANGLEOFFSET", "0,-60");
		singletonSigningForItem01.put("RECTANGLESIZE", "200,50");
		singletonSigningForItem01.put("VISIBLESIGNATURE", "True");
		singletonSigningForItem01.put("VISUALSTATUS", "False");
		singletonSigningForItem01.put("IMAGEANDTEXT", "False");
		singletonSigningForItem01.put("TEXTDIRECTION", "LEFTTORIGHT");
		singletonSigningForItem01.put("SHOWSIGNERINFO", "True");
		singletonSigningForItem01.put("SIGNERINFOPREFIX", "Được ký bởi:");
		singletonSigningForItem01.put("SHOWDATETIME", "True");
		singletonSigningForItem01.put("DATETIMEPREFIX", "Ngày ký:");
		singletonSigningForItem01.put("SHOWREASON", "True");
		singletonSigningForItem01.put("SIGNREASONPREFIX", "Lý do:");
		singletonSigningForItem01.put("SIGNREASON", "Tôi đồng ý");
		singletonSigningForItem01.put("SHOWLOCATION", "True");
//        singletonSigningForItem01.put("LOCATION", "Hà Nội");
//        singletonSigningForItem01.put("LOCATIONPREFIX", "Nơi ký:");
		singletonSigningForItem01.put("TEXTCOLOR", "black");
		eSignCall service = new eSignCall();
		signCloudMetaDataForItem01.setSingletonSigning(singletonSigningForItem01);

		String jsonResponse = service.prepareFileForSignCloud(agreementUUID,
				// ESignCloudConstant.AUTHORISATION_METHOD_SMS,
				ESignCloudConstant.AUTHORISATION_METHOD_EMAIL, null, notificationTemplate, notificationSubject,
				fileData01, nameFile, mimeType01, signCloudMetaDataForItem01);

		return jsonResponse;

	}

	@RequestMapping(path = "/ekyc-enterprise/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(String param) throws IOException {
		File file = new File("/image/scanIdCard-0.0.1-SNAPSHOT.jar");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=craw.jar");
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	public void saveEkycDoanhNghiepTableHistory(EkycDoanhNghiepTable ekycDoanhNghiepTable) {
		EkycDoanhNghiepTableHistory tableHistory = new EkycDoanhNghiepTableHistory();

		tableHistory.setId_dn(ekycDoanhNghiepTable.getId());
		tableHistory.setEmailNguoiLienHe(ekycDoanhNghiepTable.getEmailNguoiLienHe());
		tableHistory.setMaDoanhNghiep(ekycDoanhNghiepTable.getMaDoanhNghiep());
		tableHistory.setNgayTao(ekycDoanhNghiepTable.getNgayTao());
		tableHistory.setNoiDung(ekycDoanhNghiepTable.getNoiDung());
		tableHistory.setSoDienThoaiNguoiLienHe(ekycDoanhNghiepTable.getSoDienThoaiNguoiLienHe());
		tableHistory.setStatus(ekycDoanhNghiepTable.getStatus());
		tableHistory.setTenDoanhNghiep(ekycDoanhNghiepTable.getTenDoanhNghiep());
		tableHistory.setTenNguoiLienHe(ekycDoanhNghiepTable.getTenNguoiLienHe());
		tableHistory.setTenNguoiQuanLy(ekycDoanhNghiepTable.getTenNguoiQuanLy());
		tableHistory.setToken(ekycDoanhNghiepTable.getToken());
		tableHistory.setStep(ekycDoanhNghiepTable.getStep());
		tableHistory.setUsername(ekycDoanhNghiepTable.getUsername());
		tableHistory.setCheckNoiDung(ekycDoanhNghiepTable.getCheckNoiDung());
		tableHistory.setUserNameLogin(ekycDoanhNghiepTable.getEmailLogin());
		ekycDoanhNghiepHistoryRepository.save(tableHistory);

	}

}
