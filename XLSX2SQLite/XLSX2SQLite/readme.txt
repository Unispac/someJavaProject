注:
由于项目依赖的jdbc和poi库加起来20MB，超过了上传限制。因此上传的项目中lib目录下的库文件被清空了。
这意味着，项目不能被直接编译和运行。库文件需要额外的在浙大云盘上下载。
库链接：https://pan.zju.edu.cn/share/096bd82375500d5905d51d86a6
如果不能正常下载，请联系我！



项目的结构:
总共有四个目录;
doc目录下是实验报告文档;
lib目录下是读取excel文件依赖的POI库和连接SQLite数据所依赖的JDBC库;
MANIFEST目录下是打包jar文件的格式;
SQLite dataBase目录用来存放生成的数据库(所有生成的数据库默认会放自动放在这个目录下);


如何编译/运行:
根目录下为源代码"excelReaderForSQLite.java"和可执行jar文件"excelReaderForSQLite.jar";
javac -Djava.ext.dirs=./lib excelReaderForSQLite.java 可正确的编译;
java -Djava.ext.dirs=./lib excelReaderForSQLite ...(命令行参数) 可正确执行;
或者
直接使用 java -jar excelReaderForSQLite.jar ...(命令行参数) 执行jar文件;


关于参数:
命令行参数: dataBaseName excelFileName [sheetName] [tableName];
数据库名可以省略.db后缀,但是excel文件名不可省略后缀;
.xlsx和.xls都被程序所支持;


程序的功能:
程序完成了作业的所有要求,会自动读入指定excel文件的指定页,
在指定的数据库中创建一个指定的表来存放这个excel页;
并且完成了int,real,char三种类型的区分;
一个表示行号的INT类型PK会被自动创建;


程序的输出：
程序会输出创建表时的SQL命令;
程序会输出添加条项进入表中的总耗时;
程序会输出添加进入表中的行数;


特殊声明:
程序不支持创建重复的表;(在试图创建重复的表时,会抛出异常表明这个表之前已经创建过了);
程序也不支持查询表的内容;(这需要一个额外的读表程序,这并不是这次作业所要求的);


Author : 漆翔宇 计算机科学与技术 3170104557
Email : 3170104557@zju.edu.cn
Tel : 17342017090