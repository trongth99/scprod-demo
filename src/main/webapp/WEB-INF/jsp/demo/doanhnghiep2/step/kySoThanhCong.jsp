<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-9 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Signed complete</h3>
			<div class="row register-form">
				<form action="${contextPath }/ekyc-enterprise/ekyc/step4" style="width: 100%;" method="post" id="submitForm" enctype='multipart/form-data'>
					<div class="col-sm-12">
						<h2>Infomation</h2>
						<hr/>
						<iframe id="base64File" style="width: 100%; height: 500px; border: 0;"></iframe>
					</div>
				</form>
			</div>
			<button class="btnRegister" type="button"  style="background: #CCC;color: black;width: 110px;" onclick="javascript:location.href='${contextPath }/login-doanh-nghiep?<c:out value="${params.queryParams }"></c:out>'">Succesfully save</button>
			
		</div>
	</div>
</div>
<script>
var contentType = 'application/pdf';
var b64Data = '${file }';

var blob = b64toBlob(b64Data, contentType);
var blobUrl = URL.createObjectURL(blob);

$(document).ready(function(){
	$("#base64File").attr("src", blobUrl);	
	location.href='${contextPath }/login-doanh-nghiep';
});
</script>

<%@include file="../layout/footer.jsp"%>
			
