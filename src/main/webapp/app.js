var app = angular.module('app', ['ngRoute', 'ui.bootstrap']);

app.config(['$routeProvider', function ($routeProvider) {
	$routeProvider
			.when('/', {
				controller: 'searchController',
				templateUrl: 'partials/searchPartial.html',
			})
	    .when('/login', {
	    	controller: 'loginController',
	    	templateUrl: 'partials/loginPartial.html',
	    })
			.when('/registration',{
	    	controller: 'registrationController',
	    	templateUrl: 'partials/registrationPartial.html'
	    })
			.when('/newAct',{
	    	controller: 'actController',
	    	templateUrl: 'partials/actPartial.html'
	    })
			.when('/newAmendment',{
	    	controller: 'amendmentController',
	    	templateUrl: 'partials/amendmentPartial.html'
	    })
			.when('/proposalWithdrawal',{
	    	controller: 'withdrawalController',
	    	templateUrl: 'partials/withdrawalPartial.html'
	    })
			.when('/session',{
	    	controller: 'sessionController',
	    	templateUrl: 'partials/sessionPartial.html'
	    })
	    .otherwise({
	        redirectTo: '/'
	    });
}]);

app.directive('amendmentMetaData', function() {
	var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/amendmentMetaDataTemplate.html";

    return directive;
});

app.directive('actMetaData', function() {
	var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/actMetaDataTemplate.html";

    return directive;
});

app.directive('amendmentDataTemplate', function() {
		var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/amendmentDataTemplate.html";

    return directive;
});
app.directive('amendmentPropositionList', function() {
		var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/amendmentPropositionList.html";

    return directive;
});
app.directive('addingReference', function() {
		var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/addingReference.html";

    return directive;
});
app.directive('ckeditor', function($timeout) {
    return {
        require : '?ngModel',
        link : function($scope, element, attrs, ngModel) {
            var ckeditor = CKEDITOR.replace(element[0], {
			uiColor: '#9AB8F3',
			resize_enabled : false,
			toolbar :	[
							{ name: 'clipboard', items : [ 'Undo','Redo' ] },
							{ name: 'editing', items : [ 'Find','Replace','-','SelectAll'] },
							{ name: 'links', items : [ 'Link','Unlink'] },
						],
			enterMode: CKEDITOR.ENTER_BR
		});
            if (!ngModel) {
                return;
            }
            ckeditor.on('instanceReady', function() {
                ckeditor.setData(ngModel.$viewValue);
				$('iframe').contents().click(function(e) {		
					if(typeof e.target.href != 'undefined' && e.ctrlKey == true) {			
							window.open(e.target.href, 'new' + e.screenX);
						}
					});
            });
		    ckeditor.on('change', function() {
				 $timeout(function () {
                  $scope.$apply(function () {
                    ngModel.$setViewValue(ckeditor.getData() || '');
                  });
                }, 0);
            });  
			
		 CKEDITOR.on('dialogDefinition', function (ev) {
				 

			// Take the dialog name and its definition from the event data.
			var dialogName = ev.data.name;
			var dialogDefinition = ev.data.definition;

			// Check if the definition is from the dialog we're
			// interested in (the 'link' dialog).
			if (dialogName == 'link') {		
					
					
					dialogDefinition.dialog.resize( 400, 50 );
					dialogDefinition.resizable = CKEDITOR.DIALOG_RESIZE_NONE;
					// Remove the 'Advanced' tabs from the 'Link' dialog.        
					dialogDefinition.removeContents('advanced');
					dialogDefinition.removeContents('anchor');

					dialogDefinition.removeContents('target');
					// Get a reference to the 'Link Info' tab.
					var infoTab = dialogDefinition.getContents('info');

					// Remove unnecessary widgets from the 'Link Info' tab.
				    infoTab.get( 'linkType' ).style = 'display: none';

					var urlField = infoTab.get( 'url' );
					
					//urlField['default'] = $scope.getElements();
					
				    dialogDefinition.onShow = function () {
					    var dialog = CKEDITOR.dialog.getCurrent(); 
						var infoTab = dialogDefinition.getContents('info');
						//var urlField = infoTab.get( 'url' );
						// urlField['default'] = $scope.getElements();
						//dialog.hidePage( 'target' ); // now via config 
						//dialog.hidePage( 'advanced' ); // now via config 
						elem = dialog.getContentElement('info','anchorOptions');     
						elem.getElement().hide(); 
						elem = dialog.getContentElement('info','emailOptions');     
						elem.getElement().hide();    
						this.setValueOf( 'info', 'protocol', '');
						elem = dialog.getContentElement('info','protocol');     
						elem.getElement().hide();
						var elem = dialog.getContentElement('info','linkType');     
						elem.getElement().hide(); 
						elem = dialog.getContentElement('info','url');  
						elem.getElement().setSize('width','400',true);
						if($scope.getElements()!=" "){
						this.setValueOf( 'info', 'url', $scope.getElements());
							elem.disable(); 
							ok = dialog._.buttons['ok'];
							ok.click();
						}else{
							cancel = dialog._.buttons['cancel'];
							cancel.click();
						}		
						
						 
					};
					//infoTab.remove('linkType');
					//infoTab.remove('browse');
				


				}
			});
            ngModel.$render = function(value) {
                ckeditor.setData(ngModel.$viewValue);
            };
        }
    };
});
app.directive('actMetaDataFilter', function() {
		var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/actMetaDataFilterTemplate.html";

    return directive;
});

app.directive('datePicker', function() {
		var directive = {};
    directive.restrict = 'E';
    directive.templateUrl = "templates/datePickerTemplate.html";
		directive.link = function($scope, element, attrs) {
			$scope.isOpened = false;
			$scope.open = function() { $scope.isOpened = true;}
		}
		directive.scope =  {
        model:'=ngModel',	minDate: '=min'
    }

    return directive;
});
