// Book.aidl
package com.lily.androidkfysts.ipc;

// Declare any non-default types here with import statements

// 由于 Book 类型不是 aidl 语言本身定义的基本类型，所以定义的时候，必须要额外的用 parcelable 字段说明
// 且这个类的包名要与 java 文件所在的包名一致，且java 中必须实现 Parcelable 接口
parcelable Book;
