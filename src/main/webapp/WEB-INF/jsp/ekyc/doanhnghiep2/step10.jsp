<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-10">
	<div class="panel-heading">
		<h3 class="panel-title">Special Instruction/ <i>Chỉ Dẫn Đặc Biệt</i></h3>
	</div>
	<div class="panel-body">
		<div class="form-group text-center">
		
			<div class="row">
				<div class="form-group col-sm-12">
					<div class="form-group  has-feedback">
						<label class="control-label">Special Instructions <br/> <i style="font-weight: normal;">Chỉ Dẫn Đặc Biệt</i></label>
						<input type="text" class="form-control input-sm" name="specialInstructions" id="specialInstructions" value="<c:out value='${ekycDoanhNghiep.specialInstructions}'/>"/>
					</div>
				</div>
			</div>	
			<button class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep7Start(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />" id="step10">Save/ <i>Lưu</i></button>
		</div>
	</div>
</div>
<script type="text/javascript">
function validateStep7Start(obj) {
	
		uploadDuLieuStep9(obj);
		//nextStep(obj);
	
}
var token = "";
function uploadDuLieuStep9(obj) {
	$(obj).button('loading');
	var data = {
		
		//step3
		
		
		"specialInstructions": 	$("#specialInstructions").val(),
	};
	$.ajax({
		url:'/ekyc-enterprise/step10',
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
			location.href='/ekyc-enterprise';
		} else {
			toastr.error("Not enough information to store / Không đủ thông tin cần lưu trữ");
			$(obj).button('reset');	
		}
	}).fail(function(data) {
		toastr.error("Error check / Lỗi lưu thông tin");
		$(obj).button('reset');
	}); 
}



</script>