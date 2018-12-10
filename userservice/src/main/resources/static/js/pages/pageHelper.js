/**
 * 分页器，依赖于bootstrap,jquery
 */
var pager = {
    init : function(r) {
        this.obj = $(".pj_pager");
        this.total = Number(this.obj.attr("pj_total"));
        this.rows = r != undefined ? r : 10;// Number(this.obj.find(.page-count).html())
        this.page = 1;
        this.initpagecount = 10;
        this.pages = parseInt(this.total / pager.rows)
            + (pager.total % pager.rows > 0 ? 1 : 0);
        this.maxpages = this.pages > this.initpagecount ? this.initpagecount
            : this.pages;
        this.outnumber = this.pages > this.initpagecount ? true : false;
        this.start = 1;
        this.end = this.start + (this.maxpages - 1);
        this.html();
    },
    next : function() {
        this.obj.find(".pj_next").click(function() {
            if (pager.pages > pager.page) {
                pager.page++;
                pager.html();
            }
        });
    },
    prov : function() {
        this.obj.find(".pj_prov").click(function() {
            if (pager.page > 1) {
                pager.page--;
                pager.html();
            }
        });

    },
    first : function() {
        this.obj.find(".first").click(function() {
            if (pager.page != 1) {
                pager.page = 1;
                pager.html();
            }
        });
    },
    last : function() {
        this.obj.find(".last").click(function() {
            if (pager.page != pager.pages) {
                pager.page = pager.pages;
                pager.html();
            }
        });
    },
    jump : function() {
        this.obj.find(".jump").click(function() {
            var p = $(this).prev("input").val();
            if (p != '' && Number(p) <= pager.pages) {
                pager.page = Number(p);
                pager.html();
            }
        });
    },
    setOutnumber : function() {
        if (this.pages > this.initpagecount) {
            if (this.end < this.page || this.start > this.page) {
                this.start = parseInt((this.page - 1) / this.initpagecount)
                    * this.initpagecount + 1;
                this.end = this.start + this.initpagecount - 1;
                if (this.pages - this.start < this.initpagecount) {
                    this.outnumber = false;
                    this.end = this.start + pager.total % pager.rows - 1;
                } else {
                    this.outnumber = true;
                }
            }
        }
    },
    selectRows : function() {
        $(".dropdown-menu li").click(
            function() {
                // pager.rows = Number($(this).find("a").html());
                // pager.page = 1;
                pager.init(Number($(this).find("a").html()));
                $(this).parent("ul").prev("button").children("em").html(
                    $(this).find("a").html());
            });
    },
    html : function() {

        this.setOutnumber();

        var html = '';
        html += '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><em class="page-count">'
            + this.rows + '</em> <span class="caret"></span></button>';
        html += '<ul class="dropdown-menu" style="min-width: auto" role="menu">';
        html += '<li><a href="#">10</a></li><li><a href="#">20</a></li><li><a href="#">30</a></li><li><a href="#">40</a></li><li><a href="#">50</a></li><li><a href="#">100</a></li>';
        html += '</ul>';
        html += '<button type="button" class="btn btn-default first">首页</button>';
        html += '<button type="button" class="btn btn-default pj_prov"><<</button>';
        if (this.pages > 0) {
            for (var i = this.start; i <= this.end; i++) {
                var cls = (i == this.page ? 'btn-success' : 'btn-default');
                html += '<button type="button" class="btn ' + cls + '">' + i
                    + '</button>';
            }
            if (this.outnumber) {
                html += '<button type="button" class="btn btn-default">...</button>';
            }
        }
        html += '<button type="button" class="btn btn-default pj_next">>></button>';
        html += '<button type="button" class="btn btn-default last">尾页</button>';
        html += '<input type="text"  style="width: 50px;display:inherit" class="btn form-control" placeholder="页数">';
        html += '<button type="button" class="btn btn-default jump">跳转</button>';
        html += '<span> </span><span>' + this.total
            + '</span><span>条</span>';
        html += '<span> 共</span><span>' + this.pages
            + '</span><span>页</span>';
        this.obj.html(html);
        this.next();
        this.prov();
        this.first();
        this.last();
        this.jump();
        this.selectRows();
    }

}

$(function() {
    pager.init();
})
