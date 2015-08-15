package apfelin.qrbeer;

public class User {

    public int beers, age;
    public String QRCode;

    public User (int age) {

        if (age >= 18) {

            beers = 3;
        }
        else {

            beers = 0;
        }
    }

    /*private User(Parcel in) {

        firstName = in.readString();
        lastName = in.readString();
        beers = in.readInt();
        age = in.readInt();
        imageUri = in.readString();
        QRCodeGen = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(firstName);
        out.writeString(lastName);
        out.writeInt(beers);
        out.writeInt(age);
        out.writeString(imageUri);
        out.writeString(QRCodeGen);
    }

    public int describeContents() {

        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        public User createFromParcel(Parcel in) {

            return new User(in);
        }

        public User[] newArray(int size) {

            return new User[size];
        }
    };*/
}
