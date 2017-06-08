app.service('amendmentService', ['$http', 'amendmentConstants', function($http, amendmentConstants){
	var url = '/amendment';

    this.getData = function() {
	    return $http.get(url + "/data");
    };
	 this.getReferenceElement = function(selected) {
		 var appendix = selected.act.trim();
		 if(selected.member){
			 appendix +="/"+selected.member.trim();
			 if(selected.paragraph){
				 appendix +="/"+selected.paragraph.trim();
			 	 if(selected.clause){
					 appendix +="/"+selected.clause.trim();
					 if(selected.subclause){
						 appendix +="/"+selected.subclause.trim();
					 }
				 }
		 	}
		 }
	    return $http.get(url + "/getReferenceElement/"+appendix);
    };
	 this.getMembersByAct = function(name) {
		return $http.get(url + "/getMembersByAct/"+name);
    };
	 this.getMember = function(name,id) {
		return $http.get(url + "/getMember/"+name+"/"+id);
    };
	this.getByUser = function(email) {
		return $http.get(url + "/getByUser/" + email + "/all");
	}

	this.delete = function(id) {
		return $http.delete(url + "/" + id);
	}

	this.getByAct = function(actName) {
		return $http.get(url + "/getByAct/" + actName);
	}

	this.acceptAmendment = function(id) {
		var amendment = { id : id };
		return $http.patch(url + "/accept", amendment);
	}

	this.getAllByActAndOperationType = function(actName, operation) {
		return $http.get(url + "/getByAct/" + operation + "/" + actName);
	}

	this.getNextOperation = function(currentOperation) {
		if(currentOperation === amendmentConstants.operations.DELETE) {
			return amendmentConstants.operations.UPDATE;
		} else if (currentOperation === amendmentConstants.operations.UPDATE) {
			return amendmentConstants.operations.APPEND;
		} else {
			return amendmentConstants.operations.DELETE;
		}
	}

	this.hasNextOperation = function(currentOperation) {
		return currentOperation !== amendmentConstants.operations.APPEND;
	}

	this.hasAmendmentsForOperation = function(currentOperation, act) {
		if(currentOperation === amendmentConstants.operations.DELETE) {
			return act.numberOfDeleteAmendments > 0;
		} else if (currentOperation === amendmentConstants.operations.UPDATE) {
			return act.numberOfUpdateAmendments > 0;
		} else {
			return act.numberOfAppendAmendments > 0;
		}
	}

	this.generatePDF = function(amendmentId) {
		return $http.get(url + "/generatePDF/" + amendmentId);
	}

	this.generateXHTML = function(amendmentId) {
		return $http.get(url + "/generateXHTML/" + amendmentId);
	}
	this.addAmendment = function(amendment) {
		return $http.post(url + "/addAmendment",amendment);
	}
}]);
