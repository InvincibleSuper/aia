

export {
    storeAiaData,
    gainAiaData,
    storeChooseInfo,
    gainChooseInfo,
    gainAiaDataByType
}


function storeChooseInfo (chooseInfo){
    window.localStorage.setItem("chooseInfo",JSON.stringify(chooseInfo))
}

function gainChooseInfo (){
    return JSON.parse(window.localStorage.getItem("chooseInfo"))
}


function storeAiaData(data){
    window.localStorage.setItem("aiaData",JSON.stringify(data))
}

function gainAiaData(){
    return JSON.parse(window.localStorage.getItem("aiaData"))
}

function gainAiaDataByType(type){
    return gainAiaData()[type]
}