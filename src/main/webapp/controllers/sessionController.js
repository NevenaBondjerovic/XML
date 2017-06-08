app.controller('sessionController', ['$scope', '$window', 'userService', 'actService', 'amendmentService', 'amendmentConstants', 'sessionService',
 																		function($scope, $window, userService, actService, amendmentService, amendmentConstants, sessionService){

	function init(){
    $scope.session = {};
		if( !localStorage.userInfo) {
			$scope.$parent.user = undefined;
		}
    $scope.getSessionData();
		if(!$scope.$parent.user) {
			if(localStorage.userInfo){
				userService.getUserByEmail(localStorage.userInfo).then(
					function(response) {
						 $scope.$parent.user = response.data;
						 if(response.data.role !== 'Predsednik') {
				 			$window.location.href = "#/";
						} else {	//ako je pokupio korisnika sa emailom iz localStorage-a i to je predsednik, procitaj podatke
							$scope.readInitUserData();
						}
					}, function(response) {
						$window.location.href = "#/login";
					});
			} else {	//ako ne postoji korisnik ni na appController-u ni u local sotrage => nije niko prijavljen
				$window.location.href = "#/login";
			}
		} else { //ako postoji user u appControleru
				if($scope.$parent.user.role !== 'Predsednik') {	//ako ulogovani korisnik nije predsednik
			 		$window.location.href = "#/";
		 		} else {	//i ako je predsednik, procitaj podatke
					$scope.readInitUserData();
				}
	 		}
	}

  $scope.getSessionData = function() {
    sessionService.isSessionInProgress().then(
      function(response) {
        $scope.inPogress = true;
        $scope.session.councilors = response.data;
      }, function() {
        $scope.inPogress = false;
        $scope.session.councilors = 0;
      }
    );
  }

	$scope.readInitUserData = function() {
		$scope.acts = [];
		$scope.amendments = [];
		$scope.isSession = {};
		//true/false trigger za tok razmatranja amandmana.
		$scope.consideringAmendments = undefined;

		actService.getActsInProgress().then(
			function(response) {
				$scope.acts = response.data;
        sessionService.getActForAmendments().then(
          function(response) {
            var name, index = -1;
            name = response.data.currentAct;
            if(name !== null) {
              angular.forEach($scope.acts, function(value) {
                if(value.name !== name) {
                  index++;
                }
              });
              
              $scope.allowAmendmentProposing(index);
            }
          }
        );
			},
			function(response) {
				alert("Ne postoje akti za sednicu!");
				$window.location.href = "#/";
			}
		);
	}

	init();

	$scope.acceptAmendment = function(index) {
		var amendmentId = $scope.amendments[index].id;
		if(amendmentId) {
			amendmentService.acceptAmendment(amendmentId).then(
				function(response) {
          var act, clause, currentAmendment;

          act = $scope.amendments[index].referredActName;
          clause = $scope.amendments[index].referredClauseNumber;
					$scope.amendments.splice(index, 1);
					if($scope.currentAmendmentOperation === amendmentConstants.operations.DELETE && $scope.amendments.length > 0) {
						//ako je prihvacen amandman za brisanje i ima jos amandmana za brisanje, proci kroz amandmane i obrisati one koji brisu isti...
            angular.forEach($scope.amendments, function(value,index){
              currentAmendment = $scope.amendments[index];
              if(currentAmendment.referredActName === act && currentAmendment.referredClauseNumber === clause) {
                $scope.amendments.splice(index, 1);
              }
            });
					}
          $scope.update();
				},
				 function(response) {
					 alert("Neuspešno prihvatanje amandmana!");
				 }
			);
		}
	}

	$scope.rejectAmendment = function(index) {
		var amendmentId = $scope.amendments[index].id;
		if(amendmentId) {
			amendmentService.delete(amendmentId).then(
				function(response) {
					$scope.amendments.splice(index, 1);
					$scope.update();
				},
				 function(response) {
					 alert("Neuspešno odbijanje amandmana!");
				 }
			);
		}
	}

	$scope.update  = function() {
		//ako se operacije vrse nad amandmanima akta, i ako ih nema vise u nizu
		if($scope.consideringAmendments && $scope.amendments.length === 0) {
			//ako je ispraznjen poslednji amandman poslednje operacije
			if(!amendmentService.hasNextOperation($scope.currentAmendmentOperation)) {
				$scope.consideringAmendments = undefined;  //resetuj === prikazi dijalog za prihvatanje akta u celosti
        $scope.acceptingInWhole = {};
        if(!$scope.acts.length && !$scope.consideringAmendments && !$scope.acceptingInWhole) {
          $scope.endSession();
        }
			} else { //ako ima dalje operacija,
				//prebaci trenutnu na sledecu
				$scope.currentAmendmentOperation = amendmentService.getNextOperation($scope.currentAmendmentOperation);
				//proveri da li sledeca ima amandmane
				if(amendmentService.hasAmendmentsForOperation($scope.currentAmendmentOperation, $scope.currentAct)) {
					//ako ima, pokupi amandmane za tu operaciju
					$scope.getAmendmentsForCurrentOperationAndAct();
				} else {
					//ako nema amandmana za tu operaciju, opet uradi update
					$scope.update();
				}

			}
		}
	}

	$scope.getAmendmentsForCurrentOperationAndAct = function() {
		if($scope.currentAct) {
			amendmentService.getAllByActAndOperationType($scope.currentAct.name, $scope.currentAmendmentOperation).then(
				function(response) {
					$scope.amendments = response.data;
					$scope.update();
				}
			);
		}
	}

  $scope.allowAmendmentProposing = function(actIndex) {
    $scope.currentActIndex = actIndex;
    $scope.waitingForAmendmentPropositions = true;
    sessionService.startDiscuissionInDetail($scope.acts[actIndex].name);
  }

	$scope.acceptActInPrinciple = function(index) {
		//zatvori predlaganje amandmana
    $scope.waitingForAmendmentPropositions = false;
    sessionService.endDiscuissionInDetail($scope.acts[index].name);

    //uzmi akt na koji je kliknuto prihvatanje u nacelu
		var act = $scope.acts[index];
		$scope.acts.splice(index, 1);

		//ako odabrani akt ima amandmana
		if(actService.hasAmendments(act)) {
			$scope.currentAct = act;
			//aktiviraj prikaz amandmana
			$scope.consideringAmendments = {};
      $scope.currentAmendmentOperation = amendmentConstants.operations.DELETE;
      //doklegod nema za tekucu operaciju amandmana....
      while(amendmentService.hasNextOperation($scope.currentAmendmentOperation)
            && !amendmentService.hasAmendmentsForOperation($scope.currentAmendmentOperation, $scope.currentAct)) {
        $scope.currentAmendmentOperation = amendmentService.getNextOperation($scope.currentAmendmentOperation);
      }
      $scope.getAmendmentsForCurrentOperationAndAct();
		} else { //ako nema amandmana, daj mu da prihvati u celosti
				alert("Ne postoje amandmani na akt " + act.name + ", tako da je on prihvaćen u celosti!");
				$scope.acceptActInWhole(act.name);
		}
	}

  $scope.rejectActInPrinciple = function(index) {
    var actName = $scope.acts[index].name;
    actService.delete(actName).then(
      function(response) {
        $scope.acts.splice(index, 1);
        if($scope.acts.length === 0) {
          $scope.endSession();
        }
      },
      function(response) {
        alert("Greška prilikom odbijanja akta u načelu!");
      }
    );
  }

	$scope.acceptActInWhole = function(actName) {
		var name;

		if(actName){
			name = actName
		} else {
			name = $scope.currentAct.name;
		}
    $scope.acceptingInWhole = undefined;
		actService.acceptAct(name, $scope.session.votesFor, $scope.session.votesReserved, $scope.session.votesAgainst).then(
      function() {
        if($scope.acts.length === 0) {
          $scope.endSession();
        }
      }, function() {
        if($scope.acts.length === 0) {
          $scope.endSession();
        }
      }
    );
	}

  $scope.rejectActInWhole = function() {
    actService.delete($scope.currentAct.name).then(
      function() {
        if($scope.acts.length === 0) {
          $scope.endSession();
        }
      }, function() {
        if($scope.acts.length === 0) {
          $scope.endSession();
        }
      }
    );;
    $scope.acceptingInWhole = undefined;
  }

  $scope.startSession = function() {
    if($scope.validateCouncilorsNumber()) {
      sessionService.startSession($scope.session.councilors).then(
        function(response) {
          $scope.inPogress = true;
        }, function() {
          alert("Sednica je već u toku, nemoguće je započeti novu!");
        }
      );
    } else {
      alert("Broj odbornika se mora uneti i mora biti pozitivan!")
    }
  }

  $scope.endSession = function() {
    sessionService.endSession().then(
      function(response) {
        $scope.inPogress = false;
        $scope.session.councilors = 0;
      }, function() {
        alert("Nijedna sednica nije u toku!");
      }
    );
  }

  $scope.validateCouncilorsNumber = function() {
    return $scope.session.councilors && $scope.session.councilors > 0;
  }

  $scope.concludeVotingInPrinciple = function(index) {
    if($scope.areVotesValid()) {
      if($scope.isAccepted()) {
        $scope.allowAmendmentProposing(index);
      } else {
        $scope.rejectActInPrinciple(index);
      }
    }
  }

  $scope.concludeVotingInWhole = function(index) {
    if($scope.areVotesValid()) {
      if($scope.isAccepted()) {
        $scope.acceptActInWhole($scope.currentAct.name);
      } else {
        $scope.rejectActInWhole();
      }
    }
  }

  $scope.concludeAmendmentVoting = function(index) {
    if($scope.areVotesValid()) {
      if($scope.isAccepted()) {
        $scope.acceptAmendment(index);
      } else {
        $scope.rejectAmendment(index);
      }
    }
  }

  $scope.areVotesValid = function(){
    if($scope.session.votesFor >-1 && $scope.session.votesReserved >-1 && $scope.session.votesAgainst >-1 &&
      $scope.session.votesFor + $scope.session.votesReserved + $scope.session.votesAgainst === $scope.session.councilors) {
        return true;
      }else {
        alert("Glasovi nisu validni! Svi moraju biti unešeni, pozitivni brojevi, i zbir im mora biti " + $scope.session.councilors);
        return false;
      }
  }

  $scope.isAccepted = function() {
    if($scope.session.votesFor > $scope.session.councilors/2) {
        return true;
    }else {
      return false;
    }
  }

}]);
