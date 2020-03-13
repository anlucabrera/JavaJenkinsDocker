$(document).ready(function () {
    let fixHeader = new FixHeader();
});

function FixHeader() {
    this.init();
}

FixHeader.prototype = {
    init: function () {
        this._tableClass = '.fix_header_table';
        this._headerClass = '.cloned_header';
        this._cloneTableElement = '<table class="cloned_header" style="display: none; position: fixed; top: 0; table-layout: fixed;"><colgroup></colgroup></table>';

        this.tableObj = $(this._tableClass);
        this.originalHeaderObj = this.tableObj.find('thead');
        this.cloneHeaderObj = {};

        this.colNum = this.originalHeaderObj.find('tr').children().length;

        this.appendElements();
        this.eventBuild();
    },

    eventBuild: function() {
        let self = this;
        self.switchHeaderWithScroll();
        self.detectWindowResize();
    },

    appendElements: function() {
        let self = this;

        self.tableObj.before(self._cloneTableElement);
        self.cloneHeaderObj = $(self._headerClass);
        self.cloneHeaderObj.append(self.tableObj.find('thead').clone());

        self.adjustHeaderAndTableWidth(self.getOffsetWidth(self.tableObj.get(0)))

        self.tableObj.prepend('<colgroup></colgroup>')
        for ($i = 0; $i < self.colNum; $i++) {
            colWidth = self.getOffsetWidth(self.originalHeaderObj.find('tr').children().get($i));

            self.cloneHeaderObj.find('colgroup').append("<col style='width: " + colWidth + "px'>");
            self.tableObj.find('colgroup').append("<col style='width: " + colWidth + "px'>");
        }
    },

    switchHeaderWithScroll: function() {
        let self = this;
        $(window).scroll(function () {
            if($(window).scrollTop() >  self.tableObj.offset().top
              && $(window).scrollTop() < ( self.tableObj.offset().top + self.tableObj.height() ) ) {
                self.cloneHeaderObj
                    .show()
                    .css('left', -$(window).scrollLeft() + self.tableObj.offset().left);
            } else {
                self.cloneHeaderObj.hide();
            }
        });
    },

    detectWindowResize: function() {
        let self = this;
        let timer = 0;

        $(window).on('resize', function () {
          if (timer > 0) {
            clearTimeout(timer);
          }

          timer = setTimeout(function () {
            self.resizeHeaderAllCol();
          }, 200);
        });
    },

    resizeHeaderOneCol: function() {
        let self = this;
        $(document).on('resizeFixHeaderCol', function (e, colNo) {
            self.tableObj.css({'min-width': ''});
            self.tableObj.find('colgroup').children().eq(colNo).css({'width': ''});

            self.adjustHeaderAndTableWidth(self.getOffsetWidth(self.tableObj.get(0)))
            self.adjustColWidthOriginHeaderByColNo(colNo)
        });
    },

    resizeHeaderAllCol: function() {
        let self = this;

        self.tableObj.css({'min-width': ''});
        self.tableObj.find('colgroup').children().css({'width': ''});

        self.adjustHeaderAndTableWidth(self.getOffsetWidth(self.tableObj.get(0)))
        for ($i = 0; $i < self.colNum; $i++) {
            self.adjustColWidthOriginHeaderByColNo($i)
        }
    },

    getOffsetWidth: function(el) {
        let rect = el.getBoundingClientRect();
        return rect.width || rect.right - rect.left;
    },

    adjustHeaderAndTableWidth: function(fixWidth) {
        let self = this;
        self.cloneHeaderObj.width(fixWidth);
        self.tableObj.css({'min-width': fixWidth});
    },

    adjustColWidthOriginHeaderByColNo: function(colNo) {
        let self = this;
        colWidth = self.getOffsetWidth(self.originalHeaderObj.find('tr').children().get(colNo));

        self.cloneHeaderObj.find('colgroup').children().eq(colNo).width(colWidth);
        self.tableObj.find('colgroup').children().eq(colNo).width(colWidth);
    }
};
