<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><spring:message code="title" text="Quản trị"/></title>
<c:if test="${empty imageLogo }">
<link rel='SHORTCUT ICON' href='${contextPath }/img/anhlogoSc.png' type='image/x-icon' />
</c:if>  
<link rel="stylesheet" href="${contextPath }/plugins/toastr/toastr.min.css">
 <script src="${contextPath }/plugins/toastr/toastr.min.js"></script>
<script src="${contextPath }/plugins/jquery/jquery.min.js"></script>
<script src="${contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${contextPath }/css/lte2/dist/css/AdminLTE.min.css">
<%@include file="../layout/header4.jsp"%>
<%@include file="../layout/js.jsp"%>
</head>

<style>
 label.error{
    color:red;
    margin-left:5px;
    display:inline;
}

.register-page{
background: white;
}
   
</style>

<body class="hold-transition register-page">

	<div class="register-box" style="width: 60%;">
        <div class="pull-left">
			 <img style="width: 185px;height: 95px;margin-top: -50px;" src="${contextPath}/img/logosc%2031.png" alt="Logo"> 
		</div> 
		<div 
			style="background: white; ">
			<form action="${contextPath }/doanh-nghiep/change-pass" style="width: 100%;" method="post" id="submitForm" enctype='multipart/form-data'>
				<div class="  setup-content">


					<div class="col-lg-12   " style="background-color: white; border: 2px solid #ccc;">
						<div class="modal-header">
							<h2>Change Password/ <i>Đổi Mật Khẩu</i>
							</h2>
						</div>
						<div class="modal-body">
							<div class="row clearfix">
								<input type="hidden" class="form-control" value="<c:out value='${business.id }'/>" name="id" id="id"/>
								
								
								<div class="col-md-12 mb-0">
									<label class="form-label">Old Password/ <i>Mật Khẩu Cũ</i> *</label>
									<div class="form-group form-float">
										<div class="form-line">
											<input type="password" class="form-control" value=""
												name="old-password">
										</div>
									</div>
								</div>
								
								<div class="col-md-12 mb-0">
									<label class="form-label">New Password/ <i>Mật Khẩu Mới</i> *</label>
									<div class="form-group form-float">
										<div class="form-line">
											<input type="password" class="form-control" value=""
												 name="new-password" >
										</div>
									</div>
								</div>
								
								
								<div class="col-md-12 mb-0">
									<label class="form-label">Re-Enter Password/ <i>Nhập Lại Mật Khẩu</i> *</label>
									<div class="form-group form-float">
										<div class="form-line">
											<input type="password" class="form-control" value=""
												name="re-password" >
										</div>
									</div>
								</div>
								
								<div class="col-md-12 mb-0 text-right">
									
									<button type="submit" class="btn btn-primary btn-sm">
										<i class="fa fa-save"></i> <span>Save/ <i>Lưu</i></span>
									</button>
									<a href="${contextPath}/login-doanh-nghiep" class="btn btn-default m-t-15 btn-sm">Back/ <i>Quay lại</i></a>
								</div>
							</div>
							<div class="pull-left">
					            <ol>
				                    <li>Password length must be 8 - 12 characters/ <i>Độ dài Mật khẩu cho phép từ 8-12 ký tự.</i> </li>
				                    <li>Password must contain uppercase, lowercase letters, numbers and special characters/ <i>Mật khẩu phải chứa chữ hoa, chữ thường, số và ký tự đặc biệt.</i></li>
				                    <li>Password shall not allow to be the same as user account name/ <i>Mật khẩu không được phép giống với tên tài khoản người dùng </i></li>
			                    </ol>
	                        </div>
	                        </br>
	                        
	                        <div class="pull-left"> *: Mandatory/ <i>Bắt buộc</i></div>
						</div>
					</div>
				</div>
			</form>

		</div>

	</div>




</body>
</html>

<script type="text/javascript" >
$(document).ready(function() {

	$("#submitForm").validate({
		rules: {
			"old-password": {
				required: true
			
			},
			"new-password": {
				required: true
			},
			"re-password": {
				required: true
				
			}
		},
		messages: {
			"old-password": {
				required: '<spring:message code="bat_buoc_nhap_mat_khau_cu" />'
			},
			"new-password": {
				required: '<spring:message code="bat_buoc_nhap_mat_khau_moi" />'
			},
			"re-password": {
				required: '<spring:message code="nhap_lai_mat_khau_moi" />'
			}
		}, 
        errorPlacement: function(error, element) {
        	$(element).parent().parent().append(error);
         }
	});
});
/* $(document).ready(function() {
	
	
		  rules: {
			  passwordOld : "required",
			  newPassrord : "required",
	          rePassword : "required"

	        },
		messages : {

			passwordOld :  'Old Password is not empty',
			newPassrord : 'New Password is not empty',
			rePassword :  'Re_Password is not empty'
			
		}
	});	 */				      
</script>
