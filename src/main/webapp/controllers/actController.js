app.controller('actController', ['$scope', '$window', 'userService', 'actService', 'amendmentService', function($scope, $window, userService, actService, amendmentService){

	function init(){
		if( !localStorage.userInfo) {
			$scope.$parent.user = undefined;
		}
		if(!$scope.$parent.user) {
			if(localStorage.userInfo){
				userService.getUserByEmail(localStorage.userInfo).then(
					function(response) {
						 $scope.$parent.user = response.data;
					}, function(response) {
						$window.location.href = "#/login";
					}
				);
			} else {
				$window.location.href = "#/login";
			}
		}
		actService.getAllActNames().then(
			function(response) {
				$scope.allActNames = response.data;
			}
		);
	
		actService.getAllActNamesAccepted().then(
				function(response) {
					$scope.allActNamesAccepted = response.data;
				}
			);
		
		$scope.imaClan = false;
		$scope.tree = []
		$scope.pododeljak = true;
		$scope.clan = true;
		$scope.references = [];
		$scope.selectedRef = [];
	}

	init();

	function contains(a, obj) {
	    for (var i = 0; i < a.length; i++) {
	        if (a[i] === obj) {
	            return true;
	        }
	    }
	    return false;
	}

	$scope.createAct = function(){
		var tree = $scope.tree
		var allNames = [];
		putNames(tree, allNames)
		hasClan($scope.tree);
		convertData($scope.tree);
		if (!hasDuplicates(allNames) && !contains($scope.allActNames, tree[0].name) && $scope.imaClan){
			actService.writeAct(angular.toJson(tree[0])).then(
				function(response){
					$scope.allActNames.push(tree[0].name);
					$scope.tree = [];
				}
			);
		}else{
			alert("Akt sa takvim nazivom vec postoji, ili postoji duplikat imena u aktu ili nema clan");
		}
	}

	function convertData(tree){
		for (var item in tree){
			if (tree[item].content){
				$scope.parseContentToObjects(tree[item].content);
				//console.log(tree[item].content + " Tree content");
				tree[item].content = $scope.resultContent;
				//console.log($scope.resultContent + " Result content")
			}
			if (tree[item].children){
				convertData(tree[item].children);
			}
		}
	}
	
	function hasClan(tree){
		for (var item in tree){
			if (tree[item].type == "Clan"){
				$scope.imaClan = true;
				return;
			}else{
				if (tree[item].children){
					hasClan(tree[item].children);
				}
			}
		}
	}

	function putNames(tree, allNames){
		for (var item in tree){
			if(tree[item].type !== "Stav") {
				allNames.push(tree[item].name);
				if (tree[item].children){
					putNames(tree[item].children, allNames);
				}
			}
		}
	}

	function hasDuplicates(array) {
	    var valuesSoFar = Object.create(null);
	    for (var i = 0; i < array.length; ++i) {
	        var value = array[i];
	        if (value in valuesSoFar) {
	            return true;
	        }
	        valuesSoFar[value] = true;
	    }
	    return false;
	}

	$scope.initialiseAct = function(){
		if (!$scope.tree.length){
			$scope.clanCounter = 1;
			var akt = {}
			akt.name = $scope.aktNaziv;
			akt.childType = "Deo";
			akt.type = "Akt";
			akt.id = 1;
			akt.children = [];
			akt.predlagac = localStorage.userInfo;
			$scope.tree.push(akt);
		}else{
			$scope.tree[0].name = $scope.aktNaziv;
		}
	}

	$scope.discardAct = function(){
		$scope.tree = [];
		$scope.pododeljak = true;
		$scope.clan = true;
	}

	$scope.addSubelement = function(data, pdOrC){
		var name = "";
		var type = "";
		var post = data.children.length + 1;
		var id;

		if(data.type.includes("Akt")){
			name  = "Deo";
			type = "Deo";
			cType = "Glava";
		}
		else if(data.type.includes("Deo")){
			name  = "Glava";
			type = "Glava";
			cType = "Odeljak";
		}
		else if(data.type.includes("Glava")){
			name  = "Odeljak";
			type = "Odeljak";
			cType = "Pododeljak";
		}
		else if(data.type.includes("Odeljak")){
			if (pdOrC === "Pododeljak"){
				name  = "Pododeljak";
				type = "Pododeljak";
				cType = "Clan";
				$scope.clan = false;
			}else if (pdOrC === "Clan"){
				name  = "Clan" + $scope.clanCounter;
				type = "Clan";
				cType = "Stav";
				id = $scope.clanCounter++;
				$scope.pododeljak = false;
			}
		}
		else if(data.type.includes("Pododeljak")){
			name = "Clan" + $scope.clanCounter;
			id = $scope.clanCounter++;
			type = "Clan";
			cType = "Stav";
		}
		else if(data.type.includes("Clan")){
			name  = "Stav";
			type = "Stav";
			cType = "Tacka";
		}
		else if(data.type.includes("Stav")){
			name = "Tacka";
			type = "Tacka";
			cType = "Podtacka";
		}
		else if(data.type.includes("Tacka")){
			name = "Podtacka";
			type = "Podtacka";
			cType = "Alineja";
		}
		else if(data.type.includes("Podtacka")){
			name = "Alineja";
			type = "Alineja";
			cType = null;
		}


		var parentId = data.id;
		var newName;
		if (!(data.childType == "Clan")){
			id = post;
			//newName = name + '-' + parentId + post;
			newName = data.name + " " + name + post;
		}else{
			newName = name;
		}
		data.children.push({id: id, parentId: parentId, name: newName, type: type, childType : cType, children: []});
	}

	$scope.deleteElement = function(data, tree1){
		$scope.selected = null;
		for (var item in tree1){
			if(tree1[item].name == data.name){
				tree1.splice(item,1);
			}
			else{
				if(tree1[item].children){
					$scope.deleteElement(data, tree1[item].children);
				}
			}
		}
	};

	$scope.findElementTree = function(tree){
		 for(var item in tree){
				if(tree[item].name == $scope.selected.name){
					if(tree[item].content){
						$scope.selectedContent = tree[item].content;
					}else{
						return false;
					}
				}
				else{
					if(tree[item].children){
						$scope.findElementTree(tree[item].children);
					}
				}
			}
	}

	$scope.clickedElement = function(data, event){
		 $scope.selectedContent = [];
		 $scope.selected = data;
		 $scope.sadrzajIzmenjiv = false;
		 $scope.imeIzmenljivo = false;
		 if (data.type == "Clan" || data.type == "Akt" || data.type == "Odeljak" || data.type == "Deo" || data.type == "Pododeljak" || data.type == "Glava"){
			 $scope.imeIzmenljivo = true;
		 }
		 if (data.type == "Clan" || data.type == "Stav" || data.type == "Tacka" || data.type == "Podtacka" || data.type == "Alineja"){
			 $scope.sadrzajIzmenjiv = true;
			 $scope.findElementTree($scope.tree);
		 }
	}

	$scope.addContent = function(){
		if (CKEDITOR.instances.editor1.checkDirty()){
			$scope.AddchildrenOfElementTree($scope.tree);
		}else{
			alert("Ubacite sadržaj elementa.");
		}
		$scope.selected = [];
		$scope.selectedContent = [];
	}

	$scope.AddchildrenOfElementTree = function(tree){
		for(var item in tree){
			if(tree[item].name == $scope.selected.name){
				tree[item].content = $scope.selectedContent;
				alert("Ubačen sadržaj.");
			}
			else{
				if(tree[item].children){
					$scope.AddchildrenOfElementTree(tree[item].children);
				}
			}
		}
	};
	
	$scope.getElements = function(){
		
		 var ret = " ";
		 if($scope.selectedRef.act){
		 	ret+= $scope.selectedRef.act.trim();
			 if($scope.selectedRef.member){
				 ret+="^"+$scope.selectedRef.member.trim();
				 if($scope.selectedRef.paragraph){
					 ret+= "^"+$scope.selectedRef.paragraph.trim();
					 if($scope.selectedRef.clause){
						 ret+= "^"+$scope.selectedRef.clause.trim();
						 if($scope.selectedRef.subclause){
							 ret+= "^"+$scope.selectedRef.subclause.trim();
							 if($scope.selectedRef.indent){
								 ret+= "^"+$scope.selectedRef.indent.trim();
							 }
						 }
					 }
				 }
			 }
		 }
		 return ret;
  }
	 $scope.changeAct = function(){		
		 delete $scope.selectedRef.member;
		 delete $scope.selectedRef.paragraph;
		 delete $scope.selectedRef.clause;
		 delete $scope.selectedRef.subclause;
		 delete $scope.selectedRef.indent;
	     $scope.paragraphs = {};
		 $scope.clauses = {};
		 $scope.subclauses = {};
		 $scope.indents = {};
		 amendmentService.getReferenceElement($scope.selectedRef).then(
			function(response){		
				 if(response.data.type == 'member'){
					 $scope.members = response.data.data;
							
				 }
			}
		); 
	 };
	 $scope.changeMember = function(){	
		 delete $scope.selectedRef.paragraph;
		 delete $scope.selectedRef.clause;
		 delete $scope.selectedRef.subclause;
		 delete $scope.selectedRef.indent;
		 $scope.clauses = {};
		 $scope.subclauses = {};
		 $scope.indents = {};	
		
		 amendmentService.getReferenceElement($scope.selectedRef).then(
			function(response){		
				 if(response.data.type == 'paragraph'){
					 $scope.paragraphs = response.data.data;
						
				 }
			}
		); 
	 };
	 $scope.changeParagraph = function(){	
		 delete $scope.selectedRef.clause;
		 delete $scope.selectedRef.subclause;
		 delete $scope.selectedRef.indent;
		 $scope.subclauses = {};
		 $scope.indents = {};
		
		 amendmentService.getReferenceElement($scope.selectedRef).then(
			function(response){		
				 if(response.data.type == 'clause'){
					 $scope.clauses = response.data.data;
						
				 }
			}
		); 
	 };
	 $scope.changeClause = function(){	
		 delete $scope.selectedRef.subclause;
		 delete $scope.selectedRef.indent;
		 $scope.indents = {};
		
		 amendmentService.getReferenceElement($scope.selectedRef).then(
			function(response){		
				 if(response.data.type == 'subclause'){
					 $scope.subclauses = response.data.data;
					 		
				 }
			}
		); 
	 };
	 $scope.changeSubclause = function(){	
		
		 amendmentService.getReferenceElement($scope.selectedRef).then(
			function(response){		
				 if(response.data.type == 'indent'){
					 $scope.indents = response.data.data;
				 }
			}
		); 
	 };
	 
	
	   $scope.parseContentToObjects = function(x){
		  $scope.resultContent= [];
		  while(x.length > 0) {
			  var index = x.indexOf("<a href");
			  if(index > -1) {
				   var string = x.substring(0, index).split('&nbsp;').join(' ');
				   if(string.trim().length > 0){
					   $scope.resultContent.push(string);
				   }
				   x = x.substring(index+2);
				   var refIndex = x.indexOf("</a>");
				   var refText = x.substring(7, refIndex+4); //uzimam  "Akt2"> nesto </a> 
				   $scope.addReference(refText);
				   x = x.substring(refIndex+4);
			  } else {
				   var string = x.split('&nbsp;').join(' ');
				   if(string.trim().length > 0){
					   $scope.resultContent.push(string);
				   }
				   break;
			  }
		 }
		   console.log($scope.resultContent);
		   return $scope.resultContent;
	   };
	   $scope.addReference = function(ref){
		   $scope.reference = {};
		    for(var i = 0; i < ref.length; i++){
			   if(ref.charAt(i) == '"' && ref.substring(i+1,i+2) == '>'){
				   $scope.createRef(ref.substring(0,i),0);	
				    for(var j = i; j < ref.length; j++){
					   if(ref.charAt(j) == '<' && ref.substring(j+1,j+4) == '/a>'){
						   $scope.reference.content = ref.substring(i+2,j).split('&nbsp;').join(' ');
						   $scope.resultContent.push($scope.reference);
					   }
					}
			   }
			} 
		 //\  console.log($scope.reference);
	   }
	   $scope.createRef = function(ref){
		   
		console.log($scope.selectedRef.act);
		   var find = false;
		   var end = false;
		   k = 0;
		   
		  while(ref.length > 0) {
			  var index = ref.indexOf("^");
			  if(index > -1) {
				   var string = ref.substring(0, index);
				   if(string.includes($scope.selectedRef.act.trim())){
					   $scope.reference.act = string;				   
				   }else if(string.includes('clan')){
					   $scope.reference.member = string;					   
				   }else if(string.includes('stav')){
					   $scope.reference.paragraph = string;					   
				   }else if(string.includes('tacka') && !string.includes('podtacka')){
					   $scope.reference.clause = string;					   
				   }else if(string.includes('podtacka')){
					   $scope.reference.subclause = string;					   
				   }else if(string.includes('alineja')){
					   $scope.reference.indent = string;					   
				   }
				   ref = ref.substring(index+1);
			  } else {
				   var string = ref;
				   if(string.trim().length > 0){
					  if(string.includes($scope.selectedRef.act.trim())){
						   $scope.reference.act = string;				   
					   }else if(string.includes('clan')){
						   $scope.reference.member = string;					   
					   }else if(string.includes('stav')){
						   $scope.reference.paragraph = string;					   
					   }else if(string.includes('tacka') && !string.includes('podtacka')){
						   $scope.reference.clause = string;					   
					   }else if(string.includes('podtacka')){
						   $scope.reference.subclause = string;					   
					   }else if(string.includes('alineja')){
						   $scope.reference.indent = string;					   
					   }
				   }
				   break;
			  }
		  }		   		    
	   }
}]);
