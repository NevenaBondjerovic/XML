app.service('sessionService', function($http){
	var url = '/session';
	this.isSessionInProgress = function(){
		return $http.get(url + "/inProgress");
	}

  this.startSession = function(numberOfCouncilors){
		return $http.post(url + "/startSession",  numberOfCouncilors);
	}

  this.endSession = function(){
		return $http.post(url + "/endSession");
	}

	this.startDiscuissionInDetail = function(actName){
		return $http.post(url + "/startDiscuissionInDetail", actName);
	}

	this.endDiscuissionInDetail = function(actName){
		return $http.post(url + "/endDiscuissionInDetail");
	}

	this.getActForAmendments = function(){
		return $http.get(url + "/getActForAmendments");
	}
});
