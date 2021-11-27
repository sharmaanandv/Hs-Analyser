# Hs- Analyser

## Purpose
Monitor storage(local, FTP, S3 etc) for any addition of resources(txt, pdf, word etc).
On resource found , read resource ,extract statistical data and move resource to processed folder

##Technology Used
Java 8
Maven
Junit

## How to run
```bash
java -jar hsanalyser-0.0.1-SNAPSHOT.jar "<root-directory>"
e.g. java -jar hsanalyser-0.0.1-SNAPSHOT.jar "D:\\storage\\hs"

```
##Note:
For debugging/running the code, Initialize args[0] with the path to be monitor in Application.main().

## Key Components
Project is designed considering future scope of expansion and huge volume of resources.

MonitorService will choose ResourceScanner based on input received, For now we are supporting only FileResourceScanner.
Moreover, In future other implementations of ResourceScanner can be given in like , FTP, S3 etc.

ResourceScanner will keep on watching for any new file, Also It will process already present files under the root directory.
Based on file extension, ResourceScanner will pick HsFileReader, If extension is .txt then TxtFileReader will be initialized.
Other readers like PdfFileReader are not implemented as of now.

HsFileReader will read file line by line,It won't read the whole file at once considering performance.
All implementations of HsFileReader can use common Analyser to get statistical data from files.

Analyser- Currently Analyser is having methods to get statistical data e.g. countOfSpecialChar(), countNoOfWords().
Many other methods can be added here.

Future scope for Analyser, Analyser can be enhanced to have multiple implementations, where each Implementation will have only
single statistical methods i.e. retrieveData(). HsFileReader will dynamically call retrieveData() methods of all Analyser's implementation.
With this way, If we want to have more statistical methods, then we just need to give one more implementation of it.





