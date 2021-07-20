package com.demo.android.cassianass.focustimer.util

import com.demo.android.cassianass.focustimer.R
import com.google.android.material.chip.ChipGroup

object ExtensionFunctions {

    fun ChipGroup.getSelectedTime(): Array<Long>{
        return when(this.checkedChipId){
            R.id.time25_chip-> arrayOf(1500000, 300000)
            R.id.time30_chip -> arrayOf(1800000, 300000)
            R.id.time40_chip -> arrayOf(2400000, 600000)
            else -> arrayOf(3000000, 900000)
        }
    }

    fun ChipGroup.getSelectedSession(): Int{
        return when(this.checkedChipId){
            R.id.session1_chip-> 1
            R.id.session2_chip -> 2
            R.id.session3_chip -> 3
            else -> 4
        }
    }

}