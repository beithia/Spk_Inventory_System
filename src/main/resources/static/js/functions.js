$(document).ready(function() {
    $('#fetchProdBtn').click(function(){
        if($('#custSelect').val() == 0) {
            $('#validateCustList').show();
            return false;
        }
    });
    $(function() {
        $('.datepicker').datepicker();
    });
});

