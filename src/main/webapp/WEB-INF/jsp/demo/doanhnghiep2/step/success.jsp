<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading" style="color: black;">Successfully save information</br> <i>Lưu thông tin thành công</i></h3>
			<br/>
		</div>
		
	</div>
	<div class="row justify-content-center">
	  <button class="btnRegister "  id="btnRegisters" type="button"  style="background: #CCC;color: black;width: 200px;" style="background-color: black;">Close/ Đóng</button>
	</div>
	
</div>


<script>
 $(document).ready(function(){
	$("#btnRegisters").click(function() {
		window.close();
	});
	
}); 
</script>
<%@include file="../layout/footer.jsp"%>
			
