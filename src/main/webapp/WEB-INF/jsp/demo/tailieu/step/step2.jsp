<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-2">
	<div class="panel-heading">
		<h3 class="panel-title">Tải tài liệu</h3>
	</div>
	<div class="panel-body">
		<div class="row">
		<div class="col-sm-3"></div>
			<div class="col-sm-6">	
				<div class="form-group">
					<input  type="file" class="form-control-file" id='taiFile' accept=".pdf" style="display: none;"/>
					<button class="btn btn-default btn-lg" onclick="document.getElementById('taiFile').click(); this.blur();" style="width: 100%" type="button">
		                <i class="glyphicon glyphicon-cloud-upload"></i></br>
		                Hợp đồng ký                                           
		            </button>
		            <small id="nameTaiFile" style="display: none;"></small>
					<textarea id="base64TaiFile" style="display: none;"><c:out value="${fileKy }"/></textarea>
				</div>
			</div>
			<div class="col-sm-3"></div>

		</div>	
			
		
		<button class="btn btn-default btn-sm" type="button" onclick="prevStep(this)">Quay lại</button>
		<button class="btn btn-primary nextBtn pull-right btn-sm" type="button" onclick="validateStep2(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> Đang xử lý">Tiếp tục</button>
	</div>
</div>
<c:if test="${not empty fileKy }">
<script type="text/javascript">
var contentType = 'application/pdf';
var b64Data = '${fileKy }';
var blob = b64toBlob(b64Data, contentType);
var blobUrl = URL.createObjectURL(blob);
$(document).ready(function(){
	$("#iframeid").attr("src", blobUrl);
});
</script>
</c:if>
<script type="text/javascript">





$(document).ready(function(){
	$("#taiFile").change(function(){
		if(ValidateSingleInput1(this)) {
			var file =document.getElementById("taiFile").files[0];
			
			var url =URL.createObjectURL(file );
			console.log(url);
			$('#iframeid').attr('src',url);
			
			$("#base64TaiFile").html("");
			getBase64(file, "base64TaiFile");
			
			$("#nameTaiFile").html($("#taiFile").val());
			if($("#nameTaiFile").html()!="") {
				$("#nameTaiFile").show();
			}
		}
	}); 
	
	$("#taiFile").click(function(){
		thayDoiFileKy = 'true';
		$("#taiFile").val("");
		$("#nameTaiFile").hide();
		$('#iframeid').attr('src',"");
		$("#base64TaiFile").html("");
	})

});

function validateStep2(obj) {
	if ($("#base64TaiFile").html() == "") {
		toastr.error("Tải lên file ký số")
	}  else{
		nextStep(obj);
	}
}
var _validFileExtensions1 = [".pdf"];    
function ValidateSingleInput1(oInput) {
    if (oInput.type == "file") {
        var sFileName = oInput.value;
         if (sFileName.length > 0) {
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions1.length; j++) {
                var sCurExtension = _validFileExtensions1[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                    blnValid = true;
                    break;
                }
            }
             
            if (!blnValid) {
            	toastr.error("Chỉ chấp nhận file có định dạng " + _validFileExtensions1.join(", "));
                oInput.value = "";
                return false;
            }
        }
    }
    return true;
}

/* function validateStep2(obj) {
	var check = 0;
	
	if($("#viTriKyDn").val().trim() == "" || $("#viTriKyCaNhan").val().trim() == "") {
		check ++;	
	}
	if($("#base64TaiFileBaoHiem").html() != "" && $("#viTriKyBaoHiem").val().trim() == "") {
		check ++;
	}
	if(check == 0) {
		luuNoiDungKy($("#base64TaiFile").html(), obj);		
	} else {
		if($("#base64TaiFileBaoHiem").html() != "" && $("#viTriKyBaoHiem").val().trim() == "") {
			toastr.error("Nhập vào vị trí ký cho bảo hiểm")
		} else {
			toastr.error("Nhập vào vị trí ký cho doanh nghiệp và cá nhân")			
		}
	}
} */
function luuNoiDungKy(base64, obj) {
	if(base64 != "") {
		var data = {
				"noiDungFile": base64,
				"noiDungFileBaoHiem": $("#base64TaiFileBaoHiem").html(),
				"tenFile": $("#taiFile").val().split('\\').pop(),
				"soCmt": $("#soCmt").val().trim(),
				"email": $("#email").val(),
				"hoVaTen":$("#hoVaTen").val().trim(),
				"soDienThoai":$("#soDienThoai").val().trim(),
				"viTriKyDn":$("#viTriKyDn").val(),
				"viTriKyBaoHiem":$("#viTriKyBaoHiem").val(),
				"soHopDong":$("#soHopDong").val(),
				"trangKydn":$("#trangKydn").val(),
				"khuVuc":$("#khuVuc").val(),
				"viTriKyCaNhan":$("#viTriKyCaNhan").val(),
				"trangKyCaNhan":$("#trangKyCaNhan").val(),
				"anhCaNhan":$("#base64AnhCaNhanKtt").html(),
				"id":'${ekycKyso.id}',
				"thayDoiFileKy":thayDoiFileKy,
				"thayDoiFileBaoHiem":thayDoiFileBaoHiem
		}
		$(obj).button('loading');
		$.ajax({
			url:'${contextPath}/tao-tai-lieu/luu',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			console.log(data)
			if(data.status == "200") {
				nextStep(obj);
			} else {
				toastr.error(data.message);
			}
			$(obj).button('reset');;
		}).fail(function(data) {
			toastr.error("Lỗi ký số");
			$(obj).button('reset');;
		});
	} else {
		toastr.error("Lỗi ký số");
	}
}
</script>