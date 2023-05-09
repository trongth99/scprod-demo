
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title> Business Banking | Log in</title>
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<c:if test="${empty imageLogo }">
<link rel='SHORTCUT ICON' href='${contextPath }/img/logoscnew.ico' sizes="62x62" type='image/x-icon'  />
</c:if>  
<!-- Font Awesome -->
<link rel="stylesheet" href="${contextPath}/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet" href="${contextPath}/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${contextPath}/css/adminlte.min.css">
<%@include file="./layout/header5.jsp"%>
<%@include file="./layout/js2.jsp"%>
<!-- Google Font: Source Sans Pro -->
</head>

<body class="hold-transition login-page" style="background: white;">
	<div class="container">
	     <div  style="margin-top: -240px;">
					<div class="pull-left">
						 <img style="width: 185px;height: 95px;" src="${contextPath}/img/logosc%2031.png" alt="Logo" class="brand-image "> 
					</div> 
	  
	    </div>
	</div>
	<div class="login-box">
		<div class="login-logo">
			<a href="${contextPath}/">
				<b> Business Banking</b>
			</a>
		</div>
		<!-- /.login-logo -->
		<div class="card">
			<div class="card-body login-card-body">
				<c:if test="${not empty message }">
					<p class="login-box-msg" style="color: red;">${message }</p>
				</c:if>
				<form id="sign_in" method="POST" action="${contextPath}/login-doanh-nghiep">
					<div class="input-group mb-3">
						<input type="text" class="form-control" name="username" id="username" placeholder="User name/ Tên đăng nhập" required autofocus>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-user"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="password" class="form-control" name="password" placeholder="Password/ Mật khẩu" required>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-lock"></span>
							</div>
						</div>
					</div>
					<div class="row">
						
						<!-- /.col -->
						<div class="col-7">
							<button type="submit" data-toggle="modal" data-target="#largeModal" class="btn btn-primary btn-block" style="background-color: #0473ea">Log In/ <i>Đăng Nhập</i></button>
						</div>
						<!-- /.col -->
					</div>
				</form>
			</div>
			<!-- /.login-card-body -->
		</div>
	</div>
	<!-- /.login-box -->

	<!-- jQuery -->
	<script src="${contextPath}/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap 4 -->
	<script src="${contextPath}/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE App -->
	<script src="${contextPath}/js/adminlte.min.js"></script>

</body>
</html>
