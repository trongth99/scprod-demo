<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>




   
<script src="${contextPath }/css/bootstrap.3.4.0.css"></script>
<script src="${contextPath }/js/jquery.3.2.1.js"></script>
<script src="${contextPath }/js/bootstrap.3.4.1.js"></script> 


<%@include file="../../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<script type="text/javascript">
$(document).ready(function(){


	var myAlert = document.getElementById('checksum').value;
	console.log(myAlert);
	//alert($("#success").val());

	console.log($("#checksum").val())
	 if($("#checksum").val() == "yes") {	
		 $('#alert').show();
		 setTimeout(function () {

	            $('#alert').alert('close');
	        }, 4000);

    }
    if($("#checksum").val() == "no") {
    
    	 $('#alert1').show();
    	
		 setTimeout(function () {
	            $('#alert1').alert('close');
	        }, 4000);
    } 
    
   
});
</script>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2><spring:message code="ekycdn.thong_tin_doanh_nghiep" /></h2>
		<div class="pull-right">
		<input type="hidden" class="checksum" value="${checksum }" id="checksum"/>
       
         <div class="row">
        <div  id="alert" class="alert alert-success" style="display: none;">
 
                Data is not changed / dữ liệu không bị thay đổi
         
        </div>
        
        <div  id="alert1" class="alert alert-danger " style="display: none;">
                Data is changed / dữ liệu bị thay đổi
        
        </div>
       </div>
	</div>
	</div>
	

	<div class="modal-body">
		<div class="nav-tabs-custom">
			<ul class="nav nav-tabs">
				<li class="active">
					<a href="#tab_1" data-toggle="tab">
						<spring:message code="ekycdn.thong_tin_doanh_nghiep" />
					</a>
				</li>
				<li>
					<a href="#tab_2" data-toggle="tab">
						<spring:message code="ekycdn.nguoi_dai_dien_phap_luat" />
						<c:if test="${checkLegalRepFinish eq true}">
						<i class="fa fa-check-circle" style="color: green;"></i>
						</c:if>
						<c:if test="${checkLegalRepFinish eq false}">
						<i class="fa fa-times-circle" style="color:red"></i>
						</c:if>
					</a>
				</li>
				<li>
					<a href="#tab_3" data-toggle="tab">
						<spring:message code="ekycdn.thong_tin_ke_toan_truong" />
						<c:if test="${checkChiefAccFinish eq true}">
						<i class="fa fa-check-circle" style="color: green;"></i>
						</c:if>
						<c:if test="${checkChiefAccFinish eq false}">
						<i class="fa fa-times-circle" style="color:red"></i>
						</c:if>
					</a>
				</li>
				<li>
				 <c:if test="${ekycDoanhNghiep.allInOne eq 'no'}">
					<a href="#tab_4" data-toggle="tab">
						<spring:message code="ekycdn.nguoi_uy_quyen" />
						<c:if test="${checkPAuthorChiefFinish eq true}">
						<i class="fa fa-check-circle" style="color: green;"></i>
						</c:if>
						<c:if test="${checkPAuthorChiefFinish eq false}">
						<i class="fa fa-times-circle" style="color:red"></i>
						</c:if>
					</a>
					</c:if>
				</li>
				<li>
					<a href="#tab_5" data-toggle="tab">
						<spring:message code="ekycdn.ban_lanh_dao" />
						<c:if test="${checkListOfLeadersFinish eq true}">
						<i class="fa fa-check-circle" style="color: green;"></i>
						</c:if>
						<c:if test="${checkListOfLeadersFinish eq false}">
						<i class="fa fa-times-circle" style="color:red"></i>
						</c:if>
					</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active " id="tab_1">
					<%@include file="chitiet/chiTietDoanhNghiep.jsp"%>
				</div>
				<div class="tab-pane" id="tab_2">
					<%@include file="chitiet/nguoiDaiDienPhapLuat.jsp"%>
				</div>
				<div class="tab-pane" id="tab_3">
					<%@include file="chitiet/keToanTruong.jsp"%>
				</div>
				
				<div class="tab-pane" id="tab_4">
					<%@include file="chitiet/nguoiDuocUyQuyen.jsp"%>
				</div>
				
				<div class="tab-pane" id="tab_5">
					<%@include file="chitiet/banLanhDao.jsp"%>
				</div>
			</div>
			<div style="clear: both; margin-top: 10px;"></div>
		</div>
		
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i>
				<span><spring:message code="thoat" /></span>
			</button>
		</div>
		<div style="clear: both;margin-top: 20px;"></div>
	</div>
	
</div>

<%@include file="../../layout/footerAjax.jsp"%>

