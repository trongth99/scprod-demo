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
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<c:if test="${empty imageLogo }">
<link rel='SHORTCUT ICON' href='${contextPath }/img/logoscnew.ico' sizes="62x62" type='image/x-icon'  />
</c:if>  
<link href="${contextPath }/css/style.css" rel="stylesheet">

<link rel="stylesheet" href="${contextPath }/plugins/toastr/toastr.min.css">
<script src="${contextPath }/plugins/toastr/toastr.min.js"></script>
<script src="${contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<link href="${contextPath}/plugins/sweetalert/sweetalert.css" rel="stylesheet" />
<script src="${contextPath}/plugins/sweetalert/sweetalert.min.js"></script>

<!------ Include the above in your HEAD tag ---------->
</head>
<body>
 <div class="container">
    <div class="mr-3">
			 <img style="width: 185px;height: 95px;" src="${contextPath}/img/logosc%2031.png" alt="Logo" class="brand-image elevation-3"> 
    </div>      
	<div class=" register">
 
		<div class="row">
		
<%--          <div class="pull-left">
			 <img style="width: 190px;height: 90px;margin-top: -230px; margin-left: -45px;" src="${contextPath}/img/LogoSC1.png" alt="Logo" class="brand-image elevation-3"> 
		</div>   --%>