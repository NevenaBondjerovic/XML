app.controller('appController', ['$scope', '$window', '$location', 'userService', 'actService', 'amendmentService', 'sessionService',
								function($scope, $window, $location, userService, actService, amendmentService, sessionService){

	function init(){
		if(localStorage.userInfo && !$scope.user){
			userService.getUserByEmail(localStorage.userInfo).then(
				function(response) {
					 $scope.user = response.data;
				}
			);
		}
	}

	init();

  $scope.logout = function() {
    $scope.user = undefined;
		localStorage.removeItem('userInfo')
    $window.location.href = "#/login";
  }

	$scope.downloadActPDF = function(act) {
		actService.generatePDF(act.name).then(
			function(response) {
				$scope.genericDownload(response.data.path, response.data.fileName);
			}, function(response) {
				alert("Neuspešno generisanje PDF-a");
			}

		);
	}

	$scope.downloadActXHTML = function(act) {
		actService.generateXHTML(act.name).then(
			function(response) {
				$scope.genericDownload(response.data.path, response.data.fileName);
			}, function(response) {
				alert("Neuspešno generisanje XHTML-a");
			}
		);
	}

	$scope.downloadAmendmentPDF = function(amendment) {
		amendmentService.generatePDF(amendment.id).then(
			function(response) {
				$scope.genericDownload(response.data.path, response.data.fileName);
			}, function(response) {
				alert("Neuspešno generisanje PDF-a");
			}
		);
	}

	$scope.downloadAmendmentXHTML = function(amendment) {
		amendmentService.generateXHTML(amendment.id).then(
			function(response) {
				$scope.genericDownload(response.data.path, response.data.fileName);
			}, function(response) {
				alert("Neuspešno generisanje XHTML-a");
			}
		);
	}

	$scope.genericDownload = function(path, fileName) {
		var name = "";

		if(fileName) {
			name = fileName;
		}
		var downloadLink = angular.element('<a></a>');
		downloadLink.attr('href', path);
		downloadLink.attr('download', name);
		downloadLink[0].click();
	}

	$scope.redirectToNewAct = function() {
		sessionService.isSessionInProgress().then(
			function() {
				alert("Predlaganje akata je zatvoreno jer je sednica u toku!");
				$window.location.href = "#/";
			}, function() {
				$window.location.href = "#/newAct";
			}
		);
	}

	$scope.redirectToNewAmendment = function() {
		sessionService.getActForAmendments().then(
			function(response) {
				if(response.data.currentAct == null) {
					alert("Predlaganje amandmana je zatvoreno jer nije u toku period predlaganja amandmana na akt na sednici!");
					$window.location.href = "#/";
				}else {
						$window.location.href = "#/newAmendment";
				}
			}, function() {	}
		);
	}

	$scope.amendmentPreview = function(amendment) {
		amendmentService.generateXHTML(amendment.id).then(
			function(response) {
				$window.open(response.data.path);
			}, function(response) {
				alert("Nije moguć prikaz amandmana.");
			}
		);
	}

	$scope.actPreview = function(act) {
		actService.generateXHTML(act.name).then(
			function(response) {
				$window.open(response.data.path);
			}, function(response) {
				alert("Nije moguć prikaz akta.");
			}
		);
	}
}]);
