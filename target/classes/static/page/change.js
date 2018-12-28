$( function ()
{	
	$(".zx_bt li").each(function(i){
		$(this).hover(function(){
			

			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			$(".zx_nrs").css("display","none");
			$(".zx_nrs").eq(i).css("display","block");
		})
	})
});

$( function ()
{	
	$("#tese li").each(function(i){
		$(this).hover(function(){
			

			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			$(".ts_mains").css("display","none");
			$(".ts_mains").eq(i).css("display","block");
		})
	})
});

