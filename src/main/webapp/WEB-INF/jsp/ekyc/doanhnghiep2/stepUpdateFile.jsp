<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-1">
	<div class="panel-heading">
		<h3 class="panel-title"><spring:message code="ekycdn.upload_document" /></h3>
	</div>
	<div class="panel-body">
		<form id="dataGiayDangKyKd" method="post" enctype="multipart/form-data">
			<input type="hidden" value="dn" name="loaiGiayTo" id="loaiGiayTo"/>
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileDangKyKinhDoanh" id="fileDangKyKinhDoanh" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFile" id="tenFileDangKyKinhDoanh" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileDangKyKinhDoanh').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                <span style="font-style: oblique;">Enterprise registration certificate/ Giấy chứng nhận đăng ký kinh doanh</span> <span style="color: red;">(*)</span>                                     
	            </button>
	            <a href="javascript:void(0)" id="fileDangKyKinhDoanhDelete">Remove/Xóa</a>
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
	                <span style="font-style: oblique;">Chief accountant/PIC of accounting appointment / Giấy bổ nhiệm kế toán trưởng/ người phụ trách kế toán</span> <span style="color: red;">(*)</span>                                     
	            </button>
	            <a href="javascript:void(0)" id="fileQuyetDinhBoNhiemKttDelete">Remove/Xóa</a>
	            <small id="nameFileQuyetDinhBoNhiemKtt" style="display: none;"></small><br/>
	            <iframe id="base64FileQuyetDinhBoNhiemKttImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileAppointmentOfChiefAccountant }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileAppointmentOfChiefAccountant }&token=${token}"></iframe>
				<textarea id="base64FileQuyetDinhBoNhiemKtt" style="display: none;"><c:out value="${fileAppointmentOfChiefAccountantBase64 }"/></textarea>
			</div>
		
		<div class="form-group text-left">
			<input type="file" class="form-control-file" name="fileCompanyCharter" id="fileCompanyCharter" accept=".pdf" style="display: none;"/>
			<input type="hidden" class="form-control-file" name="tenFileCompanyCharter" id="tenFileCompanyCharter" style="display: none;"/>
			<button class="btn btn-default btn-lg" onclick="document.getElementById('fileCompanyCharter').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
                <span class="glyphicon glyphicon-cloud-upload"></span>
                <span style="font-style: oblique;">Company’s charter/ Điều lệ công ty</span>  <span style="color: red;">(*)</span>                                
            </button>
            <a href="javascript:void(0)" id="fileCompanyCharterDelete">Remove/Xóa</a>
            <small id="nameFileCompanyCharter" style="display: none;"></small><br/>
            <iframe id="base64FileCompanyCharterImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileCompanyCharter }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileCompanyCharter }&token=${token}"></iframe>
			<textarea id="base64FileCompanyCharter" style="display: none;"><c:out value="${fileCompanyCharterBase64 }"/></textarea>
		</div>
		<div class="form-group text-left">
			<input type="file" class="form-control-file" name="fileFatcaForms" id="fileFatcaForms" accept=".pdf" style="display: none;"/>
			<input type="hidden" class="form-control-file" name="tenFileFatcaForms" id="tenFileFatcaForms" style="display: none;"/>
			<button class="btn btn-default btn-lg" onclick="document.getElementById('fileFatcaForms').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
                <span class="glyphicon glyphicon-cloud-upload"></span>
                <spring:message code="ekycdn.bieu_mat_fatcas" /> <span style="font-style: oblique;">Các biểu mẫu FATCA</span> <span style="color: red;">(*)</span>                                  
            </button>
            <a href="javascript:void(0)" id="fileFatcaFormsDelete">Remove/Xóa</a>
            <small id="nameFileFatcaForms" style="display: none;"></small><br/>
            <iframe id="base64FileFatcaFormsImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileFatcaForms }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileFatcaForms }&token=${token}"></iframe>
			<textarea id="base64FileFatcaForms" style="display: none;"><c:out value="${fileFatcaFormsBase64 }"/></textarea>
		</div>
		</form>
		  	<input type="hidden" name="typeDocument" id="typeDocument" value="<c:out value='${ekycDoanhNghiep.typeDocument}'/>"/>
		
	<div id="ctyvn" style="display: none">
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                Shareholder list <span style="font-style: oblique;">/ Danh sách cổ đông</span>                                
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
	                Tax certificate (if applicable)    <span style="font-style: oblique;">/ Giấy chứng nhận mã số thuế</span>                         
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
	                <spring:message code="ekycdn.khac" />  <span style="font-style: oblique;">Khác</span>                                 
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
	                Letter of undertaking and indemnity regarding capital account <span style="font-style: oblique;">/ Thư cam kết và bồi hoàn liên quan đến tài khoản vốn</span>                                
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
	                Investment registration certificate (if applicable)<span style="font-style: oblique;">/Giấy chứng nhận đầu tư (nếu có)</span>                         
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
	                Shareholder list <span style="font-style: oblique;">/ Danh sách cổ đông</span>                                
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
	                Tax certificate (if applicable)    <span style="font-style: oblique;">/ Giấy chứng nhận mã số thuế</span>                         
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
	                <spring:message code="ekycdn.khac" />  <span style="font-style: oblique;">Khác</span>                                 
	            </button>
	            
	            <small id="nameFileOthers" style="display: none;"></small><br/>
	            <iframe id="base64FileOthersImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileOthers }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileOthers }&token=${token}"></iframe>
				<textarea id="base64FileOthers" style="display: none;"><c:out value="${fileOthersBase64 }"/></textarea>
		    </div> 
		</div>
			<div id ="ctnho" style="display: none">
		
			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileInvestmentCertificate" id="fileInvestmentCertificate" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileInvestmentCertificate" id="tenFileInvestmentCertificate" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileInvestmentCertificate').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                Declaration on micro SME<span style="font-style: oblique;">/ Tờ khai xác định doanh nghiệp siêu nhỏ*</span>                                
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
	                Investment registration certificate (if applicable)<span style="font-style: oblique;">/Giấy chứng nhận đầu tư (nếu có)</span>                         
	            </button>
	            
	            <small id="nameFileSealSpecimen" style="display: none;"></small><br/>
	            <iframe id="base64FileSealSpecimenImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileSealSpecimen }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileSealSpecimen }&token=${token}"></iframe>
				<textarea id="base64FileSealSpecimen" style="display: none;"><c:out value="${fileSealSpecimenBase64 }"/></textarea>
			</div>
			

			<div class="form-group text-left">
				<input type="file" class="form-control-file" name="fileSealSpecimen" id="fileSealSpecimen" accept=".pdf" style="display: none;"/>
				<input type="hidden" class="form-control-file" name="tenFileSealSpecimen" id="tenFileSealSpecimen" style="display: none;"/>
				<button class="btn btn-default btn-lg" onclick="document.getElementById('fileSealSpecimen').click(); this.blur();" style="width: 100%; text-align: left;" type="button">
	                <span class="glyphicon glyphicon-cloud-upload"></span>
	                Tax certificate (if applicable)    <span style="font-style: oblique;">/ Giấy chứng nhận mã số thuế</span>                         
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
	                <spring:message code="ekycdn.khac" />  <span style="font-style: oblique;">Khác</span>                                 
	            </button>
	            
	            <small id="nameFileOthers" style="display: none;"></small><br/>
	            <iframe id="base64FileOthersImg" style="width: 100%; height: 400px; border: 0;<c:if test="${empty fileOthers }">display: none;</c:if>" src="${contextPath }/ekyc-enterprise/pdf-byte?path=${fileOthers }&token=${token}"></iframe>
				<textarea id="base64FileOthers" style="display: none;"><c:out value="${fileOthersBase64 }"/></textarea>
		    </div> 
		
		</div>
<!-- 		<span style="font-weight: bold;color: red;"><spring:message code="ekycdn.chu_y" /></span> -->
		<button class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep1(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />"><spring:message code="ekycdn.tiep_theo" /></button>
	</div>
</div>

<script type="text/javascript">
	var changeFileDangKyKinhDoanh = false;
	function setCookie(cname, cvalue, exdays) {
		  var d = new Date();
		  d.setTime(d.getTime() + (exdays*24*60*60*1000));
		  var expires = "expires="+ d.toUTCString();
		  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}
	$(document).ready(function(){
		console.log($("#typeDocument").val());
		if($("#typeDocument").val() == "1"){
			
			$("#ctyvn").show();
		}else{
			$("#ctyvn").hide();
		}
		
		if($("#typeDocument").val() == "2"){
			
			$("#ctyncngoai").show();
		
		}else{
			$("#ctyncngoai").hide();
			
		}
		if($("#typeDocument").val() == "3"){
			$("#ctnho").show();
			
		}else {
			$("#ctnho").hide();
		
		}
		
		
		addEventAllFile("fileDangKyKinhDoanh", "base64FileDangKyKinhDoanh", "nameFileDangKyKinhDoanh");
		addEventAllFile("fileQuyetDinhBoNhiemKtt", "base64FileQuyetDinhBoNhiemKtt", "nameFileQuyetDinhBoNhiemKtt");
		addEventAllFile("fileBusinessRegistrationCertificate", "base64FileBusinessRegistrationCertificate", "nameFileBusinessRegistrationCertificate");
		addEventAllFile("fileDecisionToAppointChiefAccountant", "base64FileDecisionToAppointChiefAccountant", "nameFileDecisionToAppointChiefAccountant");
		addEventAllFile("fileInvestmentCertificate", "base64FileInvestmentCertificate", "nameFileInvestmentCertificate");
		addEventAllFile("fileCompanyCharter", "base64FileCompanyCharter", "nameFileCompanyCharter");
		addEventAllFile("fileSealSpecimen", "base64FileSealSpecimen", "nameFileSealSpecimen");
		addEventAllFile("fileFatcaForms", "base64FileFatcaForms", "nameFileFatcaForms");
		addEventAllFile("fileOthers", "base64FileOthers", "nameFileOthers");
		
		$("#fileDangKyKinhDoanh").change(function(){
			changeFileDangKyKinhDoanh = true;
		});
		
	
		    checkNextKtUpdate = 1 ;
	
			checkNextCcUpdate = 1;

			checkNextFfUpdate = 1; 
			   
		
		$("#fileOthersDelete").click(function(){
			remove("base64FileOthersImg","nameFileOthers","base64FileOthers");
		});
		$("#fileFatcaFormsDelete").click(function(){
			remove("base64FileFatcaFormsImg","nameFileFatcaForms","base64FileFatcaForms");
		});
		$("#fileDangKyKinhDoanhDelete").click(function(){
			remove("base64FileDangKyKinhDoanhImg","nameFileDangKyKinhDoanh","base64FileDangKyKinhDoanh");
		});
		$("#fileQuyetDinhBoNhiemKttDelete").click(function(){
			remove("base64FileQuyetDinhBoNhiemKttImg","nameFileQuyetDinhBoNhiemKtt","base64FileQuyetDinhBoNhiemKtt");
		});
		$("#fileInvestmentCertificateDelete").click(function(){
			remove("base64FileInvestmentCertificateImg","nameFileInvestmentCertificate","base64FileInvestmentCertificate");
		});
		$("#fileCompanyCharterDelete").click(function(){
			remove("base64FileCompanyCharterImg","nameFileCompanyCharter","base64FileCompanyCharter");
		});
		$("#fileSealSpecimenDelete").click(function(){
			remove("base64FileSealSpecimenImg","nameFileSealSpecimen","base64FileSealSpecimen");
		});
	});
	
	
	function remove(iframeImg, nameFile, textarea) {
		$("#"+iframeImg).attr("src", "");
		$("#"+iframeImg).css("display", "none");
		$("#"+nameFile).css("display", "none");
		$("#"+textarea).html("");
	}
	
	function validateStep1(obj) {
		if ($("#base64FileDangKyKinhDoanh").html() == "") {
			toastr.error("Upload certificate of business registration");
			return false;
		} if ($("#base64FileQuyetDinhBoNhiemKtt").html() == "") {
			toastr.error("Upload Decision on appointment of chief accountant");
			return false;
		} if ($("#base64FileCompanyCharter").html() == "") {
			toastr.error("Upload Company charter");
			return false;
		} if ($("#base64FileFatcaForms").html() == "") {
			toastr.error("Upload FATCA forms");
			return false;
		} else {
			
			if(checkNextKtUpdate == 1 &&  checkNextCcUpdate == 1 &&  checkNextFfUpdate == 1){
				var file = document.querySelector('#fileDangKyKinhDoanh').files[0];
				var base64 = $("#base64FileDangKyKinhDoanh").html();
				
				var fileQuyetDinhBoNhiemKtt = document.querySelector('#fileQuyetDinhBoNhiemKtt').files[0];
				var base64FileQuyetDinhBoNhiemKtt = $("#base64FileQuyetDinhBoNhiemKtt").html();
				
				if(base64 != "" && base64FileQuyetDinhBoNhiemKtt != null) {
					$(obj).button('loading');
					if(changeFileDangKyKinhDoanh) {
						docThongTinDoanhNghiep(obj);
					} else {
						luuThongTinFile(obj);
					}
				} else {
					toastr.warning("Loading file, please wait.")
				}
			}
			
		}
		return true;
	}
	
	function luuThongTinFile(obj) {
		if(changeFileDangKyKinhDoanh) {
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
					
					//step2
					"nameOfTheAccountHolder": 	$("#nameOfTheAccountHolder").val(),
					"number": 	$("#number").val(),
					"dateAccountOpening": 	$("#dateAccountOpening").val(),
					"nameCompany": 	$("#nameCompany").val(),
					"registeredAddress": 	$("#registeredAddress").val(),
					"operatingAddress": 	$("#operatingAddress").val(),
					"countryOfIncorporation": 	$("#countryOfIncorporation").val(),
					"registrationNumber": 	$("#registrationNumber").val(),
					"straight2BankGroupID": 	$("#straight2BankGroupID").val(),
					"mailingAddress": 	$("#mailingAddress").val(),
					"swiftBankIDCode": 	$("#swiftBankIDCode").val(),
					"contactPerson": 	$("#contactPerson").val(),
					"emailAddress": 	$("#emailAddress").val(),
					"accountType": 	$("#accountType").val(),
					"currency": 	$("#currency").val(),
					"accountTitle": 	$("#accountTitle").val(),
					"registeringEmailAddress": 	$("#registeringEmailAddress").val(),
					"shortName": 	$("#shortName").val(),
					"nameInEnglish": 	$("#nameInEnglish").val(),
					"faxNumber": 	$("#faxNumber").val(),
					"taxCode": 	$("#taxCode").val(),
					"applicableAccountingSystems": 	$("#applicableAccountingSystems").val(),
					"taxMode": 	$("#taxMode").val(),
					"residentStatus": 	$("#residentStatus").val(),
					"businessActivities": 	$("#businessActivities").val(),
					"yearlyAveragenumber": 	$("#yearlyAveragenumber").val(),
					"totalSalesTurnover": 	$("#totalSalesTurnover").val(),
					"totalCapital": 	$("#totalCapital").val(),
					"companysSealRegistration": 	$("#companysSealRegistration").val(),
					"sampleOfTheSeal": 	$("#sampleOfTheSeal").val(),
					"agreeToReceive": 	$("#agreeToReceive").val(),
					"applicantsRepresentative": 	$("#applicantsRepresentative").val(),
					"relationshipManagerName": 	$("#relationshipManagerName").val(),
				};
		} else {
			var data = {
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
		}
		
		$.ajax({
			url:'/ekyc-enterprise/update-file',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			if(data.status == 200) {
				//nextStep($("#step2"));
				nextStep(obj);
				console.log(1)
			} else {
				toastr.error("Edit file fail");
			}
			
			$(obj).button('reset');
		}).fail(function(data) {
			toastr.error("Error");
			$(obj).button('reset');
		});
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
//					$("#mobileOfficeTelephone").val(data.data.dienThoai);
				$("#emailAddress").val(data.data.email);
				
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
							if(thongTinChuKy[x][y] == "Signature is not exists") thongTinChuKy[x][y] = '<spring:message code="ekycdn.khong_co_chu_ky_so" />';
							table += "<tr><td>"+title+"</td><td>"+thongTinChuKy[x][y]+"</td></tr>";
						}
					}
					table += "</table>";
				} catch (error) {
					 console.error(error);
				}
				if(!checkCks) {
					toastr.error('<spring:message code="ekycdn.khong_co_chu_ky_so" />');
				} else {
					console.log(data.data.maSoThue)
					console.log(cksMst)
					if(data.data.maSoThue == cksMst) {
						docGiayBoNhiemKtt(obj);
						luuThongTinFile(obj);
					} else {
						toastr.error('<spring:message code="ekycdn.mst_khong_dung" />');
					}
				}
				
				
			} else if(data.status == "505") {
				location.href='/ekyc-enterprise';
			} else {
				toastr.error(data.message);
			}
		}).fail(function(data) {
			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
			$(obj).button('reset');;
		});
	}
	///
	var thongTinBoNhiemKtt;
	function docGiayBoNhiemKtt(obj) {
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
			$(obj).button('reset');;
 		}).fail(function(data) {
 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
 			$(obj).button('reset');;
 		});
	}
	
	//
	var checkNextKtUpdate = 0;
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
 				checkNextKtUpdate = 1 ;
 				//thongTinBoNhiemKtt = data.data;
				//nextStep(obj);	
				//$(obj).button('reset');
 			}else if(data.status == 401){
 				 checkNextKtUpdate = 0 ;
 				if($("#fileQuyetDinhBoNhiemKtt").val() != ""){
 	 				toastr.error("Chief accountant signature is not exists");
 	 				//$(obj).button('reset');
 				}
 				
 			}
			
 		}).fail(function(data) {
 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
 			$(obj).button('reset');;
 		});
    });

	///
	var checkNextCcUpdate = 0;
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
	 				 checkNextCcUpdate = 1;
	 				//thongTinBoNhiemKtt = data.data;
					//nextStep(obj);	
					//$(obj).button('reset');
	 			}else if(data.status == 401){
	 				
	 					 checkNextCcUpdate = 0;
	 	 				toastr.error("CompanyCharter signature is not exists");
	 	 				//$(obj).button('reset');
	 				
	 				
	 			}
				
	 		}).fail(function(data) {
	 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
	 			$(obj).button('reset');;
	 		});
       });
/* 	async function checkChuKySoCompanyCharter(obj) {
 	
	} */
	
	var checkNextFfUpdate = 0;
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
					 checkNextFfUpdate = 1;
					//$(obj).button('reset');
	 			}else if(data.status == 401){
	 				
	 					 checkNextFfUpdate = 0;
	 	 				toastr.error(" FatcaForms signature is not exists");
	 	 				//$(obj).button('reset');
	 				

	 			}
				
	 		}).fail(function(data) {
	 			toastr.error("<spring:message code="ekycdn.loi_kiem_tra_thong_tin" />");
	 			$(obj).button('reset');
	 		});
       });
</script>