package com.example.nasamarsrovers.model

import androidx.annotation.StringRes
import com.example.nasamarsrovers.R

enum class SpiritAndOpportunityCameras(@StringRes val resId: Int) {
    FHAZ(R.string.FHAZ),
    RHAZ(R.string.RHAZ),
    NAVCAM(R.string.NAVCAM),
    PANCAM(R.string.PANCAM),
    MINITES(R.string.MINITES),
    ALL(R.string.ALL)
}