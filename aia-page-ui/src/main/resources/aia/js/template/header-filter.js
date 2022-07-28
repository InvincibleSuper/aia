export default filterHeader




var filterHeaders = {
    "Content-Type":function (contentType){
        if (contentType == 'no-support'){
            alert('不支持的Content-type，无法调用')
        }
    }
}


function filterHeader(headerName,headerValue){
    for (let excludeHeader of Object.keys(filterHeaders)) {
        if (headerName.toLowerCase() == excludeHeader.toLowerCase()){
            filterHeaders[excludeHeader](headerValue);
            return true;
        }
    }
    return false;
}