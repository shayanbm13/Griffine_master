package com.example.griffin.mudels

import android.os.Parcel
import android.os.Parcelable

data class YourParcelableModel(val id: Int, val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YourParcelableModel> {
        override fun createFromParcel(parcel: Parcel): YourParcelableModel {
            return YourParcelableModel(parcel)
        }

        override fun newArray(size: Int): Array<YourParcelableModel?> {
            return arrayOfNulls(size)
        }
    }
}
