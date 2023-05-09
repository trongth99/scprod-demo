<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-6">
	<div class="panel-heading">
		<h3 class="panel-title">Authorised Person(s) of Chief Accountant (Person In Charge of Accounting) Information/ <i>Thông Tin Người Được Kế Toán Trưởng (Người Phụ Trách Bộ Phận Kế Toán) Ủy Quyền Ký</i></h3>
	</div>
	
	<div class="panel-body">
		<div id="divTemplateNuqKtt">
			<!-- <div style="margin-bottom: 10px;" id="templateNuqKtt"> -->
			<c:forEach items="${personAuthorizedChiefAccountant}" var="item" varStatus="status">
			<c:if test="${status.index == 0 }">
			
			<div style="margin-bottom: 10px;" id="templateNuqKtt">
			
			</c:if>
			
			
			
			<div class="row">
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Full Name<br/><i style="font-weight: normal;">Họ và Tên</i></label>
							<input type="text" class="form-control input-sm" name="hoVaTenNuqKtt" value="<c:out value='${item.hoTen}'/>"/>
							<input type="hidden" class="form-control input-sm" name="idNuqKtt" value="<c:out value='${item.id}'/>"/>
						</div>
					</div>	
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Mobile Number<br/><i style="font-weight: normal;">Số Điện Thoại</i></label>
							<input type="text" class="form-control input-sm" name="soDienThoaiNuqKtt" value="<c:out value='${item.phone}'/>"//>
						</div>
					</div>
					<div class="form-group col-sm-3">
						<div class="form-group  has-feedback">
							<label class="control-label">Email<br/><i style="font-weight: normal;">Thư Điện Tử</i></label>
							<input type="text" class="form-control input-sm" name="emailNuqKtt" value="<c:out value='${item.email}'/>"/>
						</div>
					</div>
                 <div class="form-group col-sm-3 delete">
							<button  type="button" style="margin-top: 10px;" id="boTempalteNuqKtt" onclick="remove6(this)"><i  class="fa fa-minus minus" aria-hidden="true" ></i></button>
		            </div>

				</div>
				
					<c:if test="${status.index == 0 }"></div></c:if>
				</c:forEach>
			
			<!-- </div> -->
		</div>
	
		<button type="button" style="margin-top: 10px;" id="themTempalteNuqKtt"><i class="fa fa-plus" aria-hidden="true"></i></button>
		<!-- <button type="button" style="margin-top: 10px;" id="boTempalteNuqKtt"><i class="fa fa-minus minus" aria-hidden="true"></i></button> -->
		<div class="form-group col-sm-12">
			<button class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep6Start(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />" id="step6"><spring:message code="ekycdn.tiep_theo" /></button>
			<button class="btn btn-default pull-right" type="button" onclick="prevStep(this)" style="margin-right: 10px;"><spring:message code="ekycdn.quay_lai" /></button>
		</div>
	</div>
</div>
<script type="text/javascript">

	function validateStep6Start(obj) {
		if(validateThongTin("NuqKtt")) {
			uploadDuLieuStep8(obj);
		}
	}
	$(document).ready(function(){
		$("#themTempalteNuqKtt").click(function(){
			if($("#divTemplateNuqKtt .row").length < 4)
			$("#divTemplateNuqKtt").append($("#templateNuqKtt .row").clone());
			$("input[name='idNuqKtt']").last().val("");
			$("input[name='hoVaTenNuqKtt']").last().val("");
			$("input[name='soDienThoaiNuqKtt']").last().val("");
			$("input[name='emailNuqKtt']").last().val("");
		});

	});
/* 	function remove6(obj) {
		if($("#divTemplateNuqKtt .row").length > 1){
			$(obj).parent().parent().remove();
		}
		
	}
	 */
	function remove6(obj) {
 		if($("#divTemplateNuqKtt .row").length > 1){
 			for(i=1; i<=$("#divTemplateNuqKtt .row").length; i++) {
 				var temp = $(obj).parent().parent().parent().attr("id");
 				$(obj).parent().parent().remove();
 				if(temp == "templateNuqKtt") {
 					$("#templateNuqKtt").append($("#divTemplateNuqKtt .row").get(0));
 				}
 				
 			}
 		}
 		
 	}
	
	var token = "";
	function uploadDuLieuStep8(obj) {
		$(obj).button('loading');
		var data = {
			
			//step3
			"personAuthorizedChiefAccountant": getArrayPersonStep6("NuqKtt"),
			
			//"specialInstructions": 	$("#specialInstructions").val(),
			//"editStatusNuqKtt": $("#editStatusNuqKtt").is(":checked")?"yes":"no"	
		};
		$.ajax({
			url:'/ekyc-enterprise/step8',
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

	function getArrayPersonStep6(type) {
		var arr = [];
		$("input[name='soDienThoai"+type+"']").each(function(index){
			var json = {};
			if($("input[name='soDienThoai"+type+"']").eq(index).val() != "") {
				json["hoTen"] = $("input[name='hoVaTen"+type+"']").eq(index).val();
				json["phone"] = $("input[name='soDienThoai"+type+"']").eq(index).val();
				json["email"] = $("input[name='email"+type+"']").eq(index).val();
				json["tokenCheck"] = uuidv4();
				if($("input[name='id"+type+"']").eq(index).val() == null || $("input[name='id"+type+"']").eq(index).val() == ""){
					
					json["id"] = uuidv4().substring(1, 8);
				}else{
				
					json["id"] = $("input[name='id"+type+"']").eq(index).val();
				}
				json["time"] = Date.now();
//		 		console.log(json);
				arr.push(json);
			}
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