package fis.com.vn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.BusinessRepository;
import fis.com.vn.repository.EkycDoanhNghiepRepository;
import fis.com.vn.repository.UserModuleRepository;
import fis.com.vn.table.Business;
import fis.com.vn.table.EkycDoanhNghiepTable;

@Controller
public class DangNhapDoanhNghiepController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DangNhapDoanhNghiepController.class);

	@Autowired
	BusinessRepository businessRepository;
	@Autowired
	UserModuleRepository userModuleRepository;
	@Autowired
	EkycDoanhNghiepRepository ekycDoanhNghiepRepository;

	@GetMapping(value = "/login-doanh-nghiep")
	public String get(Model model) {
		return "loginbusiness";
	}

	@PostMapping(value = "/login-doanh-nghiep")
	public String get(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		try {

			LOGGER.info("loginbusiness: " + getStringParams(allParams, "username"));
			Business business = businessRepository.findByusername(getStringParams(allParams, "username"));
			LOGGER.info("business: " + business.toString());
			if (business == null)
				throw new ErrorException(" Username is not correct/ Tên người dùng không chính xác");
			if (!business.getPassword().equals(CommonUtils.getMD5(getStringParams(allParams, "password"))))
				throw new ErrorException("Password is not correct/ Mật khẩu không chính xác");
			if (business.getStatus() == null || (business.getStatus() != null && business.getStatus() != 1))
				throw new ErrorException("The account has not activated/ Tài khoản chưa được kích hoạt");

			if (business.getStatusPassword().equals(Contains.MAT_KHAU_LAN_DAU_TIEN)) {
				business = businessRepository.findByusername(getStringParams(allParams, "username"));

				req.getSession().setAttribute("usernameChange", getStringParams(allParams, "username"));
				if (business != null) {
					model.addAttribute("business", business);
					return "redirect:/doanh-nghiep/change-pass";
				}

			}
			req.getSession().setAttribute("usernameChange", getStringParams(allParams, "username"));
			req.getSession().setAttribute("b_username", getStringParams(allParams, "username"));
			req.getSession().setAttribute("b_fullName", business.getFullName());
			req.getSession().setAttribute("b_email", business.getEmail());
			req.getSession().setAttribute("b_userid", business.getId());
			req.getSession().setAttribute("b_token", business.getToken());
			System.out.println("username :" + business.getUsername());

			EkycDoanhNghiepTable doanhNghiepTable = ekycDoanhNghiepRepository
					.findByUsername(business.getUsername().toString());
			if (doanhNghiepTable == null || Integer.parseInt(doanhNghiepTable.getStep()) == 1
					|| Integer.parseInt(doanhNghiepTable.getStep()) == 2) {
				return "redirect:/ekyc-enterprise";
			} else if (doanhNghiepTable != null && Integer.parseInt(doanhNghiepTable.getStep()) >= 3) {
				return "redirect:/ekyc-enterprise/update-ekyc-business";
			}

		} catch (ErrorException e) {
			model.addAttribute("message", e.getMessage());
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			LOGGER.error("ErrorException", e);
		}
		return "loginbusiness";

	}

	@GetMapping(value = "/doanh-nghiep/change-pass")
	public String getBusiness(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		// model.addAttribute("thanhPhos", layTinhThanhPho());
		try {

			Business business = businessRepository
					.findByUsername(req.getSession().getAttribute("usernameChange").toString());
			if (business == null) {
				throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
			}
			model.addAttribute("business", business);
			model.addAttribute("name", language.getMessage("sua"));
			forwartParams(allParams, model);
			return "quanlydoanhnghiep/doimatkhaudoanhnghiep";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/login-doanh-nghiep?" + getParamsQuery(allParams);
		}
	}

	@PostMapping(value = "/doanh-nghiep/change-pass")
	public String updateBusiness(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) {

		int check = 0;
		try {

			Business businessDb = businessRepository.findById(Long.valueOf(allParams.get("id"))).get();
			EkycDoanhNghiepTable ekycDoanhNghiepTable = ekycDoanhNghiepRepository
					.findByUsername(businessDb.getUsername());
			System.out.println("usser name222222 :" + ekycDoanhNghiepTable.toString());
			System.out.println("usser name 1111:" + allParams.get("old-password"));
			System.out.println("usser name 3333:" + allParams.get("new-password"));
			System.out.println("usser name 444:" + allParams.get("re-password"));

			if (!allParams.get("new-password").equals(allParams.get("re-password"))) {
				redirectAttributes.addFlashAttribute("error",
						"The New and Confirm passwords must match.Please re-type them");

				return "redirect:/doanh-nghiep/change-pass";
			} else if (!CommonUtils.getMD5(allParams.get("old-password")).equals(businessDb.getPassword())) {
				redirectAttributes.addFlashAttribute("error", "Password is not correct/ Mật khẩu không chính xác");

				return "redirect:/doanh-nghiep/change-pass";
			} else if (!isValidPasswordCharacters(allParams.get("new-password"))
					&& !isValidPasswordCharacters(allParams.get("re-password"))) {
				redirectAttributes.addFlashAttribute("error", language.getMessage("loi_password"));
				return "redirect:/doanh-nghiep/change-pass" + getParamsQuery(allParams);
			} else {
				businessDb.setPassword(CommonUtils.getMD5(allParams.get("new-password")));

				businessDb.setStatusPassword(Contains.MAT_KHAU_LAN_THU_2);
				businessRepository.save(businessDb);
				ekycDoanhNghiepTable.setUsername(businessDb.getUsername());
				ekycDoanhNghiepRepository.save(ekycDoanhNghiepTable);

				check = 1;

			}

		} catch (Exception e) {
			model.addAttribute("error", language.getMessage("loi_he_thong"));

			e.printStackTrace();
		}
		// return null;
		if (check == 1) {
			redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));
			return "redirect:/login-doanh-nghiep?" + getParamsQuery(allParams);
		} else {
			return "redirect:/login-doanh-nghiep?" + getParamsQuery(allParams);
		}

	}
}
