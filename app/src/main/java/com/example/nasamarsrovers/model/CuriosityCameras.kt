package com.example.nasamarsrovers.model

import androidx.annotation.StringRes
import com.example.nasamarsrovers.R

enum class CuriosityCameras(@StringRes val resId: Int) {
    FHAZ(R.string.FHAZ),
    RHAZ(R.string.RHAZ),
    MAST(R.string.MAST),
    CHEMCAM(R.string.CHEMCAM),
    MAHLI(R.string.MAHLI),
    MARDI(R.string.MARDI),
    NAVCAM(R.string.NAVCAM),
    ALL(R.string.ALL)
}