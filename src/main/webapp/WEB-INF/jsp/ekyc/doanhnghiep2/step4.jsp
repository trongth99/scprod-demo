<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-4">
	<div class="panel-heading">
		<h3 class="panel-title">Chief Accountant or PIC of Accounting Information/ <i>Thông Tin của Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán</i></h3>
	</div>
	<div class="panel-body">
		
		<div  >
			<div id="divTemplateKtt">
				<div style="margin-bottom: 10px;" id="templateKtt">
				<c:forEach items="${chiefAccountant}" var="item" >
				<div class="row">
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Full Name<br/><i style="font-weight: normal;">Họ và Tên</i></label>
								<input type="text" class="form-control input-sm" name="hoVaTenKtt" value="<c:out value='${item.hoTen}'/>"/>
							</div>
						</div>	
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Mobile Number<br/><i style="font-weight: normal;">Số Điện Thoại</i></label>
								<input type="text" class="form-control input-sm" name="soDienThoaiKtt" value="<c:out value='${item.phone}'/>"/>
							</div>
						</div>
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Email<br/><i style="font-weight: normal;">Thư Điện Tử</i></label>
								<input type="text" class="form-control input-sm" name="emailKtt"  value="<c:out value='${item.email}'/>"/>
							</div>
						</div>
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Type<br/><i style="font-weight: normal;">Loại</i></label>
								<select class="form-control input-sm" id="typeKtt" name="typeKtt">
									<option value="Chief Account / Kế toán trưởng" <c:if test="${item.loai eq 'Chief Account/ Kế Toán Trưởng' }">selected="selected"</c:if>>Chief Account/ Kế Toán Trưởng</option>
									<option value="PIC of Accountant / Người phụ trách Kế toán" <c:if test="${item.loai eq 'PIC of Accountant/ Người Phụ Trách Kế Toán' }">selected="selected"</c:if>>PIC of Accountant/ Người Phụ Trách Kế Toán</option>
								</select>
							</div>
						</div>


					</div>
				</c:forEach>
				</div>
					<div class="row">
					<div class="form-group col-sm-8">
						<div class="form-group  has-feedback">
							<label class="control-label">
								
							Is there any authorised person(s) of Chief accountant (Person in Charge of Accounting)?<i style="font-weight: normal;">/ Có người được Kế toán trưởng (Người phụ trách bộ phận kế toán) ủy quyền ký?</i>
							</label>
						</div>
					</div>
					<input type="hidden"  id="xacNhanKtt" value="${haveAChiefAccountant}" />
					<div class="form-group col-sm-2" style="text-align: end;">
					     <input type="radio"  name="xacNhanKtt" id="xacNhanKttYes" style="margin-right: 20px;" />Yes/ <i>Có</i>
					</div>
					<div class="form-group col-sm-2">
					      <input type="radio"  name="xacNhanKtt" id="xacNhanKttNo" style="margin-right: 20px;"checked="checked"/>No/ <i>Không</i>
					</div>
				</div>

				
			</div>
		</div>
		<div class="form-group col-sm-12">
			<button class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep4Start(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />" id="step4"><spring:message code="ekycdn.tiep_theo" /></button>
			<button class="btn btn-default pull-right" type="button" onclick="prevStep(this)" style="margin-right: 10px;"><spring:message code="ekycdn.quay_lai" /></button>
		</div>
	</div>
</div>
<script type="text/javascript">

$(document).ready(function(){
	  if( $('#xacNhanKtt').val() == "yes"){
			
			$("#xacNhanKttYes").prop("checked", true );
				 
			     
			}else if($('#xacNhanKtt').val() == "no") {
				$("#xacNhanKttNo").prop("checked", true );
			}
		
	 
	}); 

	$("#themTempalteKtt").click(function(){
		$("#divTemplateKtt").append($("#templateKtt .row").clone());
	});

 	function remove4(obj) {
 		if($("#divTemplateKtt .row").length > 1){
 			for(i=1; i<=$("#divTemplateKtt .row").length; i++) {
 				
 				var temp = $(obj).parent().parent().parent().attr("id");
 				
 				$(obj).parent().parent().remove();
 				
 				if(temp == "templateKtt") {
 					$("#templateKtt").append($("#divTemplateKtt .row").get(0));
 				}
 				
 			}
 		}
 		
 	}
	function validateStep4Start(obj) {
		if(validateThongTinStep4("Ktt")) {
			uploadDuLieuStep5(obj);
		} 
	}
	
	function validateThongTinStep4(sub) {
		var check = 0;
		$("input[name='hoVaTen"+sub+"']").each(function(){
			if($(this).val() == "") {
				check ++;
				toastr.error("Full Name is not empty");
			}
		});
		$("input[name='soDienThoai"+sub+"']").each(function(){
			if($(this).val() == "") {
				check ++;
				toastr.error("Phone number is not empty");
			}
		});
		$("input[name='email"+sub+"']").each(function(){
			if(!validateEmail( $(this).val())) {
				check ++;
				toastr.error("Email invalid");
			}
		});
		
		console.log(check)
		
		if(check > 0) return false;
		return true;
	}
	var token = "";
	function uploadDuLieuStep5(obj) {
		$(obj).button('loading');
		var haveCA = "";
		if(	$("#xacNhanKttYes").is(":checked")){
			haveCA ="yes";
		}else if($("#xacNhanKttNno").is(":checked")){
			haveCA ="no";
		}
		var data = {
		
			"chiefAccountant": 	getArrayPersonStep4("Ktt"),
			"haveAChiefAccountant": haveCA,
			//"editStatusKtt":$("#editStatusKtt").is(":checked")?"no":"yes",
		};

		
		$.ajax({
			url:'/ekyc-enterprise/step5',
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
			//toastr.error("Error check / Lỗi lưu thông tin");
			$(obj).button('reset');
			location.href='/login-doanh-nghiep';
		}); 
		
	}
	function getArrayPersonStep4(type) {
	
		console.log($("select[name='typeKtt'] option:selected").val());
		var arr = [];
		$("input[name='hoVaTen"+type+"']").each(function(index){
			var json = {};
				json["hoTen"] = $("input[name='hoVaTen"+type+"']").eq(index).val();
				json["phone"] = $("input[name='soDienThoai"+type+"']").eq(index).val();
				json["email"] = $("input[name='email"+type+"']").eq(index).val();
				json["loai"] = $("select[name='type"+type+"']").eq(index).val();
				json["tokenCheck"] = uuidv4();
                if($("input[name='id"+type+"']").eq(index).val() == null || $("input[name='id"+type+"']").eq(index).val() == ""){
					
					json["id"] = uuidv4().substring(1, 8);
				}else{
				
					json["id"] = $("input[name='id"+type+"']").eq(index).val();
				}
				json["time"] = Date.now();
		 		console.log(json);
				arr.push(json);
			//}
		});
		
	 	console.log(arr);
		return arr;
	}

	function uuidv4() {
	  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
	    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
	  );
	}
	
</script>