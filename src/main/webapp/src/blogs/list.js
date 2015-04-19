define(['require', 'bootpag'], function (require) {
	if(totalPage > 0) {
		$('#page-selection').bootpag({
			total: totalPage,
			page: pageNum,
			maxVisible: 10
		}).on('page', function(event, num){
			var s = $('#s').value;
			document.location.href = "/search?s=" + s + "&pageNumber=" + num;
		});
	}
})