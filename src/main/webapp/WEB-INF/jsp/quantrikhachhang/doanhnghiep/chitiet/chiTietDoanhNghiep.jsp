<%@ page contentType="text/html; charset=UTF-8"%>
<div class="row clearfix">
	<div>
		<div class="panel-body">
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Full Legal Name * <br /> <i
						style="font-weight: normal;">Tên Pháp Lý Đầy Đủ *</i></label> <input
						type="text" class="form-control input-sm"
						id="nameOfTheAccountHolder"
						value="<c:out value='${ekycDoanhNghiep.nameOfTheAccountHolder}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<%-- <div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Number <br /> <i
						style="font-weight: normal;">Số</i></label> <input type="text"
						class="form-control input-sm" id="number" name="number"
						value="<c:out value='${ekycDoanhNghiep.number}'/>" />
				</div>
			</div> --%>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Name * <br /> <i
						style="font-weight: normal;">Tên *</i></label> <input type="text"
						class="form-control input-sm" id="nameCompany" name="nameCompany"
						value="<c:out value='${ekycDoanhNghiep.nameCompany}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Date (Account Opening
						Application Form) <br /> <i style="font-weight: normal;">Ngày (Mẫu Đơn Đăng Ký Mở Tài Khoản)</i>
					</label> <input type="text" class="form-control input-sm datepicker"
						id="dateAccountOpening"
						value="<c:out value='${ekycDoanhNghiep.dateAccountOpening}'/>" />
				</div>
			</div>
			
		</div>
		<div class="row">
		    <div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Short Name (If applicable)<br />
						<i style="font-weight: normal;">Tên Viết Tắt (Nếu có)</i></label> 
						<input type="text" class="form-control input-sm" id="shortName" name="shortName"
						value="<c:out value='${ekycDoanhNghiep.shortName}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Name in English (In line with Constitutional Documents) *<br /> 
					<i style="font-weight: normal;">Tên bằng Tiếng Anh (Theo các Văn Kiện Thành Lập) *</i>
					</label>
					 <input type="text" class="form-control input-sm" id="nameInEnglish" name="nameInEnglish"
						value="<c:out value='${ekycDoanhNghiep.nameInEnglish}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Registered Address * <br />
						<i style="font-weight: normal;">Địa Chỉ Trụ Sở Chính (*)</i></label> <input
						type="text" class="form-control input-sm" id="registeredAddress"
						name="registeredAddress"
						value="<c:out value='${ekycDoanhNghiep.registeredAddress}'/>" />
				</div>
			</div>
			
		</div>
		<div class="row">
			
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Operating Address * <br />
						<i style="font-weight: normal;">Địa Chỉ Hoạt Động *</i></label> <input
						type="text" class="form-control input-sm" id="operatingAddress"
						name="operatingAddress"
						value="<c:out value='${ekycDoanhNghiep.operatingAddress}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Country of Incorporation *<br />
						<i style="font-weight: normal;">Quốc Gia Nơi Thành Lập *</i></label> <input
						type="text" class="form-control input-sm"
						id="countryOfIncorporation" name="countryOfIncorporation"
						value="Việt Nam" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Registration Number (*)<br />
						<i style="font-weight: normal;">Số Quyết Định/ Giấy Phép/ Đăng Ký *</i>
					</label>
					<input type="text" class="form-control input-sm"
						id="registrationNumber" name="registrationNumber"
						value="<c:out value='${ekycDoanhNghiep.registrationNumber}'/>" readonly="readonly"/>
				</div>
			</div>
		</div>
		
		<div class="row">
			
		
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Mailing Address *<br /> <i
						style="font-weight: normal;">Địa Chỉ Gửi Thư *</i></label> <input
						type="text" class="form-control input-sm" id="mailingAddress"
						name="mailingAddress"
						value="<c:out value='${ekycDoanhNghiep.mailingAddress}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">SWIFT Bank ID Code (If
						applicable)<br /> <i style="font-weight: normal;">Mã số SWIFT Ngân Hàng (Nếu có)</i>
					</label> <input type="text" class="form-control input-sm"
						id="swiftBankIDCode" name="swiftBankIDCode"
						value="<c:out value='${ekycDoanhNghiep.swiftBankIDCode}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label" style="">Mobile *<br /> <i
						style="font-weight: normal;">Di Động</i> <input type="radio"
						name="mobileOfficeTelephoneRadio" value="yes" checked="checked"/>
					</label> 
					<label class="control-label" style="margin-left: 20px;">OfficeTelephone *<br />
					 <i style="font-weight: normal;">Điện Thoại Văn Phòng</i> <input type="radio" name="mobileOfficeTelephoneRadio" value="no" />
					</label> <input type="text" class="form-control input-sm"
						id="mobileOfficeTelephone" name="mobileOfficeTelephone"
						value="<c:out value='${ekycDoanhNghiep.mobileOfficeTelephone}'/>" />
				</div>
			</div>
		</div>
		<hr />
		<!-- <div class="row">
			
		</div>
		<hr /> -->
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Contact Person *<br /> <i
						style="font-weight: normal;">Người Liên Lạc *</i></label> <input
						type="text" class="form-control input-sm" id="contactPerson"
						name="contactPerson"
						value="<c:out value='${ekycDoanhNghiep.contactPerson}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Email Address *<br /> <i
						style="font-weight: normal;">Địa Chỉ Thư Điện Tử *</i></label> <input
						type="text" class="form-control input-sm" id="emailAddress"
						name="emailAddress"
						value="<c:out value='${ekycDoanhNghiep.emailAddress}'/>" />
				</div>
			</div>
		</div>
			<!-- <hr/> -->
			<div id="divTemplateTk">
				<div style="margin-bottom: 10px;margin-top: 10px;" id="templateTk">
					<div class="row" style="background: #CCC; padding: 5px 0;">
						<c:forEach items="${ekycDoanhNghiepListAccount}" var="item">
                        <div class="col-sm-4">
							<div class="form-group  has-feedback">
								<label class="control-label">Account Type (*)<br /> <i
									style="font-weight: normal;">Loại Tài Khoản (*)</i></label> <select
									class="form-control input-sm" id="accountType"
									name="accountType">
									<option value="">-- Account Type/Loại Tài Khoản --</option>
									<option value="Payment account/ Tài khoản thanh toán"
										<c:if test="${item.accountType eq 'Payment account/ Tài khoản thanh toán' }">selected="selected"</c:if>>Payment account/ Tài khoản thanh toán</option>
									<option value="DICA/ Tài khoản vốn đầu tư trực tiếp nước ngoài"
										<c:if test="${item.accountType eq 'DICA/ Tài khoản vốn đầu tư trực tiếp nước ngoài' }">selected="selected"</c:if>>DICA/ Tài khoản vốn đầu tư trực tiếp nước ngoài</option>
									<option value="IICA/ Tài khoản vốn đầu tư gián tiếp nước ngoài"
										<c:if test="${item.accountType eq 'IICA/ Tài khoản vốn đầu tư gián tiếp nước ngoài' }">selected="selected"</c:if>>IICA/ Tài khoản vốn đầu tư gián tiếp nước ngoài</option>
									<option
										value="Offshore loan account/ Tài khoản vay, trả nợ nước ngoài"
										<c:if test="${item.accountType eq 'Offshore loan account/ Tài khoản vay, trả nợ nước ngoài' }">selected="selected"</c:if>>Offshore loan account/ Tài khoản vay, trả nợ nước ngoài</option>
								</select>
							</div>
						</div>
							<div class="col-sm-4">
								<div class="form-group  has-feedback">
									<label class="control-label">Currency (*)<br /> <i
										style="font-weight: normal;">Loại tiền tệ (*)</i>
									</label> <select class="form-control form-control-sm" id="currency"
										name="currency">
										<option value="">-- Currency/Loại tiền tệ --</option>
										<option value="VND"
											<c:if test="${item.currency eq 'VND' }">selected="selected"</c:if>>VND</option>
										<option value="USD"
											<c:if test="${item.currency eq 'USD' }">selected="selected"</c:if>>USD</option>
										<option value="GBP"
											<c:if test="${item.currency eq 'GBP' }">selected="selected"</c:if>>GBP</option>
										<option value="EUR"
											<c:if test="${item.currency eq 'EUR' }">selected="selected"</c:if>>EUR</option>
										<option value="AUD"
											<c:if test="${item.currency eq 'AUD' }">selected="selected"</c:if>>AUD</option>
										<option value="CHF"
											<c:if test="${item.currency eq 'CHF' }">selected="selected"</c:if>>CHF</option>
										<option value="SGD"
											<c:if test="${item.currency eq 'SGD' }">selected="selected"</c:if>>SGD</option>
										<option value="HKD"
											<c:if test="${item.currency eq 'HKD' }">selected="selected"</c:if>>HKD</option>
										<option value="CAD"
											<c:if test="${item.currency eq 'CAD' }">selected="selected"</c:if>>CAD</option>
										<option value="JPY"
											<c:if test="${item.currency eq 'JPY' }">selected="selected"</c:if>>JPY</option>
										<option value="THB"
											<c:if test="${item.currency eq 'THB' }">selected="selected"</c:if>>THB</option>
										<option value="DKK"
											<c:if test="${item.currency eq 'DKK' }">selected="selected"</c:if>>DKK</option>
										<option value="SEK"
											<c:if test="${item.currency eq 'SEK' }">selected="selected"</c:if>>SEK</option>
										<option value="NOK"
											<c:if test="${item.currency eq 'NOK' }">selected="selected"</c:if>>NOK</option>
									</select>
								</div>
							</div>
							<div class="col-sm-4" style="">
								<div class="form-group  has-feedback">
									<label class="control-label">Account Title<br /> <i
										style="font-weight: normal;">Tên Tài Khoản</i>
									</label> <input type="text" class="form-control form-control-sm"
										id="accountTitle" name="accountTitle"
										value="<c:out value='${item.accountTitle}'/>" />
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<!-- 	<hr/> -->
			<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Registering Email Address for Monthly Statement, Debit/Credit Advice, E-invoice *<br />
					<i style="font-weight: normal;">Đăng Ký Địa Chỉ Thư Điện Tử Để Nhận Sao Kê Hàng Tháng, Giấy Báo Nợ/Có, Hóa Đơn Điện Tử  *</i>
					</label> 
					<input type="text" class="form-control input-sm"id="registeringEmailAddress" name="registeringEmailAddress"
						value="<c:out value='${ekycDoanhNghiep.registeringEmailAddress}'/>" />
				</div>
			</div>

			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Fax Number (If utilise)<br />
						<i style="font-weight: normal;">Số Fax (Nếu sử dụng)</i></label> 
						<input type="text" class="form-control input-sm" id="faxNumber" name="faxNumber"
						value="<c:out value='${ekycDoanhNghiep.faxNumber}'/>" />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Tax Code (If different from Enterprise’s Registration Number)<br /> 
					<i style="font-weight: normal;">Mã Số Thuế (Nếu khác với Số Chứng Nhận Đăng Ký Doanh Nghiệp)</i>
					</label> 
					<input type="text" class="form-control input-sm" id="taxCode"
						name="taxCode" value="<c:out value='${ekycDoanhNghiep.taxCode}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Applicable Accounting Systems *<br />
						<i style="font-weight: normal;">Chế Độ Kế Toán Áp Dụng *</i>
						</label> 
						<select class="form-control input-sm" id="applicableAccountingSystems" name="applicableAccountingSystems">
						<!-- <option value="">-- Applicable Account Systems/ Chế Độ Kế Toán Áp Dụng --</option> -->
						<option value="Vietnamese Accounting Regime/ Chế độ kế toán Việt Nam"
							<c:if test="${ekycDoanhNghiep.applicableAccountingSystems eq 'Vietnamese Accounting Regime/ Chế độ kế toán Việt Nam' }">selected="selected"</c:if>>Vietnamese Accounting Regime/ Chế độ kế toán Việt Nam
						</option>
						<option value="Other (Please specify)/ Khác (Đề nghị nêu cụ thể)" <c:if test="${ekycDoanhNghiep.applicableAccountingSystems eq 'Other (Please specify)/ Khác (Đề nghị nêu cụ thể)' }">selected="selected"</c:if> >
							Other (Please specify)/ Khác (Đề nghị nêu cụ thể)
						</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Tax Mode * <br /> <i
						style="font-weight: normal;">Phương Thức Khai Nộp Thuế *</i></label>
					<select class="form-control input-sm" id="taxMode" name="taxMode">
						<!-- <option value="">-- Tax Mode/ Phương Thức Khai Nộp Thuế--</option> -->
						<option value="Direct/ Trực tiếp khai nộp thuế"
							<c:if test="${ekycDoanhNghiep.taxMode eq 'Direct/ Trực tiếp khai nộp thuế' }">selected="selected"</c:if>>Direct/ Trực tiếp khai nộp thuế
						</option>
						<option value="Withholding/ Khấu trừ tại nguồn tại đơn vị trả thu nhập"
							<c:if test="${ekycDoanhNghiep.taxMode eq 'Withholding/ Khấu trừ tại nguồn tại đơn vị trả thu nhập' }">selected="selected"</c:if>>
							Withholding/ Khấu trừ tại nguồn tại đơn vị trả thu nhập
						</option>
					</select>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Resident Status *<br /> <i
						style="font-weight: normal;">Tình Trạng Cư Trú *</i></label> <select
						class="form-control input-sm" id="residentStatus"
						name="residentStatus">
						<!-- <option value="">-- Resident Status/ Tình Trạng Cư Trú --</option> -->
						<option value="Resident/ Người Cư Trú"
							<c:if test="${ekycDoanhNghiep.residentStatus eq 'Resident/ Người Cư Trú' }">selected="selected"</c:if>>Resident/ Người Cư Trú
						</option>
						<option value="Non-Resident/ Người Không Cư Trú"
							<c:if test="${ekycDoanhNghiep.residentStatus eq 'Non-Resident/ Người Không Cư Trú' }">selected="selected"</c:if>>Non-Resident/ Người Không Cư Trú
						</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Business Activities *<br />
						<i style="font-weight: normal;">Hoạt Động Kinh Doanh *</i></label> <input
						type="text" class="form-control input-sm" id="businessActivities"
						name="businessActivities"
						value="<c:out value='${ekycDoanhNghiep.businessActivities}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Yearly Average Number of
						Employee Contributing to Social Insurance Fund *<br /> 
						<i style="font-weight: normal;">Số Lao Động Tham Gia Bảo Hiểm Xã Hội Bình Quân Năm *</i>
					</label> 
					<input type="text" class="form-control input-sm" id="yearlyAveragenumber" name="yearlyAveragenumber"
						value="<c:out value='${ekycDoanhNghiep.yearlyAveragenumber}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Total Sales Turnover * <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="left"  title="i* Total Sales Turnnover (the turnover of sales of goods and services as shown in Financial Report submitted to Tax Department in the previous year)*
i* Tổ định trên báo cáo tài chính của năm trước liền kề mà doanh nghiệp nộp cho cơ quan thuế)*"></i> <br />
						<i style="font-weight: normal;">Tổng Doanh Thu *</i>
					</label> 
					<input type="text" class="form-control input-sm" id="totalSalesTurnover" name="totalSalesTurnover"
						value="<c:out value='${ekycDoanhNghiep.totalSalesTurnover}'/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Total Capital * <br /> 
					<i style="font-weight: normal;">Tổng Nguồn Vốn  *</i>
					</label> 
					<input type="text" class="form-control input-sm" id="totalCapital" name="totalCapital"
						value="<c:out value='${ekycDoanhNghiep.totalCapital}'/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"> Agree to Receive Through All
						Means Communication <br /> <i style="font-weight: normal;">Đồng Ý Nhận Thông Tin Từ Ngân Hàng Qua Mọi Phương Tiện Liên Lạc  </i>
							<c:if test="${ekycDoanhNghiep.agreeToReceive eq 'yes'}">
							<input type="checkbox" style="margin-left: 20px;" id="agreeToReceive" name="agreeToReceive" checked="checked" />
							</c:if>
							<c:if test="${ekycDoanhNghiep.agreeToReceive eq 'no'}">
							<input type="checkbox" style="margin-left: 20px;" id="agreeToReceive" name="agreeToReceive"  />
							</c:if>
							<c:if test="${agreeToReceive eq 'no'}">
							<input type="checkbox" style="margin-left: 20px;" id="agreeToReceive" name="agreeToReceive" />
							</c:if>
						 
					</label>
				</div>
			</div>
		</div>
			<div class="row">
				<%-- <div class="col-sm-12">
					<div class="form-group  has-feedback">
						<label class="control-label"> SC Relationship manager name
							<br /> <i style="font-weight: normal;">SC tên người quản lý,
								quan hệ</i>
						</label> <input type="text" class="form-control form-control-sm"
							id="relationshipManagerName" name="relationshipManagerName"
							value="<c:out value='${ekycDoanhNghiep.relationshipManagerName}'/>" />
					</div>
				</div> --%>
			</div>
		</div>
	</div>
	<div style="clear: both;" />
	<c:if test="${not empty ekycDoanhNghiep.fileKy }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Opening an account</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileKy }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileBusinessRegistration }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Certificate of business registration</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileBusinessRegistration }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if
		test="${not empty ekycDoanhNghiep.fileAppointmentOfChiefAccountant }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Decision on appointment of chief accountant</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileAppointmentOfChiefAccountant }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileInvestmentCertificate }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Investment certificate</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileInvestmentCertificate }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileCompanyCharter }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Company charter</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileCompanyCharter }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileSealSpecimen }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Seal specimen</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileSealSpecimen }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileFatcaForms }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>FATCA forms</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileFatcaForms }" />
		</div>
		<div style="clear: both;" />
	</c:if>
	<c:if test="${not empty ekycDoanhNghiep.fileOthers }">
		<hr style="color: #CCC; border-top: 1px solid #CCC; clear: both;" />
		<h2>Others</h2>
		<div class="col-sm-12">
			<iframe style="width: 100%; height: 400px; border: 0;"
				src="${contextPath }/ekyc-doanh-nghiep/pdf-byte?path=${ekycDoanhNghiep.fileOthers }" />
		</div>
		<div style="clear: both;" />
	</c:if>
</div>
<script type="text/javascript">
	var successIcon = '<span class="glyphicon glyphicon-ok form-control-feedback"/>';
	var successClass = 'is-valid';
	var errorIcon = '<span class="glyphicon glyphicon-remove form-control-feedback"/>';
	var errorClass = 'is-invalid';
	$(document)
			.ready(
					function() {
						$("#registrationNumber").val(
								'${ekycDoanhNghiep.registrationNumber}');
						addCheckClass('${ekycDoanhNghiep.registrationNumber}',
								'${ktraDoanhNghiep.maSoDoanhNghiep}', "string",
								"registrationNumber");

						$("#nameOfTheAccountHolder").val(
								'${ekycDoanhNghiep.nameCompany}');
						addCheckClass('${ekycDoanhNghiep.nameCompany}',
								'${ktraDoanhNghiep.tenDoanhNghiep}', "string",
								"nameOfTheAccountHolder");

						$("#nameCompany").val('${ekycDoanhNghiep.nameCompany}');
						addCheckClass('${ekycDoanhNghiep.nameCompany}',
								'${ktraDoanhNghiep.tenDoanhNghiep}', "string",
								"nameCompany");

						$("#nameInEnglish").val(
								'${ekycDoanhNghiep.nameInEnglish}');
						addCheckClass('${ekycDoanhNghiep.nameInEnglish}',
								'${ktraDoanhNghiep.tenDoanhNghiepEn}',
								"string", "nameInEnglish");

						$("#shortName").val('${ekycDoanhNghiep.shortName}');
						addCheckClass('${ekycDoanhNghiep.shortName}',
								'${ktraDoanhNghiep.tenDoanhNghiepVietTat}',
								"string", "shortName");

						$("#registeredAddress").val(
								'${ekycDoanhNghiep.registeredAddress}');
						addCheckClass('${ekycDoanhNghiep.registeredAddress}',
								'${ktraDoanhNghiep.diaChi}', "string",
								"registeredAddress");

						$("#fax").val('${ekycDoanhNghiep.faxNumber}');
						$("#email").val('${ekycDoanhNghiep.emailAddress}');
						$("#dienThoai").val(
								'${ekycDoanhNghiep.mobileOfficeTelephone}');

					});

	function addCheckClass(value, valueCheck, type, id) {
		if (type == "string") {
			if ((valueCheck != null && value != null && value.toLowerCase() == valueCheck
					.toLowerCase())) {
				$("#" + id).addClass(successClass);
			} else if (valueCheck == "" || valueCheck == null || value == ""
					|| value == null) {

			} else {
				$("#" + id).addClass(errorClass);
				$("#" + id).parent().append(
						'<span class="help-block">' + valueCheck + '</span>');
			}
		}
	}
	function numberWithCommas(x) {
		if (x && x.trim() != "")
			return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

		return "";
	}
</script>