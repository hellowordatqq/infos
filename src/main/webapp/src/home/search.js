define(['require', 'bootpag'], function (require) {
	if(totalPage > 0) {
		$('#page-selection').bootpag({
			total: totalPage,
			page: pageNum,
			maxVisible: 10
		}).on('page', function(event, num){
			document.location.href = "/blogList?rootCategory=" + rootCategory + "&childCategory=" + childCategory + "&pageNumber=" + num;
		});
	}
})