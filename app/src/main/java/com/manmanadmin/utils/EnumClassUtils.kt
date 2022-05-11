package com.manmanadmin.utils

import com.google.android.gms.maps.model.LatLng

enum class CITIES(val latLng: LatLng){
    Jinotepe(LatLng(11.849599178079286, -86.19761457080827)),
    Dolores(LatLng(11.85554896724195, -86.2177420013539)),
    Diriamba(LatLng(11.858748440740365, -86.24079830097646)),
    San_Marcos(LatLng(11.909254310276708, -86.20480209674466)),
    La_Paz(LatLng(11.823124867234226, -86.12820114552922)),
    El_Rosario(LatLng(11.832311808050468, -86.16674779903566)),
    Santa_Teresa(LatLng(11.803500999811195, -86.16286405862859)),
    Guisquiliapa(LatLng(11.821852649471236, -86.18162863080084))
}
enum class STATUS(){
    Received,Progress,Finished, Canceled, Unknown
}
enum class TYPES(){
    Buy,Ship,Ride,Other
}

enum class LOCALITIES{
    Jinotepe,
    Diriamba,
    Dolores,
    SanMarcos,
    LaPazdeCarazo,
    ElRosario,
    SantaTeresa,
    Guisquiliapa
}



enum class GeneralStatus{
    loading, success, error
}


