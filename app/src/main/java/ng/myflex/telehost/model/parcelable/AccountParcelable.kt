package ng.myflex.telehost.model.parcelable

import android.os.Parcel
import android.os.Parcelable
import ng.myflex.telehost.model.AccountModel

class AccountParcelable() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    constructor(model: AccountModel) : this() {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AccountParcelable> {
        override fun createFromParcel(parcel: Parcel): AccountParcelable {
            return AccountParcelable(parcel)
        }

        override fun newArray(size: Int): Array<AccountParcelable?> {
            return arrayOfNulls(size)
        }
    }
}