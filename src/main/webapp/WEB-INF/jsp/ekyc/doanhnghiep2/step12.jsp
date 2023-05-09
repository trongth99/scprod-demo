<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-11" >
	<div class="panel-heading">
	<h3 class="panel-title">	Complete the Registration Process/ <i>Hoàn Tất Quá Trình Đăng Ký<i/></h3>
	</div>
	<div class="panel-body"style="font-style: normal;">
		<div class="form-group text-center">
				<div class="row">
				<div class="form-group col-sm-4">
					<div class="form-group  has-feedback">
						<label class="control-label">Full Name of Contact Person<br/><i style="font-weight: normal;">Họ và Tên của Người Liên Hệ</i></label>
						<input type="text" class="form-control input-sm" name="nameContractPersion" id="nameContractPersion" value="<c:out value='${ekycDoanhNghiepTable.tenNguoiLienHe}'/>"/>
					</div>
				</div>
				<div class="form-group col-sm-4">
					<div class="form-group  has-feedback">
						<label class="control-label">Email Address of Contact Person<br/><i style="font-weight: normal;">Địa Chỉ Thư Điện Tử của Người Liên Hệ</label>
						<input type="text" class="form-control input-sm" name="emailContractPersion" id="emailContractPersion" value="<c:out value='${ekycDoanhNghiepTable.emailNguoiLienHe}'/>"/>
					</div>
				</div>
				<div class="form-group col-sm-4" style="font-style: normal;">
					<div class="form-group  has-feedback">
						<label class="control-label">Phone of Contact Person<br/><i style="font-weight: normal;">Số Điện Thoại của Người Liên Hệ</i></label>
						<input type="text" class="form-control input-sm" name="phoneContractPersion" id="phoneContractPersion" value="<c:out value='${ekycDoanhNghiepTable.soDienThoaiNguoiLienHe}'/>"/>
					</div>
				</div>
			</div>

			<button class="btn btn-primary nextBtn pull-right " style="font-style: normal;" type="button" onclick="validateStepSendMail(this)"data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />" id="textLoad4"><spring:message code="ekycdn.ket_thuc" /></button>
		</div>
	</div>
</div>
<script type="text/javascript">
function validateStepSendMail(obj) {
	
	 $(document).ready(function() {
		 
		
				if($("#editStatusContractPersion").is(":checked")){
					$('#hoVaTenContractPersion').prop('readonly', false);
					$('#soDienThoaiContractPersion').prop('readonly', false);
					$('#emailContractPersion').prop('readonly', false);
				}
				 $("#editStatusContractPersion").click(function(){
					if($("#editStatusContractPersion").is(":checked")){
						$('#hoVaTenContractPersion').prop('readonly', false);
						$('#soDienThoaiContractPersion').prop('readonly', false);
						$('#emailContractPersion').prop('readonly', false);
					}else{
						$('#hoVaTenContractPersion').prop('readonly', true);
						$('#soDienThoaiContractPersion').prop('readonly', true);
						$('#emailContractPersion').prop('readonly', true);
					}
					
				});
			
		 
		
		  }); 
	
		 
	if($("#emailContractPersion").val().trim() == "") {
		toastr.error("Email address of contact person is not empty");
	} else if(!validateEmail($("#emailContractPersion").val().trim())) {
		toastr.error("Email address of contact invalid");
	} else if ( $("#phoneContractPersion").val().trim() == "" ){
		toastr.error("Phone of contact person is not empty");
	} else if ( $("#nameContractPersion").val().trim() == "" ){
		toastr.error("Full name of contact person is not empty");
	}  else {
		$(obj).button('loading');
		var data = {
				"emailContractPersion": $("#emailContractPersion").val(),
				"phoneContractPersion": $("#phoneContractPersion").val(),
				"nameContractPersion": $("#nameContractPersion").val(),
				"token": token,
				"editStatusContractPersion":$("#editStatusContractPersion").is(":checked")?"yes":"no"	
		};
		$.ajax({
			url:'/ekyc-enterprise/send-mail-edit',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			 toastr.options.timeOut = 5000;
			toastr.info("You have successfully completed the first step to register to open an account. Please check your email again to start the eKYC process./Quý khách đã thực hiện thành công bước đầu để đăng ký mở tài khoản. Quý khách vui lòng kiểm tra lại email để bắt đầu quy trình eKYC.");
			$(document).ready(function() {
				 setInterval( function () {
		        	 location.href='${contextPath }/login-doanh-nghiep';
						//nextStep(obj);
						$(obj).button('reset');
		         }, 2000);
			});
			
		}).fail(function(data) {
			toastr.error("Lỗi kiểm tra thông tin");
			$(obj).button('reset');
		});
	}

	
}


 
 
</script>