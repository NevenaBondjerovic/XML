app.controller('withdrawalController',
				['$scope', '$window', 'userService', 'actService', 'amendmentService',
				function($scope, $window, userService, actService, amendmentService){

	function init(){
		if( !localStorage.userInfo) {
			$scope.$parent.user = undefined;
		}
		if(!$scope.$parent.user) {
			if(localStorage.userInfo){
				userService.getUserByEmail(localStorage.userInfo).then(
					function(response) {
						 $scope.$parent.user = response.data;
						 $scope.readInitUserData();
					}, function(response) {
						$window.location.href = "#/login";
					}
				);
			} else {
				$window.location.href = "#/login";
			}
		} else {
			$scope.readInitUserData();
		}

		$scope.isWithdrawal = {};
		$scope.acts = [];
		$scope.amendments = [];
	}

	$scope.readInitUserData = function() {

		actService.getByUser($scope.$parent.user.email).then(
			function(response) {
				$scope.acts = response.data;
			}
		);

		amendmentService.getByUser($scope.$parent.user.email).then(
			function(response) {
				$scope.amendments = response.data;
			}
		);
	}

	init();

	$scope.withdrawAmendmentProposal = function(event, index) {
		var amendmentId = event.currentTarget.id;
		if(amendmentId) {
			amendmentService.delete(amendmentId).then(
				function(response) {
					$scope.amendments.splice(index, 1);
				},
				 function(response) {
					 alert("Neuspešno povlačenje amandmana!");
				 }
			);
		}
	}

	$scope.withdrawActProposal = function(event, index) {
		var actName = event.currentTarget.id;
		if(actName) {
			actService.delete(actName).then(
				function(response) {
					$scope.acts.splice(index, 1);
				},
				function(response) {
						alert("Neuspešno povlačenje akta!");
				}
			);
		}
	}
}]);
