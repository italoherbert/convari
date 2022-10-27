
function Finder() {		
	
	var instance = this;
	
	this.cmd = undefined;
	this.infoElementId = undefined;
	this.errorElementId = undefined;		
	
	this.beforeSend = undefined;
	this.success = undefined;
	this.invoker = undefined;
	
	this.find = function( params ) {		
		var pagesPanelId = undefined;		
		var limit = undefined;
		var responseProcessed = undefined;
		
		var findBTClickedBuild = undefined;
		var backBTClickedBuild = undefined;
		var nextBTClickedBuild = undefined;
		var firstFindBuild = undefined;
		
		if( params ) {
			if( params.pagesPanelId ) {
				pagesPanelId = params.pagesPanelId;
				if( params.limit )
					limit = params.limit;
				if( params.find )
					findBTClickedBuild = params.find;
				if( params.first )
					firstFindBuild = params.first;
				if( params.back )
					backBTClickedBuild = params.back;
				if( params.next )
					nextBTClickedBuild = params.next;					
			}
			if( params.responseProcessed )
				responseProcessed = params.responseProcessed;
		}
				
		var pagesPanel = undefined;
		if( pagesPanelId ) {
			pagesPanel = new PagesPanel();
			pagesPanel.rootElementId = pagesPanelId;
			pagesPanel.pageButtonClicked = function( params ) {
				instance.execute( { 
					buildParameters : function( parameters ) {
						if( typeof( findBTClickedBuild ) == 'function' )
							findBTClickedBuild.call( this, parameters );
						
						if( limit )
							parameters[ 'limit' ] = limit;
						
						var controldate = params.instance.controlDates[ params.pageIndex ];
						if( controldate )
							parameters[ 'controldate' ] = controldate;
					}
				} );
			};
			pagesPanel.backButtonClicked = function( params ) {				
				instance.execute(  {
					buildParameters : function( parameters ) {
						if( typeof( backBTClickedBuild ) == 'function' )
							backBTClickedBuild.call( this, parameters );
												
						var pageIndex = params.instance.currentPageIndex;
						var controldate = params.instance.controlDates[ pageIndex ];
						if( limit )
							parameters[ 'limit' ] = limit;	
						parameters[ 'controldate' ] = controldate;
					},
					afterResponse : function( pars ) {														
						pagesPanel.buildPanel();				
					}
				} ); 				
			};
			pagesPanel.nextButtonClicked = function( params ) {	
				var pageIndex = params.instance.currentPageIndex;
								
				instance.execute(  {
					buildParameters : function( parameters ) {
						if( typeof( nextBTClickedBuild ) == 'function' )
							nextBTClickedBuild.call( this, parameters );						
						
						if( limit )
							parameters[ 'limit' ] = limit;
						parameters[ 'controldate' ] = params.instance.controlDates[ pageIndex ];											
						if( !params.instance.controlDates[ pageIndex+1 ] )							
							parameters[ 'pagescount' ] = params.instance.maximunPagesCountByPanel;						
					},
					afterResponse : function( pars ) {
						if( !params.instance.controlDates[ pageIndex+1 ] )
							params.instance.processControlDatesResponse( pars.doc );														
						params.instance.buildPanel();								
					}
				} ); 
			};
			
		}							
		
		this.execute( {
			buildParameters : function( parameters ) {
				if( typeof( firstFindBuild ) == 'function' )
					firstFindBuild.call( this, parameters );				
				
				if( limit )
					parameters[ 'limit' ] = limit;
				if( pagesPanel )
					parameters[ 'pagescount' ] = pagesPanel.maximunPagesCountByPanel;
			},
			afterResponse : function( params ) {
				if( pagesPanel ) {
					pagesPanel.processControlDatesResponse( params.doc );														
					pagesPanel.buildPanel();
				}
				if( typeof( responseProcessed ) == 'function' )
					responseProcessed.call( this );
				else if( typeof( responseProcessed ) == 'string' )
					eval( responseProcessed );				
			}
		} );
	};
		
	this.execute = function( ftParams ) {				
		sender.send( {
			cmd : this.cmd,
			parameters : function( params ) {	
				var pars = urlutil.params();
				var parameters = { 'uid' : pars[ 'uid' ] };						
				if( ftParams )
					if( ftParams.buildParameters )
						ftParams.buildParameters.call( this, parameters );	
				return parameters;
			},
			infoId : this.infoElementId,
			errorId : this.errorElementId,
			showProcessingMessage : true,
			beforeSend : this.beforeSend,
			success : function( params ) {				
				var doc = params.doc;
				var responseText = params.responseText;
				
				var findSuccess = params.invoker.findSuccess;
				var findInvoker = params.invoker.findInvoker;
				
				if( typeof( findSuccess ) == 'function' )
					findSuccess.call( this, { responseText : responseText, doc : doc, invoker : findInvoker } );

				if( typeof( params.invoker.afterResponse ) == 'function' )
					params.invoker.afterResponse.call( this, { doc : doc, invoker : params.invoker.afterResponseInvoker } );				
			},
			invoker : {
				source : this,
				findSuccess : this.success,
				findInvoker : this.invoker,
				afterResponse : ( ftParams ? ftParams.afterResponse : undefined ),
				afterResponseInvoker : ( ftParams ? ftParams.afterResponseInvoker : undefined )
			}
		} );
		
	};			
				
}