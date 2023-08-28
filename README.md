## How this game was made

I made this game with a couple other guys. I almost entirely programmed the menu screens, and programmed a significant portion of most other sections. However, although I programmed the essence of the main character's movement and its interaction with walls, I did not at all program the character animations, and did not create any of the game art. 

## Mouse Mirage Screenshots

![image](https://github.com/JaredTweed/MouseMirageGame/assets/59375645/75321ebf-e306-47e6-a713-1b765736532d)
![image](https://github.com/JaredTweed/MouseMirageGame/assets/59375645/6a0eae48-9fb7-42a6-87aa-fd86276f2366)
![image](https://github.com/JaredTweed/MouseMirageGame/assets/59375645/1739b087-d5ed-4859-b5a1-c1522a1f939c)
![image](https://github.com/JaredTweed/MouseMirageGame/assets/59375645/6b654177-44ac-426f-9936-70582f87eff8)
![image](https://github.com/JaredTweed/MouseMirageGame/assets/59375645/494a99a2-d1bb-44ea-a71b-e4877a23ae02)





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
