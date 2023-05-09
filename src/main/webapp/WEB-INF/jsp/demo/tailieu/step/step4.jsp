<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-4">
   <div class="panel-heading">
		<h3 class="panel-title">Thiết kế tài liệu</h3>
	</div>
	<div class="panel-body">
		<div class="form-group" style="text-align: center;font-size: 20px;">
			<div id="draggable" class="ui-widget-content" style='width:100px;'>
			  <p>Drag me to my target</p>
			  <span></span>
			</div>
			<div id="">
			  <div id="pdf-loader">Loading document ...</div>
			   <div class="row">
					   <div class="col-sm-8" >
					    <div id="pdf-contents" style="margin-left: 20px;">
					        <div id="pdf-meta">
					            <div id="pdf-buttons">
					                <button id="pdf-prev" style="font-size: 20px;">Previous</button>
					                <button id="pdf-next"  style="font-size: 20px;">Next</button>
					            </div>
					            <div id="page-count-container">Page
					                <div id="pdf-current-page"></div>
					                of
					                <div id="pdf-total-pages"></div>
					            </div>
					        </div>
					
					      
						       
							       <div id="canvas">
										<canvas id="pdf-canvas" width="700"></canvas>
										<div id="box1">Signature1</div>
										<div id="box2">Signature2</div>
									</div>
						       </div>
						      
					      
							
					
							
					      
					        <div id="page-loader">Loading page ...</div>
					    </div>
					     <div class="col-sm-4" style="margin-top: 50px;">
					              
					     <h3>Thông tin chung</h3>
								       <div id="results"> 
											
										</div>
										<div id="results1">
											
										</div>
						           
						 </div>
			  </div>
			</div>
		</div>
		<button class="btn btn-primary nextBtn pull-right btn-sm" type="button" onclick="">Tiếp theo</button>
	</div>
</div>
<script>

    var _PDF_DOC,
        _CURRENT_PAGE,
        _TOTAL_PAGES,
        _PAGE_RENDERING_IN_PROGRESS = 0,
        _CANVAS = document.querySelector('#pdf-canvas');

    // initialize and load the PDF
    async function showPDF(pdf_url) {
        document.querySelector("#pdf-loader").style.display = 'block';

        // get handle of pdf document
        try {
            _PDF_DOC = await pdfjsLib.getDocument({url: pdf_url});
        } catch (error) {
            alert(error.message);
        }

        // total pages in pdf
        _TOTAL_PAGES = _PDF_DOC.numPages;

        // Hide the pdf loader and show pdf container
        document.querySelector("#pdf-loader").style.display = 'none';
        document.querySelector("#pdf-contents").style.display = 'block';
        document.querySelector("#pdf-total-pages").innerHTML = _TOTAL_PAGES;

        // show the first page
        showPage(1);
    }

    // load and render specific page of the PDF
    async function showPage(page_no) {
        _PAGE_RENDERING_IN_PROGRESS = 1;
        _CURRENT_PAGE = page_no;

        // disable Previous & Next buttons while page is being loaded
        document.querySelector("#pdf-next").disabled = true;
        document.querySelector("#pdf-prev").disabled = true;

        // while page is being rendered hide the canvas and show a loading message
        document.querySelector("#pdf-canvas").style.display = 'none';
        document.querySelector("#page-loader").style.display = 'block';

        // update current page
        document.querySelector("#pdf-current-page").innerHTML = page_no;

        // get handle of page
        try {
            var page = await _PDF_DOC.getPage(page_no);
        } catch (error) {
            alert(error.message);
        }

        // original width of the pdf page at scale 1
        var pdf_original_width = page.getViewport(1).width;

        // as the canvas is of a fixed width we need to adjust the scale of the viewport where page is rendered
        var scale_required = _CANVAS.width / pdf_original_width;

        // get viewport to render the page at required scale
        var viewport = page.getViewport(scale_required);

        // set canvas height same as viewport height
        _CANVAS.height = viewport.height;

        // setting page loader height for smooth experience
        document.querySelector("#page-loader").style.height = _CANVAS.height + 'px';
        document.querySelector("#page-loader").style.lineHeight = _CANVAS.height + 'px';

        // page is rendered on <canvas> element
        var render_context = {
            canvasContext: _CANVAS.getContext('2d'),
            viewport: viewport
        };

        // render the page contents in the canvas
        try {
            await page.render(render_context);
        } catch (error) {
            alert(error.message);
        }

        _PAGE_RENDERING_IN_PROGRESS = 0;

        // re-enable Previous & Next buttons
        document.querySelector("#pdf-next").disabled = false;
        document.querySelector("#pdf-prev").disabled = false;

        // show the canvas and hide the page loader
        document.querySelector("#pdf-canvas").style.display = 'block';
        document.querySelector("#page-loader").style.display = 'none';
    }
   
    // click on "Show PDF" button
   // document.querySelector("#show-pdf-button").addEventListener('click', function () {
   //     this.style.display = 'none';
        showPDF('http://localhost:8080/viewpdf/byte?path=${urlFile}');
 //   }); 

     console.log(${LINK_ADMIN })

    // click on the "Previous" page button
    document.querySelector("#pdf-prev").addEventListener('click', function () {
        if (_CURRENT_PAGE != 1)
            showPage(--_CURRENT_PAGE);
    });

    // click on the "Next" page button
    document.querySelector("#pdf-next").addEventListener('click', function () {
        if (_CURRENT_PAGE != _TOTAL_PAGES)
            showPage(++_CURRENT_PAGE);
    });

</script>

<script>
  //  $('#show-pdf-button').trigger('click');

  /*   var coordinates = function(element) {
        element = $(element);
        var top = element.position().top;
        var left = element.position().left;
       // $('#results').text('X: ' + left + ' ' + 'Y: ' + top);
        $('#results').html(" Người nhận</br><select name='cars' id='cars'>  <option value='volvo'>trong</option> <option value='volvo'>nam</option></select> </br>VỊ TRÍ - KÍCH THƯỚC</br> <div class='row'> <div class='col-sm-6'>X</br><input style='width: 70px;' value='"+top+"'/></div> <div class='col-sm-6'>Y</br><input style='width: 70px;' value='"+left+"'/></div></div>  ");
    }
    var coordinates1 = function(element) {
        element = $(element);
        var top = element.position().top;
        var left = element.position().left;
      //  $('#results1').text('X: ' + left + ' ' + 'Y: ' + top);
        $('#results1').html("Người nhận</br><select name='cars' id='cars'> <option value='volvo'>trong</option> <option value='volvo'>nam</option></select> </br>VỊ TRÍ - KÍCH THƯỚC <p>X :"+top+" Y :"+left+"</p> ");
    }
	

    $('#box1').draggable({
        start: function() {
            coordinates('#box1');
        },
        stop: function() {
            coordinates('#box1');
        }
    });
	$('#box2').draggable({
        start: function() {
            coordinates1('#box2');
        },
        stop: function() {
            coordinates1('#box2');
        }
    });
	 */
	
	 function renderPdfByUrl(url) {
			var currPage = 1; 
			var numPages = 0;
			var thePDF = null;
		//---------------------------------------------------------------------
			//This is where you start
			pdfjsLib.getDocument(url).then(function(pdf) {

				//Set PDFJS global object (so we can easily access in our page functions
				thePDF = pdf;

				//How many pages it has
				numPages = pdf.numPages;
				
				//Start with first page
				pdf.getPage(1).then(handlePages);
			});


			function handlePages(page) {					
				//This gives us the page's dimensions at full scale
				var viewport = page.getViewport(1);
				
				//We'll create a canvas for each page to draw it on
				var div = document.createElement("div");
				div.classList.add("otherclass");
				div.style.height = viewport.height+"px";
				div.style.width = viewport.width+"px";
				
				var canvas = document.createElement("canvas");
				canvas.style.display = "block";
				var context = canvas.getContext('2d');
				canvas.height = viewport.height;
				canvas.width = viewport.width;
				canvas.classList.add(page.pageIndex);
				//Draw it on the canvas
				page.render({
					canvasContext: context,
					viewport: viewport
				});
				div.appendChild(canvas)
				//Add it to the web page
				document.body.appendChild(div);

				//Move to next page
				currPage++;
				if (thePDF !== null && currPage <= numPages) {
					thePDF.getPage(currPage).then(handlePages);
				} else {
					$( "#draggable" ).draggable({
						helper: 'clone'
					});
					
					$( ".otherclass" ).droppable({
					  drop: function( event, ui ) {
						var clone1 = $(ui.draggable).clone();
						if(!clone1.hasClass('item')) {
							clone1.addClass("item");
							clone1.removeAttr("id");
							$(this).append(clone1);
							clone1.resizable({
								containment: "parent"
							}).draggable({
								containment: 'parent',
								scroll: false,
								drag: function() {
									var $this = $(this);
									var thisPos = $this.position();
									var parentPos = $this.parent().position();

									var x = thisPos.left - parentPos.left;
									var y = thisPos.top - parentPos.top;

									$this.find("span").text("Page: "+$(this).parent().find("canvas").attr("class")+", " +x + ", " + y);
								}
							});
							
						}
					  }
					});
				}
			}
		}
</script>