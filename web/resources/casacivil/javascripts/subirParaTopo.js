$(document).ready(function () {
    
    $("body").append("<a id='subirTopo'> ^ <br/> subir</a>");
    
    $("#subirTopo").hide();

    $('a#subirTopo').click(function () {
        $('body,html').animate({
            scrollTop: 0
        }, 800);
        return false;
    });

    $(window).scroll(function () {
        if ($(this).scrollTop() > 1000) {
            $('#subirTopo').fadeIn();
        } else {
            $('#subirTopo').fadeOut();
        }
    });
    
});