app.service('actService', function($http){
	var url = '/act';

	this.getAllActNames = function(){
		return $http.get(url + "/allActNames");
	}
	this.getAllActNamesAccepted = function(){
		return $http.get(url + "/allActNamesPrihvaceni");
	}
	this.writeAct = function(act){
		return $http.post(url + "/addAct", act);
	}
	this.filter = function(filter, type) {
    var appendix = "/" + type + "-filter/"+filter;
		return $http.get(url + appendix);
	}
   this.getActsInProgressMock = function() {
        return $http.get(url + "/all");
   }
   this.getClan = function(naziv) {
        return $http.get(url + "/getClan/"+naziv);
   }
   this.getActsInProgres = function() {
        return $http.get(url + "/actsInProgress");
   }
	this.filterByMetaData = function(metaData) {
		metaData.votesFor = -1;
		metaData.votesReserved = -1;
		metaData.votesAgainst = -1;
		if(metaData.votesFor1 && metaData.votesFor1 > -1) {
			metaData.votesFor = metaData.votesFor1;
		}
		if(metaData.votesReserved1 && metaData.votesReserved1 > -1) {
			metaData.votesReserved = metaData.votesReserved1;
		}
		if(metaData.votesAgainst1 && metaData.votesAgainst1 > -1) {
			metaData.votesAgainst = metaData.votesAgainst1;
		}
		return $http.post(url + "/meta-data-filter", metaData);
	}

  this.getAll = function() {
    return $http.get(url + "/all");
  }

	this.getByUser = function(email) {
		return $http.get(url + "/getByUser/" + email + "/all");
	}

	this.delete = function(name) {
		return $http.delete(url + "/" + name);
	}

	this.getActsInProgress = function() {
		return $http.get(url + "/allInProgress");
	}

	this.acceptAct = function(actName, votesFor, votesReserved, votesAgainst) {
		var act = {
			name : actName,
			votesFor: votesFor,
			votesReserved: votesReserved,
			votesAgainst: votesAgainst
		 };
		return $http.patch(url + "/accept", act);
	}

	this.hasAmendments = function(act) {
		return act.numberOfDeleteAmendments + act.numberOfUpdateAmendments + act.numberOfAppendAmendments > 0;
	}

	this.generatePDF = function(actName) {
		return $http.get(url + "/generatePDF/" + actName);
	}

	this.generateXHTML = function(actName) {
		return $http.get(url + "/generateXHTML/" + actName);
	}

	this.getNumber = function() {
		return $http.get(url+"/getNumber");
	}

	this.setNumber = function(number) {
		return $http.post(url+"/setNumber/" + number);
	}
});
