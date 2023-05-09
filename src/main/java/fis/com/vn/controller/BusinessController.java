package fis.com.vn.controller;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Email;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.BusinessRepository;
import fis.com.vn.repository.EkycDoanhNghiepRepository;
import fis.com.vn.repository.UserGroupRepository;
import fis.com.vn.repository.UserInfoRepository;
import fis.com.vn.table.Business;
import fis.com.vn.table.EkycDoanhNghiepTable;
import fis.com.vn.table.UserGroup;
import fis.com.vn.table.UserInfo;

@Controller
public class BusinessController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EkycDoanhNghiepAdminController.class);

	@Value("${LINK_ADMIN}")
	protected String LINK_ADMIN;

	@Value("${MOI_TRUONG}")
	protected String MOI_TRUONG;

	@Autowired
	BusinessRepository businessRepository;
	@Autowired
	UserGroupRepository userGroupRepository;
	@Autowired
	EkycDoanhNghiepRepository ekycDoanhNghiepRepository;
	@Autowired
	Email email;
	@Autowired
	UserInfoRepository userInfoRepository;

	@GetMapping(value = "/doanh-nghiep")
	public String nguoiDung(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		handlingGet(allParams, model, req);
		forwartParams(allParams, model);
		return "quanlydoanhnghiep/doanhnghiep";
	}

	private void handlingGet(Map<String, String> allParams, Model model, HttpServletRequest req) {
		Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));
		// clear all param if reset
		if (allParams.get("reset") != null) {
			allParams.clear();
		}

		Page<Business> business = businessRepository.selectParams(getStringParams(allParams, "b_uname"),
				getStringParams(allParams, "b_fname"), getStringParams(allParams, "b_email"),
				getIntParams(allParams, "b_status"), getPageable(allParams, paginate));

		model.addAttribute("currentPage", paginate.getPage());
		model.addAttribute("totalPage", business.getTotalPages());
		model.addAttribute("totalElement", business.getTotalElements());
		model.addAttribute("business", business.getContent());
	}

	@GetMapping(value = "/doanh-nghiep/them-moi")
	public String getguoiDungThemMoi(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams)
			throws JsonProcessingException {
		Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.TT_NHOM_HOATDONG);
		// model.addAttribute("thanhPhos", layTinhThanhPho());
		model.addAttribute("userGroups", userGroups);
		model.addAttribute("business", new Business());
		model.addAttribute("name", language.getMessage("them_moi"));
		forwartParams(allParams, model);
		return "quanlydoanhnghiep/adddoanhnghiep";
	}

	@PostMapping(value = "/doanh-nghiep/them-moi")
	public String addBusiness(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam Map<String, String> allParams, @ModelAttribute("business") Business business) {

		try {
			checkErrorMessage(business);

			Business checkUserName = businessRepository.findByUsername(business.getUsername());
			if (checkUserName != null)
				throw new ErrorException(language.getMessage("ten_dang_nhap_da_ton_tai"));
			req.getSession().setAttribute("pass", business.getPassword());

			if (business.getUsername().equals(business.getPassword())) {

				redirectAttributes.addFlashAttribute("error",
						"Password must not be the same as user account name/ Mật khẩu không được phép trùng với tên tài khoản người dùng");
			} else if (!isValidPasswordCharacters(business.getPassword())) {

				redirectAttributes.addFlashAttribute("error", language.getMessage("loi_password"));
			} else {
				System.out.println("email user: " + req.getSession().getAttribute("emailUser").toString());
				UserInfo userInfo = userInfoRepository
						.findByUsername(req.getSession().getAttribute("username").toString());
				business.setEmailWebAdmin(userInfo.getEmail());
				guiMailDangNhap(business, business.getPassword());

				business.setCreateTime(new Date());
				business.setPassword(CommonUtils.getMD5(business.getPassword()));
				business.setToken(UUID.randomUUID().toString());
				business.setStatusPassword(Contains.MAT_KHAU_LAN_DAU_TIEN);

				businessRepository.save(business);

				EkycDoanhNghiepTable userameDoanhNghiep = ekycDoanhNghiepRepository
						.findByUsername(business.getUsername());

				System.out.println("user: " + business.getUsername());
				System.out.println("user: " + userameDoanhNghiep);
				if (userameDoanhNghiep == null) {
					System.out.println("email user:4 ");
					EkycDoanhNghiepTable doanhNghiepTable = new EkycDoanhNghiepTable();
					doanhNghiepTable.setUsername(business.getUsername());
					doanhNghiepTable.setEmailLogin(req.getSession().getAttribute("emailUser").toString());
					doanhNghiepTable.setStep("1");
					doanhNghiepTable.setStatusDonKy(Contains.TRANG_THAI_KY_THAT_BAI);
					doanhNghiepTable.setNgayTao(new Date());
					ekycDoanhNghiepRepository.save(doanhNghiepTable);
					System.out.println("email user:5 ");
				}

				redirectAttributes.addFlashAttribute("success", "Add success/ Thêm thành công");
			}

		} catch (ErrorException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "System error/ Lỗi hệ thống ");
		}

		return "redirect:/doanh-nghiep?" + getParamsQuery(allParams);
	}

	@RequestMapping(value = "/doanh-nghiep/xoa", method = { RequestMethod.GET })
	public String delete(Model model, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes, HttpServletRequest req) {
		if (!StringUtils.isEmpty(allParams.get("id"))) {
			Business checkDN = businessRepository.findById(Long.valueOf(allParams.get("id"))).get();
			if (checkDN != null && !checkDN.getUsername().equals("supper_admin")) {
				businessRepository.delete(checkDN);
			}
			redirectAttributes.addFlashAttribute("success", language.getMessage("xoa_thanh_cong"));
		} else {
			redirectAttributes.addFlashAttribute("error", language.getMessage("xoa_that_bai"));
		}

		return "redirect:/doanh-nghiep?" + getParamsQuery(allParams);
	}

	@GetMapping(value = "/doanh-nghiep/sua")
	public String getnguoiDungSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		// model.addAttribute("thanhPhos", layTinhThanhPho());
		try {
			if (StringUtils.isEmpty(allParams.get("id"))) {
				throw new Exception(language.getMessage("sua_that_bai"));
			}
			Business business = businessRepository.findById(Long.valueOf(allParams.get("id"))).get();
			if (business == null) {
				throw new Exception("Record does not exists/ Không tồn tại bản ghi");
			}

			Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.TT_NHOM_HOATDONG);

			model.addAttribute("userGroups", userGroups);
			model.addAttribute("business", business);
			model.addAttribute("name", language.getMessage("sua"));
			forwartParams(allParams, model);
			return "quanlydoanhnghiep/adddoanhnghiep";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/doanh-nghiep?" + getParamsQuery(allParams);
		}
	}

	@PostMapping(value = "/doanh-nghiep/sua")
	public String editBusiness(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam Map<String, String> allParams, @ModelAttribute("Business") Business business) {
		// model.addAttribute("thanhPhos", layTinhThanhPho());
		try {
			checkErrorMessage(business);

			Business businessDb = businessRepository.findById(Long.valueOf(allParams.get("id"))).get();
			EkycDoanhNghiepTable ekycDoanhNghiepTable = ekycDoanhNghiepRepository
					.findByUsername(businessDb.getUsername());
			guiMailDangNhap(business, business.getPassword());
			if (!StringUtils.isEmpty(business.getPassword())) {
				business.setPassword(CommonUtils.getMD5(business.getPassword()));
			} else {
				business.setPassword(businessDb.getPassword());
			}

			updateObjectToObject(businessDb, business);

			businessRepository.save(businessDb);

			if (ekycDoanhNghiepTable != null) {
				ekycDoanhNghiepTable.setUsername(businessDb.getUsername());
				ekycDoanhNghiepRepository.save(ekycDoanhNghiepTable);
			}

			redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));
		} catch (ErrorException e) {
			e.printStackTrace();
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "System error/ Lỗi hệ thống ");
			e.printStackTrace();
		}
		return "redirect:/doanh-nghiep?" + getParamsQuery(allParams);
	}

	private void guiMailDangNhap(Business business, String passwork) {
		LOGGER.info("Send mail: Quý khách có thể truy cập vào đường link:" + LINK_ADMIN + "/login-doanh-nghiep"
				+ " Cùng với Tên đăng nhập: " + business.getUsername() + ":Mật khẩu: " + passwork);

		if (!MOI_TRUONG.equals("dev"))
			email.sendText(business.getEmail(), "Hướng dẫn cách đăng ký mở tài khoản doanh nghiệp",

					"<div style='font-weight: bold;'>Kính gửi Quý Khách hàng,<div/><br/><br/>"
							+ "<div style='font-weight: normal;'>Ngân hàng Standard Chartered Việt Nam chân thành cảm ơn Quý Khách hàng đã quan tâm đến các\r\n"
							+ "các sản phẩm của ngân hàng của chúng tôi. <br/><br/>"
							+ "Để đăng ký mở tài khoản Doanh nghiệp, Quý khách vui lòng làm theo hướng dẫn sau:<br/>"
							+ "&emsp;-  &ensp;Truy cập vào đường link này: <a href='" + LINK_ADMIN
							+ "/login-doanh-nghiep" + "'>Link đăng ký mở tài khoản doanh nghiệp</a>, . <br/>"
							+ "&emsp;-  &ensp;Điền Tên đăng nhập : " + business.getUsername() + "<br/>"
							+ "&emsp;-  &ensp;Điền Mật khẩu: " + passwork + " ban đầu để tiến hành việc đăng ký.<br/>"
							+ "&emsp;-  &ensp;Đổi mật khẩu và tiến hành các bước như hướng dẫn <br/>"
							+ "Để được hỗ trợ thêm xin vui lòng liên hệ Chuyên viên Quan hệ Khách hàng <br/><br/> <div/>"
							+ "<div style='font-weight: bold;'>Trân trọng, <br/><br/><div/>"
							+ "<div style='font-weight: bold;'>Ngân Hàng TNHH MTV Standard Chartered (Việt Nam)<div/> ");

	}

}
