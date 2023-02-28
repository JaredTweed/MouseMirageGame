## How this game was made

I made this game with a couple other guys. I almost entirely programmed the menu screens, and programmed a significant portion of most other sections. However, although I programmed the essence of the main character's movement and its interaction with walls, I did not at all program the character animations, and did not create any of the game art. 

## How to run the game

Thank you for checking out Mouse Mirage follow these steps to play the game
1. Install Maven if you do not have it on your computer
2. Clone the repository onto your computer
3. Navigate to the project folder in your computers command prompt
4. Execute: "mvn package" in the Game directory
5. Execute: "java -jar Mouse-Mirage.jar" in project/Game/target 
	a. Alternatively, double click the "Mouse-Mirage.jar" file.
	b. Alternatively, double click the "Mouse-Mirage.exe" file.
Enjoy playing Mouse Mirage

To view JavaDocs
1. Execute: "mvn package" in project/Game directory
2. Execute: "open target/apidocs/allpackages-index.html"
	a. Alternatively, double click the allpackages-index.html file in Game/target/apidocs
	b. Documentation for a specific Class can be opened by choosing the file "SpecificClass.html" in the Game/target/apidocs

To run tests follow these steps:
1. Navigate to the project folder in your computers command prompt
2. Execute: "mvn test"

If the "mvn package" execution cannot find a compiler on your Windows 
computer, ensure that there is a "JAVA_HOME" variable that has a value 
of a path to a JDK (e.g. "C:\Program Files\Java\openjdk-17.0.2") 
in your environment variables on your computer.

How to download Maven on Windows:
Download and extract the binary zip archive link from 
"https://maven.apache.org/download.cgi". Then add the path to the extracted 
Maven bin directory (e.g. "C:\dev\apache-maven-3.8.5\bin") as a value to 
the "Path" variable in your environment variables on your computer.

How to download Maven on Mac OS X:
If you have home brew installed simply Execute: "brew install maven" in Terminal
otherwise Download and extract the binary zip archive link from 
"https://maven.apache.org/download.cgi". place in the directory of your choice
Next locate and edit your .bash_profile in your root directory the file name may be different on your system
To see hidden files Execute: "ls -a"
Update your environment PATH variable by inserting this code into the file
export PATH="Applications/apache-maven-3.8.4/bin:$PATH"
in this example I installed maven in my applications folder but the exact path may be different on your system
If you have trouble finding and automatical updating your environment PATH variable
manually change by Executing: "export PATH="Applications/apache-maven-3.8.4/bin:$PATH"" in terminal
verify with: "mvn --version"
