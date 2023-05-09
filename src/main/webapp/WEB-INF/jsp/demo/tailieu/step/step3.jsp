<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-3">
   <div class="panel-heading">
		<h3 class="panel-title">Xác định người ký</h3>
	</div>
	<div class="panel-body">
		<div id="divTemplateNddpl">
			
		
			
			<div style="margin-bottom: 10px;" id="templateNddpl">
			
			
				<div class="row" >
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Full Name<br/><i style="font-weight: normal;">Họ và Tên</i></label>
							<input type="text" class="form-control input-sm" name="hoVaTenNddpl" id="hoVaTenNddpl" value="<c:out value='${item.hoTen}'/>"/>
							<input type="hidden" class="form-control input-sm" name="idNddpl" id="idNddpl" value="<c:out value='${item.id}'/>"/>
						</div>
					</div>	
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Mobile Number<br/><i style="font-weight: normal;">Số Điện Thoại</i></label>
							<input type="text" class="form-control input-sm" name="soDienThoaiNddpl" id="soDienThoaiNddpl" value="<c:out value='${item.phone}'/>"/>
						</div>
					</div>	
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Email<br/><i style="font-weight: normal;">Thư Điện Tử</i></label>
							<input type="text" class="form-control input-sm" name="emailNddpl" id="emailNddpl" value="<c:out value='${item.email}'/>"/>
						</div>
					</div>	
					

					<div class="form-group col-sm-1 delete">
							<button  type="button" style="margin-top: 20px;" id="boTempalteNddpl" onclick="remove3(this)"><i  class="fa fa-minus minus" aria-hidden="true" ></i></button>
		            </div>
				</div>
			

			
				
			</div>
		</div>
		<div>
		</div>
		<button  type="button" style="margin-bottom: 10px;" id="themTempalteNddpl"><i class="fa fa-plus" aria-hidden="true"></i></button><br>
		
        <button class="btn btn-default btn-sm" type="button" onclick="prevStep(this)">Quay lại</button>
		<button class="btn btn-primary nextBtn pull-right btn-sm" type="button"onclick="validateStep3Start(this)">Tiếp tục</button>
	</div>
</div>
<script type="text/javascript">

function validateStep3Start(obj) {
		
		if(validateThongTin("Nddpl")){
			nextStep(obj);
		}
	
	
		
}


$("#themTempalteNddpl").click(function(){
	if($("#divTemplateAdd .row").length < 6) {
		$("#divTemplateNddpl").append($("#templateNddpl .row").clone());

		$("input[name='idNddpl']").last().val("");
		$("input[name='hoVaTenNddpl']").last().val("");
		$("input[name='soDienThoaiNddpl']").last().val("");
		$("input[name='emailNddpl']").last().val("");
	}
	// $("input[name='checkMainNddpl']").last().prop("checked", false ); 
});


function remove3(obj) {
		if($("#divTemplateNddpl .row").length > 1){
			for(i=1; i<=$("#divTemplateNddpl .row").length; i++) {
				
				var temp = $(obj).parent().parent().parent().attr("id");
				
				$(obj).parent().parent().remove();
				$("input[name='checkMainNddpl']").prop("checked", true );
				if(temp == "templateNddpl") {
					$("#templateNddpl").append($("#divTemplateNddpl .row").get(0));
				}
				
			}
		}
		
	}


var checkEmail = false;
function validateThongTin(sub) {
	var check = 0;
	
	$("input[name='hoVaTen"+sub+"']").each(function(){
		if($(this).val() == "") {
			check ++;
			toastr.error("Lack of Full Name/ Thiếu Họ và tên đầy đủ");
		}
	});
	$("input[name='soDienThoai"+sub+"']").each(function(){
		if($(this).val() == "") {
			check ++;
			toastr.error("Lack of Phone number/ Thiếu Số Điện thoại");
		}
	});
	$("input[name='email"+sub+"']").each(function(){
		if(!validateEmail( $(this).val())) {
			check ++;
			toastr.error("Email invalid/ email không hiệu lực");
		}
	});
	
	
	
	
	if(check > 0){
		
		return false;
	}
	return true;
}

</script>