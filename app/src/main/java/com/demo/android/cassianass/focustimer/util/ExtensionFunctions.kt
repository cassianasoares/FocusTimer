package com.demo.android.cassianass.focustimer.util

import com.demo.android.cassianass.focustimer.R
import com.google.android.material.chip.ChipGroup

object ExtensionFunctions {

    fun ChipGroup.getSelectedTime(): Array<Long>{
        return when(this.checkedChipId){
            R.id.time25_chip-> arrayOf(30000, 20000)
            R.id.time30_chip -> arrayOf(60000, 30000)
            R.id.time40_chip -> arrayOf(90000, 60000)
            else -> arrayOf(90000, 900000)
        }
    }

    fun ChipGroup.getSelectedSession(): Int{
        return when(this.checkedChipId){
            R.id.session1_chip-> 1
            R.id.session2_chip -> 3
            R.id.session3_chip -> 4
            else -> 5
        }
    }

}