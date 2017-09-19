/*$(function () {
 $('#datetimepicker1').datetimepicker();
 });*/

/*
 $(document).on("focusin","input[type=text]", function () {
 $(this).datepicker({
 autoclose: true,
 format: 'yyyy-mm-dd'
 });
 });*/
$(document).on("focusin", "input[type=text]", function () {
    $('#datetimepicker1').datetimepicker();
});