```bash
$sources = Get-ChildItem .\src\main\java -Recurse -Filter *.java 
| ForEach-Object FullName

```bash
New-Item -ItemType Directory -Force .\out

```bash
javac -d .\out $sources

## Run the Program
```bash
java -cp .\out com.tictactoe.Main