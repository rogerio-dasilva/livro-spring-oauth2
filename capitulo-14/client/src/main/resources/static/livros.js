$(function() {
    if  (window.livros === undefined) { window.livros = {}; };

    livros.getTitulos = function(token, callback) {

        $.ajax({
            url: 'http://localhost:8081/api/v2/livros',
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function(data){

                $(data).each(function(index, livro) {

                    var $div = $('<span class="col-sm-12"></span>');
                    $div.text(livro.titulo);
                    callback($div);

                });
            },
            error: function(jqXHR, textStatus, errorThrown)   {
                console.log(textStatus);
            }
        });

    };

});

