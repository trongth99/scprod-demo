<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-14" style="font-style: normal;">

	<div class="panel-heading">
		<h3 class="panel-title">Straight2Bank User Designation & Authorisation/ <i>Chỉ Định và Ủy Quyền Người Dùng Straight2Bank</i></h3>
	</div>
	<div class="panel-body">
		<div id="divTemplateAdd">
			<c:forEach items="${userDesignation}" var="item" varStatus="status">
				<c:if test="${status.index == 0 }"><div style="margin-bottom: 10px;" id="templateAdd"></c:if>
					<div class="row" title="${status.index}">
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Full Name<br/><i style="font-weight: normal;">Họ và Tên</i></label>
								<input type="text" class="form-control input-sm" name="hoVaTenAdd" value="<c:out value='${item.hoTen}'/>"/>
								<input type="hidden" class="form-control input-sm" name="idAdd" value="<c:out value='${item.id}'/>"/>
								<input type="hidden" class="form-control input-sm"  value="<c:out value='${status.index}'/>"/>
							</div>
						</div>	
						
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">ID/ Passsport<br/><i style="font-weight: normal;">Số CMND/ CCCD/ Hộ Chiếu</i></label>
								<input type="text" class="form-control input-sm" name="soCmtAdd" value="<c:out value='${item.soCmt}'/>"/>
							</div>
						</div>
						<div class="form-group col-sm-3">
							<div class="form-group  has-feedback">
								<label class="control-label">Email<br/><i style="font-weight: normal;">Thư Điện Tử</i></label>
								<input type="text" class="form-control input-sm" name="emailAdd" value="<c:out value='${item.email}'/>"/>
							</div>
						</div>
						<div class="form-group col-sm-1 delete">
							<button  type="button" style="margin-top: 10px;" id="boTempalteAdd" onclick="remove111(this)"><i  class="fa fa-minus minus" aria-hidden="true" ></i></button>
		            </div>
					</div>
				<c:if test="${status.index == 0 }"></div></c:if>
				<input type="hidden" id="${item.hanmucLoai}" value="<c:out value='${item.hanmuc}_${item.hanmuctext}_${item.typemoney}'/>" />
				<input type="hidden" id="${item.hanmuctextLoai}" value="<c:out value='${item.hanmuctextLoai}'/>" />
				<input type="hidden" id="${item.typemoneyLoai}" value="<c:out value='${item.typemoneyLoai}'/>" />
			</c:forEach>
		</div>
		<button type="button" style="margin-top: 10px;" id="themTempalteAdd"><i class="fa fa-plus" aria-hidden="true"></i></button>
		
		<div class="row" style="margin-top: 30px;">
			<div class="form-group col-sm-12">
				<table class="table table-striped table-hover table-bordered">
					<tr id="title">
						<!--<td>Authorisation for<br/><i> Ủy quyền đối với</i></td>
						<td style="text-align: center;">User 1<br/><i>Người dùng 1</i></td>
						 <td>User 2<br/><i>Người dùng 2</i></td>
						<td>User 3<br/><i>Người dùng 3</i></td>
						<td>User 4<br/><i>Người dùng 4</i></td>
						<td>User 5<br/><i>Người dùng 5</i></td>
						<td>User 6<br/><i>Người dùng 6</i></td> -->
					</tr>
					<tr id="tdr1">
						<!--<td>Prepare Instructions<br/><i>Tạo các Lệnh</i></td>
						<td class="user1" style="text-align: center;"></td>
						 <td class="user2" style="text-align: center;"></td>
						<td class="user3" style="text-align: center;"></td>
						<td class="user4" style="text-align: center;"></td>
						<td class="user5" style="text-align: center;"></td>
						<td class="user6" style="text-align: center;"></td> -->
					</tr>
					<tr id="tdr2">
						<!-- <td>View, print, download reports<br/><i>Xem, in, tải các báo cáo</i></td>
						<td class="user1" style="text-align: center;"></td>
						<td class="user2" style="text-align: center;"></td>
						<td class="user3" style="text-align: center;"></td>
						<td class="user4" style="text-align: center;"></td>
						<td class="user5" style="text-align: center;"></td>
						<td class="user6" style="text-align: center;"></td> -->
					</tr>
					<tr id="tdr3">
					<!-- 	<td>Approve and release Instructions  <sup>1</sup><br/><i>Chấp thuận và đưa ra các lệnh <sup>1</sup></i></td>
						<td class="user1" style="text-align: center;"></td>
						<td class="user2" style="text-align: center;"></td>
						<td class="user3" style="text-align: center;"></td>
						<td class="user4" style="text-align: center;"></td>
						<td class="user5" style="text-align: center;"></td>
						<td class="user6" style="text-align: center;"></td> -->
					</tr>
					<tr id="tdr4">
						<!-- <td>Approve and release Instructions only jointly  <sup>1</sup><br/><i>Chấp thuận và đưa ra các Lệnh chỉ thị đồng thời <sup>1</sup></i></td>
						<td class="user1" style="text-align: center;"></td>
						<td class="user2" style="text-align: center;"></td>
						<td class="user3" style="text-align: center;"></td>
						<td class="user4" style="text-align: center;"></td>
						<td class="user5" style="text-align: center;"></td>
						<td class="user6" style="text-align: center;"></td> -->
					</tr>
					 <tr id="tdr5">
						<!-- <td>Approve and release Instructions only jointly  <sup>1</sup><br/><i>Chấp thuận và đưa ra các Lệnh chỉ thị đồng thời <sup>1</sup></i></td>
						<td class="user1" style="text-align: center;"></td>
						<td class="user2" style="text-align: center;"></td>
						<td class="user3" style="text-align: center;"></td>
						<td class="user4" style="text-align: center;"></td>
						<td class="user5" style="text-align: center;"></td>
						<td class="user6" style="text-align: center;"></td> -->
					</tr> 
				</table>
			</div>
		</div>
		<div class="row" style="margin-top: 30px;">
			<div class="form-group col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Register User(s) for e-Supporting Document Upload via S2B<br/><i style="font-weight: normal;">Đăng ký Người Sử Dụng chức năng Tải Chứng Từ Bổ Sung qua S2B</i></label>
					<input type="text" class="form-control input-sm" name="dangKyNguoiSuDung" id="dangKyNguoiSuDung" value="All users" value="<c:out value='${ekycDoanhNghiep.registerUser}'/>"/>
				</div>
			</div>	
		</div>
		<div class="row">
				<div class="form-group col-sm-12">
					<div class="form-group  has-feedback">
						<label class="control-label">Special Instructions <br/> <i style="font-weight: normal;">Chỉ Dẫn Đặc Biệt</i></label>
						<input type="text" class="form-control input-sm" name="specialInstructionsUser" id="specialInstructionsUser" value="<c:out value='${ekycDoanhNghiep.specialInstructionsUser}'/>"/>
					</div>
				</div>
		</div>
		
		
		<div class="form-group col-sm-12">
			<button class="btn btn-primary nextBtn pull-right" type="button" onclick="validateStep14Start(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <spring:message code="ekycdn.dang_xy_ly" />"><spring:message code="ekycdn.tiep_theo" /></button>
			<button class="btn btn-default pull-right" type="button" onclick="prevStep(this)" style="margin-right: 10px;"><spring:message code="ekycdn.quay_lai" /></button>
		</div>
		<div style="font-style: normal;"><sup>1</sup>:Please provide a copy of identification Card or Passport of Straight2Bank Appprovers.<br/><i><sup>1</sup>: Vui lòng cung cấp bản sao Giấy CCCD hoặc CMND hoặc Hộ Chiếu của Người Có Thẩm Quyền trên Straight2Bank</i></div>
		
		<div style="font-style: normal;"><sup>2</sup>: When choosing <b>Approve and release Instructions only jointly</b>, please note details in <b>Special instruction</b>. Ex: User1 + User2 = Unlimited<br/>
		<i><sup>2</sup>:Khi chọn <b>Chấp thuận và đưa ra các Lệnh chỉ thị đồng thời</b>, vui lòng điền chi tiết vào phần <b>Chỉ Dẫn Đặc Biệt</b>. Ví dụ: User1 + User2 = Không giới hạn số tiền</i></div>
	</div>
</div>
<style>	
#td{
text-align: center;
}
</style>
<script type="text/javascript">
var tdr1 = [];
var tdr2 = [];
var tdr3 = [];
var tdr4 = [];
<c:forEach items="${userDesignation}" var="item" varStatus="status">
tdr1.push('${item.taoLenh}');
tdr2.push('${item.baoCao}');
tdr3.push('${item.chapThuanLenh}');
tdr4.push('${item.chapThuanLenhDongThoi}');
</c:forEach>
</script>

<script type="text/javascript">







$(document).ready(function(){
	

	
	$(function () {	
		
		$('#hanmuc1').on('change', function (e) {
			if($( "#hanmuc1" ).val() == "Other"){
				
				$("#text1").show();
				
			}else{
				$("#text1").hide();
				
			}
	          
	      });
		
		$('#hanmuc2').on('change', function (e) {
			if($( "#hanmuc2" ).val() == "Other"){
				
				$("#text2").show();
				
			}else{
				$("#text2").hide();
				
			}
	          
	      });
		
		$('#hanmuc3').on('change', function (e) {
			if($( "#hanmuc3" ).val() == "Other"){
				
				$("#text3").show();
				
			}else{
				$("#text3").hide();
				
			}
	          
	      });
		
		$('#hanmuc4').on('change', function (e) {
			if($( "#hanmuc4" ).val() == "Other"){
				
				$("#text4").show();
				
			}else{
				$("#text4").hide();
				
			}
	          
	      });
		
		$('#hanmuc5').on('change', function (e) {
			if($( "#hanmuc5" ).val() == "Other"){
				
				$("#text5").show();
				
			}else{
				$("#text5").hide();
				
			}
	          
	      });
		
		$('#hanmuc6').on('change', function (e) {
			if($( "#hanmuc6" ).val() == "Other"){
				
				$("#text6").show();
				
			}else{
				$("#text6").hide();
				
			}
	          
	      });




		$("#tdr3user1").change(function() {
			   if ($("#tdr3user1").is(":checked") == true) {
				   $("#limit1").show();
			  	 
			   } else  if ($("#tdr3user1").is(":checked") == false){
				   $("#limit1").hide();
			   }
			});
			
			$("#tdr3user2").change(function() {
				   if ($("#tdr3user2").is(":checked") == true) {
					   $("#limit2").show();
				  	 
				   } else  if ($("#tdr3user2").is(":checked") == false){
					   $("#limit2").hide();
				   }
				});
			
			$("#tdr3user3").change(function() {
				   if ($("#tdr3user3").is(":checked") == true) {
					   $("#limit3").show();
				  	 
				   } else  if ($("#tdr3user3").is(":checked") == false){
					   $("#limit3").hide();
				   }
				});
			
			$("#tdr3user4").change(function() {
				   if ($("#tdr3user4").is(":checked") == true) {
					   $("#limit4").show();
				  	 
				   } else  if ($("#tdr3user4").is(":checked") == false){
					   $("#limit4").hide();
				   }
				});
			
			$("#tdr3user5").change(function() {
				   if ($("#tdr3user5").is(":checked") == true) {
					   $("#limit5").show();
				  	 
				   } else  if ($("#tdr3user5").is(":checked") == false){
					   $("#limit5").hide();
				   }
				});
			
			$("#tdr3user6").change(function() {
				   if ($("#tdr3user6").is(":checked") == true) {
					   $("#limit6").show();
				  	 
				   } else  if ($("#tdr3user6").is(":checked") == false){
					   $("#limit6").hide();
				   }
				});
		
	});
	
	
	
	loadTable();
	load();

});


function load(){
	
	
	
 	 for(i=1; i<=7; i++) {
		
		$("#tdr1 .user"+i).html("");
		$("#tdr2 .user"+i).html("");
		$("#tdr3 .user"+i).html("");
		$("#tdr4 .user"+i).html("");
		$("#tdr5 .user"+i).html("");
		
	} 
 	 
 
 	
	 for(i=1; i<=$("#divTemplateAdd .row").length; i++) {
		
		var j = i-1;
		 //if(tdr1[j] && tdr1[j] == 'Y')  
			$("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"' checked>");
		 //else
			//$("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"'>"); 
		 //$("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"' checked>");
		
		
		if(tdr2[j] && tdr2[j] == 'Y') 
			$("#tdr2 .user"+i).html("<input type='checkbox' name='tdr2user"+i+"' checked>");
		else
			$("#tdr2 .user"+i).html("<input type='checkbox' name='tdr2user"+i+"'>");
		
		if(tdr3[j] && tdr3[j] == 'Y') 
			$("#tdr3 .user"+i).html("<input type='checkbox' name='tdr3user"+i+"' id='tdr3user"+i+"' checked>");
		else
			$("#tdr3 .user"+i).html("<input type='checkbox' name='tdr3user"+i+"' id='tdr3user"+i+"'>");
		
		if(tdr4[j] && tdr4[j] == 'Y')
			$("#tdr5 .user"+i).html("<input type='checkbox' name='tdr5user"+i+"' checked>");
		else
			$("#tdr5 .user"+i).html("<input type='checkbox' name='tdr5user"+i+"'>");
	
	   
		
			
		$("#tdr4 .user"+i).html("<div id='limit"+i+"' value='limit"+i+"' style='display:none;'>  <select style='width:100%;' id='hanmuc"+i+"'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other'>Other/ Khác</option></select> <div id='text"+i+"' style='margin-top:10px;width:100%;display:none;'> <input style='width:100%;' id='hanmuctext"+i+"' value=''/>  <input type='radio' name='typemoney"+i+"'  value='VND'>VND <input type='radio' name='typemoney"+i+"' value='USD'>USD</div></div>");
	
		
          if($("#hanmuc1_Other").val()){
			
        	const str1 = $("#hanmuc1_Other").val();
  			const split1 = str1.split('_');
  			 
  			if(split1[2] == "VND"){
  				$("#tdr4 .user1").html("<div id='limit1' value='limit1' style='display:none;'>  <select style='width:100%;' id='hanmuc1'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text1' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext1' value='"+split1[1]+"'/>  <input type='radio' name='typemoney1'  value='VND' checked>VND <input type='radio' name='typemoney1' value='USD'>USD</div></div>");

  			}else{
  				$("#tdr4 .user1").html("<div id='limit1' value='limit1' style='display:none;'>  <select style='width:100%;' id='hanmuc1'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text1' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext1' value='"+split1[1]+"'/>  <input type='radio' name='typemoney1'  value='VND' >VND <input type='radio' name='typemoney1' value='USD' checked>USD</div></div>");

  			}            
			
		}
		
		if($("#hanmuc2_Other").val()){
			const str2 = $("#hanmuc2_Other").val();
			const split2 = str2.split('_');
			 
			if(split2[2] == "VND"){
				$("#tdr4 .user2").html("<div id='limit2' value='limit2' style='display:none;'>  <select style='width:100%;' id='hanmuc2'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text2' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext2' value='"+split2[1]+"'/>  <input type='radio' name='typemoney2'  value='VND' checked>VND <input type='radio' name='typemoney2' value='USD'>USD</div></div>");

			}else{
				$("#tdr4 .user2").html("<div id='limit2' value='limit2' style='display:none;'>  <select style='width:100%;' id='hanmuc2'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text2' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext2' value='"+split2[1]+"'/>  <input type='radio' name='typemoney2'  value='VND' >VND <input type='radio' name='typemoney2' value='USD' checked>USD</div></div>");

			}
            
			
		}
		if($("#hanmuc3_Other").val()){
			const str3 = $("#hanmuc3_Other").val();
			const split3 = str3.split('_');
			 
			if(split3[2] == "VND"){
				$("#tdr4 .user3").html("<div id='limit3' value='limit3' style='display:none;'>  <select style='width:100%;' id='hanmuc3'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text3' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext3' value='"+split3[1]+"'/>  <input type='radio' name='typemoney3'  value='VND' checked>VND <input type='radio' name='typemoney3' value='USD'>USD</div></div>");

			}else{
				$("#tdr4 .user3").html("<div id='limit3' value='limit3' style='display:none;'>  <select style='width:100%;' id='hanmuc3'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text3' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext3' value='"+split3[1]+"'/>  <input type='radio' name='typemoney3'  value='VND' >VND <input type='radio' name='typemoney3' value='USD' checked>USD</div></div>");

			}
            
			
		}
		if($("#hanmuc4_Other").val()){
			const str4 = $("#hanmuc4_Other").val();
			const split4 = str4.split('_');
			 
			if(split4[2] == "VND"){
				$("#tdr4 .user4").html("<div id='limit4' value='limit4' style='display:none;'>  <select style='width:100%;' id='hanmuc4'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text4' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext4' value='"+split4[1]+"'/>  <input type='radio' name='typemoney4'  value='VND' checked>VND <input type='radio' name='typemoney4' value='USD'>USD</div></div>");

			}else{
				$("#tdr4 .user4").html("<div id='limit4' value='limit4' style='display:none;'>  <select style='width:100%;' id='hanmuc4'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text4' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext4' value='"+split4[1]+"'/>  <input type='radio' name='typemoney4'  value='VND' >VND <input type='radio' name='typemoney4' value='USD' checked>USD</div></div>");

			}
            
			
		}
		if($("#hanmuc5_Other").val()){
			const str5 = $("#hanmuc5_Other").val();
			const split5 = str5.split('_');
			 
			if(split5[2] == "VND"){
				$("#tdr4 .user5").html("<div id='limit5' value='limit5' style='display:none;'>  <select style='width:100%;' id='hanmuc5'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text5' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext5' value='"+split5[1]+"'/>  <input type='radio' name='typemoney5'  value='VND' checked>VND <input type='radio' name='typemoney5' value='USD'>USD</div></div>");

			}else{
				$("#tdr4 .user5").html("<div id='limit5' value='limit5' style='display:none;'>  <select style='width:100%;' id='hanmuc5'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text5' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext5' value='"+split5[1]+"'/>  <input type='radio' name='typemoney5'  value='VND' >VND <input type='radio' name='typemoney5' value='USD' checked>USD</div></div>");

			}
            
			
		}
		if($("#hanmuc6_Other").val()){
			const str6 = $("#hanmuc6_Other").val();
			const split6 = str6.split('_');
			 
			if(split6[2] == "VND"){
				$("#tdr4 .user6").html("<div id='limit6' value='limit6' style='display:none;'>  <select style='width:100%;' id='hanmuc6'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text6' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext6' value='"+split6[1]+"'/>  <input type='radio' name='typemoney6'  value='VND' checked>VND <input type='radio' name='typemoney6' value='USD'>USD</div></div>");

			}else{
				$("#tdr4 .user6").html("<div id='limit6' value='limit6' style='display:none;'>  <select style='width:100%;' id='hanmuc6'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other' selected>Other/ Khác</option></select> <div id='text6' style='margin-top:10px;width:100%;'> <input style='width:100%;' id='hanmuctext6' value='"+split6[1]+"'/>  <input type='radio' name='typemoney6'  value='VND' >VND <input type='radio' name='typemoney6' value='USD' checked>USD</div></div>");

			}
            
			
		}
		///////////////////////////////
	
	    //////////////////////////////////
		if($("#tdr3user1").is(":checked") == true){
			 
			
			$("#limit1").show();
		}else if($("#tdr3user1").is(":checked") == false){
			
		
			$("#limit1").hide();
		} 
	 
		
		
		if($("#tdr3user2").is(":checked")  ){
			 
			
			$("#limit2").show();
		}else if(!$("tdr3user2").is(":checked")){
			
			
			$("#limit2").hide();
		} 
		
		
		if($("input[name='tdr3user3']").is(":checked")  ){
			 
			
			$("#limit3").show();
		}else if(!$("input[name='tdr3user3']").is(":checked")){
			
			
			$("#limit3").hide();
		} 
		
		
		if($("input[name='tdr3user4']").is(":checked")  ){
			 
			
			$("#limit4").show();
		}else if(!$("input[name='tdr3user4']").is(":checked")){
			
			
			$("#limit4").hide();
		} 
		
		
		if($("input[name='tdr3user5']").is(":checked")  ){
			 
			 
			$("#limit5").show();
		}else if(!$("input[name='tdr3user5']").is(":checked")){
			
		
			$("#limit5").hide();
		} 
		
		if($("input[name='tdr3user6']").is(":checked")  ){
			 
			
			$("#limit6").show();
		}else if(!$("input[name='tdr3user6']").is(":checked")){
			
			
			$("#limit6").hide();
		} 
	 
	 }  
	 
}
function remove111(obj) {
	if($("#divTemplateAdd .row").length > 1){
		for(i=1; i<=$("#divTemplateAdd .row").length; i++) {
			var index = $(obj).parent().parent().attr("title");
			var temp = $(obj).parent().parent().parent().attr("id");
			$(obj).parent().parent().remove();
			if(temp == "templateAdd") {
				$("#templateAdd").append($("#divTemplateAdd .row").get(0));
			}
			addIndexRow();
			removeUser(index);
			load();
		}
	}
	
}
function removeUser(index) {
	var removeIndex = parseInt(index) +1;
	console.log("removeIndex"+removeIndex)
	 
	$("input[name='tdr1user"+removeIndex+"']").remove();
	$("input[name='tdr2user"+removeIndex+"']").remove();
	$("input[name='tdr3user"+removeIndex+"']").remove();
	$("input[name='tdr5user"+removeIndex+"']").remove();
	
	 $("#title .title"+removeIndex).remove();
	 $("#tdr1 .user"+removeIndex).remove();
	 $("#tdr2 .user"+removeIndex).remove();
	 $("#tdr3 .user"+removeIndex).remove();
	 $("#tdr4 .user"+removeIndex).remove();
	 $("#tdr5 .user"+removeIndex).remove();
	for(i=removeIndex; i<=$("#divTemplateAdd .row").length+1; i++) {
		if($("input[name='tdr1user"+(i+1)+"']").is(":checked"))
			$("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"' checked>");
		else
			$("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"'>");
		
		if($("input[name='tdr2user"+(i+1)+"']").is(":checked"))
			$("#tdr2 .user"+i).html("<input type='checkbox' name='tdr2user"+i+"' checked>");
		else
			$("#tdr2 .user"+i).html("<input type='checkbox' name='tdr2user"+i+"'>");
		
		if($("input[name='tdr3user"+(i+1)+"']").is(":checked"))
			$("#tdr3 .user"+i).html("<input type='checkbox' name='tdr3user"+i+"' checked>");
		else
			$("#tdr3 .user"+i).html("<input type='checkbox' name='tdr3user"+i+"'>");
		
		if($("input[name='tdr5user"+(i+1)+"']").is(":checked"))
			$("#tdr5 .user"+i).html("<input type='checkbox' name='tdr5user"+i+"' checked>");
		else
			$("#tdr5 .user"+i).html("<input type='checkbox' name='tdr5user"+i+"'>");
		
		if(i == ($("#divTemplateAdd .row").length+1)) {
			  
			$("input[name='tdr1user"+i+"']").remove();
			$("input[name='tdr2user"+i+"']").remove();
			$("input[name='tdr3user"+i+"']").remove();
			$("input[name='tdr5user"+i+"']").remove();
			
			$("#title .title"+removeIndex).remove();
			$("#tdr1 .user"+removeIndex).remove();
			 $("#tdr2 .user"+removeIndex).remove();
			 $("#tdr3 .user"+removeIndex).remove();
			 $("#tdr4 .user"+removeIndex).remove();
			 $("#tdr5 .user"+removeIndex).remove();
		}
		
		
	}
	
}
 
function validateStep14Start(obj) {
	 
		if(validateThongTin("Add")) {
			  if(!$("input[name='tdr1user1']").is(':checked') && !$("input[name='tdr2user1']").is(':checked') && !$("input[name='tdr3user1']").is(':checked') && !$("input[name='tdr4user1']").is(':checked') && $("#divTemplateAdd .row").length >= 1 ){
				toastr.error("Authorisation for User 1 can not be empty ");
			}else if(!$("input[name='tdr1user2']").is(':checked') && !$("input[name='tdr2user2']").is(':checked') && !$("input[name='tdr3user2']").is(':checked') && !$("input[name='tdr4user2']").is(':checked') && $("#divTemplateAdd .row").length >= 2){
				toastr.error("Authorisation for User 2 can not be empty ");
			}else if(!$("input[name='tdr1user3']").is(':checked') && !$("input[name='tdr2user3']").is(':checked') && !$("input[name='tdr3user3']").is(':checked') && !$("input[name='tdr4user3']").is(':checked') && $("#divTemplateAdd .row").length >= 3){
				toastr.error("Authorisation for User 3 can not be empty ");
			}else if(!$("input[name='tdr1user4']").is(':checked') && !$("input[name='tdr2user4']").is(':checked') && !$("input[name='tdr3user4']").is(':checked') && !$("input[name='tdr4user4']").is(':checked') && $("#divTemplateAdd .row").length >= 4){
				toastr.error("Authorisation for User 4 can not be empty ");
			}else if(!$("input[name='tdr1user5']").is(':checked') && !$("input[name='tdr2user5']").is(':checked') && !$("input[name='tdr3user5']").is(':checked') && !$("input[name='tdr4user5']").is(':checked') && $("#divTemplateAdd .row").length >= 5){
				toastr.error("Authorisation for User 5 can not be empty ");
			}else if(!$("input[name='tdr1user6']").is(':checked') && !$("input[name='tdr2user6']").is(':checked') && !$("input[name='tdr3user6']").is(':checked') && !$("input[name='tdr4user6']").is(':checked') && $("#divTemplateAdd .row").length >= 6){
				toastr.error("Authorisation for User 6 can not be empty ");
			}else { 
				
				$(obj).button('loading');
				var data = {
					"userDesignation": 	getArrayPersonUserDesignation("Add"),
					"registerUser": $("#dangKyNguoiSuDung").val(),
					"specialInstructionsUser": $("#specialInstructionsUser").val()
				};
				$.ajax({
					url:'/ekyc-enterprise/luu-thong-tin-step9',
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
			
		}
	}

		
			

		$("#themTempalteAdd").click(function(){
			if($("#divTemplateAdd .row").length < 6) {
				$("#divTemplateAdd").append($("#templateAdd .row").clone());
				addIndexRow();
				$("input[name='idAdd']").last().val("");
				$("input[name='hoVaTenAdd']").last().val("");
				$("input[name='soCmtAdd']").last().val("");
				$("input[name='emailAdd']").last().val("");
			
				loadTable();
				load();
				//addCheckUser();
				$('#hanmuc1').on('change', function (e) {
					if($( "#hanmuc1" ).val() == "Other"){
						
						$("#text1").show();
						
					}else{
						$("#text1").hide();
						
					}
			          
			      });
				
				$('#hanmuc2').on('change', function (e) {
					if($( "#hanmuc2" ).val() == "Other"){
						
						$("#text2").show();
						
					}else{
						$("#text2").hide();
						
					}
			          
			      });
				
				$('#hanmuc3').on('change', function (e) {
					if($( "#hanmuc3" ).val() == "Other"){
						
						$("#text3").show();
						
					}else{
						$("#text3").hide();
						
					}
			          
			      });
				
				$('#hanmuc4').on('change', function (e) {
					if($( "#hanmuc4" ).val() == "Other"){
						
						$("#text4").show();
						
					}else{
						$("#text4").hide();
						
					}
			          
			      });
				
				$('#hanmuc5').on('change', function (e) {
					if($( "#hanmuc5" ).val() == "Other"){
						
						$("#text5").show();
						
					}else{
						$("#text5").hide();
						
					}
			          
			      });
				
				$('#hanmuc6').on('change', function (e) {
					if($( "#hanmuc6" ).val() == "Other"){
						
						$("#text6").show();
						
					}else{
						$("#text6").hide();
						
					}
			          
			      });




				$("#tdr3user1").change(function() {
					   if ($("#tdr3user1").is(":checked") == true) {
						   $("#limit1").show();
					  	
					   } else  if ($("#tdr3user1").is(":checked") == false){
						   $("#limit1").hide();
					   }
					});
					
					$("#tdr3user2").change(function() {
						   if ($("#tdr3user2").is(":checked") == true) {
							   $("#limit2").show();
						  	 
						   } else  if ($("#tdr3user2").is(":checked") == false){
							   $("#limit2").hide();
						   }
						});
					
					$("#tdr3user3").change(function() {
						   if ($("#tdr3user3").is(":checked") == true) {
							   $("#limit3").show();
						  	 
						   } else  if ($("#tdr3user3").is(":checked") == false){
							   $("#limit3").hide();
						   }
						});
					
					$("#tdr3user4").change(function() {
						   if ($("#tdr3user4").is(":checked") == true) {
							   $("#limit4").show();
						  	 
						   } else  if ($("#tdr3user4").is(":checked") == false){
							   $("#limit4").hide();
						   }
						});
					
					$("#tdr3user5").change(function() {
						   if ($("#tdr3user5").is(":checked") == true) {
							   $("#limit5").show();
						  	 
						   } else  if ($("#tdr3user5").is(":checked") == false){
							   $("#limit5").hide();
						   }
						});
					
					$("#tdr3user6").change(function() {
						   if ($("#tdr3user6").is(":checked") == true) {
							   $("#limit6").show();
						  	 
						   } else  if ($("#tdr3user6").is(":checked") == false){
							   $("#limit6").hide();
						   }
						});
			}
		});
		
		function addIndexRow() {
			$("#divTemplateAdd .row").each(function(index){
				$(this).attr("title", index);
			});
		}
		

		
			
	function loadTable(){
		
		
		
		   var title ="<td>Authorisation  for <br/><i>Ủy quyền đối với</i></td>";
		   var tdr1 ="<td>Prepare Instructions<br/><i>Tạo các Lệnh</i></td>";
		   var tdr2 ="<td>View, print, download reports<br/><i>Xem, in, tải các báo cáo</i></td>";
		   var tdr3 ="<td>Approve and release Instructions  <sup>1</sup><br/><i>Chấp thuận và đưa ra các lệnh <sup>1</sup></i></td>";
		   var tdr4 ="<td>Signing Limit (equivalent amount in selected currency)<br/><i>Hạn mức duyệt lệnh (số tiền tương dương theo loại tiền tệ được chọn)</i></td>"; 
		   var tdr5 ="<td>Approve and release Instructions only jointly  <sup>1,2</sup><br/><i>Chấp thuận và đưa ra các Lệnh chỉ thị đồng thời <sup>1,2</sup></i></td>";
		  
		   
		 
		   for(i=1; i<=$("#divTemplateAdd .row").length; i++) {
			   var st ='text-align: center;';
	            title +="<td class='title"+i+"' style='"+st+"'>User "+i+"<br/>Người dùng "+i+"</td>"
				tdr1 +="<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr1user"+i+"' checked></td>"
			    tdr2 +="<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr2user"+i+"' ></td>"
				tdr3 +="<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr3user"+i+"' id='tdr3user"+i+"'></td>"
				tdr4 +="<td class='user"+i+"'   ><div id='limit"+i+"' value='limit"+i+"' style='display:none;'>  <select style='width:100%;' id='hanmuc"+i+"'> <option id='select' value='Unlimited'>Unlimited/ Không giới hạn số tiền</option> <option value='Other'>Other/ Khác</option></select> <div  id='text"+i+"' style='margin-top:10px;width:100%;display:none;'> <input style='width:100%;' id='hanmuctext"+i+"' value=''/>  <input type='radio' name='typemoney"+i+"'  value='VND'>VND <input type='radio' name='typemoney"+"' value='USD'>USD</div></div></td>" 
				tdr5 +="<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr5user"+i+"' ></td>"
				
				
			
				
		   }
		   
		   $("#title").html(title);
			$("#tdr1").html(tdr1);
		 	$("#tdr2" ).html(tdr2);
			$("#tdr3 ").html(tdr3);
			$("#tdr4 ").html(tdr4); 
			$("#tdr5 ").html(tdr5); 
			
			
	}
	 
 	 	function addCheckUser() {
	 		
 	 		
			 var i =$("#divTemplateAdd .row").length;
			 console.log(i)
			 var st ='text-align: center;';
			 $("#title").html("<td class='title"+i+"' style='"+st+"'>User "+i+"<br/>Người dùng "+i+"</td>");
				$("#tdr1").html("<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr1user"+i+"' checked></td>");
			 	$("#tdr2" ).html("<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr2user"+i+"' ></td>");
				$("#tdr3 ").html("<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr3user"+i+"' ></td>");
				$("#tdr4 ").html("<td class='user"+i+"' style='"+st+"'><input type='checkbox' name='tdr4user"+i+"' ></td>"); 
			/* $("#tdr1 .user"+i).html("<input type='checkbox' name='tdr1user"+i+"' checked>");
			$("#tdr2 .user"+i).html("<input type='checkbox' name='tdr2user"+i+"' >");
			$("#tdr3 .user"+i).html("<input type='checkbox' name='tdr3user"+i+"' >");
			$("#tdr4 .user"+i).html("<input type='checkbox' name='tdr4user"+i+"' >");  */
			
				
		}  
		
		
//	});
	function validateThongTin2(sub) {
		
		var check = 0;
		
		$("input[name='hoVaTen"+sub+"']").each(function(){
			if($(this).val() == "") {
				check ++;
				toastr.error("Full Name is not empty");
			}
		});
		$("input[name='soCmt"+sub+"']").each(function(){
			if($(this).val() == "") {
				check ++;
				toastr.error("ID card number is not empty");
			}
		});
		$("input[name='email"+sub+"']").each(function(){
			if(!validateEmail( $(this).val())) {
				check ++;
				toastr.error("Email invalid");
			}
		});
		if(check > 0) return false;
		return true;
	}
	function getArrayPersonUserDesignation(type) {
		var arr = [];
		$("input[name='soCmt"+type+"']").each(function(index){
			var json = {};
			if($("input[name='soCmt"+type+"']").eq(index).val() != "") {
				var userIndex = index+1;
				json["hoTen"] = $("input[name='hoVaTen"+type+"']").eq(index).val();
				json["soCmt"] = $("input[name='soCmt"+type+"']").eq(index).val();
				json["email"] = $("input[name='email"+type+"']").eq(index).val();
				json["tokenCheck"] = uuidv4();
				 if($("input[name='id"+type+"']").eq(index).val() == null || $("input[name='id"+type+"']").eq(index).val() == ""){
						
						json["id"] = uuidv4().substring(1, 8);
					}else{
					
						json["id"] = $("input[name='id"+type+"']").eq(index).val();
					}
				if($("input[name='tdr1user"+userIndex+"']")) {
					var valtaoLenh = $("input[name='tdr1user"+userIndex+"']").prop("checked")?"Y":"N";
					json["taoLenh"] = valtaoLenh;		
					var valbaoCao = $("input[name='tdr2user"+userIndex+"']").prop("checked")?"Y":"N";
					json["baoCao"] = valbaoCao;	
					var valchapThuanLenh = $("input[name='tdr3user"+userIndex+"']").prop("checked")?"Y":"N";
					json["chapThuanLenh"] = valchapThuanLenh;	
					var valchapThuanLenhDongThoi = $("input[name='tdr5user"+userIndex+"']").prop("checked")?"Y":"N";
					json["chapThuanLenhDongThoi"] = valchapThuanLenhDongThoi;	
					
					var hanmuc = $("#hanmuc"+userIndex+"").val();
					console.log(hanmuc)
					json["hanmuc"] = hanmuc;	
					json["hanmucLoai"] = "hanmuc"+userIndex+"_"+hanmuc+"";
					
					var hanmuctext = $("#hanmuctext"+userIndex+"").val();
					console.log(hanmuctext)
					json["hanmuctext"] = hanmuctext;
					json["hanmuctextLoai"] = "hanmuctext"+userIndex+"_"+hanmuctext+"";
					
					var typemoney = $("input[name = 'typemoney"+userIndex+"']:checked").val();
					console.log(typemoney)
					json["typemoney"] = typemoney;
					json["typemoneyLoai"] = "typemoney"+userIndex+"_"+typemoney+"";
				}
				arr.push(json);
			}
		});
//	 	console.log(arr);
		return arr;
	}
</script>