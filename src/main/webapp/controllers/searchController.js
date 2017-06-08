app.controller('searchController',  ['$scope', '$window', 'actService', 'userService', function($scope, $window, actService, userService){

	function init(){
		if(localStorage.userInfo && !$scope.$parent.user){
			userService.getUserByEmail(localStorage.userInfo).then(
				function(response) {
					 $scope.$parent.user = response.data;
				}
			);
		}
		$scope.isRefFilter = {};
    $scope.ref = {};
    $scope.akt = {};
		$scope.acts = [];
    actService.getAll().then(
      function(response) {
        $scope.allActs = response.data;
      }
    );
  }

	init();

	$scope.filter = function(){
		$scope.acts = {};
    if($scope.areSearchParamsValid()) {
      var filter;

			if($scope.searchType !== 'meta-data') {
	      if($scope.searchType == 'refs') {
	        filter = $scope.ref.act.name;
	      } else if($scope.searchType == 'content') {
	        filter = $scope.akt.keyword;
	      }
	  		actService.filter(filter.trim(), $scope.searchType).then(
	  				function(response){
	            $scope.acts = response.data;
							if(!$scope.acts.length) {
								alert("Ne postoji nijedan akt koji zadovoljava uslove pretrage.");
							}
	  				}
	  		);
			} else {
				actService.filterByMetaData($scope.metaData).then(
	  				function(response){
	            $scope.acts = response.data;
							if(!$scope.acts.length) {
								alert("Ne postoji nijedan akt koji zadovoljava uslove pretrage.");
							}
	  				}
	  		);
			}
    }else {
      alert("Forma nije validna.");
    }
	}

	$scope.onChange = function() {
		$scope.metaData = {};
		$scope.metaData.votesFor = -1;
		$scope.metaData.votesReserved = -1;
		$scope.metaData.votesAgainst = -1;
		$scope.acts = [];
	}

  $scope.areSearchParamsValid = function() {
		var contentValid, refValid, metaDataValid, isIntervalValid;

		contentValid = $scope.searchType === 'content' && $scope.akt.keyword && $scope.akt.keyword.trim().length > 0;
		refValid = $scope.searchType === 'refs' && $scope.ref.act && $scope.ref.act.name.trim().length > 0;

		isIntervalValid = true;
		if($scope.metaData.dateFrom && $scope.metaData.dateTo) {
				isIntervalValid = $scope.metaData.dateFrom < $scope.metaData.dateTo;
		}

		metaDataValid = $scope.searchType === 'meta-data' && ($scope.metaData.status || $scope.metaData.name) && isIntervalValid;



    return contentValid || refValid || metaDataValid ;
  }

	$scope.open = function() {
		$scope.popup1.opened = true;
	}

}]);
