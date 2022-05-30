//keycode enter 인지 아닌지
function enter () {
    if (event.keyCode != "13") {
        return true;
    }
    return false;
}

//숫자만 입력하는것
$.phoneNum = function(){
$("#phoneNum").on('input', function () {
    $(this).val($(this).val().replace(/[^0-9|.]/g, '').replace(/(\..*)\./g, '$1'));
})}
