package com.example.watertrackerapp

import android.os.Parcel
import android.os.Parcelable

data class RecycleViewData (var email: String = "", var date: String = "", var amount: String = "", var percent: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(date)
        parcel.writeString(amount)
        parcel.writeInt(percent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecycleViewData> {
        override fun createFromParcel(parcel: Parcel): RecycleViewData {
            return RecycleViewData(parcel)
        }

        override fun newArray(size: Int): Array<RecycleViewData?> {
            return arrayOfNulls(size)
        }
    }

}