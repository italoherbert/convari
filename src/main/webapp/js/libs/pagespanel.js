
function PagesPanel() {
		
	this.firstPageIndex = 1;
	
	this.rootElementId = undefined;
	this.pageButtonClicked = undefined;
	this.backButtonClicked = undefined;
	this.nextButtonClicked = undefined;
	
	this.currentPageIndex = 1;
	this.maximunPagesCountByPanel = 8;
	
	this.pagesCount = 0;
	
	this.controlDates = new Array();
	this.hasMoreControlDates = true; 
	
	this.getStartAndEndPageIndexes = function() {
		var panelIndex = parseInt( (this.currentPageIndex-1) / this.maximunPagesCountByPanel );
		
		var startPageIndex = (panelIndex * this.maximunPagesCountByPanel) + 1;
		var endPageIndex;
		if( (this.pagesCount - startPageIndex) >= this.maximunPagesCountByPanel )
			endPageIndex = startPageIndex + this.maximunPagesCountByPanel-1;
		else endPageIndex = this.pagesCount;
		
		return { start : startPageIndex, end : endPageIndex };
	};
	
	this.buildPanel = function() {
		var el = document.getElementById( this.rootElementId );
		if( !el )
			throw "Elemento raiz não encontrado pelo identificador. ID="+this.rootElementId;
		
		var pageIndexes = this.getStartAndEndPageIndexes();
		var startPageIndex = pageIndexes.start;
		var endPageIndex = pageIndexes.end;
		
		var instance = this;
		
		var panelElement = document.createElement( 'span' );
		panelElement.className = 'pagespanel';
		
		var backBTElement = document.createElement( 'span' );
		backBTElement.className = 'pagespanel-button';
		if( startPageIndex == this.firstPageIndex ) {
			elementManager.addClass( backBTElement, 'pagespanel-button-disabled' );
		} else {
			elementManager.addClass( backBTElement, 'pagespanel-button-enabled' );						
			backBTElement.onclick = function() {
				instance.back( instance );
			};
		}
		backBTElement.innerHTML = '&lt;&lt;';
		panelElement.appendChild( backBTElement );
		
		for( var i = startPageIndex; i <= endPageIndex; i++ ) {
			var pageBTElement = document.createElement( 'span' );
			pageBTElement.id = this.rootElementId+"_"+i;
			pageBTElement.name = i;
			pageBTElement.className = 'pagespanel-button pagespanel-button-enabled';
			if( i == instance.currentPageIndex )
				elementManager.addClass( pageBTElement, 'pagespanel-button-selected' );
			pageBTElement.onclick = function() {
				var index = this.name;
				var selectedPageElementId = instance.rootElementId+"_"+instance.currentPageIndex;
				
				elementManager.removeClassById( selectedPageElementId, 'pagespanel-button-selected' );
				instance.currentPageIndex = index;
				elementManager.addClass( this, 'pagespanel-button-selected' );

				if( typeof( instance.pageButtonClicked ) == 'function' )
					instance.pageButtonClicked.call( this, { instance : instance, pageIndex : index } );
				
			};
			pageBTElement.innerHTML = i;
			panelElement.appendChild( pageBTElement );
		}
		
		var nextBTElement = document.createElement( 'span' );
		nextBTElement.className = 'pagespanel-button';
		if( !this.hasMoreControlDates || endPageIndex - startPageIndex < this.maximunPagesCountByPanel-1 ) {
			elementManager.addClass( nextBTElement, 'pagespanel-button-disabled' );
		} else {
			elementManager.addClass( nextBTElement, 'pagespanel-button-enabled' );	
			nextBTElement.onclick = function() {
				instance.next( instance );
			};		
		}
		
		nextBTElement.innerHTML = '&gt;&gt;';
		panelElement.appendChild( nextBTElement );		
				
		el.innerHTML = "";
		el.appendChild( panelElement );	
				
	};
	
	this.back = function() {
		var pageIndexes = this.getStartAndEndPageIndexes();
		var startPageIndex = pageIndexes.start;

		if( startPageIndex > this.firstPageIndex ) {
			this.currentPageIndex = startPageIndex-1;
			
			if( typeof( this.backButtonClicked ) == 'function' )
				this.backButtonClicked.call( this, { instance : this } );						
		}
	};
	
	this.next = function() {
		var pageIndexes = this.getStartAndEndPageIndexes();
		var startPageIndex = pageIndexes.start;
		var endPageIndex = pageIndexes.end;
		
		if( this.hasMoreControlDates && endPageIndex - startPageIndex >= this.maximunPagesCountByPanel-1 ) {
			this.currentPageIndex = endPageIndex+1;
			
			if( typeof( this.nextButtonClicked ) == 'function' )
				this.nextButtonClicked.call( this, { instance : this } );			
		}
	};		
	
	this.processControlDatesResponse = function( doc ) {
		var index = parseInt( this.currentPageIndex+1 );
		
		var body = doc.getElementsByTagName( 'body' )[0];
		var controlDatesList = body.getElementsByTagName( 'controldates' );
		if( controlDatesList.length > 0 ) {
			var controlDates = controlDatesList[0];
			var count = controlDates.getAttribute( 'count' );	
			var hasMore = controlDates.getAttribute( 'hasMore' );
			var dates = controlDates.getElementsByTagName( 'msdate' );
			for( var i = 0; i < dates.length; i++ )
				this.controlDates[ index++ ] = dates[ i ].firstChild.nodeValue;						
			this.pagesCount = this.currentPageIndex + parseInt( count ) - this.firstPageIndex;
			this.hasMoreControlDates = ( hasMore != 'false' ? true : false );
		}
	};
	
}
