# FoldText_Java
#### 自定义折叠TextView
* 支持文字设置在结尾或者另起一行
* 支持设置点击事件
* 支持设置提示文本内容
###### 效果如下:
![avatar](https://github.com/zzh12138/FoldText_Java/blob/master/ezgif-2-5b8c188b2f.gif)
###### 方法一控件为SpannableFoldTextView
Step 1
```
 <com.example.zzh.foldtext.SpannableFoldTextView
                android:id="@+id/text1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="#fff"
                android:textColor="#000"
                app:showMaxLine="4"
                app:showTipAfterExpand="true"
                app:tipClickable="true"
                app:tipColor="@color/colorPrimary"
                app:tipGravity="0" />
```
* showMaxLine 最大行数
* showTipAfterExpand 点击展开后是否显示收起全文提示
* tipClickable 提示是否可以点击
* tipColor 提示文字颜色
* tipGravity 提示文字位置 0-最后一行末尾，1-另起一行
* isSetParentClick 是否为父View设置了点击事件
* foldText 折叠时的提示文字
* expandText 展开时的提示文字

Step 2

setText()

###### 方法二控件为FoldTextView。
Step 1
```
 <com.example.zzh.foldtext.FoldTextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="#fff"
                android:textColor="#000"
                app:showMaxLine="4"
                app:showTipAfterExpand="true"
                app:tipClickable="true"
                app:tipColor="@color/colorPrimary"
                app:tipGravity="0" />
```
* showMaxLine 最大行数
* showTipAfterExpand 点击展开后是否显示收起全文提示
* tipClickable 提示是否可以点击
* tipColor 提示文字颜色
* tipGravity 提示文字位置 0-最后一行末尾，1-另起一行
* foldText 折叠时的提示文字
* expandText 展开时的提示文字

Step 2

setText()

###### [详细请戳这里](https://www.jianshu.com/p/53d47c54177e)
## License
MIT