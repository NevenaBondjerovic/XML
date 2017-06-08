app.controller('amendmentController', ['$scope', '$window', 'userService','actService','amendmentService',
									   'amendmentConstants','sessionService',
							   function($scope, $window, userService, actService, amendmentService, amendmentConstants,sessionService){

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
		sessionService.getActForAmendments().then(
			function(response) {	
				//$scope.actsInProgress = response.data;
		    	$scope.actsInProgress = response.data.currentAct;				
            }			
		);
		actService.getAllActNamesAccepted().then(
			function(response) {		
		    	$scope.allActNamesAccepted = response.data;				
            }			
		);
	
		$scope.clickDelete;
		$scope.clickEdit;
		$scope.proposalList = [];
		$scope.ref = {};
		$scope.content = {};
		$scope.actName = {};
		$scope.proposition = {};
		$scope.amendment = {};
		$scope.description = "";
		$scope.type = {};	
		$scope.state = false;
		$scope.createClan = false;
		$scope.treeView = [];
		$scope.node = {};
		$scope.selektovan = {};
		$scope.references = [];
		$scope.selectedRef = [];
}

	init();
								   
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
	$scope.changeSelected  = function(item){
		
		 if(item.includes("Akt")){
			 $scope.selektovan["act"] = item;
		 }else if(item == 'clan'){
			  $scope.selektovan["member"] = item;
		 }else if(item == 'stav'){
			  $scope.selektovan["paragraph"] = item;
		 }else if(item == 'tacka'){
			  $scope.selektovan["clause"] = item;
		 }else if(item == 'podtacka'){
			  $scope.selektovan["subclause"] = item;
		 }else if(item == 'alineja'){
			  $scope.selektovan["indent"] = item;
		 }
		
		amendmentService.getReferenceElement($scope.selektovan).then(
			function(response) {		
		    	$scope.references = response.data;
            }
			
		);
	 };		
	   
	$scope.changeSelection = function(){		
		amendmentService.getMembersByAct($scope.selectedAct.trim()).then(
			function(response){				
				$scope.filteredMembers = response.data;
				$scope.newClan = $scope.filteredMembers.length+1;
			}
  		);  
	};	
	
	$scope.getClanovi = function(clan){  
		for(var clan in $scope.filteredMembers){
			if($scope.filteredMembers[clan].name == $scope.selectedClan.trim()){
					amendmentService.getMember($scope.selectedAct.trim(),$scope.filteredMembers[clan].id).then(
						function(response){	
							console.log(response.data);
							if(!$scope.treeView[$scope.selectedClan.trim()]){
								var responseTree = response.data;
								$scope.disable = {};
								$scope.disableEditing(responseTree);
								$scope.tree = responseTree;
								$scope.treeView[$scope.selectedClan.trim()] = $scope.tree;	
							}else if($scope.treeView[$scope.selectedClan.trim()]){
								$scope.tree = $scope.treeView[$scope.selectedClan.trim()];
							}
						}
					);
			}
		}
		$scope.selected = [];
			
	};	
    $scope.disableEditing = function(tree){
		for(var item in tree){
			$scope.disable[tree[item].name] = true;
			tree[item]["check"] = true; 
		
			if(tree[item].content && tree[item].content.length !== 0){
				$scope.disableEditing(tree[item].content);
			}
		}
	};
	$scope.addProposition = function() {
		$scope.resultContent = [];
		$scope.isValid = true;		
		
		if($scope.state == 'update'){
			if($scope.proposalList && $scope.proposalList.length !== 0 && $scope.selected && $scope.selected.length !== 0){
				for(var item in $scope.proposalList){
					if($scope.proposalList[item].ref == $scope.selected.ref){
						if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
							$scope.proposalList[item].content = $scope.parseContentToObjects($scope.selectedContent);
							alert("Promenjen sadržaj "+$scope.selected.name);
						}
						else{
							alert("Unesite sadržaj.");
						}
					//	alert("Već postoji predlog za "+$scope.proposalList[item].name);
					//	$scope.isValid = false;
						return;
					}
				}
				if($scope.selected && $scope.selected.length !== 0 && $scope.isValid){

					var str1 = $scope.selectedContent.split('&nbsp;').join(' ');
					if($scope.text && $scope.text.length){
						var str2 = $scope.text.split('&nbsp;').join(' ');
							if (str1.trim().length !== str2.trim().length){
								if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
									$scope.proposition = {};

									$scope.proposition.ref = $scope.selected.ref;								
									$scope.parseContentToObjects($scope.selectedContent);	
									$scope.proposition.content = $scope.resultContent;
									$scope.proposalList.push($scope.proposition);
									alert("Uspešno kreiran predlog.");
									$scope.selected = [];
								}else{
									alert("Unesite sadrzaj");
								}
							}
					}else if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
							$scope.proposition = {};

							$scope.proposition.ref = $scope.selected.ref;								
							$scope.parseContentToObjects($scope.selectedContent);	
							$scope.proposition.content = $scope.resultContent;
							$scope.proposalList.push($scope.proposition);
							alert("Uspešno kreiran predlog.");
							$scope.selected = [];
					}else{
						alert("Unesite sadrzaj");
					}

				} 
			}else if($scope.selected && $scope.selected.length !== 0){
					
				var str1 = $scope.selectedContent.split('&nbsp;').join(' ');
				if($scope.text && $scope.text.length){
					var str2 = $scope.text.split('&nbsp;').join(' ');
					if (str1.trim().length !== str2.trim().length){
						if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
							$scope.proposition = {};
							$scope.proposition.ref = $scope.selected.ref;
							$scope.parseContentToObjects($scope.selectedContent);	
							$scope.proposition.content = $scope.resultContent;
							$scope.proposalList.push($scope.proposition);
							alert("Uspešno kreiran predlog.");
							$scope.selected = [];
						}	else{
							alert("Unesite sadržaj.");
						}
						
					}
				}				
				else if($scope.selected && $scope.selected.length !== 0){
						if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
							$scope.proposition = {};
							$scope.proposition.ref = $scope.selected.ref;
							$scope.parseContentToObjects($scope.selectedContent);	
							$scope.proposition.content = $scope.resultContent;
							$scope.proposalList.push($scope.proposition);
							alert("Uspešno kreiran predlog.");
							$scope.selected = [];
						}
					}else{
						alert("Nema izmene sadržaja.");
					}

			}else{
				alert("Nema predloga za dodavanje.");
			}
		
		}else if($scope.state == 'append'){
			if($scope.createClan){
				var have = false;
				
				$scope.clanRef = {};
				$scope.clanRef['akt'] =  $scope.selectedAct.trim();
				$scope.clanRef['member'] =  $scope.member.name.trim();
				
				$scope.validateNewClan($scope.tree1);
				if($scope.isValid){
					//$scope.findPropositions($scope.tree1);
					if($scope.proposalList && $scope.proposalList.length > 0){
						 for(var item1 in $scope.proposalList){
							  if($scope.proposalList[item1].ref === $scope.clanRef){
								  alert("Već postoji predlog.");
								  have = true;
							  }
						 }
					}
					if(!have){
						
						for(var item in $scope.tree1){
							if($scope.tree1[item].type == 'member'){
								if($scope.member.name.trim().length > 0){
									$scope.tree1[item].name =  $scope.member.name.trim();
									
											$scope.proposition = {};
											$scope.proposition.ref = $scope.clanRef;				
											$scope.proposition.node = $scope.tree1[item];

											$scope.proposalList.push($scope.proposition);
											$scope.selected = [];
											alert("Uspešno kreiran predlog.");
											$scope.newClan = $scope.newClan+1;
											$scope.returnFromAddingClan();
											$scope.member = {};
											$scope.member.name = "";
									console.log($scope.proposition);
									
								}else{
									alert("Unesite ime clana.");
								}
							}
						}
				
					}
				}
			}else if(!$scope.createClan){
				var proposalListLength = $scope.proposalList.length;
				$scope.validateNewClan($scope.tree);
				if($scope.isValid){	
					$scope.findPropositions($scope.tree);
				}
			}
		}
	};
 
   $scope.findPropositions = function(tree){
	   $scope.haveChild = false;
	  
	   for(var item in tree){
		   var modified = false;
		   if(($scope.disable && !$scope.disable[tree[item].name]) || $scope.createClan){
			  if($scope.proposalList.length){
				  for(var item1 in $scope.proposalList){
					  if($scope.proposalList[item1].ref === tree[item].ref){
						  if(tree[item].content.length !== 0 && $scope.proposalList[item1].node !== tree[item].content){  
								$scope.proposalList[item1].node = tree[item].content;	
							  	alert("Dodati podelementi.");
							  	modified = true;
						  }else if(tree[item].text.length !== 0 && $scope.proposalList[item1].content !== tree[item].text){
							  $scope.proposalList[item1].content = tree[item].text;	
							  modified = true;
						  }
						  else{
							modified = true;
							alert("Već postoji predlog za "+tree[item].name);
						  }
					  }else{
						  
					  }
				  }
				  if(!modified){
					    
				    $scope.proposition = {};

					$scope.proposition.ref = tree[item].ref;	
					$scope.parseContentToObjects(tree[item].text);
					$scope.proposition.content = $scope.resultContent;	
					if(tree[item].content.length !== 0){
						$scope.proposition.node = tree[item].content;
						$scope.haveChild = true;
					}
					$scope.proposalList.push($scope.proposition);
					alert("Uspešno kreiran predlog.");
					if($scope.createClan){
						$scope.newClan = $scope.newClan+1;
				    }
					$scope.selected = [];
				  }

			   }else{
				   
				    $scope.proposition = {};

					$scope.proposition.ref = tree[item].ref;	
				    $scope.parseContentToObjects(tree[item].text);
					$scope.proposition.content = $scope.resultContent;	
					if(tree[item].content.length !== 0){
						$scope.proposition.node = tree[item].content;
						$scope.haveChild = true;
					}
					$scope.proposalList.push($scope.proposition);
					alert("Uspešno kreiran predlog.");
				   	if($scope.createClan){
						$scope.newClan = $scope.newClan+1;
					}
			   }
		   }
		   if(($scope.disable && $scope.disable[tree[item].name] || $scope.createClan) && tree[item].content.length !==0){
			    $scope.findPropositions(tree[item].content);
		   }
           //else if(tree[item].content && !$scope.haveChild){
		//	   $scope.findPropositions(tree[item].content);
		  // }
	   }
   };
   $scope.validateNewClan = function(tree1){
	  // $scope.isValid = true;
	   
		for(var item in tree1){			
			if(tree1[item].text && tree1[item].text === undefined || tree1[item].text === null || tree1[item].text === ""){
				alert("Unesite sadržaj za "+ tree1[item].name);		
				$scope.isValid = false;
				return;
			}
			if(tree1[item].content && tree1[item].content.length !== 0){	
				$scope.validateNewClan(tree1[item].content);
			}		
		}
	};
	$scope.chooseState = function() {
			$scope.state = false;
			$scope.selectedClan = [];
			$scope.proposalList = [];
			$scope.selected = [];
			$scope.treeView = [];
	};							   
	$scope.addState = function() {
			$scope.state = amendmentConstants.operations.APPEND;
			$scope.selected = [];
	};
    $scope.editState = function() {
			$scope.state = amendmentConstants.operations.UPDATE;
	};
    $scope.deleteState = function() {
			$scope.state = amendmentConstants.operations.DELETE;
	};
    $scope.remove = function (data){
		if($scope.createClan){
			$scope.deleteElement(data,$scope.tree1);
		}else if(!$scope.createClan){
			$scope.deleteElement(data,$scope.tree);
		}
		$scope.selected = [];
		$scope.selectedContent = [];
		
	};
	$scope.deleteElement = function(data, tree1){
		for (var item in tree1){			
			if(tree1[item].name == data.name){				
				tree1.splice(item,1);	
			}
			else{				
				if(tree1[item].content && tree1[item].content.length !== 0){					
					$scope.deleteElement(data, tree1[item].content);
				}
			}
		}
	};
	$scope.addSubelement = function(data) {
		
		var name = "";
		var type = "";
		
		var post = data.content.length + 1;
		
		if(data.type=='member'){
			name  = "Stav";
			type = "paragraph";			
		}else if(data.type=='paragraph'){
			name = "Tacka";
			type = "clause";
		}
		else if(data.type=='clause'){
			name = "Podtacka";
			type = "subclause";
		}
		else if(data.type=='subclause'){
			name = "Alineja";
			type = "indent";
		}			
		
		var id = post;
		var parentId = data.id;
		var newName = name + '-' + parentId + post;
		
		var newRef = {};
		for(var k in data.ref){
			newRef[k]=data.ref[k];
		}
		newRef[type] = newName;
		
		data.content.push({id: post,parentId: parentId, name: newName, type: type, ref: newRef, text: "", content: []});	
		//console.log(data.content);
	};
								   
    $scope.addClan = function(){
		$scope.member = {};
		$scope.member.name = "";
	//	$scope.member.name = "Clan";
		$scope.selected = [];
		$scope.selectedContent = [];
		$scope.tree1 = [{
			"id" : $scope.newClan,
			"parentId": "",
			"name" : "clan",
			"type" : "member",
			"text" : "",
			"ref" : {
					clan : "clan"+$scope.newClan
			},
			"content" : []
		}];
		
		$scope.createClan = true;
	};
    $scope.addContent = function(){
		if (CKEDITOR.instances.ckeditor.checkDirty()){
			if($scope.createClan){
				$scope.AddContentOfElementTree1($scope.tree1);
			}else if(!$scope.createClan){
				$scope.AddContentOfElementTree1($scope.tree);
			}
				
		}else{
			alert("Nema sadržaja za ubacivanje.");
		}
		$scope.selected = [];
		$scope.selectedContent = [];
	};
	$scope.AddContentOfElementTree1 = function(tree1){
			for(var item in tree1){
				if(tree1[item].name == $scope.selected.name){	
					if($scope.selectedContent && $scope.selectedContent.split('&nbsp;').join(' ').trim().length > 0){
						$scope.parseContentToObjects($scope.selectedContent);
						tree1[item].text = $scope.selectedContent;
						alert("Ubačen sadržaj.");
					}else{
						alert("Nema sadržaja za ubacivanje.");
					}
				}
				else{
					if(tree1[item].content && tree1[item].content.length > 0){
						$scope.AddContentOfElementTree1(tree1[item].content);
					}
				}
			}
	};
	$scope.findElementTree1 = function(tree1,data){
	
		for(var item in tree1){
			if(tree1[item].name == data.name){
				$scope.selected = data;
				$scope.selectedContent = tree1[item].text;		    
			}
			else{
				if(tree1[item].content.length !== 0){
					$scope.findElementTree1(tree1[item].content, data);
				}
			}
		}
	};
    $scope.returnFromAddingClan = function(){
		$scope.selected = [];
		$scope.createClan = false;	
	};
	 $scope.clickedElement = function(data, event) {
		 $scope.selectedContent = [];
		 $scope.selected = [];
		 var added = false;
		 
				 
		 if($scope.state == 'update'){
			 if($scope.proposalList && $scope.proposalList.length !== 0){
				 for(var item in $scope.proposalList){
					 if($scope.proposalList[item].ref == data.ref){
						 $scope.parseContent($scope.proposalList[item].content);
						 $scope.selected = data;
						 //alert("Već postoji predlog za "+ data.name);
						 added = true;
						 return;
					 }
				 }
				 if(!added){
					 var selectedElementId = event.currentTarget.id;
					 $scope.selectedElementId = selectedElementId;
					 $scope.selected = data;

					if (data.text === undefined || data.text === null) {
						 $scope.selectedContent = [];
					 }else{
						 $scope.parseContent(data.text);
					 }
				 }
		 }else{
				 var selectedElementId = event.currentTarget.id;
				 $scope.selectedElementId = selectedElementId;
				 $scope.selected = data;

				if (data.text === undefined || data.text === null) {
					 $scope.selectedContent = [];
				 }else{
					 $scope.parseContent(data.text);
				 }
		 	  }
		 }
			
		 else if($scope.state == 'delete'){
			 var added = false;
			  for(var item in $scope.proposalList){
				 if($scope.proposalList[item].ref == data.ref){
					//data.added = true;
					 added = true;
					alert("Već postoji predlog.");
					//return;
				 }
			 }
			if(!added) {
				$scope.proposition = {};

				$scope.proposition.ref = data.ref;	
			//	if(data.content && data.content.length){
					//$scope.proposition.node = data.content;
				//}
				$scope.proposalList.push($scope.proposition);
				
				//if(data.content && data.content.length > 0){ 
				//	$scope.changeChildState(data.content);
				//}
				//data.added = true;
			}
			
		 }else if($scope.state == 'append' && $scope.createClan){
			  $scope.findElementTree1($scope.tree1, data);			  
		 }else if($scope.state == 'append' && !$scope.createClan){
			 if(!data.check){ //!$scope.disable[data.name]
				 $scope.selected = data;
				 $scope.parseContent(data.text);
			 }else{
				 alert("Moguće menjati sadržaj samo dodatim elementima.");
			 }	
		 }
	 };
	$scope.removeProposition = function(data, index){
		$scope.proposalList.splice(index, 1);
		if($scope.state == 'delete'){
			$scope.find(data);
			//data.added = false;				
			//if(data.node){
			//	$scope.changeChildStateRemove(data.node);
			//}
		}else if($scope.createClan){
			$scope.newClan = $scope.newClan-1; 
		}
	}
	$scope.find = function(data){
		for(var item in $scope.tree){
			if($scope.tree[item].ref == data.ref){
				//data.added = false;
			//	$scope.changeChildStateRemove(data.content);
			}
		}
	}
	 $scope.changeChildState = function(data){
		var added;
		for (var item in data) {
			for(var item1 in $scope.proposalList){
				if($scope.proposalList[item1].ref == data[item].ref){
					$scope.proposalList.splice(item1,1);
					data[item].added = true;
				}else{
					data[item].added = true;
				}
			}
			if(data[item].content != null){
				$scope.changeChildState(data[item].content);
			}
		 }
	 };
	$scope.changeChildStateRemove = function(data){	
	   	for(var item in data){
			data[item].added = false;
			if(data[item].content){
				$scope.changeChildStateRemove(data[item].content);
			}
		}
		
	 };
		$scope.parseContent  = function(sadrzaj) {
			var parsedText = "";
		    var selectedContent = [];
			var result;
			
			for(var element in sadrzaj){
		
				if(typeof(sadrzaj[element]) === 'string'){
					selectedContent.push(sadrzaj[element]);
				}
				else if(typeof(sadrzaj[element]) === 'object'){
					if(sadrzaj[element].act && sadrzaj[element].content){
						var str = sadrzaj[element].content;
						if(sadrzaj[element].act){
							if(sadrzaj[element].member){
								if(sadrzaj[element].paragraph){
									if(sadrzaj[element].clause){
										if(sadrzaj[element].subclause){
											if(sadrzaj[element].indent){
													result = str.link(sadrzaj[element].act+"^"+sadrzaj[element].member+"^"+
														 sadrzaj[element].paragraph+"^"+ sadrzaj[element].clause+"^"+
																  sadrzaj[element].subclause+"^"+sadrzaj[element].subclause);
											}else{
												result = str.link(sadrzaj[element].act+"^"+sadrzaj[element].member+"^"+
														 sadrzaj[element].paragraph+"^"+ sadrzaj[element].clause+"^"+
																  sadrzaj[element].subclause);
											}
										}else{
												result = str.link(sadrzaj[element].act+"^"+sadrzaj[element].member+"^"+
														 sadrzaj[element].paragraph+"^"+ sadrzaj[element].clause);
										}
									}else{
										result = str.link(sadrzaj[element].act+"^"+sadrzaj[element].member+"^"+
														 sadrzaj[element].paragraph);
									}
								}
								else{
									result = str.link(sadrzaj[element].act+"^"+sadrzaj[element].member);
								}
							}else{
								result = str.link(sadrzaj[element].act);
							}
						}
						
						var links = [];
						links.push(result);
						$scope.links = links;						
						selectedContent.push(result);
					}
				}
			}
			var temp = selectedContent[0];
			for(var i = 1; i < selectedContent.length; i++){
				temp += selectedContent[i];
			}
			$scope.selectedContent = temp;
			$scope.text = $scope.selectedContent;
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
	   }
	   $scope.createRef = function(ref){
		
		   var find = false;
		   var end = false;
		   k = 0;
		   
		  while(ref.length > 0) {
			  var index = ref.indexOf("^");
			  if(index > -1) {
				   var string = ref.substring(0, index);
				  if($scope.selectedRef && $scope.selectedRef.act && string.includes($scope.selectedRef.act.trim())){
					   $scope.reference.act = string;	
				  }else if(string.includes('akt') || string.includes('zakon')){
					  $scope.reference.act = string;	
				  }				  
				  else if(string.includes('clan')){
					   $scope.reference.member = string;					   
				   }else if(string.includes('stav')){
					   $scope.reference.paragraph = string;					   
				   }else if(string.includes('tacka')){
					   $scope.reference.clause = string;					   
				   }else if(string.includes('podtacka')){
					   $scope.reference.subclause = string;					   
				   }else if(string.includes('alineja')){
					   $scope.reference.subclause = string;					   
				   }
				   ref = ref.substring(index+1);
			  } else {
				   var string = ref;
				   if(string.trim().length > 0){
					 if($scope.selectedRef && $scope.selectedRef.act && string.includes($scope.selectedRef.act.trim())){
						   $scope.reference.act = string;	
					  }else if(string.includes('akt') || string.includes('zakon')){
						  $scope.reference.act = string;	
					  }		
					   else if(string.includes('clan')){
						   $scope.reference.member = string;					   
					   }else if(string.includes('stav')){
						   $scope.reference.paragraph = string;					   
					   }else if(string.includes('tacka')){
						   $scope.reference.clause = string;					   
					   }else if(string.includes('podtacka')){
						   $scope.reference.subclause = string;					   
					   }else if(string.includes('alineja')){
						   $scope.reference.subclause = string;					   
					   }
				   }
				   break;
			  }
		  }	
		   console.log($scope.reference);
	   }
		$scope.quit = function(){
			$scope.amandman = false;
		}
	
		$scope.createAmendment = function(){
			$scope.amandman = true;
			$scope.selected = [];
			$scope.selectedContent = [];
			$scope.propositionText = [];
		}
		$scope.addAmendment = function(){	
			
			if(typeof $scope.propositionText === 'undefined' || !$scope.propositionText){
				alert("Morate uneti obrazloženje!");
			}else{
				$scope.parseContentToObjects($scope.propositionText);
				$scope.amendment.description = $scope.resultContent;
				$scope.amendment.proposalList = $scope.proposalList;
				$scope.amendment.user = $scope.$parent.user.email;
				$scope.amendment.actName = $scope.selectedAct.trim();
				if($scope.state === 'append'){
					$scope.amendment.type = "Dodavanje";
				}
				if($scope.state === 'update'){
					$scope.amendment.type = "Izmena";
				}
				if($scope.state === 'delete'){
					$scope.amendment.type = "Brisanje";
				}
				$scope.amendment.status = "U_procesu";
				$scope.amendment.name = "Amandman";
				console.log($scope.amendment);
				amendmentService.addAmendment($scope.amendment).then(
					function(response) {		
		    			alert("Uspešno dodat amandman.");
						$scope.state = undefined;
						$scope.amandman = undefined;
						$scope.proposalList = [];
						$scope.selectedClan = [];
						$scope.treeView = [];
						$scope.selectedAct = undefined;
						$scope.propositionText = undefined;
            		}, 
				    function(response) {
						alert("Greška!");
				    }
			
    			);
				
			}
		};
}]);