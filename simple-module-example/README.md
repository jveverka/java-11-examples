# Simple Java 11 example
This simple example shows modular java 11 gradle project.
Project requires __JDK 10__ or __JDK 11__ and __gradle 4.8.1__ or later.

### Compile & Run with Java 10/11
```gradle clean build installDist distZip```  
```./application/build/install/application/bin/application```

### Run optimized build by jlink
After gradle full build, run ```./jlink.sh``` script.
This scipr will create customized JRE build with application modules.
Go to directory ```application/build/app-runtime-distro/bin```
and start application using ```./launch``` script.
Directory ```application/build/app-runtime-distro``` contains complete
runtime environment.

