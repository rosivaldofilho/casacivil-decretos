$(document).ready(function () {
    $('.summernote').summernote({
        lang: 'pt-BR',
        fontSizes: ['8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '24', '36', '48', '64'],
        toolbar: [
            // [groupName, [list of button]]
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['strikethrough', 'superscript', 'subscript']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['color', ['color']],
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['link', ['link']],
            ['picture', ['picture']],
            ['table', ['table']],
            ['codeview', ['codeview']],
            ['insert', ['gxcode']],
            ['fullscreen', ['fullscreen']]
        ]
    });
    $('.summernote-ementa').summernote({
        toolbar: [
            // [groupName, [list of button]]
            ['font', ['underline', 'superscript']],
            ['clear', ['clear']],
            ['fontname', ['fontname']]
        ]
    });
});