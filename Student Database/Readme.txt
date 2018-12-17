1.How to Compile it ?
	
	javac Input.java
	javac Import.java
	javac CheckStudent.java
	javac CheckCourse.java

2.How to run it ?
	
	java Input <student name> <course name> <marks>
	(For each of the three parameters,no space is allowed)
	
	java Import <file name>
	(Only one file name is allowed for one time,space is allowed)
	
	java CheckStudent <student name 1> <student name 2> <student name 3> <...> ..
	(support search for multiple students,but the name of student can't include space)	

	java CheckCourse <course name 1> <course name 2> <course name 3> <...>...
	(support search for multiple courses,but the name of course can't include space)

3. Others.
	A. The default name of the database is "mydatabase.CSV" . As the given 4 interfaces we need
	in this project don't have a parameter for the name of database, we don't foucs on the name of it.
	But we do have provided an interface to change the default database name in the class "Base".
	In this project we won't use it.
	
	B. When you import a .CSV file into the database, you must make sure that your .CSV file can't have the same name to the
	default database's. And when you use the Import command to import your .CSV file, the suffix ".CSV" is not needed.
	
	For example,you have a .CSV file "Myfile.CSV", you can use 
			java Import Myfile 
		to import it into our database.

	Last but not least, the .CSV must be upper-case.

	
	C. It dose matter whether the letter is upper-case or lower-case,so you must notice
	the case-problem.
	
	D. The format of .CSV file is not so strict. Redunt space and newline is allowed.
	But the lack of comma won't be tolerent.

	E. The main structure of the Database is packed into the package DataBase.
	The part in the default pakage is mainly using the interface we offer in the DataBase package.

	