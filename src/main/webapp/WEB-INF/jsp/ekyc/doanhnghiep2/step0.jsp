<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-0">
	<div class="panel-heading">
		<h3 class="panel-title"><spring:message code="ekycdn.guideline" /></h3>
	</div>
	<div class="panel-body">
		<p>
			<b>Step 1: Upload Documents </b><br/>
			<i>Bước 1: Tải Tài Liệu</i><br/>
			<p>All documents must be digitally signed before uploading/ <i>Tất cả các tài liệu cần được ký điện tử trước khi tải lên</i></p>
			<select name="chontailieu" id="chontailieu">
			    <option value="1" <c:if test="${ekycDoanhNghiep.typeDocument eq '1' }">selected="selected"</c:if> >Vietnamese Local Company/ Công Ty Việt Nam</option>
			    <option value="2" <c:if test="${ekycDoanhNghiep.typeDocument eq '2' }">selected="selected"</c:if> >Foreign Direct Investment Company/ <i>Công Ty Có Vốn Đầu Tư Nước Ngoài</i></option>
			    <option value="3" <c:if test="${ekycDoanhNghiep.typeDocument eq '3' }">selected="selected"</c:if> >Vietnamese Micro Small Enterprises/ <i>Công Ty Việt Nam Siêu Nhỏ</i></option>
			 
			</select>
		</p>
      	

      
      	<input type="hidden" class="form-control input-sm" name="typeDocument" id="typeDocument" value="<c:out value='${ekycDoanhNghiep.typeDocument}'/>"/>
      		<input type="hidden" class="form-control input-sm" name="statusDk" id="statusDk" value="<c:out value='${ekycDoanhNghiepTable.statusDonKy}'/>"/>
      
        <ul class="" id="vietnam" style="display: none;">
		
			<li>Enterprise Registration Certificate/ <i>Giấy Chứng Nhận Đăng Ký Kinh Doanh</i> *</li>
			<li>Company’s Charter/ <i>Điều Lệ Công Ty</i> *</li>
			<li>Chief Accountant or PIC of Accounting Appointment Letter/ <i>Giấy Bổ Nhiệm Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i> *</li>
			<li>FATCA form/ <i>Biểu mẫu FATCA</i> *</li>
			<li>Shareholder list/ <i>Danh sách Cổ Đông </i>*</li>
			<li>Tax Certificate (if applicable)/ <i>Giấy Chứng Nhận Mã Số Thuế (nếu có)</i></li>
			<li>Others (if any)/ <i>Giấy tờ khác (nếu có)</i></li>
		</ul>
     
		<ul class="" id="ctyvietnam" style="display: none;">
		
			<li>Enterprise Registration Certificate/ <i>Giấy Chứng Nhận Đăng Ký Kinh Doanh</i>  *</li>
			<li>Company’s Charter/ <i>Điều Lệ Công Ty</i>  *</li>
			<li>Chief Accountant or PIC of Accounting Appointment Letter/ <i>Giấy Bổ Nhiệm Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i>  *</li>
			<li>FATCA form/ <i>Biểu mẫu FATCA</i>  *</li>
			<li>Shareholder list/ <i>Danh sách Cổ Đông</i> * </li>
			<li>Tax Certificate (if applicable)/ <i>Giấy Chứng Nhận Mã Số Thuế (nếu có)</i> </li>
			<li>Others (if any)/ <i>Giấy tờ khác (nếu có)</i></li>
		</ul>
		
		<ul   id="ctyvondtuncngoai"  style="display: none;">
			<li>Enterprise Registration Certificate/ <i>Giấy Chứng Nhận Đăng Ký Kinh Doanh </i>*</li>
			<li>Company’s Charter/ <i>Điều Lệ Công Ty</i> *</li>
			<li>Chief Accountant or PIC of Accounting Appointment Letter/ <i>Giấy Bổ Nhiệm Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i> *</li>
			<li>FATCA form/ <i>Biểu mẫu FATCA</i>  *</li>
			<li>Letter of Undertaking and Indemnity regarding Capital Account/ <i>Thư Cam Kết và Bồi Hoàn liên quan đến Tài Khoản Vốn</i></li>
			<li>Investment Registration Certificate (if applicable)/ <i>Giấy Chứng Nhận Đầu Tư (nếu có)</i></li>
			<li>Shareholder list/ <i>Danh sách Cổ Đông</i> *</li>
			<li>Tax Certificate (if applicable)/ <i>Giấy Chứng Nhận Mã Số Thuế (nếu có)</i></li>
			<li>Others (if any)/ <i>Giấy tờ khác (nếu có)</i></li>
		</ul>
		<ul id="ctynho"  style="display: none;">
			<li>Enterprise Registration Certificate/ <i>Giấy Chứng Nhận Đăng Ký Kinh Doanh</i>  *</li>
			<li>Company’s Charter/ <i>Điều Lệ Công Ty</i>  *</li>
			<li>Chief Accountant or PIC of Accounting Appointment Letter/ <i>Giấy Bổ Nhiệm Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i>  *</li>
			<li>FATCA form/ <i>Biểu mẫu FATCA</i>  *</li>
			<li>Declaration on Micro SME/ <i>Tờ khai xác định Doanh Nghiệp Siêu Nhỏ</i>  *</li>
			<li>Investment Registration Certificate (if applicable)/ <i>Giấy Chứng Nhận Đầu Tư (nếu có)</i> </li>
			<!-- <li>Tax certificate (if applicable) <br/> <i>Giấy chứng nhận mã số thuế</i></li> -->
			<li>Others (if any)/ <i>Giấy tờ khác (nếu có)</i></li>
		</ul>
	
		<p><b>Step 2:  Company Information</b><br/><i>Bước 2: Thông Tin Công Ty</i></p>
		<p><b>Step 3: Legal Representative Information</b> <br/> <i>Bước 3: Thông Tin của Đại Diện Pháp Luật</i></p>
		<p><b>Step 4: Chief Accountant or Person in Charge (PIC) of Accounting Information</b> <br/> <i>Bước 4: Thông Tin Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i></p>
		<p><b>Step 5: Member’s Council, Board of Directors, Board of Management Information</b><br/><i>Bước 5:Thông Tin Hội Đồng Thành Viên, Hội Đồng Quản Trị, Ban Điều Hành</i></p>
		<!-- <p><b>Step 6:  Special Instruction</b><br/><i>Bước 6: Chỉ Dẫn Đặc Biệt</i></p> -->
		<p><b>Step 6:  Authorised Person(s) of Account Holder’s Representative Information</b><br/><i>Bước 6: Thông Tin Người Được Đại Diện Chủ Tài Khoản Ủy Quyền Ký</i></p>
		<p><b>Step 7:  Authorised Person(s) of Chief Accountant (Person In Charge of Accounting) Information</b><br/><i>Bước 7:Thông Tin Người Được Kế Toán Trưởng (Người Phụ Trách Bộ Phận Kế Toán) Ủy Quyền Ký</i></p>
		<p><b>Step 8:  Straight2Bank User Designation & Authorisation</b><br/><i>Bước 8: Chỉ Định và Ủy Quyền Người Sử Dụng Straight2Bank</i></p>
		<p><b>Step 9:  Special Instruction</b><br/><i>Bước 9: Chỉ Dẫn Đặc Biệt</i></p>
		<p><b>Step 10:  Complete the Registration Process </b><br/><i>Bước 10: Hoàn Tất Quá Trình Đăng Ký</i></p>
		
		 <p>*: Mandatory/ <i>Bắt buộc</i></p>
		<button class="btn btn-primary nextBtn pull-right" type="button"  onclick="validateStep0(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />"><spring:message code="ekycdn.tiep_theo" /></button>
	</div>
</div>

<script type="text/javascript">

$(document).ready(function(){
	
	    
           var type = 10;
          if( type == 10){
        	  $("#vietnam").show();
        	  type = 11;
        	  $("#ctyvn").show();
          }else {
        	  $("#vietnam").hide();
        	  $("#ctyvn").hide();
		} 
	
		
		if($("#typeDocument").val() == "1"){
			 $("#vietnam").hide();
			$("#ctyvietnam").show();
		}
		
		if($("#typeDocument").val() == "2"){
			 $("#vietnam").hide();
			$("#ctyvondtuncngoai").show();
		
		}else{
			$("#ctyvondtuncngoai").hide();
			
		}
		if($("#typeDocument").val() == "3"){
			$("#ctynho").show();
			 $("#vietnam").hide();
		}else {
			$("#ctynho").hide();
		
		}
		

		
		//if(typeDc == "1"){
		/* 	var type = $("#typeDocument").val();
			alert(type)
			
			if($("#typeDocument").val() == null){
				$("#ctyvietnam").show();
			}else{
				$("#ctyvietnam").hide();
			}  */
		//}
	 	
	
	
});
var chon;
$('#chontailieu').on('change', function () {
	  
	   if($("#chontailieu").val() == "3"){
		   chon = 3;
	       $("#ctynho").show();
	       $("#vietnam").hide();
	     	$("#ctnho").show();
	   } else {
			$("#ctnho").hide();
	       $("#ctynho").hide();
	   }
	   if($("#chontailieu").val() == "2"){
		   chon = 2
	       $("#ctyvondtuncngoai").show();
	       $("#vietnam").hide();
	       $("#ctyncngoai").show();
	   } else {
	       $("#ctyvondtuncngoai").hide();
	       $("#ctyncngoai").hide();
	   }
	   if($("#chontailieu").val() == "1"){
		   chon = 1;
	       $("#ctyvietnam").show();
	       $("#vietnam").hide();
	       $("#ctyvn").show();
	   } else {
			$("#ctyvn").hide();
	       $("#ctyvietnam").hide();
	   }
	});
	function validateStep0(obj) {
		var token = "";
			$(obj).button('loading');
			
			var data = {
			
				"typeDocument": $("#chontailieu").val()
				//"haveAChiefAccountant": $("#xacNhanKtt").is(":checked")?"yes":"no",
				//"editStatusKtt":$("#editStatusKtt").is(":checked")?"no":"yes",
			};

			
			$.ajax({
				url:'/ekyc-enterprise/luu-thong-tin-step0',
			    data: JSON.stringify(data),
			    type: 'POST',
			    processData: false,
			    contentType: 'application/json'
			   
			}).done(function(data) {
				if(data.status == 200) {
					token = data.token;
					nextStep(obj);
					$(obj).button('reset');
				} else if(data.status == 505){
					location.href='/login-doanh-nghiep';
				}else if(data.status == 500){
					location.href='/login-doanh-nghiep';
				}else {
					toastr.error("Not enough information to store / Không đủ thông tin cần lưu trữ");
					$(obj).button('reset');	
				}
			}).fail(function(data) {
				//toastr.error("Error check / Lỗi lưu thông tin");
				$(obj).button('reset');
				location.href='/login-doanh-nghiep';
			}); 
			
		
		//nextStep(obj);
	}
</script>