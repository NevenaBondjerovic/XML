app.controller('registrationController', ['$scope', '$window', 'userService', function($scope, $window, userService){

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
		$scope.user = {};
	}

	init();

	$scope.register = function(){
		if($scope.isFormValid()){
			userService.register(angular.toJson($scope.user)).then(
					function(response){
						$window.location.href = "#/login";
					},
					function(response){
						alert("Vec postoji korisnik sa tom email adresom");
					}
			)
		} else {
			alert("Forma nije validna!")
		}
	};

	$scope.isFormValid = function() {
		return $scope.user.email && $scope.user.name && $scope.user.password && $scope.user.surname && $scope.user.password.trim().length > 0;
	};
}]);
