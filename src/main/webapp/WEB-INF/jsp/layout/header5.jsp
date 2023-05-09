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


<%-- <link rel="stylesheet" href="${contextPath }/css/fonts.css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> --%>
<script src="${contextPath }/plugins/jquery/jquery.min.js"></script>

<!-- Font Awesome -->
<link rel="stylesheet" href="${contextPath }/css/lte2/bower_components/font-awesome/css/font-awesome.min.css">

<link rel="stylesheet" href="${contextPath }/css/midpoint.css">
<link rel="stylesheet" href="${contextPath }/plugins/toastr/toastr.min.css">
<link rel="stylesheet" href="${contextPath }/plugins/pace-progress/themes/black/pace-theme-flat-top.css">


<script src="${contextPath }/plugins/toastr/toastr.min.js"></script>
<link href="${contextPath }/plugins/sweetalert/sweetalert.css" rel="stylesheet" />
<script src="${contextPath }/plugins/sweetalert/sweetalert.min.js"></script>




