
app.controller('loginController', ['$scope', '$window', 'userService', function($scope, $window, userService){

	function init(){
		if(!$scope.$parent.user) {
			if(localStorage.userInfo){
				userService.getUserByEmail(localStorage.userInfo).then(
					function(response) {
					 	$scope.$parent.user = response.data;
					 	$window.location.href = "#/";
					}
				);
			}
		} else {
			$window.location.href = "#/";
		}
		$scope.user = {}
	}

	init();

	$scope.login = function() {
		if($scope.formIsValid()) {
			userService.login(angular.toJson($scope.user)).then(
					function(response){
						userService.getUserByEmail($scope.user.email).then(
							function(response) {
								$scope.$parent.user = response.data;
								localStorage.userInfo = response.data.email;
								$window.location.href = "#/";
							}
						);
					},
					function(response){
						alert("Neuspešna prijava!");
					}
			);
		} else {
			alert("Forma nije validna!");
		}
	};

	$scope.formIsValid = function(){
		return $scope.user.email && $scope.user.password && $scope.user.password.trim().length > 0;
	}
}]);
