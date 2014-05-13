Hrisey [![Build Status](https://travis-ci.org/mg6maciej/hrisey.svg?branch=master)](https://travis-ci.org/mg6maciej/hrisey)
======

Boilerplate code suppressor tool for Android platform based on [Project Lombok](http://projectlombok.org/).

Usage
-----

Add Hrisey as a dependency:

```Groovy
dependencies {
    provided 'pl.mg6.hrisey:hrisey:0.1.1'
}
```

Install [Lombok Plugin](http://plugins.jetbrains.com/plugin/6317) in your IntelliJ Idea or Android Studio. (TODO: release Hrisey Plugin)

And then just start using it:

```Java
@hrisey.Parcelable
class ParcelableClass implements android.os.Parcelable {

    String myString;
}
```

This will generate

```Java
class ParcelableClass implements android.os.Parcelable {

    public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();

    String myString;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.myString);
    }

    protected ParcelableClass(android.os.Parcel source) {
        this.myString = source.readString();
    }

    private static class CreatorImpl implements android.os.Parcelable.Creator<ParcelableClass> {

        public ParcelableClass createFromParcel(android.os.Parcel source) {
            return new ParcelableClass(source);
        }

        public ParcelableClass[] newArray(int size) {
            return new ParcelableClass[size];
        }
    }
}
```

during preprocessing phase. You will never see this code again!

Why is Hrisey better?
---------------------

Because it "adds" code to your classes. Other tools use JSR 269 to generate new classes, which leads to leaking abstraction in your code. Hrisey is transparent and can be technically removed from your project in a matter of minutes. But when you start using it, you will never think of doing so.

Why is Hrisey worse?
--------------------

Because it "adds" code to your classes. You have to be careful not to put there too much logic yourself, because debugging might be more complicated.

About Lombok
============

Project Lombok makes java a spicier language by adding 'handlers' that know how to build and compile simple, boilerplate-free, not-quite-java code.
See LICENSE for the Project Lombok license.


To start, run:

```
ant -projecthelp
```

HINT: If you'd like to develop lombok in eclipse, run `ant eclipse` first. It creates the necessary project infrastructure and downloads dependencies. Note that, in order to run "LombokizedEclipse.launch", you need to have "Eclipse SDK" installed.

For a list of all authors, see the AUTHORS file.

Project Lombok was started by:

Reinier Zwitserloot  
twitter: @surial  
home: http://zwitserloot.com/

Roel Spilker  
twitter: @rspilker
