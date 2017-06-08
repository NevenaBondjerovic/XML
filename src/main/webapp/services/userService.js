app.service('userService', function($http){
	var url = '/user';
	this.register = function(user){
		return $http.post(url + "/register", user);
	}
	
	this.login = function(user){
		return $http.post(url + "/login", user);
	}

	this.getUserByEmail = function(email) {
		return $http.get(url + "/" + email + "/one");
	}
});
