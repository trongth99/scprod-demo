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
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><spring:message code="title" text="Quản trị"/></title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<c:if test="${empty imageLogo }">
<link rel='icon' href='${contextPath }/img/logobe3.ico'  type='image/x-icon'/>
</c:if>  
<%-- <link rel="stylesheet" href="${contextPath }/css/fonts.css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> --%>
<script src="${contextPath }/plugins/jquery/jquery.min.js"></script>
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/bootstrap/dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/font-awesome/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/Ionicons/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${contextPath }/css/select2.min.css">
<link rel="stylesheet" href="${contextPath }/css/lte2/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${contextPath }/css/lte2/dist/css/skins/skin-blue-light.min.css">

<link rel="stylesheet" href="${contextPath }/css/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="${contextPath }/css/midpoint.css">
<link rel="stylesheet" href="${contextPath }/plugins/toastr/toastr.min.css">
<link rel="stylesheet" href="${contextPath }/plugins/pace-progress/themes/black/pace-theme-flat-top.css">


<script src="${contextPath }/plugins/toastr/toastr.min.js"></script>
<link href="${contextPath }/plugins/sweetalert/sweetalert.css" rel="stylesheet" />
<script src="${contextPath }/plugins/sweetalert/sweetalert.min.js"></script>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

<!-- Google Font -->
<script src="${contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">

