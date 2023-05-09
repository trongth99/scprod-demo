<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-1">
	<div class="panel-heading">
		<h3 class="panel-title">Upload Documents/ <i>Tải Tài Liệu</i></h3>
	</div>
	<div class="panel-body">
		<form id="dataGiayDangKyKd" method="post" enctype="multipart/form-data">
			<input type="hidden" value="dn" name="loaiGiayTo" id="loaiGiayTo"/>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileDangKyKinhDoanh" id="fileDangKyKinhDoanh" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFile" id="tenFileDangKyKinhDoanh" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileDangKyKinhDoanh').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span >Enterprise Registration Certificate/ <i>Giấy Chứng Nhận Đăng Ký Kinh Doanh</i> </span> <span >*</span>                                      
	            </button>
	        
	            <small id="nameFileDangKyKinhDoanh" style="display: none;"></small><br/>
	            <iframe id="base64FileDangKyKinhDoanhImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileBusinessRegistration }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileBusinessRegistration }&token=${token}"></iframe>
				<textarea id="base64FileDangKyKinhDoanh" style="display: none;"><c:out value="${fileBusinessRegistrationBase64 }"/></textarea>
			</div>
			
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileQuyetDinhBoNhiemKtt" id="fileQuyetDinhBoNhiemKtt" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileQuyetDinhBoNhiemKtt" id="tenFileQuyetDinhBoNhiemKtt" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="loaiGiayToQuyetDinhBoNhiemKtt" id="loaiGiayToQuyetDinhBoNhiemKtt" style="display: none;" value="qdbnktt"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileQuyetDinhBoNhiemKtt').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span >Chief Accountant or PIC of Accounting Appointment Letter/ <i>Giấy Bổ Nhiệm Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán </i>   </span> <span >*</span>                                     
	            </button>
	      
	            <small id="nameFileQuyetDinhBoNhiemKtt" style="display: none;"></small><br/>
	            <iframe id="base64FileQuyetDinhBoNhiemKttImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileAppointmentOfChiefAccountant }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileAppointmentOfChiefAccountant }&token=${token}"></iframe>
				<textarea id="base64FileQuyetDinhBoNhiemKtt" style="display: none;"><c:out value="${fileAppointmentOfChiefAccountantBase64 }"/></textarea>
			</div>
		

		<div class="form-group text-left">
			<input type="file" class="form-control-file" name="fileCompanyCharter" id="fileCompanyCharter" accept=".pdf" style="display: none;"/>
			<input type="hidden" class="form-control-file" name="tenFileCompanyCharter" id="tenFileCompanyCharter" style="display: none;"/>
			<button class="btn btn-default btn-lg" onclick="document.getElementById('fileCompanyCharter').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
                <span class="glyphicon glyphicon-cloud-upload"></span>
                <span >Company’s Charter/ <i>Điều Lệ Công Ty</i>   </span>  <span >*</span>                                
            </button>
          
            <small id="nameFileCompanyCharter" style="display: none;"></small><br/>
            <iframe id="base64FileCompanyCharterImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileCompanyCharter }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileCompanyCharter }&token=${token}"></iframe>
			<textarea id="base64FileCompanyCharter" style="display: none;"><c:out value="${fileCompanyCharterBase64 }"/></textarea>
		</div>
		<div class="form-group text-left">
			<input type="file" class="form-control-file" name="fileFatcaForms" id="fileFatcaForms" accept=".pdf" style="display: none;"/>
			<input type="hidden" class="form-control-file" name="tenFileFatcaForms" id="tenFileFatcaForms" style="display: none;"/>
			<button class="btn btn-default btn-lg" onclick="document.getElementById('fileFatcaForms').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
                <span class="glyphicon glyphicon-cloud-upload"></span>
                 <span  >FATCA form/ <i>Biểu mẫu FATCA</i> </span> <span >*</span>                                  
            </button>
            
            <small id="nameFileFatcaForms" style="display: none;"></small><br/>
            <iframe id="base64FileFatcaFormsImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileFatcaForms }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileFatcaForms }&token=${token}"></iframe>
			<textarea id="base64FileFatcaForms" style="display: none;"><c:out value="${fileFatcaFormsBase64 }"/></textarea>
		</div>
		
	
		
	
		
		<div id="ctyvn" style="display: none">
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                 <span  >Shareholder list/ <i>Danh sách Cổ Đông</i>   *</span> 
	                <!-- Shareholder list/ Danh sách Cổ Đông<span style="font-style: oblique;"></span>   -->                              
	            </button>
	         
	            <small id="nameFileInvestmentCertificate" style="display: none;"></small><br/>
	            <iframe id="base64FileInvestmentCertificateImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileInvestmentCertificate }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileInvestmentCertificate }&token=${token}"></iframe>
				<textarea id="base64FileInvestmentCertificate" style="display: none;"><c:out value="${fileInvestmentCertificateBase64 }"/></textarea>
			</div>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span > Tax Certificate (if applicable)/ <i>Giấy Chứng Nhận Mã Số Thuế (nếu có)</i> </span>                          
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileOthers" id="fileOthers" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileOthers" id="tenFileOthers" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileOthers').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	               <span > Others (if any)/<i> Tài liệu khác (nếu có)</i></span>                                 
	            </button>
	            
	            <small id="nameFileOthers" style="display: none;"></small><br/>
	            <iframe id="base64FileOthersImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileOthers }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileOthers }&token=${token}"></iframe>
				<textarea id="base64FileOthers" style="display: none;"><c:out value="${fileOthersBase64 }"/></textarea>
		    </div>  
		
		</div>
		
		
		<div id ="ctyncngoai" style="display: none">
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	               <span > Letter of Undertaking and Indemnity regarding Capital Account/ <i>Thư Cam Kết và Bồi Hoàn liên quan đến Tài Khoản Vốn</i></span>                                 
	            </button>
	         
	            <small id="nameFileInvestmentCertificate" style="display: none;"></small><br/>
	            <iframe id="base64FileInvestmentCertificateImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileInvestmentCertificate }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileInvestmentCertificate }&token=${token}"></iframe>
				<textarea id="base64FileInvestmentCertificate" style="display: none;"><c:out value="${fileInvestmentCertificateBase64 }"/></textarea>
			</div>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                 <span >Investment Registration Certificate (if applicable)/ <i>Giấy Chứng Nhận Đầu Tư (nếu có)</i>  </span>                   
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div>
			
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	               <span > Shareholder list/ <i>Danh sách Cổ Đông *</i> </span>                                
	            </button>
	         
	            <small id="nameFileInvestmentCertificate" style="display: none;"></small><br/>
	            <iframe id="base64FileInvestmentCertificateImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileInvestmentCertificate }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileInvestmentCertificate }&token=${token}"></iframe>
				<textarea id="base64FileInvestmentCertificate" style="display: none;"><c:out value="${fileInvestmentCertificateBase64 }"/></textarea>
			</div>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span >Tax Certificate (if applicable)/ <i>Giấy Chứng Nhận Mã Số Thuế (nếu có)</i></span>                         
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div>
		  <div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileOthers" id="fileOthers" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileOthers" id="tenFileOthers" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileOthers').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	               <span > Others (if any)/<i> Tài liệu khác (nếu có)</i></span>                                     
	            </button>
	            
	            <small id="nameFileOthers" style="display: none;"></small><br/>
	            <iframe id="base64FileOthersImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileOthers }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileOthers }&token=${token}"></iframe>
				<textarea id="base64FileOthers" style="display: none;"><c:out value="${fileOthersBase64 }"/></textarea>
		    </div>  
		</div>
		<%-- <input type="text" class="form-control input-sm" name="typeDocument2" id="typeDocument2" value="<c:out value='${ekycDoanhNghiep.typeDocument}'/>"/> --%>
		<div id ="ctnho" style="display: none">
		
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span >Declaration on Micro SME/ <i>Tờ khai xác định Doanh Nghiệp Siêu Nhỏ *</i></span>                              
	            </button>
	         
	            <small id="nameFileInvestmentCertificate" style="display: none;"></small><br/>
	            <iframe id="base64FileInvestmentCertificateImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileInvestmentCertificate }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileInvestmentCertificate }&token=${token}"></iframe>
				<textarea id="base64FileInvestmentCertificate" style="display: none;"><c:out value="${fileInvestmentCertificateBase64 }"/></textarea>
			</div>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span >Investment Registration Certificate (if applicable)/ <i>Giấy Chứng Nhận Đầu Tư (nếu có)</i> </i></span>   
	                                      
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div>
			

			<%-- <div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                Tax certificate (if applicable)    <span style="font-style: oblique;">/ Giấy chứng nhận mã số thuế</span>                         
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div> --%>
		     <div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileOthers" id="fileOthers" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileOthers" id="tenFileOthers" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileOthers').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	               <span > Others (if any)/<i> Tài liệu khác (nếu có)</i></span>                                      
	            </button>
	            
	            <small id="nameFileOthers" style="display: none;"></small><br/>
	            <iframe id="base64FileOthersImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileOthers }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileOthers }&token=${token}"></iframe>
				<textarea id="base64FileOthers" style="display: none;"><c:out value="${fileOthersBase64 }"/></textarea>
		    </div>  
		
		</div>
		

		


		 
<!-- 		<span style="font-weight: bold;color: red;"><spring:message code="ekycdn.chu_y" /></span> -->
        <div class="pull-left"> *: Mandatory/ <i>Bắt buộc</i></div>
		<button  class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep1(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />"><spring:message code="ekycdn.tiep_theo" /></button>
	</form>
	</div>
</div>

<script type="text/javascript">
var changeFileDangKyKinhDoanh = false;
var changeFileKtt = false;

	function setCookie(cname, cvalue, exdays) {
		  var d = new Date();
		  d.setTime(d.getTime() + (exdays*24*60*60*1000));
		  var expires = "expires="+ d.toUTCString();
		  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}
	$(document).ready(function(){
		
		
		addEventAllFile("fileDangKyKinhDoanh", "base64FileDangKyKinhDoanh", "nameFileDangKyKinhDoanh");
		addEventAllFile("fileQuyetDinhBoNhiemKtt", "base64FileQuyetDinhBoNhiemKtt", "nameFileQuyetDinhBoNhiemKtt");
		addEventAllFile("fileBusinessRegistrationCertificate", "base64FileBusinessRegistrationCertificate", "nameFileBusinessRegistrationCertificate");
		addEventAllFile("fileDecisionToAppointChiefAccountant", "base64FileDecisionToAppointChiefAccountant", "nameFileDecisionToAppointChiefAccountant");
		
		addEventAllFile("fileInvestmentCertificate", "base64FileInvestmentCertificate", "nameFileInvestmentCertificate");
		addEventAllFile("fileCompanyCharter", "base64FileCompanyCharter", "nameFileCompanyCharter");
		addEventAllFile("fileSealSpecimen", "base64FileSealSpecimen", "nameFileSealSpecimen");
		addEventAllFile("fileFatcaForms", "base64FileFatcaForms", "nameFileFatcaForms");
		addEventAllFile("fileOthers", "base64FileOthers", "nameFileOthers");
		
		checkNextKt = 1 ;	
 		 checkNextCc = 1;		
 		 checkNextFf = 1;	
 		$("#fileDangKyKinhDoanh").change(function(){
			changeFileDangKyKinhDoanh = true;
		});
 		$("#fileQuyetDinhBoNhiemKtt").change(function(){
 			changeFileKtt = true;
		});

 	
 			
	});
	
	
	function remove(iframeImg, nameFile, textarea) {
		$("#"+iframeImg).attr("src", "");
		$("#"+iframeImg).css("display", "none");
		$("#"+nameFile).css("display", "none");
		$("#"+textarea).html("");
	}
	var obj2;
	function validateStep1(obj) {
		obj2 = obj;
		$(obj).button('loading');
		
		
			if ($("#base64FileDangKyKinhDoanh").html() == "") {
				toastr.error("Please upload certificate of Business Registration/ Vui lòng tải Giấy Phép Kinh Doanh");
				$(obj).button('reset');
				return false;
			} if ($("#base64FileQuyetDinhBoNhiemKtt").html() == "") {
				toastr.error(" Please upload Decision on Appointment of Chief Accountant/ Vui lòng tải lên Quyết Định Bổ Nhiệm Kế Toán Trưởng");
				$(obj).button('reset');
				return false;
			} if ($("#base64FileCompanyCharter").html() == "") {
				toastr.error("Please upload Company Charter/ Vui lòng tải lên Điều Lệ Công Ty");
				$(obj).button('reset');
				return false;
			} if ($("#base64FileFatcaForms").html() == "") {
				toastr.error("Please upload FATCA forms/ Vui lòng tải lên biểu mẫu FATCA");
				$(obj).button('reset');
				return false;
			}if ($("#base64FileInvestmentCertificate").html() == "") {
				toastr.error("Please upload Shareholder list/ Vui lòng tải lên Danh sách Cổ Đông");
				$(obj).button('reset');
				return false;
			} else{
				
					 console.log(checkNextKt);
					console.log(checkNextCc);
					console.log(checkNextFf); 
					
					if(checkNextKt == 1 && checkNextCc == 1 && checkNextFf == 1){
						 console.log("Success!!!!");
						var file = document.querySelector('#fileDangKyKinhDoanh').files[0];
						
						var base64 = $("#base64FileDangKyKinhDoanh").html();
						//alert($("#fileDangKyKinhDoanh").val())
						var fileQuyetDinhBoNhiemKtt = document.querySelector('#fileQuyetDinhBoNhiemKtt').files[0];
						var base64FileQuyetDinhBoNhiemKtt = $("#base64FileQuyetDinhBoNhiemKtt").html();
						
					
					if($("#fileDangKyKinhDoanh").val() == "" && $("#fileQuyetDinhBoNhiemKtt").val() != ""){
							
							docGiayBoNhiemKtt(obj);
							uploadDuLieuStep2(obj);
						}else{ 
							if(base64 != "" && base64FileQuyetDinhBoNhiemKtt != null) {
								$(obj).button('loading');
								
								if(changeFileDangKyKinhDoanh ) {
									docThongTinDoanhNghiep(obj);
								
								
								}else{
									uploadDuLieuStep2(obj);
								}
								
								
							} else {
								toastr.warning("Loading file, please wait.")
							}
						}
					}else{
						$(obj).button('reset');
					}
			}
			return true;
		
	
	}
	

	
	function docThongTinDoanhNghiep(obj) {
		var formData = new FormData($("#dataGiayDangKyKd")[0]);
		
		$.ajax({
			url:'/ekyc-enterprise/thong-tin-doanh-nghiep',
		    data: formData,
		    type: 'POST',
		    processData: false,
		    contentType: false
		}).done(function(data) {
			console.log(data);
			if(data.status == "200") {
				dataDn = data;
				$(".help-block").remove();
				$(".form-control-feedback").remove();
				$("#loaiGiayToText").val($("#loaiGiayTo option:selected").text());
				
				$("#nameOfTheAccountHolder").val(data.data.tenDoanhNghiep);
				$("#nameCompany").val(data.data.tenDoanhNghiep);
				$("#taxCode").val(data.data.maSoThue);
				$("#operatingAddress").val(data.data.diaChi);
				$("#registeredAddress").val(data.data.diaChi);
				$("#registrationNumber").val(data.data.maSoDoanhNghiep);
				$("#nameInEnglish").val(data.data.tenDoanhNghiepEn);
				$("#shortName").val(data.data.tenDoanhNghiepVietTat);
				$("#emailAddress").val(data.data.email);
				$("#hoVaTenNddpl").val(data.data.hoVaTen);
				
				setCookie("mst", data.data.maSoThue, 1)
				if(!data.data.maLoaiGiayTo) {
					$("#maGiayTo").prop("disabled", false);
				} else {
					$("#maGiayTo").val(data.data.maLoaiGiayTo);
					$("#maGiayTo").prop("disabled", true);
				}
				if(data.data.maLoaiGiayTo == 'hc') {
					$("#divAnhMatSau").hide();				
				} else {
					$("#divAnhMatSau").show();
				}
				
				var checkCks = false;
				var cksMst = "";
				try {
					var thongTinChuKy = JSON.parse(data.data.thongTinChuKy);
					console.log(thongTinChuKy)
					var table = "<table class='table'>";
					for (x in thongTinChuKy) {
						for (y in thongTinChuKy[x]) {
							var title = y;
							if(y=='error') title = '<spring:message code="ekycdn.thong_bao" />';
							if(y=='signature') title = '<spring:message code="ekycdn.chu_ky" />';
							if(y=='subject') title = '<spring:message code="ekycdn.ten" />';
							if(y=='signtime') title = '<spring:message code="ekycdn.thoi_gian" />';
							if(y=='certificate') title = '<spring:message code="ekycdn.chung_chi" />';
							if(y=='from') title = '<spring:message code="ekycdn.tu_ngay" />';
							if(y=='to') title = '<spring:message code="ekycdn.den_ngay" />';
							if(y=='validity') title = '<spring:message code="ekycdn.hieu_luc" />';
							if(y=='issuer') title = '<spring:message code="ekycdn.nguoi_phat_hanh" />';
							if(y=='mst') {
								title = '<spring:message code="ekycdn.mst" />';
								cksMst = thongTinChuKy[x][y];
								checkCks = true;
							}
						//	if(thongTinChuKy[x][y] == "Signature is not exists") thongTinChuKy[x][y] = '<spring:message code="ekycdn.khong_co_chu_ky_so" />';
							table += "<tr><td>"+title+"</td><td>"+thongTinChuKy[x][y]+"</td></tr>";
						}
					}
					table += "</table>";
				} catch (error) {
					 console.error(error);
					 $(obj).button('reset');
				}
				if(!checkCks) {
				//	toastr.error('<spring:message code="ekycdn.khong_co_chu_ky_so" />');
					$(obj).button('reset');
				} else {
					console.log(data.data.maSoThue)
					console.log(cksMst)
					if(data.data.maSoThue == cksMst) {
				
						if($("#fileQuyetDinhBoNhiemKtt").val() == ""){
						
							//if(checkNextKt == 1 && checkFatca == 1 && checkCompanyCharter == 1){
								nextStep(obj);
							//}
							
							
						}else{
						
							docGiayBoNhiemKtt(obj);
							
						}
						
					   
						
					} else {
						toastr.error('<spring:message code="ekycdn.mst_khong_dung" />');
						$(obj).button('reset');
					}
				}
			
				uploadDuLieuStep2(obj);
			} else if(data.status == "505") {
				location.href='/login-doanh-nghiep';
			} else {
				toastr.error(data.message);
				$(obj).button('reset');
			}
		}).fail(function(data) {
			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
			$(obj).button('reset');
		});
	};
	

	var thongTinBoNhiemKtt;
	function docGiayBoNhiemKtt(obj) {
		$(obj).button('loading');
 		var formData = new FormData($("#dataGiayDangKyKd")[0]);
		
 		$.ajax({
 			url:'/ekyc-enterprise/thong-tin-ktt',
 		    data: formData,
 		    type: 'POST',
 		    processData: false,
 		    contentType: false
 		}).done(function(data) {
 			console.log(data);
 			if(data.status==200) {
 				thongTinBoNhiemKtt = data.data;
				nextStep(obj);	
 			}
			$(obj).button('reset');
 		}).fail(function(data) {
 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
 			$(obj).button('reset');
 		});
	}
	
	var checkNextKt = 0;
	$("#fileQuyetDinhBoNhiemKtt").change(function(){
		
	var formData = new FormData($("#dataGiayDangKyKd")[0]);
		
 		$.ajax({
 			url:'/ekyc-enterprise/check-chu-ky-so-kt',
 		    data: formData,
 		    type: 'POST',
 		    processData: false,
 		    contentType: false
 		}).done(function(data) {
 			console.log(data);
 			if(data.status==200) {
 				checkNextKt = 1 ;
 				//thongTinBoNhiemKtt = data.data;
				//nextStep(obj);	
				//$(obj).button('reset');
 			}else if(data.status == 401){
 				 checkNextKt = 0 ;
 				if($("#fileQuyetDinhBoNhiemKtt").val() != ""){
 	 				toastr.error("Chief accountant signature does not exist/ Chữ ký KTT không tồn tại");
 	 				//$(obj).button('reset');
 				}
 				
 			}else if(data.status == "505"){
 				location.href='/login-doanh-nghiep';
 			}
			
 		}).fail(function(data) {
 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
 			$(obj).button('reset');
 		});
    }); 
/* 	async function checkChuKySoKt(obj) {
 	
	} */
	
	 var checkNextCc = 0;
	   $("#fileCompanyCharter").change(function(){
			var formData = new FormData($("#dataGiayDangKyKd")[0]);
			
	 		$.ajax({
	 			url:'/ekyc-enterprise/check-chu-ky-so-charter',
	 		    data: formData,
	 		    type: 'POST',
	 		    processData: false,
	 		    contentType: false
	 		}).done(function(data) {
	 			console.log(data);
	 			if(data.status==200) {
	 				 checkNextCc = 1;
	 				//thongTinBoNhiemKtt = data.data;
					//nextStep(obj);	
					//$(obj).button('reset');
	 			}else if(data.status == 401){
	 				if($("#fileCompanyCharter").val() != ""){
	 					 checkNextCc = 0;
	 	 				toastr.error("Lack of digital signature in Company Charter/ Biên bản Điều Lệ Công Ty thiếu chữ ký");
	 	 				//$(obj).button('reset');
	 				}
	 				
	 			}else if(data.status == "505"){
	 				location.href='/login-doanh-nghiep';
	 			}
				
	 		}).fail(function(data) {
	 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
	 			$(obj).button('reset');;
	 		});
       }); 
/* 	async function checkChuKySoCompanyCharter(obj) {
 	
	} */
	
	 var checkNextFf = 0;
	   $("#fileFatcaForms").change(function(){
			var formData = new FormData($("#dataGiayDangKyKd")[0]);
			
	 		$.ajax({
	 			url:'/ekyc-enterprise/check-chu-ky-so-fatca',
	 		    data: formData,
	 		    type: 'POST',
	 		    processData: false,
	 		    contentType: false
	 		}).done(function(data) {
	 			console.log(data);
	 			if(data.status==200) {
	 				//thongTinBoNhiemKtt = data.data;
					//nextStep(obj);	
					 checkNextFf = 1;
					//$(obj).button('reset');
	 			}else if(data.status == 401){
	 				if($("#fileFatcaForms").val() != ""){
	 					 checkNextFf = 0;
	 	 				toastr.error("Lack of digital signature in Fatca Forms/ Đơn Fatca thiếu chữ ký");
	 	 				//$(obj).button('reset');
	 				}

	 			}else if(data.status == "505"){
	 				location.href='/login-doanh-nghiep';
	 			}
				
	 		}).fail(function(data) {
	 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
	 			$(obj).button('reset');
	 		});
       });
 
		 var checkNextSl = 0;
		   $("#fileInvestmentCertificate").change(function(){
				var formData = new FormData($("#dataGiayDangKyKd")[0]);
				
		 		$.ajax({
		 			url:'/ekyc-enterprise/check-chu-ky-so-share-list',
		 		    data: formData,
		 		    type: 'POST',
		 		    processData: false,
		 		    contentType: false
		 		}).done(function(data) {
		 			console.log(data);
		 			if(data.status==200) {
		 				//thongTinBoNhiemKtt = data.data;
						//nextStep(obj);	
						 checkNextSl = 1;
						//$(obj).button('reset');
		 			}else if(data.status == 401){
		 				if($("#fileInvestmentCertificate").val() != ""){
		 					checkNextSl = 0;
		 	 				toastr.error("Lack of digital signature in Shareholder list/ Đơn Danh sách cổ đông thiếu chữ ký");
		 	 				//$(obj).button('reset');
		 				}

		 			}else if(data.status == "505"){
		 				location.href='/login-doanh-nghiep';
		 			}
					
		 		}).fail(function(data) {
		 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
		 			$(obj).button('reset');
		 		});
	       });
	 
	var token = "";
	function uploadDuLieuStep2(obj) {
	
		$(obj).button('loading');
		var data = {
			//step1
			"fileBusinessRegistration": 	$("#base64FileDangKyKinhDoanh").val(),
			"fileAppointmentOfChiefAccountant": 	$("#base64FileQuyetDinhBoNhiemKtt").val(),
			"fileBusinessRegistrationCertificate": 	$("#base64FileBusinessRegistrationCertificate").val(),
			"fileDecisionToAppointChiefAccountant":   $("#base64FileDecisionToAppointChiefAccountant").val(),
			"fileInvestmentCertificate": 	$("#base64FileInvestmentCertificate").val(),
			"fileCompanyCharter": 	$("#base64FileCompanyCharter").val(),
			"fileSealSpecimen": 	$("#base64FileSealSpecimen").val(),
			"fileFatcaForms": 	$("#base64FileFatcaForms").val(),
			"fileOthers": 	$("#base64FileOthers").val(),
						
		};
		$.ajax({
			url:'/ekyc-enterprise/luu-thong-tin-step2',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			if(data.status == 200) {
				token = data.token;
				console.log(checkNextKt);

				nextStep(obj);
				
				
				$(obj).button('reset');
			} else if(data.status == 505){
				location.href='/login-doanh-nghiep';
			}else if(data.status == 500){
				location.href='/login-doanh-nghiep';
			} else {
				toastr.error("Not enough information to store / Không đủ thông tin cần lưu trữ");
				$(obj).button('reset');	
			}
		}).fail(function(data) {
			//toastr.error("Error check / Lỗi lưu thông tin");
			$(obj).button('reset');
			location.href='/login-doanh-nghiep';
		}); 
	}
	
</script>